package fieldtracker;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.InputStream;

/**
 * Handler for requests to Lambda function.
 */
public class App implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private Logger lgr = Logger.getLogger(App.class.getName());

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "OPTIONS,GET");

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);
        try {
            Yaml yaml = new Yaml(new Constructor(Datasource.class));
            InputStream inputStream = this.getClass()
                    .getResourceAsStream("/application.yml");
            Datasource ds = yaml.load(inputStream);
            String url = ds.getUrl();
            String user = ds.getUsername();
            String password = ds.getPassword();
            String output = "{}";
            StringBuilder sql = new StringBuilder("SELECT jsonb_build_object(");
            sql.append("   'type',     'FeatureCollection', ");
            sql.append("   'features', jsonb_agg(features.feature) ");
            sql.append(" ) as geojson ");
            sql.append(" FROM ( ");
            sql.append("   SELECT jsonb_build_object( ");
            sql.append("    'type',       'Feature', ");
            sql.append("   'fieldId',         field_id,  ");
            //sql.append("   'geometry',   ST_AsGeoJSON(ST_Transform(field_geom, 3857))::jsonb, ");
            sql.append("     'geometry',   ST_AsGeoJSON(field_geom)::jsonb, ");
            sql.append("   'properties', to_jsonb(inputs) - 'field_id' - 'field_geom' ");
            sql.append("   ) AS feature ");
            sql.append(" FROM (SELECT field.field_id, field_geom, field.field_name as fieldName,field_user.first_name|| ' ' ||field_user.last_name as growerName, ");
            sql.append(" (select cast(count(*) as text) from field_manage.field_activity where field_activity.field_id=field.field_id) as activity_count ");
            sql.append(" from field_manage.field ");
            sql.append(" left outer join field_manage.field_grower ");
            sql.append(" on field.field_id = field_grower.field_id  ");
            sql.append(" left outer join field_manage.field_user  ");
            sql.append(" on field_grower.grower_id = field_user.field_user_id ) ");
            sql.append(" inputs) features ");

            lgr.log(Level.INFO, "sql: " + sql);
            try (Connection con = DriverManager.getConnection(url, user, password);
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql.toString());) {
                if (rs.next()) {
                    output = rs.getString("geojson");
                    lgr.log(Level.INFO, "output: " + output);
                }

            } catch (SQLException ex) {
                lgr.log(Level.SEVERE, "SQLException caught: " + ex.getMessage(), ex);
            }

            return response
                    .withStatusCode(200)
                    .withBody(output);
        } catch (Exception e) {
            lgr.log(Level.SEVERE, "Exception caught: " + e.getMessage(), e);
            return response
                    .withBody("{}")
                    .withStatusCode(500);
        }
    }


    private String getPageContents(String address) throws IOException {
        URL url = new URL(address);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
            return br.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}

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

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;

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
        headers.put("Access-Control-Allow-Methods", "OPTIONS,POST");
        String fieldGeoJson = null;
        try {
            String requestString = input.getBody();
            JSONParser parser = new JSONParser();
            JSONObject requestJsonObject = (JSONObject) parser.parse(requestString);
            if (requestJsonObject != null) {
                if (requestJsonObject.get("geometry") != null) {
                    fieldGeoJson = requestJsonObject.get("geometry").toString();
                }
            }
        } catch (ParseException ex) {
            lgr.log(Level.SEVERE, "ParseException caught: " + ex.getMessage(), ex);
        }

        lgr.log(Level.INFO, "fieldGeoJson: " + fieldGeoJson);
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
            StringBuilder sql = new StringBuilder();
            if (fieldGeoJson != null) {
                sql.append("INSERT INTO field_manage.field(field_geom, created_datetz)  ");
                sql.append("  VALUES (ST_Transform(ST_Force2D(ST_GeomFromGeoJSON('");
                sql.append(fieldGeoJson);
                sql.append("')),4326), current_timestamp) ");
//                sql.append("RETURNING field_id ");

                lgr.log(Level.INFO, "sql: " + sql);
                String[] columnNames = {"field_id"};
                try (Connection con = DriverManager.getConnection(url, user, password);
                     Statement st = con.createStatement();
                ) {
                    int rowCount = st.executeUpdate(sql.toString(), columnNames);
                    Map<String, Integer> newFieldMap = new HashMap<String, Integer>();
                    try (ResultSet rs = st.getGeneratedKeys()) {
                        if (rs.next()) {
                            newFieldMap.put("newFieldId", rs.getInt(1));
                        }
                        ObjectMapper mapper = new ObjectMapper();
                        output = mapper.writeValueAsString(newFieldMap);
                        lgr.log(Level.INFO, "output: " + output);
                    } catch (SQLException innerSQLEx) {
                        lgr.log(Level.SEVERE, "SQLException inner try/catch caught: " + innerSQLEx.getMessage(), innerSQLEx);
                    }
                } catch (SQLException ex) {
                    lgr.log(Level.SEVERE, "SQLException caught: " + ex.getMessage(), ex);
                }
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

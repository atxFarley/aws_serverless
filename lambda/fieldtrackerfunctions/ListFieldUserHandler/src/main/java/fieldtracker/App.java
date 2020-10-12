package fieldtracker;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
            StringBuilder sql = new StringBuilder("select ");
            sql.append("  field_user_id, last_name, first_name, app_admin_b, app_login,    ");
            sql.append("  pgp_sym_decrypt(app_encpassword::bytea, 'aes') app_password,  ");
            sql.append(" email_address, created_datetz, edit_datetz, ");
            sql.append(" last_name || ', '|| first_name as field_user  ");
            sql.append(" FROM  ");
            sql.append(" field_manage.field_user  ");
            sql.append(" order by  ");
            sql.append(" last_name asc ");

            lgr.log(Level.INFO, "sql: " + sql);
            try (Connection con = DriverManager.getConnection(url, user, password);
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(sql.toString());) {
                List<FieldUser> fieldUserList = new ArrayList<FieldUser>();
                while (rs.next()) {
                    FieldUser fieldUser = new FieldUser();
                    fieldUser.setFieldUserId(rs.getInt("field_user_id"));
                    fieldUser.setLastName(rs.getString("last_name"));
                    fieldUser.setFirstName(rs.getString("first_name"));
                    fieldUser.setAppAdmin(rs.getBoolean("app_admin_b"));
                    fieldUser.setAppLogin(rs.getString("app_login"));
                    fieldUser.setAppPassword(rs.getString("app_password"));
                    fieldUser.setEmail(rs.getString("email_address"));
                    fieldUser.setCreatedDate(rs.getTimestamp("created_datetz"));
                    fieldUser.setEditDate(rs.getTimestamp("edit_datetz"));
                    fieldUser.setListDisplayName(fieldUser.getLastName() + ", " + fieldUser.getFirstName());
                    fieldUserList.add(fieldUser);
                }

                ObjectMapper mapper = new ObjectMapper();
                output = mapper.writeValueAsString(fieldUserList);
                lgr.log(Level.INFO, "output: " + output);
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

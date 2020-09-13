package dbconnect;

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


    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");

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
            String output = String.format("{ \"message\": \"db connect\", \"location\": \"%s\" }", "no one");
            try (Connection con = DriverManager.getConnection(url, user, password);
                 Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery("SELECT last_name from field_manage.field_user where field_user_id=1")) {

                if (rs.next()) {
                    output = String.format("{ \"message\": \"db connect\", \"location\": \"%s\" }", rs.getString("last_name"));
                }

            } catch (SQLException ex) {

                Logger lgr = Logger.getLogger(App.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);
            }

            return response
                    .withStatusCode(200)
                    .withBody(output);
        } catch (Exception e) {
            Logger lgr = Logger.getLogger(App.class.getName());
            lgr.log(Level.SEVERE, e.getMessage(), e);
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

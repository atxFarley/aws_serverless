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
import com.fasterxml.jackson.core.type.TypeReference;


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
        headers.put("Access-Control-Allow-Methods", "OPTIONS,DELETE");
        String fieldId = null;
        if (input != null) {
            Map<String, String> queryStringParameters = input.getQueryStringParameters();
            Map<String, String> pathParameters = input.getPathParameters();
            if (pathParameters != null) {
                fieldId = pathParameters.get("fieldid");
            } else if (queryStringParameters != null) {
                fieldId = queryStringParameters.get("fieldid");
            } else {
                lgr.log(Level.INFO, "fieldId from AWS Console Test Event: " + fieldId);
                try {
                    String requestString = input.getBody();
                    JSONParser parser = new JSONParser();
                    JSONObject requestJsonObject = (JSONObject) parser.parse(requestString);
                    if (requestJsonObject != null) {
                        if (requestJsonObject.get("fieldId") != null) {
                            fieldId = requestJsonObject.get("fieldId").toString();
                        }
                    }
                } catch (ParseException ex) {
                    lgr.log(Level.SEVERE, "ParseException caught: " + ex.getMessage(), ex);
                }
            }
        }
        lgr.log(Level.INFO, "fieldId: " + fieldId);
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        String output = "{}";
        try {
            if (fieldId != null && fieldId.trim() != null && Integer.valueOf(fieldId) != null && Integer.valueOf(fieldId) > 0) {
                Yaml yaml = new Yaml(new Constructor(Datasource.class));
                InputStream inputStream = this.getClass()
                        .getResourceAsStream("/application.yml");
                Datasource ds = yaml.load(inputStream);
                String url = ds.getUrl();
                String user = ds.getUsername();
                String password = ds.getPassword();
                ObjectMapper objectMapper = new ObjectMapper();
                StringBuilder deleteFieldGrowerSql = new StringBuilder();
                StringBuilder deleteFieldSql = new StringBuilder();
                deleteFieldSql.append(" Delete from field_manage.field where field_id= ? ");
                deleteFieldGrowerSql.append(" Delete from field_manage.field_grower where field_id= ? ");
                try (Connection con = DriverManager.getConnection(url, user, password);
                     PreparedStatement ps = con.prepareStatement(deleteFieldGrowerSql.toString());) {
                    ps.setInt(1, Integer.valueOf(fieldId));
                    lgr.log(Level.INFO, "deleteGrowerSql: " + deleteFieldGrowerSql);
                    try {
                        int rowCount = ps.executeUpdate();
                    } catch (SQLException innerSQLEx) {
                        lgr.log(Level.SEVERE, "SQLException caught deleting field grower : " + innerSQLEx.getMessage(), innerSQLEx);
                    }
                } catch (SQLException ex) {
                    lgr.log(Level.SEVERE, "SQLException caught: " + ex.getMessage(), ex);
                }
                try (Connection con = DriverManager.getConnection(url, user, password);
                     PreparedStatement ps = con.prepareStatement(deleteFieldSql.toString());) {
                    ps.setInt(1, Integer.valueOf(fieldId));
                    lgr.log(Level.INFO, "deleteFieldSql: " + deleteFieldSql);
                    try {
                        int rowCount = ps.executeUpdate();
                        Map<String, Integer> deleteFieldMap = new HashMap<String, Integer>();
                        deleteFieldMap.put("deleteFieldId", Integer.valueOf(fieldId));
                        ObjectMapper mapper = new ObjectMapper();
                        output = mapper.writeValueAsString(deleteFieldMap);
                        lgr.log(Level.INFO, "output: " + output);
                    } catch (SQLException innerSQLEx) {
                        lgr.log(Level.SEVERE, "SQLException caught deleting field: " + innerSQLEx.getMessage(), innerSQLEx);
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

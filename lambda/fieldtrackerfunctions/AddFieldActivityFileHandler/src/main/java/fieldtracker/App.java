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
        headers.put("Access-Control-Allow-Methods", "OPTIONS,POST");
        String fieldActivityId = null;
        String fieldId = null;
        String activityFile = null;
        if (input != null) {
            try {
                Map<String, String> queryStringParameters = input.getQueryStringParameters();
                Map<String, String> pathParameters = input.getPathParameters();
                String requestString = input.getBody();
                lgr.log(Level.INFO, "input.getBody(): " + requestString);
                JSONParser parser = new JSONParser();
                JSONObject requestJsonObject = (JSONObject) parser.parse(requestString);
                if (pathParameters != null) {
                    fieldId = pathParameters.get("fieldid");
                    fieldActivityId = pathParameters.get("activityid");
                    lgr.log(Level.INFO, "pathParameters: : " + pathParameters);
                } else if (queryStringParameters != null) {
                    fieldId = queryStringParameters.get("fieldid");
                    fieldActivityId = queryStringParameters.get("activityid");
                } else {
                    lgr.log(Level.INFO, "fieldId from AWS Console Test Event: " + fieldId);
                    if (requestJsonObject != null) {
                        if (requestJsonObject.get("fieldId") != null) {
                            fieldId = requestJsonObject.get("fieldId").toString();
                        }
                        if (requestJsonObject.get("fieldActivityId") != null) {
                            fieldActivityId = requestJsonObject.get("fieldActivityId").toString();
                        }
                    }
                }
                if (requestJsonObject != null) {
                    if (requestJsonObject.get("fieldActivityFileLocation") != null) {
                        activityFile = requestJsonObject.get("fieldActivityFileLocation").toString();
                    }
                }
            } catch (ParseException ex) {
                lgr.log(Level.SEVERE, "ParseException caught: " + ex.getMessage(), ex);
            }
        }
        lgr.log(Level.INFO, "fieldActivityId: " + fieldActivityId);
        lgr.log(Level.INFO, "input: " + input.getBody());
        lgr.log(Level.INFO, "activityFile: " + activityFile);
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        String output = "{}";
        try {
            if (fieldActivityId != null && fieldActivityId.trim() != null && Integer.valueOf(fieldActivityId) != null && Integer.valueOf(fieldActivityId) > 0) {
                Yaml yaml = new Yaml(new Constructor(Datasource.class));
                InputStream inputStream = this.getClass()
                        .getResourceAsStream("/application.yml");
                Datasource ds = yaml.load(inputStream);
                String url = ds.getUrl();
                String user = ds.getUsername();
                String password = ds.getPassword();
                ObjectMapper objectMapper = new ObjectMapper();
                StringBuilder sql = new StringBuilder();
                if (activityFile != null) {
                    String filename = activityFile.substring((activityFile.lastIndexOf("/") + 1));
                    lgr.log(Level.INFO, "filename: " + filename);
                    sql.append("INSERT INTO field_manage.field_activity_file(field_activity_id, field_activity_file_type_id, file_location, filename, file_datetz, created_datetz, georeferenced_b)  ");
                    sql.append("  VALUES ");
                    sql.append("(");
                    sql.append(fieldActivityId).append(", ");
                    sql.append(4).append(", '");
                    sql.append(activityFile).append("', '");
                    sql.append(filename).append("', ");
                    sql.append("current_timestamp").append(", ");
                    //sql.append("to_timestamp('").append(activityDate).append("', 'MM/DD/YYYY'").append(")").append(", ");
                    sql.append("current_timestamp").append(", ");
                    sql.append("false");
                    sql.append(")");
                    lgr.log(Level.INFO, "sql: " + sql);
                    String[] columnNames = {"field_activity_file_id"};
                    try (Connection con = DriverManager.getConnection(url, user, password);
                         Statement st = con.createStatement();
                    ) {
                        int rowCount = st.executeUpdate(sql.toString(), columnNames);
                        Map<String, Integer> newFieldActivityFileMap = new HashMap<String, Integer>();
                        try (ResultSet rs = st.getGeneratedKeys()) {
                            if (rs.next()) {
                                newFieldActivityFileMap.put("newFieldActivityFileId", rs.getInt(1));
                            }
                            ObjectMapper mapper = new ObjectMapper();
                            output = mapper.writeValueAsString(newFieldActivityFileMap);
                            lgr.log(Level.INFO, "output: " + output);
                        } catch (SQLException innerSQLEx) {
                            lgr.log(Level.SEVERE, "SQLException inner try/catch caught: " + innerSQLEx.getMessage(), innerSQLEx);
                        }
                    } catch (SQLException ex) {
                        lgr.log(Level.SEVERE, "SQLException caught: " + ex.getMessage(), ex);
                    }

                }


            }
            return response
                    .withStatusCode(200)
                    .withBody(output);

        } catch (
                Exception e) {
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

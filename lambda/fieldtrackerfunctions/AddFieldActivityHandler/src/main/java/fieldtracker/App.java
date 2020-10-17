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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Date;
import java.time.format.DateTimeFormatter;

import java.util.ArrayList;
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
    final static String NO_GROWER_ASSIGN = "NONE";

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        headers.put("X-Custom-Header", "application/json");
        headers.put("Access-Control-Allow-Origin", "*");
        headers.put("Access-Control-Allow-Methods", "OPTIONS,POST");
        String fieldId = null;
        String fieldActivityTypeId = null;
        String activityDesc = null;
        String activityDate = null;
        if (input != null) {
            try {
                Map<String, String> queryStringParameters = input.getQueryStringParameters();
                Map<String, String> pathParameters = input.getPathParameters();
                String requestString = input.getBody();
                JSONParser parser = new JSONParser();
                JSONObject requestJsonObject = (JSONObject) parser.parse(requestString);
                if (pathParameters != null) {
                    fieldId = pathParameters.get("fieldid");
                } else if (queryStringParameters != null) {
                    fieldId = queryStringParameters.get("fieldid");
                } else {
                    lgr.log(Level.INFO, "fieldId from AWS Console Test Event: " + fieldId);
                    if (requestJsonObject != null) {
                        if (requestJsonObject.get("fieldId") != null) {
                            fieldId = requestJsonObject.get("fieldId").toString();
                        }
                    }
                }

                if (requestJsonObject != null) {
                    if (requestJsonObject.get("fieldActivityType") != null) {
                        fieldActivityTypeId = requestJsonObject.get("fieldActivityType").toString();
                    }
                    if (requestJsonObject.get("fieldActivityDesc") != null) {
                        activityDesc = requestJsonObject.get("fieldActivityDesc").toString();
                    }
                    if (requestJsonObject.get("fieldActivityDate") != null) {
                        activityDate = requestJsonObject.get("fieldActivityDate").toString();
                    }
                }
            } catch (ParseException ex) {
                lgr.log(Level.SEVERE, "ParseException caught: " + ex.getMessage(), ex);
            }
        }
        lgr.log(Level.INFO, "fieldId: " + fieldId);
        lgr.log(Level.INFO, "input: " + input.getBody());
        lgr.log(Level.INFO, "fieldActivityTypeId: " + fieldActivityTypeId);
        lgr.log(Level.INFO, "activityDesc: " + activityDesc);
        lgr.log(Level.INFO, "activityDate: " + activityDate);
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
                StringBuilder sql = new StringBuilder();


                if (fieldActivityTypeId != null && activityDate != null) {
                    Integer activityTypeId = Integer.valueOf(fieldActivityTypeId);

                    if (activityTypeId != null && activityTypeId > 0) {
                        sql.append("INSERT INTO field_manage.field_activity(field_id, field_activity_type_id, description, activity_datetz, created_datetz)  ");
                        sql.append("  VALUES ");
                        sql.append("(");
                        sql.append(fieldId).append(", ");
                        sql.append(activityTypeId).append(", ");
                        sql.append(activityDesc).append(", ");
                        sql.append("to_timestamp('").append(activityDate).append("', 'MM/DD/YYYY'").append(")").append(", ");
                        sql.append("current_timestamp");
                        sql.append(")");
                        lgr.log(Level.INFO, "sql: " + sql);
                        String[] columnNames = {"field_activity_id"};
                        try (Connection con = DriverManager.getConnection(url, user, password);
                             Statement st = con.createStatement();
                        ) {
                            int rowCount = st.executeUpdate(sql.toString(), columnNames);
                            Map<String, Integer> newFieldActivityMap = new HashMap<String, Integer>();
                            try (ResultSet rs = st.getGeneratedKeys()) {
                                if (rs.next()) {
                                    newFieldActivityMap.put("newFieldActivityId", rs.getInt(1));
                                }
                                ObjectMapper mapper = new ObjectMapper();
                                output = mapper.writeValueAsString(newFieldActivityMap);
                                lgr.log(Level.INFO, "output: " + output);
                            } catch (SQLException innerSQLEx) {
                                lgr.log(Level.SEVERE, "SQLException inner try/catch caught: " + innerSQLEx.getMessage(), innerSQLEx);
                            }
                        } catch (SQLException ex) {
                            lgr.log(Level.SEVERE, "SQLException caught: " + ex.getMessage(), ex);
                        }

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

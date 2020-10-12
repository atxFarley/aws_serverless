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
        headers.put("Access-Control-Allow-Methods", "OPTIONS,PUT");
        String fieldId = null;
        String editFieldName = null;
        String editFieldDesc = null;
        String editGrowerId = null;
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
                    if (requestJsonObject.get("fieldName") != null) {
                        editFieldName = requestJsonObject.get("fieldName").toString();
                    }
                    if (requestJsonObject.get("fieldDesc") != null) {
                        editFieldDesc = requestJsonObject.get("fieldDec").toString();
                    }
                    if (requestJsonObject.get("growerId") != null) {
                        editGrowerId = requestJsonObject.get("growerId").toString();
                    }
                }
            } catch (ParseException ex) {
                lgr.log(Level.SEVERE, "ParseException caught: " + ex.getMessage(), ex);
            }
        }
        lgr.log(Level.INFO, "fieldId: " + fieldId);
        lgr.log(Level.INFO, "input: " + input.getBody());
        lgr.log(Level.INFO, "editFieldName: " + editFieldName);
        lgr.log(Level.INFO, "editFieldDesc: " + editFieldDesc);
        lgr.log(Level.INFO, "editFieldGrowerId: " + editGrowerId);
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
                StringBuilder deleteGrowerSql = new StringBuilder();
                StringBuilder insertGrowerSql = new StringBuilder();
                StringBuilder updateFieldSql = new StringBuilder();
                deleteGrowerSql.append(" Delete from field_manage.field_grower where field_id= ? ");
                insertGrowerSql.append("insert into field_manage.field_grower (field_id, grower_id, created_datetz) values (?, ?, current_timestamp) ");
                updateFieldSql.append("update field_manage.field set edit_datetz = current_timestamp, ");
                if (editFieldName != null) {
                    updateFieldSql.append("  field_name = ? ");
                } else {
                    updateFieldSql.append(" field_name == null ");
                }
                if (editFieldDesc != null) {
                    updateFieldSql.append(" ,field_desc = ? ");
                }

                updateFieldSql.append("  where field_id = ?");


                if (editGrowerId != null && Integer.valueOf(editGrowerId) != null && Integer.valueOf(editGrowerId) > 0) {
                    try (Connection con = DriverManager.getConnection(url, user, password);
                         PreparedStatement ps = con.prepareStatement(deleteGrowerSql.toString());) {
                        ps.setInt(1, Integer.valueOf(fieldId));
                        lgr.log(Level.INFO, "deleteGrowerSql: " + deleteGrowerSql);
                        try {
                            int rowCount = ps.executeUpdate();
                        } catch (SQLException innerSQLEx) {
                            lgr.log(Level.SEVERE, "SQLException caught deleting grower : " + innerSQLEx.getMessage(), innerSQLEx);
                        }
                    } catch (SQLException ex) {
                        lgr.log(Level.SEVERE, "SQLException caught: " + ex.getMessage(), ex);
                    }
                    try (Connection con = DriverManager.getConnection(url, user, password);
                         PreparedStatement ps = con.prepareStatement(insertGrowerSql.toString());) {
                        ps.setInt(1, Integer.valueOf(fieldId));
                        ps.setInt(2, Integer.valueOf(editGrowerId));
                        lgr.log(Level.INFO, "insertGrowerSql: " + insertGrowerSql);
                        try {
                            int rowCount = ps.executeUpdate();
                        } catch (SQLException innerSQLEx) {
                            lgr.log(Level.SEVERE, "SQLException caught inserting grower : " + innerSQLEx.getMessage(), innerSQLEx);
                        }
                    } catch (SQLException ex) {
                        lgr.log(Level.SEVERE, "SQLException caught: " + ex.getMessage(), ex);
                    }

                }


                try (Connection con = DriverManager.getConnection(url, user, password);
                     PreparedStatement ps = con.prepareStatement(updateFieldSql.toString());) {
                    int idx = 1;
                    if (editFieldName != null) {
                        ps.setString(idx, editFieldName);
                        idx++;
                    }
                    if (editFieldDesc != null) {
                        updateFieldSql.append(" ,field_desc = ? ");
                        ps.setString(idx, editFieldDesc);
                        idx++;
                    }
                    ps.setInt(idx, Integer.valueOf(fieldId));
                    lgr.log(Level.INFO, "updateFieldSql: " + updateFieldSql);
                    try {
                        int rowCount = ps.executeUpdate();
                        Map<String, Integer> updateFieldMap = new HashMap<String, Integer>();
                        updateFieldMap.put("updateFieldId", Integer.valueOf(fieldId));
                        ObjectMapper mapper = new ObjectMapper();
                        output = mapper.writeValueAsString(updateFieldMap);
                        lgr.log(Level.INFO, "output: " + output);
                    } catch (SQLException innerSQLEx) {
                        lgr.log(Level.SEVERE, "SQLException caught updating field : " + innerSQLEx.getMessage(), innerSQLEx);
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

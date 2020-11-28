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
        headers.put("Access-Control-Allow-Methods", "OPTIONS,GET");
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
                        if (requestJsonObject.get("fieldid") != null) {
                            fieldId = requestJsonObject.get("fieldid").toString();
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
                StringBuilder sql = new StringBuilder();
                sql.append("select field.*, grower.first_name || grower.last_name as growername, ");
                sql.append(" grower.field_user_id as growerId, ");
                sql.append(" owner.first_name || owner.last_name as ownername, ");
                sql.append(" owner.field_user_id as ownerId, ");
                sql.append(" (st_area(st_transform(field_geom,26914)) * 0.00024710538146717) as area_acres ");
                sql.append("from field_manage.field  ");
                sql.append("left outer join field_manage.field_grower ");
                sql.append("on field.field_id = field_grower.field_id ");
                sql.append("left outer join field_manage.field_user as grower ");
                sql.append("on field_grower.grower_id = grower.field_user_id ");
                sql.append(" left outer join field_manage.field_owner ");
                sql.append(" on field.field_id = field_owner.field_id ");
                sql.append(" left outer join field_manage.field_user as owner ");
                sql.append(" on field_owner.owner_id = owner.field_user_id ");
                sql.append("  where field.field_id =  ? ");
                lgr.log(Level.INFO, "sql: " + sql.toString());
                Field fieldDetail = new Field();
                ArrayList<Field.FieldActivity> fieldActivityArrayList = new ArrayList<Field.FieldActivity>();
                try (Connection con = DriverManager.getConnection(url, user, password);
                     PreparedStatement ps = con.prepareStatement(sql.toString());) {
                    ps.setInt(1, Integer.valueOf(fieldId));
                    try (ResultSet rs = ps.executeQuery();) {
                        if (rs.next()) {
                            fieldDetail.setFieldId(rs.getInt("field_id"));
                            fieldDetail.setFieldName(rs.getString("field_name"));
                            fieldDetail.setFieldDesc(rs.getString("field_desc"));
                            fieldDetail.setAddressStreet(rs.getString("address") + " " + rs.getString("adddress_2"));
                            fieldDetail.setAddressCity(rs.getString("city"));
                            fieldDetail.setAddressState(rs.getString("state"));
                            fieldDetail.setAddressZip(rs.getString("zip"));
                            fieldDetail.setAddressCounty(rs.getString("county"));
                            fieldDetail.setAcres(rs.getBigDecimal("area_acres"));
                            fieldDetail.setGrowerName(rs.getString("growername"));
                            fieldDetail.setGrowerId(rs.getInt("growerId"));
                            fieldDetail.setOwnerName(rs.getString("ownername"));
                            fieldDetail.setOwnerId(rs.getInt("ownerId"));
                            String attributesString = rs.getString("field_attributes");
                            if (attributesString != null) {

                                Map<String, String> attributes = objectMapper.readValue(attributesString, new TypeReference<Map<String, String>>() {
                                });
                                ArrayList<Field.FieldAttribute> fieldAttributeArrayList = new ArrayList<Field.FieldAttribute>();
                                for (String key : attributes.keySet()) {
                                    Field.FieldAttribute fieldAttribute = fieldDetail.new FieldAttribute();
                                    fieldAttribute.setFieldId(fieldDetail.getFieldId());
                                    fieldAttribute.setAttributeName(key);
                                    fieldAttribute.setAttributeValue(attributes.get(key));
                                    fieldAttributeArrayList.add(fieldAttribute);
                                }
                                fieldDetail.setFieldAttributes(fieldAttributeArrayList);
                            }
                            //get fieldActivities and activity files

                        }
                    } catch (SQLException innerEx) {
                        lgr.log(Level.SEVERE, "SQLException caught: " + innerEx.getMessage(), innerEx);
                    }
                } catch (SQLException ex) {
                    lgr.log(Level.SEVERE, "SQLException caught: " + ex.getMessage(), ex);
                }
                StringBuilder activitySql = new StringBuilder();
                activitySql.append("select field_activity_id, field_id, field_activity_type_id, description, to_char(activity_datetz, 'MM/DD/YYYY') activity_datetz ");
                activitySql.append(" from field_manage.field_activity ");
                activitySql.append("where field_id = ? order by activity_datetz desc, created_datetz desc ");
                lgr.log(Level.INFO, "activity sql: " + activitySql.toString());
                try (Connection con = DriverManager.getConnection(url, user, password);
                     PreparedStatement ps = con.prepareStatement(activitySql.toString());) {
                    ps.setInt(1, Integer.valueOf(fieldId));
                    try (ResultSet rs = ps.executeQuery();) {
                        while (rs.next()) {
                            Field.FieldActivity fieldActivity = fieldDetail.new FieldActivity();
                            fieldActivity.setFieldActivityId(rs.getInt("field_activity_id"));
                            fieldActivity.setFieldId(rs.getInt("field_id"));
                            fieldActivity.setFieldActivityTypeId(rs.getInt("field_activity_type_id"));
                            fieldActivity.setFieldActivityDesc(rs.getString("description"));
                            fieldActivity.setFieldActivityDate(rs.getString("activity_datetz"));
                            fieldActivityArrayList.add(fieldActivity);
                        }
                        fieldDetail.setFieldActivities(fieldActivityArrayList);
                    } catch (SQLException innerEx) {
                        lgr.log(Level.SEVERE, "SQLException caught: " + innerEx.getMessage(), innerEx);
                    }
                } catch (SQLException ex) {
                    lgr.log(Level.SEVERE, "SQLException caught: " + ex.getMessage(), ex);
                }
                StringBuilder activityFileSql = new StringBuilder();
                activityFileSql.append("select field_activity_file_id, field_activity_id, field_activity_file_type_id, file_size_mb, file_location, filename ");
                activityFileSql.append(" from field_manage.field_activity_file ");
                activityFileSql.append(" where field_activity_id = ? ");
                for (Field.FieldActivity activity : fieldActivityArrayList) {
                    Integer fieldActivityId = activity.getFieldActivityId();
                    try (Connection con = DriverManager.getConnection(url, user, password);
                         PreparedStatement ps = con.prepareStatement(activityFileSql.toString());) {
                        ps.setInt(1, Integer.valueOf(fieldActivityId));
                        ArrayList<Field.FieldActivity.FieldActivityFile> fieldActivityFiles = new ArrayList<Field.FieldActivity.FieldActivityFile>();
                        try (ResultSet rs = ps.executeQuery();) {
                            while (rs.next()) {
                                Field.FieldActivity.FieldActivityFile fieldActivityFile = activity.new FieldActivityFile();
                                fieldActivityFile.setFieldActivityFileId(rs.getInt("field_activity_file_id"));
                                fieldActivityFile.setFieldActivityId(rs.getInt("field_activity_id"));
                                fieldActivityFile.setFieldActivityFileTypeId(rs.getInt("field_activity_file_type_id"));
                                fieldActivityFile.setFieldActivityFileSizeMB(rs.getBigDecimal("file_size_mb"));
                                fieldActivityFile.setFieldActivityFileLocation(rs.getString("file_location"));
                                fieldActivityFile.setFieldActivityFilename(rs.getString("filename"));
                                fieldActivityFiles.add(fieldActivityFile);
                            }
                            activity.setFieldActivityFiles(fieldActivityFiles);
                        } catch (SQLException innerEx) {
                            lgr.log(Level.SEVERE, "SQLException caught: " + innerEx.getMessage(), innerEx);
                        }
                    } catch (SQLException ex) {
                        lgr.log(Level.SEVERE, "SQLException caught: " + ex.getMessage(), ex);
                    }

                }

                try {
                    output = objectMapper.writeValueAsString(fieldDetail);
                } catch (Exception jsonEx) {
                    lgr.log(Level.SEVERE, "Exception caught: " + jsonEx.getMessage(), jsonEx);
                }
                lgr.log(Level.INFO, "output: " + output);
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

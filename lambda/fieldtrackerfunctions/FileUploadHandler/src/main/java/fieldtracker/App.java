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
        String activityFile = null;
        if (input != null) {
            try {
                Map<String, String> queryStringParameters = input.getQueryStringParameters();
                Map<String, String> pathParameters = input.getPathParameters();
                String requestString = input.getBody();
                JSONParser parser = new JSONParser();
                JSONObject requestJsonObject = (JSONObject) parser.parse(requestString);
                if (pathParameters != null) {
                    fieldActivityId = pathParameters.get("fieldActivityId");
                } else if (queryStringParameters != null) {
                    fieldActivityId = queryStringParameters.get("fieldActivityId");
                } else {
                    lgr.log(Level.INFO, "fieldActivityId from AWS Console Test Event: " + fieldActivityId);
                    if (requestJsonObject != null) {
                        if (requestJsonObject.get("fieldActivityId") != null) {
                            fieldActivityId = requestJsonObject.get("fieldActivityId").toString();
                        }
                    }
                }

                if (requestJsonObject != null) {
                    if (requestJsonObject.get("activityFile") != null) {
                        activityFile = requestJsonObject.get("activityFile").toString();
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

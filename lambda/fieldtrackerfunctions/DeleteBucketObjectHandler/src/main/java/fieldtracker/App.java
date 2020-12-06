package fieldtracker;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;

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
        String fileKey = null;
        String fileContentType = null;
        if (input != null) {
            try {
                Map<String, String> queryStringParameters = input.getQueryStringParameters();
                Map<String, String> pathParameters = input.getPathParameters();
                String requestString = input.getBody();
                JSONParser parser = new JSONParser();
                JSONObject requestJsonObject = (JSONObject) parser.parse(requestString);
                if (pathParameters != null) {
                    fileKey = pathParameters.get("fileKey");
                    lgr.log(Level.INFO, "fileKey from pathParameters: " + fileKey);
                } else if (queryStringParameters != null) {
                    fileKey = queryStringParameters.get("fileKey");
                    lgr.log(Level.INFO, "fileKey from queryStringParameters: " + fileKey);
                } else {
                    if (requestJsonObject != null) {
                        if (requestJsonObject.get("fileKey") != null) {
                            fileKey = requestJsonObject.get("fileKey").toString().trim();
                            lgr.log(Level.INFO, "fileKey from JSON: " + fileKey);
                        }
                    }
                }

                if (requestJsonObject != null) {
                    if (requestJsonObject.get("fileContentType") != null) {
                        fileContentType = requestJsonObject.get("fileContentType").toString().trim();
                    }

                }
            } catch (ParseException ex) {
                lgr.log(Level.SEVERE, "ParseException caught: " + ex.getMessage(), ex);
            }
        }
        lgr.log(Level.INFO, "fileKey: " + fileKey);
        lgr.log(Level.INFO, "input: " + input.getBody());
        lgr.log(Level.INFO, "fileContentType: " + fileContentType);

        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent()
                .withHeaders(headers);

        String output = "{}";
        try {
            if (fileKey != null) {
                Yaml yaml = new Yaml(new Constructor(Bucket.class));
                InputStream inputStream = this.getClass()
                        .getResourceAsStream("/application.yml");
                Bucket bucket = yaml.load(inputStream);
                String bucketName = bucket.getBucketName();
                //Regions clientRegion = Regions.DEFAULT_REGION;
                Regions clientRegion = Regions.fromName(bucket.getRegion());
                lgr.log(Level.INFO, "clientRegion: " + clientRegion);
                Regions defaultClientRegion = Regions.DEFAULT_REGION;
                lgr.log(Level.INFO, "defaultClientRegion: " + defaultClientRegion);

                try {

                    AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
                            .withRegion(clientRegion)
                            .withCredentials(DefaultAWSCredentialsProviderChain.getInstance())
                            .build();

                    s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileKey));
                    Map<String, String> deleteFieldActivityFileKeyMap = new HashMap<String, String>();
                    deleteFieldActivityFileKeyMap.put("deleteFieldActivityFileKey", fileKey);
                    ObjectMapper mapper = new ObjectMapper();
                    output = mapper.writeValueAsString(deleteFieldActivityFileKeyMap);
                    lgr.log(Level.INFO, "output: " + output);
                } catch (AmazonServiceException e) {
                    // The call was transmitted successfully, but Amazon S3 couldn't process
                    // it, so it returned an error response.
                    lgr.log(Level.SEVERE, "AmazonServiceException  caught : " + e.getMessage(), e);

                } catch (SdkClientException e) {
                    // Amazon S3 couldn't be contacted for a response, or the client
                    // couldn't parse the response from Amazon S3.
                    lgr.log(Level.SEVERE, "SdkClientException caught : " + e.getMessage(), e);
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

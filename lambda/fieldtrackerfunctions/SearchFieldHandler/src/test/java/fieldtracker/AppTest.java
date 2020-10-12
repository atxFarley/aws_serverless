package fieldtracker;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AppTest {

    @Test
    public void allRecordsResponse() {
        App app = new App();
        APIGatewayProxyResponseEvent result = app.handleRequest(null, null);
        assertEquals(result.getStatusCode().intValue(), 200);
        assertEquals(result.getHeaders().get("Content-Type"), "application/json");
        String content = result.getBody();
        assertNotNull(content);
//    assertTrue(content.contains("EPSG:3857"));
        assertTrue(content.contains("Polygon"));
        assertTrue(content.contains("fieldname"));
    }

    @Test
    public void successfulResponse() {
        App app = new App();
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
        Map<String, String> queryStringParameters = new HashMap<String, String>();
        queryStringParameters.put("search", "Farley");
        input.setQueryStringParameters(queryStringParameters);
        APIGatewayProxyResponseEvent result = app.handleRequest(input, null);
        assertEquals(result.getStatusCode().intValue(), 200);
        assertEquals(result.getHeaders().get("Content-Type"), "application/json");
        String content = result.getBody();
        assertNotNull(content);
//    assertTrue(content.contains("EPSG:3857"));
        assertTrue(content.contains("Polygon"));
        assertTrue(content.contains("fieldname"));
    }

    @Test
    public void emptyResponse() {
        App app = new App();
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
        Map<String, String> queryStringParameters = new HashMap<String, String>();
        queryStringParameters.put("search", "wibbly wobbly whoa");
        input.setQueryStringParameters(queryStringParameters);
        APIGatewayProxyResponseEvent result = app.handleRequest(input, null);
        assertEquals(result.getStatusCode().intValue(), 200);
        assertEquals(result.getHeaders().get("Content-Type"), "application/json");
        String content = result.getBody();
        assertNotNull(content);
//    assertTrue(content.contains("EPSG:3857"));
        assertFalse(content.contains("Polygon"));
        assertFalse(content.contains("fieldname"));
    }


    @Test
    public void successfulInputBodyResponse() {
        App app = new App();
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
//    Map<String, String> queryStringParameters = new HashMap<String, String>();
//    queryStringParameters.put ("search", "Farley");
//    input.setQueryStringParameters(queryStringParameters);
        input.setBody("{\"search\":\"Farley\"}");
        APIGatewayProxyResponseEvent result = app.handleRequest(input, null);
        assertEquals(result.getStatusCode().intValue(), 200);
        assertEquals(result.getHeaders().get("Content-Type"), "application/json");
        String content = result.getBody();
        assertNotNull(content);
//    assertTrue(content.contains("EPSG:3857"));
        assertTrue(content.contains("Polygon"));
        assertTrue(content.contains("fieldname"));
    }

    @Test
    public void emptyInputBodyResponse() {
        App app = new App();
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
//    Map<String, String> queryStringParameters = new HashMap<String, String>();
//    queryStringParameters.put ("search", "wibbly wobbly whoa");
//    input.	setQueryStringParameters(queryStringParameters);
        input.setBody("{\"search\":\"wibbly wobbly whoa\"}");
        APIGatewayProxyResponseEvent result = app.handleRequest(input, null);
        assertEquals(result.getStatusCode().intValue(), 200);
        assertEquals(result.getHeaders().get("Content-Type"), "application/json");
        String content = result.getBody();
        assertNotNull(content);
//    assertTrue(content.contains("EPSG:3857"));
        assertFalse(content.contains("Polygon"));
        assertFalse(content.contains("fieldname"));
    }

    @Test
    public void successfulNoneInputBodyResponse() {
        App app = new App();
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
//    Map<String, String> queryStringParameters = new HashMap<String, String>();
//    queryStringParameters.put ("search", "Farley");
//    input.setQueryStringParameters(queryStringParameters);
        input.setBody("{\"search\":\"none\"}");
        APIGatewayProxyResponseEvent result = app.handleRequest(input, null);
        assertEquals(result.getStatusCode().intValue(), 200);
        assertEquals(result.getHeaders().get("Content-Type"), "application/json");
        String content = result.getBody();
        assertNotNull(content);
//    assertTrue(content.contains("EPSG:3857"));
        assertTrue(content.contains("Polygon"));
        assertTrue(content.contains("\"growername\": null"));
    }
}

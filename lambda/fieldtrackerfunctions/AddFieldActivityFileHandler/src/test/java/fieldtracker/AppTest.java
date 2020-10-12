package fieldtracker;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class AppTest {
//
//
//    @Test
//    public void noFieldIdResponse() {
//        App app = new App();
//        APIGatewayProxyResponseEvent result = app.handleRequest(null, null);
//        assertEquals(result.getStatusCode().intValue(), 200);
//        assertEquals(result.getHeaders().get("Content-Type"), "application/json");
//        String content = result.getBody();
//        assertEquals(content, "{}");
//
//    }
//
//    @Test
//    public void successfulResponse() {
//        App app = new App();
//        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
//        Map<String, String> queryStringParameters = new HashMap<String, String>();
//        queryStringParameters.put("fieldid", "4");
//        input.setQueryStringParameters(queryStringParameters);
//        APIGatewayProxyResponseEvent result = app.handleRequest(input, null);
//        assertEquals(result.getStatusCode().intValue(), 200);
//        assertEquals(result.getHeaders().get("Content-Type"), "application/json");
//        String content = result.getBody();
//        assertNotNull(content);
//        assertTrue(content.contains("fieldId"));
//        assertTrue(content.contains("acres"));
//    }
//
//
//
//
//    @Test
//    public void successfulInputBodyResponse() {
//        App app = new App();
//        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
////    Map<String, String> queryStringParameters = new HashMap<String, String>();
////    queryStringParameters.put ("search", "Farley");
////    input.setQueryStringParameters(queryStringParameters);
//        input.setBody("{\"fieldid\":\"4\"}");
//        APIGatewayProxyResponseEvent result = app.handleRequest(input, null);
//        assertEquals(result.getStatusCode().intValue(), 200);
//        assertEquals(result.getHeaders().get("Content-Type"), "application/json");
//        String content = result.getBody();
//        assertNotNull(content);
//        assertTrue(content.contains("fieldId"));
//        assertTrue(content.contains("acres"));
//    }
//
//    @Test
//    public void pathParametersResponse() {
//        App app = new App();
//        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
//
//        Map<String, String> pathParameters = new HashMap<String, String>();
//        pathParameters.put("fieldid", "4");
//        input.setPathParameters(pathParameters);
//        APIGatewayProxyResponseEvent result = app.handleRequest(input, null);
//        assertEquals(result.getStatusCode().intValue(), 200);
//        assertEquals(result.getHeaders().get("Content-Type"), "application/json");
//        String content = result.getBody();
//        assertNotNull(content);
//        assertTrue(content.contains("fieldId"));
//        assertTrue(content.contains("acres"));
//    }
//


}


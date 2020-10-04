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
    public void successfulInputBodyResponse() {
        App app = new App();
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
        String layerJSON = "{\"type\":\"Feature\",\"properties\":{},\"geometry\":{\"type\":\"Polygon\",\"coordinates\":[[[-98.580644,32.137055],[-98.580644,32.137055],[-98.541142,32.144032],[-98.541142,32.144032],[-98.556256,32.171644],[-98.556256,32.171644],[-98.579957,32.138218],[-98.580644,32.137055]]]}}";
        input.setBody(layerJSON);
        APIGatewayProxyResponseEvent result = app.handleRequest(input, null);
        assertEquals(result.getStatusCode().intValue(), 200);
        assertEquals(result.getHeaders().get("Content-Type"), "application/json");
        String content = result.getBody();
         assertNotNull(content);
        assertTrue(content.contains("newFieldId"));
    }
}

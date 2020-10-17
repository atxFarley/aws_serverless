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



    @Test
    public void successfulInputBodyResponse() {
        App app = new App();
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
        String layerJSON = "{\n" +
                "    \"fieldId\": 4,\n" +
                "    \"fieldActivityId\": null,\n" +
                "    \"fieldActivityDate\": \"10/17/2020\",\n" +
                "    \"fieldActivityType\": \"1\",\n" +
                "    \"fieldActivityDesc\": null,\n" +
                "    \"fieldActivityFiles\": null\n" +
                "}";
        input.setBody(layerJSON);
        APIGatewayProxyResponseEvent result = app.handleRequest(input, null);
        assertEquals(result.getStatusCode().intValue(), 200);
        assertEquals(result.getHeaders().get("Content-Type"), "application/json");
        String content = result.getBody();
        assertNotNull(content);
        assertTrue(content.contains("newFieldActivityId"));
    }


}


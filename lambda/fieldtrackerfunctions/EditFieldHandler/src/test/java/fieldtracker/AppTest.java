package fieldtracker;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AppTest {


    @Test
    public void successfulInputBodyNullAttributesResponse() {
        App app = new App();
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
        String layerJSON = "{\n" +
                "    \"fieldId\": 20,\n" +
                "    \"fieldName\": \"Amy's Field\",\n" +
                "    \"fieldDesc\": null,\n" +
                "    \"growerName\": null,\n" +
                "    \"growerId\": \"2\",\n" +
                "    \"ownerName\": null,\n" +
                "    \"ownerId\": 0,\n" +
                "    \"acres\": 1550.9238210668125,\n" +
                "    \"addressStreet\": \"null null\",\n" +
                "    \"addressCity\": null,\n" +
                "    \"addressState\": null,\n" +
                "    \"addressZip\": null,\n" +
                "    \"addressCounty\": null,\n" +
                "    \"addressDesc\": null,\n" +
                " \"fieldAttributes\": null,\n" +
                "    \"fieldHistory\": null,\n" +
                "    \"fieldActivities\": null\n" +
                "}";
        input.setBody(layerJSON);
        APIGatewayProxyResponseEvent result = app.handleRequest(input, null);
        assertEquals(result.getStatusCode().intValue(), 200);
        assertEquals(result.getHeaders().get("Content-Type"), "application/json");
        String content = result.getBody();
        assertNotNull(content);
        assertTrue(content.contains("updateFieldId"));


    }


    @Test
    public void successfulInputBodyNullFieldNameResponse() {
        App app = new App();
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
        String layerJSON = "{\n" +
                "    \"fieldId\": 20,\n" +
                "    \"fieldName\": null,\n" +
                "    \"fieldDesc\": null,\n" +
                "    \"growerName\": null,\n" +
                "    \"growerId\": \"2\",\n" +
                "    \"ownerName\": null,\n" +
                "    \"ownerId\": 0,\n" +
                "    \"acres\": 1550.9238210668125,\n" +
                "    \"addressStreet\": \"null null\",\n" +
                "    \"addressCity\": null,\n" +
                "    \"addressState\": null,\n" +
                "    \"addressZip\": null,\n" +
                "    \"addressCounty\": null,\n" +
                "    \"addressDesc\": null,\n" +
                " \"fieldAttributes\": [{ \"attributeValues\": [\"cotton\"],\"attributeName\": \"crop\", \"fieldId\": 4 },{\"attributeValues\": [\"none\"            ],            \"attributeName\": \"irrigation\",                \"fieldId\": 4        },        {            \"attributeValues\": [            \"flood/furrow\"            ],            \"attributeName\": \"irrigation\",                \"fieldId\": 4        }    ],\n" +
                "    \"fieldHistory\": null,\n" +
                "    \"fieldActivities\": null\n" +
                "}";
        input.setBody(layerJSON);
        APIGatewayProxyResponseEvent result = app.handleRequest(input, null);
        assertEquals(result.getStatusCode().intValue(), 200);
        assertEquals(result.getHeaders().get("Content-Type"), "application/json");
        String content = result.getBody();
        assertNotNull(content);
        assertTrue(content.contains("updateFieldId"));


    }

    @Test
    public void successfulInputBodyResponse() {
        App app = new App();
        APIGatewayProxyRequestEvent input = new APIGatewayProxyRequestEvent();
        String layerJSON = "{\n" +
                "    \"fieldId\": 20,\n" +
                "    \"fieldName\": \"Amy's Field\",\n" +
                "    \"fieldDesc\": null,\n" +
                "    \"growerName\": null,\n" +
                "    \"growerId\": \"2\",\n" +
                "    \"ownerName\": null,\n" +
                "    \"ownerId\": 0,\n" +
                "    \"acres\": 1550.9238210668125,\n" +
                "    \"addressStreet\": \"null null\",\n" +
                "    \"addressCity\": null,\n" +
                "    \"addressState\": null,\n" +
                "    \"addressZip\": null,\n" +
                "    \"addressCounty\": null,\n" +
                "    \"addressDesc\": null,\n" +
                " \"fieldAttributes\": [{ \"attributeValues\": [\"cotton\"],\"attributeName\": \"crop\", \"fieldId\": 4 },{\"attributeValues\": [\"none\"            ],            \"attributeName\": \"irrigation\",                \"fieldId\": 4        },        {            \"attributeValues\": [            \"flood/furrow\"            ],            \"attributeName\": \"irrigation\",                \"fieldId\": 4        }    ],\n" +
                "    \"fieldHistory\": null,\n" +
                "    \"fieldActivities\": null\n" +
                "}";
        input.setBody(layerJSON);
        APIGatewayProxyResponseEvent result = app.handleRequest(input, null);
        assertEquals(result.getStatusCode().intValue(), 200);
        assertEquals(result.getHeaders().get("Content-Type"), "application/json");
        String content = result.getBody();
        assertNotNull(content);
        assertTrue(content.contains("updateFieldId"));


    }


}


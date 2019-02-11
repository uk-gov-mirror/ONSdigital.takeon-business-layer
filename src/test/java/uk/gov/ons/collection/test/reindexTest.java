package uk.gov.ons.collection.test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import uk.gov.ons.collection.service.ResponseComparison2;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

public class reindexTest {

    @BeforeClass
    public ResponseComparison2 setup(){
        JSONArray updatedArray = new JSONArray();
        JSONArray originalArray = new JSONArray();
        JSONObject warpperObject = new JSONObject();
        Map<String, String> updatedMap = new HashMap<>();
        Map<String, String> originalMap = new HashMap<>();

        updatedMap.put("questionCode:001|instance:0","response 1");
        updatedMap.put("createdBy","bob");
        updatedArray.put(updatedMap);
        updatedMap.clear();

        updatedMap.put("questionCode:002|instance:0","updated response 2");
        updatedMap.put("createdBy","bob");
        updatedArray.put(updatedMap);

        originalMap.put("questionCode:001|instance:0","response 1");
        originalMap.put("createdBy","bob");
        originalArray.put(originalMap);
        originalMap.clear();

        originalMap.put("questionCode:002|instance:0","response 2");
        originalMap.put("createdBy","bob");
        originalArray.put(originalMap);

        warpperObject.put("Updated Responses", updatedArray);
        warpperObject.put("Original Responses", originalArray);
        return new ResponseComparison2(warpperObject);

    }

    @Test
    public void findCombinedKey(){
        JSONObject testObject = new JSONObject();
        testObject.put("questionCode:001|instance:0", "a test responses");
        testObject.put("createdBy", "Bob");
        assertThat(setup().getCombinedKey(testObject), is("questionCode:001|instance:0"));
    }

    @Test
    public void relableResponseTest(){
        JSONObject testObject = new JSONObject();
        JSONObject returnedJson = new JSONObject();
        testObject.put("questionCode:001|instance:0", "a test response");
        testObject.put("createdBy", "Bob");

        returnedJson.put("questionCode:001|instance:0", "a test response");
        returnedJson.put("createdBy", "Bob");
        returnedJson.put("response", "a test response");
        assertEquals(setup().relabelResponses("questionCode:001|instance:0", testObject).toString().trim(), returnedJson.toString().trim());
    }

    @Test
    public void reindexTest(){
        JSONObject testObject = new JSONObject();
        JSONObject returnedJson = new JSONObject();
        JSONObject wrapperObject = new JSONObject();
        JSONArray testArray = new JSONArray();

        testObject.put("questionCode:001|instance:0", "a test response");
        testObject.put("createdBy", "Bob");

        returnedJson.put("questionCode:001|instance:0", "a test response");
        returnedJson.put("createdBy", "Bob");
        returnedJson.put("response", "a test response");

        testArray.put(testObject);

        wrapperObject.put("questionCode:001|instance:0", returnedJson);
        assertEquals(setup().reindex(testArray).toString().trim(), wrapperObject.toString().trim());
    }

    @Test
    public void getOnlyUpdatedTest(){
        JSONObject testObject = new JSONObject();
        JSONObject alteredJson = new JSONObject();
        JSONObject wrapperObject = new JSONObject();
        JSONObject updatedWrapperObject = new JSONObject();

        testObject.put("questionCode:001|instance:0", "a test response");
        testObject.put("response", "a test response");
        testObject.put("createdBy", "Bob");

        alteredJson.put("questionCode:001|instance:0", "a test response");
        alteredJson.put("createdBy", "Bob");
        alteredJson.put("response", "a different response");
        wrapperObject.put("questionCode:001|instance:0", testObject);
        updatedWrapperObject.put("questionCode:001|instance:0", alteredJson);

        assertEquals(setup().getOnlyUpdatedResponses(updatedWrapperObject, wrapperObject).toString().trim(), updatedWrapperObject.toString().trim());
    }
// Need to find test that doesn't care about order!
//    @Test
//    public void wholeTest(){
//        JSONObject warpperObject2 = new JSONObject();
//        Map<String, String> updatedMap2 = new HashMap<>();
//        Map<String, Map<String, String>> updatedMap3 = new HashMap<>();
//        updatedMap2.put("questionCode:002|instance:0","updated response 2");
//        updatedMap2.put("createdBy","bob");
//        updatedMap2.put("response", "updated response 2");
//        updatedMap3.put("questionCode:002|instance:0", updatedMap2);
//        warpperObject2.put("Updated Responses", updatedMap3);
//        assertEquals(setup().getUpdatedResponses().toString().trim(), warpperObject2.toString().trim());
//    }
}

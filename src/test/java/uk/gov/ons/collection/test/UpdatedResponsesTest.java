//package uk.gov.ons.collection.test;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.junit.BeforeClass;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import uk.gov.ons.collection.service.ResponseComparison;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import java.util.HashMap;
//import java.util.Map;
//import static org.hamcrest.MatcherAssert.assertThat;
//import static org.hamcrest.CoreMatchers.is;
//
//@DisplayName("Response mapper test")
//public class UpdatedResponsesTest {
//
//
//
//
//    @BeforeClass
//    public ResponseComparison setUp(){
//        Map<String, String> mapObject = new HashMap<>();
//        mapObject.put("questionCode", "001");
//        mapObject.put("instance", "0");
//        mapObject.put("response", "123");
//
//        Map<String, String> mapObjectTwo = new HashMap<>();
//        mapObjectTwo.put("questionCode", "001");
//        mapObjectTwo.put("instance", "1");
//        mapObjectTwo.put("response", "23");
//
//        JSONArray jsonArray = new JSONArray();
//        JSONArray jsonArray1 = new JSONArray();
//        jsonArray.put(mapObject);
//        jsonArray.put(mapObjectTwo);
//
//
//        JSONObject testObject =  new JSONObject();
//        testObject.put("Original Responses", jsonArray);
//        testObject.put("Updated Responses", jsonArray1);
//
//        ResponseComparison responseComparison2 = new ResponseComparison(testObject);
//        return responseComparison2;
//    }
//
//    @Test
//    void testMethodReturnsMap(){
//        JSONArray emptyArray = new JSONArray();
////        Map<String, String> returnMap = setUp().getResponsesIntoMap(emptyArray);
////        assertThat(returnMap.isEmpty(), is(true));
//    }
//
//
//    @Test
//    void testQuestionCodesAndInstanceConcatTogether(){
//        JSONArray testArray =  new JSONArray();
//        JSONObject testObject = new JSONObject();
//        JSONObject testObjectTwo = new JSONObject();
//        Map<String, String> actualToCompare = new HashMap<>();
//
//        actualToCompare.put("questionCode:001|instance:0", "123");
//        actualToCompare.put("questionCode:002|instance:0", "456");
//
//        testObject.put("questionCode", "001");
//        testObject.put("instance", 0);
//        testObject.put("response", "123");
//
//        testObjectTwo.put("questionCode", "002");
//        testObjectTwo.put("instance", 0);
//        testObjectTwo.put("response", "456");
//
//        testArray.put(testObject);
//        testArray.put(testObjectTwo);
//
//        JSONArray output = setUp().concatenateQuestionCodesAndInstance(testArray);
//        assertThat(output.getJSONObject(0).get("questionCode:002|instance:0"), is("456"));
//        assertThat(output.getJSONObject(0).get("questionCode:001|instance:0"), is("123"));
//    }
//
//    @Test
//    void testMethodReturnsJsonObject(){
//        Map<String, String> emptyMap = new HashMap<>();
//        assertThat(setUp().getOnlyUpdatedResponses().getClass().getName(), is("org.json.JSONObject"));
//    }
//
////    @Test
////    void testMethodReturnsOnlyUpdatedResponses(){
////        Map<String, String> originalDataMap = new HashMap<>();
////        Map<String, String> updatedDataMap = new HashMap<>();
////        Map<String, String> mapForUpdated = new HashMap<>();
////        JSONArray jsonArray = new JSONArray();
////        JSONObject jsonObject = new JSONObject();
////
////        originalDataMap.put("questionCode:001|instance:0", "123");
////        originalDataMap.put("questionCode:002|instance:0", "123");
////        originalDataMap.put("questionCode:003|instance:0", "000");
////
////        updatedDataMap.put("questionCode:001|instance:0", "123");
////        updatedDataMap.put("questionCode:002|instance:0", "000");
////        updatedDataMap.put("questionCode:003|instance:0", "000");
////
////        mapForUpdated.put("questionCode:002|instance:0", "000");
////        jsonArray.put(mapForUpdated);
////        jsonObject.put("Updated Responses", jsonArray);
////        System.out.println(jsonObject.toString());
////
////        assertThat(setUp().findUpdatedResponses(originalDataMap, updatedDataMap), is(jsonObject));
////    }
//}

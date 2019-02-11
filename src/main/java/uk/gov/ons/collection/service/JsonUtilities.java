package uk.gov.ons.collection.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

// Simple utility class
public class JsonUtilities {

    private JSONArray inputJsonArray;
    private JSONObject inputJsonObject;
    private Map<String, String> inputMapObject;
    private String outputKey;
    private JSONArray outputJsonArray = new JSONArray();
    private JSONObject outputJsonObject = new JSONObject();

     JsonUtilities(String key, Map<String, String> inputMapObject){
        this.outputKey = key;
        this.inputMapObject = inputMapObject;
    }

    JsonUtilities(Map<String, String> inputMapObject){
        this.inputMapObject = inputMapObject;
    }

    JsonUtilities(String key, JSONArray inputJsonArray){
        this.outputKey = key;
        this.inputJsonArray = inputJsonArray;
    }

    public JsonUtilities(JSONObject inputJsonObject){
         this.inputJsonObject = inputJsonObject;
         this.inputJsonArray = inputJsonObject.getJSONArray("Updated Responses");
    }

    public JSONObject buildJsonObjectFromMap(){
        return outputJsonObject.put(outputKey, outputJsonArray.put(inputMapObject));
    }

    public JSONObject buildJsonObjectFromJsonArray(){
        return outputJsonObject.put(outputKey, outputJsonArray.put(inputJsonArray));
    }

    public JSONObject buildJsonObjectFromJsonArray(String outputKey, JSONArray inputJsonArray){
        return outputJsonObject.put(outputKey, inputJsonArray);
    }

    public void buildJsonArrayFromMap(Map<String, String> inputMapObject){
          this.outputJsonArray.put(inputMapObject);
    }

    public JSONArray buildJsonArrayFromMap(){
        return outputJsonArray.put(this.inputMapObject);
    }

    // breakKeysUp
    public JSONObject breakKeysUp(){
         // There should only be one object in this array
         JSONObject inputJson = this.inputJsonArray.getJSONObject(0);
         JSONArray outputJson = new JSONArray();
         Set<String> inputKeySet = inputJson.keySet();
         // Get each key from the input keyset
         for(String key: inputKeySet){
             Map<String, String> outputMap = new HashMap<>();
             // Get the response
             String response = inputJson.get(key).toString();
             // put the response into the map
             outputMap.put("response", response);
             // split the key at the | symbol
             String[] parts = key.split("\\|");
             String[] keyAtoms;
             // for each part in the the array, split at the colon
             for(String part: parts){
                 keyAtoms = part.split(":");
                 System.out.println(keyAtoms[0]);
                 // put the key atoms into the map
                 outputMap.put(keyAtoms[0], keyAtoms[1]);
             }
             // build an object out of the map
             outputJson.put(outputMap);
         }
         // build a JSONObject
         return buildJsonObjectFromJsonArray("Updated Responses", outputJson);
    }
}

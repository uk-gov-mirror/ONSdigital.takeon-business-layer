package uk.gov.ons.collection.controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class qlQueryResponse {

    private JSONObject jsonQlResponse;

    // If we receive invalid json we consume the error and instead instantiate the current response object with valid but empty Json
    public qlQueryResponse(String jsonString) {
        try {
            jsonQlResponse = new JSONObject(jsonString);
        }
        catch(Exception e) {
            jsonString = "{}";
            jsonQlResponse = new JSONObject(jsonString);
        }
    }

    // Conversion from the QL response JSON structure to remove some nested attributes
    public String parse(){                      
        JSONObject parsedJsonQlResponse = new JSONObject();
        try {       
            JSONObject pageInfo = new JSONObject();     
            pageInfo.put("pageInfo", jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONObject("pageInfo"));
            pageInfo.getJSONObject("pageInfo").put("totalCount", jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getInt("totalCount"));
            parsedJsonQlResponse.put("data", jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes"));
            parsedJsonQlResponse.put("pageInfo", pageInfo.getJSONObject("pageInfo"));
        }
        catch(JSONException e){
            return "{\"error\":\"Invalid response from graphQL\"}";
        }
        return parsedJsonQlResponse.toString().replaceAll(":", ": ");
    }


    public ArrayList<Integer> parseForPeriodOffset(){                      
        ArrayList<Integer> uniqueOffsets = new ArrayList<>();
        JSONArray offsets = jsonQlResponse.getJSONObject("data").getJSONObject("allValidationperiods").getJSONArray("nodes");
        for (int i=0; i < offsets.length(); i++) {    
            Integer offset = Integer.valueOf(offsets.getJSONObject(i).getInt("periodoffset"));            
            if (!uniqueOffsets.contains(offset)) {
                uniqueOffsets.add(offset);
            }
        }
        return uniqueOffsets;
    }

}

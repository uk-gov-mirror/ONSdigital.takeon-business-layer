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

    public String toString() {
        return jsonQlResponse.toString();
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

    public int getFormID() {
        // {"data":{"allContributors":{"nodes":[
            //{"reference":"12345678001","formid":1,"period":"201801","surveyBySurvey":{"periodicity":"Monthly"},"survey":"999A"}]}}}
        return jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").getJSONObject(0).getInt("formid");
    }

    public String getPeriodicity() {
        return jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").getJSONObject(0).getJSONObject("surveyBySurvey").getString("periodicity");     
    }
    // "{\"data\":{\"allContributors\":{\"nodes\":[{\"formid\":1,\"birthdate\":\"\",\"selectiontype\":\" \"," +
    // "\"createddate\":\"2019-10-03T11:42:10.985316+00:00\",\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":" + 
    // "{\"reference\":\"12345678003\",\"period\":\"201801\",\"instance\":0,\"response\":\"\",\"questioncode\":\"1001\",\"survey\":\"999A\"}," +
    // "{\"reference\":\"12345678003\",\"period\":\"201801\",\"instance\":0,\"response\":\"0\",\"questioncode\":\"4001\",\"survey\":\"999A\"}]}" +
    // ",\"surveyBySurvey\":{\"periodicity\":\"Monthly\"},\"status\":\"Status\"}]}}}";
    public String getResponses() {
        return jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").getJSONObject(0).getJSONObject("responsesByReferenceAndPeriodAndSurvey")
                .getJSONArray("nodes").toString();
    }

}

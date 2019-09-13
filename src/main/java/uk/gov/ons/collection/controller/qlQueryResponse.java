package uk.gov.ons.collection.controller;

import org.json.JSONException;
import org.json.JSONObject;


public class qlQueryResponse {

    private JSONObject jsonQlResponse;

    public qlQueryResponse(String jsonString) {
        try {
            jsonQlResponse = new JSONObject(jsonString);
        }
        catch(Exception e) {
            jsonString = "{}";
            jsonQlResponse = new JSONObject(jsonString);
        }
    }


    public String parse(){
        try {
            jsonQlResponse.put("data", jsonQlResponse.getJSONObject("data")
                    .getJSONObject("allContributors")
                    .getJSONArray("nodes"));
            jsonQlResponse.remove("nodes");
        }
        catch(JSONException e){
            return "{\"error\":\"Invalid response from graphQL\"}";
        }
        return jsonQlResponse.toString().replaceAll(":", ": ");
    }

//    public String stringConvert() {
//        if(attributeCheck() == true) {
//            return filter();
//        }
//        return "";
//    }

    // string convert() { wrapper method to call attribute checks and filter then return string}
}

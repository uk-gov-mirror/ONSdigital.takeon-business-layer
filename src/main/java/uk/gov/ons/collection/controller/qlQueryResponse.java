package uk.gov.ons.collection.controller;

import org.json.JSONObject;


public class qlQueryResponse {

    private JSONObject jsonQlResponse;

    public qlQueryResponse(String jsonString) {
        jsonQlResponse = new JSONObject(jsonString);
    }


    // bool AttributeCheck(AttributeNameList[])

    public boolean attributeCheck() {
        if (jsonQlResponse.has("data")) {
            if (jsonQlResponse.getJSONObject("data").has("allContributors")) {
                if (jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").has("nodes")) {
                    return true;
                }
            }
        }
        return false;
    }

    public String filter(){

        String testString = jsonQlResponse
                                        .getJSONObject("data")
                                        .getJSONObject("allContributors")
                                        .getJSONArray("nodes")
                                        .toString();

        if(testString != null && !testString.isEmpty()) {
            jsonQlResponse
                        .put("data", jsonQlResponse
                        .getJSONObject("data")
                        .getJSONObject("allContributors")
                        .getJSONArray("nodes"));
            jsonQlResponse
                        .remove("nodes");

            return jsonQlResponse.toString().replaceAll(":", ": ");
        }
         return "";
    }

    public String stringConvert() {
        if(attributeCheck() == true) {
            return filter();
        }
        return "";
    }

    // string convert() { wrapper method to call attribute checks and filter then return string}
}

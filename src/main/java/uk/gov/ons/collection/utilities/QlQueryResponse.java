package uk.gov.ons.collection.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QlQueryResponse {

    private JSONObject jsonQlResponse;

    // If we receive invalid json we consume the error and instead instantiate the current response object with valid but empty Json
    public QlQueryResponse(String jsonString) {
        try {
            jsonQlResponse = new JSONObject(jsonString);
        } catch (Exception e) {
            jsonString = "{}";
            jsonQlResponse = new JSONObject(jsonString);
        }
    }

    public String toString() {
        return jsonQlResponse.toString();
    }

    // Conversion from the QL response JSON structure to remove some nested attributes
    public String parse() {
        JSONObject parsedJsonQlResponse = new JSONObject();
        try {       
            JSONObject pageInfo = new JSONObject();     
            pageInfo.put("pageInfo", jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONObject("pageInfo"));
            pageInfo.getJSONObject("pageInfo").put("totalCount", jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getInt("totalCount"));
            parsedJsonQlResponse.put("data", jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes"));
            parsedJsonQlResponse.put("pageInfo", pageInfo.getJSONObject("pageInfo"));
        } catch (JSONException e) {
            return "{\"error\":\"Invalid response from graphQL\"}";
        }
        return parsedJsonQlResponse.toString().replaceAll(":", ": ");
    }

    public JSONObject parseValidationOutputs() {
        var outputArray = new JSONArray();
        var valOutputArray = new JSONArray();
        System.out.println("Output from Validation Query before parsing: " + jsonQlResponse.toString());
        if (jsonQlResponse.getJSONObject("data").getJSONObject("allValidationoutputs").getJSONArray("nodes").length() > 0) {
            outputArray = jsonQlResponse.getJSONObject("data").getJSONObject("allValidationoutputs").getJSONArray("nodes");
        }
        for (int i = 0; i < outputArray.length(); i++) {
            JSONObject validationRule = new JSONObject();
            validationRule.put("formula", outputArray.getJSONObject(i).get("formula"));
            validationRule.put("triggered", outputArray.getJSONObject(i).get("triggered"));
            validationRule.put("lastupdatedby", outputArray.getJSONObject(i).get("lastupdatedby"));
            validationRule.put("lastupdateddate", outputArray.getJSONObject(i).get("lastupdateddate"));
            validationRule.put("instance", outputArray.getJSONObject(i).get("instance"));
            validationRule.put("validationoutputid", outputArray.getJSONObject(i).get("validationoutputid"));
            validationRule.put("overridden", outputArray.getJSONObject(i).get("overridden"));
            validationRule.put("severity", outputArray.getJSONObject(i).getJSONObject("validationformByValidationid").get("severity"));
            validationRule.put("validationid", outputArray.getJSONObject(i).getJSONObject("validationformByValidationid").get("validationid"));
            validationRule.put("rule", outputArray.getJSONObject(i).getJSONObject("validationformByValidationid").get("rule"));
            validationRule.put("primaryquestion", outputArray.getJSONObject(i).getJSONObject("validationformByValidationid").get("primaryquestion"));
            validationRule.put("description", outputArray.getJSONObject(i).getJSONObject("validationformByValidationid").get("description"));
            validationRule.put("name", outputArray.getJSONObject(i).getJSONObject("validationformByValidationid").getJSONObject("validationruleByRule").get("name"));
            validationRule.put("validationmessage", outputArray.getJSONObject(i).getJSONObject("validationformByValidationid").getJSONObject("validationruleByRule").get("validationmessage"));
            valOutputArray.put(validationRule);
        }
        var validationOutputs = new JSONObject().put("validation_outputs", valOutputArray);
        return validationOutputs;
    }

    public JSONObject buildDelayResponseDict(response) {
        var outputArray = new JSONArray();
        var delayResponseArray = new JSONArray();
        if (response.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").length() > 0) {
            outputArray = response.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes");
        }
        for (int i = 0; i < outputArray.length(); i++) {
            JSONObject delayResponseElement = new JSONObject();
            delayResponseElement.put("survey", outputArray.getJSONObject(i).get("survey"));
            delayResponseElement.put("period", outputArray.getJSONObject(i).get("period"));
            delayResponseArray.put(delayResponseElement);
        }
        return delayResponseArray;
    }
}


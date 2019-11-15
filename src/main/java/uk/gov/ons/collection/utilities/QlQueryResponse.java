package uk.gov.ons.collection.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import lombok.extern.log4j.Log4j2;

@Log4j2
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

    public JSONArray getResponses() {
        var outputArray = new JSONArray();
        if (jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").length() > 0) {
            outputArray = jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").getJSONObject(0)
                                        .getJSONObject("responsesByReferenceAndPeriodAndSurvey").getJSONArray("nodes");
        }
        return outputArray;        
    }

    public JSONObject getContributors() {
        JSONObject output = new JSONObject();
        if (jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").length() > 0) {
            output = jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").getJSONObject(0);
            output.remove("surveyBySurvey");
            output.remove("formByFormid");
            output.remove("responsesByReferenceAndPeriodAndSurvey");
        }
        return output;              
    }

    public JSONArray getForm(String survey, String period) {
        var outputArray = new JSONArray();
        if (jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").length() > 0) {
            outputArray = jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").getJSONObject(0)
                                        .getJSONObject("formByFormid").getJSONObject("formdefinitionsByFormid").getJSONArray("nodes");
        }
        for (int i = 0; i < outputArray.length(); i++) {
            outputArray.getJSONObject(i).put("survey",survey);
            outputArray.getJSONObject(i).put("period",period);
        }
        return outputArray;
    }

    public JSONArray parseValidationConfig() {                      
        var outputArray = new JSONArray();        
        if (jsonQlResponse.getJSONObject("data").getJSONObject("allValidationrules").getJSONArray("nodes").length() > 0) {
            outputArray = jsonQlResponse.getJSONObject("data").getJSONObject("allValidationrules").getJSONArray("nodes");
        }
        return outputArray;

    }

    public String getViewForm() {
        var outputObject = new JSONObject();
        var formArray = new JSONArray();
        var responseArray = new JSONArray();
        if (jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").length() > 0){
            formArray = jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").getJSONObject(0)
                          .getJSONObject("formByFormid").getJSONObject("formdefinitionsByFormid").getJSONArray("nodes");
            responseArray = jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").getJSONObject(0)
            .getJSONObject("responsesByReferenceAndPeriodAndSurvey").getJSONArray("nodes");              
        }   
        outputObject.put("responses", responseArray);
        outputObject.put("form_data", formArray);
        return outputObject.toString();

    }

    public String combineFormAndResponse(){
        var outputObject = new JSONObject();
        var outputArray = new JSONArray();
        var inputString = getViewForm();
        JSONObject inputObject;
        JSONArray formArray = new JSONArray();
        JSONArray responseArray = new JSONArray();
        try {
            inputObject = new JSONObject(inputString);
            formArray = inputObject.getJSONArray("form_data");
            responseArray = inputObject.getJSONArray("responses");
        } catch (Exception e) {
            inputString = "{}";
            inputObject = new JSONObject(inputString);
        }
        for(int i = 0; i < formArray.length(); i++){
            var rowObject = new JSONObject();
            for(int j = 0; j < responseArray.length(); j++){
                if(formArray.getJSONObject(i).getString("questioncode").equals(responseArray.getJSONObject(j).getString("questioncode"))) {
                    rowObject.put("questioncode", formArray.getJSONObject(i).getString("questioncode"));
                    rowObject.put("displaytext", formArray.getJSONObject(i).getString("displaytext"));
                    rowObject.put("displayquestionnumber", formArray.getJSONObject(i).getString("displayquestionnumber"));
                    rowObject.put("type", formArray.getJSONObject(i).getString("type"));
                    rowObject.put("response", responseArray.getJSONObject(j).getString("response"));
                    rowObject.put("instance", responseArray.getJSONObject(j).getInt("instance"));
                }
            }
            outputArray.put(rowObject);
        }
        outputObject.put("view_form_responses", outputArray);
        log.info("output array.tostring" + outputObject.toString());
        return outputObject.toString();
    }

}

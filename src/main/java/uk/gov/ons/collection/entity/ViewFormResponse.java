package uk.gov.ons.collection.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import lombok.extern.log4j.Log4j2;
import uk.gov.ons.collection.exception.InvalidJsonException;

@Log4j2
public class ViewFormResponse {
    private JSONObject jsonQlResponse;

    public ViewFormResponse(String inputJSON) throws InvalidJsonException {
        try {
            jsonQlResponse = new JSONObject(inputJSON);
        } catch (JSONException e) {
            log.info("exception" + e.toString());
            throw new InvalidJsonException("Given string could not be converted/processed: " + e);
        }
    }
    public String parseViewForm() {
        log.info("Form response" + jsonQlResponse.toString());
        var outputObject = new JSONObject();
        var formArray = new JSONArray();
        if (jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").length() > 0){
            formArray = jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").getJSONObject(0)
                        .getJSONObject("formByFormid").getJSONObject("formdefinitionsByFormid").getJSONArray("nodes");             
        }   
        outputObject.put("form_data", formArray);
        log.info("Form data object" + outputObject.toString());
        return outputObject.toString();

    }

    public String parseResponseData() {
        log.info("Responses" + jsonQlResponse.toString());
        var responseArray = new JSONArray();
        var outputObject = new JSONObject();
        if (jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").length() > 0){
            responseArray = jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").getJSONObject(0)
            .getJSONObject("responsesByReferenceAndPeriodAndSurvey").getJSONArray("nodes"); 
        }
        outputObject.put("responses", responseArray);
        log.info("Response data object" + outputObject.toString());
        return outputObject.toString();
    }

    public JSONArray extractFormData() throws InvalidJsonException {
        var inputString = parseViewForm();
        JSONObject inputObject;
        JSONArray formArray = new JSONArray();
        var outputFormArray = new JSONArray();
        try {
            inputObject = new JSONObject(inputString);
            formArray = inputObject.getJSONArray("form_data");
        } catch (JSONException e) {
            log.info("exception" + e.toString());
            throw new InvalidJsonException("Given string could not be converted/processed: " + e);
        }
        for(int i = 0; i < formArray.length(); i++){
            var formObject = new JSONObject();
            formObject.put("questioncode", formArray.getJSONObject(i).getString("questioncode"));
            formObject.put("displaytext", formArray.getJSONObject(i).getString("displaytext"));
            formObject.put("displayquestionnumber", formArray.getJSONObject(i).getString("displayquestionnumber"));
            formObject.put("displayorder", formArray.getJSONObject(i).getInt("displayorder"));
            formObject.put("type", formArray.getJSONObject(i).getString("type"));
            formObject.put("response", "");
            formObject.put("adjustedresponse", "");
            formObject.put("instance", "");

            outputFormArray.put(formObject);
        }
        return outputFormArray;
    }

    public String combineFormAndResponseData() throws InvalidJsonException {
        var outputObject = new JSONObject();
        var outputFormArray = extractFormData();
        var inputString = parseResponseData();
        JSONObject inputObject;
        JSONArray responseArray = new JSONArray();
        try {
            inputObject = new JSONObject(inputString);
            responseArray = inputObject.getJSONArray("responses");
        } catch (JSONException e) {
            log.info("exception" + e.toString());
            throw new InvalidJsonException("Given string could not be converted/processed: " + e);
        }
        for(int i = 0; i < outputFormArray.length(); i++){
            for(int j = 0; j < responseArray.length(); j++){
                if(outputFormArray.getJSONObject(i).getString("questioncode").equals(responseArray.getJSONObject(j).getString("questioncode"))) {
                    //Adding
                    if(responseArray.getJSONObject(j).isNull("response")){
                        outputFormArray.getJSONObject(i).put("response", "");
                    }
                    else{
                        outputFormArray.getJSONObject(i).put("response", responseArray.getJSONObject(j).getString("response"));
                    }
                    //End of Adding
                    //outputFormArray.getJSONObject(i).put("response", responseArray.getJSONObject(j).getString("response"));
                    if(responseArray.getJSONObject(j).isNull("adjustedresponse")){
                        outputFormArray.getJSONObject(i).put("adjustedresponse", "");
                    }
                    else{
                        outputFormArray.getJSONObject(i).put("adjustedresponse", responseArray.getJSONObject(j).getString("adjustedresponse"));
                    }
                    outputFormArray.getJSONObject(i).put("instance", responseArray.getJSONObject(j).getInt("instance"));
                }
            }
        }

        outputObject.put("view_form_responses", outputFormArray);
        log.info("view_form_responses.tostring" + outputObject.toString());
        return outputObject.toString();
    }
}
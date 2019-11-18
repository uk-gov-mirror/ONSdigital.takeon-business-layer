
package uk.gov.ons.collection.entity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ViewFormResponse {
    private JSONObject jsonQlResponse;

public ViewFormResponse(String inputJSON) {
	}

public String parseViewForm() {
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
    log.info("output array.tostring" + outputObject.toString());
    return outputObject.toString();

}

private JSONArray extractFormData(){
    var inputString = parseViewForm();
    JSONObject inputObject;
    JSONArray formArray = new JSONArray();
    var outputFormArray = new JSONArray();
    try {
        inputObject = new JSONObject(inputString);
        formArray = inputObject.getJSONArray("form_data");
    } catch (Exception e) {
        inputString = "{}";
        inputObject = new JSONObject(inputString);
    }
    for(int i = 0; i < formArray.length(); i++){
        var formObject = new JSONObject();
        formObject.put("questioncode", formArray.getJSONObject(i).getString("questioncode"));
        formObject.put("displaytext", formArray.getJSONObject(i).getString("displaytext"));
        formObject.put("displayquestionnumber", formArray.getJSONObject(i).getString("displayquestionnumber"));
        formObject.put("type", formArray.getJSONObject(i).getString("type"));
        formObject.put("response", "");
        formObject.put("instance", "");

        outputFormArray.put(formObject);
    }
    return outputFormArray;
}

public String combineFormAndResponseData() {
    var outputObject = new JSONObject();
    var outputFormArray = extractFormData();
    var inputString = parseViewForm();
    JSONObject inputObject;
    JSONArray responseArray = new JSONArray();
    try {
        inputObject = new JSONObject(inputString);
        responseArray = inputObject.getJSONArray("responses");
    } catch (Exception e) {
        inputString = "{}";
        inputObject = new JSONObject(inputString);
    }
    for(int i = 0; i < outputFormArray.length(); i++){
        for(int j = 0; j < responseArray.length(); j++){
            if(outputFormArray.getJSONObject(i).getString("questioncode").equals(responseArray.getJSONObject(j).getString("questioncode"))); {
                outputFormArray.getJSONObject(i).put("response", responseArray.getJSONObject(j).getString("response"));
                outputFormArray.getJSONObject(i).put("instance", responseArray.getJSONObject(j).getInt("instance"));
            }
        }
    }

    outputObject.put("view_form_responses", outputFormArray);
    log.info("output array.tostring" + outputObject.toString());
    return outputObject.toString();
}

public Object parse() {
	return null;
}
}

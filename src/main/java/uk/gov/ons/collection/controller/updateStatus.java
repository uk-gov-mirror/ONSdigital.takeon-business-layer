package uk.gov.ons.collection.controller;

import org.json.JSONArray;
import org.json.JSONObject;

public class updateStatus {

    private String response = "{\"validationoutput\":[{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"true\",\"validationid\":\"\",\"bpmid\":\"\"}]}";
    private JSONObject jsonResponse;

    public boolean determineStatus(String response) {
        JSONObject jsonResponse =  new JSONObject(response);
        JSONArray array = jsonResponse.getJSONArray("validationoutput");
        for (int i=0; i < array.length(); i++) {
            Object triggered = array.getJSONObject(i).get("triggered");
            if(triggered.equals("true")){
                return true;
            }
        }
        return false;
    }

    public String updateStatusQuery(String response){
        JSONObject jsonResponse =  new JSONObject(response);
        JSONObject validationOutput = jsonResponse.getJSONArray("validationoutput").getJSONObject(0);
        String reference = validationOutput.get("reference").toString();
        String period = validationOutput.get("period").toString();
        String survey = validationOutput.get("survey").toString();
        String queryPrefix = "{\"query\" : \"mutation updateStatus {updateContributorByReferenceAndPeriodAndSurvey(input: {";
        String updateQuery = "reference: \\\"" + reference + "\\\", period: \\\"" + period + "\\\", survey: \\\"" + survey + "\\\"";
        String querySuffix = ", contributorPatch: {status: \\\"ValidationsTriggered\\\"}}) {clientMutationId}}\"}";
        StringBuilder updateStatusQuery = new StringBuilder();
        updateStatusQuery.append(queryPrefix).append(updateQuery).append(querySuffix);
        return updateStatusQuery.toString();
    }
}
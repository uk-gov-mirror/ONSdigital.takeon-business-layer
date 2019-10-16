package uk.gov.ons.collection.controller;

import org.json.JSONArray;
import org.json.JSONObject;

public class deleteInsertValidationOutput {

    String reference;
    String period;
    String survey;

    private String response = "{\"validationoutput\":[{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"true\",\"validationid\":\"\",\"bpmid\":\"\"}]}";
    private JSONObject jsonResponse;

    public String buildDeleteOutputQuery(String response){
        JSONObject jsonResponse =  new JSONObject(response);
        JSONObject validationOutput = jsonResponse.getJSONArray("validationoutput").getJSONObject(0);
        reference = validationOutput.get("reference").toString();
        period = validationOutput.get("period").toString();
        survey = validationOutput.get("survey").toString();
        String queryPrefix = "{\"query\" : \"mutation deleteOutput{deleteoutput(input: {";
        String deleteOutput = "reference:\\\"" + reference + "\\\",period:\\\"" + period + "\\\",survey:\\\""
                + survey + "\\\"";
        String querySuffix = "}){clientMutationId}}\"}";
        StringBuilder deleteQuery = new StringBuilder();
        deleteQuery.append(queryPrefix).append(deleteOutput).append(querySuffix);
        return deleteQuery.toString();
    }

    public String buildInsertByArrayQuery(String response){
        JSONObject jsonResponse =  new JSONObject(response);
        JSONObject validationOutput = jsonResponse.getJSONArray("validationoutput").getJSONObject(0);
        reference = validationOutput.get("reference").toString();
        period = validationOutput.get("period").toString();
        survey = validationOutput.get("survey").toString();
        String validationid = validationOutput.get("validationid").toString();
        String instance = "0";
        String triggered = validationOutput.get("triggered").toString();
        String formula = validationOutput.get("formula").toString();
        String createdby = "User";
        String createddate = "2016-06-22 22:10:25-04";

        String queryPrefix = "{\"query\" : \"mutation insertOutputArray{insertvalidationoutputbyarray(input: {arg0:[{";
        String deleteOutput = "reference:\\\"" + reference + "\\\",period:\\\"" + period + "\\\",survey:\\\""
                + survey + "\\\",validationid:\\\"" + validationid + "\\\",instance:\\\"" + instance
                + "\\\",triggered:\\\"" + triggered + "\\\",formula:\\\"" + formula + "\\\", createdby:\\\""
                + createdby + "\\\", createddate:\\\"" + createddate + "\\\"";
        String querySuffix = "}]}){clientMutationId}}\"}";
        StringBuilder insertQuery = new StringBuilder();
        insertQuery.append(queryPrefix).append(deleteOutput).append(querySuffix);
        return insertQuery.toString();
    }

}

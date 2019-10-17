package uk.gov.ons.collection.controller;

import java.sql.Timestamp;
import java.util.Date;
import java.util.StringJoiner;

import org.json.JSONException;
import org.json.JSONObject;

import uk.gov.ons.collection.exception.InvalidJsonException;

public class deleteInsertValidationOutput {

    private final String reference;
    private final String period;
    private final String survey;
    private final Timestamp time = new Timestamp(new Date().getTime());

    //private String response = "{\"validationoutput\":[{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"true\",\"validationid\":\"\",\"bpmid\":\"\"}]}";
    private final JSONObject validationOutputs;
    private final String qlDeleteQuery = "mutation deleteOutput($period: String!, $reference: String!, $survey: String!)" + 
        "{deleteoutput(input: {reference: $reference, period: $period, survey: $survey}){clientMutationId}}";

    public deleteInsertValidationOutput(String reference, String period, String survey, String jsonString) throws InvalidJsonException {
        this.reference = reference;
        this.period = period;
        this.survey = survey;
        try {
            validationOutputs = new JSONObject(jsonString);
        }
        catch (JSONException err) {
            throw new InvalidJsonException("Given string could not be converted/processed: " + jsonString, err);
        }
    }

    public String buildDeleteOutputQuery(String response) {
        var queryJson = new StringBuilder();
        queryJson.append("{\"query\": \"" + qlDeleteQuery + "\",\"variables\": {\"reference\": \"" + reference + "\",\"period\": \"" + period + "\",\"survey\": \"" + survey + "\"})");
        return queryJson.toString();
    }

    public String buildInsertByArrayQuery(String response) throws InvalidJsonException {

        var queryJson = new StringBuilder();
        queryJson.append("{\"query\": \"mutation insertOutputArray{insertvalidationoutputbyarray(input: {arg0:");
        queryJson.append("[" + getValidationOutputs() + "]");
        queryJson.append("}){clientMutationId}}\"}");
        return "";
    }

    // Loop through the given validation output array json and convert it into a graphQL compatable format
    public String getValidationOutputs() throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");
        try {
            var outputArray = validationOutputs.getJSONArray("validationOutputs");
            for (int i=0; i < outputArray.length(); i++) {
                joiner.add("{" + extractValidationOutputRow(i) + "}" );
            }
            return joiner.toString();
        }
        catch (Exception err) {
            throw new InvalidJsonException("Error processing validation output json structure: " + validationOutputs, err);
        }
    }

    // Convert a row for the given index and provide it in graphQL desired format
    public String extractValidationOutputRow(int index) throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");
        try {
            var outputRow = validationOutputs.getJSONArray("validationOutputs").getJSONObject(index);
            joiner.add("reference: \"" + outputRow.getString("reference") + "\"");   
            joiner.add("period: \"" + outputRow.getString("period") + "\"");
            joiner.add("survey: \"" + outputRow.getString("survey") + "\"");
            joiner.add("formula: \"" + outputRow.getString("formula") + "\"");
            joiner.add("validationid: \"" + outputRow.getString("validationid") + "\"");
            joiner.add("instance: \"" + outputRow.getString("instance") + "\"");
            joiner.add("triggered: \"" + outputRow.getString("triggered") + "\"");
            joiner.add("createdby: \"fisdba\"");
            joiner.add("createddate: \"" + time.toString() + "\"");
            return joiner.toString();
        }
        catch (Exception err) {
            throw new InvalidJsonException("Error processing validation output json structure: " + validationOutputs, err);
        }
    }
}

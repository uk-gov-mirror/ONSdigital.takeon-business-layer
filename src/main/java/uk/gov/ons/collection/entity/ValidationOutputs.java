package uk.gov.ons.collection.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.StringJoiner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.gov.ons.collection.exception.InvalidJsonException;

public class ValidationOutputs {

    private JSONArray outputArray;
    private final Timestamp time = new Timestamp(new Date().getTime());
    private final String qlDeleteQuery = "mutation deleteOutput($period: String!, $reference: String!, $survey: String!)" +
                                         "{deleteoutput(input: {reference: $reference, period: $period, survey: $survey}){clientMutationId}}";
    
    public ValidationOutputs(String jsonString) throws InvalidJsonException {
        try {
            outputArray = new JSONObject(jsonString).getJSONArray("validation_outputs");
        }
        catch (JSONException err) {
            throw new InvalidJsonException("Given string could not be converted/processed: " + jsonString, err);
        }
    }

    public String buildDeleteOutputQuery() throws InvalidJsonException {
        var queryJson = new StringBuilder();
        queryJson.append("{\"query\": \""); 
        queryJson.append(qlDeleteQuery); 
        queryJson.append("\",\"variables\":{\"reference\": \"" + getReference() + "\",\"period\": \"" + getPeriod() + "\",\"survey\": \"" + getSurvey() + "\"}}");
        return queryJson.toString();
    }

    public String buildInsertByArrayQuery() throws InvalidJsonException {
        var queryJson = new StringBuilder();
        queryJson.append("{\"query\": \"mutation insertOutputArray{insertvalidationoutputbyarray(input: {arg0:");
        queryJson.append("[" + getValidationOutputs() + "]");
        queryJson.append("}){clientMutationId}}\"}");
        return queryJson.toString();
    }

    // Loop through the given validation output array json and convert it into a graphQL compatable format
    private String getValidationOutputs() throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");
        for (int i=0; i < outputArray.length(); i++) {
            joiner.add("{" + extractValidationOutputRow(i) + "}" );
        }
        return joiner.toString();
    }

    public String getTime() {
        return time.toString();
    }

    // Convert a row for the given index and provide it in graphQL desired format
    private String extractValidationOutputRow(int index) throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");
        try {
            var outputRow = outputArray.getJSONObject(index);
            joiner.add("reference: \\\"" + outputRow.getString("reference") + "\\\"");   
            joiner.add("period: \\\"" + outputRow.getString("period") + "\\\"");
            joiner.add("survey: \\\"" + outputRow.getString("survey") + "\\\"");
            joiner.add("formula: \\\"" + outputRow.getString("formula") + "\\\"");
            joiner.add("validationid: \\\"" + outputRow.getInt("validationid") + "\\\"");
            joiner.add("instance: \\\"" + outputRow.getInt("instance") + "\\\"");
            joiner.add("triggered: " + outputRow.getBoolean("triggered"));
            joiner.add("createdby: \\\"fisdba\\\"");
            joiner.add("createddate: \\\"" + time.toString() + "\\\"");
            return joiner.toString();
        }
        catch (Exception err) {
            throw new InvalidJsonException("Error processing validation output json structure: " + outputArray, err);
        }
    }

    private String getFirstRowAttribute(String attribute) throws InvalidJsonException {
        try {   
            return outputArray.getJSONObject(0).getString(attribute);
        }
        catch (Exception err) {
            throw new InvalidJsonException("Given JSON did not contain " + attribute + " in the expected location: " + outputArray, err);
        } 
    }

    public String getReference() throws InvalidJsonException {
        return getFirstRowAttribute("reference");
    }

    public String getPeriod() throws InvalidJsonException {
        return getFirstRowAttribute("period");
    }

    public String getSurvey() throws InvalidJsonException {
        return getFirstRowAttribute("survey");
    }

    public String getStatusText() throws InvalidJsonException {
        if (isTriggeredFound()) {
            return "Validations Triggered";
        }
        return "Clear";
    }

    private boolean isTriggeredFound() throws InvalidJsonException {
        try {    
            for (int i=0; i < outputArray.length(); i++) {
                if (outputArray.getJSONObject(i).getBoolean("triggered")){
                    return true;
                }
            }
        }
        catch (Exception err) {
            throw new InvalidJsonException("Given JSON did not contain triggered in the expected location(s): " + outputArray, err);
        } 
        return false;
    }

}



// mutation insertOutputArray {
//   insertvalidationoutputbyarray(input: {arg0: [
//     {reference: "123", period: "123", survey: "123", formula: "123", validationid: "12", instance:"0",
//     triggered: true, createdby:"testuser",createddate:"2016-06-22 22:10:25-04"},
//     {reference: "123", period: "123", survey: "123", formula: "123", validationid: "12", instance:"0",
//     triggered: true, createdby:"testuser",createddate:"2016-06-22 22:10:25-04"}
//   ]}
//   ) {
//     clientMutationId
//   }
// }


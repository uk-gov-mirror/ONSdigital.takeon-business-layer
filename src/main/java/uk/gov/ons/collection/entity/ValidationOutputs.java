package uk.gov.ons.collection.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.gov.ons.collection.exception.InvalidJsonException;

@Log4j2
public class ValidationOutputs {

    private JSONArray outputArray;
    private final Timestamp time = new Timestamp(new Date().getTime());
    private final String qlDeleteQuery = "mutation deleteOutput($period: String!, $reference: String!, $survey: String!)" +
                                         "{deleteoutput(input: {reference: $reference, period: $period, survey: $survey}){clientMutationId}}";
    
    public ValidationOutputs(String jsonString) throws InvalidJsonException {
        try {
            outputArray = new JSONObject(jsonString).getJSONArray("validation_outputs");
        } catch (JSONException err) {
            throw new InvalidJsonException("Given string could not be converted/processed: " + jsonString, err);
        }
    }

    public String buildValidationOutputQuery() throws InvalidJsonException {
        StringBuilder referenceQuery = new StringBuilder();
        referenceQuery.append("{\"query\":\"query validationoutputdata {");
        referenceQuery.append("allValidationoutputs(condition: {");
        referenceQuery.append(getReferencePeriodSurvey());
        referenceQuery.append("}){nodes {reference period survey formula validationid triggered overridden ");
        referenceQuery.append("}}}\"}");
        log.info("Validation Output query {}", referenceQuery.toString());
        return referenceQuery.toString();
    }

    public String buildDeleteOutputQuery() throws InvalidJsonException {
        var queryJson = new StringBuilder();
        queryJson.append("{\"query\": \""); 
        queryJson.append(qlDeleteQuery); 
        queryJson.append("\",\"variables\":" + 
            "{\"reference\": \"" + getReference() + "\",\"period\": \"" + getPeriod() + "\",\"survey\": \"" + getSurvey() + "\"}}");
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
        for (int i = 0; i < outputArray.length(); i++) {
            joiner.add("{" + extractValidationOutputRow(i) + "}");
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
            joiner.add("formula: \\\"" + outputRow.getString("formula").replace("\"","'") + "\\\"");
            joiner.add("validationid: \\\"" + outputRow.getInt("validationid") + "\\\"");
            joiner.add("instance: \\\"" + outputRow.getInt("instance") + "\\\"");
            joiner.add("triggered: " + outputRow.getBoolean("triggered"));
            joiner.add("createdby: \\\"fisdba\\\"");
            joiner.add("createddate: \\\"" + time.toString() + "\\\"");
            return joiner.toString();

        } catch (Exception err) {
            throw new InvalidJsonException("Error processing validation output json structure: " + err + " JSON: " + outputArray, err);
        }
    }

    public List<ValidationOutputData> extractValidationDataFromDatabase(String validationOutputResponse) throws InvalidJsonException {

        List<ValidationOutputData> validationDataList = new ArrayList<ValidationOutputData>();
        JSONArray validationOutputArray;
        try {
            JSONObject referenceExistsObject = new JSONObject(validationOutputResponse);
            validationOutputArray = referenceExistsObject.getJSONObject("data")
                    .getJSONObject("allValidationoutputs").getJSONArray("nodes");
            for (int i = 0; i < validationOutputArray.length(); i++) {
                ValidationOutputData validationData = new ValidationOutputData();
                validationData.setValidationOutputId(validationOutputArray.getJSONObject(i).getInt("validationoutputid"));
                validationData.setOverridden(validationOutputArray.getJSONObject(i).getBoolean("overridden"));
                validationData.setTriggered(validationOutputArray.getJSONObject(i).getBoolean("triggered"));
                validationData.setFormula(validationOutputArray.getJSONObject(i).getString("formula"));
                validationData.setValidationId(validationOutputArray.getJSONObject(i).getInt("validationid"));
                validationDataList.add(validationData);
            }
        } catch (JSONException e) {
            log.error("Invalid JSON from validation output query response " + e);
            throw new InvalidJsonException("Invalid JSON from validation output query response: " + validationOutputResponse, e);
        }
        return validationDataList;
    }

    public List<ValidationOutputData> extractValidationDataFromLambda() throws InvalidJsonException {

        List<ValidationOutputData> validationLambdaList = new ArrayList<ValidationOutputData>();
        try {
            for (int i = 0; i < outputArray.length(); i++) {
                var outputRow = outputArray.getJSONObject(i);
                ValidationOutputData validationLambdaData = new ValidationOutputData();
                validationLambdaData.setReference(outputRow.getString("reference"));
                validationLambdaData.setPeriod(outputRow.getString("period"));
                validationLambdaData.setSurvey(outputRow.getString("survey"));
                validationLambdaData.setFormula(outputRow.getString("formula"));
                validationLambdaData.setValidationId(outputRow.getInt("validationid"));
                validationLambdaData.setInstance(outputRow.getInt("instance"));
                validationLambdaData.setTriggered(outputRow.getBoolean("triggered"));
                validationLambdaList.add(validationLambdaData);
            }

        } catch (JSONException e) {
            log.error("Invalid JSON from validation output query response " + e);
            throw new InvalidJsonException("Invalid JSON from validation output query response: " + outputArray, e);
        }
        return validationLambdaList;
    }

    private String getFirstRowAttribute(String attribute) throws InvalidJsonException {
        try {   
            return outputArray.getJSONObject(0).getString(attribute);
        } catch (Exception err) {
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
            for (int i = 0; i < outputArray.length(); i++) {
                if (outputArray.getJSONObject(i).getBoolean("triggered")) {
                    return true;
                }
            }
        } catch (Exception err) {
            throw new InvalidJsonException("Given JSON did not contain triggered in the expected location(s): " + outputArray, err);
        } 
        return false;
    }

    private String getReferencePeriodSurvey() throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");
        joiner.add("reference: \\\"" + getReference() + "\\\"");
        joiner.add("period: \\\""    + getPeriod()    + "\\\"");
        joiner.add("survey: \\\""    + getSurvey()    + "\\\"");
        return joiner.toString();
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


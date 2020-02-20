package uk.gov.ons.collection.entity;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

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
        referenceQuery.append("}){nodes {reference period survey formula validationid triggered overridden instance ");
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

    public String buildUpsertByArrayQuery(List<ValidationOutputData> upsertData, List<ValidationOutputData> deleteData) throws InvalidJsonException {
        var queryJson = new StringBuilder();
        queryJson.append("{\"query\": \"mutation upsertOutputArray{upsertDeleteValidationoutput(input: {arg0:");
        queryJson.append("[" + getValidationOutputsForUpsertAndDelete(upsertData) + "], arg1:");
        queryJson.append("[" + getValidationOutputsForUpsertAndDelete(deleteData) + "]");
        queryJson.append("}){clientMutationId}}\"}");
        return queryJson.toString();
    }

    private String getValidationOutputsForUpsertAndDelete(List<ValidationOutputData> validationOutputData) throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");
        for (ValidationOutputData data : validationOutputData) {
            joiner.add("{" + extractValidationOutputRowForUpsertAndDelete(data) + "}");
        }
        return joiner.toString();
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
    private String extractValidationOutputRowForUpsertAndDelete(ValidationOutputData data) throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");
        try {

            joiner.add("reference: \\\"" + data.getReference() + "\\\"");
            joiner.add("period: \\\"" + data.getPeriod() + "\\\"");
            joiner.add("survey: \\\"" + data.getSurvey() + "\\\"");
            joiner.add("formula: \\\"" + data.getFormula() + "\\\"");
            joiner.add("validationid: \\\"" + data.getValidationId() + "\\\"");
            joiner.add("instance: \\\"" + data.getInstance() + "\\\"");
            joiner.add("triggered: " + data.isTriggered());
            joiner.add("overridden: " + data.isOverridden());
//            joiner.add("createdby: \\\"" + data.getCreatedBy() + "\\\"");
//            joiner.add("createddate: \\\"" + (data.getCreatedDate() == null ? "" : data.getCreatedDate()) + "\\\"");
//            joiner.add("lastupdatedby: \\\"" + data.getLastupdatedBy() + "\\\"");
//            joiner.add("lastupdateddate: \\\"" + (data.getLastupdatedDate() == null ? "" : data.getLastupdatedDate())  + "\\\"");
            joiner.add("createdby: \\\"fisdba\\\"");
            joiner.add("createddate: \\\"" + time.toString() + "\\\"");
            joiner.add("lastupdatedby: \\\"fisdba\\\"");
            joiner.add("lastupdateddate: \\\"" + time.toString() + "\\\"");
            return joiner.toString();

        } catch (Exception err) {
            throw new InvalidJsonException("Error processing validation output json structure: " + err + " JSON: " + outputArray, err);
        }
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
                validationData.setOverridden(validationOutputArray.getJSONObject(i).getBoolean("overridden"));
                validationData.setTriggered(validationOutputArray.getJSONObject(i).getBoolean("triggered"));
                validationData.setFormula(validationOutputArray.getJSONObject(i).getString("formula"));
                validationData.setValidationId(validationOutputArray.getJSONObject(i).getInt("validationid"));
                validationData.setInstance(validationOutputArray.getJSONObject(i).getInt("instance"));
                validationData.setReference(validationOutputArray.getJSONObject(i).getString("reference"));
                validationData.setPeriod(validationOutputArray.getJSONObject(i).getString("period"));
                validationData.setSurvey(validationOutputArray.getJSONObject(i).getString("survey"));
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
                validationLambdaData.setFormula(outputRow.getString("formula").replace("\"","'"));
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


    public List<ValidationOutputData> getUpsertAndInsertValidationOutputList(List<ValidationOutputData> validationLambdaList,
                                                                                List<ValidationOutputData> validationDataList) {
        List<ValidationOutputData> insertedList = validationLambdaList.stream().filter(lambdadata -> validationDataList.stream().noneMatch(validationdata ->
                (validationdata.getValidationId().equals(lambdadata.getValidationId())))).collect(Collectors.toList());
        List<ValidationOutputData> modifiedList = validationLambdaList.stream().filter(lambdadata -> validationDataList.stream().anyMatch(validationdata ->
                (validationdata.getValidationId().equals(lambdadata.getValidationId()) && !(validationdata.getFormula().equals(lambdadata.getFormula()))))).collect(Collectors.toList());
        modifiedList.addAll(insertedList);
        System.out.println("Final List containing both update and insert :" + modifiedList.toString());
        return modifiedList;
    }

    public List<ValidationOutputData> getDeleteValidationOutputList(List<ValidationOutputData> validationLambdaList,
                                                                             List<ValidationOutputData> validationDataList) {
        List<ValidationOutputData> deletedList = validationDataList.stream().filter(validationdata -> validationLambdaList.stream().noneMatch(lambdadata ->
                (lambdadata.getValidationId().equals(validationdata.getValidationId())))).collect(Collectors.toList());
        System.out.println("Deleted List :" + deletedList.toString());
        return deletedList;
    }




    public List<ValidationOutputData> getConsolidatedUpsertValidationOutputList(List<ValidationOutputData> validationLambdaList,
        List<ValidationOutputData> validationDataList) {

        List<ValidationOutputData> finalValidationList = new ArrayList<ValidationOutputData>();

        List<ValidationOutputData> insertedList = validationLambdaList.stream().filter(lambdadata -> validationDataList.stream().noneMatch(validationdata ->
                (validationdata.getValidationId().equals(lambdadata.getValidationId())))).collect(Collectors.toList());
        insertedList.forEach(insertBy -> insertBy.setCreatedBy("fisdba"));
        insertedList.forEach(insertDate -> insertDate.setCreatedDate(time.toString()));


        List<ValidationOutputData> matchedList = validationLambdaList.stream().filter(lambdadata -> validationDataList.stream().anyMatch(validationdata ->
                (validationdata.getValidationId().equals(lambdadata.getValidationId())))).collect(Collectors.toList());

        List<ValidationOutputData> formulaEqualList = matchedList.stream().filter(matcheddata -> validationDataList.stream().anyMatch(validationdata ->
                (validationdata.getValidationId().equals(matcheddata.getValidationId())  && validationdata.getFormula().equals(matcheddata.getFormula())))).collect(Collectors.toList());

        List<ValidationOutputData> comparisonList = matchedList.stream().filter(matcheddata -> formulaEqualList.stream().noneMatch(formulaequaldata ->
                (formulaequaldata.getValidationId().equals(matcheddata.getValidationId())))).collect(Collectors.toList());

        //Test
        List<ValidationOutputData> modifiedList = validationLambdaList.stream().filter(lambdadata -> validationDataList.stream().anyMatch(validationdata ->
                (validationdata.getValidationId().equals(lambdadata.getValidationId()) && !(validationdata.getFormula().equals(lambdadata.getFormula()))))).collect(Collectors.toList());
        modifiedList.forEach(modifiedBy -> modifiedBy.setLastupdatedBy("fisdba"));
        modifiedList.forEach(modifiedDate -> modifiedDate.setLastupdatedDate(time.toString()));
        modifiedList.addAll(insertedList);
        //End

        System.out.println("Inserted List :" + insertedList.toString());
        System.out.println("Matched List :" + matchedList.toString());

        System.out.println("Formula Equal List :" + formulaEqualList.toString());

        System.out.println("Updated List :" + comparisonList.toString());
        System.out.println("Final List containing both update and insert :" + modifiedList.toString());

        List<ValidationOutputData> deletedList = validationDataList.stream().filter(validationdata -> validationLambdaList.stream().noneMatch(lambdadata ->
                (lambdadata.getValidationId().equals(validationdata.getValidationId())))).collect(Collectors.toList());

        System.out.println("Deleted List :" + deletedList.toString());

        return finalValidationList;
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



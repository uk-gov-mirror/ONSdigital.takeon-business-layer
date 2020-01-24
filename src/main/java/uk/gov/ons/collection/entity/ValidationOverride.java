package uk.gov.ons.collection.entity;

import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uk.gov.ons.collection.exception.InvalidJsonException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

@Log4j2
public class ValidationOverride {

    private JSONArray validationOutputArray;
    private JSONObject validationOutputOverrideObject;
    private String reference;
    private String period;
    private String survey;
    private final Timestamp time = new Timestamp(new Date().getTime());

    public ValidationOverride(String jsonString) throws InvalidJsonException {
        try {
            validationOutputOverrideObject = new JSONObject(jsonString);
            validationOutputArray = validationOutputOverrideObject.getJSONArray("validation_outputs");
        } catch (JSONException err) {
            throw new InvalidJsonException("The Validation override json string cannot be processed: " + jsonString, err);
        }
    }

    public List<ValidationData> extractValidationDataFromUI() {
        reference = validationOutputOverrideObject.getString("reference");
        period = validationOutputOverrideObject.getString("period");
        survey = validationOutputOverrideObject.getString("survey");

        List<ValidationData> validationDataList = new ArrayList<ValidationData>();
        for (int i = 0; i < validationOutputArray.length(); i++) {
            ValidationData validationData = new ValidationData();
            validationData.setValidationOutputId(validationOutputArray.getJSONObject(i).getInt("validationoutputid"));
            validationData.setLastupdatedBy(validationOutputArray.getJSONObject(i).getString("user"));
            validationData.setOverridden(validationOutputArray.getJSONObject(i).getBoolean("override"));
            validationDataList.add(validationData);
        }
        return validationDataList;
    }

    public List<ValidationData> extractValidationDataFromDatabase(String validationOutputResponse) throws InvalidJsonException {

        List<ValidationData> validationDataList = new ArrayList<ValidationData>();
        JSONArray validationOutputArray;
        try {
            JSONObject referenceExistsObject = new JSONObject(validationOutputResponse);
            validationOutputArray = referenceExistsObject.getJSONObject("data")
                    .getJSONObject("allValidationoutputs").getJSONArray("nodes");
            for (int i = 0; i < validationOutputArray.length(); i++) {
                ValidationData validationData = new ValidationData();
                validationData.setValidationOutputId(validationOutputArray.getJSONObject(i).getInt("validationoutputid"));
                validationData.setOverridden(validationOutputArray.getJSONObject(i).getBoolean("overridden"));
                validationDataList.add(validationData);
            }
        } catch (JSONException e) {
            log.error("Invalid JSON from validation output query response " + e);
            throw new InvalidJsonException("Invalid JSON from validation output query response: " + validationOutputResponse, e);
        }
        return validationDataList;
    }

    public List<ValidationData> extractUpdatedValidationOutputData(List<ValidationData> validationUIList, List<ValidationData> validationDBList) {
        List<ValidationData> updatedList = new ArrayList<ValidationData>();

        for (ValidationData validationDBData : validationDBList) {
            for (ValidationData validationUIData : validationUIList) {
                if(validationDBData.getValidationOutputId().equals(validationUIData.getValidationOutputId())) {
                    if(validationUIData.isOverridden() != validationDBData.isOverridden()) {
                        validationDBData.setOverridden(validationUIData.isOverridden());
                        validationDBData.setLastupdatedBy(validationUIData.getLastupdatedBy());
                        validationDBData.setLastupdatedDate(time.toString());
                        updatedList.add(validationDBData);
                    }
                }
            }
        }
        return updatedList;
    }

    public String buildValidationOutputQuery() throws InvalidJsonException {
        StringBuilder referenceQuery = new StringBuilder();
        referenceQuery.append("{\"query\":\"query validationoutputinformation {");
        referenceQuery.append("allValidationoutputs(condition: {");
        referenceQuery.append(getReferencePeriodSurveyAndTriggered());
        referenceQuery.append("}){nodes {validationoutputid overridden ");
        referenceQuery.append("}}}\"}");
        log.info("Output of validationoutputinformation query {}", referenceQuery.toString());
        return referenceQuery.toString();
    }

    private String getReferencePeriodSurveyAndTriggered() {
        StringJoiner joiner = new StringJoiner(",");
        joiner.add("reference: \\\"" + reference + "\\\"");
        joiner.add("period: \\\""    + period    + "\\\"");
        joiner.add("survey: \\\""    + survey    + "\\\"");
        joiner.add("triggered: "    + true);
        return joiner.toString();
    }

    // Builds Batch Update query
    public String buildUpdateByArrayQuery(List<ValidationData> validationList)  {
        var queryJson = new StringBuilder();
        queryJson.append("{\"query\" : \"mutation updateValidation {updateValidationoutputMulti(input: {arg0: ");
        queryJson.append("[" + getValidationOutputs(validationList) + "]");
        queryJson.append("}){clientMutationId}}\"}");
        return queryJson.toString();
    }

    // Loop through the given validation output array list and convert it into a graphQL compatible format
    private String getValidationOutputs(List<ValidationData> validationList) {
        StringJoiner joiner = new StringJoiner(",");
        for (int i = 0; i < validationList.size(); i++) {
            joiner.add("{" + extractValidationOutputRow(validationList.get(i)) + "}");
        }
        return joiner.toString();
    }

    private String extractValidationOutputRow(ValidationData validationData)  {
        StringJoiner joiner = new StringJoiner(",");
        joiner.add("validationoutputid: " + validationData.getValidationOutputId());
        joiner.add("overridden: " + validationData.isOverridden());
        joiner.add("lastupdatedby: \\\"" + validationData.getLastupdatedBy() + "\\\"");
        joiner.add("lastupdateddate: \\\"" + validationData.getLastupdatedDate() + "\\\"");
        return joiner.toString();
    }

}

package uk.gov.ons.collection.utilities;

import uk.gov.ons.collection.exception.InvalidJsonException;
import uk.gov.ons.collection.entity.ContributorStatus;

import java.sql.Timestamp;
import java.util.Date;
import java.util.StringJoiner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpsertResponse {

    private JSONArray responseArray;
    private JSONObject responseObject;
    private final Timestamp time = new Timestamp(new Date().getTime());

    public UpsertResponse(String jsonString) throws InvalidJsonException {
        try {
            responseObject = new JSONObject(jsonString);
            responseArray = responseObject.getJSONArray("responses");

        } catch (JSONException err) {
            throw new InvalidJsonException("Given string could not be converted/processed: " + jsonString, err);
        }
    }

    // Builds query to retrieve old response data currently held in the database
    public String buildRetrieveOldResponseQuery() throws InvalidJsonException {
        var queryJson = new StringBuilder();
        queryJson.append("{\"query\" : \"query filteredResponse {allResponses(condition: ");
        queryJson.append(retrieveOldResponse());
        queryJson.append("}){nodes{reference,period,survey,questioncode,response," +
                "createdby,createddate,lastupdatedby,lastupdateddate}}}\"}");
        return queryJson.toString();
    }

    // Loops through json array to pull out data
    private String retrieveOldResponse() throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");
        for (int i = 0; i < responseArray.length(); i++) {
            joiner.add("{" + extractOldResponseRow(i));
        }
        return joiner.toString();
    }

    // Extracts values from json and stores and adds to StringJoiner
    private String extractOldResponseRow(int index) throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");
        try {
            var outputRow = responseObject;
            joiner.add("reference: \\\"" + outputRow.getString("reference") + "\\\"");
            joiner.add("period: \\\"" + outputRow.getString("period") + "\\\"");
            joiner.add("survey: \\\"" + outputRow.getString("survey") + "\\\"");

            return joiner.toString();
        } catch (Exception err) {
            throw new InvalidJsonException("Error processing response json structure: " + responseArray, err);
        }
    }

    // Uses contributor status class to build update query, takes reference period survey and status text as args
    public String updateContributorStatus() throws InvalidJsonException {
        try {
            var outputRow = responseObject;
            String reference = (outputRow.getString("reference"));
            String period = (outputRow.getString("period"));
            String survey = (outputRow.getString("survey"));
            String statusText = ("Form Saved");

            ContributorStatus status = new ContributorStatus(reference,period,survey,statusText);
            return status.buildUpdateQuery();
        } catch (Exception err) {
            throw new InvalidJsonException("Error processing update status json structure: " + responseObject, err);
        }

    }

    // Builds Upsert query
    public String buildUpsertByArrayQuery() throws InvalidJsonException {
        var queryJson = new StringBuilder();
        queryJson.append("{\"query\" : \"mutation saveResponse {saveresponsearray(input: {arg0: ");
        queryJson.append("[" + getResponseOutputs() + "]");
        queryJson.append("}){clientMutationId}}\"}");
        return queryJson.toString();
    }

    // Loop through the given validation output array json and convert it into a graphQL compatable format
    private String getResponseOutputs() throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");
        for (int i = 0; i < responseArray.length(); i++) {
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
            var outputRow = responseArray.getJSONObject(index);
            var refPerSur = responseObject;
            joiner.add("reference: \\\"" + refPerSur.getString("reference") + "\\\"");
            joiner.add("period: \\\"" + refPerSur.getString("period") + "\\\"");
            joiner.add("survey: \\\"" + refPerSur.getString("survey") + "\\\"");
            joiner.add("questioncode: \\\"" + outputRow.getString("questioncode") + "\\\"");
            joiner.add("instance: " + outputRow.getInt("instance"));
            joiner.add("response: \\\"" + outputRow.getString("response") + "\\\"");
            joiner.add("createdby: \\\"fisdba\\\"");
            joiner.add("createddate: \\\"" + time.toString() + "\\\"");
            return joiner.toString();
        } catch (Exception err) {
            throw new InvalidJsonException("Error processing response json structure: " + responseArray, err);
        }
    }
}

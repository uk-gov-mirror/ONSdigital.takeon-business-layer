package uk.gov.ons.collection.utilities;

import uk.gov.ons.collection.exception.InvalidJsonException;

import java.sql.Timestamp;
import java.util.Date;
import java.util.StringJoiner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpsertResponse {

    private JSONArray responseArray;
    private final Timestamp time = new Timestamp(new Date().getTime());

    public UpsertResponse(String jsonString) throws InvalidJsonException {
        try {
            responseArray = new JSONObject(jsonString).getJSONArray("response");
        } catch (JSONException err) {
            throw new InvalidJsonException("Given string could not be converted/processed: " + jsonString, err);
        }
    }

    public String buildRetrieveResponseQuery() throws InvalidJsonException {
        var queryJson = new StringBuilder();
        queryJson.append("{\"query\" : \"query filteredResponse {allResponses(condition: ");
        queryJson.append(retrieveOutputs());
        queryJson.append("){nodes{reference,period,survey,questioncode,response," +
                "createdby,createddate,lastupdatedby,lastupdateddate}}}\"}");
        return queryJson.toString();
    }

    private String retrieveOutputs() throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");
        for (int i = 0; i < responseArray.length(); i++) {
            joiner.add("{" + extractRetrieveResponseOutputRow(i) + "}");
        }
        return joiner.toString();
    }

    private String extractRetrieveResponseOutputRow(int index) throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");
        try {
            var outputRow = responseArray.getJSONObject(index);
            joiner.add("reference: \\\"" + outputRow.getString("reference") + "\\\"");
            joiner.add("period: \\\"" + outputRow.getString("period") + "\\\"");
            joiner.add("survey: \\\"" + outputRow.getString("survey") + "\\\"");

            return joiner.toString();
        } catch (Exception err) {
            throw new InvalidJsonException("Error processing validation output json structure: " + responseArray, err);
        }
    }

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
            joiner.add("reference: \\\"" + outputRow.getString("reference") + "\\\"");
            joiner.add("period: \\\"" + outputRow.getString("period") + "\\\"");
            joiner.add("survey: \\\"" + outputRow.getString("survey") + "\\\"");
            joiner.add("questioncode: \\\"" + outputRow.getString("questioncode") + "\\\"");
            joiner.add("instance: " + outputRow.getInt("instance"));
            joiner.add("response: \\\"" + outputRow.getString("response") + "\\\"");
            joiner.add("createdby: \\\"fisdba\\\"");
            joiner.add("createddate: \\\"" + time.toString() + "\\\"");
            return joiner.toString();
        } catch (Exception err) {
            throw new InvalidJsonException("Error processing validation output json structure: " + responseArray, err);
        }
    }
}

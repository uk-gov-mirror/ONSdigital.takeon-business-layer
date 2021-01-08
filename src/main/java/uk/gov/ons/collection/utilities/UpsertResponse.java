package uk.gov.ons.collection.utilities;

import uk.gov.ons.collection.entity.ResponseData;
import uk.gov.ons.collection.exception.InvalidJsonException;
import uk.gov.ons.collection.entity.ContributorStatus;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
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
        queryJson.append("}){nodes{reference,period,survey,questioncode,response,instance," +
                "createdby,createddate,lastupdatedby,lastupdateddate}}}\"}");
        return queryJson.toString();
    }

    // Loops through json array to pull out data
    private String retrieveOldResponse() throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");

        joiner.add("{" + extractOldResponseRow());

        return joiner.toString();
    }



    // Extracts values from json and stores and adds to StringJoiner
    private String extractOldResponseRow() throws InvalidJsonException {
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
            String statusText = ("Form saved");

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

    public List<ResponseData> buildCurrentResponseEntities(JSONArray outputArray) throws InvalidJsonException {

        List<ResponseData> currentResponseEntities = new ArrayList<ResponseData>();

        for (int i = 0; i < outputArray.length(); i++) {
            ResponseData response = new ResponseData();
            String reference =  outputArray.getJSONObject(i).getString("reference");
            response.setReference(reference);
            response.setSurvey(outputArray.getJSONObject(i).getString("survey"));
            response.setPeriod(outputArray.getJSONObject(i).getString("period"));
            response.setQuestionCode(outputArray.getJSONObject(i).getString("questioncode"));
            if(outputArray.getJSONObject(i).isNull("response")){
                response.setResponse("");
            } else {
                response.setResponse(outputArray.getJSONObject(i).getString("response"));
            }
            response.setCreatedBy(outputArray.getJSONObject(i).getString("createdby"));
            response.setCreatedDate(outputArray.getJSONObject(i).getString("createddate"));
            response.setInstance(outputArray.getJSONObject(i).getInt("instance"));
            Object obLastUpdatedDate = outputArray.getJSONObject(i).get("lastupdateddate");
            response.setLastUpdateDate(obLastUpdatedDate == null ? "" : obLastUpdatedDate.toString());
            Object obLastUpdatedBy = outputArray.getJSONObject(i).get("lastupdatedby");
            response.setLastUpdatedBy(obLastUpdatedBy == null ? "" : obLastUpdatedBy.toString());
            currentResponseEntities.add(response);
        }

        return currentResponseEntities;
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
            joiner.add("lastupdatedby: \\\"fisdba\\\"");
            joiner.add("lastupdateddate: \\\"" + time.toString() + "\\\"");
            return joiner.toString();
        } catch (Exception err) {
            throw new InvalidJsonException("Error processing response json structure: " + responseArray, err);
        }
    }

    public String processConsolidatedJsonList(List<ResponseData> responsesToPassToDatabase, String updatedResponses)
            throws InvalidJsonException {

        JSONObject updatedResponsesJson = new JSONObject(updatedResponses);
        JSONObject upsertResponses = new JSONObject();

        var updatedResponseArray = new JSONArray();
        try {

            for (ResponseData response : responsesToPassToDatabase) {
                var updatedResponsesObject = new JSONObject();
                updatedResponsesObject.put("instance", response.getInstance());
                updatedResponsesObject.put("questioncode", response.getQuestionCode());
                updatedResponsesObject.put("response", response.getResponse());
                updatedResponseArray.put(updatedResponsesObject);
            }
            JSONObject updatedDerivedResponses = new JSONObject().put("responses", updatedResponseArray);
            // Create new object with reference, period, survey and user included before saving
            upsertResponses.put("reference", updatedResponsesJson.get("reference"));
            upsertResponses.put("period", updatedResponsesJson.get("period"));
            upsertResponses.put("survey", updatedResponsesJson.get("survey"));
            upsertResponses.put("user", updatedResponsesJson.get("user"));
            upsertResponses.put("responses", updatedDerivedResponses.getJSONArray("responses"));

        } catch (Exception err) {
            throw new InvalidJsonException("Error processing response json structure: " + updatedResponseArray, err);
        }

        return upsertResponses.toString();

    }
}

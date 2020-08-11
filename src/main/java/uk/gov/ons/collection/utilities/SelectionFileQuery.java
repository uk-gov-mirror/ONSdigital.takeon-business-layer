package uk.gov.ons.collection.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.gov.ons.collection.exception.InvalidJsonException;

public class SelectionFileQuery {
    private HashMap<String, String> variables;
    private List<String> intVariables = new ArrayList<>(Arrays.asList("first", "last", "formid"));
    private JSONArray responseArray;
    private JSONObject responseObject;

    public SelectionFileQuery(Map<String, String> variables){
        this.variables = (variables == null) ? new HashMap<>() : new HashMap<>(variables);
    }

    public SelectionFileQuery(String jsonString) throws InvalidJsonException {
        try {
            responseObject = new JSONObject(jsonString);
            responseArray = responseObject.getJSONArray("responses");

        } catch (JSONException err) {
            throw new InvalidJsonException("Given string could not be converted/processed: " + jsonString, err);
        }
    }

    public String buildVariables() {
        StringJoiner joiner = new StringJoiner(",");
        variables.forEach((key,value) -> {
            if (intVariables.contains(key)) {
                joiner.add("\"" + key + "\": " + value);
            } else {
                joiner.add("\"" + key + "\": \"" + value + "\"");
            }
        });
        return joiner.toString();
    }
    
    public String buildCheckIDBRFormidQuery() {
        StringBuilder checkIDBRFormidQuery = new StringBuilder();
        checkIDBRFormidQuery.append("{\"query\": \"query checkidbrformid($formtype: String) " +
                "{ checkidbrformid (condition: {formtype: $formtype}) {" +
                "nodes { formid survey formtype periodstart periodend }}}}}\"," +
                "\"variables\": {"); 
        checkIDBRFormidQuery.append(buildVariables());
        checkIDBRFormidQuery.append("}}");
        return checkIDBRFormidQuery.toString();
    }

    public String buildSaveSelectionFileQuery() throws InvalidJsonException {
        var jsonQlResponse = new StringBuilder();
        jsonQlResponse.append("{\"query\" : \"mutation loadResponse {LoadIDBRForm(input: {arg0: ");
        jsonQlResponse.append("[" + getSelectionLoadData() + "]");
        jsonQlResponse.append("}){clientMutationId}}\"}");
        return jsonQlResponse.toString();
    }

    // Loop through the given validation output array json and convert it into a graphQL compatable format
    private String getSelectionLoadData() throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");
        for (int i = 0; i < responseArray.length(); i++) {
            joiner.add("{" + extractValidationOutputRow(i) + "}");
        }
        return joiner.toString();
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

}
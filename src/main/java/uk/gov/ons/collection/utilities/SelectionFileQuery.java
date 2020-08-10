package uk.gov.ons.collection.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

import org.json.JSONArray;
import org.json.JSONObject;

public class SelectionFileQuery {
    private String reference;
    private String period;
    private String survey;
    private JSONObject jsonQlResponse;
    private HashMap<String, String> variables;
    private List<String> intVariables = new ArrayList<>(Arrays.asList("first", "last", "formid"));

    public SelectionFileQuery(String jsonString) {
        try {
            jsonQlResponse = new JSONObject(jsonString);
        } catch (Exception e) {
            jsonString = "{}";
            jsonQlResponse = new JSONObject(jsonString);
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

    public int parseFormidResponse() {
        var outputArray = new JSONArray();
        int Formid;
        System.out.println("Output from checkIDBR Table: " + jsonQlResponse.toString());
        if (jsonQlResponse.getJSONObject("data").getJSONObject("checkIDBRFormid").getJSONArray("nodes").length() > 0) {
            outputArray = jsonQlResponse.getJSONObject("data").getJSONObject("checkIDBRFormid").getJSONArray("nodes");
        }
        Formid = outputArray.getJSONObject(0).getInt("formid");
        return Formid;
    }
}
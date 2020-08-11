package uk.gov.ons.collection.utilities;

import org.json.JSONArray;
import org.json.JSONObject;

import uk.gov.ons.collection.exception.InvalidJsonException;

public class SelectionFileResponse {
    private JSONObject jsonQlResponse;

    public SelectionFileResponse(String jsonString) {
        try {
            jsonQlResponse = new JSONObject(jsonString);
        } catch (Exception e) {
            jsonString = "{}";
            jsonQlResponse = new JSONObject(jsonString);
        }
    }
    public int parseFormidResponse() {
        var outputArray = new JSONArray();
        int formId;
        System.out.println("Output from checkIDBR Table: " + jsonQlResponse.toString());
        if (jsonQlResponse.getJSONObject("data").getJSONObject("checkIDBRFormid").getJSONArray("nodes").length() > 0) {
            outputArray = jsonQlResponse.getJSONObject("data").getJSONObject("checkIDBRFormid").getJSONArray("nodes");
        }
        formId = outputArray.getJSONObject(0).getInt("formid");
        return formId;
    }

}
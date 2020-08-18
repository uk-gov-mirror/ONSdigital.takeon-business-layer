package uk.gov.ons.collection.utilities;

import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;

import uk.gov.ons.collection.exception.InvalidJsonException;

@Log4j2
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

    public int parseFormidResponse() throws InvalidJsonException {
        var outputArray = new JSONArray();
        int formId;
        log.info("Output from checkIDBR Table: " + jsonQlResponse.toString());
        if (jsonQlResponse.getJSONObject("data").getJSONObject("allCheckidbrs").getJSONArray("nodes").length() > 0) {
            outputArray = jsonQlResponse.getJSONObject("data").getJSONObject("allCheckidbrs").getJSONArray("nodes");
            formId = outputArray.getJSONObject(0).getInt("formid");
        } else {
            throw new InvalidJsonException("There is no mapping between IDBR form type (on selection file) and the form ID held in database ");
        }

        return formId;
    }

}
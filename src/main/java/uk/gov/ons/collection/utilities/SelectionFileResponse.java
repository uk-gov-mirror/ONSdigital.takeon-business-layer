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

    public int parseFormidResponse(int period) throws InvalidJsonException {
        var outputArray = new JSONArray();
        int formId = 0;
        log.info("Output from checkIDBR Table: " + jsonQlResponse.toString());
        log.info("Period from Lambda: " + period);
        outputArray = jsonQlResponse.getJSONObject("data").getJSONObject("allIdbrformtypes").getJSONArray("nodes");
        if (outputArray.length() > 0) {
            int countNumberOfMatchingRecords = 0;
            for (int i = 0; i < outputArray.length(); i++) {
                int periodStart = Integer.parseInt(outputArray.getJSONObject(i).getString("periodstart"));
                log.info("Period start: "+periodStart);
                int periodEnd = Integer.parseInt(outputArray.getJSONObject(i).getString("periodend"));
                log.info("Period End: "+periodEnd);
                if(period >= periodStart && period <= periodEnd) {
                    formId = outputArray.getJSONObject(i).getInt("formid");
                    countNumberOfMatchingRecords++;
                }
            }
            log.info("No of Matching Records: " + countNumberOfMatchingRecords);
            //Exception handling when more than 1 match and this is an error
            if (countNumberOfMatchingRecords > 1) {
                throw new InvalidJsonException("There is more than one mapping between IDBR form type (on selection file) and the form ID held in database ");
            } else if (countNumberOfMatchingRecords == 0) {//if no matches then also an error
                throw new InvalidJsonException("There is no mapping between IDBR form type (on selection file) and the form ID held in database ");
            }

        } else {
            //This is when no records in the configuration table
            throw new InvalidJsonException("There is no mapping between IDBR form type (on selection file) and the form ID held in database ");
        }

        return formId;
    }

}
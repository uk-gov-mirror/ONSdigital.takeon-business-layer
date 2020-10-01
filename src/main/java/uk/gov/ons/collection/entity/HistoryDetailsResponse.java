package uk.gov.ons.collection.entity;

import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import uk.gov.ons.collection.exception.InvalidJsonException;
import uk.gov.ons.collection.utilities.RelativePeriod;

@Log4j2
public class HistoryDetailsResponse {
    private JSONObject jsonPeriodicityResponse;
    private static final int NUMBER_HISTORY_PERIODS = 12;

    public HistoryDetailsResponse(String jsonString) {
        try {
            jsonPeriodicityResponse = new JSONObject(jsonString);
        } catch (Exception e) {
            jsonString = "{}";
            jsonPeriodicityResponse = new JSONObject(jsonString);
        }
    }

    public String parsePeriodicityFromSurvey() throws InvalidJsonException {
        var outputArray = new JSONArray();
        String periodStr = "";
        outputArray = jsonPeriodicityResponse.getJSONObject("data").getJSONObject("allSurveys").getJSONArray("nodes");
        if (outputArray.length() > 0) {
            for (int i = 0; i < outputArray.length(); i++) {
                periodStr = outputArray.getJSONObject(i).getString("periodicity");
            }
            if (periodStr.isEmpty()) {
                throw new InvalidJsonException(" There is no periodicity for the given survey ");
            }

        } else {
            //This is when no no periodicity is associated to a given survey
            throw new InvalidJsonException(" There is no periodicity for the given survey ");
        }

        return periodStr;
    }

    public List<String> getHistoryPeriods(String currentPeriod, String periodicity) {
        List<String> historyPeriodList = new ArrayList<String>();
        List<Integer> offsetList = new ArrayList<Integer>();

        historyPeriodList.add(currentPeriod);
        for(int i=1; i <= NUMBER_HISTORY_PERIODS; i++) {
            offsetList.add(i);
        }
        try {
            RelativePeriod rp = new RelativePeriod(periodicity);
            List<String> periods = rp.getIdbrPeriods(offsetList, currentPeriod);
            historyPeriodList.addAll(periods);
            System.out.println(historyPeriodList.toString());
        } catch (Exception e) {

        }
        return historyPeriodList;
    }

}

package uk.gov.ons.collection.entity;

import uk.gov.ons.collection.exception.InvalidJsonException;
import org.json.JSONException;
import org.json.JSONObject;

public class Contributor {

    private JSONObject jsonResponse;

    public Contributor(String queryResponse) throws InvalidJsonException {
        try {
            this.jsonResponse = new JSONObject(queryResponse);
        } catch (JSONException err) {
            throw new InvalidJsonException("Given string could not be converted to JSON: " + queryResponse, err);
        } catch (NullPointerException err) {
            throw new InvalidJsonException("NULL json passed through!", err);
        }
    }

    public int getFormId() throws InvalidJsonException {
        try {
            return jsonResponse.getJSONObject("data").getJSONObject("contributorByReferenceAndPeriodAndSurvey").getInt("formid");
        } catch (Exception err) {
            throw new InvalidJsonException("Given JSON did not contain formID in the expected location: " + jsonResponse, err);
        }
    }

    public String getSurveyPeriodicity() throws InvalidJsonException {
        try {
            return jsonResponse.getJSONObject("data").getJSONObject("contributorByReferenceAndPeriodAndSurvey")
                .getJSONObject("surveyBySurvey").getString("periodicity");
        } catch (Exception err) {
            throw new InvalidJsonException("Given JSON did not contain formID in the expected location: " + jsonResponse, err);
        }
    }

}
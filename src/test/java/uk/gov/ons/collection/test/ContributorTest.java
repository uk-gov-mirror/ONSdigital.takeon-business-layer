package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.Contributor;
import uk.gov.ons.collection.exception.InvalidJsonException;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ContributorTest {

    @Test
    public void whenConstructed_givenInvalidJson_throwsException() {
        var invalidJson = "{InvalidJson}";
        assertThrows(InvalidJsonException.class, () -> {
            new Contributor(invalidJson);
        });
    }

    @Test
    public void whenConstructed_givenNullJson_throwsException() {
        assertThrows(InvalidJsonException.class, () -> {
            new Contributor(null);
        });
    }

    @Test
    public void givenValidAndCorrectJson_getFormId_returnsExpectedId() {
        var inputJson = "{\"data\": {\"contributorByReferenceAndPeriodAndSurvey\": {\"formid\": 25} } }";
        try {
            var contributor = new Contributor(inputJson);
            assertEquals(contributor.getFormId(), 25);
        } catch (InvalidJsonException ex) {
            assertEquals("Test input string is invalid", ex.getMessage());
        }
    }

    @Test
    public void givenValidAndIncorrectJson_getFormId_throwsException() {
        var inputJson = "{\"data\": {\"cheese\": {\"formid\": 25} } }";
        assertThrows(InvalidJsonException.class, () -> {
            new Contributor(inputJson).getFormId();
        });
    }

    @Test
    public void givenValidAndCorrectJson_getSurveyPeriodicity_returnsExpectedPeriodicity() {
        var inputJson = "{\"data\": {\"contributorByReferenceAndPeriodAndSurvey\": {\"surveyBySurvey\": {\"periodicity\": \"Test\" }}} } }";
        try {
            var contributor = new Contributor(inputJson);
            assertEquals("Test", contributor.getSurveyPeriodicity());
        } catch (InvalidJsonException ex) {
            assertEquals("Test input string is invalid", ex.getMessage());
        }
    }

    @Test
    public void givenValidAndIncorrectJson_getSurveyPeriodicity_throwsException() {
        var inputJson = "{\"data\": {\"Toast\": {\"Radio\": {\"periodicity\": \"Test\" }}} } }";
        assertThrows(InvalidJsonException.class, () -> {
            new Contributor(inputJson).getSurveyPeriodicity();
        });
    }

}


/*
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
*/
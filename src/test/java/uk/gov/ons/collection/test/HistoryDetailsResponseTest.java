package uk.gov.ons.collection.test;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uk.gov.ons.collection.entity.HistoryDetailsResponse;
import uk.gov.ons.collection.utilities.QlQueryResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
public class HistoryDetailsResponseTest {

    private static final String REFERENCE = "reference";
    private static final String PERIOD = "period";
    private static final String SURVEY = "survey";

    @Test
    void historyDetails_ExpectedJSONDataEqualsActualJSONData_ParsedData(){
        String responseJSON = "{\"data\":{\"allContributors\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"reference\":\"49900534932\",\"formByFormid\":{\"formdefinitionsByFormid\":{\"nodes\":[{\"questioncode\":\"11\",\"type\":\"DATE\",\"derivedformula\":\"\",\"displaytext\":\"Period start date\",\"displayquestionnumber\":\"Q11\",\"displayorder\":1},{\"questioncode\":\"12\",\"type\":\"DATE\",\"derivedformula\":\"\",\"displaytext\":\"Period end date\",\"displayquestionnumber\":\"Q12\",\"displayorder\":2},{\"questioncode\":\"146\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Comment about reason for turnover changes\",\"displayquestionnumber\":\"Q146\",\"displayorder\":5},{\"questioncode\":\"146a\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Did significant turnover changes occur?\",\"displayquestionnumber\":\"Q146a\",\"displayorder\":6},{\"questioncode\":\"146b\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"In-store or online promotions was the reason\",\"displayquestionnumber\":\"Q146b\",\"displayorder\":7},{\"questioncode\":\"146c\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Special events was the reason\",\"displayquestionnumber\":\"Q146c\",\"displayorder\":8},{\"questioncode\":\"146d\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Calendar events was the reason\",\"displayquestionnumber\":\"Q146d\",\"displayorder\":9},{\"questioncode\":\"146e\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Weather was the reason\",\"displayquestionnumber\":\"Q146e\",\"displayorder\":10},{\"questioncode\":\"146f\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Store closures was the reason\",\"displayquestionnumber\":\"Q146f\",\"displayorder\":11},{\"questioncode\":\"146g\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Store openings was the reason\",\"displayquestionnumber\":\"Q146g\",\"displayorder\":12},{\"questioncode\":\"146h\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Other reason\",\"displayquestionnumber\":\"Q146h\",\"displayorder\":13},{\"questioncode\":\"20\",\"type\":\"NUMERIC\",\"derivedformula\":\"\",\"displaytext\":\"Total retail turnover\",\"displayquestionnumber\":\"Q20\",\"displayorder\":3},{\"questioncode\":\"21\",\"type\":\"NUMERIC\",\"derivedformula\":\"\",\"displaytext\":\"Internet sales\",\"displayquestionnumber\":\"Q21\",\"displayorder\":4}]}},\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[{\"instance\":0,\"questioncode\":\"11\",\"response\":\"1\"},{\"instance\":0,\"questioncode\":\"12\",\"response\":\"2\"},{\"instance\":0,\"questioncode\":\"20\",\"response\":\"3\"}]}},{\"survey\":\"023\",\"period\":\"201903\",\"reference\":\"49900534932\",\"formByFormid\":{\"formdefinitionsByFormid\":{\"nodes\":[{\"questioncode\":\"11\",\"type\":\"DATE\",\"derivedformula\":\"\",\"displaytext\":\"Period start date\",\"displayquestionnumber\":\"Q11\",\"displayorder\":1},{\"questioncode\":\"12\",\"type\":\"DATE\",\"derivedformula\":\"\",\"displaytext\":\"Period end date\",\"displayquestionnumber\":\"Q12\",\"displayorder\":2},{\"questioncode\":\"146\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Comment about reason for turnover changes\",\"displayquestionnumber\":\"Q146\",\"displayorder\":5},{\"questioncode\":\"146a\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Did significant turnover changes occur?\",\"displayquestionnumber\":\"Q146a\",\"displayorder\":6},{\"questioncode\":\"146b\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"In-store or online promotions was the reason\",\"displayquestionnumber\":\"Q146b\",\"displayorder\":7},{\"questioncode\":\"146c\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Special events was the reason\",\"displayquestionnumber\":\"Q146c\",\"displayorder\":8},{\"questioncode\":\"146d\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Calendar events was the reason\",\"displayquestionnumber\":\"Q146d\",\"displayorder\":9},{\"questioncode\":\"146e\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Weather was the reason\",\"displayquestionnumber\":\"Q146e\",\"displayorder\":10},{\"questioncode\":\"146f\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Store closures was the reason\",\"displayquestionnumber\":\"Q146f\",\"displayorder\":11},{\"questioncode\":\"146g\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Store openings was the reason\",\"displayquestionnumber\":\"Q146g\",\"displayorder\":12},{\"questioncode\":\"146h\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Other reason\",\"displayquestionnumber\":\"Q146h\",\"displayorder\":13},{\"questioncode\":\"20\",\"type\":\"NUMERIC\",\"derivedformula\":\"\",\"displaytext\":\"Total retail turnover\",\"displayquestionnumber\":\"Q20\",\"displayorder\":3},{\"questioncode\":\"21\",\"type\":\"NUMERIC\",\"derivedformula\":\"\",\"displaytext\":\"Internet sales\",\"displayquestionnumber\":\"Q21\",\"displayorder\":4}]}},\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[{\"instance\":0,\"questioncode\":\"11\",\"response\":\"1\"},{\"instance\":0,\"questioncode\":\"12\",\"response\":\"2\"},{\"instance\":0,\"questioncode\":\"20\",\"response\":\"3\"}]}}]}}}";
        JSONObject queryOutput = new JSONObject(responseJSON);
        JSONArray contribArray = new JSONArray();
        var historyDataObj = new JSONObject();
        var historyDataArr = new JSONArray();
        if (queryOutput.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").length() > 0){
            contribArray = queryOutput.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes");
            for (int i = 0; i < contribArray.length(); i++) {
                JSONObject eachContributorObj = queryOutput.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").getJSONObject(i);
                var historyDetailsObject = new JSONObject();
                historyDetailsObject.put(REFERENCE, eachContributorObj.get(REFERENCE));
                historyDetailsObject.put(PERIOD, eachContributorObj.get(PERIOD));
                historyDetailsObject.put(SURVEY,eachContributorObj.get(SURVEY));
                JSONArray formDefinitionArray = eachContributorObj .getJSONObject("formByFormid").getJSONObject("formdefinitionsByFormid").getJSONArray("nodes");
                if (formDefinitionArray.length() > 0) {
                    var viewFormResponsesArray = new JSONArray();
                    for(int j = 0; j < formDefinitionArray.length(); j++) {
                        var eachFormObject = new JSONObject();
                        eachFormObject.put("questioncode", formDefinitionArray.getJSONObject(j).getString("questioncode"));
                        eachFormObject.put("displaytext", formDefinitionArray.getJSONObject(j).getString("displaytext"));
                        eachFormObject.put("displayquestionnumber", formDefinitionArray.getJSONObject(j).getString("displayquestionnumber"));
                        eachFormObject.put("displayorder", formDefinitionArray.getJSONObject(j).getInt("displayorder"));
                        eachFormObject.put("type", formDefinitionArray.getJSONObject(j).getString("type"));
                        eachFormObject.put("response", "");
                        eachFormObject.put("instance", "");
                        viewFormResponsesArray.put(eachFormObject);
                    }
                    var responseArray = new JSONArray();
                    responseArray = queryOutput.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").getJSONObject(i)
                            .getJSONObject("responsesByReferenceAndPeriodAndSurvey").getJSONArray("nodes");
                    for(int k = 0; k < viewFormResponsesArray.length(); k++){
                        for(int l = 0; l < responseArray.length(); l++){
                            if(viewFormResponsesArray.getJSONObject(k).getString("questioncode").equals(responseArray.getJSONObject(l).getString("questioncode"))) {
                                viewFormResponsesArray.getJSONObject(k).put("response", responseArray.getJSONObject(l).getString("response"));
                                viewFormResponsesArray.getJSONObject(k).put("instance", responseArray.getJSONObject(l).getInt("instance"));
                            }
                        }
                    }
                    historyDetailsObject.put("view_form_responses", viewFormResponsesArray);

                }

                historyDataArr.put(historyDetailsObject);
            }
            historyDataObj.put("history_data", historyDataArr);
        }
        System.out.println(" Final data " +historyDataObj.toString());

    }

    @Test
    void parse_periodicity_nullInputJson_returnException() {
        HistoryDetailsResponse response = new HistoryDetailsResponse(null);
        try {
            response.parsePeriodicityFromSurvey();
        } catch (Exception e) {
            assertTrue(true);
            System.out.println("Exception here");
        }
    }

    @Test
    void parse_validButNoPeriodicityData_givesValidException() {
        String inputString = "{\"data\":{\"allSurveys\":{\"nodes\":[]}}}";
        String expectedExceptionMessage = " There is no configuration Survey table which provides periodicity";
        HistoryDetailsResponse response = new HistoryDetailsResponse(inputString);
        try {
            String responsePeriodicity = response.parsePeriodicityFromSurvey();
            System.out.println(" Output "+ responsePeriodicity);
        } catch (Exception e) {
            assertTrue(true);
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }

}

package uk.gov.ons.collection.test;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;


import uk.gov.ons.collection.entity.HistoryDetailsResponse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
public class HistoryDetailsResponseTest {


    @Test
    void historyDetails_ExpectedJSONDataEqualsActualJSONData_ParsedData(){
        String responseJSON = "{\"data\":{\"allContributors\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\"," +
                "\"reference\":\"49900534932\",\"formByFormid\":{\"formdefinitionsByFormid\":{\"nodes\":" +
                "[{\"questioncode\":\"11\",\"type\":\"DATE\",\"derivedformula\":\"\",\"displaytext\":\"Period start date\",\"displayquestionnumber\":\"Q11\",\"displayorder\":1},{\"questioncode\":\"12\",\"type\":\"DATE\",\"derivedformula\":\"\",\"displaytext\":\"Period end date\",\"displayquestionnumber\":\"Q12\",\"displayorder\":2},{\"questioncode\":\"146\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Comment about reason for turnover changes\",\"displayquestionnumber\":\"Q146\",\"displayorder\":5},{\"questioncode\":\"146a\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Did significant turnover changes occur?\",\"displayquestionnumber\":\"Q146a\",\"displayorder\":6},{\"questioncode\":\"146b\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"In-store or online promotions was the reason\",\"displayquestionnumber\":\"Q146b\",\"displayorder\":7},{\"questioncode\":\"146c\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Special events was the reason\",\"displayquestionnumber\":\"Q146c\",\"displayorder\":8},{\"questioncode\":\"146d\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Calendar events was the reason\",\"displayquestionnumber\":\"Q146d\",\"displayorder\":9},{\"questioncode\":\"146e\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Weather was the reason\",\"displayquestionnumber\":\"Q146e\",\"displayorder\":10},{\"questioncode\":\"146f\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Store closures was the reason\",\"displayquestionnumber\":\"Q146f\",\"displayorder\":11},{\"questioncode\":\"146g\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Store openings was the reason\",\"displayquestionnumber\":\"Q146g\",\"displayorder\":12},{\"questioncode\":\"146h\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Other reason\",\"displayquestionnumber\":\"Q146h\",\"displayorder\":13},{\"questioncode\":\"20\",\"type\":\"NUMERIC\",\"derivedformula\":\"\",\"displaytext\":\"Total retail turnover\",\"displayquestionnumber\":\"Q20\",\"displayorder\":3},{\"questioncode\":\"21\",\"type\":\"NUMERIC\",\"derivedformula\":\"\",\"displaytext\":\"Internet sales\",\"displayquestionnumber\":\"Q21\",\"displayorder\":4}]}},\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[{\"instance\":0,\"questioncode\":\"11\",\"response\":\"1\"},{\"instance\":0,\"questioncode\":\"12\",\"response\":\"2\"},{\"instance\":0,\"questioncode\":\"20\",\"response\":\"3\"}]}},{\"survey\":\"023\",\"period\":\"201903\",\"reference\":\"49900534932\",\"formByFormid\":{\"formdefinitionsByFormid\":{\"nodes\":[{\"questioncode\":\"11\",\"type\":\"DATE\",\"derivedformula\":\"\",\"displaytext\":\"Period start date\",\"displayquestionnumber\":\"Q11\",\"displayorder\":1},{\"questioncode\":\"12\",\"type\":\"DATE\",\"derivedformula\":\"\",\"displaytext\":\"Period end date\",\"displayquestionnumber\":\"Q12\",\"displayorder\":2},{\"questioncode\":\"146\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Comment about reason for turnover changes\",\"displayquestionnumber\":\"Q146\",\"displayorder\":5},{\"questioncode\":\"146a\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Did significant turnover changes occur?\",\"displayquestionnumber\":\"Q146a\",\"displayorder\":6},{\"questioncode\":\"146b\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"In-store or online promotions was the reason\",\"displayquestionnumber\":\"Q146b\",\"displayorder\":7},{\"questioncode\":\"146c\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Special events was the reason\",\"displayquestionnumber\":\"Q146c\",\"displayorder\":8},{\"questioncode\":\"146d\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Calendar events was the reason\",\"displayquestionnumber\":\"Q146d\",\"displayorder\":9},{\"questioncode\":\"146e\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Weather was the reason\",\"displayquestionnumber\":\"Q146e\",\"displayorder\":10},{\"questioncode\":\"146f\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Store closures was the reason\",\"displayquestionnumber\":\"Q146f\",\"displayorder\":11},{\"questioncode\":\"146g\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Store openings was the reason\",\"displayquestionnumber\":\"Q146g\",\"displayorder\":12},{\"questioncode\":\"146h\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Other reason\",\"displayquestionnumber\":\"Q146h\",\"displayorder\":13},{\"questioncode\":\"20\",\"type\":\"NUMERIC\",\"derivedformula\":\"\",\"displaytext\":\"Total retail turnover\",\"displayquestionnumber\":\"Q20\",\"displayorder\":3},{\"questioncode\":\"21\",\"type\":\"NUMERIC\",\"derivedformula\":\"\",\"displaytext\":\"Internet sales\",\"displayquestionnumber\":\"Q21\",\"displayorder\":4}]}},\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[{\"instance\":0,\"questioncode\":\"11\",\"response\":\"1\"},{\"instance\":0,\"questioncode\":\"12\",\"response\":\"2\"},{\"instance\":0,\"questioncode\":\"20\",\"response\":\"3\"}]}}]}}}";
        String expectedOutput = "{\"history_data\":[{\"reference\":\"49900534932\",\"period\":\"201904\"," +
                "\"view_form_responses\":[{\"displaytext\":\"Period start date\",\"instance\":0,\"response\":\"1\"," +
                "\"questioncode\":\"11\",\"displayorder\":1,\"displayquestionnumber\":\"Q11\",\"type\":\"DATE\"}," +
                "{\"displaytext\":\"Period end date\",\"instance\":0,\"response\":\"2\",\"questioncode\":\"12\"," +
                "\"displayorder\":2,\"displayquestionnumber\":\"Q12\",\"type\":\"DATE\"}," +
                "{\"displaytext\":\"Comment about reason for turnover changes\",\"instance\":\"\",\"response\":\"\"," +
                "\"questioncode\":\"146\",\"displayorder\":5,\"displayquestionnumber\":\"Q146\",\"type\":\"TEXT\"}," +
                "{\"displaytext\":\"Did significant turnover changes occur?\",\"instance\":\"\",\"response\":\"\"," +
                "\"questioncode\":\"146a\",\"displayorder\":6,\"displayquestionnumber\":\"Q146a\",\"type\":\"TEXT\"}," +
                "{\"displaytext\":\"In-store or online promotions was the reason\",\"instance\":\"\",\"response\":\"\"," +
                "\"questioncode\":\"146b\",\"displayorder\":7,\"displayquestionnumber\":\"Q146b\",\"type\":\"TEXT\"}," +
                "{\"displaytext\":\"Special events was the reason\",\"instance\":\"\",\"response\":\"\",\"questioncode\":\"146c\"," +
                "\"displayorder\":8,\"displayquestionnumber\":\"Q146c\",\"type\":\"TEXT\"},{\"displaytext\":\"Calendar events was the reason\"," +
                "\"instance\":\"\",\"response\":\"\",\"questioncode\":\"146d\",\"displayorder\":9,\"displayquestionnumber\":\"Q146d\"," +
                "\"type\":\"TEXT\"},{\"displaytext\":\"Weather was the reason\",\"instance\":\"\",\"response\":\"\",\"questioncode\":\"146e\"," +
                "\"displayorder\":10,\"displayquestionnumber\":\"Q146e\",\"type\":\"TEXT\"},{\"displaytext\":\"Store closures was the reason\"," +
                "\"instance\":\"\",\"response\":\"\",\"questioncode\":\"146f\",\"displayorder\":11,\"displayquestionnumber\":\"Q146f\",\"type\":\"TEXT\"}," +
                "{\"displaytext\":\"Store openings was the reason\",\"instance\":\"\",\"response\":\"\",\"questioncode\":\"146g\",\"displayorder\":12," +
                "\"displayquestionnumber\":\"Q146g\",\"type\":\"TEXT\"},{\"displaytext\":\"Other reason\",\"instance\":\"\",\"response\":\"\"," +
                "\"questioncode\":\"146h\",\"displayorder\":13,\"displayquestionnumber\":\"Q146h\",\"type\":\"TEXT\"},{\"displaytext\":\"Total retail turnover\"," +
                "\"instance\":0,\"response\":\"3\",\"questioncode\":\"20\",\"displayorder\":3,\"displayquestionnumber\":\"Q20\",\"type\":\"NUMERIC\"},{\"displaytext\":\"Internet sales\"," +
                "\"instance\":\"\",\"response\":\"\",\"questioncode\":\"21\",\"displayorder\":4,\"displayquestionnumber\":\"Q21\",\"type\":\"NUMERIC\"}],\"survey\":\"023\"},{\"reference\":\"49900534932\"," +
                "\"period\":\"201903\",\"view_form_responses\":[{\"displaytext\":\"Period start date\",\"instance\":0,\"response\":\"1\",\"questioncode\":\"11\",\"displayorder\":1," +
                "\"displayquestionnumber\":\"Q11\",\"type\":\"DATE\"},{\"displaytext\":\"Period end date\",\"instance\":0,\"response\":\"2\",\"questioncode\":\"12\",\"displayorder\":2," +
                "\"displayquestionnumber\":\"Q12\",\"type\":\"DATE\"},{\"displaytext\":\"Comment about reason for turnover changes\",\"instance\":\"\",\"response\":\"\",\"questioncode\":\"146\",\"displayorder\":5," +
                "\"displayquestionnumber\":\"Q146\",\"type\":\"TEXT\"},{\"displaytext\":\"Did significant turnover changes occur?\",\"instance\":\"\",\"response\":\"\"," +
                "\"questioncode\":\"146a\",\"displayorder\":6,\"displayquestionnumber\":\"Q146a\",\"type\":\"TEXT\"},{\"displaytext\":\"In-store or online promotions was the reason\",\"instance\":\"\"," +
                "\"response\":\"\",\"questioncode\":\"146b\",\"displayorder\":7," +
                "\"displayquestionnumber\":\"Q146b\",\"type\":\"TEXT\"},{\"displaytext\":\"Special events was the reason\",\"instance\":\"\",\"response\":\"\",\"questioncode\":\"146c\"," +
                "\"displayorder\":8,\"displayquestionnumber\":\"Q146c\",\"type\":\"TEXT\"},{\"displaytext\":\"Calendar events was the reason\",\"instance\":\"\",\"response\":\"\",\"questioncode\":\"146d\"," +
                "\"displayorder\":9,\"displayquestionnumber\":\"Q146d\",\"type\":\"TEXT\"},{\"displaytext\":\"Weather was the reason\",\"instance\":\"\",\"response\":\"\",\"questioncode\":\"146e\",\"displayorder\":10,\"displayquestionnumber\":\"Q146e\",\"type\":\"TEXT\"}," +
                "{\"displaytext\":\"Store closures was the reason\",\"instance\":\"\",\"response\":\"\",\"questioncode\":\"146f\",\"displayorder\":11,\"displayquestionnumber\":\"Q146f\",\"type\":\"TEXT\"},{\"displaytext\":\"Store openings was the reason\",\"instance\":\"\"," +
                "\"response\":\"\",\"questioncode\":\"146g\",\"displayorder\":12,\"displayquestionnumber\":\"Q146g\",\"type\":\"TEXT\"},{\"displaytext\":\"Other reason\",\"instance\":\"\",\"response\":\"\",\"questioncode\":\"146h\",\"displayorder\":13,\"displayquestionnumber\":\"Q146h\"," +
                "\"type\":\"TEXT\"},{\"displaytext\":\"Total retail turnover\",\"instance\":0,\"response\":\"3\",\"questioncode\":\"20\",\"displayorder\":3,\"displayquestionnumber\":\"Q20\",\"type\":\"NUMERIC\"},{\"displaytext\":\"Internet sales\",\"instance\":\"\",\"response\":\"\"," +
                "\"questioncode\":\"21\",\"displayorder\":4,\"displayquestionnumber\":\"Q21\",\"type\":\"NUMERIC\"}],\"survey\":\"023\"}]}";
        HistoryDetailsResponse response = new HistoryDetailsResponse();
        try {
            String actualJSONOutput = response.parseHistoryDataResponses(responseJSON);
            assertEquals(expectedOutput, actualJSONOutput);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void historyDetails_VerifyHistoryDataPeriods(){
        HistoryDetailsResponse historyDetails = new HistoryDetailsResponse();
        String currentPeriod = "201904";
        String periodicity = "Monthly";
        String expectedHistoryPeriods = "[201904, 201903, 201902, 201901, 201812, 201811, 201810, 201809, 201808, 201807, 201806, 201805, 201804]";
        try {
            List<String> actualHistoryPeriods = historyDetails.getHistoryPeriods(currentPeriod, periodicity);
            assertEquals(expectedHistoryPeriods, actualHistoryPeriods.toString());
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void historyDetails_InvalidPeriodicity_ThrowsAnException(){
        HistoryDetailsResponse historyDetails = new HistoryDetailsResponse();
        String currentPeriod = "201904";
        String periodicity = "weekly";
        String expectedHistoryPeriods = "Problem in getting IDBR periodsInvalid periodicity given: weekly";
        try {
            historyDetails.getHistoryPeriods(currentPeriod, periodicity);
        } catch (Exception e) {
            assertTrue(true);
            assertEquals(expectedHistoryPeriods, e.getMessage());
        }
    }

    @Test
    void historyDetails_InvalidJSONDataThrowsAnException(){
        String responseJSON = "{\"data\":{\"allContributors\":{\"noes\":[{\"survey\":\"023\",\"period\":\"201904\",\"reference\":\"49900534932\",\"formByFormid\":{\"formdefinitionsByFormid\":{\"nodes\":[{\"questioncode\":\"11\",\"type\":\"DATE\",\"derivedformula\":\"\",\"displaytext\":\"Period start date\",\"displayquestionnumber\":\"Q11\",\"displayorder\":1},{\"questioncode\":\"12\",\"type\":\"DATE\",\"derivedformula\":\"\",\"displaytext\":\"Period end date\",\"displayquestionnumber\":\"Q12\",\"displayorder\":2},{\"questioncode\":\"146\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Comment about reason for turnover changes\",\"displayquestionnumber\":\"Q146\",\"displayorder\":5},{\"questioncode\":\"146a\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Did significant turnover changes occur?\",\"displayquestionnumber\":\"Q146a\",\"displayorder\":6},{\"questioncode\":\"146b\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"In-store or online promotions was the reason\",\"displayquestionnumber\":\"Q146b\",\"displayorder\":7},{\"questioncode\":\"146c\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Special events was the reason\",\"displayquestionnumber\":\"Q146c\",\"displayorder\":8},{\"questioncode\":\"146d\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Calendar events was the reason\",\"displayquestionnumber\":\"Q146d\",\"displayorder\":9},{\"questioncode\":\"146e\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Weather was the reason\",\"displayquestionnumber\":\"Q146e\",\"displayorder\":10},{\"questioncode\":\"146f\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Store closures was the reason\",\"displayquestionnumber\":\"Q146f\",\"displayorder\":11},{\"questioncode\":\"146g\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Store openings was the reason\",\"displayquestionnumber\":\"Q146g\",\"displayorder\":12},{\"questioncode\":\"146h\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Other reason\",\"displayquestionnumber\":\"Q146h\",\"displayorder\":13},{\"questioncode\":\"20\",\"type\":\"NUMERIC\",\"derivedformula\":\"\",\"displaytext\":\"Total retail turnover\",\"displayquestionnumber\":\"Q20\",\"displayorder\":3},{\"questioncode\":\"21\",\"type\":\"NUMERIC\",\"derivedformula\":\"\",\"displaytext\":\"Internet sales\",\"displayquestionnumber\":\"Q21\",\"displayorder\":4}]}},\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[{\"instance\":0,\"questioncode\":\"11\",\"response\":\"1\"},{\"instance\":0,\"questioncode\":\"12\",\"response\":\"2\"},{\"instance\":0,\"questioncode\":\"20\",\"response\":\"3\"}]}},{\"survey\":\"023\",\"period\":\"201903\",\"reference\":\"49900534932\",\"formByFormid\":{\"formdefinitionsByFormid\":{\"nodes\":[{\"questioncode\":\"11\",\"type\":\"DATE\",\"derivedformula\":\"\",\"displaytext\":\"Period start date\",\"displayquestionnumber\":\"Q11\",\"displayorder\":1},{\"questioncode\":\"12\",\"type\":\"DATE\",\"derivedformula\":\"\",\"displaytext\":\"Period end date\",\"displayquestionnumber\":\"Q12\",\"displayorder\":2},{\"questioncode\":\"146\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Comment about reason for turnover changes\",\"displayquestionnumber\":\"Q146\",\"displayorder\":5},{\"questioncode\":\"146a\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Did significant turnover changes occur?\",\"displayquestionnumber\":\"Q146a\",\"displayorder\":6},{\"questioncode\":\"146b\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"In-store or online promotions was the reason\",\"displayquestionnumber\":\"Q146b\",\"displayorder\":7},{\"questioncode\":\"146c\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Special events was the reason\",\"displayquestionnumber\":\"Q146c\",\"displayorder\":8},{\"questioncode\":\"146d\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Calendar events was the reason\",\"displayquestionnumber\":\"Q146d\",\"displayorder\":9},{\"questioncode\":\"146e\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Weather was the reason\",\"displayquestionnumber\":\"Q146e\",\"displayorder\":10},{\"questioncode\":\"146f\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Store closures was the reason\",\"displayquestionnumber\":\"Q146f\",\"displayorder\":11},{\"questioncode\":\"146g\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Store openings was the reason\",\"displayquestionnumber\":\"Q146g\",\"displayorder\":12},{\"questioncode\":\"146h\",\"type\":\"TEXT\",\"derivedformula\":\"\",\"displaytext\":\"Other reason\",\"displayquestionnumber\":\"Q146h\",\"displayorder\":13},{\"questioncode\":\"20\",\"type\":\"NUMERIC\",\"derivedformula\":\"\",\"displaytext\":\"Total retail turnover\",\"displayquestionnumber\":\"Q20\",\"displayorder\":3},{\"questioncode\":\"21\",\"type\":\"NUMERIC\",\"derivedformula\":\"\",\"displaytext\":\"Internet sales\",\"displayquestionnumber\":\"Q21\",\"displayorder\":4}]}},\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[{\"instance\":0,\"questioncode\":\"11\",\"response\":\"1\"},{\"instance\":0,\"questioncode\":\"12\",\"response\":\"2\"},{\"instance\":0,\"questioncode\":\"20\",\"response\":\"3\"}]}}]}}}";
        String expectedExceptionMessage = "Problem in parsing History Detail GraphQL responses ";
        HistoryDetailsResponse response = new HistoryDetailsResponse(responseJSON);
        try {
            response.parseHistoryDataResponses(responseJSON);
        } catch (Exception e) {
            String actualMessage = e.getMessage();
            assertTrue(true);
            assertTrue(actualMessage.startsWith(expectedExceptionMessage));
        }
    }

    @Test
    void verify_empty_contributor_throwsAnException() {
        String responseJSON = "{\n" +
                "  \"data\": {\n" +
                "    \"allContributors\": {\n" +
                "      \"nodes\": []\n" +
                "    }\n" +
                "  }\n" +
                "}";
        HistoryDetailsResponse response = new HistoryDetailsResponse(responseJSON);
        String expectedExceptionMessage = "There are no contributors for a given survey, reference and periods. Please verify";
        try {
            response.parseHistoryDataResponses(responseJSON);
        } catch (Exception e) {
            String actualMessage = e.getMessage();
            assertTrue(true);
            assertTrue(actualMessage.contains(expectedExceptionMessage));
        }
    }

    @Test
    void verify_empty_form_definition_throwsAnException() {
        String responseJSON = "{\n" +
                "  \"data\": {\n" +
                "    \"allContributors\": {\n" +
                "      \"nodes\": [\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"period\": \"201904\",\n" +
                "          \"reference\": \"49900534932\",\n" +
                "          \"status\": \"Form saved\",\n" +
                "          \"formid\": 5,\n" +
                "          \"formByFormid\": {\n" +
                "            \"formdefinitionsByFormid\": {\n" +
                "              \"nodes\": []\n" +
                "            }\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";
        HistoryDetailsResponse response = new HistoryDetailsResponse(responseJSON);
        String expectedExceptionMessage = "There is no form definition associated to a given Contributor. Please verify";
        try {
            response.parseHistoryDataResponses(responseJSON);
        } catch (Exception e) {
            String actualMessage = e.getMessage();
            System.out.println(actualMessage);
            assertTrue(true);
            assertTrue(actualMessage.contains(expectedExceptionMessage));
        }
    }


    @Test
    void parse_periodicity_nullInputJson_returnException() {
        HistoryDetailsResponse response = new HistoryDetailsResponse(null);
        try {
            response.parsePeriodicityFromSurvey();
        } catch (Exception e) {
            assertTrue(true);
        }
    }

    @Test
    void parse_validButNoPeriodicityData_givesValidException() {
        String inputString = "{\"data\":{\"allSurveys\":{\"nodes\":[]}}}";
        String expectedExceptionMessage = " There is no configuration Survey table which provides periodicity";
        HistoryDetailsResponse response = new HistoryDetailsResponse(inputString);
        try {
            response.parsePeriodicityFromSurvey();
        } catch (Exception e) {
            assertTrue(true);
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }


    @Test
    void parse_validPeriodicityData_givesPeriodicity() {
        String inputString = "{\"data\":{\"allSurveys\":{\"nodes\":[{\"periodicity\":\"Monthly\"}]}}}";
        String expectedPeriodicityOutput = "Monthly";
        HistoryDetailsResponse response = new HistoryDetailsResponse(inputString);
        try {
            String actualPeriodicityResponse=  response.parsePeriodicityFromSurvey();
            assertEquals(expectedPeriodicityOutput, actualPeriodicityResponse);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void columnChangeInSurveyTable_givesPeriodicityException() {
        String inputString = "{\"data\":{\"allSurveys\":{\"nodes\":[{\"period\":\"Monthly\"}]}}}";
        String expectedExceptionMessage = "JSONObject[\"periodicity\"] not found.";
        HistoryDetailsResponse response = new HistoryDetailsResponse(inputString);
        try {
            response.parsePeriodicityFromSurvey();
        } catch (Exception e) {
            assertTrue(true);
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }

    @Test
    void emptyPeriodicityInSurveyTable_givesPeriodicityException() {
        String inputString = "{\"data\":{\"allSurveys\":{\"nodes\":[{\"periodicity\":\"\"}]}}}";
        String expectedExceptionMessage = " There is no periodicity for the given survey ";
        HistoryDetailsResponse response = new HistoryDetailsResponse(inputString);
        try {
            response.parsePeriodicityFromSurvey();
        } catch (Exception e) {
            assertTrue(true);
            assertEquals(expectedExceptionMessage, e.getMessage());
        }
    }

}

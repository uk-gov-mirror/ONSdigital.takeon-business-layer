package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.SelectiveEditingQuery;
import uk.gov.ons.collection.entity.SelectiveEditingResponse;

import java.sql.Timestamp;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SelectiveEditingResponseTest {

    private final Timestamp time = new Timestamp(new Date().getTime());

    String saveResponse = "{\n" +
            "  \"reference\": \"49900534932\",\n" +
            "  \"period\": \"201904\",\n" +
            "  \"survey\": \"023\",\n" +
            "  \"final_score\": 609676552345000,\n" +
            "  \"output_flag\": \"F\"\n" +
            "}";

    String invalidJsonResponse = "{\n" +
            "  \"reference\": \"49900534932\",\n" +
            "  \"period\": \"201904\",\n" +
            "  \"survey\": \"023\",\n" +
            "  \"final_score\": 609676552345000,\n" +
            "  \"output_flag\": \"F\"\n" +
            "";
    String saveInvalidResponse = "{\n" +
            "  \"reerence\": \"49900534932\",\n" +
            "  \"period\": \"201904\",\n" +
            "  \"survey\": \"023\",\n" +
            "  \"final_score\": 609676552345000,\n" +
            "  \"output_flag\": \"F\"\n" +
            "}";


    @Test
    void verify_selectiveEditingSaveDetails_Query() {
        try {
            SelectiveEditingResponse response = new SelectiveEditingResponse(saveResponse);
            String output = response.buildUpsertQuery();
            String expectedOutput = "{\"query\" : \"mutation saveSelectiveEditingDetails {saveselectiveeditingdetails(input: {arg0: [{reference: \\\"49900534932\\\",period: \\\"201904\\\",survey: \\\"023\\\",score: 609676552345000,flag: \\\"F\\\",createdby: \\\"fisdba\\\",createddate: \\\"" ;
            assertTrue(output.contains(expectedOutput));
        } catch (Exception e) {
            assertTrue(false);
        }
    }


    @Test
    void selectiveEditingConfigDetailsDetails_validJSONData(){

        String responseJSON = "{\"data\":{\"allContributors\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"reference\":\"49900534932\",\"frozenturnover\":99999,\"resultscellnumber\":1,\"domain\":\"1\",\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[{\"questioncode\":\"20\",\"period\":\"201904\",\"response\":\"400\"}]}},{\"survey\":\"023\",\"period\":\"201903\",\"reference\":\"49900534932\",\"resultscellnumber\":1,\"domain\":\"1\",\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[{\"questioncode\":\"20\",\"period\":\"201903\",\"response\":\"200\"}]}}]},\"allSelectiveeditingconfigs\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"1\",\"questioncode\":\"20\",\"threshold\":2,\"estimate\":5}]},\"allCelldetails\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"cellnumber\":1,\"designweight\":5}]}}}";
        String expectedOutput = "{\"reference\":\"49900534932\",\"designweight\":5,\"resultscellnumber\":1,\"period\":\"201904\",\"domain\":\"1\",\"survey\":\"023\",\"frozenturnover\":99999,\"domainconfig\":[{\"currentresponse\":\"400\",\"questioncode\":\"20\",\"estimate\":5,\"threshold\":2,\"previousresponse\":\"200\"}]}";

        try {
            SelectiveEditingResponse response = new SelectiveEditingResponse(responseJSON);
            String actualOutput = response.parseSelectiveEditingQueryResponse();
            assertEquals(expectedOutput, actualOutput);

        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }



    @Test
    void selectiveEditingConfigDetails_ThrowsAnException_when_domain_is_null(){

        String responseJSON = "{\"data\":{\"allContributors\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"reference\":\"49900534932\",\"frozenturnover\":99999,\"resultscellnumber\":null,\"domain\":null,\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}},{\"survey\":\"023\",\"period\":\"201903\",\"reference\":\"49900534932\",\"resultscellnumber\":null,\"domain\":null,\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}}]},\"allSelectiveeditingconfigs\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"1\",\"questioncode\":\"20\",\"threshold\":0.002,\"estimate\":100000000},{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"1\",\"questioncode\":\"21\",\"threshold\":0.002,\"estimate\":100000000}]},\"allCelldetails\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"cellnumber\":1,\"designweight\":2}]}}}";
        String expectedErrorMessage = "Problem in parsing Selective Editing GraphQL responses Either Domain or Results Cell Number is null in Contributor table. Please verify";

        try {
            SelectiveEditingResponse response = new SelectiveEditingResponse(responseJSON);
            response.parseSelectiveEditingQueryResponse();
        } catch (Exception e) {
            String actualMessage = e.getMessage();
            assertEquals(expectedErrorMessage, actualMessage);
            assertTrue(true);

        }
    }

    @Test
    void selectiveEditingResponses_Invalid_Json_ThrowsAnException(){
        String expectedErrorMessage = "Given string could not be converted/processed:";
        try {
            SelectiveEditingResponse response = new SelectiveEditingResponse(invalidJsonResponse);
            response.parseSelectiveEditingQueryResponse();
        } catch (Exception e) {
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains(expectedErrorMessage));
            assertTrue(true);
        }
    }

    @Test
    void selectiveEditingResponses_Invalid_Key_Json_ThrowsAnException(){
        String expectedErrorMessage = "Error in processing save selective editing json structure:";
        try {
            SelectiveEditingResponse response = new SelectiveEditingResponse(saveInvalidResponse);
            response.buildUpsertQuery();
        } catch (Exception e) {
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains(expectedErrorMessage));
            assertTrue(true);
        }
    }


    @Test
    void selectiveEditingConfigDetails_ThrowsAnException_when_no_domainconfig(){

        String responseJSON = "{\"data\":{\"allContributors\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"reference\":\"49900534932\",\"frozenturnover\":99999,\"resultscellnumber\":1,\"domain\":\"1\",\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}},{\"survey\":\"023\",\"period\":\"201903\",\"reference\":\"49900534932\",\"resultscellnumber\":null,\"domain\":null,\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}}]},\"allSelectiveeditingconfigs\":{\"nodes\":[]},\"allCelldetails\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"cellnumber\":1,\"designweight\":2}]}}}";
        String expectedErrorMessage = "Problem in parsing Selective Editing GraphQL responses There is no domain configuration. Please verify";

        try {
            SelectiveEditingResponse response = new SelectiveEditingResponse(responseJSON);
            response.parseSelectiveEditingQueryResponse();
        } catch (Exception e) {
            String actualMessage = e.getMessage();
            assertEquals(expectedErrorMessage, actualMessage);
            assertTrue(true);

        }
    }

    @Test
    void selectiveEditingConfigDetails_ThrowsAnException_when_no_celldetailconfig(){

        String responseJSON = "{\"data\":{\"allContributors\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"reference\":\"49900534932\",\"frozenturnover\":99999,\"resultscellnumber\":1,\"domain\":\"1\",\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}},{\"survey\":\"023\",\"period\":\"201903\",\"reference\":\"49900534932\",\"resultscellnumber\":null,\"domain\":null,\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}}]},\"allSelectiveeditingconfigs\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"1\",\"questioncode\":\"20\",\"threshold\":0.002,\"estimate\":100000000},{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"1\",\"questioncode\":\"21\",\"threshold\":0.002,\"estimate\":100000000}]},\"allCelldetails\":{\"nodes\":[]}}}";
        String expectedErrorMessage = "Problem in parsing Selective Editing GraphQL responses There is no celldetail configuration. Please verify";

        try {
            SelectiveEditingResponse response = new SelectiveEditingResponse(responseJSON);
            response.parseSelectiveEditingQueryResponse();
        } catch (Exception e) {
            e.printStackTrace();
            String actualMessage = e.getMessage();
            assertEquals(expectedErrorMessage, actualMessage);
            assertTrue(true);

        }
    }

    @Test
    void selectiveEditingDetails_ThrowsAnException_when_no_contributor(){

        String responseJSON = "{\"data\":{\"allContributors\":{\"nodes\":[]},\"allSelectiveeditingconfigs\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"1\",\"questioncode\":\"20\",\"threshold\":0.002,\"estimate\":100000000},{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"1\",\"questioncode\":\"21\",\"threshold\":0.002,\"estimate\":100000000}]},\"allCelldetails\":{\"nodes\":[]}}}";
        String expectedErrorMessage = "Problem in parsing Selective Editing GraphQL responses There is no contributor for a given survey, reference and periods. Please verify";

        try {
            SelectiveEditingResponse response = new SelectiveEditingResponse(responseJSON);
            response.parseSelectiveEditingQueryResponse();
        } catch (Exception e) {
            String actualMessage = e.getMessage();
            assertEquals(expectedErrorMessage, actualMessage);
            assertTrue(true);

        }
    }

    @Test
    void selectiveEditingConfigDetails_ThrowsAnException_when_no_matching_domain_in_domainconfig(){

        String responseJSON = "{\"data\":{\"allContributors\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"reference\":\"49900534932\",\"frozenturnover\":99999,\"resultscellnumber\":1,\"domain\":\"1\",\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}},{\"survey\":\"023\",\"period\":\"201903\",\"reference\":\"49900534932\",\"resultscellnumber\":null,\"domain\":null,\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}}]},\"allSelectiveeditingconfigs\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"2\",\"questioncode\":\"20\",\"threshold\":0.002,\"estimate\":100000000},{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"2\",\"questioncode\":\"21\",\"threshold\":0.002,\"estimate\":100000000}]},\"allCelldetails\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"cellnumber\":1,\"designweight\":2}]}}}";
        String expectedErrorMessage = "Problem in parsing Selective Editing GraphQL responses There are no thresholds for a given domain in the contributor. Please verify";

        try {
            SelectiveEditingResponse response = new SelectiveEditingResponse(responseJSON);
            response.parseSelectiveEditingQueryResponse();
        } catch (Exception e) {
            String actualMessage = e.getMessage();
            assertEquals(expectedErrorMessage, actualMessage);
            assertTrue(true);

        }
    }

    @Test
    void selectiveEditingConfigDetails_ThrowsAnException_when_no_matching_cell_number_in_cell_detail_config(){

        String responseJSON = "{\"data\":{\"allContributors\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"reference\":\"49900534932\",\"frozenturnover\":99999,\"resultscellnumber\":1,\"domain\":\"1\",\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}},{\"survey\":\"023\",\"period\":\"201903\",\"reference\":\"49900534932\",\"resultscellnumber\":null,\"domain\":null,\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}}]},\"allSelectiveeditingconfigs\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"1\",\"questioncode\":\"20\",\"threshold\":0.002,\"estimate\":100000000},{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"1\",\"questioncode\":\"21\",\"threshold\":0.002,\"estimate\":100000000}]},\"allCelldetails\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"cellnumber\":2,\"designweight\":2}]}}}";
        String expectedErrorMessage = "Problem in parsing Selective Editing GraphQL responses There are no design weight for a given cell number . Please verify";

        try {
            SelectiveEditingResponse response = new SelectiveEditingResponse(responseJSON);
            response.parseSelectiveEditingQueryResponse();
        } catch (Exception e) {
            String actualMessage = e.getMessage();
            assertEquals(expectedErrorMessage, actualMessage);
            assertTrue(true);

        }
    }

}

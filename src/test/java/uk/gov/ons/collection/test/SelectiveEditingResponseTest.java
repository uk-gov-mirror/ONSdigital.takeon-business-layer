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
    void selectiveEditingConfigDetailsDetails_validJSONDataT(){

        String responseJSON = "{\"data\":{\"allContributors\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"reference\":\"49900534932\",\"resultscellnumber\":1,\"domain\":1,\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}},{\"survey\":\"023\",\"period\":\"201903\",\"reference\":\"49900534932\",\"resultscellnumber\":null,\"domain\":null,\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}}]},\"allSelectiveeditingconfigs\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"1\",\"questioncode\":\"20\",\"threshold\":0.002,\"estimate\":100000000},{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"1\",\"questioncode\":\"21\",\"threshold\":0.002,\"estimate\":100000000}]},\"allCelldetails\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"cellnumber\":1,\"designweight\":2}]}}}";
        String expectedOutput = "{\"reference\":\"49900534932\",\"designweight\":2,\"resultscellnumber\":1,\"period\":\"201904\",\"domain\":1,\"survey\":\"023\",\"domainconfig\":[{\"currentresponse\":\"\",\"questioncode\":\"20\",\"estimate\":100000000,\"threshold\":0.002,\"previousresponse\":\"\"},{\"currentresponse\":\"\",\"questioncode\":\"21\",\"estimate\":100000000,\"threshold\":0.002,\"previousresponse\":\"\"}]}";

        try {
            SelectiveEditingResponse response = new SelectiveEditingResponse(responseJSON);
            String actualOutput = response.parseSelectiveEditingQueryResponse();
            System.out.println("Output to be sent to Lambda: " + actualOutput);
            assertEquals(expectedOutput, actualOutput);

        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void selectiveEditingConfigDetails_ThrowsAnException_when_domain_is_null(){

        String responseJSON = "{\"data\":{\"allContributors\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"reference\":\"49900534932\",\"resultscellnumber\":null,\"domain\":null,\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}},{\"survey\":\"023\",\"period\":\"201903\",\"reference\":\"49900534932\",\"resultscellnumber\":null,\"domain\":null,\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}}]},\"allSelectiveeditingconfigs\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"1\",\"questioncode\":\"20\",\"threshold\":0.002,\"estimate\":100000000},{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"1\",\"questioncode\":\"21\",\"threshold\":0.002,\"estimate\":100000000}]},\"allCelldetails\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"cellnumber\":1,\"designweight\":2}]}}}";
        String expectedErrorMessage = "Problem in parsing Selective Editing GraphQL responses Either Domain or Results Cell Number is null in Contributor table. Please verify";

        try {
            SelectiveEditingResponse response = new SelectiveEditingResponse(responseJSON);
            response.parseSelectiveEditingQueryResponse();
        } catch (Exception e) {
            String actualMessage = e.getMessage();
            System.out.println("Error Message: "+actualMessage);
            assertEquals(expectedErrorMessage, actualMessage);
            assertTrue(true);

        }
    }



    @Test
    void selectiveEditingConfigDetails_ThrowsAnException_when_no_domainconfig(){

        String responseJSON = "{\"data\":{\"allContributors\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"reference\":\"49900534932\",\"resultscellnumber\":1,\"domain\":1,\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}},{\"survey\":\"023\",\"period\":\"201903\",\"reference\":\"49900534932\",\"resultscellnumber\":null,\"domain\":null,\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}}]},\"allSelectiveeditingconfigs\":{\"nodes\":[]},\"allCelldetails\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"cellnumber\":1,\"designweight\":2}]}}}";
        String expectedErrorMessage = "Problem in parsing Selective Editing GraphQL responses There is no domain configuration. Please verify";

        try {
            SelectiveEditingResponse response = new SelectiveEditingResponse(responseJSON);
            response.parseSelectiveEditingQueryResponse();
        } catch (Exception e) {
            String actualMessage = e.getMessage();
            System.out.println("Error Message: "+actualMessage);
            assertEquals(expectedErrorMessage, actualMessage);
            assertTrue(true);

        }
    }

    @Test
    void selectiveEditingConfigDetails_ThrowsAnException_when_no_celldetailconfig(){

        String responseJSON = "{\"data\":{\"allContributors\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"reference\":\"49900534932\",\"resultscellnumber\":1,\"domain\":1,\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}},{\"survey\":\"023\",\"period\":\"201903\",\"reference\":\"49900534932\",\"resultscellnumber\":null,\"domain\":null,\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}}]},\"allSelectiveeditingconfigs\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"1\",\"questioncode\":\"20\",\"threshold\":0.002,\"estimate\":100000000},{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"1\",\"questioncode\":\"21\",\"threshold\":0.002,\"estimate\":100000000}]},\"allCelldetails\":{\"nodes\":[]}}}";
        String expectedErrorMessage = "Problem in parsing Selective Editing GraphQL responses There is no celldetail configuration. Please verify";

        try {
            SelectiveEditingResponse response = new SelectiveEditingResponse(responseJSON);
            response.parseSelectiveEditingQueryResponse();
        } catch (Exception e) {
            String actualMessage = e.getMessage();
            System.out.println("Error Message: "+actualMessage);
            assertEquals(expectedErrorMessage, actualMessage);
            assertTrue(true);

        }
    }

    @Test
    void selectiveEditingConfigDetails_ThrowsAnException_when_no_matching_domain_in_domainconfig(){

        String responseJSON = "{\"data\":{\"allContributors\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"reference\":\"49900534932\",\"resultscellnumber\":1,\"domain\":1,\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}},{\"survey\":\"023\",\"period\":\"201903\",\"reference\":\"49900534932\",\"resultscellnumber\":null,\"domain\":null,\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}}]},\"allSelectiveeditingconfigs\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"2\",\"questioncode\":\"20\",\"threshold\":0.002,\"estimate\":100000000},{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"2\",\"questioncode\":\"21\",\"threshold\":0.002,\"estimate\":100000000}]},\"allCelldetails\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"cellnumber\":1,\"designweight\":2}]}}}";
        String expectedErrorMessage = "Problem in parsing Selective Editing GraphQL responses There are no thresholds for a given domain in the contributor. Please verify";

        try {
            SelectiveEditingResponse response = new SelectiveEditingResponse(responseJSON);
            response.parseSelectiveEditingQueryResponse();
        } catch (Exception e) {
            String actualMessage = e.getMessage();
            System.out.println("Error Message: "+actualMessage);
            assertEquals(expectedErrorMessage, actualMessage);
            assertTrue(true);

        }
    }

    @Test
    void selectiveEditingConfigDetails_ThrowsAnException_when_no_matching_cell_number_in_cell_detail_config(){

        String responseJSON = "{\"data\":{\"allContributors\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"reference\":\"49900534932\",\"resultscellnumber\":1,\"domain\":1,\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}},{\"survey\":\"023\",\"period\":\"201903\",\"reference\":\"49900534932\",\"resultscellnumber\":null,\"domain\":null,\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[]}}]},\"allSelectiveeditingconfigs\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"1\",\"questioncode\":\"20\",\"threshold\":0.002,\"estimate\":100000000},{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"1\",\"questioncode\":\"21\",\"threshold\":0.002,\"estimate\":100000000}]},\"allCelldetails\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"cellnumber\":2,\"designweight\":2}]}}}";
        String expectedErrorMessage = "Problem in parsing Selective Editing GraphQL responses There are no design weight for a given cell number . Please verify";

        try {
            SelectiveEditingResponse response = new SelectiveEditingResponse(responseJSON);
            response.parseSelectiveEditingQueryResponse();
        } catch (Exception e) {
            String actualMessage = e.getMessage();
            System.out.println("Error Message: "+actualMessage);
            assertEquals(expectedErrorMessage, actualMessage);
            assertTrue(true);

        }
    }

}

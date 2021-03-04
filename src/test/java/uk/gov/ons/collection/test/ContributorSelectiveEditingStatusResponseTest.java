package uk.gov.ons.collection.test;
import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.ContributorSelectiveEditingStatusResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContributorSelectiveEditingStatusResponseTest {

    String noContributorGraphQLOutput = "{\n" +
            "  \"data\": {\n" +
            "    \"allContributors\": {\n" +
            "      \"nodes\": []\n" +
            "    }\n" +
            "  }\n" +
            "}";

    @Test
    void verify_bpm_ExpectedBpmContract_with_ActualBpmContract(){
        try {
            String graphQLOutput = "{\"data\":{\"allContributors\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201903\",\"reference\":\"12000534932\",\"formid\":5,\"status\":\"Check needed\",\"contributorselectiveeditingByReferenceAndPeriodAndSurvey\":{\"reference\":\"12000534932\",\"period\":\"201903\",\"survey\":\"023\",\"flag\":\"P\"}}]}}}";
            ContributorSelectiveEditingStatusResponse contributorStatusResponse = new ContributorSelectiveEditingStatusResponse(graphQLOutput);
            String expectedBpmContract = "{\"reference\":\"12000534932\",\"period\":\"201903\",\"survey\":\"023\",\"validationPassed\":true,\"BPMvalidationCallID\":\"0\",\"status\":\"CHECK_NEEDED\",\"selective_editing_flag\":\"PASSED\"}";
            String expectedBpmContractNoSelectiveEditingFlag = "{\"reference\":\"12000567891\",\"period\":\"201904\",\"survey\":\"023\",\"validationPassed\":true,\"BPMvalidationCallID\":\"0\",\"status\":\"OVERRIDDEN\",\"selective_editing_flag\":\"\"}";
            String noSelectiveEditingResponse = "{\"data\":{\"allContributors\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"reference\":\"12000567891\",\"formid\":5,\"status\":\"Clear - overridden\",\"contributorselectiveeditingByReferenceAndPeriodAndSurvey\":null}]}}}";
            String actualBpmContract = contributorStatusResponse.parseContributorStatusQueryResponse().toString();
            assertEquals(expectedBpmContract, actualBpmContract);

            ContributorSelectiveEditingStatusResponse contributorNoSelectiveEditingResponse = new ContributorSelectiveEditingStatusResponse(noSelectiveEditingResponse);
            String actualBPMContractWithEmptyFlag = contributorNoSelectiveEditingResponse.parseContributorStatusQueryResponse().toString();
            assertEquals(expectedBpmContractNoSelectiveEditingFlag, actualBPMContractWithEmptyFlag);

        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    void contributorStatus_ThrowsAnException_when_no_contributor(){

        String expectedErrorMessage = "Problem in parsing Contributor Status GraphQL responses There is no contributor for a given survey, reference and periods. Please verify";
        try {
            ContributorSelectiveEditingStatusResponse contributorStatusResponse = new ContributorSelectiveEditingStatusResponse(noContributorGraphQLOutput);
            contributorStatusResponse.parseContributorStatusQueryResponse();
        } catch (Exception e) {
            String actualMessage = e.getMessage();
            assertEquals(expectedErrorMessage, actualMessage);
            assertTrue(true);

        }
    }

    @Test
    void contributorStatusDetails_ThrowsAnException__when_invalid_json(){

        String expectedErrorMessage = "Given string could not be converted/processed: org.json.JSONException:";

        String invalidJson = "{\"data\":\"allContributors\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201903\",\"reference\":\"12000534932\",\"formid\":5,\"status\":\"Check needed\",\"contributorselectiveeditingByReferenceAndPeriodAndSurvey\":{\"reference\":\"12000534932\",\"period\":\"201903\",\"survey\":\"023\",\"flag\":\"P\"}}]}}}";
        try {
            ContributorSelectiveEditingStatusResponse contributorStatusResponse = new ContributorSelectiveEditingStatusResponse(invalidJson);
            contributorStatusResponse.parseContributorStatusQueryResponse();
        } catch (Exception e) {
            String actualMessage = e.getMessage();
            assertTrue(actualMessage.contains(expectedErrorMessage));
            assertTrue(true);
        }
    }



}

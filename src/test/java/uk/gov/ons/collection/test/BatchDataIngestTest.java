package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.exception.InvalidJsonException;
import uk.gov.ons.collection.service.BatchDataIngest;
import uk.gov.ons.collection.service.GraphQlService;
import org.json.JSONArray;
import org.json.JSONObject;

import static org.junit.jupiter.api.Assertions.*;

public class BatchDataIngestTest {

    @Test
    void buildBatchDataJsonArray_WrongArrayName_throwsException() {

        GraphQlService qlService = new GraphQlService();
        var inputJson = "{ 'batc_data':         \n" +
                "\n" +
                "                    [   \n" +
                "                        {\n" +
                "                            'user': 'fisdba',\n" +
                "                            'reference': '12345678000', \n" +
                "                            'period': '201801', \n" +
                "                            'survey': '999A',\n" +
                "                            'responses': [\n" +
                "                                {'questioncode': '1000', 'response': '2', 'instance': '0'},    \n" +
                "                                {'questioncode': '1001', 'response': '3', 'instance': '0'}]\n" +
                "                        }\n" +
                "                    ]\n" +
                "            \n" +
                "                }";
        assertThrows(InvalidJsonException.class, () -> new BatchDataIngest(inputJson, qlService));
    }

    @Test
    void verify_ContributorExists_TrueAndFalse() {


        var contributorJson1 = "{\"data\":{\"allContributors\":{\"nodes\":[]}}}";
        var contributorJson2 = "{\"data\":{\"allContributors\":{\"nodes\":[{\"reference\":\"12345678001\",\"period\":\"201801\",\"survey\":\"999A\"}]}}}";
        BatchDataIngest batchData = new BatchDataIngest();
        try {
            assertTrue(batchData.isContributorEmpty(contributorJson1));
            assertFalse(batchData.isContributorEmpty(contributorJson2));
        } catch(Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void verify_ContributorExists_inValidJson_throwsException() {

        var contributorJson1 = "{\"data\":{\"allContributors\":{\"nods\":[]}}}";
        assertThrows(InvalidJsonException.class, () -> new BatchDataIngest().isContributorEmpty(contributorJson1));
    }

    @Test
    void verify_ContributorArray_TrueAndFalse() {


        var contributorEmptyJson = "{\"data\":{\"allContributors\":{\"nodes\":[]}}}";
        var contributorExistsJson = "{\"data\":{\"allContributors\":{\"nodes\":[{\"reference\":\"12345678001\",\"period\":\"201801\",\"survey\":\"999A\"}]}}}";
        BatchDataIngest batchData = new BatchDataIngest();
        try {
            assertTrue(batchData.getContributorArray(contributorEmptyJson).isEmpty());
            assertFalse(batchData.getContributorArray(contributorExistsJson).length() == 0);
        } catch(Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void verify_ContributorStatus_inValidJson_throwsException() {

        var contributorExistsJson = "{\"data\":{\"allContributors\":{\"nodes\":[{\"reference\":\"12345678001\",\"period\":\"201801\",\"survey\":\"999A\"}]}}}";

        JSONObject referenceExistsObject;
        JSONArray contributorArray;

        referenceExistsObject = new JSONObject(contributorExistsJson);
        contributorArray = referenceExistsObject.getJSONObject("data")
                .getJSONObject("allContributors").getJSONArray("nodes");

        assertThrows(InvalidJsonException.class, () -> new BatchDataIngest().getContributorStatus(contributorArray));
    }

    @Test
    void verify_ContributorStatus_Exists() {

        var contributorExistsJson = "{\"data\":{\"allContributors\":{\"nodes\":[{\"reference\":\"12345678001\",\"period\":\"201801\",\"survey\":\"999A\",\"status\":\"Form Saved\"}]}}}";

        JSONObject referenceExistsObject;
        JSONArray contributorArray;

        referenceExistsObject = new JSONObject(contributorExistsJson);
        contributorArray = referenceExistsObject.getJSONObject("data")
                .getJSONObject("allContributors").getJSONArray("nodes");
        try {
            assertEquals("Form Saved", new BatchDataIngest().getContributorStatus(contributorArray));
        } catch(Exception e) {
            assertTrue(false);
        }
    }
}

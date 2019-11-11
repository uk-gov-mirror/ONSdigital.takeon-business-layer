package uk.gov.ons.collection.test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.ResponseData;
import uk.gov.ons.collection.exception.InvalidJsonException;
import uk.gov.ons.collection.service.CompareUiAndCurrentResponses;
import uk.gov.ons.collection.utilities.UpsertResponse;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class UpsertResponseTest {


    @Test
    void buildUpdateStatusQuery_validJson_validRetrieveQueryGenerated() {

        var inputJson = "{\"reference\":\"12345678000\",\"period\": \"201801\",\"survey\": \"999A\",\"responses\": [{\"instance\": 0,\"questioncode\": \"1000\",\"response\":\"test\"}]}";

        try {
            var upsertResponse = new UpsertResponse(inputJson);
            var query = upsertResponse.updateContributorStatus();
            var expectedQuery = "{\"query\": \"mutation updateStatus($period: String!, $reference: " +
                    "String!, $survey: String!, $status: String!) " +
                    "{updateContributorByReferenceAndPeriodAndSurvey(input: {reference: $reference, " +
                    "period: $period, survey: $survey, contributorPatch: {status: $status}}) {contributor " +
                    "{ reference period survey status }}}\",\"variables\": {\"reference\": \"12345678000\"" +
                    ",\"period\": \"201801\",\"survey\": \"999A\",\"status\": \"Form Saved\"}}";
            assertEquals(expectedQuery, query);
        } catch (Exception e) {
            assertTrue(false);

        }
    }

    @Test
    void buildRetrieveResponseQuery_validJson_validRetrieveQueryGenerated() {

        var inputJson = "{\"reference\":\"12345678000\",\"period\": \"201801\",\"survey\": \"999A\",\"responses\": [{\"instance\": 0,\"questioncode\": \"1000\",\"response\":\"test\"}]}";

        try {
            var upsertResponse = new UpsertResponse(inputJson);
            var query = upsertResponse.buildRetrieveOldResponseQuery();
            var expectedQuery = "{\"query\" : \"query filteredResponse {allResponses(condition: {reference: \\\"12345678000\\\"," +
                    "period: \\\"201801\\\",survey: \\\"999A\\\"}){nodes{reference,period,survey,questioncode,response,instance," +
                    "createdby,createddate,lastupdatedby,lastupdateddate}}}\"}";
            assertEquals(expectedQuery, query);
        } catch (Exception e) {
            assertTrue(false);

        }
    }


    @Test
    void buildUpsertByArrayQuery_validJson_validUpsertQueryGenerated() {

        var inputJson = "{\"reference\":\"12345678000\",\"period\": \"201801\",\"survey\": \"999A\",\"responses\": [{\"instance\": 0,\"questioncode\": \"1000\",\"response\":\"test\"}]}";

        try {
            var upsertResponse = new UpsertResponse(inputJson);
            var query = upsertResponse.buildUpsertByArrayQuery();
            var expectedQuery = "{\"query\" : \"mutation saveResponse {saveresponsearray(input: {arg0: " +
                    "[{reference: \\\"12345678000\\\",period: \\\"201801\\\",survey: \\\"999A\\\"," +
                    "questioncode: \\\"1000\\\",instance: 0,response: \\\"test\\\",createdby: \\\"fisdba\\\"," +
                    "createddate: \\\"" + upsertResponse.getTime() + "\\\"}]}){clientMutationId}}\"}";

            assertEquals(expectedQuery, query);
        } catch (Exception e) {
            assertTrue(false);

        }
    }

    @Test
    void buildUpsertByArrayQuery_multidimensional_validUpsertQueryGenerated() {

        var inputJson = "{\"reference\":\"12345678000\",\"period\": \"201801\",\"survey\": \"999A\",\"responses\": " +
                "[{\"instance\": 0,\"questioncode\": \"1000\",\"response\":\"test\"}," +
                "{\"instance\": 0,\"questioncode\": \"2000\",\"response\":\"test\"}]}";

        try {
            var upsertResponse = new UpsertResponse(inputJson);
            var query = upsertResponse.buildUpsertByArrayQuery();
            var expectedQuery = "{\"query\" : \"mutation saveResponse {saveresponsearray" +
                    "(input: {arg0: [{reference: \\\"12345678000\\\",period: \\\"201801\\\"," +
                    "survey: \\\"999A\\\",questioncode: \\\"1000\\\",instance: 0,response: \\\"test\\\"," +
                    "createdby: \\\"fisdba\\\",createddate: \\\"" + upsertResponse.getTime() + "\\\"}," +
                    "{reference: \\\"12345678000\\\",period: \\\"201801\\\"," +
                    "survey: \\\"999A\\\",questioncode: \\\"2000\\\",instance: 0,response: \\\"test\\\"," +
                    "createdby: \\\"fisdba\\\",createddate: \\\"" + upsertResponse.getTime() + "\\\"}]" +
                    "}){clientMutationId}}\"}";

            assertEquals(expectedQuery, query);
        } catch (Exception e) {
            assertTrue(false);

        }
    }

    @Test
    void buildUpsertByArrayQuery_validJsonMissingAttribute_throwsException() {
        var inputJson = "{\"response\":[{\"reference\":\"12345678000\",\"period\": \"201801\"," +
                "\"survey\": \"999A\",\"questioncode\": \"1000\",\"instance\": 0}]}";
        assertThrows(InvalidJsonException.class, () -> new UpsertResponse(inputJson).buildUpsertByArrayQuery());
    }

    @Test
    void testCompareUiAndCurrentResponses() {

        //UI Responses
        String json1 = "{\"responses\": [{\"question\": \"1000\", \"response\": \"1\", \"instance\": 0}, " +
                "{\"question\": \"1001\", \"response\": \"2\", \"instance\": 0}, " +
                "{\"question\": \"2000\", \"response\": \"3\", \"instance\": 0}, " +
                "{\"question\": \"3000\", \"response\": \"\", \"instance\": 0}, " +
                "{\"question\": \"4000\", \"response\": \"\", \"instance\": 0}, " +
                "{\"question\": \"4001\", \"response\": \"\", \"instance\": 0}]," +
                " \"user\": \"fisdba\", \"reference\": \"12345678000\", \"period\": \"201801\", \"survey\": \"999A\"}";
        //Old Responses
        String json2 = "[{\"reference\":\"12345678000\",\"period\":\"201801\",\"instance\":0,\"createdby\":\"fisdba\"," +
                "\"createddate\":\"2019-11-08T16:11:16.009+00:00\",\"lastupdateddate\":\"2019-11-08T16:11:16.001+00:00\"," +
                "\"response\":\"1\",\"questioncode\":\"1000\",\"lastupdatedby\":\"fisdba\",\"survey\":\"999A\"}," +
                "{\"reference\":\"12345678000\",\"period\":\"201801\",\"instance\":0,\"createdby\":\"fisdba\"," +
                "\"createddate\":\"2019-11-08T16:11:16.009+00:00\",\"lastupdateddate\":\"2019-11-08T16:11:16.001+00:00\"," +
                "\"response\":\"1\",\"questioncode\":\"1001\",\"lastupdatedby\":\"fisdba\",\"survey\":\"999A\"}" +
                "]";

        String expectedCondition = "{reference: \\\"12345678000\\\",period: \\\"201801\\\",survey: \\\"999A\\\",questioncode: \\\"1001\\\"," +
                "instance: 0,response: \\\"2\\\"";
        String expectedCondition1 = "{reference: \\\"12345678000\\\",period: \\\"201801\\\",survey: \\\"999A\\\",questioncode: \\\"1000\\\"," +
                "instance: 0,response: \\\"1\\\"";

        int responseChangeCount = 2;

        List<ResponseData> currentResponseEntities;
        JSONObject updatedResponsesJson = new JSONObject(json1);
        CompareUiAndCurrentResponses responseComparison;

        try {
            var upsertResponse = new UpsertResponse(json1);
            var outputArray = new JSONArray(json2);
            currentResponseEntities = upsertResponse.buildCurrentResponseEntities(outputArray);
            responseComparison = new CompareUiAndCurrentResponses(currentResponseEntities, updatedResponsesJson);
            List<ResponseData> responsesToPassToDatabase = responseComparison.getFinalConsolidatedResponses();
            //Veryfying how many number of elements changed.
            assertEquals(responseChangeCount, responsesToPassToDatabase.size());

            JSONArray jsonArray = new JSONArray(responsesToPassToDatabase);
            var upsertSaveResponse = new UpsertResponse(jsonArray);
            var saveQuery = upsertSaveResponse.buildConsolidateUpsertByArrayQuery();
            //Save Query contains current updated date. So I cannot compare the actual query with expected query.
            // Verifying whether the query which was built has database function or not
            assertTrue(saveQuery.contains("mutation saveResponse {saveresponsearray(input: {arg0:"));
            //The element Question Code 1001 changed so it should be in Save Query
            assertTrue(saveQuery.contains(expectedCondition));
            //The element Question Code 1000 has not changed and it should not be in Save Query
            assertFalse(saveQuery.contains(expectedCondition1));

        } catch (Exception e) {
            assertTrue(false);
        }

    }



}
package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.exception.InvalidJsonException;
import uk.gov.ons.collection.utilities.UpsertResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class UpsertResponseTest {


    @Test
    void buildUpdateStatusQuery_validJson_validRetrieveQueryGenerated() {
//        var inputJson = "{\"response\":[{\"reference\":\"12345678000\",\"period\": \"201801\"," +
//                "\"survey\": \"999A\",\"questioncode\": \"1000\",\"instance\": 0,\"response\": \"test\"}]}";

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
//        var inputJson = "{\"response\":[{\"reference\":\"12345678000\",\"period\": \"201801\"," +
//                "\"survey\": \"999A\",\"questioncode\": \"1000\",\"instance\": 0,\"response\": \"test\"}]}";

        var inputJson = "{\"reference\":\"12345678000\",\"period\": \"201801\",\"survey\": \"999A\",\"responses\": [{\"instance\": 0,\"questioncode\": \"1000\",\"response\":\"test\"}]}";

        try {
            var upsertResponse = new UpsertResponse(inputJson);
            var query = upsertResponse.buildRetrieveResponseQuery();
            var expectedQuery = "{\"query\" : \"query filteredResponse {allResponses(condition: {reference: \\\"12345678000\\\"," +
                    "period: \\\"201801\\\",survey: \\\"999A\\\"}){nodes{reference,period,survey,questioncode,response," +
                    "createdby,createddate,lastupdatedby,lastupdateddate}}}\"}";
            assertEquals(expectedQuery, query);
        } catch (Exception e) {
            assertTrue(false);

        }
    }


    @Test
    void buildUpsertByArrayQuery_validJson_validUpsertQueryGenerated() {
//        var inputJson = "{\"response\":[{\"reference\":\"12345678000\",\"period\": \"201801\"," +
//                "\"survey\": \"999A\",\"questioncode\": \"1000\",\"instance\": 0,\"response\": \"test\"}]}";

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
    void buildUpsertByArrayQuery_validJsonMissingAttribute_throwsException() {
        var inputJson = "{\"response\":[{\"reference\":\"12345678000\",\"period\": \"201801\"," +
                "\"survey\": \"999A\",\"questioncode\": \"1000\",\"instance\": 0}]}";
        assertThrows(InvalidJsonException.class, () -> new UpsertResponse(inputJson).buildUpsertByArrayQuery());
    }

}
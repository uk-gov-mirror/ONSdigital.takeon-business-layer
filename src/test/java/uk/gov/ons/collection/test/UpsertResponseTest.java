package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.utilities.UpsertResponse;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class UpsertResponseTest {

    @Test
    void buildInsertByArrayQuery_validJson_validInsertQueryGenerated() {
        var inputJson = "{\"response\":[{\"reference\":\"12345678000\",\"period\": \"201801\"," +
                "\"survey\": \"999A\",\"questioncode\": \"1000\",\"instance\": 0,\"response\": \"test\"}," +
                "{\\\"reference\\\":\\\"12345678000\\\",\\\"period\\\": \\\"201801\\\",\" +\n" +
                "\"\\\"survey\\\": \\\"999A\\\",\\\"questioncode\\\": \\\"3000\\\",\\\"instance\\\": " +
                "0,\\\"response\\\": \\\"test\\\"}" +
                "]}";
        try {
            var upsertResponse = new UpsertResponse(inputJson);
            var query = upsertResponse.buildUpsertByArrayQuery();
            var expectedQuery = "{\"query\" : \"mutation saveResponse {saveresponsearray(input: {arg0: " +
                    "[{reference: \\\"12345678000\\\", period: \\\"201801\\\", survey: \\\"999A\\\", " +
                    "questioncode: \\\"1000\\\", instance: 0, response: \\\"test\\\", createdby: \\\"fisdba\\\", " +
                    "createddate: \\\"" + upsertResponse.getTime() + "\\\"}, {reference: \\\"12345678000\\\", " +
                    "period: \\\"201801\\\", survey: \\\"999A\\\", questioncode: \\\"3000\\\", instance: 0, " +
                    "response: \\\"test\\\", createdby: \\\"fisdba\\\", " +
                    "createddate: \\\"" + upsertResponse.getTime() + "\\\"}]}) {clientMutationId}}\"}";
            System.out.println("try");
            assertEquals(expectedQuery, query);
        } catch (Exception e) {
            System.out.println("catch");
            assertTrue(false);

        }
    }

}
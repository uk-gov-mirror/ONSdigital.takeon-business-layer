package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.controller.qlQueryResponse;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

public class qlQueryResponseTest {

    @Test
    void parse_nullInputJson_returnErrorJSON() {
        String expectedOutput = "{\"error\":\"Invalid response from graphQL\"}";
        qlQueryResponse response = new qlQueryResponse(null);
        assertEquals(expectedOutput, response.parse());
    }

    @Test
    void parse_validButNoContributorData_givesValidEmptyContributor() {
        String inputString = "{\"data\": {\"allContributors\": {\"nodes\": [], \"pageInfo\": {\"hasNextPage\": false," +
            "\"hasPreviousPage\": false,\"startCursor\": null,\"endCursor\": null },\"totalCount\": 0}}}";
        String outputString = "{\"data\": [],\"pageInfo\": {\"hasNextPage\": false," +
            "\"hasPreviousPage\": false,\"endCursor\": null,\"totalCount\": 0,\"startCursor\": null}}";
        qlQueryResponse response = new qlQueryResponse(inputString);
        assertEquals(outputString, response.parse());
    }

    @Test
    void parse_attributesPresent_returnValidParsedJsonString() {
        String inputString = 
            "{\"data\": {" +
                "\"allContributors\": {" +
                    "\"nodes\": " +
                        "[{\"reference\": \"4990012\",\"period\": \"201211\",\"survey\": \"066 \"}," +
                            "{\"reference\": \"4990012\",\"period\": \"201212\",\"survey\": \"066 \"}]," +
                    "\"pageInfo\": " +
                        "{\"hasNextPage\": true,\"hasPreviousPage\": false,\"startCursor\": \"base64string\",\"endCursor\": \"base64string\"}," +
                    "\"totalCount\": 30 " +
            "}}}";

        String expectedOutput = 
                "{\"data\": " +
                       "[{\"reference\": \"4990012\",\"period\": \"201211\",\"survey\": \"066 \"}," +
                        "{\"reference\": \"4990012\",\"period\": \"201212\",\"survey\": \"066 \"}]," +
                  "\"pageInfo\": " +
                        "{\"hasNextPage\": true,\"hasPreviousPage\": false,\"endCursor\": \"base64string\",\"totalCount\": 30,\"startCursor\": \"base64string\"}" +
                "}";

        qlQueryResponse response = new qlQueryResponse(inputString);
        assertEquals(expectedOutput, response.parse());
    }

    @Test
    void parse_invalidJSON_returnErrorJSON(){
        String inputString = "fdgsgsrdgf";
        String outputString = "{\"error\":\"Invalid response from graphQL\"}";
        qlQueryResponse response = new qlQueryResponse(inputString);
        assertEquals(outputString, response.parse());
    }

    @Test
    void parse_missingContributors_returnErrorJSON() {
        String inputString = "{\"data\": {\"nodes\": []}}}";
        String outputString = "{\"error\":\"Invalid response from graphQL\"}";
        qlQueryResponse response = new qlQueryResponse(inputString);
        assertEquals(outputString, response.parse());
    }


    @Test
    void parseForPeriodOffset_onePeriodOffSet_returnsOneItem(){
        String jsonInput = "{ " +
            "\"data\": { " +
                "\"allContributors\": { " +
                    "\"nodes\": [{ " +
                        "\"formByFormid\": { " +
                            "\"validationformsByFormid\": { " +
                                "\"nodes\": [{ " +
                                    "\"validationruleByRule\": { " +
                                        "\"validationperiodsByRule\": { " +
                                            "\"nodes\": [{ \"periodoffset\": 3 }]}}}]}}}]}}}";
        ArrayList<Integer> expectedOutput = new ArrayList<>(Arrays.asList(3));
        qlQueryResponse response = new qlQueryResponse(jsonInput);
        assertEquals(expectedOutput, response.parseForPeriodOffset());
        
    }

}
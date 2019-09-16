package uk.gov.ons.collection.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.controller.qlQueryResponse;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Ql Response tests")
public class qlQueryResponseTest {

    @Test
    void parse_nullInputJson_returnErrorJSON() {
        String expectedOutput = "{\"error\":\"Invalid response from graphQL\"}";
        qlQueryResponse response = new qlQueryResponse(null);
        assertEquals(expectedOutput, response.parse());
    }

    @Test
    void parse_validButNoContributorData_givesValidEmptyContributor() {
        String inputString = "{\"data\": {\"allContributors\": {\"nodes\": []}}}";
        String outputString = "{\"data\": []}";
        qlQueryResponse response = new qlQueryResponse(inputString);
        assertEquals(outputString, response.parse());
    }

    @Test
    void parse_attributesPresent_returnValidParsedJsonString() {
        String inputString = "{" +
                "\"data\": {" +
                    "\"allContributors\": {" +
                        "\"nodes\": [" +
                            "{\"reference\": \"4990012\",\"period\": \"201211\",\"survey\": \"066 \"}," +
                            "{\"reference\": \"4990012\",\"period\": \"201212\",\"survey\": \"066 \"}" +
                "]}}}";

        String expectedOutput = "{" +
                "\"data\": [" +
                    "{\"reference\": \"4990012\",\"period\": \"201211\",\"survey\": \"066 \"}," +
                    "{\"reference\": \"4990012\",\"period\": \"201212\",\"survey\": \"066 \"}" +
                "]}";

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
}
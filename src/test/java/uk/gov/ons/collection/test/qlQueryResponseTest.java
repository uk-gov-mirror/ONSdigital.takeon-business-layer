package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.utilities.QlQueryResponse;
import uk.gov.ons.collection.utilities.RelativePeriod;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class qlQueryResponseTest {

    @Test
    void parse_nullInputJson_returnErrorJSON() {
        String expectedOutput = "{\"error\":\"Invalid response from graphQL\"}";
        QlQueryResponse response = new QlQueryResponse(null);
        assertEquals(expectedOutput, response.parse());
    }
    
    @Test
    void parse_validButNoContributorData_givesValidEmptyContributor() {
        String inputString = "{\"data\": {\"allContributors\": {\"nodes\": [], \"pageInfo\": {\"hasNextPage\": false," +
            "\"hasPreviousPage\": false,\"startCursor\": null,\"endCursor\": null },\"totalCount\": 0}}}";
        String outputString = "{\"data\": [],\"pageInfo\": {\"hasNextPage\": false," +
            "\"hasPreviousPage\": false,\"endCursor\": null,\"totalCount\": 0,\"startCursor\": null}}";
        QlQueryResponse response = new QlQueryResponse(inputString);
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

        QlQueryResponse response = new QlQueryResponse(inputString);
        assertEquals(expectedOutput, response.parse());
    }

    @Test
    void parse_invalidJSON_returnErrorJSON() {
        String inputString = "fdgsgsrdgf";
        String outputString = "{\"error\":\"Invalid response from graphQL\"}";
        QlQueryResponse response = new QlQueryResponse(inputString);
        assertEquals(outputString, response.parse());
    }

    @Test
    void parse_missingContributors_returnErrorJSON() {
        String inputString = "{\"data\": {\"nodes\": []}}}";
        String outputString = "{\"error\":\"Invalid response from graphQL\"}";
        QlQueryResponse response = new QlQueryResponse(inputString);
        assertEquals(outputString, response.parse());
    }
   
    @Test
    void checkRelativePeriods() {
        String startingPeriod = "201209";
        List<Integer> inputList = new ArrayList<>(Arrays.asList(0,1,2));
        List<String> expectedPeriods = new ArrayList<>(Arrays.asList("201209","201206","201203"));
        List<String> outputPeriods = new ArrayList<>();
        try { RelativePeriod rp = new RelativePeriod("Quarterly");
            for ( int i = 0; i < inputList.size(); i++) {
                String idbrPeriod = rp.calculateRelativePeriod(inputList.get(i).intValue(), startingPeriod);
                outputPeriods.add(idbrPeriod);
            }
        }
        catch (Exception e) {
            System.out.println(e);
        }
        assertEquals(expectedPeriods, outputPeriods);
    }

}
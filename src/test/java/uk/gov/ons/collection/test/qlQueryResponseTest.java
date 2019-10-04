package uk.gov.ons.collection.test;

import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.controller.qlQueryResponse;
import uk.gov.ons.collection.utilities.RelativePeriod;

import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
                "\"allValidationperiods\": { " +
                    "\"nodes\": [{ \"periodoffset\": 3 }, { \"periodoffset\": 2 }]}}}";
        ArrayList<Integer> expectedOutput = new ArrayList<>(Arrays.asList(3, 2));
        qlQueryResponse response = new qlQueryResponse(jsonInput);
        assertEquals(expectedOutput, response.parseForPeriodOffset());
        
    }

    
    @Test
    void checkRelativePeriods() {
        String startingPeriod = "201209";
        List<Integer> inputList = new ArrayList<>(Arrays.asList(0,1,2));
        List<String> expectedPeriods = new ArrayList<>(Arrays.asList("201209","201206","201206"));
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

    @Test
     void getFormID_validData_works() {
        String jsonInput = "{\"data\":{\"allContributors\":{\"nodes\":[{\"reference\":\"12345678001\",\"formid\":1,\"period\":\"201801\",\"surveyBySurvey\":{\"periodicity\":\"Monthly\"},\"survey\":\"999A\"}]}}}";
        qlQueryResponse response = new qlQueryResponse(jsonInput);
        assertEquals(1, response.getFormID());    
    }

    @Test
    void getResponses_validData_works() {
        String jsonInput = "{\"data\":{\"allContributors\":{\"nodes\":[{\"formid\":1,\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":" + 
            "[{\"reference\":\"12345678003\",\"period\":\"201801\",\"instance\":0,\"response\":\"\",\"questioncode\":\"1001\",\"survey\":\"999A\"}," +
            "{\"reference\":\"12345678003\",\"period\":\"201801\",\"instance\":0,\"response\":\"0\",\"questioncode\":\"4001\",\"survey\":\"999A\"}]}" +
            ",\"surveyBySurvey\":{\"periodicity\":\"Monthly\"},\"status\":\"Status\"}]}}}";
        qlQueryResponse response = new qlQueryResponse(jsonInput);
        assertEquals("X", response.getResponses()); 
    }

    @Test
    void testConcat() {
        String string1 = "[{\"reference\":\"12345678003\",\"period\":\"201801\",\"instance\":0,\"response\":\"\",\"questioncode\":\"1001\",\"survey\":\"999A\"}," + 
                          "{\"reference\":\"12345678003\",\"period\":\"201801\",\"instance\":0,\"response\":\"0\",\"questioncode\":\"4001\",\"survey\":\"999A\"}]";
        String string2 = "[{\"reference\":\"12345678003\",\"period\":\"201712\",\"instance\":0,\"response\":\"\",\"questioncode\":\"1001\",\"survey\":\"999A\"}," + 
                          "{\"reference\":\"12345678003\",\"period\":\"201712\",\"instance\":0,\"response\":\"0\",\"questioncode\":\"4001\",\"survey\":\"999A\"}]";
        JSONArray s1 = new JSONArray(string1);
        JSONArray s2 = new JSONArray(string2);
        JSONArray output = new JSONArray();

        for (int i = 0; i < s1.length(); i++) {
            output.put(s1.getJSONObject(i));
        }
        for (int i = 0; i < s2.length(); i++) {
            output.put(s2.getJSONObject(i));
        }        
        assertEquals("X", output.toString()); 
    }


    @Test
    void getContributor_validdata_works() {
        var inputJson = "{\"data\":{\"allContributors\":{\"nodes\":[{\"formid\":1,\"birthdate\":\"\",\"selectiontype\":\" \",\"createddate\":\"2019-10-04T09:17:36.061812+00:00\",\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[{\"reference\":\"12345678003\",\"period\":\"201801\",\"instance\":0,\"response\":\"\",\"questioncode\":\"1001\",\"survey\":\"999A\"},{\"reference\":\"12345678003\",\"period\":\"201801\",\"instance\":0,\"response\":\"0\",\"questioncode\":\"4001\",\"survey\":\"999A\"}]},\"surveyBySurvey\":{\"periodicity\":\"Monthly\"},\"checkletter\":\" \",\"rusicoutdated\":\"     \",\"tradingstyle\":\"\",\"frozenemployees\":\"0\",\"companyregistrationnumber\":\"\",\"reference\":\"12345678003\",\"reportingunitmarker\":\" \",\"inclusionexclusion\":\" \",\"legalstatus\":\" \",\"createdby\":\"fisdba\",\"lastupdateddate\":null,\"rusic\":\"     \",\"contact\":\"\",\"lastupdatedby\":null,\"formByFormid\":{\"survey\":\"999A\",\"formdefinitionsByFormid\":{\"nodes\":[{\"questioncode\":\"1000\",\"type\":\"NUMERIC\"},{\"questioncode\":\"1001\",\"type\":\"NUMERIC\"},{\"questioncode\":\"2000\",\"type\":\"TICKBOX-Yes\"},{\"questioncode\":\"3000\",\"type\":\"Text\"},{\"questioncode\":\"4000\",\"type\":\"NUMERIC\"},{\"questioncode\":\"4001\",\"type\":\"NUMERIC\"}]}},\"frozenturnover\":\"0\",\"currency\":\"S\",\"receiptdate\":\"2019-10-04T09:17:36.061812+00:00\",\"frozensicoutdated\":\"     \",\"fax\":\"\",\"frozenemployment\":\"0\",\"turnover\":\"0\",\"payereference\":\"\",\"period\":\"201801\",\"wowenterprisereference\":\"\",\"numberlivevat\":\"0\",\"telephone\":\"\",\"employment\":\"0\",\"numberlivepaye\":\"0\",\"vatreference\":\"\",\"lockedby\":null,\"frozenfteemployment\":\"0.000\",\"cellnumber\":0,\"fteemployment\":\"0.000\",\"lockeddate\":null,\"survey\":\"999A\",\"enterprisereference\":\"          \",\"numberlivelocalunits\":\"0\",\"employees\":\"0\",\"region\":\"  \",\"frozensic\":\"     \",\"status\":\"Status\"}]}}}";
        var formJson = new qlQueryResponse(inputJson).getForm("999A", "199912");       
        assertEquals("X", formJson);
    }

}
package uk.gov.ons.collection.test;

import uk.gov.ons.collection.entity.ViewFormResponse;
import uk.gov.ons.collection.exception.InvalidJsonException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import lombok.extern.log4j.Log4j2;

@Log4j2
class ViewFormTestResponse {
        String inputJSON = "{\"data\": {\"allContributors\": {\"nodes\": [{\"formByFormid\": {\"formdefinitionsByFormid\": "+
                "{\"nodes\": [" +
                    "{\"derivedformula\": \"\", \"questioncode\": \"1000\", \"type\": \"NUMERIC\"}," +
                    "{\"derivedformula\": \"\", \"questioncode\": \"1001\", \"type\": \"NUMERIC\"}," +
                    "{\"derivedformula\": \"\", \"questioncode\": \"2000\", \"type\": \"TICKBOX-Yes\"}," +
                    "{\"derivedformula\": \"\", \"questioncode\": \"3000\", \"type\": \"Text\"}," +
                    "{\"derivedformula\": \"1000 + 1001\", \"questioncode\": \"4000\", \"type\": \"NUMERIC\"}," +
                    "{\"derivedformula\": \"1000 - 1001\", \"questioncode\": \"4001\", \"type\": \"NUMERIC\"} " +
                "]}}," +
            "\"responsesByReferenceAndPeriodAndSurvey\": " +
                "{\"nodes\": [" +
                "{\"instance\": 0, \"response\": \"20\", \"questioncode\": \"1000\"}," +
                "{\"instance\": 0, \"response\": \"1\", \"questioncode\": \"1001\"}," +
                "{\"instance\": 0, \"response\": \"1\", \"questioncode\": \"2000\"}," +
                "{\"instance\": 0, \"response\": \"Rhubarb\", \"questioncode\": \"3000\"}," +
                "{\"instance\": 0, \"response\": \"21\", \"questioncode\": \"4000\"}," +
                "{\"instance\": 0, \"response\": \"19\", \"questioncode\": \"4001\"} " +
                "]}}]}}}";

        String expectedOutput = "{\"form_data\":[{\"derivedformula\":\"\",\"questioncode\":\"1000\",\"type\":\"NUMERIC\"},"+
                    "{\"derivedformula\":\"\",\"questioncode\":\"1001\",\"type\":\"NUMERIC\"},"+
                    "{\"derivedformula\":\"\",\"questioncode\":\"2000\",\"type\":\"TICKBOX-Yes\"},"+
                    "{\"derivedformula\":\"\",\"questioncode\":\"3000\",\"type\":\"Text\"},"+
                    "{\"derivedformula\":\"1000 + 1001\",\"questioncode\":\"4000\",\"type\":\"NUMERIC\"},"+
                    "{\"derivedformula\":\"1000 - 1001\",\"questioncode\":\"4001\",\"type\":\"NUMERIC\"}"+
                "],"+
            "\"responses\":["+
                "{\"instance\":0,\"response\":\"20\",\"questioncode\":\"1000\"},"+
                "{\"instance\":0,\"response\":\"1\",\"questioncode\":\"1001\"},"+
                "{\"instance\":0,\"response\":\"1\",\"questioncode\":\"2000\"},"+
                "{\"instance\":0,\"response\":\"Rhubarb\",\"questioncode\":\"3000\"},"+
                "{\"instance\":0,\"response\":\"21\",\"questioncode\":\"4000\"},"+ 
                "{\"instance\":0,\"response\":\"19\",\"questioncode\":\"4001\"}"+
                "]}";
        
    @Test
    void viewFormQuery_ExpectedJSONDataEqualsActualJSONData_ParsedData(){
        String response = new String();
        try {
            response = new ViewFormResponse(inputJSON).parseViewForm();
        } catch (InvalidJsonException e) {
            log.info("error in parseViewFormTest" + e.toString());
        }
        assertEquals(expectedOutput, response);
        log.info("output array.tostring" + expectedOutput.toString());
    }

    @Test
    void viewFormQuery_invalidJson_throwsException(){
        String wrongInput = "\"allContributors\": {\"nodes\": [{\"formByFormid\": {\"formdefinitionsByFormid\": "+
        "{\"nodes\": [" +
            "{\"derivedformula\": \"\", \"questioncode\": \"1000\", \"type\": \"NUMERIC\"}," +
            "{\"derivedformula\": \"\", \"questioncode\": \"1001\", \"type\": \"NUMERIC\"}," +
            "{\"derivedformula\": \"\", \"questioncode\": \"2000\", \"type\": \"TICKBOX-Yes\"}," +
            "{\"derivedformula\": \"\", \"questioncode\": \"3000\", \"type\": \"Text\"}," +
            "{\"derivedformula\": \"1000 + 1001\", \"questioncode\": \"4000\", \"type\": \"NUMERIC\"}," +
            "{\"derivedformula\": \"1000 - 1001\", \"questioncode\": \"4001\", \"type\": \"NUMERIC\"} " +
        "]}}," +
    "\"responsesByReferenceAndPeriodAndSurvey\": " +
        "{\"nodes\": [" +
        "{\"instance\": 0, \"response\": \"20\", \"questioncode\": \"1000\"}," +
        "{\"instance\": 0, \"response\": \"1\", \"questioncode\": \"1001\"}," +
        "{\"instance\": 0, \"response\": \"1\", \"questioncode\": \"2000\"}," +
        "{\"instance\": 0, \"response\": \"Rhubarb\", \"questioncode\": \"3000\"}," +
        "{\"instance\": 0, \"response\": \"21\", \"questioncode\": \"4000\"}," +
        "{\"instance\": 0, \"response\": \"19\", \"questioncode\": \"4001\"} " +
        "]}}]}}}";
        Assertions.assertThrows(InvalidJsonException.class, () -> {
            new ViewFormResponse(wrongInput);
    });
}
}

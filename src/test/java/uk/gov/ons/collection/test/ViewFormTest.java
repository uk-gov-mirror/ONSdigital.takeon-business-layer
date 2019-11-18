package uk.gov.ons.collection.test;

import uk.gov.ons.collection.entity.ViewFormResponse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class ViewFormTest{

    @Test
    void parseViewFormTest(){

    String inputJSON = "{\"data\": {\"allContributors\": {\"nodes\": [" +
        "{\"formByFormid\": {" +
                "{\"formdefinitionsByFormid\": {" +
                "\"nodes\": [" +
                    "{" +
                    "\"questioncode\": \"1000\"," +
                    "\"type\": \"NUMERIC\", " +
                    "\"derivedformula\": " +
                    "}," +
                    "{" +
                    "\"questioncode\": \"1001\"," +
                    "\"type\": \"NUMERIC\", " +
                    "\"derivedformula\": \"\" " +
                    "}," +
                    "{" +
                    "\"questioncode\": \"2000\", " +
                    "\"type\": \"TICKBOX-Yes\", " +
                    "\"derivedformula\": \"\" " +
                    "}," +
                    "{" +
                    "\"questioncode\": \"3000\", " +
                    "\"type\": \"Text\", " +
                    "\"derivedformula\": \"\" " +
                    "}," +
                    "{" +
                    "\"questioncode\": \"4000\", " +
                    "\"type\": \"NUMERIC\", " +
                    "\"derivedformula\": \"1000 + 1001\" " +
                    "}," +
                    "{" +
                    "\"questioncode\": \"4001\", " +
                    "\"type\": \"NUMERIC\", " +
                    "\"derivedformula\": \"1000 - 1001\" " +
                    "}" +
                "]" +
                "}" +
            "}," +
            "\"responsesByReferenceAndPeriodAndSurvey\": {" +
                "\"nodes\": [" +
                "{" +
                    "\"instance\": 0, " +
                    "\"questioncode\": \"1000\", " +
                    "\"response\": \"20\" " +
                "}," +
                "{" +
                    "\"instance\": 0, " +
                    "\"questioncode\": \"1001\", " +
                    "\"response\": \"1\" " +
                "}," +
                "{" +
                    "\"instance\": 0, " +
                    "\"questioncode\": \"2000\", " +
                    "\"response\": \"1\" " +
                "}," +
                "{" +
                    "\"instance\": 0, " +
                    "\"questioncode\": \"3000\", " +
                    "\"response\": \"Rhubarb\" " +
                "}, " +
                "{" +
                    "\"instance\": 0, " +
                    "\"questioncode\": \"4000\", " +
                    "\"response\": \"21\" " +
                "}," +
                "{" +
                    "\"instance\": 0, " +
                    "\"questioncode\": \"4001\", " +
                    "\"response\": \"19\" " +
                "}]}},";
    String expectedOutput = "{}";
    // String expectedOutput = "{\"'view_form_responses': [{'displaytext': 'This is a numeric question', 'instance': 0, 'response': '', 'questioncode': '1000', " +
    // "'displayquestionnumber': 'Q1', 'type': 'NUMERIC'}, {'displaytext': 'This is another numeric question', 'instance': 0, 'response': '', 'questioncode': '1001'," + 
    // "'displayquestionnumber': 'Q2', 'type': 'NUMERIC'}, {'displaytext': 'This is a checkbox question', 'instance': '', 'response': '', 'questioncode': '2000'," + 
    // "'displayquestionnumber': 'Q3', 'type': 'TICKBOX-Yes'}, {'displaytext': 'This is a text question', 'instance': '', 'response': '', 'questioncode': '3000'," + 
    // "'displayquestionnumber': 'Q4', 'type': 'Text'}, {'displaytext': 'This is a postive derived question', 'instance': '', 'response': '', 'questioncode': '4000', " + 
    // "'displayquestionnumber': 'Q5', 'type': 'NUMERIC'}, {'displaytext': 'This is a subtracted derived question', 'instance': '', 'response': '', 'questioncode': '4001', " +
    // "'displayquestionnumber': 'Q6', 'type': 'NUMERIC'}]}{\"";

    ViewFormResponse response = new ViewFormResponse(inputJSON);
    assertEquals(expectedOutput, response.parseViewForm());
    }
}
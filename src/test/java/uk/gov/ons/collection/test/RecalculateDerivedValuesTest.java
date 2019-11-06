package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;

import cucumber.api.java.Before;
import uk.gov.ons.collection.utilities.calculateDerivedValuesQuery;
import uk.gov.ons.collection.utilities.calculateDerviedValuesResponse;

import static org.junit.jupiter.api.Assertions.*;

class RecalculateDerivedValuesTest {

    //private String formInput;
    //private String responseInput;
    
    @Before
    public void setUp() throws Exception {

    }

    @Test
    void buildFormDefinitionQuery_validInput_queryBuiltSuccessfully() {
        var key = "{\"reference\":\"12345678001\",\"period\":\"201801\",\"survey\":\"999A\"}";
        var recalculateDerivedValues = new calculateDerivedValuesQuery(key);
        var query = recalculateDerivedValues.buildFormDefinitionQuery();
        var expectedQuery = "{\"query\":\"query formDefinitionByReference "+
                "{allContributors(condition: {reference: \"12345678001\",period: \"201801\",survey: \"999A\"})"+
                "{nodes {formByFormid {formdefinitionsByFormid {nodes {questioncode,derivedformula}}}}}}\"}";
        assertEquals(expectedQuery, query);
    }

    @Test
    void extractDerivedFormulae_validInput_derivedFormulaeFound() {
        var key = "{\"reference\":\"12345678001\",\"period\":\"201801\",\"survey\":\"999A\"}";
        var inputJson = "{"+
                "\"data\": {"+
                "\"allContributors\": {"+
                "\"nodes\": ["+
                "{"+
                "\"formByFormid\": {"+
                "\"formdefinitionsByFormid\": {"+
                "\"nodes\": ["+
                "{"+
                "\"questioncode\": \"1000\","+
                "\"derivedformula\": \"\""+
                "},"+
                "{"+
                "\"questioncode\": \"1001\","+
                "\"derivedformula\": \"\""+
                "},"+
                "{"+
                "\"questioncode\": \"2000\","+
                "\"derivedformula\": \"\""+
                "},"+
                "{"+
                "\"questioncode\": \"3000\","+
                "\"derivedformula\": \"\""+
                "},"+
                "{"+
                "\"questioncode\": \"4000\","+
                "\"derivedformula\": \"1000 + 1001\""+
                "},"+
                "{"+
                "\"questioncode\": \"4001\","+
                "\"derivedformula\": \"1000 - 1001\""+
                "}"+
                "]"+
                "}"+
                "}"+
                "}"+
                "]"+
                "}"+
                "}"+
                "}";
        var expectedOutputString = "{questioncode: \"4000\",derivedformula: \"1000 + 1001\",questioncode: \"4001\",derivedformula: \"1000 - 1001\"}";

        var recalculateDerivedValues = new calculateDerivedValuesQuery(inputJson, key);
        var outputString = recalculateDerivedValues.extractDerivedFormulae();
        assertEquals(expectedOutputString,outputString);
    }

    // @Test
    // void extractQuestionCodes_validInput_codesExtracted() {
    //     var key = "{\"reference\":\"12345678001\",\"period\":\"201801\",\"survey\":\"999A\"}";
    //     var inputJson = "{questioncode: \"4000\",derivedformula: \"1000 + 1001\"}";
    //     var expectedList = "[1000, +, 1001]";
    //     var recalculateDerivedValues = new calculateDerivedValuesResponse(inputJson);
    //     var extractResponseQuery = recalculateDerivedValues.getQuestionCodes();
    //     assertEquals(expectedList,extractResponseQuery);
    // }

    @Test
    void buildResponseQuery_validInput_querySuccessful() {
        var key = "{\"reference\":\"12345678001\",\"period\":\"201801\",\"survey\":\"999A\"}";
        var recalculateDerivedValues = new calculateDerivedValuesQuery(key);
        var query = recalculateDerivedValues.buildGetResponsesQuery();
        var expectedQuery = "{\"query\":\"query getResponses($reference: String, $period: String, $survey: String){" +
            "allResponses(condition: {reference: \"12345678001\",period: \"201801\",survey: \"999A\"}){" +
            "nodes {response questioncode}}}\"}";
        assertEquals(expectedQuery, query);
    }

    @Test
    void parseFormData_returnsExpectedOutput_validInput(){
        var formInput = "{"+
        "\"data\": {"+
        "\"allContributors\": {"+
        "\"nodes\": ["+
        "{"+
        "\"formByFormid\": {"+
        "\"formdefinitionsByFormid\": {"+
        "\"nodes\": ["+
        "{"+
        "\"questioncode\": \"1000\","+
        "\"derivedformula\": \"\""+
        "},"+
        "{"+
        "\"questioncode\": \"1001\","+
        "\"derivedformula\": \"\""+
        "},"+
        "{"+
        "\"questioncode\": \"2000\","+
        "\"derivedformula\": \"\""+
        "},"+
        "{"+
        "\"questioncode\": \"3000\","+
        "\"derivedformula\": \"\""+
        "},"+
        "{"+
        "\"questioncode\": \"4000\","+
        "\"derivedformula\": \"1000 + 1001\""+
        "},"+
        "{"+
        "\"questioncode\": \"4001\","+
        "\"derivedformula\": \"1000 - 1001\""+
        "}"+
        "]"+
        "}"+
        "}"+
        "}"+
        "]"+
        "}"+
        "}"+
        "}";

        var responseInput = "{"
            + "\"data\": {"
            + "\"allResponses\": {"
            +  "\"nodes\": ["
            +  "{"
            +  "\"response\": \"20\","
            + "\"questioncode\": \"1000\""
            +  "},"
            +  "{"
            +  "\"response\": \"1\","
            +        "\"questioncode\": \"1001\""
            +     "},"
            +      "{"
            +        "\"response\": \"1\","
            +        "\"questioncode\": \"2000\""
            +      "},"
            +      "{"
            +        "\"response\": \"Rhubarb\","
            +        "\"questioncode\": \"3000\""
            +      "},"
            +      "{"
            +        "\"response\": \"21\","
            +        "\"questioncode\": \"4000\""
            +      "},"
            +     "{"
            +        "\"response\": \"19\","
            +        "\"questioncode\": \"4001\""
            +     "}"
            +    "]"
            +  "}"
            + "}"
            + "}";
        // var expectedOutput = "{"
        // +  "\"response_data\": ["
        // +  "{"
        // +  "\"response\": \"20\","
        // + "\"questioncode\": \"1000\""
        // +  "},"
        // +  "{"
        // +  "\"response\": \"1\","
        // +        "\"questioncode\": \"1001\""
        // +     "},"
        // +      "{"
        // +        "\"response\": \"1\","
        // +        "\"questioncode\": \"2000\""
        // +      "},"
        // +      "{"
        // +        "\"response\": \"Rhubarb\","
        // +        "\"questioncode\": \"3000\""
        // +      "},"
        // +      "{"
        // +        "\"response\": \"21\","
        // +        "\"questioncode\": \"4000\""
        // +      "},"
        // +     "{"
        // +        "\"response\": \"19\","
        // +        "\"questioncode\": \"4001\""
        // +     "}"
        // +    "]"
        // +  "}";

        var expectedOutput = "{"
            + "data": {
            +  "allContributors": {
            +    "nodes": [
            +     {
            +       "formByFormid": {
            +         "formdefinitionsByFormid": {
            +           "nodes": [
            +             {
            +               "questioncode": "1000",
            +               "derivedformula": ""
            +             },
            +             {
            +               "questioncode": "1001",
            +               "derivedformula": ""
            +             },
            +             {
            +               "questioncode": "2000",
            +               "derivedformula": ""
            +             },
            +             {
            +               "questioncode": "3000",
            +               "derivedformula": ""
            +             },
            +             {
            +               "questioncode": "4000",
            +               "derivedformula": "1000 + 1001"
            +             },
            +             {
            +               "questioncode": "4001",
            +               "derivedformula": "1000 - 1001"
            +             }
            +           ]
            +         }
            +       }
            +     }
            +   ]
            + }
            + }
          + }
        var response = new calculateDerviedValuesResponse(formInput, responseInput).parseFormData();
        assertEquals(expectedOutput, response);
        
    }

}
package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.utilities.RecalculateDerivedValues;

import static org.junit.jupiter.api.Assertions.*;

class RecalculateDerivedValuesTest {

    @Test
    void buildFormDefinitionQuery_validInput_queryBuiltSuccessfully() {
        var key = "{\"reference\":\"12345678001\",\"period\":\"201801\",\"survey\":\"999A\"}";
        var inputJson = "{}";
        var recalculateDerivedValues = new RecalculateDerivedValues(inputJson,key);
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


        var recalculateDerivedValues = new RecalculateDerivedValues(inputJson, key);
        var outputString = recalculateDerivedValues.extractDerivedFormulae();
        assertEquals(expectedOutputString,outputString);
    }

    @Test
    // void extractResponseQuery_validInput_querySuccessful() {
    //     var key = "{\"reference\":\"12345678001\",\"period\":\"201801\",\"survey\":\"999A\"}";
    //     var inputJson = "{questioncode: \"4000\",derivedformula: \"1000 + 1001\"}";
    //     var expectedQuery = "{\"query\": \"query responseByQuestionCode {alias1: allResponses(condition: " +
    //             "{questioncode: \\\"1000\\\", reference: \\\"12345678001\\\"}) {nodes {questioncode,response}}" +
    //             "alias2: allResponses(condition: {questioncode: \\\"1001\\\", reference: \\\"12345678001\\\"}) " +
    //             "{nodes {questioncode,response}}alias3: allResponses(condition: " +
    //             "{questioncode: \\\"4000\\\", reference: \\\"12345678001\\\"}) {nodes {questioncode,response}}}\"}";
    //     var recalculateDerivedValues = new RecalculateDerivedValues(inputJson, key);
    //     var extractResponseQuery = recalculateDerivedValues.buildExtractResponseQuery();
    //     assertEquals(expectedQuery,extractResponseQuery);
    // }

    @Test
    void extractQuestionCodes_validInput_codesExtracted() {
        var key = "{\"reference\":\"12345678001\",\"period\":\"201801\",\"survey\":\"999A\"}";
        var inputJson = "{questioncode: \"4000\",derivedformula: \"1000 + 1001\"}";
        var expectedList = "[1000, +, 1001]";
        var recalculateDerivedValues = new RecalculateDerivedValues(inputJson, key);
        var extractResponseQuery = recalculateDerivedValues.getQuestionCodes();
        assertEquals(expectedList,extractResponseQuery);
    }

    @Test
    void buildResponseQuery_validInput_querySuccessful() {
        var key = "{\"reference\":\"12345678001\",\"period\":\"201801\",\"survey\":\"999A\"}";
        var inputJson = "{}";
        var recalculateDerivedValues = new RecalculateDerivedValues(inputJson,key);
        var query = recalculateDerivedValues.buildGetResponsesQuery();
        var expectedQuery = "{\"query\":\"query getResponses($reference: String, $period: String, $survey: String){" +
            "allResponses(condition: {reference: \"12345678001\",period: \"201801\",survey: \"999A\"}){" +
            "nodes {response questioncode}}}\"}";
        assertEquals(expectedQuery, query);
    }

}
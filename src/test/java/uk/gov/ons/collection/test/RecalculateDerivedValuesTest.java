package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.utilities.RecalculateDerivedValues;

import static org.junit.jupiter.api.Assertions.*;

class RecalculateDerivedValuesTest {

    @Test
    void buildFormDefinitionQuery_validInput_queryBuiltSuccessfully() {
        var inputJson = "{\"reference\":\"12345678001\",\"period\":\"201801\",\"survey\":\"999A\"}";
        var recalculateDerivedValues = new RecalculateDerivedValues(inputJson);
        var query = recalculateDerivedValues.buildFormDefinitionQuery();
        var expectedQuery = "{\"query\":\"query formDefinitionByReference "+
                "{allContributors(condition: {reference: \"12345678001\",period: \"201801\",survey: \"999A\"})"+
                "{nodes {formByFormid {formdefinitionsByFormid {nodes {questioncode,derivedformula}}}}}}\"}";
        assertEquals(expectedQuery, query);
    }

    @Test
    void extractDerivedFormula_validInput_derivedFormulaFound() {
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
        var expectedOutputString = "questioncode: \"4000\",derivedformula: \"1000 + 1001\",questioncode: \"4001\",derivedformula: \"1000 - 1001\"";


        var recalculateDerivedValues = new RecalculateDerivedValues(inputJson);
        var outputString = recalculateDerivedValues.extractDerivedValues();
        assertEquals(expectedOutputString,outputString);
    }

}
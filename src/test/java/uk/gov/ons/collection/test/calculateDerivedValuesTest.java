package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;

import uk.gov.ons.collection.exception.FormulaCalculationException;
import uk.gov.ons.collection.exception.InvalidDerivedResponseException;
import uk.gov.ons.collection.exception.InvalidJsonException;
import uk.gov.ons.collection.utilities.calculateDerivedValuesQuery;
import uk.gov.ons.collection.utilities.calculateDerviedValuesResponse;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;

class calculateDerivedValuesTest {

    String formInput = "{" + "\"data\": {" + "\"allContributors\": {" + "\"nodes\": [" + "{" + "\"formByFormid\": {"
            + "\"formdefinitionsByFormid\": {" + "\"nodes\": [" + "{" + "\"questioncode\": \"1000\","
            + "\"derivedformula\": \"\"" + "}," + "{" + "\"questioncode\": \"1001\"," + "\"derivedformula\": \"\""
            + "}," + "{" + "\"questioncode\": \"2000\"," + "\"derivedformula\": \"\"" + "}," + "{"
            + "\"questioncode\": \"3000\"," + "\"derivedformula\": \"\"" + "}," + "{" + "\"questioncode\": \"4000\","
            + "\"derivedformula\": \"1000 + 1001\"" + "}," + "{" + "\"questioncode\": \"4001\","
            + "\"derivedformula\": \"1000 - 1001\"" + "}" + "]" + "}" + "}" + "}" + "]" + "}" + "}" + "}";

    String responseInput = "{" + "\"data\": {" + "\"allResponses\": {" + "\"nodes\": [" + "{" + "\"response\": \"20\","
            + "\"questioncode\": \"1000\"," + "\"instance\": 0" + "}," + "{" + "\"response\": \"1\"," + "\"questioncode\": \"1001\"," + "\"instance\": 0},"
            + "{" + "\"response\": \"1\"," + "\"questioncode\": \"2000\"," + "\"instance\": 0}," + "{" + "\"response\": \"Rhubarb\","
            + "\"questioncode\": \"3000\"," + "\"instance\": 0}," + "{" + "\"response\": \"21\"," + "\"questioncode\": \"4000\"," + "\"instance\": 0},"
            + "{" + "\"response\": \"19\"," + "\"questioncode\": \"4001\"," + "\"instance\": 0}" + "]" + "}" + "}" + "}";

    @Test
    void buildFormDefinitionQuery_validInput_queryBuiltSuccessfully() {
        Map<String, String> variables = new HashMap<>();
        variables.put("reference", "12345678001");
        variables.put("period", "201801");
        variables.put("survey", "999A");
        String formQuery = new String();
        try {
            formQuery = new calculateDerivedValuesQuery(variables).buildFormDefinitionQuery();
        } catch (InvalidJsonException e) {
            System.out.println("Can't build form query for calculating derived values" + e);
        }
        var expectedQuery = "{\"query\":\"query formDefinitionByReference "+
                "{allContributors(condition: {reference: \\\"12345678001\\\",period: \\\"201801\\\",survey: \\\"999A\\\"})"+
                "{nodes {formByFormid {formdefinitionsByFormid {nodes {questioncode,derivedformula}}}}}}\"}";
        assertEquals(expectedQuery, formQuery);
    }

    @Test
    void buildResponseQuery_validInput_querySuccessful() {
        Map<String,String> variables = new HashMap<>();
        variables.put("reference", "12345678001");
        variables.put("period", "201801");
        variables.put("survey", "999A");
        String responseQuery = new String();
        try {
            responseQuery = new calculateDerivedValuesQuery(variables).buildGetResponsesQuery();
        } catch (InvalidJsonException e) {
            System.out.println("Can't build response query for calculating derived values" + e);
        }
        var expectedQuery = "{\"query\":\"query getResponses {" +
            "allResponses(condition: {reference: \\\"12345678001\\\",period: \\\"201801\\\",survey: \\\"999A\\\"}){" +
            "nodes {response questioncode instance}}}\"}";
        assertEquals(expectedQuery, responseQuery);
    }

    @Test
    void incorrectJSON_returnsInvalidJsonException() {
        String incorrectFormInput = "\"allContributors\": {" + "\"nodes\": [" + "{" + "\"formByFormid\": {"
        + "\"formdefinitionsByFormid\": {" + "\"nodes\": [" + "{" + "\"questioncode\": \"1000\","
        + "\"derivedformula\": \"\"" + "}," + "{" + "\"questioncode\": \"1001\"," + "\"derivedformula\": \"\""
        + "}," + "{" + "\"questioncode\": \"2000\"," + "\"derivedformula\": \"\"" + "}," + "{"
        + "\"questioncode\": \"3000\"," + "\"derivedformula\": \"\"" + "}," + "{" + "\"questioncode\": \"4000\","
        + "\"derivedformula\": \"1000 + 1001\"" + "}," + "{" + "\"questioncode\": \"4001\","
        + "\"derivedformula\": \"1000 - 1001\"" + "}" + "]" + "}" + "}" + "}" + "]" + "}" + "}" + "}";

        Assertions.assertThrows(InvalidJsonException.class, () -> {
            new calculateDerviedValuesResponse(incorrectFormInput, responseInput);
        });
    }

    @Test
    void parseFormData_returnsExpectedOutput_validInput(){
        var expectedOutput =  "{"
            +           "\"form_data\":["
            +             "{"
            +               "\"derivedformula\":\"\","
            +               "\"questioncode\":\"1000\""
            +             "},"
            +             "{"
            +               "\"derivedformula\":\"\","
            +               "\"questioncode\":\"1001\""
            +             "},"
            +             "{"
            +               "\"derivedformula\":\"\","
            +               "\"questioncode\":\"2000\""
            +             "},"
            +             "{"
            +               "\"derivedformula\":\"\","
            +               "\"questioncode\":\"3000\""
            +             "},"
            +             "{"
            +               "\"derivedformula\":\"1000 + 1001\","
            +               "\"questioncode\":\"4000\""
            +             "},"
            +             "{"
            +               "\"derivedformula\":\"1000 - 1001\","
            +               "\"questioncode\":\"4001\""
            +             "}"
            +           "]"
            +         "}";
        JSONObject response = new JSONObject();
        try {
            response = new calculateDerviedValuesResponse(formInput, responseInput).parseFormData();
        } catch (Exception e) {
            System.out.println("Can't parse form data calculating derived values" + e);
        }
        assertEquals(expectedOutput, response.toString());
        
    }

    @Test
    void parseResponseData_returnsExpectedOutput_validInput() {

        var expectedOutput = "{"
        +  "\"response_data\":["
        +  "{"
        +  "\"instance\":0,"
        +  "\"response\":\"20\","
        + "\"questioncode\":\"1000\""
        +  "},"
        +  "{"
        +  "\"instance\":0,"
        +  "\"response\":\"1\","
        +       "\"questioncode\":\"1001\""
        +     "},"
        +      "{"
        +        "\"instance\":0,"
        +        "\"response\":\"1\","
        +        "\"questioncode\":\"2000\""
        +      "},"
        +      "{"
        +        "\"instance\":0,"
        +        "\"response\":\"Rhubarb\","
        +        "\"questioncode\":\"3000\""
        +      "},"
        +      "{"
        +        "\"instance\":0,"
        +        "\"response\":\"21\","
        +        "\"questioncode\":\"4000\""
        +      "},"
        +     "{"
        +        "\"instance\":0,"
        +        "\"response\":\"19\","
        +        "\"questioncode\":\"4001\""
        +     "}"
        +    "]"
        +  "}";

        JSONObject response = new JSONObject();
        try {
            response = new calculateDerviedValuesResponse(formInput, responseInput).parseResponseData();
        } catch (Exception e) {
            System.out.println("Can't parse response data calculating derived values" + e);
        }
        assertEquals(expectedOutput, response.toString());

    }

    @Test
    void calculateDerviedValues_returnsExpectedValues(){

        var expectedOutput = "[{result=21, instance=0, updatedformula=[20, +, 1], questioncode=4000, formulatorun=[20, +, 1]}," +
                                " {result=19, instance=0, updatedformula=[20, -, 1], questioncode=4001, formulatorun=[20, -, 1]}]";
        ArrayList<HashMap<String, Object>> response = new ArrayList<>();
        try {
            response = new calculateDerviedValuesResponse(formInput, responseInput).calculateDerviedValues();
        } catch (Exception e) {
            System.out.println("Can't calculate derived values" + e);
        }
        assertEquals(expectedOutput, response.toString());
        
    }

    @Test
    void calculateDerviedValues_decimalInput_returnsDecimalValues() {
        var responseDecimalInput = "{"
        + "\"data\": {"
        + "\"allResponses\": {"
        +  "\"nodes\": ["
        +  "{"
        +  "\"response\": \"20.345\","
        + "\"questioncode\": \"1000\","
        +  "\"instance\":0"
        +  "},"
        +  "{"
        +  "\"response\": \"1.343\","
        +        "\"questioncode\": \"1001\","
        +        "\"instance\":0"
        +     "},"
        +      "{"
        +        "\"response\": \"1\","
        +        "\"questioncode\": \"2000\","
        +        "\"instance\":0"
        +      "},"
        +      "{"
        +        "\"response\": \"Rhubarb\","
        +        "\"questioncode\": \"3000\","
        +        "\"instance\":0"
        +      "},"
        +      "{"
        +        "\"response\": \"21\","
        +        "\"questioncode\": \"4000\","
        +        "\"instance\":0"
        +      "},"
        +     "{"
        +        "\"response\": \"19\","
        +        "\"questioncode\": \"4001\","
        +        "\"instance\":0"
        +     "}"
        +    "]"
        +  "}"
        + "}"
        + "}";

        var expectedOutput = "[{result=21.688, instance=0, updatedformula=[20.345, +, 1.343], questioncode=4000, formulatorun=[20.345, +, 1.343]}," + 
                            " {result=19.002, instance=0, updatedformula=[20.345, -, 1.343], questioncode=4001, formulatorun=[20.345, -, 1.343]}]";
        ArrayList<HashMap<String, Object>> response = new ArrayList<>();
        try {
            response = new calculateDerviedValuesResponse(formInput, responseDecimalInput).calculateDerviedValues();
        } catch (Exception e) {
            System.out.println("Can't calculate derived values" + e);
        }
        assertEquals(expectedOutput, response.toString());

    }

    @Test
    void calculateDerviedValues_stringResponseInput_returnsError() {
        var responseStringInput = "{"
        + "\"data\": {"
        + "\"allResponses\": {"
        +  "\"nodes\": ["
        +  "{"
        +  "\"response\": \"TEST\","
        + "\"questioncode\": \"1000\","
        +  "\"instance\":0"
        +  "},"
        +  "{"
        +  "\"response\": \"1.00000000\","
        +        "\"questioncode\": \"1001\","
        +        "\"instance\":0"
        +     "},"
        +      "{"
        +        "\"response\": \"1\","
        +        "\"questioncode\": \"2000\","
        +        "\"instance\":0"
        +      "},"
        +      "{"
        +        "\"response\": \"Rhubarb\","
        +        "\"questioncode\": \"3000\","
        +        "\"instance\":0"
        +      "},"
        +      "{"
        +        "\"response\": \"21\","
        +        "\"questioncode\": \"4000\","
        +        "\"instance\":0"
        +      "},"
        +     "{"
        +        "\"response\": \"19\","
        +        "\"questioncode\": \"4001\","
        +        "\"instance\":0"
        +     "}"
        +    "]"
        +  "}"
        + "}"
        + "}";

        Assertions.assertThrows(InvalidDerivedResponseException.class, () -> {
            new calculateDerviedValuesResponse(formInput, responseStringInput).calculateDerviedValues();
        });
    }

    @Test
    void updateDerivedResponses_validInput_returnsExpectedData() {
        var expectedOutput = "{\"responses\":[{\"instance\":0,\"response\":\"21\",\"questioncode\":\"4000\"}," +
        "{\"instance\":0,\"response\":\"19\",\"questioncode\":\"4001\"}]}";
        JSONObject response = new JSONObject();
        try {
            response = new calculateDerviedValuesResponse(formInput, responseInput)
                    .updateDerivedQuestionResponses();
        } catch (Exception e) {
            System.out.println("Can't update derived values" + e);
        }
        assertEquals(expectedOutput, response.toString());
    }

    @Test
    void calculateDerviedValues_blankResponseInput_convertsToZero() {
        var responseStringInput = "{"
        + "\"data\": {"
        + "\"allResponses\": {"
        +  "\"nodes\": ["
        +  "{"
        +  "\"response\": \"\","
        + "\"questioncode\": \"1000\","
        +  "\"instance\":0"
        +  "},"
        +  "{"
        +  "\"response\": \"1.00000000\","
        +        "\"questioncode\": \"1001\","
        +        "\"instance\":0"
        +     "},"
        +      "{"
        +        "\"response\": \"1\","
        +        "\"questioncode\": \"2000\","
        +        "\"instance\":0"
        +      "},"
        +      "{"
        +        "\"response\": \"Rhubarb\","
        +        "\"questioncode\": \"3000\","
        +        "\"instance\":0"
        +      "},"
        +      "{"
        +        "\"response\": \"21\","
        +        "\"questioncode\": \"4000\","
        +        "\"instance\":0"
        +      "},"
        +     "{"
        +        "\"response\": \"19\","
        +        "\"questioncode\": \"4001\","
        +        "\"instance\":0"
        +     "}"
        +    "]"
        +  "}"
        + "}"
        + "}";

        var expectedOutput  ="[{result=1.00000000, instance=0, updatedformula=[0, +, 1.00000000], questioncode=4000, formulatorun=[, +, 1.00000000]}," +
                            " {result=-1.00000000, instance=0, updatedformula=[0, -, 1.00000000], questioncode=4001, formulatorun=[, -, 1.00000000]}]";
        ArrayList<HashMap<String, Object>> response = new ArrayList<>();
        try {
            response = new calculateDerviedValuesResponse(formInput, responseStringInput).calculateDerviedValues();
        } catch (Exception e) {
            System.out.println("Can't calculate derived values" + e);
        }
        assertEquals(expectedOutput, response.toString());

    }

    @Test
    void incorrectFormula_returnsDerivedResponseException() {
        String incorrectFormInput = "{" + "\"data\": {" + "\"allContributors\": {" + "\"nodes\": [" + "{" + "\"formByFormid\": {"
        + "\"formdefinitionsByFormid\": {" + "\"nodes\": [" + "{" + "\"questioncode\": \"1000\","
        + "\"derivedformula\": \"\"" + "}," + "{" + "\"questioncode\": \"1001\"," + "\"derivedformula\": \"\""
        + "}," + "{" + "\"questioncode\": \"2000\"," + "\"derivedformula\": \"\"" + "}," + "{"
        + "\"questioncode\": \"3000\"," + "\"derivedformula\": \"\"" + "}," + "{" + "\"questioncode\": \"4000\","
        + "\"derivedformula\": \"1000 + + + 1001\"" + "}," + "{" + "\"questioncode\": \"4001\","
        + "\"derivedformula\": \"1000 - 1001\"" + "}" + "]" + "}" + "}" + "}" + "]" + "}" + "}" + "}";

        Assertions.assertThrows(FormulaCalculationException.class, () -> {
            new calculateDerviedValuesResponse(incorrectFormInput, responseInput).calculateDerviedValues();
        });
    }

    @Test
    void incorrectFormulaFormat_returnsResultEqualsZero() {
        String incorrectFormInput = "{" + "\"data\": {" + "\"allContributors\": {" + "\"nodes\": [" + "{" + "\"formByFormid\": {"
        + "\"formdefinitionsByFormid\": {" + "\"nodes\": [" + "{" + "\"questioncode\": \"1000\","
        + "\"derivedformula\": \"\"" + "}," + "{" + "\"questioncode\": \"1001\"," + "\"derivedformula\": \"\""
        + "}," + "{" + "\"questioncode\": \"2000\"," + "\"derivedformula\": \"\"" + "}," + "{"
        + "\"questioncode\": \"3000\"," + "\"derivedformula\": \"\"" + "}," + "{" + "\"questioncode\": \"4000\","
        + "\"derivedformula\": \"1000+1001\"" + "}," + "{" + "\"questioncode\": \"4001\","
        + "\"derivedformula\": \"1000 - 1001\"" + "}" + "]" + "}" + "}" + "}" + "]" + "}" + "}" + "}";

        var expectedOutput = "[{result=0, instance=0, updatedformula=[], questioncode=4000, formulatorun=[]}," +
                        " {result=19, instance=0, updatedformula=[20, -, 1], questioncode=4001, formulatorun=[20, -, 1]}]";
        ArrayList<HashMap<String, Object>> response = new ArrayList<>();
        try {
            response = new calculateDerviedValuesResponse(incorrectFormInput, responseInput).calculateDerviedValues();
        } catch (Exception e) {
            System.out.println("Can't calculate derived values" + e);
        }
        assertEquals(expectedOutput, response.toString());
    }

    // If all derivedformulas = blank
    @Test
    void noDerivedFormulas_returnsEmptyArray() {
        String incorrectFormInput = "{" + "\"data\": {" + "\"allContributors\": {" + "\"nodes\": [" + "{" + "\"formByFormid\": {"
        + "\"formdefinitionsByFormid\": {" + "\"nodes\": [" + "{" + "\"questioncode\": \"1000\","
        + "\"derivedformula\": \"\"" + "}," + "{" + "\"questioncode\": \"1001\"," + "\"derivedformula\": \"\""
        + "}," + "{" + "\"questioncode\": \"2000\"," + "\"derivedformula\": \"\"" + "}," + "{"
        + "\"questioncode\": \"3000\"," + "\"derivedformula\": \"\"" + "}," + "{" + "\"questioncode\": \"4000\","
        + "\"derivedformula\": \"\"" + "}," + "{" + "\"questioncode\": \"4001\","
        + "\"derivedformula\": \"\"" + "}" + "]" + "}" + "}" + "}" + "]" + "}" + "}" + "}";

        var expectedOutput = "{\"responses\":[]}";
        JSONObject response = new JSONObject();
        try {
            response = new calculateDerviedValuesResponse(incorrectFormInput, responseInput).updateDerivedQuestionResponses();
        } catch (Exception e) {
            System.out.println("Can't calculate derived values" + e);
        }
        assertEquals(expectedOutput, response.toString());
    }
}
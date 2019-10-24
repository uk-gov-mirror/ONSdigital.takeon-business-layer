package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.ValidationOutputs;
import uk.gov.ons.collection.exception.InvalidJsonException;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationOutputsTest {

    @Test
    void class_invalidJson_throwsExeption() {
        assertThrows(InvalidJsonException.class, () -> new ValidationOutputs("Dummy"));
    }

    @Test
    void buildDeleteOutputQuery_3validAttributes_validDeleteQueryGenerated() {
        var inputJson =  "{\"validation_outputs\": [{\"reference\": \"ABC\", \"period\": \"DEF\", \"survey\": \"HIJ\"}]}";
        var expectedQuery = "{\"query\": \"mutation deleteOutput($period: String!, $reference: String!, $survey: String!)" +
            "{deleteoutput(input: {reference: $reference, period: $period, survey: $survey}){clientMutationId}}\"," +
            "\"variables\":{\"reference\": \"ABC\",\"period\": \"DEF\",\"survey\": \"HIJ\"}}";
        try {
            var query = new ValidationOutputs(inputJson).buildDeleteOutputQuery();
            assertEquals(expectedQuery, query);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void buildInsertByArrayQuery_validJson_validInsertQueryGenerated() {
        var inputJson = "{\"validation_outputs\":[{\"validationid\":10,\"reference\":\"12345678000\",\"period\": \"201801\"," +
            "\"survey\": \"999A\",\"triggered\": false,\"formula\": \"47\\\"X\\\"45634 > 5\",\"instance\": 0}," +
            "{\"validationid\": 11,\"reference\": \"12345678000\",\"period\": \"201801\",\"survey\": \"999A\"," +
            "\"triggered\": false,\"formula\": \"1233.52 > 5\",\"instance\": 0}]}";
        try {
            var validationOutput = new ValidationOutputs(inputJson);
            var query = validationOutput.buildInsertByArrayQuery();
            var expectedQuery = "{\"query\": \"mutation insertOutputArray{insertvalidationoutputbyarray(input:" +
                " {arg0:[{reference: \\\"12345678000\\\",period: \\\"201801\\\",survey: \\\"999A\\\",formula: \\\"47'X'45634 > 5\\\"" +
                ",validationid: \\\"10\\\",instance: \\\"0\\\",triggered: false,createdby: \\\"fisdba\\\",createddate: \\\"" + validationOutput.getTime() + 
                "\\\"},{reference: \\\"12345678000\\\",period: \\\"201801\\\",survey: \\\"999A\\\",formula: \\\"1233.52 > 5\\\",validationid: \\\"11\\\"," +
                "instance: \\\"0\\\",triggered: false,createdby: \\\"fisdba\\\",createddate: \\\"" +
                validationOutput.getTime() + "\\\"}]}){clientMutationId}}\"}";
            assertEquals(expectedQuery, query);
        } catch (Exception e) {
            assertEquals("", e);
        }
    }

    @Test
    void buildInsertByArrayQuery_validJsonMissingAttribute_throwsException() {
        var inputJson = "{\"validation_outputs\":[{\"validationid\":10,\"reference\":\"12345678000\",\"period\": \"201801\"," +
            "\"survey\": \"999A\",\"formula\": \"4745634 > 5\",\"instance\": 0}," +
            "{\"validationid\": 11,\"reference\": \"12345678000\",\"period\": \"201801\",\"survey\": \"999A\"," +
            "\"triggered\": false,\"formula\": \"1233.52 > 5\",\"instance\": 0}]}";
        assertThrows(InvalidJsonException.class, () -> new ValidationOutputs(inputJson).buildInsertByArrayQuery());
    }

    @Test
    void getReference_validJson_returnsGivenReference() {
        var inputJson =  "{\"validation_outputs\": [{\"reference\": \"ABC\"}]}";
        var expectedReference = "ABC";
        try {
            var reference = new ValidationOutputs(inputJson).getReference();
            assertEquals(expectedReference, reference);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void getReference_missingReference_throwsException() {
        assertThrows(InvalidJsonException.class, () -> new ValidationOutputs("{\"validation_outputs\": []}").getReference());
    }

    @Test
    void getPeriod_missingAttribute_throwsException() {
        assertThrows(InvalidJsonException.class, () -> new ValidationOutputs("{\"validation_outputs\": []}").getPeriod());
    }

    @Test
    void getSurvey_missingAttribute_throwsException() {
        assertThrows(InvalidJsonException.class, () -> new ValidationOutputs("{\"validation_outputs\": []}").getSurvey());
    }
    
    @Test
    void getPeriod_periodInJson_returnsGivenPeriod() {
        var inputJson =  "{\"validation_outputs\": [{\"period\": \"ABC\"}]}";
        var expectedPeriod = "ABC";
        try {
            var period = new ValidationOutputs(inputJson).getPeriod();
            assertEquals(expectedPeriod, period);
        } catch (Exception e) {
            assertTrue(false);
        }
    }  
    
    @Test
    void getSurvey_surveyInJson_returnsGivenSurvey() {
        var inputJson =  "{\"validation_outputs\": [{\"survey\": \"ABC\"}]}";
        var expectedPeriod = "ABC";
        try {
            var survey = new ValidationOutputs(inputJson).getSurvey();
            assertEquals(expectedPeriod, survey);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void getStatusText_missingTriggeredAttribute_throwsException() {
        assertThrows(InvalidJsonException.class, () -> new ValidationOutputs("{\"validation_outputs\": [{\"a\":\"b\"}]}").getStatusText());
    }

    @Test
    void getStatusText_1triggered_Triggered() {
        var inputJson =  "{\"validation_outputs\": [{\"triggered\": true}]}";
        var expectedStatus = "Validations Triggered";
        try {
            var status = new ValidationOutputs(inputJson).getStatusText();
            assertEquals(expectedStatus, status);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void getStatusText_1of3triggeredInJson_returnsTriggered() {
        var inputJson =  "{\"validation_outputs\": [{\"triggered\": false},{\"triggered\": true},{\"triggered\": false}]}";
        var expectedStatus = "Validations Triggered";
        try {
            var status = new ValidationOutputs(inputJson).getStatusText();
            assertEquals(expectedStatus, status);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void getStatusText_0triggeredInJson_returnsClear() {
        var inputJson =  "{\"validation_outputs\": [{\"triggered\": false}]}";
        var expectedStatus = "Clear";
        try {
            var status = new ValidationOutputs(inputJson).getStatusText();
            assertEquals(expectedStatus, status);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void getStatusText_0of5triggeredInJson_returnsClear() {
        var inputJson = "{\"validation_outputs\": [{\"triggered\": false},{\"triggered\": false}," + 
            "{\"triggered\": false},{\"triggered\": false},{\"triggered\": false}]}";
        var expectedStatus = "Clear";
        try {
            var status = new ValidationOutputs(inputJson).getStatusText();
            assertEquals(expectedStatus, status);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

}
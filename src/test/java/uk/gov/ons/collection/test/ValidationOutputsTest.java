package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.ValidationOutputData;
import uk.gov.ons.collection.entity.ValidationOutputs;
import uk.gov.ons.collection.exception.InvalidJsonException;
import uk.gov.ons.collection.utilities.QlQueryResponse;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationOutputsTest {

    @Test
    void class_invalidJson_throwsExeption() {
        assertThrows(InvalidJsonException.class, () -> new ValidationOutputs("Dummy"));
    }


    @Test
    void getReference_validJson_returnsGivenReference() {
        var inputJson = "{\"validation_outputs\": [{\"reference\": \"ABC\"}]}";
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
        assertThrows(InvalidJsonException.class,
            () -> new ValidationOutputs("{\"validation_outputs\": []}").getReference());
    }

    @Test
    void getPeriod_missingAttribute_throwsException() {
        assertThrows(InvalidJsonException.class,
            () -> new ValidationOutputs("{\"validation_outputs\": []}").getPeriod());
    }

    @Test
    void getSurvey_missingAttribute_throwsException() {
        assertThrows(InvalidJsonException.class,
            () -> new ValidationOutputs("{\"validation_outputs\": []}").getSurvey());
    }

    @Test
    void getPeriod_periodInJson_returnsGivenPeriod() {
        var inputJson = "{\"validation_outputs\": [{\"period\": \"ABC\"}]}";
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
        var inputJson = "{\"validation_outputs\": [{\"survey\": \"ABC\"}]}";
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
        assertThrows(InvalidJsonException.class,
            () -> new ValidationOutputs("{\"validation_outputs\": [{\"a\":\"b\"}]}").getStatusText());
    }

    @Test
    void getStatusText_1triggered_Triggered() {
        var inputJson = "{\"validation_outputs\": [{\"triggered\": true}]}";
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
        var inputJson = "{\"validation_outputs\": [{\"triggered\": false},{\"triggered\": true},{\"triggered\": false}]}";
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
        var inputJson = "{\"validation_outputs\": [{\"triggered\": false}]}";
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
        var inputJson = "{\"validation_outputs\": [{\"triggered\": false},{\"triggered\": false},"
                + "{\"triggered\": false},{\"triggered\": false},{\"triggered\": false}]}";
        var expectedStatus = "Clear";
        try {
            var status = new ValidationOutputs(inputJson).getStatusText();
            assertEquals(expectedStatus, status);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void testParseValidationOutputs_returnsExpectedFormat() {
        String inputString = "{\"data\": {\"allValidationoutputs\": {\"nodes\": [" + "{\"formula\": \"1=1\","
                + "\"triggered\": true, \"lastupdatedby\": null, \"lastupdateddate\": null, \"instance\": 0,"
                + "\"overridden\": true, \"validationoutputid\": 35,"
                + "\"validationformByValidationid\": {\"severity\": \"W\", \"validationid\": 10, \"rule\": VP,"
                + "\"primaryquestion\": \"3000\", \"validationruleByRule\": {\"name\": \"Value Present\"}}}]}}}";

        String expectedOutput = "{\"validation_outputs\":[{\"severity\":\"W\",\"primaryquestion\":\"3000\",\"triggered\":true,"
                + "\"instance\":0,\"validationoutputid\":35,\"validationid\":10,\"lastupdateddate\":null,\"lastupdatedby\":null,\"name\":\"Value Present\","
                + "\"formula\":\"1=1\",\"rule\":\"VP\",\"overridden\":true"
                + "}]}";

        QlQueryResponse response = new QlQueryResponse(inputString);
        System.out.println("Actual output " + response.parseValidationOutputs().toString());
        System.out.println("Expected output " + expectedOutput);
        assertEquals(expectedOutput, response.parseValidationOutputs().toString());
    }

    @Test
    void test_validationOutput() {

        try {
            String graphQLOutput1 = "{\n" +
                    "  \"data\": {\n" +
                    "    \"allValidationoutputs\": {\n" +
                    "      \"nodes\": []\n" +
                    "    }\n" +
                    "  }\n" +
                    "}";


            String graphQLOutput = "{\n" +
                    "    \"data\": {\n" +
                    "      \"allValidationoutputs\": {\n" +
                    "        \"nodes\": [\n" +
                    "          {\n" +
                    "            \"reference\": \"12345678012\",\n" +
                    "            \"period\": \"201801\",\n" +
                    "            \"survey\": \"999A\",\n" +
                    "            \"validationoutputid\": 33,\n" +
                    "            \"triggered\": true,\n" +
                    "            \"instance\": 0,\n" +
                    "            \"formula\": \"abs(40000 - 10000) > 20000 AND 400000 > 0 AND 10000 > 0\",\n" +
                    "            \"validationid\": \"10\",\n" +
                    "            \"overridden\": false\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"reference\": \"12345678012\",\n" +
                    "            \"period\": \"201801\",\n" +
                    "            \"survey\": \"999A\",\n" +
                    "            \"validationoutputid\": 34,\n" +
                    "            \"triggered\": true,\n" +
                    "            \"instance\": 0,\n" +
                    "            \"formula\": \"2 = 2\",\n" +
                    "            \"validationid\": \"20\",\n" +
                    "            \"overridden\": false\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"reference\": \"12345678012\",\n" +
                    "            \"period\": \"201801\",\n" +
                    "            \"survey\": \"999A\",\n" +
                    "            \"validationoutputid\": 36,\n" +
                    "            \"triggered\": true,\n" +
                    "            \"instance\": 0,\n" +
                    "            \"formula\": \"'0' != ''\",\n" +
                    "            \"validationid\": \"30\",\n" +
                    "            \"overridden\": false\n" +
                    "          },\n" +
                    "          {\n" +
                    "            \"reference\": \"12345678012\",\n" +
                    "            \"period\": \"201801\",\n" +
                    "            \"survey\": \"999A\",\n" +
                    "            \"validationoutputid\": 39,\n" +
                    "            \"triggered\": true,\n" +
                    "            \"instance\": 0,\n" +
                    "            \"formula\": \"543 != 5143\",\n" +
                    "            \"validationid\": \"100\",\n" +
                    "            \"overridden\": false\n" +
                    "          }  \n" +
                    "        ]\n" +
                    "      }\n" +
                    "    }\n" +
                    "  }";
            String lambdaoutput = "{\"validation_outputs\": [\n" +
                    "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 70, \"bpmid\": \"0\", \"instance\": 0}, \n" +
                    "      {\"formula\": \"2 = 2\", \"triggered\": true, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 20, \"bpmid\": \"0\", \"instance\": 0},\n" +
                    "      {\"formula\": \"0 != 0\", \"triggered\": false, \"validation\": \"QVDQ\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 30, \"bpmid\": \"0\", \"instance\": 0}]}";
            ValidationOutputs outputs = new  ValidationOutputs(lambdaoutput);

            List<ValidationOutputData> validationData = outputs.extractValidationDataFromDatabase(graphQLOutput);
            List<ValidationOutputData> lambdaData = outputs.extractValidationDataFromLambda();

            System.out.println("Lambda Count " + lambdaData.size());
            System.out.println("Graph QL Validation output record count " + validationData.size());

            System.out.println("Validation Data" + validationData.toString());
            System.out.println("Lambda Data" + lambdaData.toString());


            List<ValidationOutputData> insertList = outputs.getValidationOutputInsertList(lambdaData, validationData);
            List<ValidationOutputData> modifiedList = outputs.getValidationOutputModifiedList(lambdaData, validationData);
            List<ValidationOutputData> upsertList = outputs.getValidationOutputUpsertList(modifiedList, insertList);
            List<ValidationOutputData> deleteData = outputs.getDeleteValidationOutputList(lambdaData, validationData);

            String graphQLQuery = outputs.buildUpsertByArrayQuery(upsertList, deleteData);
            System.out.println(graphQLQuery);

        } catch (Exception exp) {
            exp.printStackTrace();
            assertTrue(false);
        }
    }

}
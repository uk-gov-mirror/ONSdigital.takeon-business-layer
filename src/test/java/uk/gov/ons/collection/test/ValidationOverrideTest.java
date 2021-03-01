package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.ValidationData;
import uk.gov.ons.collection.entity.ValidationOverride;
import uk.gov.ons.collection.exception.InvalidJsonException;
import uk.gov.ons.collection.service.GraphQlService;
import uk.gov.ons.collection.service.ValidationOverrideService;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidationOverrideTest {

    String inputJson = "{\n" +
            "       'reference': '12345678000', \n" +
            "       'period': '201801', \n" +
            "       'survey': '999A',\n" +
            "       'validation_outputs': [\n" +
            "       {'validationoutputid': 35, 'override': true, 'user': 'fisdba'}, \n" +
            "       {'validationoutputid': 36, 'override': true, 'user': 'fisdba1'}, \n" +
            "       {'validationoutputid': 39, 'override': false, 'user': 'fisdba2'},   \n" +
            "       {'validationoutputid': 40, 'override': false, 'user': 'fisdba'}]\n" +
            "    }";

    String graphQlOutput = "{\"data\":{\"allValidationoutputs\":{\"nodes\":[{\"validationoutputid\":35,\"overridden\":true}," +
            "{\"validationoutputid\":36,\"overridden\":false},{\"validationoutputid\":39,\"overridden\":true}," +
            "{\"validationoutputid\":40,\"overridden\":false}]}}}";
    String graphQlOutput1 = "{\"data\":{\"allValidationoutputs\":{\"nodes\":[{\"validationoutputid\":35,\"overridden\":true}," +
            "{\"validationoutputid\":36,\"overridden\":true},{\"validationoutputid\":39,\"overridden\":true}," +
            "{\"validationoutputid\":40,\"overridden\":true}]}}}";

    String inputJson1 = "{\n" +
            "       'reference': '12345678000', \n" +
            "       'period': '201801', \n" +
            "       'survey': '999A',\n" +
            "       'validation_outputs': [\n" +
            "       {'validationoutputid': 35, 'override': true, 'user': 'fisdba'}, \n" +
            "       {'validationoutputid': 36, 'override': true, 'user': 'fisdba1'}, \n" +
            "       {'validationoutputid': 39, 'override': true, 'user': 'fisdba2'},   \n" +
            "       {'validationoutputid': 40, 'override': true, 'user': 'fisdba'}]\n" +
            "    }";

    private static final String STATUS_CLEAR_OVERRIDDEN = "Clear - overridden";
    private static final String STATUS_CHECK_NEEDED = "Check needed";

    @Test
    void class_validation_override_invalidJson_throwsExeption() {
        assertThrows(InvalidJsonException.class, () -> new ValidationOverride("dummy_validation_output_data"));
    }


    @Test
    void test_validation_override_dateFromUI() {

        try {
            ValidationOverride overrideObject = new ValidationOverride(inputJson);
            List<ValidationData> uiDataList = overrideObject.extractValidationDataFromUI();
            List<ValidationData> expectedUiDatalist = Arrays.asList(new ValidationData(35, true, "fisdba", null),
                    new ValidationData(36, true, "fisdba1", null),
                    new ValidationData(39, false, "fisdba2", null),
                    new ValidationData(40, false, "fisdba", null));
            assertEquals(expectedUiDatalist.toString(), uiDataList.toString());

        } catch (Exception exp) {
            assertTrue(false);
        }

    }

    @Test
    void test_validationOutput_override_graphQl_query() {

        try {
            ValidationOverride overrideObject = new ValidationOverride(inputJson);
            overrideObject.extractValidationDataFromUI();
            String actualGraphQlQuery = overrideObject.buildValidationOutputQuery();
            String expectedGraphQlQuery = "{\"query\":\"query validationoutputinformation {allValidationoutputs(condition:" +
                    " {reference: \\\"12345678000\\\",period: \\\"201801\\\",survey: \\\"999A\\\",triggered: true}){nodes {validationoutputid " +
                    "overridden }}}\"}";
            assertEquals(expectedGraphQlQuery, actualGraphQlQuery);

        } catch (Exception exp) {
            assertTrue(false);
        }

    }

    @Test
    void test_validationOverride_updatedList_sentToDatabase() {

        try {
            ValidationOverride overrideObject = new ValidationOverride(inputJson);
            List<ValidationData> databaseList = overrideObject.extractValidationDataFromDatabase(graphQlOutput);
            List<ValidationData> uiList = overrideObject.extractValidationDataFromUI();
            List<ValidationData> updatedList = overrideObject.extractUpdatedValidationOutputData(uiList, databaseList);
            String actualUpdatedElements =  updatedList.toString();
            String expectedUpdatedElement1 = "validationOutputId=36, overridden=true, lastupdatedBy='fisdba1'";
            String expectedUpdatedElement2 = "validationOutputId=39, overridden=false, lastupdatedBy='fisdba2'";

            //check the element validationOutputId=36 exists in the updated list
            assertTrue(actualUpdatedElements.contains(expectedUpdatedElement1));

            //check the element validationOutputId=39 exists in the updated list
            assertTrue(actualUpdatedElements.contains(expectedUpdatedElement2));

            String outputUpdateQuery = overrideObject.buildUpdateByArrayQuery(updatedList);

            String expectedDatabaseElement1 = "validationoutputid: 36,overridden: true";
            String expectedDatabaseElement2 = "validationoutputid: 39,overridden: false";
            //check the element validationOutputId=36 exists in the graphql query sent to database
            assertTrue(outputUpdateQuery.contains(expectedDatabaseElement1));

            //check the element validationOutputId=39 exists in the graphql query sent to database
            assertTrue(outputUpdateQuery.contains(expectedDatabaseElement2));

        } catch (Exception exp) {
            assertTrue(false);
        }

    }

    @Test
    void test_invalidJson_validationOutput_from_database_throwsException() {
        String graphQlOutput = "{\"data\":{\"allValidationoutput\":{\"nodes\":[{\"validationoutputid\":35,\"overridden\":true}," +
                "{\"validationoutputid\":36,\"overridden\":false},{\"validationoutputid\":39,\"overridden\":true}," +
                "{\"validationoutputid\":40,\"overridden\":false}]}}}";

        assertThrows(InvalidJsonException.class, () -> new ValidationOverride(inputJson).extractValidationDataFromDatabase(graphQlOutput));

    }

    @Test
    void test_validation_override_no_graphQl_service_throwsException() {
        GraphQlService service = new GraphQlService();
        ValidationOverrideService validationService = new ValidationOverrideService(inputJson, service);

        assertThrows(NullPointerException.class, () -> validationService.processValidationDataAndSave());

    }

    @Test
    void test_validationOverride_formStatus_clear() {

        try {
            ValidationOverride overrideObject = new ValidationOverride(inputJson1);
            List<ValidationData> databaseList = overrideObject.extractValidationDataFromDatabase(graphQlOutput1);
            List<ValidationData> uiList = overrideObject.extractValidationDataFromUI();
            overrideObject.extractUpdatedValidationOutputData(uiList, databaseList);
            int triggerCount = databaseList.size();
            String statusText = overrideObject.processStatusMessage(triggerCount);
            //check Form Status - Clear
            assertEquals(STATUS_CLEAR_OVERRIDDEN, statusText);
            String contributorStatusQuery = overrideObject.buildContributorStatusQuery(statusText);
            assertTrue(contributorStatusQuery.contains(STATUS_CLEAR_OVERRIDDEN));
        } catch (Exception exp) {
            assertTrue(false);
        }
    }

    @Test
    void test_BpmContract_All_formStatuses() {

        try {
            ValidationOverride overrideObject = new ValidationOverride(inputJson1);
            String actualStatusText = overrideObject.processBpmStatusMessage("Clear - overridden");
            //check Form Status - Overridden
            assertEquals("OVERRIDDEN", actualStatusText);

            //check Form Status - Check needed
            actualStatusText = overrideObject.processBpmStatusMessage("Check needed");
            assertEquals("CHECK_NEEDED", actualStatusText);

            //check Form Status - Empty
            actualStatusText = overrideObject.processBpmStatusMessage("Clear");
            assertEquals("", actualStatusText);

        } catch (Exception exp) {
            assertTrue(false);
        }
    }

    @Test
    void test_BpmContract_ValidationPassedFlag() {

        try {
            ValidationOverride overrideObject = new ValidationOverride(inputJson1);
            List<ValidationData> databaseList = overrideObject.extractValidationDataFromDatabase(graphQlOutput);
            List<ValidationData> uiList = overrideObject.extractValidationDataFromUI();
            overrideObject.extractUpdatedValidationOutputData(uiList, databaseList);
            int triggerCount = databaseList.size();
            boolean actualValidationPassed = overrideObject.processValidationPassedMessage(triggerCount);
            assertTrue(actualValidationPassed);

        } catch (Exception exp) {
            assertTrue(false);
        }
    }

    @Test
    void test_validationOverride_formStatus_checkNeeded() {

        try {
            ValidationOverride overrideObject = new ValidationOverride(inputJson);
            List<ValidationData> databaseList = overrideObject.extractValidationDataFromDatabase(graphQlOutput);
            List<ValidationData> uiList = overrideObject.extractValidationDataFromUI();
            overrideObject.extractUpdatedValidationOutputData(uiList, databaseList);
            int triggerCount = databaseList.size();
            String statusText = overrideObject.processStatusMessage(triggerCount);
            assertEquals(STATUS_CHECK_NEEDED, statusText);
            String contributorStatusQuery = overrideObject.buildContributorStatusQuery(statusText);
            //check Form Status - n
            assertTrue(contributorStatusQuery.contains(STATUS_CHECK_NEEDED));
        } catch (Exception exp) {
            assertTrue(false);
        }
    }


}

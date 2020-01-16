package uk.gov.ons.collection.test;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.ValidationData;
import uk.gov.ons.collection.entity.ValidationOutputs;
import uk.gov.ons.collection.entity.ValidationOverride;
import uk.gov.ons.collection.exception.InvalidJsonException;
import uk.gov.ons.collection.service.GraphQlService;
import uk.gov.ons.collection.service.ValidationOverrideService;

import java.util.ArrayList;
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

    String graphQlOutput = "{\"data\":{\"allValidationoutputs\":{\"nodes\":[{\"validationoutputid\":35,\"overriddenby\":null,\"overriddendate\":null,\"overridden\":true}," +
            "{\"validationoutputid\":36,\"overriddenby\":\"fisdba1\",\"overriddendate\":\"2020-01-15T10:40:42.415+00:00\",\"overridden\":false},{\"validationoutputid\":39," +
            "\"overriddenby\":\"fisba\",\"overriddendate\":\"2020-01-08T12:04:45.506+00:00\",\"overridden\":true},{\"validationoutputid\":40,\"overriddenby\":\"fisba\",\"overriddendate\":\"2020-01-08T12:04:45.506+00:00\",\"overridden\":false}]}}}";

    @Test
    void class_validation_override_invalidJson_throwsExeption() {
        assertThrows(InvalidJsonException.class, () -> new ValidationOverride("dummy_validation_output_data"));
    }
    @Test
    void test_validation_override() {

        try {
            ValidationOverride overrideObject = new ValidationOverride(inputJson);
            List<ValidationData> uiList = overrideObject.extractValidationDataFromUI();
            String validationGraphQlQuery = overrideObject.buildValidationOutputQuery();
            System.out.println("Validation GraphQl Query "+validationGraphQlQuery);
            List<ValidationData> databaseList = overrideObject.extractValidationDataFromDatabase(graphQlOutput);
            List<ValidationData> validationList = new ArrayList<ValidationData>();
            overrideObject.buildUpdateByArrayQuery(validationList);

            List<ValidationData> list = Arrays.asList(new ValidationData(34, true, "fisdba", "16/01/2019"),
                    new ValidationData(35, false, "fisdba", "16/01/2019"));
            List<ValidationData> uiDataList = new ArrayList<ValidationData>();
            uiDataList.add(new ValidationData(34, true, "fisdba", "16/01/2019"));
            uiDataList.add(new ValidationData(35, false, "fisdba", "16/01/2019"));

            List<ValidationData> dbList = new ArrayList<ValidationData>();
            dbList.add(new ValidationData(34, false, "fisdba", "16/01/2019"));
            dbList.add(new ValidationData(35, false, "fisdba", "16/01/2019"));

            List<ValidationData> updatedList = overrideObject.extractUpdatedValidationOutputData(uiList, databaseList);
            String outputUpdateQuery = overrideObject.buildUpdateByArrayQuery(updatedList);
            System.out.println("Updated List: "+ updatedList.toString());
            System.out.println("Update GraphQl Query: "+ outputUpdateQuery);

        } catch (Exception exp) {
            assertTrue(false);
        }

    }

    @Test
    void class_validation_override_no_graphql_service_throwsExeption() {
        GraphQlService service = new GraphQlService();
        ValidationOverrideService validationService = new ValidationOverrideService(inputJson, service);

        assertThrows(NullPointerException.class, () -> validationService.processValidationDataAndSave());
    }
}

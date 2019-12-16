package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;

import uk.gov.ons.collection.entity.ValidationConfig;
import uk.gov.ons.collection.exception.InvalidJsonException;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ContributorConfigTest {

    @Test
    public void load_minimumViableSubset_givesJsonOutputInSimplerFormat() {
        var validQlResponse = "{ \"data\":{" +
            "\"allValidationforms\":{" +
                "\"nodes\":[{ " +
                "\"Dummy\": 1," +
                "\"validationparametersByValidationid\":{" +
                    "\"nodes\":[" +
                        "{\"dummy\":\"Dummy\"}" +
                    "]" +
                "}," +
                "\"validationruleByRule\":{" +
                    "\"rule\":\"DummyRule\"," +
                    "\"name\":\"DummyRuleName\"," +
                    "\"baseformula\":\"DummyFormula\"," +
                "\"validationperiodsByRule\":{" +
                    "\"nodes\":[" +
                        "{\"periodoffset\":12}," +
                        "{\"periodoffset\":6}," +
                    "]" +
                "}" +
                "}}]}}}";
        var expectedOutput = "{\"validation_config\":[{" +
            "\"baseformula\":\"DummyFormula\"," +
            "\"name\":\"DummyRuleName\"," +
            "\"period_offset\":[" +
                "{\"periodoffset\":12}," +
                "{\"periodoffset\":6}" +
                "]," +
            "\"rule\":\"DummyRule\"," +
            "\"Dummy\":1," +
            "\"parameters\":[" +
                "{\"dummy\":\"Dummy\"}]}]}";

        try {
            var validationConfig = new ValidationConfig(validQlResponse);
            assertEquals(expectedOutput, validationConfig.getValidationConfig());
        } catch (InvalidJsonException ex) {
            assertEquals("Test input string is invalid", ex.getMessage());
        }
    }
/*
    @Test
    public void getValidationConfig_nullInput_throwsException() {
        assertThrows(InvalidJsonException.class, () -> {
            new ValidationConfig(null);
        });
    }

    @Test
    public void getValidationConfig_invalidInput_throwsException() {
        assertThrows(InvalidJsonException.class, () -> {
            new ValidationConfig("{sdfasdhhbf324,sdf./234u7123;.asdfr}");
        });
    }

    @Test
    public void getUniqueOffsets_duplicateOffsets_givesCorrectUniqueList() {
        var validQlResponseWithDuplicates = "{ \"data\":{" +
            "\"allValidationforms\":{" +
                "\"nodes\":[{ " +
                "\"Dummy\": 1," +
                "\"validationparametersByValidationid\":{" +
                    "\"nodes\":[" +
                        "{\"dummy\":\"Dummy\"}" +
                    "]" +
                "}," +
                "\"validationruleByRule\":{" +
                    "\"rule\":\"DummyRule\"," +
                    "\"name\":\"DummyRuleName\"," +
                    "\"baseformula\":\"DummyFormula\"," +
                "\"validationperiodsByRule\":{" +
                    "\"nodes\":[" +
                        "{\"periodoffset\":1}," +
                        "{\"periodoffset\":2}," +
                        "{\"periodoffset\":6}," +
                        "{\"periodoffset\":8}," +
                        "{\"periodoffset\":2}," +
                        "{\"periodoffset\":8}," +
                    "]" +
                "}" +
                "}}]}}}";
        var expectedOutput = new ArrayList<Integer>(Arrays.asList(1,2,6,8));

        try {
            var validationConfig = new ValidationConfig(validQlResponseWithDuplicates);
            assertEquals(expectedOutput, validationConfig.getUniqueOffsets());
        } catch (InvalidJsonException ex) {
            assertEquals("Test input string is invalid", ex.getMessage());
        }
    }

    @Test
    public void getUniqueOffsets_noOffsets_givesEmptyList() {
        var validQlResponseWithNoOffsets = "{ \"data\":{" +
            "\"allValidationforms\":{" +
                "\"nodes\":[{ " +
                "\"validationparametersByValidationid\":{" +
                    "\"nodes\":[]" +
                "}," +
                "\"validationruleByRule\":{" +
                    "\"rule\":\"DummyRule\"," +
                    "\"name\":\"DummyRuleName\"," +
                    "\"baseformula\":\"DummyFormula\"," +
                "\"validationperiodsByRule\":{" +
                    "\"nodes\":[]" +
                "}" +
                "}}]}}}";

        try {
            var validationConfig = new ValidationConfig(validQlResponseWithNoOffsets);
            assertTrue(validationConfig.getUniqueOffsets().isEmpty());
        } catch (InvalidJsonException ex) {
            assertEquals("Test input string is invalid", ex.getMessage());
        }
    }

    @Test
    public void getUniqueOffsets_missingOffset_throwsException() {
        var validQlResponseWithMissingOffsetAttribute = "{ \"data\":{" +
            "\"allValidationforms\":{" +
                "\"nodes\":[{ " +
                "\"validationparametersByValidationid\":{" +
                    "\"nodes\":[]" +
                "}," +
                "\"validationruleByRule\":{" +
                    "\"rule\":\"DummyRule\"," +
                    "\"name\":\"DummyRuleName\"," +
                    "\"baseformula\":\"DummyFormula\"," +
                "\"validationperiodsByRule\":{" +
                    "\"nodes\":[{\"NotOffset\":12}]" +
                "}" +
                "}}]}}}";

        assertThrows(InvalidJsonException.class, () -> {
            new ValidationConfig(validQlResponseWithMissingOffsetAttribute).getUniqueOffsets();
        });
    }
*/
}
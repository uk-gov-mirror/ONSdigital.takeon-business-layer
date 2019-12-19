package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.ContributorConfig;
import uk.gov.ons.collection.exception.InvalidJsonException;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

public class ContributorConfigTest {

    @Test
    public void load_minimumViableSubset_givesJsonOutputInSimplerFormat() {
        var inputJson = new ArrayList<>(Arrays.asList(
            "{\"data\":{" +
                "\"contributorByReferenceAndPeriodAndSurvey\":{" +
                    "\"survey\": \"4321\"," +
                    "\"period\": \"12345678\"," +
                    "\"responsesByReferenceAndPeriodAndSurvey\": {\"nodes\": []}," +
                    "\"formByFormid\": {formdefinitionsByFormid: {\"nodes\": [{\"test\": \"NotEmpty\"}]}}" +
            "}}}"));
        var expectedJson = "{" +
            "\"contributor\":[{\"period\":\"12345678\",\"survey\":\"4321\"}]," +
            "\"response\":[]," +
            "\"question_schema\":[{\"period\":\"12345678\",\"test\":\"NotEmpty\",\"survey\":\"4321\"}]}";
        try {
            var contributorConfig = new ContributorConfig(inputJson).getContributorConfig();
            JSONAssert.assertEquals(expectedJson, contributorConfig, JSONCompareMode.LENIENT);
        } catch (InvalidJsonException ex) {
            assertEquals("Test input string is invalid. You shouldn't see this assert!", ex.getMessage());
        }
    }

    @Test
    public void load_missingFormDefinition_throwsException() {
        var inputJson = new ArrayList<>(Arrays.asList(
            "{\"data\":{" +
                "\"contributorByReferenceAndPeriodAndSurvey\":{" +
                    "\"survey\": \"4321\"," +
                    "\"period\": \"12345678\"," +
                    "\"responsesByReferenceAndPeriodAndSurvey\": {\"nodes\": []}," +
                    "\"formByFormid\": {formdefinitionsByFormid: {\"nodes\": []}}" +
            "}}}"));
        Assertions.assertThrows(InvalidJsonException.class, () -> {
            new ContributorConfig(inputJson).getContributorConfig();
        });
    }

    @Test
    public void load_twoContributorsButSecondNotFound_givesOnlyFirstContributor() {
        var inputJson = new ArrayList<>(Arrays.asList(
            "{\"data\":{" +
                "\"contributorByReferenceAndPeriodAndSurvey\":{" +
                    "\"survey\": \"4321\"," +
                    "\"period\": \"12345678\"," +
                    "\"responsesByReferenceAndPeriodAndSurvey\": {\"nodes\": []}," +
                    "\"formByFormid\": {formdefinitionsByFormid: {\"nodes\": [{\"test\": \"NotEmpty\"}]}}" +
            "}}}",
            "{\"data\":{}}"));
        var expectedJson = "{" +
            "\"contributor\":[{\"period\":\"12345678\",\"survey\":\"4321\"}]," +
            "\"response\":[]," +
            "\"question_schema\":[{\"period\":\"12345678\",\"test\":\"NotEmpty\",\"survey\":\"4321\"}]}";
        try {
            var contributorConfig = new ContributorConfig(inputJson).getContributorConfig();
            JSONAssert.assertEquals(expectedJson, contributorConfig, JSONCompareMode.LENIENT);
        } catch (InvalidJsonException ex) {
            assertEquals("Test input string is invalid. You shouldn't see this assert!", ex.getMessage());
        }
    }

    @Test
    public void load_oneContributorWithResponses_giveSimplerJsonOutput() {
        var inputJson = new ArrayList<>(Arrays.asList(
            "{\"data\":{" +
                "\"contributorByReferenceAndPeriodAndSurvey\":{" +
                    "\"survey\": \"4321\"," +
                    "\"period\": \"12345678\"," +
                    "\"responsesByReferenceAndPeriodAndSurvey\": {\"nodes\": [{\"q1\":\"x\"}]}," +
                    "\"formByFormid\": {formdefinitionsByFormid: {\"nodes\": [{\"test\": \"NotEmpty\"}]}}" +
            "}}}",
            "{\"data\":{}}"));
        var expectedJson = "{" +
            "\"contributor\":[{\"period\":\"12345678\",\"survey\":\"4321\"}]," +
            "\"response\":[{\"q1\":\"x\"}]," +
            "\"question_schema\":[{\"period\":\"12345678\",\"test\":\"NotEmpty\",\"survey\":\"4321\"}]}";
        try {
            var contributorConfig = new ContributorConfig(inputJson).getContributorConfig();
            JSONAssert.assertEquals(expectedJson, contributorConfig, JSONCompareMode.LENIENT);
        } catch (InvalidJsonException ex) {
            assertEquals("Test input string is invalid. You shouldn't see this assert!", ex.getMessage());
        }
    }

    @Test
    public void load_invalidJson_throwsException() {
        var inputJson = new ArrayList<>(Arrays.asList("{Invalid}"));
        Assertions.assertThrows(InvalidJsonException.class, () -> {
            new ContributorConfig(inputJson).getContributorConfig();
        });
    }

    @Test
    public void load_nullJson_throwsException() {
        Assertions.assertThrows(InvalidJsonException.class, () -> {
            new ContributorConfig(null).getContributorConfig();
        });
    }

    @Test
    public void load_nearlyEmptyDataset_throwsException() {
        var inputJson = new ArrayList<>(Arrays.asList("{\"data\":{}}"));
        Assertions.assertThrows(InvalidJsonException.class, () -> {
            new ContributorConfig(inputJson).getContributorConfig();
        });
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
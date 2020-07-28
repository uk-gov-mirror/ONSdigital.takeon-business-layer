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
}
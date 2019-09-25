package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.ContributorEntity;
import uk.gov.ons.collection.entity.ReturnedValidationOutputs;
import uk.gov.ons.collection.entity.ValidationFormEntity;
import uk.gov.ons.collection.service.ApiCallerTest;
import uk.gov.ons.collection.service.ValidationRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationRunnerTest {

    @Test
    void Given1Contributor_getformid() {
        ArrayList<ContributorEntity> testContributors = new ArrayList<>();
        testContributors.add(new ContributorEntity().builder().formid(5).build());
        ApiCallerTest dataLoader = new ApiCallerTest().builder().contributors(testContributors).build();
        ValidationRunner runner = new ValidationRunner("","","",dataLoader);
        assertEquals(5,runner.getFormIdFromForm());
    }

    @Test
    void Given2Contributor_getformid_ReturnsFirstOneInList() {
        ArrayList<ContributorEntity> testContributors = new ArrayList<>();
        testContributors.add(new ContributorEntity().builder().formid(5).build());
        testContributors.add(new ContributorEntity().builder().formid(6).build());
        ApiCallerTest dataLoader = new ApiCallerTest().builder().contributors(testContributors).build();

        ValidationRunner runner = new ValidationRunner("","","",dataLoader);
        assertEquals(5,runner.getFormIdFromForm());
    }

    @Test
    void Given0Contributor_getformid_Return0() {
        ArrayList<ContributorEntity> testContributors = new ArrayList<>();
        ApiCallerTest dataLoader = new ApiCallerTest().builder().contributors(testContributors).build();
        ValidationRunner runner = new ValidationRunner("","","",dataLoader);
        assertEquals(0,runner.getFormIdFromForm());
    }

    @Test
    void getUniqueListOfRule_givenEmptyList_returnsEmptyList() {
        List<ValidationFormEntity> validationConfig = new ArrayList<>();
        ApiCallerTest dataLoader = new ApiCallerTest().builder().validationConfig(validationConfig).build();
        ValidationRunner runner = new ValidationRunner("","","",dataLoader);
        assertEquals(0, runner.getUniqueListOfRules(0).size());
    }

    @Test
    void getUniqueListOfRule_givenConfigWithOneRule_returnsOneRule() {
        List<ValidationFormEntity> validationConfig = new ArrayList<>();
        validationConfig.add(new ValidationFormEntity().builder().validationCode("VP").build());
        ApiCallerTest dataLoader = new ApiCallerTest().builder().validationConfig(validationConfig).build();
        ValidationRunner runner = new ValidationRunner("","","",dataLoader);
        assertEquals(1, runner.getUniqueListOfRules(0).size());
        assertEquals("VP", runner.getUniqueListOfRules(0).get(0));
    }

    @Test
    void getUniqueListOfRule_givenConfigWithThreeRule_returnsTwoRule() {
        List<ValidationFormEntity> validationConfig = new ArrayList<>();
        validationConfig.add(new ValidationFormEntity().builder().validationCode("VP").build());
        validationConfig.add(new ValidationFormEntity().builder().validationCode("VP").build());
        validationConfig.add(new ValidationFormEntity().builder().validationCode("POPM").build());

        ApiCallerTest dataLoader = new ApiCallerTest().builder().validationConfig(validationConfig).build();

        ValidationRunner runner = new ValidationRunner("","","",dataLoader);

        List<String> listOfRules = runner.getUniqueListOfRules(0);
        assertEquals(2, listOfRules.size());
        assertTrue(listOfRules.contains("VP"));
        assertTrue(listOfRules.contains("POPM"));
    }

    @Test
    void givenNoApiAvailable_pickRulesToRun_GivesEmptyList() {
        List<ReturnedValidationOutputs> mockedOutput = new ArrayList<>();
        List<String> ruleList = new ArrayList<>(Arrays.asList("VP","POPM"));
        ApiCallerTest dataLoader = new ApiCallerTest().builder().returnedValidationOutputs(mockedOutput).build();
        ValidationRunner runner = new ValidationRunner("", "", "", dataLoader);
        assertEquals(0, runner.callEachValidationApi(ruleList).size());
    }

}
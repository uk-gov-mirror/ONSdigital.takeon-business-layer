package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.ContributorEntity;
import uk.gov.ons.collection.service.ValidationRunner;
import uk.gov.ons.collection.service.DataLoaderTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidationRunnerTest {

    @Test
    void Given1Contributor_getFormId() {
        ContributorEntity contributor = new ContributorEntity().builder().formId(5).build();
        ArrayList<ContributorEntity> testContributors = new ArrayList<>();
        testContributors.add(contributor);
        DataLoaderTest dataLoader = new DataLoaderTest(testContributors,null);
        ValidationRunner runner = new ValidationRunner("","","",dataLoader);
        assertEquals(5,runner.getFormId());
    }

    @Test
    void Given2Contributor_getFormId_ReturnsFirstOneInList() {
        ArrayList<ContributorEntity> testContributors = new ArrayList<>();

        ContributorEntity contributor = new ContributorEntity().builder().formId(5).build();
        testContributors.add(contributor);
        contributor = new ContributorEntity().builder().formId(6).build();
        testContributors.add(contributor);

        DataLoaderTest dataLoader = new DataLoaderTest(testContributors,null);
        ValidationRunner runner = new ValidationRunner("","","",dataLoader);
        assertEquals(5,runner.getFormId());
    }

    @Test
    void Given0Contributor_getFormId_Return0() {
        ArrayList<ContributorEntity> testContributors = new ArrayList<>();
        DataLoaderTest dataLoader = new DataLoaderTest(testContributors,null);
        ValidationRunner runner = new ValidationRunner("","","",dataLoader);
        assertEquals(0,runner.getFormId());
    }

    @Test
    void getUniqueListOfRule() {
    }

    @Test
    void pickRulesToRun() {
    }

    @Test
    void runValidation() {
    }
}
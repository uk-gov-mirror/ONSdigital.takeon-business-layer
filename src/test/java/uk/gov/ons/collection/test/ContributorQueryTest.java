package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.ContributorQuery;

import static org.junit.Assert.assertEquals;

public class ContributorQueryTest {

    @Test
    public void givenAttributes_loadQuery_producesValidJsonString() {
        var expectedResponse = "{\"output\":\"json\"}";
        var fakeQlService = new FakeGraphQlServiceAssertJson(expectedResponse);
        var contributorQuery = new ContributorQuery("ref", "period", "survey", fakeQlService);
        assertEquals(expectedResponse, contributorQuery.load());
    }

    @Test
    public void givenAttributes_updatingStatusQuery_producesValidJsonString() {
        var expectedResponse = "{\"output\":\"json\"}";
        var fakeQlService = new FakeGraphQlServiceAssertJson(expectedResponse);
        var contributorQuery = new ContributorQuery("ref", "period", "survey", fakeQlService);
        assertEquals(expectedResponse, contributorQuery.updateStatus(""));
    }

}
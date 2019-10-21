package uk.gov.ons.collection.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import uk.gov.ons.collection.entity.ContributorStatus;

public class ContributorStatusTest {

    @Test
    void buildUpdateQuery_validValues_buildsUpdateQuery() {        
        var expectedQuery = "{\"query\": \"mutation updateStatus($period: String!, $reference: String!, $survey: String!, $status: String!) " + 
            "{updateContributorByReferenceAndPeriodAndSurvey(input: {reference: $reference, period: $period, survey: $survey, contributorPatch: " + 
            "{status: $status}}) {contributor { reference period survey status }}}\",\"variables\": {\"reference\": \"1234567890\",\"period\": \"199912\"" +
            ",\"survey\": \"999B\",\"status\": \"StatusUp\"}}";
        var query = new ContributorStatus("1234567890","199912","999B","StatusUp").buildUpdateQuery();
        assertEquals(expectedQuery, query);
    }

    @Test
    void buildUpdateQuery_nullValues_buildsUpdateQueryWithNulls() {        
        var expectedQuery = "{\"query\": \"mutation updateStatus($period: String!, $reference: String!, $survey: String!, $status: String!) " +
            "{updateContributorByReferenceAndPeriodAndSurvey(input: {reference: $reference, period: $period, survey: $survey, contributorPatch: " + 
            "{status: $status}}) {contributor { reference period survey status }}}\",\"variables\": {\"reference\": \"null\",\"period\": \"null\"," + 
            "\"survey\": \"null\",\"status\": \"null\"}}";
        var query = new ContributorStatus(null,null,null,null).buildUpdateQuery();
        assertEquals(expectedQuery, query);
    }
}

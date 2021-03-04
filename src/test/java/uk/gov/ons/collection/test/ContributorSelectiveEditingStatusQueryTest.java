package uk.gov.ons.collection.test;
import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.ContributorSelectiveEditingStatusQuery;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ContributorSelectiveEditingStatusQueryTest {
    Map<String,String> parameters = new HashMap<>();

    @Test
    void verify_date_adjustment_graphql_query() {
        parameters.put("reference", "49900534932");
        parameters.put("period", "201904");
        parameters.put("survey", "023");
        String expectedQuery = "{\"query\": \"query dateadjustmentconfig { allContributors(filter: {reference: {equalTo: \\\"49900534932\\\"}, survey: {equalTo: \\\"023\\\"}, period: {equalTo: \\\"201904\\\"}}) {nodes { survey period reference formid status contributorselectiveeditingByReferenceAndPeriodAndSurvey {reference  period survey flag }}}}\"}";
        ContributorSelectiveEditingStatusQuery contributorStatusQuery = new ContributorSelectiveEditingStatusQuery(parameters);
        String actualGraphQLQuery = contributorStatusQuery.buildContributorSelectiveEditingStatusQuery();
        System.out.println(actualGraphQLQuery);
        assertEquals(expectedQuery, actualGraphQLQuery);
    }

    @Test
    void verify_null_input_parameters_date_adjustment_input_data_ThrowsAnException() {
        Map<String,String> parameters = null;
        ContributorSelectiveEditingStatusQuery contributorStatusQuery = new ContributorSelectiveEditingStatusQuery(parameters);
        assertThrows(NullPointerException.class, () -> contributorStatusQuery.retrieveCurrentReference());
        assertThrows(NullPointerException.class, () -> contributorStatusQuery.retrieveSurvey());
        assertThrows(NullPointerException.class, () -> contributorStatusQuery.retrieveCurrentPeriod());
    }

    @Test
    void verify_input_parameters_date_adjustment_data() {
        String expectedReference = "49900534932";
        String expectedPeriod = "201904";
        String expectedSurvey = "023";
        parameters.put("reference", "49900534932");
        parameters.put("period", "201904");
        parameters.put("survey", "023");
        ContributorSelectiveEditingStatusQuery contributorStatusQuery = new ContributorSelectiveEditingStatusQuery(parameters);

        assertEquals(expectedReference, contributorStatusQuery.retrieveCurrentReference());
        assertEquals(expectedPeriod, contributorStatusQuery.retrieveCurrentPeriod());
        assertEquals(expectedSurvey, contributorStatusQuery.retrieveSurvey());
    }


}

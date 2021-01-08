package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.DateAdjustmentQuery;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DateAdjustmentQueryTest {
    Map<String,String> parameters = new HashMap<>();


    @Test
    void verify_date_adjustment_graphql_query() {
        parameters.put("reference", "49900534932");
        parameters.put("period", "201904");
        parameters.put("survey", "023");
        String expectedQuery = "{\"query\": \"query dateadjustmentconfig { allContributors(filter: {reference: {equalTo: \\\"49900534932\\\"}, survey: {equalTo: \\\"023\\\"}, period: {equalTo: \\\"201904\\\"}}) {nodes { survey period reference formid status frozensic resultscellnumber domain formByFormid {survey formid formdefinitionsByFormid { nodes { questioncode dateadjustment }} dateadjustmentreturndateconfigsByFormid { nodes { formid questioncode returndatetype }}} responsesByReferenceAndPeriodAndSurvey { nodes { reference period survey questioncode instance response }}}} allDateadjustmentweightconfigs(filter: {survey: {equalTo: \\\"023\\\"}, period: {equalTo: \\\"201904\\\"}}) { nodes { survey period tradingdate domain weight periodstart periodend }} allContributordateadjustmentconfigs(filter: {reference: {equalTo: \\\"49900534932\\\"}, survey: {equalTo: \\\"023\\\"}, period: {equalTo: \\\"201904\\\"}}) { nodes { reference period survey longperiodparameter shortperiodparameter averageweekly settomidpoint settoequalweighted usecalendardays }}}\"}";
        DateAdjustmentQuery dateAdjustmentQuery = new DateAdjustmentQuery(parameters);
        String actualGraphQLQuery = dateAdjustmentQuery.buildDateAdjustmentConfigQuery();
        assertEquals(expectedQuery, actualGraphQLQuery);
    }

    @Test
    void verify_null_input_parameters_date_adjustment_input_data_ThrowsAnException() {
        Map<String,String> parameters = null;
        DateAdjustmentQuery dateAdjustmentQuery = new DateAdjustmentQuery(parameters);
        assertThrows(NullPointerException.class, () -> dateAdjustmentQuery.retrieveCurrentReference());
        assertThrows(NullPointerException.class, () -> dateAdjustmentQuery.retrieveSurvey());
        assertThrows(NullPointerException.class, () -> dateAdjustmentQuery.retrieveCurrentPeriod());
    }

    @Test
    void verify_input_parameters_date_adjustment_data() {
        String expectedReference = "49900534932";
        String expectedPeriod = "201904";
        String expectedSurvey = "023";
        parameters.put("reference", "49900534932");
        parameters.put("period", "201904");
        parameters.put("survey", "023");
        DateAdjustmentQuery dateAdjustmentQuery = new DateAdjustmentQuery(parameters);

        assertEquals(expectedReference, dateAdjustmentQuery.retrieveCurrentReference());
        assertEquals(expectedPeriod, dateAdjustmentQuery.retrieveCurrentPeriod());
        assertEquals(expectedSurvey, dateAdjustmentQuery.retrieveSurvey());
    }
}

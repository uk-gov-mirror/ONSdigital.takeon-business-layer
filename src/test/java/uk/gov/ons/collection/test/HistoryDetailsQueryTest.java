package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.HistoryDetailsQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryDetailsQueryTest {

    Map<String,String> parameters = new HashMap<>();


    @Test
    void verify_input_parameters_history_data() {
        String expectedReference = "49900534932";
        String expectedPeriod = "201904";
        String expectedSurvey = "023";
        String expected = "\"survey\": \"023\"";
        parameters.put("reference", "49900534932");
        parameters.put("period", "201904");
        parameters.put("survey", "023");
        HistoryDetailsQuery historyQuery = new HistoryDetailsQuery(parameters);

        assertEquals(expectedReference, historyQuery.retrieveCurrentReference());
        assertEquals(expectedPeriod, historyQuery.retrieveCurrentPeriod());
        assertEquals(expectedSurvey, historyQuery.retrieveSurvey());
        assertEquals(expected, historyQuery.buildVariableForPeriodicity());
    }

    @Test
    void verify_periodicity_query() {

        parameters.put("survey", "023");
        HistoryDetailsQuery historyQuery = new HistoryDetailsQuery(parameters);
        String expectedQuery = "{\"query\": \"query getperiodicitysurvey($survey: String) " +
                "{ allSurveys(condition: {survey: $survey}) " +
                "{nodes { periodicity }}}\",\"variables\": {\"survey\": \"023\"}}";
        assertEquals(expectedQuery, historyQuery.buildSurveyPeriodicityQuery());
    }

    @Test
    void verify_history_details_query() {

        parameters.put("survey", "023");
        parameters.put("reference", "49900534932");
        HistoryDetailsQuery historyDetailsQuery = new HistoryDetailsQuery(parameters);
        List<String> historyPeriodList = new ArrayList<String>();
        historyPeriodList.add("201904");
        historyPeriodList.add("201903");
        String expectedQuery = "{\"query\": \"query historydetails { allContributors(filter: " +
                "{reference: {equalTo: \\\"49900534932\\\"}, survey: {equalTo: \\\"023\\\"}," +
                " period: {in: [\\\"201904\\\",\\\"201903\\\"]}}, orderBy:PERIOD_DESC) " +
                "{nodes { survey period reference formByFormid {formdefinitionsByFormid (orderBy: DISPLAYORDER_ASC)" +
                "{ nodes { questioncode type derivedformula displaytext displayquestionnumber displayorder}}} " +
                "responsesByReferenceAndPeriodAndSurvey {nodes {instance questioncode response}}}}}\"}";
        assertEquals(expectedQuery, historyDetailsQuery.buildHistoryDetailsQuery(historyPeriodList));
    }


}

package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.SelectiveEditingQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SelectiveEditingQueryTest {

    Map<String,String> parameters = new HashMap<>();


    @Test
    void verify_input_parameters_selective_editing_data() {
        String expectedReference = "49900534932";
        String expectedPeriod = "201904";
        String expectedSurvey = "023";
        parameters.put("reference", "49900534932");
        parameters.put("period", "201904");
        parameters.put("survey", "023");
        SelectiveEditingQuery selectiveEditingQuery = new SelectiveEditingQuery(parameters);

        assertEquals(expectedReference, selectiveEditingQuery.retrieveCurrentReference());
        assertEquals(expectedPeriod, selectiveEditingQuery.retrieveCurrentPeriod());
        assertEquals(expectedSurvey, selectiveEditingQuery.retrieveSurvey());
    }

    @Test
    void verify_null_input_parameters_selective_editing_input_data_ThrowsAnException() {
        Map<String,String> parameters = null;
        SelectiveEditingQuery selectiveEditingQuery = new SelectiveEditingQuery(parameters);
        assertThrows(NullPointerException.class, () -> selectiveEditingQuery.retrieveCurrentReference());
        assertThrows(NullPointerException.class, () -> selectiveEditingQuery.retrieveSurvey());
        assertThrows(NullPointerException.class, () -> selectiveEditingQuery.retrieveCurrentPeriod());
    }

    @Test
    void verify_selective_editing_configuration_details_query() {

        parameters.put("survey", "023");
        parameters.put("reference", "49900534932");
        parameters.put("period", "201904");
        SelectiveEditingQuery selectiveEditingQuery = new SelectiveEditingQuery(parameters);

        List<String> historyPeriodList = new ArrayList<String>();
        historyPeriodList.add("201904");
        historyPeriodList.add("201903");
        String expectedQuery = "{\"query\": \"query loadselectiveeditingconfig { allContributors(filter: " +
                "{reference: {equalTo: \\\"49900534932\\\"}, survey: {equalTo: \\\"023\\\"}, period: " +
                "{in: [\\\"201904\\\",\\\"201903\\\"]}}, orderBy:PERIOD_DESC) {nodes { survey period reference frozenturnover resultscellnumber domain responsesByReferenceAndPeriodAndSurvey " +
                "{ nodes { questioncode period response }}}} allSelectiveeditingconfigs(filter: " +
                "{survey: {equalTo: \\\"023\\\"}, period: {equalTo: \\\"201904\\\"}}) " +
                "{ nodes { survey period domain questioncode threshold estimate }} allCelldetails(filter: {survey: {equalTo: \\\"023\\\"}, period: {equalTo: \\\"201904\\\"}}) " +
                "{ nodes { survey period cellnumber designweight }}}\"}";
        assertEquals(expectedQuery, selectiveEditingQuery.buildSelectiveEditingLoadConfigQuery(historyPeriodList));

    }



}

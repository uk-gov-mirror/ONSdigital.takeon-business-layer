package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.DateAdjustmentQuery;

import java.util.HashMap;
import java.util.Map;

public class DateAdjustmentQueryTest {
    Map<String,String> parameters = new HashMap<>();


    @Test
    void verify_date_adjustment_graphql_query() {
        parameters.put("reference", "49900534932");
        parameters.put("period", "201904");
        parameters.put("survey", "023");
        DateAdjustmentQuery dateAdjustmentQuery = new DateAdjustmentQuery(parameters);
        String graphQLQuery = dateAdjustmentQuery.buildDateAdjustmentConfigQuery();
        System.out.println(graphQLQuery);
    }
}

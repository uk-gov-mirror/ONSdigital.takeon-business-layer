package uk.gov.ons.collection.test;

import uk.gov.ons.collection.utilities.QlQueryBuilder;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class QlQueryBuilderTest {

    @Test
    void buildVariables_null_blankString() {
        assertEquals("", new QlQueryBuilder(null).buildVariables());
    }

    @Test
    void buildCondition_emptyParameters_blankString() {
        Map<String,String> emptyParameter = new HashMap<>();
        assertEquals("", new QlQueryBuilder(emptyParameter).buildVariables());
    }

    @Test
    void buildCondition_singleParameter_singleCondition() {
        String expectedCondition = "\"reference\": \"4990012\"";
        Map<String,String> singleParameter = new HashMap<>();
        singleParameter.put("reference", "4990012");
        assertEquals(expectedCondition, new QlQueryBuilder(singleParameter).buildVariables());
    }

    @Test
    void buildCondition_twoParameters_twoCondition() {
        String expectedCondition = "\"reference\": \"4990012\",\"survey\": \"011\"";
        Map<String,String> twoParameters = new HashMap<>();
        twoParameters.put("reference", "4990012");
        twoParameters.put("survey", "011");
        assertEquals(expectedCondition, new QlQueryBuilder(twoParameters).buildVariables());
    }

    @Test
    void buildDelayResponseQuery() {
        String expectedQuery = "{\"query\": \"query contributorSearch " +
        "{ allContributors " +
        "{ nodes { period, survey " +
        "}}}\"}";
        String actualQuery = new QlQueryBuilder().buildDelayResponseQuery();
        assertEquals(expectedQuery, actualQuery);
    }

}


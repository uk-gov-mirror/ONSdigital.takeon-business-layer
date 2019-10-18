package uk.gov.ons.collection.test;

import uk.gov.ons.collection.utilities.qlQueryBuilder;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class qlQueryBuilderTest {

    @Test
    void buildVariables_null_blankString() {
        assertEquals("", new qlQueryBuilder(null).buildVariables());
    }

    @Test
    void buildCondition_emptyParameters_blankString() {
        Map<String,String> emptyParameter = new HashMap<>();
        assertEquals("", new qlQueryBuilder(emptyParameter).buildVariables());
    }

    @Test
    void buildCondition_singleParameter_singleCondition() {
        String expectedCondition = "\"reference\": \"4990012\"";
        Map<String,String> singleParameter = new HashMap<>();
        singleParameter.put("reference", "4990012");
        assertEquals(expectedCondition, new qlQueryBuilder(singleParameter).buildVariables());
    }

    @Test
    void buildCondition_twoParameters_twoCondition() {
        String expectedCondition = "\"reference\": \"4990012\",\"survey\": \"011\"";
        Map<String,String> twoParameters = new HashMap<>();
        twoParameters.put("reference", "4990012");
        twoParameters.put("survey", "011");
        assertEquals(expectedCondition, new qlQueryBuilder(twoParameters).buildVariables());
    }

}


package uk.gov.ons.collection.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.controller.qlQueryBuilder;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Ql Query Builder tests")

public class qlQueryBuilderTest {

    String baseQuery = "query contributorSearchBy {" +
            "allContributors {" +
            "nodes {" +
            "reference, period, survey, formid, status, receiptdate, lockedby, lockeddate}}}";

    @Test
    void buildQuery_nullParameters_unfilteredQuery(){
        qlQueryBuilder qlBuild = new qlQueryBuilder();
        assertEquals(baseQuery,qlBuild.buildQuery(null));
    }

    @Test
    void buildQuery_emptyParameters_unfilteredQuery(){
        qlQueryBuilder qlBuild = new qlQueryBuilder();
        Map<String,String> emptyParameters = new HashMap<>();
        assertEquals(baseQuery,qlBuild.buildQuery(emptyParameters));
    }

    @Test
    void buildQuery_singleParameter_filteredQuery(){
        String builtQuery= "query contributorSearchBy {" +
                "allContributors (condition: { reference: \"4990012\" }){" +
                "nodes {" +
                "reference, period, survey, formid, status, receiptdate, lockedby, lockeddate}}}";
        qlQueryBuilder qlBuild = new qlQueryBuilder();
        Map<String,String> singleParameter = new HashMap<>();
        singleParameter.put("reference", "4990012");
        assertEquals(builtQuery,qlBuild.buildQuery(singleParameter));
    }

    @Test
    void buildQuery_twoParameters_filteredQuery(){
        String builtQuery= "query contributorSearchBy {" +
                "allContributors (condition: { reference: \"4990012\" period: \"201903\" }){" +
                "nodes {" +
                "reference, period, survey, formid, status, receiptdate, lockedby, lockeddate}}}";
        qlQueryBuilder qlBuild = new qlQueryBuilder();
        Map<String,String> twoParameters = new HashMap<>();
        twoParameters.put("reference", "4990012");
        twoParameters.put("period", "201903");
        assertEquals(builtQuery,qlBuild.buildQuery(twoParameters));
    }

}


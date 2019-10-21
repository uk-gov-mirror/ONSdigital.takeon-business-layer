package uk.gov.ons.collection.entity;

import java.util.HashMap;
import java.util.Map;

import uk.gov.ons.collection.utilities.QlQueryBuilder;

public class PeriodOffsetQuery {
    
    private HashMap<String, String> variables;
    private final String baseQuery = 
        "query offsets($period: String, $reference: String, $survey: String) { " +
        "allValidationperiods {nodes {" +
            "periodoffset " +
            "validationruleByRule { " +
                "validationformsByRule {nodes {" +
                    "formByFormid {" +
                    "contributorsByFormid(condition: {reference: $reference, period: $period, survey: $survey}) {nodes {" +
                        "reference period survey formid " +
                        "surveyBySurvey {" +
                            "periodicity " +
                        "}}}}}}}}}}";
    private String qlQuery;

    public PeriodOffsetQuery(Map<String, String> variables) {
        this.variables = (variables == null) ? new HashMap<>() : new HashMap<>(variables);
        buildQlQuery();
    }

    private void buildQlQuery() {
        var queryBuilder = new QlQueryBuilder(variables);
        var queryJson = new StringBuilder();
        queryJson.append("{\"query\": \""); 
        queryJson.append(baseQuery); 
        queryJson.append("\",\"variables\": {");
        queryJson.append(queryBuilder.buildVariables());
        queryJson.append("}}");
        qlQuery = queryJson.toString();
    }

    public String getQlQuery() {
        return qlQuery;
    }
}
package uk.gov.ons.collection.entity;

import java.util.HashMap;
import java.util.Map;

public class ViewFormQuery{

    private HashMap<String, String> variables;

    public String buildViewFormQuery(Map<String, String> variables) {
        
    this.variables = (variables == null) ? new HashMap<>() : new HashMap<>(variables);
        StringBuilder viewFormQuery = new StringBuilder();
        viewFormQuery.append("{\"query\": \"query responsecontributorformdefinition($period: String, $reference: String, $survey: String) " +
        "{ allContributors(condition: {reference: $reference, period: $period, survey: $survey}) {" +
            "nodes {formByFormid {formdefinitionsByFormid {nodes {questioncode type derivedformula displaytext displayquestionnumber}}}" +
            "responsesByReferenceAndPeriodAndSurvey {nodes {instance questioncode response}}}}}\"," +
            "\"variables\": {");

        viewFormQuery.append(buildVariables());
        viewFormQuery.append("}}");
        return viewFormQuery.toString();
    }

    private Object buildVariables() {
        return null;
    }
}
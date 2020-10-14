package uk.gov.ons.collection.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class ViewFormQuery{

    private HashMap<String, String> variables;
    private List<String> intVariables = new ArrayList<>(Arrays.asList("first", "last", "formid"));

    public ViewFormQuery(Map<String, String> variables) {
        this.variables = (variables == null) ? new HashMap<>() : new HashMap<>(variables);
    }

    public String buildViewFormQuery() {
        
        StringBuilder viewFormQuery = new StringBuilder();
        viewFormQuery.append("{\"query\": \"query responsecontributorformdefinition($period: String, $reference: String, $survey: String) " +
        "{ allContributors(condition: {reference: $reference, period: $period, survey: $survey}) {" +
            "nodes {formByFormid {formdefinitionsByFormid (orderBy: DISPLAYORDER_ASC){nodes {questioncode type derivedformula displaytext displayquestionnumber displayorder}}}" +
            "responsesByReferenceAndPeriodAndSurvey {nodes {instance questioncode response}}}}}\"," +
            "\"variables\": {");

        viewFormQuery.append(buildVariables());
        viewFormQuery.append("}}");
        return viewFormQuery.toString();
    }

    public String buildVariables() {    
        StringJoiner joiner = new StringJoiner(",");
        variables.forEach((key,value) -> {
            if (intVariables.contains(key)) {
                joiner.add("\"" + key + "\": " + value);
            } else {
                joiner.add("\"" + key + "\": \"" + value + "\"");
            }
        });
        return joiner.toString();
    }
}
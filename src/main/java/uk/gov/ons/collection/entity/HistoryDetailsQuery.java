package uk.gov.ons.collection.entity;

import java.util.*;

public class HistoryDetailsQuery {

    private HashMap<String, String> variables;
    private static final String SURVEY = "survey";
    private static final String PERIOD = "period";
    private static final String REFERENCE = "reference";


    public HistoryDetailsQuery(Map<String, String> variables) {
        this.variables = (variables == null) ? new HashMap<>() : new HashMap<>(variables);
    }

    public HistoryDetailsQuery() {

    }

    public String buildHistoryDetailsQuery(List<String> historyPeriodList ) {

        StringBuilder historyDetailsQuery = new StringBuilder();
        historyDetailsQuery.append("{\"query\": \"query historydetails " +
                "{ allContributors(filter: {").append(historyDetailsQuery.append(buildFilterCondition(historyPeriodList)).append(
                "}) {" +
                "nodes {survey period reference formByFormid {formdefinitionsByFormid {nodes {questioncode type derivedformula displaytext displayquestionnumber displayorder}}}" +
                "responsesByReferenceAndPeriodAndSurvey {nodes {instance questioncode response}}}}}\"," +
                "\"variables\": {"));

        historyDetailsQuery.append("}");
        return historyDetailsQuery.toString();
    }

    public String buildSurveyPeriodicityQuery() {
        StringBuilder surveyPeriodicityQuery = new StringBuilder();
        surveyPeriodicityQuery.append("{\"query\": \"query getperiodicitysurvey($survey: String) " +
                "{ allSurveys(condition: {survey: $survey}) {" +
                "nodes { periodicity }}}\"," +
                "\"variables\": {");
        surveyPeriodicityQuery.append(buildVariableForPeriodicity());
        surveyPeriodicityQuery.append("}}");
        return surveyPeriodicityQuery.toString();

    }



    public String buildVariables() {
        StringJoiner joiner = new StringJoiner(",");
        variables.forEach((key,value) -> {
            joiner.add("\"" + key + "\": \"" + value + "\"");
        });
        return joiner.toString();
    }

    public String buildVariableForPeriodicity() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"" + SURVEY + "\": \"" + variables.get(SURVEY) + "\"");
        return sb.toString();
    }

    public String retrieveCurrentPeriod() {
        return variables.get(PERIOD);
    }

    public String retrieveCurrentReference() {
        return variables.get(REFERENCE);
    }

    public String buildFilterCondition(List<String> historyPeriodList) {
        StringBuilder sbFilter = new StringBuilder();
        sbFilter.append("reference: {equalTo:");
        sbFilter.append("\": \"").append(variables.get(REFERENCE));
        sbFilter.append("\"}, survey: {equalTo:");
        sbFilter.append("\": \"").append(variables.get(SURVEY));
        sbFilter.append("\"}, period: {in: [");

        StringJoiner joiner = new StringJoiner(",");
        for (int i=0; i< historyPeriodList.size(); i++) {
            joiner.add ("\": \"" + historyPeriodList.get(i) + "\"");
        }
        sbFilter.append(joiner.toString());
        sbFilter.append("]}");


        return sbFilter.toString();
    }

}

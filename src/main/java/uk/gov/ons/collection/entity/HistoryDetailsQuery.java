package uk.gov.ons.collection.entity;

import java.util.*;

public class HistoryDetailsQuery {

    private HashMap<String, String> variables;
    private static final String SURVEY = "survey";
    private static final String PERIOD = "period";


    public HistoryDetailsQuery(Map<String, String> variables) {
        this.variables = (variables == null) ? new HashMap<>() : new HashMap<>(variables);
    }

    public HistoryDetailsQuery() {

    }

    public String buildHistoryDetailsQuery() {

        StringBuilder historyDetailsQuery = new StringBuilder();
        historyDetailsQuery.append("{\"query\": \"query historyresponse($period: String, $reference: String, $survey: String) " +
                "{ allContributors(condition: {reference: $reference, period: $period, survey: $survey}) {" +
                "nodes {formByFormid {formdefinitionsByFormid {nodes {questioncode type derivedformula displaytext displayquestionnumber displayorder}}}" +
                "responsesByReferenceAndPeriodAndSurvey {nodes {instance questioncode response}}}}}\"," +
                "\"variables\": {");

        historyDetailsQuery.append(buildVariables());
        historyDetailsQuery.append("}}");
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

}

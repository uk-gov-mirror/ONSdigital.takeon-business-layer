package uk.gov.ons.collection.entity;

import lombok.extern.log4j.Log4j2;

import java.util.*;

@Log4j2
public class HistoryDetailsQuery {

    private HashMap<String, String> variables;
    private static final String SURVEY = "survey";
    private static final String PERIOD = "period";
    private static final String REFERENCE = "reference";


    public HistoryDetailsQuery(Map<String, String> variables) {
        this.variables = (variables == null) ? new HashMap<>() : new HashMap<>(variables);
    }

    public String buildHistoryDetailsQuery(List<String> historyPeriodList) {

        StringBuilder historyDetailsQuery = new StringBuilder();
        historyDetailsQuery.append("{\"query\": \"query historydetails { allContributors(filter: ");
        historyDetailsQuery.append(buildFilterCondition(historyPeriodList));
        historyDetailsQuery.append("}, orderBy:PERIOD_DESC) {");
        historyDetailsQuery.append("nodes { survey period reference formByFormid {formdefinitionsByFormid (orderBy: DISPLAYORDER_ASC)");
        historyDetailsQuery.append("{ nodes { questioncode type derivedformula displaytext displayquestionnumber displayorder}}}");
        historyDetailsQuery.append(" responsesByReferenceAndPeriodAndSurvey {nodes {instance questioncode response}}}}}\"}");
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


    public String buildVariableForPeriodicity() {
        StringBuilder sb = new StringBuilder();
        sb.append("\"" + SURVEY + "\": \"" + variables.get(SURVEY) + "\"");
        return sb.toString();
    }

    public String retrieveCurrentPeriod() throws NullPointerException {
        String period = variables.get(PERIOD);
        if (period == null) {
            throw new NullPointerException("There is a problem and UI is not sending Current Period");
        }
        return period;
    }

    public String retrieveCurrentReference() throws NullPointerException {
        String reference = variables.get(REFERENCE);
        if (reference == null) {
            throw new NullPointerException("There is a problem and UI is not sending Reference");
        }
        return reference;
    }

    public String retrieveSurvey() throws NullPointerException {
        String survey = variables.get(SURVEY);
        if (survey == null) {
            throw new NullPointerException("There is a problem and UI is not sending Survey");
        }
        return survey;
    }

    public String buildFilterCondition(List<String> historyPeriodList) {
        log.info("Reference : "+retrieveCurrentReference());
        log.info("Survey : "+retrieveSurvey());
        StringBuilder sbFilter = new StringBuilder();
        sbFilter.append("{");
        sbFilter.append("reference: {equalTo: ");
        sbFilter.append("\\\"").append(retrieveCurrentReference());
        sbFilter.append("\\\"}, survey: {equalTo: ");
        sbFilter.append("\\\"").append(retrieveSurvey());
        sbFilter.append("\\\"}, period: {in: [");

        StringJoiner joiner = new StringJoiner(",");
        for (String eachPeriod : historyPeriodList) {
            joiner.add("\\\"" + eachPeriod + "\\\"");
        }
        sbFilter.append(joiner.toString());
        sbFilter.append("]}");
        return sbFilter.toString();
    }

}

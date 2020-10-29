package uk.gov.ons.collection.entity;

import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Log4j2
public class SelectiveEditingQuery  {

    private HashMap<String, String> variables;
    private static final String SURVEY = "survey";
    private static final String PERIOD = "period";
    private static final String REFERENCE = "reference";

    public SelectiveEditingQuery(Map<String, String> variables) {
        this.variables = (variables == null) ? new HashMap<>() : new HashMap<>(variables);
    }

    public String buildSelectiveEditingLoadConfigQuery(List<String> historyPeriodList) {

        StringBuilder selectiveEditingQuery = new StringBuilder();
        selectiveEditingQuery.append("{\"query\": \"query loadselectiveeditingconfig { allContributors(filter: ");
        selectiveEditingQuery.append(buildFilterCondition(historyPeriodList));
        selectiveEditingQuery.append("}, orderBy:PERIOD_DESC) {");
        selectiveEditingQuery.append("nodes { survey period reference resultscellnumber domain responsesByReferenceAndPeriodAndSurvey ");
        selectiveEditingQuery.append("{ nodes { questioncode period response }}}} ");
        selectiveEditingQuery.append("allSelectiveeditingconfigs(filter: ");
        selectiveEditingQuery.append(buildFilerConditionForSurveyAndPeriod());
        selectiveEditingQuery.append("{ nodes { survey period domain questioncode threshold estimate }} ");
        selectiveEditingQuery.append("allCelldetails(filter: ");
        selectiveEditingQuery.append(buildFilerConditionForSurveyAndPeriod());
        selectiveEditingQuery.append("{ nodes { survey period cellnumber designweight }}}\"}");
        return selectiveEditingQuery.toString();
    }



    public String buildFilterCondition(List<String> historyPeriodList) {
        log.info("Reference : " + retrieveCurrentReference());
        log.info("Survey : " + retrieveSurvey());
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

    public  String buildFilerConditionForSurveyAndPeriod() {
        StringBuilder sbFilter = new StringBuilder();
        sbFilter.append("{");
        sbFilter.append("survey: {equalTo: ");
        sbFilter.append("\\\"").append(retrieveSurvey());
        sbFilter.append("\\\"}, period: {equalTo: ");
        sbFilter.append("\\\"").append(retrieveCurrentPeriod());
        sbFilter.append("\\\"}}) ");
        return sbFilter.toString();

    }

    public String retrieveCurrentReference() throws NullPointerException {
        String reference = variables.get(REFERENCE);
        if (reference == null) {
            throw new NullPointerException("There is a problem and UI is not sending Reference");
        }
        return reference;
    }

    public String retrieveCurrentPeriod() throws NullPointerException {
        String period = variables.get(PERIOD);
        if (period == null) {
            throw new NullPointerException("There is a problem and UI is not sending Current Period");
        }
        return period;
    }

    public String retrieveSurvey() throws NullPointerException {
        String survey = variables.get(SURVEY);
        if (survey == null) {
            throw new NullPointerException("There is a problem and UI is not sending Survey");
        }
        return survey;
    }
}

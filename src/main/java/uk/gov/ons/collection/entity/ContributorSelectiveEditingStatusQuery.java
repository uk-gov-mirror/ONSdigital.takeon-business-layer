package uk.gov.ons.collection.entity;

import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;

@Log4j2
public class ContributorSelectiveEditingStatusQuery {

    private HashMap<String, String> variables;
    private static final String SURVEY = "survey";
    private static final String PERIOD = "period";
    private static final String REFERENCE = "reference";

    public ContributorSelectiveEditingStatusQuery(Map<String, String> variables) {
        this.variables = (variables == null) ? new HashMap<>() : new HashMap<>(variables);
    }

    public String buildContributorSelectiveEditingStatusQuery() {

        StringBuilder contributorStatusQuery = new StringBuilder();
        contributorStatusQuery.append("{\"query\": \"query dateadjustmentconfig { allContributors(filter: ");
        contributorStatusQuery.append(buildFilterCondition());
        contributorStatusQuery.append("}) {");
        contributorStatusQuery.append("nodes { survey period reference formid status ");
        contributorStatusQuery.append("contributorselectiveeditingByReferenceAndPeriodAndSurvey ");
        contributorStatusQuery.append("{reference  period survey flag }}}}\"}");

        return contributorStatusQuery.toString();
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

    public String buildFilterCondition() {
        log.info("Reference : " + retrieveCurrentReference());
        log.info("Survey : " + retrieveSurvey());
        StringBuilder sbFilter = new StringBuilder();
        sbFilter.append("{");
        sbFilter.append("reference: {equalTo: ");
        sbFilter.append("\\\"").append(retrieveCurrentReference());
        sbFilter.append("\\\"}, survey: {equalTo: ");
        sbFilter.append("\\\"").append(retrieveSurvey());
        sbFilter.append("\\\"}, period: {equalTo: ");
        sbFilter.append("\\\"").append(retrieveCurrentPeriod()).append("\\\"");
        sbFilter.append("}");
        return sbFilter.toString();
    }

}

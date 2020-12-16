package uk.gov.ons.collection.entity;

import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
public class DateAdjustmentQuery {

    private HashMap<String, String> variables;
    private static final String SURVEY = "survey";
    private static final String PERIOD = "period";
    private static final String REFERENCE = "reference";

    public DateAdjustmentQuery(Map<String, String> variables) {
        this.variables = (variables == null) ? new HashMap<>() : new HashMap<>(variables);
    }

    public String buildDateAdjustmentConfigQuery() {

        StringBuilder selectiveEditingQuery = new StringBuilder();
        selectiveEditingQuery.append("{\"query\": \"query dateadjustmentconfig { allContributors(filter: ");
        selectiveEditingQuery.append(buildFilterCondition());
        selectiveEditingQuery.append("}) {");
        selectiveEditingQuery.append("nodes { survey period reference formid status frozensic resultscellnumber domain ");
        selectiveEditingQuery.append("formByFormid {survey formid formdefinitionsByFormid(filter: {dateadjustment: {equalTo: true}}) {");
        selectiveEditingQuery.append(" nodes { questioncode dateadjustment }}}");
        selectiveEditingQuery.append(" responsesByReferenceAndPeriodAndSurvey ");
        selectiveEditingQuery.append("{ nodes { reference period survey questioncode instance response }}}} ");
        selectiveEditingQuery.append("allDateadjustmentweightconfigs(filter: ");
        selectiveEditingQuery.append(buildFilerConditionForSurveyAndPeriod());
        selectiveEditingQuery.append("{ nodes { survey period tradingdate domain weight periodstart periodend }} ");
        selectiveEditingQuery.append("allContributordateadjustmentconfigs(filter: ");
        selectiveEditingQuery.append(buildFilterCondition());
        selectiveEditingQuery.append("}) ");
        selectiveEditingQuery.append("{ nodes { reference period survey returnedstartdate returnedenddate longperiodparameter shortperiodparameter averageweekly settomidpoint settoequalweighted usecalendardays }}}\"}");
        return selectiveEditingQuery.toString();
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

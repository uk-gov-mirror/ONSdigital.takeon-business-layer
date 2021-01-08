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

        StringBuilder dateAdjustmentQuery = new StringBuilder();
        dateAdjustmentQuery.append("{\"query\": \"query dateadjustmentconfig { allContributors(filter: ");
        dateAdjustmentQuery.append(buildFilterCondition());
        dateAdjustmentQuery.append("}) {");
        dateAdjustmentQuery.append("nodes { survey period reference formid status frozensic resultscellnumber domain ");
        dateAdjustmentQuery.append("formByFormid {survey formid formdefinitionsByFormid {");
        dateAdjustmentQuery.append(" nodes { questioncode dateadjustment }}");
        dateAdjustmentQuery.append(" dateadjustmentreturndateconfigsByFormid {");
        dateAdjustmentQuery.append(" nodes { formid questioncode returndatetype }}");
        dateAdjustmentQuery.append("}");
        dateAdjustmentQuery.append(" responsesByReferenceAndPeriodAndSurvey ");
        dateAdjustmentQuery.append("{ nodes { reference period survey questioncode instance response }}}} ");
        dateAdjustmentQuery.append("allDateadjustmentweightconfigs(filter: ");
        dateAdjustmentQuery.append(buildFilerConditionForSurveyAndPeriod());
        dateAdjustmentQuery.append("{ nodes { survey period tradingdate domain weight periodstart periodend }} ");
        dateAdjustmentQuery.append("allContributordateadjustmentconfigs(filter: ");
        dateAdjustmentQuery.append(buildFilterCondition());
        dateAdjustmentQuery.append("}) ");
        dateAdjustmentQuery.append("{ nodes { reference period survey longperiodparameter ");
        dateAdjustmentQuery.append("shortperiodparameter averageweekly settomidpoint settoequalweighted usecalendardays }}}\"}");
        return dateAdjustmentQuery.toString();
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

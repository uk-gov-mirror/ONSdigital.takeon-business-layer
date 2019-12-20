package uk.gov.ons.collection.entity;

import lombok.extern.log4j.Log4j2;
import uk.gov.ons.collection.service.ServiceInterface;

public class ContributorQuery {

    private ServiceInterface service;

    private final String reference;
    private final String period;
    private final String survey;

    private String updateQuery = "mutation updateStatus($period: String!, $reference: String!, $survey: String!, $status: String!) " +
        "{updateContributorByReferenceAndPeriodAndSurvey" +
        "(input: {reference: $reference, period: $period, survey: $survey, contributorPatch: {status: $status}}) " +
        "{contributor { reference period survey status }}}";

    private String loadSingleQuery = "query contrib($period: String!, $reference: String!, $survey: String!) " +
        "{contributorByReferenceAndPeriodAndSurvey(reference: $reference, period:$period, survey:$survey) " +
        "{reference formid period survey surveyBySurvey {id periodicity description}}}";

    public ContributorQuery(String reference, String period, String survey, ServiceInterface service) {
        this.reference = reference;
        this.period = period;
        this.survey = survey;
        this.service = service;
    }

    private String buildUpdateQuery(String statusText) {
        return "{\"query\": \"" + this.updateQuery + "\",\"variables\": {" +
            "\"reference\": \"" + reference + "\"," +
            "\"period\": \"" + period + "\"," +
            "\"survey\": \"" + survey + "\"," +
            "\"status\": \"" + statusText + "\"}}";
    }

    private String buildLoadSingleQuery() {
        return "{\"query\": \"" + this.loadSingleQuery + "\",\"variables\": {" +
            "\"reference\": \"" + reference + "\"," +
            "\"period\": \"" + period + "\"," +
            "\"survey\": \"" + survey + "\"}}";
    }

    public String updateStatus(String statusText) {
        return service.runQuery(this.buildUpdateQuery(statusText));
    }

    public String load() {
        return service.runQuery(buildLoadSingleQuery());
    }
}
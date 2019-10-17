package uk.gov.ons.collection.entity;

public class ContributorStatus {
    
    private final String reference;
    private final String period;
    private final String survey;
    private final String statusText;
    
    private String qlUpdate = "mutation updateStatus($period: String!, $reference: String!, $survey: String!, $status: String!) {" +
        "updateContributorByReferenceAndPeriodAndSurvey(input: {reference: $reference, period: $period, survey: $survey, contributorPatch: {status: $status}}) {" +
         "contributor { reference period survey status }}}";

    public ContributorStatus(String reference, String period, String survey, String statusText) {
        this.reference = reference;
        this.period = period;
        this.survey = survey;
        this.statusText = statusText;
    }

    public String buildUpdateQuery() {
        var updateQuery = new StringBuilder();
        updateQuery.append("{\"query\": \"" + qlUpdate);    
        updateQuery.append("\",\"variables\": {");
        updateQuery.append("\"reference\": \"" + reference + "\",\"period\": \"" + period + "\",\"survey\": \"" + survey + "\",\"status\": \"" + statusText + "\"}}");
        return updateQuery.toString();
    }
}
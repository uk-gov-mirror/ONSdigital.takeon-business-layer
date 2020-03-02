package uk.gov.ons.collection.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class QlQueryBuilder {

    private HashMap<String, String> variables;
    private List<String> intVariables = new ArrayList<>(Arrays.asList("first", "last", "formid"));

    // Instantiate the variables and coalesce a null into an empty variable object
    public QlQueryBuilder(Map<String, String> variables) {
        this.variables = (variables == null) ? new HashMap<>() : new HashMap<>(variables);
    }

    public String buildContributorSearchQuery() {
        StringBuilder query = new StringBuilder();
        query.append("{\"query\": \"query contributorSearch($startCursor: Cursor, $first: Int, $endCursor: Cursor, $last: Int," +
                "$period: String, $reference: String, $survey: String, $formid: Int, $status: String) " +
                "{ allContributors (after: $startCursor, first: $first, before: $endCursor, last: $last," +
                "condition: {reference: $reference, period: $period, survey: $survey, status: $status, formid: $formid}) " +
                "{ nodes { reference, period, survey, formid, status, receiptdate, lockedby, lockeddate, " +
                "lastupdatedby, lastupdateddate, formtype, enterprisename, referencename, referenceaddress, referencepostcode, " +
                "checkletter, frozensicoutdated, rusicoutdated, frozensic, rusic, frozenemployees, employees, " +
                "frozenemployment, employment, frozenfteemployment, fteemployment, frozenturnover, turnover, " +
                "enterprisereference, wowenterprisereference, cellnumber, currency, vatreference, payereference, " +
                "companyregistrationnumber, numberlivelocalunits, numberlivevat, numberlivepaye, legalstatus, " +
                "reportingunitmarker, region, birthdate, tradingstyle, contact, telephone, fax, selectiontype, " +
                "inclusionexclusion, createdby, createddate, surveyBySurvey { description } } " +
                "pageInfo { hasNextPage hasPreviousPage startCursor endCursor } totalCount " +
                "}}\"," +
                "\"variables\": {");
        query.append(buildVariables());
        query.append("}}");
        return query.toString();
    }

    // Build up a graphQL syntax condition. If invalid (or empty) conditions are provided returns a blank string
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

    public String buildContributorQuery() {
        StringBuilder query = new StringBuilder();
        query.append("{\"query\": \"query contrib($period: String, $reference: String, $survey: String) { " +
                "allContributors(condition: {reference: $reference, period: $period, survey: $survey}) { " +
                "nodes { reference period survey formid surveyBySurvey { periodicity } }}}\"," +
                "\"variables\": {");
        query.append(buildVariables());
        query.append("}}");
        return query.toString();
    }

    public String buildValidationOutputQuery() {
        StringBuilder validationOutputQuery = new StringBuilder();
        validationOutputQuery.append("{\"query\": \"query allValidationOutputs($reference: String, $period: String, $survey: String) " +
                "{ allValidationoutputs(condition: {reference: $reference, period: $period, survey: $survey}) {" +
                "nodes {formula triggered lastupdatedby lastupdateddate instance validationoutputid overridden " +
                "validationformByValidationid {severity validationid rule primaryquestion " +
                "validationruleByRule {name validationmessage}}}}}\"," +
                "\"variables\": {");
        validationOutputQuery.append(buildVariables());
        validationOutputQuery.append("}}");
        return validationOutputQuery.toString();
    }
}
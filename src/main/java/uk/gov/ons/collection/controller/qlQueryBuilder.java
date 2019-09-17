package uk.gov.ons.collection.controller;

import java.util.Map;

public class qlQueryBuilder {

    public String buildContributorSearchQuery(Map<String, String> searchParameters) {
        StringBuilder builtQuery = new StringBuilder();
        builtQuery.append("{\"query\": \"query contributorSearchBy { allContributors ");
        builtQuery.append(buildCondition(searchParameters));
        builtQuery.append("{ nodes { reference, period, survey, formid, status, receiptdate, lockedby, lockeddate, " + 
                            "lastupdatedby, lastupdateddate, formtype, enterprisename," + 
                            "checkletter, frozensicoutdated, rusicoutdated, frozensic, rusic, frozenemployees, employees, " +
                            "frozenemployment, employment, frozenfteemployment, fteemployment, frozenturnover, turnover, " + 
                            "enterprisereference, wowenterprisereference, cellnumber, currency, vatreference, payereference, " +
                            "companyregistrationnumber, numberlivelocalunits, numberlivevat, numberlivepaye, legalstatus, " +
                            "reportingunitmarker, region, birthdate, tradingstyle, contact, telephone, fax, selectiontype, " +
                            "inclusionexclusion, createdby, createddate}}}\" }");
        return builtQuery.toString();
    }

    // Build up a graphQL syntax condition. If invalid (or empty) conditions are provided returns a blank string
    public String buildCondition(Map<String, String> parameters) {    
        if (parameters == null || parameters.isEmpty()) {
            return "";
        }        
        StringBuilder conditions = new StringBuilder();
        conditions.append("(condition: { ");
        parameters.forEach((key,value) -> conditions.append(key + ": \\\"" + value + "\\\" "));
        conditions.append("})");
        return conditions.toString();
    }
}

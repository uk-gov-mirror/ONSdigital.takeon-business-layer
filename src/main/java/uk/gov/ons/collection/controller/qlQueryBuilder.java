package uk.gov.ons.collection.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class qlQueryBuilder { 

    private HashMap<String, String> variables;
    private List<String> intVariables = new ArrayList<>( Arrays.asList("first", "last", "formid"));
    
    // Instantiate the variables and coalesce a null into an empty variable object
    public qlQueryBuilder(Map<String, String> variables) {
        this.variables = (variables == null) ? new HashMap<>() : new HashMap<>(variables);
    }

    public String buildContributorSearchQuery() {        
        StringBuilder query = new StringBuilder();
        query.append("{\"query\": \"query contributorSearch($startCursor: Cursor, $first: Int, $endCursor: Cursor, $last: Int, $period: String, $reference: String, $survey: String, $formid: Int, $status: String) " +
                "{ allContributors (after: $startCursor, first: $first, before: $endCursor, last: $last, condition: {reference: $reference, period: $period, survey: $survey, status: $status, formid: $formid}) " +
                "{ nodes { reference, period, survey, formid, status, receiptdate, lockedby, lockeddate, " + 
                            "lastupdatedby, lastupdateddate, formtype, enterprisename, referencename, referenceaddress, referencepostcode, " + 
                            "checkletter, frozensicoutdated, rusicoutdated, frozensic, rusic, frozenemployees, employees, " +
                            "frozenemployment, employment, frozenfteemployment, fteemployment, frozenturnover, turnover, " + 
                            "enterprisereference, wowenterprisereference, cellnumber, currency, vatreference, payereference, " +
                            "companyregistrationnumber, numberlivelocalunits, numberlivevat, numberlivepaye, legalstatus, " +
                            "reportingunitmarker, region, birthdate, tradingstyle, contact, telephone, fax, selectiontype, " +
                            "inclusionexclusion, createdby, createddate} " + 
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
                joiner.add("\"" + key + "\": " + value );
            }
            else {
                joiner.add("\"" + key + "\": \"" + value + "\"");
            }
        });
        return joiner.toString();
    }

    public String buildExportDBQuery() {
        String queryPrefix = "{\"query\": \"query  queryExport ";
        String dbExportQuery = "{" +
                " allSurveys {" +
                "   nodes {" +
                "     survey" +
                "     description" +
                "     periodicity" +
                "     createdby" +
                "     createddate" +
                "     lastupdatedby" +
                "     lastupdateddate" +
                "     formsBySurvey {" +
                " nodes {" +
                "   formid" +
                "   survey" +
                "   description" +
                "   periodstart" +
                "   periodend" +
                "   createdby" +
                "   createddate" +
                "   lastupdatedby" +
                "   lastupdateddate" +
                "   formdefinitionsByFormid {" +
                "     nodes {" +
                "  formid" +
                "  questioncode" +
                "  displayquestionnumber" +
                "  displaytext" +
                "  displayorder" +
                "  type" +
                "  derivedformula" +
                "  createdby" +
                "  createddate" +
                "  lastupdatedby" +
                "  lastupdateddate" +
                "}" +
                "}" +
                "    validationformsByFormid {" +
                "     nodes {" +
                "  validationid" +
                "  formid" +
                "  rule" +
                "  questioncode" +
                "  precalculationformula" +
                "  severity" +
                "  createdby" +
                "  createddate" +
                "  lastupdatedby" +
                "  lastupdateddate" +
                "  validationparametersByValidationid {" +
                "  nodes {" +
                "    validationid" +
                "    attributename" +
                "    attributevalue" +
                "    parameter" +
                "    value" +
                "    createdby" +
                "    createddate" +
                "    lastupdatedby" +
                "    lastupdateddate" +
                "  }" +
                "}" +
                "  validationruleByRule {" +
                "    rule" +
                "    name" +
                "    baseformula" +
                "    createdby" +
                "    createddate" +
                "    lastupdatedby" +
                "    lastupdateddate" +
                "    validationperiodsByRule {" +
                "      nodes {" +
                "  rule" +
                "  periodoffset" +
                "  createdby" +
                "  createddate" +
                "  lastupdatedby" +
                "  lastupdateddate" +
                " }" +
                "    }" +
                "}" +
                " }" +
                " }" +
                " }" +
                " }" +
                "  questionsBySurvey {" +
                "    nodes {" +
                "      survey" +
                "      questioncode" +
                "      createdby" +
                "      createddate" +
                "      lastupdatedby" +
                "      lastupdateddate" +
                "    }" +
                "  }" +
                "  contributorsBySurvey {" +
                "    nodes {" +
                "      reference" +
                "      period" +
                "      survey" +
                "      formid" +
                "      status" +
                "      receiptdate" +
                "      lockedby" +
                "      lockeddate" +
                "      formtype" +
                "      checkletter" +
                "      frozensicoutdated" +
                "      rusicoutdated" +
                "      frozensic" +
                "      rusic" +
                "      frozenemployees" +
                "      employees" +
                "      frozenemployment" +
                "      employment" +
                "      frozenfteemployment" +
                "      fteemployment" +
                "      frozenturnover" +
                "      turnover" +
                "      enterprisereference" +
                "      wowenterprisereference" +
                "      cellnumber" +
                "      currency" +
                "      vatreference" +
                "      payereference" +
                "      companyregistrationnumber" +
                "      numberlivelocalunits" +
                "      numberlivevat" +
                "      numberlivepaye" +
                "      legalstatus" +
                "      reportingunitmarker" +
                "      region" +
                "      birthdate" +
                "      enterprisename" +
                "      referencename" +
                "      referenceaddress" +
                "      referencepostcode" +
                "      tradingstyle" +
                "      contact" +
                "      telephone" +
                "      fax" +
                "      selectiontype" +
                "      inclusionexclusion" +
                "      createdby" +
                "      createddate" +
                "      lastupdatedby" +
                "      lastupdateddate" +
                "      responsesByReferenceAndPeriodAndSurvey {" +
                "        nodes {" +
                "          reference" +
                "          period" +
                "          survey" +
                "          questioncode" +
                "          instance" +
                "          response" +
                "          createdby" +
                "          createddate" +
                "          lastupdatedby" +
                "          lastupdateddate" +
                "        }" +
                "      }" +
                "    }" +
                "  }" +
                "  validationoutputsBySurvey {" +
                "    nodes {" +
                "      validationoutputid" +
                "      reference" +
                "      period" +
                "      survey" +
                "      validationid" +
                "      instance" +
                "      primaryvalue" +
                "      formula" +
                "      createdby" +
                "      createddate" +
                "      lastupdatedby" +
                "      lastupdateddate" +
                "    }" +
                "  }" +
                "                  }" +
                "                }" +
                "              } ";

        String querySuffix = "\"}";
        StringBuilder builtQuery = new StringBuilder();
        builtQuery.append(queryPrefix).append(dbExportQuery).append(querySuffix);
        return builtQuery.toString();
    }

    public String buildOffsetPeriodQuery() {
        return "{\"query\": \"query periodoffset { allValidationperiods { nodes { periodoffset }}}\"}";
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

    public String buildContribResponseFormDetailsQuery() {
        StringBuilder contributorDetailsQuery = new StringBuilder();
        contributorDetailsQuery.append("{\"query\": \"query contributordetails($period: String, $reference: String, $survey: String) " +
            "{ allContributors(condition: {reference: $reference, period: $period, survey: $survey}) {" +
                "nodes {reference period survey surveyBySurvey {periodicity} " +
                "formid status receiptdate lockedby lockeddate checkletter frozensicoutdated rusicoutdated frozensic " +
                "rusic frozenemployees employees frozenemployment employment frozenfteemployment fteemployment frozenturnover " +
                "turnover enterprisereference wowenterprisereference cellnumber currency vatreference payereference " +
                "companyregistrationnumber numberlivelocalunits numberlivevat numberlivepaye legalstatus " +
                "reportingunitmarker region birthdate tradingstyle contact telephone fax selectiontype inclusionexclusion " +
                "createdby createddate lastupdatedby lastupdateddate " +
                "formByFormid {survey formdefinitionsByFormid {nodes {questioncode type}}}" +
                "responsesByReferenceAndPeriodAndSurvey {nodes {reference period survey instance questioncode response }}}}}\"," +
                "\"variables\": {" );
        contributorDetailsQuery.append(buildVariables());
        contributorDetailsQuery.append("}}");
        return contributorDetailsQuery.toString();    
    }

}

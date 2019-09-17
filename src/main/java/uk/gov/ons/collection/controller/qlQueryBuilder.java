package uk.gov.ons.collection.controller;

import java.util.Map;

public class qlQueryBuilder {

    public String buildContributorSearchQuery(Map<String, String> searchParameters) {
        StringBuilder builtQuery = new StringBuilder();
        builtQuery.append("{\"query\": \"query contributorSearchBy { allContributors ");
        builtQuery.append(buildCondition(searchParameters));
        builtQuery.append("{ nodes { reference, period, survey, formid, status, receiptdate, lockedby, lockeddate, " + 
            "lastupdatedby, lastupdateddate, formtype, enterprisename, referencename, referenceaddress, referencepostcode, " + 
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

    public String exportDB(){

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
//        return dbTestExport;
    }
}

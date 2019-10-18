package uk.gov.ons.collection.entity;

public class dbExport { 

    private final String query = 
        "query  queryExport {" +
                " allSurveys {nodes {survey description periodicity createdby createddate lastupdatedby lastupdateddate " +
                    "formsBySurvey {nodes { formid survey description periodstart periodend createdby createddate " +
                                           "lastupdatedby lastupdateddate " +
                        "formdefinitionsByFormid {nodes {" +
                            "formid questioncode displayquestionnumber displaytext displayorder " +
                            "type derivedformula createdby createddate lastupdatedby lastupdateddate}}" +
                        "validationformsByFormid {nodes {" +
                            "validationid formid rule questioncode precalculationformula severity createdby " +
                            "createddate lastupdatedby lastupdateddate " +
                            "validationparametersByValidationid {nodes {" +
                                "validationid attributename attributevalue parameter value createdby createddate lastupdatedby lastupdateddate}}" +
                            "validationruleByRule {" +
                                "rule name baseformula createdby createddate lastupdatedby lastupdateddate " +
                                "validationperiodsByRule {nodes {rule periodoffset createdby createddate lastupdatedby lastupdateddate}}}}}}}" +
                    "questionsBySurvey { nodes {survey questioncode createdby createddate lastupdatedby lastupdateddate}}" +
                    "contributorsBySurvey { nodes {" +
                        "reference  period survey  formid  status  receiptdate  lockedby  lockeddate  formtype  checkletter  frozensicoutdated " +
                        "rusicoutdated frozensic rusic frozenemployees employees frozenemployment employment frozenfteemployment" +
                        "fteemployment frozenturnover turnover enterprisereference wowenterprisereference cellnumber currency vatreference" +
                        "payereference companyregistrationnumber numberlivelocalunits numberlivevat numberlivepaye legalstatus " +
                        "reportingunitmarker region birthdate enterprisename referencename referenceaddress referencepostcode tradingstyle " +
                        "contact telephone fax selectiontype inclusionexclusion createdby createddate lastupdatedby lastupdateddate " +
                        "responsesByReferenceAndPeriodAndSurvey {nodes {" +
                            "reference period survey questioncode instance response createdby createddate lastupdatedby lastupdateddate}}}}" +
                    "validationoutputsBySurvey {nodes {" +
                        "validationoutputid reference period survey validationid instance triggered formula " +
                        "createdby createddate lastupdatedby lastupdateddate}}}}}";

    public dbExport() {
        super();
    }

    public String buildQuery() {
        return "{\"query\": \"" + query + "\"}";
    }

}

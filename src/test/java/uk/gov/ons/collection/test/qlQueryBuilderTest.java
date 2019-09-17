package uk.gov.ons.collection.test;

import uk.gov.ons.collection.controller.qlQueryBuilder;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class qlQueryBuilderTest {

    @Test
    void buildQuery_nullParameters_unfilteredQuery() {
        String expectedQuery = "{\"query\": \"query contributorSearchBy { allContributors " + 
            "{ nodes { reference, period, survey, formid, status, receiptdate, lockedby, lockeddate, " + 
            "lastupdatedby, lastupdateddate, formtype, enterprisename, name, address, postcode, " + 
            "checkletter, frozensicoutdated, rusicoutdated, frozensic, rusic, frozenemployees, employees, " +
            "frozenemployment, employment, frozenfteemployment, fteemployment, frozenturnover, turnover, " + 
            "enterprisereference, wowenterprisereference, cellnumber, currency, vatreference, payereference, " +
            "companyregistrationnumber, numberlivelocalunits, numberlivevat, numberlivepaye, legalstatus, " +
            "reportingunitmarker, region, birthdate, tradingstyle, contact, telephone, fax, selectiontype, " +
            "inclusionexclusion, createdby, createddate}}}\" }";
        assertEquals(expectedQuery, new qlQueryBuilder().buildContributorSearchQuery(null));
    }

    @Test
    void buildQuery_emptyParameters_unfilteredQuery(){
        String expectedQuery = "{\"query\": \"query contributorSearchBy { allContributors { nodes { reference, period, survey, formid, status, receiptdate, lockedby, lockeddate, " + 
            "lastupdatedby, lastupdateddate, formtype, enterprisename, name, address, postcode, " + 
            "checkletter, frozensicoutdated, rusicoutdated, frozensic, rusic, frozenemployees, employees, " +
            "frozenemployment, employment, frozenfteemployment, fteemployment, frozenturnover, turnover, " + 
            "enterprisereference, wowenterprisereference, cellnumber, currency, vatreference, payereference, " +
            "companyregistrationnumber, numberlivelocalunits, numberlivevat, numberlivepaye, legalstatus, " +
            "reportingunitmarker, region, birthdate, tradingstyle, contact, telephone, fax, selectiontype, " +
            "inclusionexclusion, createdby, createddate}}}\" }";
        Map<String,String> emptyParameters = new HashMap<>();
        assertEquals(expectedQuery, new qlQueryBuilder().buildContributorSearchQuery(emptyParameters));
    }

    @Test
    void buildQuery_singleParameter_filteredQuery(){
        String expectedQuery= "{\"query\": \"query contributorSearchBy { " +
            "allContributors (condition: { reference: \\\"4990012\\\" }){ " +
            "nodes { reference, period, survey, formid, status, receiptdate, lockedby, lockeddate, " + 
            "lastupdatedby, lastupdateddate, formtype, enterprisename, name, address, postcode, " + 
            "checkletter, frozensicoutdated, rusicoutdated, frozensic, rusic, frozenemployees, employees, " +
            "frozenemployment, employment, frozenfteemployment, fteemployment, frozenturnover, turnover, " + 
            "enterprisereference, wowenterprisereference, cellnumber, currency, vatreference, payereference, " +
            "companyregistrationnumber, numberlivelocalunits, numberlivevat, numberlivepaye, legalstatus, " +
            "reportingunitmarker, region, birthdate, tradingstyle, contact, telephone, fax, selectiontype, " +
            "inclusionexclusion, createdby, createddate}}}\" }";
        Map<String,String> singleParameter = new HashMap<>();
        singleParameter.put("reference", "4990012");
        assertEquals(expectedQuery, new qlQueryBuilder().buildContributorSearchQuery(singleParameter));
    }

    @Test
    void buildQuery_twoParameters_filteredQuery(){
        String builtQuery= "{\"query\": \"query contributorSearchBy { " +
            "allContributors (condition: { reference: \\\"4990012\\\" period: \\\"201903\\\" }){ " +
            "nodes { reference, period, survey, formid, status, receiptdate, lockedby, lockeddate, " + 
            "lastupdatedby, lastupdateddate, formtype, enterprisename, name, address, postcode, " + 
            "checkletter, frozensicoutdated, rusicoutdated, frozensic, rusic, frozenemployees, employees, " +
            "frozenemployment, employment, frozenfteemployment, fteemployment, frozenturnover, turnover, " + 
            "enterprisereference, wowenterprisereference, cellnumber, currency, vatreference, payereference, " +
            "companyregistrationnumber, numberlivelocalunits, numberlivevat, numberlivepaye, legalstatus, " +
            "reportingunitmarker, region, birthdate, tradingstyle, contact, telephone, fax, selectiontype, " +
            "inclusionexclusion, createdby, createddate}}}\" }";
        Map<String,String> twoParameters = new HashMap<>();
        twoParameters.put("reference", "4990012");
        twoParameters.put("period", "201903");
        assertEquals(builtQuery, new qlQueryBuilder().buildContributorSearchQuery(twoParameters));
    }    

    @Test
    void buildCondition_null_blankString() {
        assertEquals("", new qlQueryBuilder().buildCondition(null));
    }

    @Test
    void buildCondition_emptyParameters_blankString() {
        Map<String,String> emptyParameter = new HashMap<>();
        assertEquals("", new qlQueryBuilder().buildCondition(emptyParameter));
    }

    @Test
    void buildCondition_singleParameter_singleCondition() {
        String expectedCondition = "(condition: { reference: \\\"4990012\\\" })";
        Map<String,String> singleParameter = new HashMap<>();
        singleParameter.put("reference", "4990012");
        assertEquals(expectedCondition, new qlQueryBuilder().buildCondition(singleParameter));
    }

    @Test
    void buildCondition_twoParameters_twoCondition() {
        String expectedCondition = "(condition: { reference: \\\"4990012\\\" survey: \\\"011\\\" })";
        Map<String,String> twoParameters = new HashMap<>();
        twoParameters.put("reference", "4990012");
        twoParameters.put("survey", "011");
        assertEquals(expectedCondition, new qlQueryBuilder().buildCondition(twoParameters));
    }



}


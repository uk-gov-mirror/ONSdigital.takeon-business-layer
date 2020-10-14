package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.FullDataExport;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FullDataExportTest {

    @Test
    void paddedMonth_1Digit_paddedOutput() {
        var expectedQuery = "{\"query\": \"query dbExport {" +
                " allSurveys {nodes {survey description periodicity createdby createddate lastupdatedby lastupdateddate " +
                    "formsBySurvey {nodes { formid survey description periodstart periodend createdby createddate " +
                                           "lastupdatedby lastupdateddate " +
                        "formdefinitionsByFormid {nodes {" +
                            "formid questioncode displayquestionnumber displaytext displayorder " +
                            "type derivedformula createdby createddate lastupdatedby lastupdateddate}}" +
                        "validationformsByFormid {nodes {" +
                            "validationid formid rule primaryquestion defaultvalue severity createdby createddate lastupdatedby lastupdateddate " +
                            "validationparametersByValidationid {nodes {" +
                                "validationid attributename attributevalue parameter value createdby createddate lastupdatedby lastupdateddate}}" +
                            "validationruleByRule {" +
                                "rule name baseformula createdby createddate lastupdatedby lastupdateddate " +
                                "validationperiodsByRule {nodes {rule periodoffset createdby createddate lastupdatedby lastupdateddate}}}}}}}" +
                    "questionsBySurvey { nodes {survey questioncode createdby createddate lastupdatedby lastupdateddate}}" +
                    "contributorsBySurvey { nodes {" +
                        "reference  period survey  formid  status  receiptdate  lockedby  lockeddate  formtype  checkletter  frozensicoutdated " +
                        "rusicoutdated frozensic rusic frozenemployees employees frozenemployment employment frozenfteemployment " +
                        "fteemployment frozenturnover turnover enterprisereference wowenterprisereference cellnumber currency vatreference " +
                        "payereference companyregistrationnumber numberlivelocalunits numberlivevat numberlivepaye legalstatus " +
                        "reportingunitmarker region birthdate enterprisename referencename referenceaddress referencepostcode tradingstyle " +
                        "contact telephone fax selectiontype inclusionexclusion createdby createddate lastupdatedby lastupdateddate " +
                        "responsesByReferenceAndPeriodAndSurvey {nodes {" +
                            "reference period survey questioncode instance response createdby createddate lastupdatedby lastupdateddate}}}}" +
                    "validationoutputsBySurvey {nodes {" +
                        "validationoutputid reference period survey validationid instance triggered formula " +
                        "createdby createddate lastupdatedby lastupdateddate}}}}}\"}";
        var query = new FullDataExport().buildQuery();
        assertEquals(expectedQuery,query);
    }

    @Test
    void verify_input_snapshot_data_periods() {

    }

    @Test
    void verify_invalid_input_snapshot_data_throwsAnException() {

    }

    @Test
    void verify_snapshot_data_output() {

    }



    @Test
    void verify_input_snapshot_data() {

        String snapshotInput = "{\n" +
                "  \"snapshot_id\": \"14e0fb27-d450-44d4-8452-9f6996b00e27\",\n" +
                "  \"surveyperiods\": [\n" +
                "    {\n" +
                "      \"survey\": \"023\",\n" +
                "      \"period\": \"201904\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"survey\": \"023\",\n" +
                "      \"period\": \"201903\"\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        try {
            FullDataExport dataExportObj = new FullDataExport(snapshotInput);
            List<String> listPeriods = dataExportObj.retrievePeriodFromSnapshotInput();
            System.out.println("Survey: " + dataExportObj.getSurvey());
            System.out.println("Period List: " + listPeriods.toString());
            System.out.println("Period Survey Filter Condition: " + dataExportObj.buildSurveyAndPeriodsFilterCondition(listPeriods));
            System.out.println("Survey Filter Condition: " + dataExportObj.buildSurveyFilterCondition());
            String queryStr = dataExportObj.buildSnapshotSurveyPeriodQuery(listPeriods);
            System.out.println("Query String :"+queryStr);
        } catch(Exception e) {

        }

    }
}


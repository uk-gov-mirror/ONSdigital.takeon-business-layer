package uk.gov.ons.collection.test;


import org.json.JSONException;
import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.FullDataExport;
import uk.gov.ons.collection.exception.InvalidJsonException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class FullDataExportTest {


    String snapshotMultipleSurveys = "{" +
            "  \"snapshot_id\": \"14e0fb27-d450-44d4-8452-9f6996b00e27\"," +
            "  \"surveyperiods\": [" +
            "    {" +
            "      \"survey\": \"066\"," +
            "      \"period\": \"201903\"" +
            "    }," +
            "    {" +
            "      \"survey\": \"066\"," +
            "      \"period\": \"201906\"" +
            "    }," +
            "    {" +
            "      \"survey\": \"023\"," +
            "      \"period\": \"201903\"" +
            "    }," +
            "    {" +
            "      \"survey\": \"023\"," +
            "      \"period\": \"201904\"" +
            "    }" +
            "  ]" +
            "}";

    String emptySnapshotResponse = "{\n" +
            "  \"data\": {\n" +
            "    \"allSurveys\": {\n" +
            "      \"nodes\": []\n" +
            "    }\n" +
            "  }\n" +
            "}";

    String emptySnapshotPeriodsInput = "{\n" +
            "  \"snapshot_id\": \"14e0fb27-d450-44d4-8452-9f6996b00e27\",\n" +
            "  \"surveyperiods\": [\n" +
            "  ]\n" +
            "}";



    @Test
    void verify_input_snapshot_data_periods_and_graphql_filters_query_output() {
        String expectedQuery = "{\"query\": \"query dbExport {  allSurveys(filter: {survey: {in: [\\\"066\\\",\\\"023\\\"]}}, orderBy: SURVEY_ASC){nodes {survey description periodicity createdby createddate lastupdatedby lastupdateddate idbrformtypesBySurvey { nodes { survey formid  formtype periodstart periodend }}formsBySurvey {nodes { formid survey description periodstart periodend createdby createddate lastupdatedby lastupdateddate formdefinitionsByFormid {nodes {formid questioncode displayquestionnumber displaytext displayorder type derivedformula createdby createddate lastupdatedby lastupdateddate}}validationformsByFormid {nodes {validationid formid rule primaryquestion defaultvalue severity createdby createddate lastupdatedby lastupdateddate validationparametersByValidationid {nodes {validationid attributename attributevalue parameter value createdby createddate lastupdatedby lastupdateddate}}validationruleByRule {rule name baseformula createdby createddate lastupdatedby lastupdateddate validationperiodsByRule {nodes {rule periodoffset createdby createddate lastupdatedby lastupdateddate}}}}}}}questionsBySurvey(filter: {survey: {in: [\\\"066\\\",\\\"023\\\"]}}, orderBy: SURVEY_ASC){ nodes {survey questioncode createdby createddate lastupdatedby lastupdateddate}}contributordateadjustmentsBySurvey(filter: {survey: {in: [\\\"066\\\",\\\"023\\\"]}}, orderBy: SURVEY_ASC){ nodes {survey period  reference dateadjustmenterrorflag dateadjustmenterrorflag actualdaysreturnedperiod sumtradingweightsoverreturnedperiod sumtradingweightsoveractualreturnedperiod createdby createddate lastupdatedby lastupdateddate}}contributorsBySurvey(filter: {or: [{and: [{survey: {equalTo: \\\"066\\\"}}, {period: {in: [\\\"201903\\\",\\\"201906\\\"]}}]},{and: [{survey: {equalTo: \\\"023\\\"}}, {period: {in: [\\\"201903\\\",\\\"201904\\\"]}}]}]}, orderBy: PERIOD_ASC){ nodes {reference  period survey  formid  status  receiptdate  lockedby  lockeddate  formtype  checkletter  frozensicoutdated rusicoutdated frozensic rusic frozenemployees employees frozenemployment employment frozenfteemployment fteemployment frozenturnover turnover enterprisereference wowenterprisereference cellnumber currency vatreference payereference companyregistrationnumber numberlivelocalunits numberlivevat numberlivepaye legalstatus reportingunitmarker region birthdate enterprisename referencename referenceaddress referencepostcode tradingstyle contact telephone fax selectiontype inclusionexclusion createdby createddate lastupdatedby lastupdateddate responsesByReferenceAndPeriodAndSurvey {nodes {reference period survey questioncode instance response adjustedresponse averageweeklyadjustedresponse createdby createddate lastupdatedby lastupdateddate}}}}validationoutputsBySurvey(filter: {or: [{and: [{survey: {equalTo: \\\"066\\\"}}, {period: {in: [\\\"201903\\\",\\\"201906\\\"]}}]},{and: [{survey: {equalTo: \\\"023\\\"}}, {period: {in: [\\\"201903\\\",\\\"201904\\\"]}}]}]}, orderBy: PERIOD_ASC){nodes {validationoutputid reference period survey validationid instance triggered formula createdby createddate lastupdatedby lastupdateddate}}}}}\"}";
        String expectedSurveyPeriods = "{066=[201903, 201906], 023=[201903, 201904]}";
        String expectedSurveyFilter = "(filter: {survey: {in: [\\\"066\\\",\\\"023\\\"]}}, orderBy: SURVEY_ASC)";
        String expectedSurveyPeriodsFilter = "(filter: {or: [{and: [{survey: {equalTo: \\\"066\\\"}}, {period: {in: [\\\"201903\\\",\\\"201906\\\"]}}]},{and: [{survey: {equalTo: \\\"023\\\"}}, {period: {in: [\\\"201903\\\",\\\"201904\\\"]}}]}]}, orderBy: PERIOD_ASC)";
        try {
            FullDataExport dataExport = new FullDataExport(snapshotMultipleSurveys);
            Set<String> surveyList = dataExport.getUniqueSurveyList();
            Map<String, List<String>> snapshotMap = dataExport.retrieveSurveyAndPeriodListFromSnapshotInput(surveyList);
            assertEquals(expectedSurveyPeriods, snapshotMap.toString());
            String surveyPeriodFilter = dataExport.buildMultipleSurveyAndPeriodFilterCondition(snapshotMap);
            assertEquals(expectedSurveyPeriodsFilter, surveyPeriodFilter);

            String surveyFilter = dataExport.buildMultipleSurveysFilterCondition(surveyList);
            assertEquals(expectedSurveyFilter, surveyFilter);
            String actualQuery = dataExport.buildMultipleSurveyPeriodSnapshotQuery(surveyList, snapshotMap);
            assertEquals(expectedQuery, actualQuery);
        } catch(Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void verify_invalid_json_input_snapshot_data_throwsAnException() {
        String snapshotInvalidInput = "{" +
                "  \"snapshot_id\": \"14e0fb27-d450-44d4-8452-9f6996b00e27\"," +
                "  \"surveyperiods\": [" +
                "    {" +
                "      \"survey\": \"023\"," +
                "      \"period\": \"201904\"" +
                "    }," +
                "    {" +
                "      \"survey\": \"023\"," +
                "      \"period\": \"201903\"" +
                "    }" +
                "  ]" +
                "";
        assertThrows(InvalidJsonException.class, () -> new FullDataExport(snapshotInvalidInput));
    }

    @Test
    void verify_invalid_snapshot_data_throwsAnException() {
        String snapshotInvalidInput = "{" +
                "  \"snapshot_id\": \"14e0fb27-d450-44d4-8452-9f6996b00e27\"," +
                "  \"surveyperiod\": [" +
                "    {" +
                "      \"survey\": \"023\"," +
                "      \"period\": \"201904\"" +
                "    }," +
                "    {" +
                "      \"survey\": \"023\"," +
                "      \"period\": \"201903\"" +
                "    }," +
                "  ]" +
                "";
        assertThrows(InvalidJsonException.class, () -> new FullDataExport(snapshotInvalidInput));
    }

    @Test
    void verify__snapshot_data_emptySurveyPeriods_throwsAnException() {
        String snapshotInput = "{" +
                "  \"snapshot_id\": \"14e0fb27-d450-44d4-8452-9f6996b00e27\"," +
                "  \"surveyperiods\": [" +
                "  ]" +
                "}";
        Set<String> uniqueSurveyList = new HashSet<String>();
        assertThrows(InvalidJsonException.class, () -> new FullDataExport(snapshotInput).retrieveSurveyAndPeriodListFromSnapshotInput(uniqueSurveyList));

    }


    @Test
    void verify_empty_snapshot_data_throwsAnException() {

        assertThrows(JSONException.class, () -> new FullDataExport().verifyEmptySnapshot(emptySnapshotResponse));
    }

    @Test
    void verify_empty_snapshot_period_input_data_throwsAnException() {
        try {
            FullDataExport dataExport = new FullDataExport(emptySnapshotPeriodsInput);
            Set<String> uniqueSurveyList = dataExport.getUniqueSurveyList();
             assertThrows(InvalidJsonException.class, () -> dataExport.retrieveSurveyAndPeriodListFromSnapshotInput(uniqueSurveyList));
        } catch(Exception e) {
            System.out.println(e.getMessage());
            assertTrue(false);
        }
    }
}


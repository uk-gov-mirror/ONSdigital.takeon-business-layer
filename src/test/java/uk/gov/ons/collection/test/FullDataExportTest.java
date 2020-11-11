package uk.gov.ons.collection.test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.FullDataExport;
import uk.gov.ons.collection.exception.InvalidJsonException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class FullDataExportTest {

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

    String snapshotMultipleSurveys = "{\n" +
            "  \"snapshot_id\": \"14e0fb27-d450-44d4-8452-9f6996b00e27\",\n" +
            "  \"surveyperiods\": [\n" +
            "    {\n" +
            "      \"survey\": \"066\",\n" +
            "      \"period\": \"202003\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"survey\": \"023\",\n" +
            "      \"period\": \"202006\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"survey\": \"023\",\n" +
            "      \"period\": \"202007\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"survey\": \"073\",\n" +
            "      \"period\": \"202006\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"survey\": \"076\",\n" +
            "      \"period\": \"202003\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"survey\": \"076\",\n" +
            "      \"period\": \"202005\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";


    @Test
    void verify_input_snapshot_Survey_periods() {
        try {
            FullDataExport dataExport = new FullDataExport(snapshotMultipleSurveys);
            Map<String, List<String>> snapshotMap = dataExport.retrieveSurveyAndPeriodListFromSnapshotInput();
            snapshotMap.forEach((k, v) -> {
                System.out.println("Survey:"+k+ " Periods:"+v.toString());
                String query = dataExport.buildSnapshotSurveyPeriodQuery(k, v);
                System.out.println("Output : "+query);
            });


            JSONObject jsonSurveySnapshotInput = new JSONObject(snapshotMultipleSurveys);
            JSONArray snapshotArray = jsonSurveySnapshotInput.getJSONArray("surveyperiods");
            Set<String> uniqueSurveyList = new HashSet<String>();
            if (snapshotArray != null && snapshotArray.length() > 0) {
                for (int i = 0; i < snapshotArray.length(); i++) {
                    JSONObject surveyPeriodObj = snapshotArray.getJSONObject(i);
                    uniqueSurveyList.add(surveyPeriodObj.getString("survey"));
                }
                Map<String, List<String>> map = new HashMap<>();
                for(String survey : uniqueSurveyList){
                    List<String> periodList = new ArrayList<String>();
                    for (int i = 0; i < snapshotArray.length(); i++) {
                        JSONObject surveyPeriodObj = snapshotArray.getJSONObject(i);
                        if(survey.equals(surveyPeriodObj.getString("survey"))) {
                            periodList.add(surveyPeriodObj.getString("period"));
                        }
                    }
                    map.put(survey, periodList);
                }
                System.out.println(map.toString());
                //Traversing through Map
                map.forEach((k, v) -> {
                    System.out.println("Survey:"+k+ " Periods:"+v.toString());
                });
            }
        } catch(Exception e) {
            assertTrue(false);
        }
    }



    @Test
    void verify_input_snapshot_data_periods() {
        try {
            /*FullDataExport dataExport = new FullDataExport(snapshotInput);
            List<String> periodList = dataExport.retrievePeriodFromSnapshotInput();
            String actualPeriods = periodList.toString();
            String expectedPeriods = "[201904, 201903]";
            assertEquals(expectedPeriods, actualPeriods);
            System.out.println(periodList);*/
            FullDataExport dataExport = new FullDataExport(snapshotMultipleSurveys);
            Map<String, List<String>> snapshotMap = dataExport.retrieveSurveyAndPeriodListFromSnapshotInput();
            snapshotMap.forEach((k, v) -> {
                System.out.println("Survey:"+k+ " Periods:"+v.toString());
            });

        } catch(Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void verify_invalid_json_input_snapshot_data_throwsAnException() {
        String snapshotInvalidInput = "{\n" +
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
                "";
        assertThrows(InvalidJsonException.class, () -> new FullDataExport(snapshotInvalidInput));
    }

    @Test
    void verify_invalid_snapshot_data_throwsAnException() {
        String snapshotInvalidInput = "{\n" +
                "  \"snapshot_id\": \"14e0fb27-d450-44d4-8452-9f6996b00e27\",\n" +
                "  \"surveyperiod\": [\n" +
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
        assertThrows(JSONException.class, () -> new FullDataExport(snapshotInvalidInput).retrieveSurveyAndPeriodListFromSnapshotInput());
    }

    @Test
    void verify__snapshot_data_emptySurveyPeriods_throwsAnException() {
        String snapshotInput = "{\n" +
                "  \"snapshot_id\": \"14e0fb27-d450-44d4-8452-9f6996b00e27\",\n" +
                "  \"surveyperiods\": [\n" +
                "  ]\n" +
                "}";
        //assertThrows(InvalidJsonException.class, () -> new FullDataExport(snapshotInput).retrieveSurveyAndPeriodListFromSnapshotInput());

    }

    /*@Test
    void verify_survey_period_query_output() {
        try {
            FullDataExport dataExportObj = new FullDataExport(snapshotInput);
            List<String> listPeriods = dataExportObj.retrievePeriodFromSnapshotInput();
            String queryStr = dataExportObj.buildSnapshotSurveyPeriodQuery(listPeriods);
            String expectedDataExportQuery = "{\"query\": \"query dbExport {  allSurveys(filter: {survey: {equalTo: \\\"023\\\"}}){nodes {survey description periodicity createdby createddate lastupdatedby lastupdateddate formsBySurvey {nodes { formid survey description periodstart periodend createdby createddate lastupdatedby lastupdateddate formdefinitionsByFormid {nodes {formid questioncode displayquestionnumber displaytext displayorder type derivedformula createdby createddate lastupdatedby lastupdateddate}}validationformsByFormid {nodes {validationid formid rule primaryquestion defaultvalue severity createdby createddate lastupdatedby lastupdateddate validationparametersByValidationid {nodes {validationid attributename attributevalue parameter value createdby createddate lastupdatedby lastupdateddate}}validationruleByRule {rule name baseformula createdby createddate lastupdatedby lastupdateddate validationperiodsByRule {nodes {rule periodoffset createdby createddate lastupdatedby lastupdateddate}}}}}}}questionsBySurvey(filter: {survey: {equalTo: \\\"023\\\"}}){ nodes {survey questioncode createdby createddate lastupdatedby lastupdateddate}}contributorsBySurvey(filter: {survey: {equalTo: \\\"023\\\"}, period: {in: [\\\"201904\\\",\\\"201903\\\"]}}, orderBy: PERIOD_ASC){ nodes {reference  period survey  formid  status  receiptdate  lockedby  lockeddate  formtype  checkletter  frozensicoutdated rusicoutdated frozensic rusic frozenemployees employees frozenemployment employment frozenfteemployment fteemployment frozenturnover turnover enterprisereference wowenterprisereference cellnumber currency vatreference payereference companyregistrationnumber numberlivelocalunits numberlivevat numberlivepaye legalstatus reportingunitmarker region birthdate enterprisename referencename referenceaddress referencepostcode tradingstyle contact telephone fax selectiontype inclusionexclusion createdby createddate lastupdatedby lastupdateddate responsesByReferenceAndPeriodAndSurvey {nodes {reference period survey questioncode instance response createdby createddate lastupdatedby lastupdateddate}}}}validationoutputsBySurvey(filter: {survey: {equalTo: \\\"023\\\"}, period: {in: [\\\"201904\\\",\\\"201903\\\"]}}, orderBy: PERIOD_ASC){nodes {validationoutputid reference period survey validationid instance triggered formula createdby createddate lastupdatedby lastupdateddate}}}}}\"}";
            System.out.println("Query String :"+queryStr);
            assertEquals(expectedDataExportQuery, queryStr);
        } catch(Exception e) {
            assertTrue(false);
        }

    }*/
}


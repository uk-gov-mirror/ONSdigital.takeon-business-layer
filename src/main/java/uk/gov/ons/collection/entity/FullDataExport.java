package uk.gov.ons.collection.entity;

import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uk.gov.ons.collection.exception.InvalidJsonException;



import java.util.*;

@Log4j2
public class FullDataExport {

    private JSONObject jsonSurveySnapshotInput;

    public FullDataExport(String inputJsonString) throws InvalidJsonException {
        try {
            jsonSurveySnapshotInput = new  JSONObject(inputJsonString);
        } catch (Exception e) {
            throw new InvalidJsonException("The Survey snapshot input is invalid. Please verify " + e.getMessage());
        }
    }

    public FullDataExport() {

    }



    public Map<String, List<String>> retrieveSurveyAndPeriodListFromSnapshotInput() throws InvalidJsonException {

        JSONArray snapshotArray = jsonSurveySnapshotInput.getJSONArray("surveyperiods");
        Set<String> uniqueSurveyList = new HashSet<String>();
        Map<String, List<String>> snapshotMap = new HashMap<>();
        if (snapshotArray != null && snapshotArray.length() > 0) {
            for (int i = 0; i < snapshotArray.length(); i++) {
                JSONObject surveyPeriodObj = snapshotArray.getJSONObject(i);
                uniqueSurveyList.add(surveyPeriodObj.getString("survey"));
            }
            for (String survey : uniqueSurveyList) {
                List<String> periodList = new ArrayList<String>();
                for (int i = 0; i < snapshotArray.length(); i++) {
                    JSONObject surveyPeriodObj = snapshotArray.getJSONObject(i);
                    if (survey.equals(surveyPeriodObj.getString("survey"))) {
                        periodList.add(surveyPeriodObj.getString("period"));
                    }
                }
                snapshotMap.put(survey, periodList);
            }
            System.out.println(snapshotMap.toString());
        } else {
            throw new InvalidJsonException("There are no snapshot survey periods. Please verify");
        }
        return snapshotMap;
    }

    public String buildSnapshotSurveyPeriodQuery(String surveyStr, List<String> periodList) {
        StringBuilder snapshotQuery = new StringBuilder();
        snapshotQuery.append("{\"query\": \"");
        snapshotQuery.append("query dbExport {  allSurveys");
        snapshotQuery.append(buildSurveyFilterCondition(surveyStr));
        snapshotQuery.append("{nodes {survey description periodicity createdby createddate lastupdatedby lastupdateddate ");
        snapshotQuery.append("formsBySurvey {nodes { formid survey description periodstart periodend createdby createddate ");
        snapshotQuery.append("lastupdatedby lastupdateddate ");
        snapshotQuery.append("formdefinitionsByFormid {nodes {");
        snapshotQuery.append("formid questioncode displayquestionnumber displaytext displayorder ");
        snapshotQuery.append("type derivedformula createdby createddate lastupdatedby lastupdateddate}}");
        snapshotQuery.append("validationformsByFormid {nodes {");
        snapshotQuery.append("validationid formid rule primaryquestion defaultvalue severity createdby createddate lastupdatedby lastupdateddate ");
        snapshotQuery.append("validationparametersByValidationid {nodes {");
        snapshotQuery.append("validationid attributename attributevalue parameter value createdby createddate lastupdatedby lastupdateddate}}");
        snapshotQuery.append("validationruleByRule {");
        snapshotQuery.append("rule name baseformula createdby createddate lastupdatedby lastupdateddate ");
        snapshotQuery.append("validationperiodsByRule {nodes {rule periodoffset createdby createddate lastupdatedby lastupdateddate}}}}}}}");
        snapshotQuery.append("questionsBySurvey");
        snapshotQuery.append(buildSurveyFilterCondition(surveyStr));
        snapshotQuery.append("{ nodes {survey questioncode createdby createddate lastupdatedby lastupdateddate}}");
        snapshotQuery.append("contributorsBySurvey");
        snapshotQuery.append(buildSurveyAndPeriodsFilterCondition(surveyStr, periodList));
        snapshotQuery.append("{ nodes {");
        snapshotQuery.append("reference  period survey  formid  status  receiptdate  lockedby  lockeddate  formtype  checkletter  frozensicoutdated ");
        snapshotQuery.append("rusicoutdated frozensic rusic frozenemployees employees frozenemployment employment frozenfteemployment ");
        snapshotQuery.append("fteemployment frozenturnover turnover enterprisereference wowenterprisereference cellnumber currency vatreference ");
        snapshotQuery.append("payereference companyregistrationnumber numberlivelocalunits numberlivevat numberlivepaye legalstatus ");
        snapshotQuery.append("reportingunitmarker region birthdate enterprisename referencename referenceaddress referencepostcode tradingstyle ");
        snapshotQuery.append("contact telephone fax selectiontype inclusionexclusion createdby createddate lastupdatedby lastupdateddate ");
        snapshotQuery.append("responsesByReferenceAndPeriodAndSurvey {nodes {");
        snapshotQuery.append("reference period survey questioncode instance response createdby createddate lastupdatedby lastupdateddate}}}}");
        snapshotQuery.append("validationoutputsBySurvey");
        snapshotQuery.append(buildSurveyAndPeriodsFilterCondition(surveyStr, periodList));
        snapshotQuery.append("{nodes {");
        snapshotQuery.append("validationoutputid reference period survey validationid instance triggered formula ");
        snapshotQuery.append("createdby createddate lastupdatedby lastupdateddate}}}}}");
        snapshotQuery.append("\"}");

        return snapshotQuery.toString();
    }


    public String buildSurveyAndPeriodsFilterCondition(String surveyStr, List<String> periodList) {

        StringBuilder sbFilter = new StringBuilder();
        sbFilter.append("(filter: {");
        sbFilter.append("survey: {equalTo: ");
        sbFilter.append("\\\"").append(surveyStr);
        sbFilter.append("\\\"}, period: {in: [");

        StringJoiner joiner = new StringJoiner(",");
        for (String eachPeriod : periodList) {
            joiner.add("\\\"" + eachPeriod + "\\\"");
        }
        sbFilter.append(joiner.toString());
        sbFilter.append("]}}, orderBy: PERIOD_ASC)");
        return sbFilter.toString();

    }

    public String buildSurveyFilterCondition(String surveyStr) {
        StringBuilder sbFilter = new StringBuilder();
        sbFilter.append("(filter: {");
        sbFilter.append("survey: {equalTo: ");
        sbFilter.append("\\\"").append(surveyStr);
        sbFilter.append("\\\"}})");
        return sbFilter.toString();

    }

    public String mergeAllSurveyDatasets(List<String> allSurveyData) throws JSONException {

        JSONObject masterSurveyObject = null;
        JSONArray masterSurveyArray = null;
        if (allSurveyData.size() > 0) {
            masterSurveyObject = new JSONObject(allSurveyData.get(0));
            masterSurveyArray = masterSurveyObject.getJSONObject("data").getJSONObject("allSurveys").getJSONArray("nodes");
        }
        for (int i = 1; i < allSurveyData.size(); i++) {
            if (masterSurveyObject.getJSONObject("data").getJSONObject("allSurveys").getJSONArray("nodes").length() > 0) {
                JSONObject anotherSurveyObject = new JSONObject(allSurveyData.get(i));
                if (anotherSurveyObject.getJSONObject("data").getJSONObject("allSurveys").getJSONArray("nodes").length() > 0) {
                    JSONObject secondSurveyObj = anotherSurveyObject.getJSONObject("data").getJSONObject("allSurveys").getJSONArray("nodes").getJSONObject(0);
                    masterSurveyArray.put(secondSurveyObj);
                }
            }
        }

        if(masterSurveyObject == null) {
            throw new JSONException("There are no snapshots exists for a given survey periods. Please verify");
        }

        return masterSurveyObject.toString();


    }

}

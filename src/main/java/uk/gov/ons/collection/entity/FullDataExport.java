package uk.gov.ons.collection.entity;

import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uk.gov.ons.collection.exception.InvalidJsonException;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;


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


    public Set<String> getUniqueSurveyList() throws JSONException  {
        JSONArray snapshotArray = jsonSurveySnapshotInput.getJSONArray("surveyperiods");
        Set<String> uniqueSurveyList = new HashSet<String>();
        for (int i = 0; i < snapshotArray.length(); i++) {
            JSONObject surveyPeriodObj = snapshotArray.getJSONObject(i);
            uniqueSurveyList.add(surveyPeriodObj.getString("survey"));
        }
        return uniqueSurveyList;
    }

    public Map<String, List<String>> retrieveSurveyAndPeriodListFromSnapshotInput(Set<String> uniqueSurveyList) throws InvalidJsonException {

        JSONArray snapshotArray = jsonSurveySnapshotInput.getJSONArray("surveyperiods");
        Map<String, List<String>> snapshotMap = new HashMap<>();
        if (snapshotArray != null && snapshotArray.length() > 0) {
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
        } else {
            throw new InvalidJsonException("There are no snapshot survey periods. Please verify");
        }
        return snapshotMap;
    }



    public String buildMultipleSurveyPeriodSnapshotQuery(Set<String> surveyList, Map<String, List<String>> surveyPeriodsMap) {
        StringBuilder snapshotQuery = new StringBuilder();
        snapshotQuery.append("{\"query\": \"");
        snapshotQuery.append("query dbExport {  allSurveys");
        snapshotQuery.append(buildMultipleSurveysFilterCondition(surveyList));
        snapshotQuery.append("{nodes {survey description periodicity createdby createddate lastupdatedby lastupdateddate ");
        snapshotQuery.append("idbrformtypesBySurvey { ");
        snapshotQuery.append("nodes { survey formid  formtype periodstart periodend }}");
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
        snapshotQuery.append(buildMultipleSurveysFilterCondition(surveyList));
        snapshotQuery.append("{ nodes {survey questioncode createdby createddate lastupdatedby lastupdateddate}}");
        snapshotQuery.append("contributordateadjustmentsBySurvey");
        snapshotQuery.append(buildMultipleSurveysFilterCondition(surveyList));
        snapshotQuery.append("{ nodes {survey period  reference dateadjustmenterrorflag dateadjustmenterrorflag actualdaysreturnedperiod ");
        snapshotQuery.append("sumtradingweightsoverreturnedperiod sumtradingweightsoveractualreturnedperiod createdby createddate lastupdatedby lastupdateddate}}");
        snapshotQuery.append("contributorsBySurvey");
        snapshotQuery.append(buildMultipleSurveyAndPeriodFilterCondition(surveyPeriodsMap));
        snapshotQuery.append("{ nodes {");
        snapshotQuery.append("reference  period survey  formid  status  receiptdate  lockedby  lockeddate  formtype  checkletter  frozensicoutdated ");
        snapshotQuery.append("rusicoutdated frozensic rusic frozenemployees employees frozenemployment employment frozenfteemployment ");
        snapshotQuery.append("fteemployment frozenturnover turnover enterprisereference wowenterprisereference cellnumber currency vatreference ");
        snapshotQuery.append("payereference companyregistrationnumber numberlivelocalunits numberlivevat numberlivepaye legalstatus ");
        snapshotQuery.append("reportingunitmarker region birthdate enterprisename referencename referenceaddress referencepostcode tradingstyle ");
        snapshotQuery.append("contact telephone fax selectiontype inclusionexclusion createdby createddate lastupdatedby lastupdateddate ");
        snapshotQuery.append("responsesByReferenceAndPeriodAndSurvey {nodes {");
        snapshotQuery.append("reference period survey questioncode instance response adjustedresponse averageweeklyadjustedresponse createdby createddate lastupdatedby lastupdateddate}}}}");
        snapshotQuery.append("validationoutputsBySurvey");
        snapshotQuery.append(buildMultipleSurveyAndPeriodFilterCondition(surveyPeriodsMap));
        snapshotQuery.append("{nodes {");
        snapshotQuery.append("validationoutputid reference period survey validationid instance triggered formula ");
        snapshotQuery.append("createdby createddate lastupdatedby lastupdateddate}}}}}");
        snapshotQuery.append("\"}");

        return snapshotQuery.toString();
    }




    public String buildMultipleSurveysFilterCondition(Set<String> surveyList) {
        StringBuilder sbFilter = new StringBuilder();
        sbFilter.append("(filter: {survey: {in: [");
        StringJoiner joiner = new StringJoiner(",");
        for (String eachSurvey : surveyList) {
            joiner.add("\\\"" + eachSurvey + "\\\"");
        }
        sbFilter.append(joiner.toString());
        sbFilter.append("]}}, orderBy: SURVEY_ASC)");
        return sbFilter.toString();
    }

    public String buildMultipleSurveyAndPeriodFilterCondition(Map<String, List<String>> snapshotMap) {

        StringBuilder sbFilter = new StringBuilder();
        sbFilter.append("(filter: {or: [");
        StringJoiner joiner = new StringJoiner(",");
        snapshotMap.forEach((survey, periodList) -> {
            StringBuilder eachFilter = new StringBuilder();
            eachFilter.append("{and: [{survey: {equalTo: ");
            eachFilter.append("\\\"").append(survey);
            eachFilter.append("\\\"");
            eachFilter.append("}}, {period: {in: [");
            StringJoiner periodJoiner = new StringJoiner(",");
            for (String eachPeriod : periodList) {
                periodJoiner.add("\\\"" + eachPeriod + "\\\"");
            }
            eachFilter.append(periodJoiner.toString());
            eachFilter.append("]}}]}");
            joiner.add(eachFilter.toString());
        });
        sbFilter.append(joiner.toString());
        sbFilter.append("]}, orderBy: PERIOD_ASC)");
        return sbFilter.toString();
    }

    public void verifyEmptySnapshot(String response) throws JSONException {
        JSONObject finalSnapshotObject = new JSONObject(response);
        JSONArray masterSurveySnapshotArray = finalSnapshotObject.getJSONObject("data")
                .getJSONObject("allSurveys").getJSONArray("nodes");
        if (masterSurveySnapshotArray.length() == 0) {
            throw new JSONException("There is no snapshot available for a given survey period combinations");
        }
    }

    public JSONObject getJsonSurveySnapshotInput() {
        return jsonSurveySnapshotInput;
    }

    public void setJsonSurveySnapshotInput(JSONObject jsonSurveySnapshotInput) {
        this.jsonSurveySnapshotInput = jsonSurveySnapshotInput;
    }



}

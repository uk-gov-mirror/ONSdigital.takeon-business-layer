package uk.gov.ons.collection.entity;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import uk.gov.ons.collection.exception.InvalidJsonException;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

@Log4j2
public class FullDataExport {

    private JSONObject jsonSurveySnapshotInput;
    private String survey;

    public FullDataExport(String inputJsonString) throws InvalidJsonException {
        try {
            jsonSurveySnapshotInput = new  JSONObject(inputJsonString);
        } catch (Exception e) {
            throw new InvalidJsonException("The Survey snapshot input is invalid. Please verify "+e.getMessage());
        }
    }


    public List<String> retrievePeriodFromSnapshotInput() throws InvalidJsonException {
        List<String> listPeriods = new ArrayList<String>();
        var snapshotArray = jsonSurveySnapshotInput.getJSONArray("surveyperiods");
        if (snapshotArray != null && snapshotArray.length() > 0) {
            for (int i = 0; i < snapshotArray.length(); i++) {
                JSONObject surveyPeriodObj = snapshotArray.getJSONObject(i);
                this.survey = surveyPeriodObj.getString("survey");
                listPeriods.add(surveyPeriodObj.getString("period"));
            }
        } else {
            throw new InvalidJsonException("There are no snapshot survey periods. Please verify");
        }

        return listPeriods;
    }

    public String buildSnapshotSurveyPeriodQuery(List<String> periodList) {
        StringBuilder snapshotQuery = new StringBuilder();
        snapshotQuery.append("{\"query\": \"");
        snapshotQuery.append("query dbExport {  allSurveys");
        snapshotQuery.append(buildSurveyFilterCondition());
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
        snapshotQuery.append(buildSurveyFilterCondition());
        snapshotQuery.append("{ nodes {survey questioncode createdby createddate lastupdatedby lastupdateddate}}");
        snapshotQuery.append("contributorsBySurvey");
        snapshotQuery.append(buildSurveyAndPeriodsFilterCondition(periodList));
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
        snapshotQuery.append(buildSurveyAndPeriodsFilterCondition(periodList));
        snapshotQuery.append("{nodes {");
        snapshotQuery.append("validationoutputid reference period survey validationid instance triggered formula ");
        snapshotQuery.append("createdby createddate lastupdatedby lastupdateddate}}}}}");
        snapshotQuery.append("\"}");

        return snapshotQuery.toString();
    }

    public String getSurvey() {
        return survey;
    }

    public String buildSurveyAndPeriodsFilterCondition(List<String> periodList) {

        log.info("Survey : " + getSurvey());
        log.info("PeriodList : " + periodList.toString());
        StringBuilder sbFilter = new StringBuilder();
        sbFilter.append("(filter: {");
        sbFilter.append("survey: {equalTo: ");
        sbFilter.append("\\\"").append(this.survey);
        sbFilter.append("\\\"}, period: {in: [");

        StringJoiner joiner = new StringJoiner(",");
        for (String eachPeriod : periodList) {
            joiner.add("\\\"" + eachPeriod + "\\\"");
        }
        sbFilter.append(joiner.toString());
        sbFilter.append("]}}, orderBy: PERIOD_ASC)");
        return sbFilter.toString();

    }

    public String buildSurveyFilterCondition() {
        log.info("Survey : " + getSurvey());
        StringBuilder sbFilter = new StringBuilder();
        sbFilter.append("(filter: {");
        sbFilter.append("survey: {equalTo: ");
        sbFilter.append("\\\"").append(this.survey);
        sbFilter.append("\\\"}})");
        return sbFilter.toString();

    }

}

package uk.gov.ons.collection.utilities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.gov.ons.collection.exception.InvalidJsonException;
import uk.gov.ons.collection.service.GraphQlService;

@Log4j2
public class SelectionFileQuery {
    private GraphQlService qlService;
    private HashMap<String, String> variables;
    private List<String> intVariables = new ArrayList<>(Arrays.asList("first", "last", "formid"));
    private JSONArray contributorValuesArray;
    private JSONObject contributorObject;
    private final Timestamp time = new Timestamp(new Date().getTime());



    public SelectionFileQuery(Map<String, String> variables) {
        this.variables = (variables == null) ? new HashMap<>() : new HashMap<>(variables);
    }

    public SelectionFileQuery(String jsonString, GraphQlService qlGraphService) throws InvalidJsonException {
        qlService = qlGraphService;
        try {
            contributorObject = new JSONObject(jsonString);
            contributorValuesArray = contributorObject.getJSONArray("attributes");

        } catch (JSONException err) {
            log.error("Problem in parsing SelectionFile {} " + err.getMessage());
            throw new InvalidJsonException("Given string could not be converted/processed: " + jsonString, err);
        }
    }

    public String buildVariables() {
        StringJoiner joiner = new StringJoiner(",");
        variables.forEach((key,value) -> {
            if (intVariables.contains(key)) {
                joiner.add("\"" + key + "\": " + value);
            } else {
                joiner.add("\"" + key + "\": \"" + value + "\"");
            }
        });
        return joiner.toString();
    }
    
    public String buildCheckIdbrFormidQuery() {
        StringBuilder checkIdbrFormidQuery = new StringBuilder();
        checkIdbrFormidQuery.append("{\"query\": \"query checkidbrformid($formtype: String, $survey: String) " +
                "{ allIdbrformtypes (condition: {formtype: $formtype, survey: $survey}) {" +
                "nodes { formid survey formtype periodstart periodend }}}\"," +
                "\"variables\": {");
        checkIdbrFormidQuery.append(buildVariables());
        checkIdbrFormidQuery.append("}}");

        return checkIdbrFormidQuery.toString();
    }

    public String buildSaveSelectionFileQuery() throws InvalidJsonException {
        var jsonQlResponse = new StringBuilder();
        jsonQlResponse.append("{\"query\" : \"mutation loadContributors {loadIdbrForm(input: {arg0: ");
        jsonQlResponse.append("[" + getSelectionLoadData() + "]");
        jsonQlResponse.append("}){clientMutationId}}\"}");
        return jsonQlResponse.toString();
    }

    // Loop through the given validation output array json and convert it into a graphQL compatable format
    private String getSelectionLoadData() throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");
        for (int i = 0; i < contributorValuesArray.length(); i++) {
            joiner.add("{" + extractContributorRow(i) + "}");
        }
        return joiner.toString();
    }

    public String getTime() {
        return time.toString();
    }

    // Convert a row for the given index and provide it in graphQL desired format
    private String extractContributorRow(int index) throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");
        var outputRow = contributorValuesArray.getJSONObject(index);
        String reference = "";
        String periodStr = "";
        String survey = "";

        try {

            var periodSurvey = contributorObject;
            reference = outputRow.getString("ruref");
            periodStr = periodSurvey.getString("period");
            survey = periodSurvey.getString("survey");
            joiner.add("period: \\\"" + periodStr + "\\\"");
            joiner.add("survey: \\\"" + survey + "\\\"");
            joiner.add("reference: \\\"" + reference + "\\\"");
            Map<String, String> vars = new HashMap<String, String>();
            vars.put("formtype", outputRow.getString("formtype"));
            vars.put("survey", periodSurvey.getString("survey"));
            String formIdQuery = new SelectionFileQuery(vars).buildCheckIdbrFormidQuery();
            log.debug("CheckIDBR Form ID Query : " + formIdQuery);
            SelectionFileResponse formResponse = new SelectionFileResponse(qlService.qlSearch(formIdQuery));
            int period = Integer.parseInt(periodSurvey.getString("period"));
            Integer formId = formResponse.parseFormidResponse(period);
            log.debug("Form ID Output :: " + formId);
            joiner.add("formid: " + formId);
            joiner.add("status: \\\"Form sent out\\\"");
            joiner.add("receiptdate: null");
            joiner.add("lockedby: \\\" \\\"");
            joiner.add("lockeddate: null");
            joiner.add("formtype: \\\"" + outputRow.getString("formtype") + "\\\"");
            joiner.add("checkletter: \\\"" + outputRow.getString("checkletter") + "\\\"");
            joiner.add("frozensicoutdated: \\\"" + outputRow.getString("frosic92") + "\\\"");
            joiner.add("rusicoutdated: \\\"" + outputRow.getString("rusic92") + "\\\"");
            joiner.add("frozensic: \\\"" + outputRow.getString("frosic2007") + "\\\"");
            joiner.add("rusic: \\\"" + outputRow.getString("rusic2007") + "\\\"");
            joiner.add("frozenemployees: \\\"" + outputRow.getString("froempees") + "\\\"");
            joiner.add("employees: \\\"" + outputRow.getString("employees") + "\\\"");
            joiner.add("frozenemployment: \\\"" + outputRow.getString("froempment") + "\\\"");
            joiner.add("employment: \\\"" + outputRow.getString("employment") + "\\\"");
            joiner.add("frozenfteemployment: \\\"" + outputRow.getString("froftempt") + "\\\"");
            joiner.add("fteemployment: \\\"" + outputRow.getString("ftempment") + "\\\"");
            joiner.add("frozenturnover: \\\"" + outputRow.getString("frotover") + "\\\"");
            joiner.add("turnover: \\\"" + outputRow.getString("turnover") + "\\\"");
            joiner.add("enterprisereference: \\\"" + outputRow.getString("entref") + "\\\"");
            joiner.add("wowenterprisereference: \\\"" + outputRow.getString("wowentref") + "\\\"");
            joiner.add("cellnumber: " + outputRow.getInt("cell_no"));
            joiner.add("currency: \\\"" + outputRow.getString("currency") + "\\\"");
            joiner.add("vatreference: \\\"" + outputRow.getString("vatref") + "\\\"");
            joiner.add("payereference: \\\"" + outputRow.getString("payeref") + "\\\"");
            joiner.add("companyregistrationnumber: \\\"" + outputRow.getString("crn") + "\\\"");
            joiner.add("numberlivelocalunits: \\\"" + outputRow.getString("live_lu") + "\\\"");
            joiner.add("numberlivevat: \\\"" + outputRow.getString("live_vat") + "\\\"");
            joiner.add("numberlivepaye: \\\"" + outputRow.getString("live_paye") + "\\\"");
            joiner.add("legalstatus: \\\"" + outputRow.getString("legalstatus") + "\\\"");
            joiner.add("reportingunitmarker: \\\"" + outputRow.getString("entrepmkr") + "\\\"");
            joiner.add("region: \\\"" + outputRow.getString("region") + "\\\"");
            joiner.add("birthdate: \\\"" + outputRow.getString("birthdate") + "\\\"");
            joiner.add("enterprisename: \\\"" + outputRow.getString("entname1")
                    + outputRow.getString("entname2") + outputRow.getString("entname3") + "\\\"");
            joiner.add("referencename: \\\"" + outputRow.getString("runame1")
                    + outputRow.getString("runame2") + outputRow.getString("runame3") + "\\\"");
            joiner.add("referenceaddress: \\\"" + outputRow.getString("ruaddr1")
                    + outputRow.getString("ruaddr2") + outputRow.getString("ruaddr3")
                    + outputRow.getString("ruaddr4") + outputRow.getString("ruaddr5") + "\\\"");
            joiner.add("referencepostcode: \\\"" + outputRow.getString("rupostcode") + "\\\"");
            joiner.add("tradingstyle: \\\"" + outputRow.getString("tradstyle1")
                    + outputRow.getString("tradstyle2") + outputRow.getString("tradstyle3") + "\\\"");
            joiner.add("contact: \\\"" + outputRow.getString("contact") + "\\\"");
            joiner.add("telephone: \\\"" + outputRow.getString("telephone") + "\\\"");
            joiner.add("fax: \\\"" + outputRow.getString("fax") + "\\\"");
            joiner.add("selectiontype: \\\"" + outputRow.getString("seltype") + "\\\"");
            joiner.add("inclusionexclusion: \\\"" + outputRow.getString("inclexcl") + "\\\"");
            joiner.add("createdby: \\\"fisdba\\\"");
            joiner.add("createddate: \\\"" + time.toString() + "\\\"");
            joiner.add("lastupdatedby: \\\" \\\"");
            joiner.add("lastupdateddate: null");
            return joiner.toString();
        } catch (Exception err) {
            log.error("Problem in extracting contributor row {} " + err.getMessage());
            StringBuilder sbErrorMessage = new StringBuilder();
            sbErrorMessage.append("Error in processing selection file for Reference: ").append(reference).append(" Period: ");
            sbErrorMessage.append(periodStr);
            sbErrorMessage.append(" Survey: ").append(survey);
            sbErrorMessage.append(err.getMessage());
            sbErrorMessage.append( " The contributor row: ");
            sbErrorMessage.append(outputRow);

            throw new InvalidJsonException(sbErrorMessage.toString(), err);
        }
    }


    public String processGraphQlErrorMessage(String graphQlResponse) throws InvalidJsonException {
        StringBuilder sbErrorMessage = new StringBuilder();
        JSONObject graphQlObject = new JSONObject(graphQlResponse);
        if (graphQlObject.has("errors") ) {
            log.debug("Errors exists");
            JSONArray errorArray = graphQlObject.getJSONArray("errors");
            log.debug("Complete GraphQL error message: "+errorArray.toString());
            for(int i=0; i< errorArray.length(); i++) {

                String message = errorArray.getJSONObject(i).getString("message");
                if (message.contains("duplicate key value violates unique constraint")) {
                    sbErrorMessage.append("Contributor already exists in the database");
                } else {
                    sbErrorMessage.append(" There is a problem in Graph QL ").append(message);
                }
                log.debug("Graph QL Error Message after parsing: "+message);
            }

        }

        log.debug("GraphQL error message if any after processing : " + sbErrorMessage.toString());
        return sbErrorMessage.toString();
    }



}
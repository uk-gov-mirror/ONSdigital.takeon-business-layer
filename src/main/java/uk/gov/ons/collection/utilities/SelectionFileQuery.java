package uk.gov.ons.collection.utilities;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import uk.gov.ons.collection.exception.InvalidJsonException;

public class SelectionFileQuery {
    private HashMap<String, String> variables;
    private List<String> intVariables = new ArrayList<>(Arrays.asList("first", "last", "formid"));
    private JSONArray contributorValuesArray;
    private JSONObject contributorObject;
    private final Timestamp time = new Timestamp(new Date().getTime());

    public SelectionFileQuery(Map<String, String> variables){
        this.variables = (variables == null) ? new HashMap<>() : new HashMap<>(variables);
    }

    public SelectionFileQuery(String jsonString) throws InvalidJsonException {
        try {
            contributorObject = new JSONObject(jsonString);
            contributorValuesArray = contributorObject.getJSONArray("attributes");

        } catch (JSONException err) {
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
    
    public String buildCheckIDBRFormidQuery() {
        StringBuilder checkIDBRFormidQuery = new StringBuilder();
        checkIDBRFormidQuery.append("{\"query\": \"query checkidbrformid($formtype: String) " +
                "{ checkidbrformid (condition: {formtype: $formtype}) {" +
                "nodes { formid survey formtype periodstart periodend }}}}}\"," +
                "\"variables\": {"); 
        checkIDBRFormidQuery.append(buildVariables());
        checkIDBRFormidQuery.append("}}");
        return checkIDBRFormidQuery.toString();
    }

    public String buildSaveSelectionFileQuery() throws InvalidJsonException {
        var jsonQlResponse = new StringBuilder();
        jsonQlResponse.append("{\"query\" : \"mutation loadContributors {loadidbrform(input: {arg0: ");
        jsonQlResponse.append("[" + getSelectionLoadData() + "]");
        jsonQlResponse.append("}){clientMutationId}}\"}");
        return jsonQlResponse.toString();
    }

    // Loop through the given validation output array json and convert it into a graphQL compatable format
    private String getSelectionLoadData() throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");
        for (int i = 0; i < contributorValuesArray.length(); i++) {
            joiner.add("{" + extractValidationOutputRow(i) + "}");
        }
        return joiner.toString();
    }

    public String getTime() {
        return time.toString();
    }

    // Convert a row for the given index and provide it in graphQL desired format
    private String extractValidationOutputRow(int index) throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");
        try {
            var outputRow = contributorValuesArray.getJSONObject(index);
            var periodSurvey = contributorObject;
            joiner.add("period: \\\"" + periodSurvey.getString("period") + "\\\"");
            joiner.add("survey: \\\"" + periodSurvey.getString("survey") + "\\\"");
            joiner.add("reference: \\\"" + outputRow.getString("ruref") + "\\\"");
            joiner.add("formid: " + outputRow.getInt("formtype")); // Call a method here to get the form id or pass it through into API for this call?
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
            joiner.add("enterprisename: \\\"" + outputRow.getString("entname1") + outputRow.getString("entname2") + outputRow.getString("entname3") + "\\\"");
            joiner.add("referencename: \\\"" + outputRow.getString("runame1") + outputRow.getString("runame2") + outputRow.getString("runame3") + "\\\"");
            joiner.add("referenceaddress: \\\"" + outputRow.getString("ruaddr1") + outputRow.getString("ruaddr2") + outputRow.getString("ruaddr3") + 
                                                outputRow.getString("ruaddr4") + outputRow.getString("ruaddr5") + "\\\"");
            joiner.add("referencepostcode: \\\"" + outputRow.getString("rupostcode") + "\\\"");
            joiner.add("tradingstyle: \\\"" + outputRow.getString("tradstyle1") + outputRow.getString("tradstyle2") + outputRow.getString("tradstyle3") + "\\\"");
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
            throw new InvalidJsonException("Error processing response json structure: " + contributorValuesArray, err);
        }
    }

}
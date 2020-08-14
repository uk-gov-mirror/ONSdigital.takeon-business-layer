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
        jsonQlResponse.append("{\"query\" : \"mutation loadResponse {LoadIDBRForm(input: {arg0: ");
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
            joiner.add("Period: \\\"" + periodSurvey.getString("period") + "\\\"");
            joiner.add("Survey: \\\"" + periodSurvey.getString("survey") + "\\\"");
            joiner.add("Reference: \\\"" + outputRow.getString("ruref") + "\\\"");
            joiner.add("FormID: " + outputRow.getInt("formtype")); // Call a method here to get the form id or pass it through into API for this call?
            joiner.add("Status: \\\" \\\"");
            joiner.add("ReceiptDate: \\\" \\\"");
            joiner.add("LockedBy: \\\" \\\"");
            joiner.add("LockedDate: \\\" \\\"");
            joiner.add("FormType: \\\"" + outputRow.getString("formtype"));
            joiner.add("Checkletter: \\\"" + outputRow.getString("checkletter"));
            joiner.add("FrozenSicOutdated: \\\"" + outputRow.getString("frosic92"));
            joiner.add("RuSicOutdated: \\\"" + outputRow.getString("rusic92"));
            joiner.add("FrozenSic: \\\"" + outputRow.getString("frosic2007"));
            joiner.add("RuSic: \\\"" + outputRow.getString("rusic2007"));
            joiner.add("FrozenEmployees: \\\"" + outputRow.getString("froempees"));
            joiner.add("Employees: \\\"" + outputRow.getString("employees"));
            joiner.add("FrozenEmployment: \\\"" + outputRow.getString("froempment"));
            joiner.add("Employment: \\\"" + outputRow.getString("employment"));
            joiner.add("FrozenFteEmployment: \\\"" + outputRow.getString("froftempt"));
            joiner.add("FteEmployment: \\\"" + outputRow.getString("ftempment"));
            joiner.add("FrozenTurnover: \\\"" + outputRow.getString("frotover"));
            joiner.add("Turnover: \\\"" + outputRow.getString("turnover"));
            joiner.add("EnterpriseReference: \\\"" + outputRow.getString("entref"));
            joiner.add("WowEnterpriseReference: \\\"" + outputRow.getString("wowentref"));
            joiner.add("CellNumber: \\\"" + outputRow.getString("cell_no"));
            joiner.add("Currency: \\\"" + outputRow.getString("currency"));
            joiner.add("VatReference: \\\"" + outputRow.getString("vatref"));
            joiner.add("PayeReference: \\\"" + outputRow.getString("payeref"));
            joiner.add("CompanyRegistrationNumber: \\\"" + outputRow.getString("crn"));
            joiner.add("NumberLiveLocalUnits: \\\"" + outputRow.getString("live_lu"));
            joiner.add("NumberLiveVat: \\\"" + outputRow.getString("live_vat"));
            joiner.add("NumberLivePaye: \\\"" + outputRow.getString("live_paye"));
            joiner.add("LegalStatus: \\\"" + outputRow.getString("legalstatus"));
            joiner.add("ReportingUnitMarker: \\\"" + outputRow.getString("entrepmkr"));
            joiner.add("Region: \\\"" + outputRow.getString("region"));
            joiner.add("BirthDate: \\\"" + outputRow.getString("birthdate"));
            joiner.add("EnterpriseName: \\\"" + outputRow.getString("entname1") + outputRow.getString("entname2") + outputRow.getString("entname3"));
            joiner.add("ReferenceName: \\\"" + outputRow.getString("runame1") + outputRow.getString("runame2") + outputRow.getString("runame3"));
            joiner.add("ReferenceAddress: \\\"" + outputRow.getString("ruaddr1") + outputRow.getString("ruaddr2") + outputRow.getString("ruaddr3") + 
                                                outputRow.getString("ruaddr4") + outputRow.getString("ruaddr5"));
            joiner.add("ReferencePostcode: \\\"" + outputRow.getString("rupostcode"));
            joiner.add("TradingStyle: \\\"" + outputRow.getString("tradstyle1") + outputRow.getString("tradstyle2") + outputRow.getString("tradstyle3"));
            joiner.add("Contact: \\\"" + outputRow.getString("contact"));
            joiner.add("Telephone: \\\"" + outputRow.getString("telephone"));
            joiner.add("Fax: \\\"" + outputRow.getString("fax"));
            joiner.add("SelectionType: \\\"" + outputRow.getString("seltype"));
            joiner.add("InclusionExclusion: \\\"" + outputRow.getString("inclexcl"));
            joiner.add("CreatedBy: \\\"fisdba\\\"");
            joiner.add("CreatedDate: \\\"" + time.toString() + "\\\"");
            joiner.add("LastUpdatedBy: \\\" \\\"");
            joiner.add("LastUpdatedDate: \\\" \\\"");
            return joiner.toString();
        } catch (Exception err) {
            throw new InvalidJsonException("Error processing response json structure: " + contributorValuesArray, err);
        }
    }

}
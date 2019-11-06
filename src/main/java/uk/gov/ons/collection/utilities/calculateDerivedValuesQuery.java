package uk.gov.ons.collection.utilities;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.var;
import java.util.StringJoiner;

// Refactor:
// 1) Class for queries - constructor for 'key'
// 2) Class for response - constructor for input json
// 3) Class for running the calculations or can do this in response
public class calculateDerivedValuesQuery {

    private JSONObject inputKey;

    public calculateDerivedValuesQuery(String key) {
        try {
            inputKey = new JSONObject(key);
        } catch (JSONException e) {
            System.out.println("Given string could not be converted/processed: " + inputKey + e);
        }
    }

    // Builds a query to the form definition table to find all questions codes and derived formulae
    public String buildFormDefinitionQuery() {
        StringBuilder formDefintionQuery = new StringBuilder();
        formDefintionQuery.append("{\"query\":\"query formDefinitionByReference {allContributors(condition: {");
        formDefintionQuery.append(getReferencePeriodAndSurvey());
        formDefintionQuery.append("}){nodes {formByFormid {formdefinitionsByFormid {nodes {questioncode,derivedformula}}}}}}\"}");

        return formDefintionQuery.toString();
    }

    public String buildGetResponsesQuery() {
        StringBuilder responseQuery = new StringBuilder();
        responseQuery.append("{\"query\":\"query getResponses($reference: String, $period: String, $survey: String){");
        responseQuery.append("allResponses(condition: {");
        responseQuery.append(getReferencePeriodAndSurvey());
        responseQuery.append("}){nodes {response questioncode}}}\"}");
        return responseQuery.toString();
    }

    // Gets reference, period, survey from the input JSON
    private String getReferencePeriodAndSurvey() {
        StringJoiner joiner = new StringJoiner(",");
            joiner.add("reference: \"" + inputKey.getString("reference") + "\"");
            joiner.add("period: \""    + inputKey.getString("period")    + "\"");
            joiner.add("survey: \""    + inputKey.getString("survey")    + "\"");
        return joiner.toString();
    }

}

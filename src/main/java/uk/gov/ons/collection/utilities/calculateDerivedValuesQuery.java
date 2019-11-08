package uk.gov.ons.collection.utilities;

import org.json.JSONException;
import org.json.JSONObject;

import uk.gov.ons.collection.exception.InvalidJsonException;

import java.util.Map;
import java.util.StringJoiner;

public class calculateDerivedValuesQuery {

    private JSONObject inputKey;

    public calculateDerivedValuesQuery(Map<String, String> variables) throws InvalidJsonException {
        try {
            inputKey = new JSONObject(variables);
        } catch (JSONException e) {
            throw new InvalidJsonException("Can't build form or response Query for calculating derived values: " + inputKey, e);
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
        responseQuery.append("}){nodes {response questioncode instance}}}\"}");
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

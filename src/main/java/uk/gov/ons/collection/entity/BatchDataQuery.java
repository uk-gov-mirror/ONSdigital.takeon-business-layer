package uk.gov.ons.collection.entity;

import org.json.JSONException;
import org.json.JSONObject;
import lombok.extern.log4j.Log4j2;

import uk.gov.ons.collection.exception.InvalidJsonException;

import java.util.Map;
import java.util.StringJoiner;

@Log4j2
public class BatchDataQuery {

    // Input key of ref, period and survey
    private JSONObject inputKey;

    public BatchDataQuery(Map<String, String> variables) throws InvalidJsonException {
        try {
            inputKey = new JSONObject(variables);
        } catch (JSONException e) {
            throw new InvalidJsonException("Can't build Query for checking if reference/period/survey exists for Batch ingest of data: " + inputKey, e);
        }
    }

    public String buildCheckReferenceExistsQuery() throws InvalidJsonException {
        StringBuilder referenceQuery = new StringBuilder();
        try {
            referenceQuery.append("{\"query\":\"query contributorformdefinition {");
            referenceQuery.append("allContributors(condition: {");
            referenceQuery.append(getReferencePeriodAndSurvey());
            referenceQuery.append("}){nodes {reference period survey status formid ");
            referenceQuery.append("formByFormid { formdefinitionsByFormid(condition: {");
            referenceQuery.append("derivedformula: \\\"");
            referenceQuery.append(inputKey.getString("derivedformula") + "\\\"");
            referenceQuery.append("}){nodes {questioncode}}}");
            referenceQuery.append("}}}\"}");
            log.info("Output of checking reference exists for batch data query {}", referenceQuery.toString());
        } catch (JSONException e) {
            throw new InvalidJsonException("Can't build Query for checking if reference/period/survey exists for Batch ingest of data: " + inputKey, e);
        }
        return referenceQuery.toString();
    }

    // Gets reference, period, survey from the input Key
    private String getReferencePeriodAndSurvey() {
        StringJoiner joiner = new StringJoiner(",");
        joiner.add("reference: \\\"" + inputKey.getString("reference") + "\\\"");
        joiner.add("period: \\\""    + inputKey.getString("period")    + "\\\"");
        joiner.add("survey: \\\""    + inputKey.getString("survey")    + "\\\"");
        return joiner.toString();
    }
}


package uk.gov.ons.collection.utilities;

import org.json.JSONObject;

import java.util.StringJoiner;

public class RecalculateDerivedValues {

    private JSONObject definitionInput;
    private JSONObject extractionInput;

    public RecalculateDerivedValues(String inputJSON) {
        definitionInput = new JSONObject(inputJSON);
        extractionInput = new JSONObject(inputJSON);
    }

    public String buildFormDefinitionQuery() {
        StringBuilder formDefintionQuery = new StringBuilder();
        formDefintionQuery.append("{\"query\": \"query formDefinitionByReference {allContributors(condition: {");
        formDefintionQuery.append(getReference());
        formDefintionQuery.append("}){nodes {formByFormid {formdefinitionsByFormid {nodes {questioncode,derivedformula}}}}}}\"}");

        return formDefintionQuery.toString();
    }

    private String getReference() {
        StringJoiner joiner = new StringJoiner(",");
            joiner.add("reference: \"" + definitionInput.getString("reference") + "\"");
            joiner.add("period: \"" + definitionInput.getString("period") + "\"");
            joiner.add("survey: \"" + definitionInput.getString("survey") + "\"");
        return joiner.toString();
    }

    public String extractDerivedValues() {
        StringJoiner joiner = new StringJoiner(",");
        var formulaArray = extractionInput.getJSONObject("data")
                .getJSONObject("allContributors")
                .getJSONArray("nodes")
                .getJSONObject(0)
                .getJSONObject("formByFormid")
                .getJSONObject("formdefinitionsByFormid")
                .getJSONArray("nodes");
        for (int i = 0; i < formulaArray.length(); i++) {
            if(!formulaArray.getJSONObject(i).getString("derivedformula").equals("")){
                joiner.add("questioncode: \"" + formulaArray.getJSONObject(i).getString("questioncode") + "\"");
                joiner.add("derivedformula: \"" + formulaArray.getJSONObject(i).getString("derivedformula") + "\"");
            }
        }
        return joiner.toString();
    }
}

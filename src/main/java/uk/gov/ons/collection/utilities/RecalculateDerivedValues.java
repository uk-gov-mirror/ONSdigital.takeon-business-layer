package uk.gov.ons.collection.utilities;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.var;
import uk.gov.ons.collection.exception.InvalidJsonException;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

// Refactor:
// 1) Class for queries - constructor for 'key'
// 2) Class for response - constructor for input json
// 3) Class for running the calculations or can do this in response
public class RecalculateDerivedValues {

    private JSONObject inputKey;
    private JSONObject inputJSON;

    public RecalculateDerivedValues(String input, String key) {
        inputJSON = new JSONObject(input);
        try {
            inputKey = new JSONObject(key);
        } catch (JSONException e) {
            throw new InvalidJsonException("Given string could not be converted/processed: " + inputKey, e);
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

    // Splits the extracted formulae on whitespace and returns a list
    public String getQuestionCodes() {
        String derivedFormula = inputJSON.getString("derivedformula");
        String questionCodes[] = derivedFormula.split("\\s+");
        List <String> questionCodeList = new ArrayList<>();
        for(int i=0; i < questionCodes.length; i++){
            questionCodeList.add(questionCodes[i]);
        }

        return questionCodeList.toString();
    }

    // Use this class 
    // In new class
    public String parseFormData(String formJsonData){
        return new String();
    }

    // In new class
    public String parseResponseData(String responseJsonData){
        return new String();
    }

    public String getDerivedQuestionResponses(String formJsonData, String reponseJsonData){
        return new String();
    }

    // HAVE ALTERNATIVE WAY OF DOING THIS - MOVE THIS TO RESPONSE CLASS ALSO
    // Goes through the JSON returned by definition query and extracts the question codes and derived formulae,
    // Ignores anything without a derived formula
    // public String extractDerivedFormulae() {
    //     StringJoiner joiner = new StringJoiner(",");
    //     var formulaArray = inputJSON.getJSONObject("data")
    //             .getJSONObject("allContributors")
    //             .getJSONArray("nodes")
    //             .getJSONObject(0)
    //             .getJSONObject("formByFormid")
    //             .getJSONObject("formdefinitionsByFormid")
    //             .getJSONArray("nodes");
    //     for (int i = 0; i < formulaArray.length(); i++) {
    //         if(!formulaArray.getJSONObject(i).getString("derivedformula").equals("")){
    //             joiner.add("questioncode: \"" + formulaArray.getJSONObject(i).getString("questioncode") + "\"");
    //             joiner.add("derivedformula: \"" + formulaArray.getJSONObject(i).getString("derivedformula") + "\"");
    //         }
    //     }
    //     var extractedFormulae = "{" + joiner + "}";

    //     return extractedFormulae;
    // }

    //public String 


    // // REMOVE THIS AND HAVE QUERY TO GET ALL RESPONSES AND KEEP IN MEMORY RATHER THAN NUMEROUS DB CALLS
    // // Using output from extract derived formulae query, builds a query to get responses
    // public String buildExtractResponseQuery() {
    //     StringBuilder extractResponseQuery = new StringBuilder();
    //     extractResponseQuery.append("{\"query\": \"query responseByQuestionCode {");
    //     extractResponseQuery.append(getAliasQueries());
    //     extractResponseQuery.append("}) {nodes {questioncode,response}}}\\\"}\")");
    //     return extractResponseQuery.toString();
    // }



    // This will form the middle part of the buildExtractResponseQuery, may not be needed if as nicer way is found
    //private String getAliasQueries() {
        //        "alias1: allResponses(condition: {questioncode: \\\"1000\\\", reference: \\\"12345678001\\\"}) {nodes {questioncode,response}}";
//        "alias2: allResponses(condition: {questioncode: \\\"1001\\\", reference: \\\"12345678001\\\"}) {nodes {questioncode,response}}";
//        "alias3: allResponses(condition: {questioncode: \\\"4000\\\", reference: \\\"12345678001\\\"";
        //return null;
    //}

}

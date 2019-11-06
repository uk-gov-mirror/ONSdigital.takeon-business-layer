package uk.gov.ons.collection.utilities;

import java.util.ArrayList;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.ibm.icu.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class calculateDerviedValuesResponse {

    private JSONObject formInputJSON;
    private JSONObject responseInputJSON;

    public calculateDerviedValuesResponse(String formJSON, String responseJSON) {
        try {
            formInputJSON = new JSONObject(formJSON);
            responseInputJSON = new JSONObject(responseJSON);
        } catch (JSONException e) {
            System.out.println("Given string could not be converted/processed: " + formJSON + responseJSON + e);
        }
    }

    // Parse form data to remove nested structure
    public JSONObject parseFormData() {
        var outputArray = new JSONArray();
        if (formInputJSON.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").length() > 0) {
            outputArray = formInputJSON.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes")
                    .getJSONObject(0).getJSONObject("formByFormid").getJSONObject("formdefinitionsByFormid")
                    .getJSONArray("nodes");
        }
        return new JSONObject().put("form_data", outputArray);
    }

    // Parse response data to remove nested structure
    public JSONObject parseResponseData() {
        var outputArray = new JSONArray();
        if (responseInputJSON.getJSONObject("data").getJSONObject("allResponses").getJSONArray("nodes").length() > 0) {
            outputArray = responseInputJSON.getJSONObject("data").getJSONObject("allResponses").getJSONArray("nodes");
        }
        return new JSONObject().put("response_data", outputArray);
    }

    // Splits the extracted formulae on whitespace and returns a list
    private List<String> getQuestionCodes(String derivedFormula) {
        String questionCodes[] = derivedFormula.split("\\s+");
        List<String> questionCodeList = new ArrayList<>();
        for (int i = 0; i < questionCodes.length; i++) {
            questionCodeList.add(questionCodes[i]);
        }
        return questionCodeList;
    }

    // Get the responses for the dervied questions by iteating through the Responses
    // and Form array (which holds all expected Questions)
    private JSONArray getDerivedQuestionResponses() {
        var formArray = new JSONArray();
        var responseArray = new JSONArray();
        var parsedFormData = parseFormData();
        var parsedResponseData = parseResponseData();
        var evaluatorArray = new JSONArray();
        try {
            formArray = parsedFormData.getJSONArray("form_data");
            responseArray = parsedResponseData.getJSONArray("response_data");
        } catch (JSONException e) {
            System.out.println("Given string could not be converted/processed: " + e);
        }

        for (int i = 0; i < formArray.length(); i++) {
            if (formArray.getJSONObject(i).getString("derivedformula") != "") {
                String questionCode = formArray.getJSONObject(i).getString("questioncode");
                List<String> formulaList = getQuestionCodes(formArray.getJSONObject(i).getString("derivedformula"));
                JSONArray responses = new JSONArray();
                for (int j = 0; j < formulaList.size(); j++) {
                    for (int k = 0; k < responseArray.length(); k++) {
                        if (formulaList.get(j).equals(responseArray.getJSONObject(k).getString("questioncode"))) {
                            responses.put(responseArray.getJSONObject(k).getString("response"));
                        }
                    }
                    if (formulaList.get(j).equals(new String("+"))) {
                        responses.put(formulaList.get(j));
                    } else if (formulaList.get(j).equals(new String("-"))) {
                        responses.put(formulaList.get(j));
                    }
                }
                var questions = new JSONObject();
                questions.put("questionCode", questionCode);
                questions.put("formulaToRun", responses);
                questions.put("result", "");

                evaluatorArray.put(questions);
            }
        }
        System.out.println("Formulas to be evaluated: " + evaluatorArray.toString());
        return evaluatorArray;
    }

    // Convert all Integer response values to Big Decimal then back to String and
    // append back to evaluator array
    // Call this in a for loop to generate the formula each time
    private JSONArray convertResponsesToBigDecimal() {
        var evaluatorArray = getDerivedQuestionResponses();
        var formulaToRun = new StringBuilder("");
        for (int i = 0; i < evaluatorArray.length(); i++) {
            JSONArray inputFormula = evaluatorArray.getJSONObject(i).getJSONArray("formulaToRun");
            for (int j = 0; j < inputFormula.length(); j++) {
                if (!inputFormula.getString(j).equals(new String("+"))
                        || !inputFormula.getString(j).equals(new String("-"))) {
                    var bigDecimalNumber = new BigDecimal(inputFormula.getString(j));
                    formulaToRun.append(bigDecimalNumber.toString());
                } else {
                    formulaToRun.append(inputFormula.getString(j));
                }
            }
        }
        // Substitute formulaToRun to be a String of the formula ready to run rather
        // than an array
        for (int i = 0; i < evaluatorArray.length(); i++) {
            evaluatorArray.getJSONObject(i).put("updatedFormula", formulaToRun);
        }
        return evaluatorArray;
    }

    // Calculate formulas - make this private too and just make updateDerivedQuestionsPublic?
    public JSONArray calculateDerviedValues() {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        Object result;
        String formula;
        var outputArray = new JSONArray();
        var evaluatorArray = convertResponsesToBigDecimal();

        // Build up array of Question codes + Result of calculated formula 
        try {
            for (int i = 0; i < evaluatorArray.length(); i++) {
            var calculatedQuestion = new JSONObject();
            formula = evaluatorArray.getJSONObject(i).getString("updatedFormula");
            result = engine.eval(formula);
            calculatedQuestion.put("questionCode", evaluatorArray.getJSONObject(i).getString("questionCode"));
            calculatedQuestion.put("result", result);
            outputArray.put(calculatedQuestion);
            }
        } catch (ScriptException e) {
            System.out.println("Error Evaluating formula: " + e);
        }
        return outputArray;

    }

    public JSONArray updateDerivedQuestions() {
        return new JSONArray();
    }


}
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

import uk.gov.ons.collection.exception.InvalidDerivedResponseException;
import uk.gov.ons.collection.exception.InvalidJsonException;

public class calculateDerviedValuesResponse {

    private JSONObject formInputJSON;
    private JSONObject responseInputJSON;

    public calculateDerviedValuesResponse(String formJSON, String responseJSON) throws InvalidJsonException {
        try {
            formInputJSON = new JSONObject(formJSON);
            responseInputJSON = new JSONObject(responseJSON);
        } catch (JSONException e) {
            throw new InvalidJsonException("Given string could not be converted/processed: " + e);
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
    // *** Take a look at refactoring this / making it more robust ***
    private List<String> getQuestionCodes(String derivedFormula) {
        String questionCodes[] = derivedFormula.split("\\s+");
        List<String> questionCodeList = new ArrayList<>();
        for (int i = 0; i < questionCodes.length; i++) {
            questionCodeList.add(questionCodes[i]);
        }
        System.out.println("Question Codes List: " + questionCodeList.toString());
        return questionCodeList;
    }

    // Get the responses for the dervied questions by iterating through the Responses
    // and Form array (which holds all expected Questions)
    private JSONArray getDerivedQuestionResponses() throws InvalidJsonException {
        var formArray = new JSONArray();
        var responseArray = new JSONArray();
        var parsedFormData = parseFormData();
        var parsedResponseData = parseResponseData();
        var derivedArray = new JSONArray();
        try {
            formArray = parsedFormData.getJSONArray("form_data");
            responseArray = parsedResponseData.getJSONArray("response_data");
        } catch (JSONException e) {
            throw new InvalidJsonException("Given JSON did not contain contain form_data or response_data: " + e);
        }
        // Could refactor this to say ! not equal blank?
        for (int i = 0; i < formArray.length(); i++) {
            if (formArray.getJSONObject(i).getString("derivedformula") != "") {
                String questionCode = formArray.getJSONObject(i).getString("questioncode");
                // Split the derivedformula string by space and put into list
                List<String> formulaList = getQuestionCodes(formArray.getJSONObject(i).getString("derivedformula"));
                JSONArray responses = new JSONArray();
                int instance = 0;
                for (int j = 0; j < formulaList.size(); j++) {
                    for (int k = 0; k < responseArray.length(); k++) {
                        if (formulaList.get(j).equals(responseArray.getJSONObject(k).getString("questioncode"))) {
                            responses.put(responseArray.getJSONObject(k).getString("response"));
                            instance = responseArray.getJSONObject(k).getInt("instance");
                        }
                    }
                    if (formulaList.get(j).equals(new String("+"))) {
                        responses.put(formulaList.get(j));
                    } else if (formulaList.get(j).equals(new String("-"))) {
                        responses.put(formulaList.get(j));
                    }
                }
                var questions = new JSONObject();
                questions.put("instance", instance);
                questions.put("questioncode", questionCode);
                questions.put("formulatorun", responses);
                questions.put("result", "");

                derivedArray.put(questions);
            }
        }
        System.out.println("Formulas to be evaluated: " + derivedArray.toString());
        return derivedArray;
    }

    // Convert all Integer response values to Big Decimal then back to String and
    // append to existing evaluator array
    // Call this in a for loop to generate the formula each time
    private JSONArray convertResponsesToBigDecimal() throws InvalidDerivedResponseException, InvalidJsonException {
        var evaluatorArray = getDerivedQuestionResponses();
        var bigDecimalArray = new JSONArray();

        // Iterate through each formula array in the evaluator array and convert to BigDecimal
        for (int i = 0; i < evaluatorArray.length(); i++) {
            var formulaToRun = new StringBuilder("");
            if (!(evaluatorArray.getJSONObject(i).getJSONArray("formulatorun").isEmpty())) {
                JSONArray inputFormula = evaluatorArray.getJSONObject(i).getJSONArray("formulatorun");
                // Substitute formulatorun to be a String of the formula ready to run
                for (int j = 0; j < inputFormula.length(); j++) {
                    System.out.println("Input formula: " + inputFormula.getString(j));
                    if (!(inputFormula.getString(j).equals(new String("+")))
                            && !(inputFormula.getString(j).equals(new String("-")))) {
                                try {
                                    var bigDecimalNumber = new BigDecimal(inputFormula.getString(j));
                                    formulaToRun.append(bigDecimalNumber.toString());
                                } catch (NumberFormatException error) {
                                    throw new InvalidDerivedResponseException("Error converting response to Big decimal: ", error);
                                }
                    } else {
                        formulaToRun.append(inputFormula.getString(j));
                    }
            }
            var bigDecimalObject = new JSONObject();
            bigDecimalObject.put("instance", evaluatorArray.getJSONObject(i).getInt("instance"));
            bigDecimalObject.put("questioncode", evaluatorArray.getJSONObject(i).getString("questioncode"));
            bigDecimalObject.put("updatedformula", formulaToRun.toString());
            bigDecimalArray.put(bigDecimalObject);
            } 
        }
        System.out.println("Output from Convert Responses to Big Decimal: " + bigDecimalArray.toString());
        return bigDecimalArray;
    }

    // Calculate formulas
    public JSONArray calculateDerviedValues() throws InvalidDerivedResponseException, InvalidJsonException {
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
            formula = evaluatorArray.getJSONObject(i).getString("updatedformula");
            result = engine.eval(formula);
            calculatedQuestion.put("instance", evaluatorArray.getJSONObject(i).getInt("instance"));
            calculatedQuestion.put("questioncode", evaluatorArray.getJSONObject(i).getString("questioncode"));
            calculatedQuestion.put("result", result.toString());
            outputArray.put(calculatedQuestion);
            }
            // *** Add custom exception here too ***
        } catch (ScriptException e) {
            // Need to throw new here so it gets caught by calling method
            System.out.println("Error Evaluating formula: " + e);
        }
        System.out.println("Calculated Results output: " + outputArray.toString());
        return outputArray;

    }

    // Now create a structure which gets parsedResponse data JSON Object and updates the response
    // from the result in above output array (matching by question code) and is ready to be used
    // by Postgres Upsert function
    public JSONObject updateDerivedQuestionResponses() throws InvalidDerivedResponseException, InvalidJsonException {
        var resultsArray = calculateDerviedValues();
        var responseArray = parseResponseData().getJSONArray("response_data");
        var updatedResponseArray = new JSONArray();
        for (int i = 0; i < resultsArray.length(); i++) {
            for (int j = 0; j < responseArray.length(); j++) {
                if (resultsArray.getJSONObject(i).getString("questioncode")
                .equals(responseArray.getJSONObject(j).getString("questioncode"))) {
                    var updatedResponsesObject = new JSONObject();
                    updatedResponsesObject.put("instance", resultsArray.getJSONObject(i).get("instance"));
                    updatedResponsesObject.put("question", resultsArray.getJSONObject(i).getString("questioncode"));
                    updatedResponsesObject.put("response", resultsArray.getJSONObject(i).getString("result"));
                    updatedResponseArray.put(updatedResponsesObject);
                }  
            }
        }
        JSONObject updatedDerivedResponses = new JSONObject().put("responses", updatedResponseArray);
        System.out.println("Updated Derived Question Responses: " + updatedDerivedResponses.toString());
        return updatedDerivedResponses;
    }
}
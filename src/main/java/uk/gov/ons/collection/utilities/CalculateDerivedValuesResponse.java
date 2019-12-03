package uk.gov.ons.collection.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import com.ibm.icu.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lombok.extern.log4j.Log4j2;
import uk.gov.ons.collection.exception.FormulaCalculationException;
import uk.gov.ons.collection.exception.InvalidDerivedResponseException;
import uk.gov.ons.collection.exception.InvalidJsonException;

@Log4j2
public class CalculateDerivedValuesResponse {

    private JSONObject formInputJson;
    private JSONObject responseInputJson;

    public CalculateDerivedValuesResponse(String formJson, String responseJson) throws InvalidJsonException {
        try {
            formInputJson = new JSONObject(formJson);
            responseInputJson = new JSONObject(responseJson);
        } catch (JSONException e) {
            throw new InvalidJsonException("Given string could not be converted/processed: " + e);
        }
    }

    // Parse form data to remove nested structure
    public JSONObject parseFormData() {
        var outputArray = new JSONArray();
        if (formInputJson.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").length() > 0) {
            outputArray = formInputJson.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes")
                    .getJSONObject(0).getJSONObject("formByFormid").getJSONObject("formdefinitionsByFormid")
                    .getJSONArray("nodes");
        }
        log.info("Parsed Form Data: " + outputArray.toString());
        return new JSONObject().put("form_data", outputArray);
    }

    // Parse response data to remove nested structure
    public JSONObject parseResponseData() {
        var outputArray = new JSONArray();
        if (responseInputJson.getJSONObject("data").getJSONObject("allResponses").getJSONArray("nodes").length() > 0) {
            outputArray = responseInputJson.getJSONObject("data").getJSONObject("allResponses").getJSONArray("nodes");
        }
        log.info("Parsed Response Data: " + outputArray.toString());
        return new JSONObject().put("response_data", outputArray);
    }

    // Splits the extracted formulae on whitespace and returns a list
    // *** Take a look at refactoring this / making it more robust ***
    private ArrayList<String> getQuestionCodes(String derivedFormula) {
        String[] questionCodes = derivedFormula.split("\\s+");
        ArrayList<String> questionCodeList = new ArrayList<>();
        for (int i = 0; i < questionCodes.length; i++) {
            questionCodeList.add(questionCodes[i]);
        }
        log.info("Question Codes List: " + questionCodeList.toString());
        return questionCodeList;
    }

    // Create HashMap of Question code and ArrayList
    private HashMap<String, ArrayList<String>> getDerivedFormulas() throws InvalidJsonException {
        var formArray = new JSONArray();
        var parsedFormData = parseFormData();
        HashMap<String, ArrayList<String>> formulaMap = new HashMap<>();
        try {
            formArray = parsedFormData.getJSONArray("form_data");
        } catch (JSONException e) {
            throw new InvalidJsonException("Given JSON did not contain contain form_data or response_data: " + e);
        }
        for (int i = 0; i < formArray.length(); i++) {
            // Check if derived formula is not blank
            if (!formArray.getJSONObject(i).getString("derivedformula").isBlank()) {
                String questionCode = formArray.getJSONObject(i).getString("questioncode");
                // Split the derivedformula string by space and put into list
                formulaMap.put(questionCode, getQuestionCodes(formArray.getJSONObject(i).getString("derivedformula")));
            }
        }
        log.info("Map of derived formulas: " + formulaMap.toString());
        return formulaMap;
    }

    // Iterate through formulaMap, substitute questionCodes with responses
    private ArrayList<HashMap<String, Object>> getQuestionResponses() throws InvalidJsonException {
        var responseArray = new JSONArray();
        var parsedResponseData = parseResponseData();
        ArrayList<HashMap<String,Object>> derivedArray = new ArrayList<>();
        var formulaMap = getDerivedFormulas();
        int instance = 0;

        try {
            responseArray = parsedResponseData.getJSONArray("response_data");
        } catch (JSONException e) {
            throw new InvalidJsonException("Given JSON did not contain contain form_data or response_data: " + e);
        }

        for (Entry<String, ArrayList<String>> mapElement : formulaMap.entrySet()) {
            // Get each formula list
            ArrayList<String> formulaList = new ArrayList<>(mapElement.getValue());
            log.info("Formula List: " + formulaList.toString());
            ArrayList<String> responseList = new ArrayList<>();
            for (int i = 0; i < formulaList.size(); i++) {
                boolean condition = false;
                for (int j = 0; j < responseArray.length(); j++) {
                    if (formulaList.get(i).equals(responseArray.getJSONObject(j).getString("questioncode"))) {
                        responseList.add(responseArray.getJSONObject(j).getString("response"));
                        instance = responseArray.getJSONObject(j).getInt("instance");
                        condition = true;
                    } 
                }
                if (formulaList.get(i).equals(new String("+"))) {
                    responseList.add(formulaList.get(i));
                    condition = true;
                } else if (formulaList.get(i).equals(new String("-"))) {
                    responseList.add(formulaList.get(i));
                    condition = true;
                } 
                if (!condition) {
                    responseList.add(new String("0"));
                }
                log.info("Condition: " + condition);
            }
            HashMap<String, Object> questions = new HashMap<>();
            questions.put("instance", instance);
            questions.put("questioncode", mapElement.getKey().toString());
            questions.put("formulatorun", responseList);

            derivedArray.add(questions);
        }
        log.info("Derived Array:" + derivedArray);
        return derivedArray;
    }

    // Convert all reponses in formulatorun in each hashmap to BigDecimal
    private ArrayList<Object> convertDerivedResponsesToBigDecimal(ArrayList<String> updatedFormulaList)
            throws InvalidDerivedResponseException {
        var updatedFormulaArray = new ArrayList<>();
        for (int i = 0; i < updatedFormulaList.size(); i++) {
            log.info("Input formula: " + updatedFormulaList.get(i));
            if (!(updatedFormulaList.get(i).equals(new String("+")))
                && !(updatedFormulaList.get(i).equals(new String("-")))) {
                try {
                    // Check if any values in the formula are blank
                    if (updatedFormulaList.get(i).isBlank()) {
                        var bigDecimalNumber = BigDecimal.ZERO;
                        updatedFormulaArray.add(bigDecimalNumber);
                    } else {
                        var bigDecimalNumber = new BigDecimal(updatedFormulaList.get(i));
                        updatedFormulaArray.add(bigDecimalNumber);
                    }
                } catch (NumberFormatException error) {
                    throw new InvalidDerivedResponseException("Error converting response to Big decimal: ", error);
                }
            } else {
                updatedFormulaArray.add(updatedFormulaList.get(i));
            }
        }
        return updatedFormulaArray;
    }

    // Method that takes in getQuestionResponsesOutput and calls convertToBigDecimal in a for loop 
    // which takes in the formulatorun list
    private ArrayList<HashMap<String, Object>> callConvertToBigDecimal() throws InvalidJsonException, InvalidDerivedResponseException {
        ArrayList<HashMap<String, Object>> inputArray = getQuestionResponses();
        for (int i = 0; i < inputArray.size(); i++) {
            // Will return a string of the formula after converted to BigDecimal
            @SuppressWarnings("unchecked")
            ArrayList<Object> updatedFormula = convertDerivedResponsesToBigDecimal((ArrayList<String>)inputArray.get(i).get("formulatorun"));
            inputArray.get(i).put("updatedformula", updatedFormula);
        }
        log.info("Conversion to Big Decimal: " + inputArray.toString());
        return inputArray;
    }

    // Get first value then get next 2, and instead of i++, i+2 to get operator and right value every time and -1 on index
    // If error, throw exception
    public ArrayList<HashMap<String, Object>> calculateDerivedValues()
            throws InvalidDerivedResponseException, InvalidJsonException, FormulaCalculationException {
        var calcFormulaArray = callConvertToBigDecimal();
        try {
            for (int i = 0; i < calcFormulaArray.size(); i++) {
                @SuppressWarnings("unchecked")
                var calculateFormulaList = (ArrayList<Object>)calcFormulaArray.get(i).get("updatedformula");
                log.info("Calculate Formula List:" + calculateFormulaList.toString());
                BigDecimal formulaResult = new BigDecimal(0);
                for (int j = 0; j < calculateFormulaList.size(); j += 2) {
                    if (j == 0) {
                        BigDecimal bigDecimalNumber = (BigDecimal)calculateFormulaList.get(j);
                        formulaResult = formulaResult.add(bigDecimalNumber);
                    } else if (j != 0) {
                        if (calculateFormulaList.get(j - 1).equals(new String("+"))) {
                            BigDecimal addBigDecimalNumber = new BigDecimal(0);
                            addBigDecimalNumber = (BigDecimal)calculateFormulaList.get(j);
                            log.info("Add bd number:" + addBigDecimalNumber.toString());
                            formulaResult = formulaResult.add(addBigDecimalNumber);
                        } else if (calculateFormulaList.get(j - 1).equals(new String("-"))) {
                            BigDecimal subtractBigDecimalNumber = new BigDecimal(0);
                            subtractBigDecimalNumber = (BigDecimal)calculateFormulaList.get(j);
                            log.info("Subtract bd number:" + subtractBigDecimalNumber.toString());
                            formulaResult = formulaResult.subtract(subtractBigDecimalNumber);
                        }
                    }
                }
                calcFormulaArray.get(i).put("result", formulaResult);
                log.info("Result of calculation:" + formulaResult.toString()); 
            }
        } catch (Exception err) {
            throw new FormulaCalculationException("Error Evaluating formula:", err);
        }
        log.info("Calc formula array: " + calcFormulaArray.toString());
        return calcFormulaArray;
    }

    // Now create a structure which gets parsedResponse data JSON Object and updates the response
    // from the result in above output array (matching by question code) and is ready to be used
    // by Postgres Upsert function
    public JSONObject updateDerivedQuestionResponses() throws InvalidDerivedResponseException, InvalidJsonException, FormulaCalculationException {
        var resultsArray = calculateDerivedValues();
        var updatedResponseArray = new JSONArray();
        for (int i = 0; i < resultsArray.size(); i++) {
            var updatedResponsesObject = new JSONObject();
            updatedResponsesObject.put("instance", resultsArray.get(i).get("instance"));
            updatedResponsesObject.put("questioncode", resultsArray.get(i).get("questioncode"));
            updatedResponsesObject.put("response", resultsArray.get(i).get("result").toString());
            updatedResponseArray.put(updatedResponsesObject);
        }
        JSONObject updatedDerivedResponses = new JSONObject().put("responses", updatedResponseArray);
        log.info("Updated Derived Question Responses: " + updatedDerivedResponses.toString());
        return updatedDerivedResponses;
    }
}
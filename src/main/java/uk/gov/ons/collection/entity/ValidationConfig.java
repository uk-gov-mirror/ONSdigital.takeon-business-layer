package uk.gov.ons.collection.entity;

import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
import org.json.JSONObject;
import uk.gov.ons.collection.exception.InvalidJsonException;

import java.util.ArrayList;
import java.util.HashSet;

@Log4j2
/**
 * This class is responsible for parsing and holding a given graphQL response of validation configuration.
*/
public class ValidationConfig {

    private final JSONObject validationConfig;

    /**
     * Constructor. Accepts a graphQL config response and undertakes some parsing/simplifying of the JSON structure
     * @param  queryResponse A graphQL JSON string containing a validation configuration
     * @throws InvalidJsonException If there are any JSON processing errors
    */
    public ValidationConfig(String queryResponse) throws InvalidJsonException {
        try {
            this.validationConfig = loadParsedJson(queryResponse);
        } catch (JSONException err) {
            throw new InvalidJsonException("Given string could not be converted to JSON: " + queryResponse, err);
        } catch (NullPointerException err) {
            throw new InvalidJsonException("NULL json passed through!", err);
        }
    }

    /**
     * Return the parsed & simplified/restructured JSON graphQL response.
     * @return  String  A graphQL query JSON structure as a String
    */
    public String getValidationConfig() {
        return validationConfig.toString();
    }

    // Take the graphQL JSON response and parse it into a slightly simpler structure for later processing/exporting.
    // Note: All of the JSON exception handling is passed up to the constructor
    private JSONObject loadParsedJson(String jsonResponse) {
        var validations = new JSONObject(jsonResponse).getJSONObject("data").getJSONObject("allValidationforms").getJSONArray("nodes");
        for (int i = 0; i < validations.length(); i++) {
            var currentValidation = validations.getJSONObject(i);
            flattenRules(currentValidation);
            simplifyOffsets(currentValidation);
            simplifyParameters(currentValidation);
        }
        return new JSONObject().put("validation_config",validations);
    }

    // Extract out the validation rule attributes and put them the same level as the matching validation
    // Note: All of the JSON exception handling is passed up to the constructor
    private void flattenRules(JSONObject currentValidation) {
        var rule = currentValidation.getJSONObject("validationruleByRule");
        currentValidation.put("name", rule.getString("name"))
                         .put("baseformula", rule.getString("baseformula"))
                         .put("rule", rule.getString("rule"));
    }

    // The period offsets are in a nested array. Simplify the structure by removing some unnecessary intermediate layers
    // Note: All of the JSON exception handling is passed up to the constructor
    private void simplifyOffsets(JSONObject currentValidation) {
        var periodOffsets = currentValidation.getJSONObject("validationruleByRule").getJSONObject("validationperiodsByRule").getJSONArray("nodes");
        currentValidation.put("period_offset", periodOffsets);
        currentValidation.remove("validationruleByRule");
    }

    // The parameters are in a nested array. Simplify the structure by removing some unnecessary intermediate layers
    // Note: All of the JSON exception handling is passed up to the constructor
    private void simplifyParameters(JSONObject currentValidation) {
        var parameters = currentValidation.getJSONObject("validationparametersByValidationid").getJSONArray("nodes");
        currentValidation.put("parameters", parameters);
        currentValidation.remove("validationparametersByValidationid");
    }


    /**
     * Each validation rule has a list of periods (period_offset) that it uses and each rule can be assigned to M questions (validation_form).
     * This method determines determine and returns a unique list of all of these offsets.
     * Note: This method uses an algorithm which is O(n^2) so will break down for large values of M and N.
     * @return  A unique list of period offsets (integers >=0)
     * @throws  InvalidJsonException    If there are any JSON processing errors
    */
    public ArrayList<Integer> getUniqueOffsets() throws InvalidJsonException {
        var uniqueOffsets = new HashSet<Integer>();
        try {
            var validations = validationConfig.getJSONArray("validation_config");
            for (int i = 0; i < validations.length(); i++) {
                var validationOffsets = validations.getJSONObject(i).getJSONArray("period_offset");
                for (int j = 0; j < validationOffsets.length(); j++) {
                    Integer offset = Integer.valueOf(validationOffsets.getJSONObject(j).getInt("periodoffset"));
                    uniqueOffsets.add(offset); // Hashsets are unique so duplicates don't get added
                }
            }
        } catch (Exception err) {
            log.info("Unable to process the validation config JSON :: " + err);
            throw new InvalidJsonException("Unable to process the validation config JSON: " + validationConfig, err);
        }
        return new ArrayList<Integer>(uniqueOffsets);
    }

}
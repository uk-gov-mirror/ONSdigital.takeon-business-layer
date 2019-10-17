package uk.gov.ons.collection.entity;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uk.gov.ons.collection.exception.InvalidJsonException;


public class PeriodOffsetResponse {
    
    private JSONObject jsonQlResponse;

    public PeriodOffsetResponse(String jsonString) throws InvalidJsonException {
        try {
            jsonQlResponse = new JSONObject(jsonString);
        }
        catch (JSONException err) {
            throw new InvalidJsonException("Given string could not be converted/processed: " + jsonString, err);
        }
    }

    public ArrayList<Integer> parsePeriodOffset() throws InvalidJsonException {
        ArrayList<Integer> uniqueOffsets = new ArrayList<>();
        try {        
            JSONArray offsets = jsonQlResponse.getJSONObject("data").getJSONObject("allValidationperiods").getJSONArray("nodes");
            for (int i=0; i < offsets.length(); i++) {    
                Integer offset = Integer.valueOf(offsets.getJSONObject(i).getInt("periodoffset"));            
                if (!uniqueOffsets.contains(offset)) {
                    uniqueOffsets.add(offset);
                }
            }
        }
        catch (Exception err) {
            throw new InvalidJsonException("Given JSON did not contain the expected variables: " + jsonQlResponse, err);
        } 
        return uniqueOffsets;
    }

    public int parseFormId() throws InvalidJsonException {        
        try {   
            return jsonQlResponse.getJSONObject("data").getJSONObject("allValidationperiods").getJSONArray("nodes")
            .getJSONObject(0).getJSONObject("validationruleByRule").getJSONObject("validationformsByRule")
            .getJSONArray("nodes").getJSONObject(0).getJSONObject("formByFormid")
            .getJSONObject("contributorsByFormid").getJSONArray("nodes").getJSONObject(0).getInt("formid");            
        }
        catch (Exception err) {
            throw new InvalidJsonException("Given JSON did not contain formID in the expected location: " + jsonQlResponse, err);
        } 
    }

    public String parsePeriodicity() throws InvalidJsonException {
        String periodicity;
        try {   
            periodicity = jsonQlResponse.getJSONObject("data").getJSONObject("allValidationperiods").getJSONArray("nodes")
            .getJSONObject(0).getJSONObject("validationruleByRule").getJSONObject("validationformsByRule")
            .getJSONArray("nodes").getJSONObject(0).getJSONObject("formByFormid")
            .getJSONObject("contributorsByFormid").getJSONArray("nodes").getJSONObject(0).getJSONObject("surveyBySurvey").getString("periodicity");
        }
        catch (Exception err) {
            throw new InvalidJsonException("Given JSON did not contain periodicity in the expected location: " + jsonQlResponse, err);
        } 

        return periodicity;  
    }
}
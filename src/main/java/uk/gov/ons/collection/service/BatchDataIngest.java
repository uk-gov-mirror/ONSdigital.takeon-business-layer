package uk.gov.ons.collection.service;

import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class BatchDataIngest {

    JSONObject inputJSON = new JSONObject();
    JSONArray referenceArray = new JSONArray();
    
    public BatchDataIngest(String batchResponses) {

        try {
            inputJSON = new JSONObject(batchResponses);
            referenceArray = new JSONArray(inputJSON.getJSONArray("batch_data"));
        } catch (JSONException e) {
            log.info("Batch responses are not valid JSON" + e);
        }
    }

    // Get response from query in controller and check size of nodes (for ref exists)


    // public String extractReferenceDetails() {
    //     // Check if reference/period/survey combination exists
    //     // Save into Hashset from JSON and loop through?
    //     HashSet<String> refPerSur = new HashSet<String>();
    //     for (int i = 0; i < referenceArray.length(); i++) {
    //         //StringBuilder refString = new StringBuilder();
    //         String reference = referenceArray.getJSONObject(i).getString("reference");
    //         String period = referenceArray.getJSONObject(i).getString("period");
    //         String survey = referenceArray.getJSONObject(i).getString("survey");
    //         //refString.append(reference).append(period).append(survey);
    //         //refPerSur.add(refString.toString());

    //     }

    //     return "";
    // }

}


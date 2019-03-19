package uk.gov.ons.collection.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.UpdatedFormData;

import java.util.List;

@Service
public class QuestionResponseService {

    @Autowired
    QuestionResponseProxy questionResponseProxy;
    @Autowired
    CurrentResponseService currentResponseService;

    public String putResponses(String params, String body){

        // Create a new JSONObject, pass the updatedResponses from the UI to the constructor
        JSONObject updatedResponsesJson = new JSONObject(body);
        JSONObject responseJsonToPassOn = new JSONObject();
        // Pass the JSONObject to the ResponseComparision class
            ResponseComparison responseComparison = new ResponseComparison(currentResponseService,updatedResponsesJson);
        // Get only the updated responses and not the responses that were already in the DB
            List<UpdatedFormData> responsesToPassTpPL = responseComparison.getOnlyUpdatedResponses();
            System.out.println(responsesToPassTpPL.toString());
            responseJsonToPassOn.put("Updated Responses", responsesToPassTpPL);
            responseJsonToPassOn.put("user", updatedResponsesJson.getJSONObject("user").getString("user"));
            System.out.println(responseJsonToPassOn.toString());

            // Since this is a new JSONObject returned from responseComparision, append the username
            // responsesToPassTpPL.put("user", updatedResponsesJson.getJSONObject("user"));
            return questionResponseProxy.putResponses(params, responseJsonToPassOn.toString());
    }
}

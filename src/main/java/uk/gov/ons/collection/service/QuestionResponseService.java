package uk.gov.ons.collection.service;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.UpdatedFormData;

import java.util.List;

@Log4j2
@Service
public class QuestionResponseService {

    @Autowired
    QuestionResponseProxy questionResponseProxy;
    
    @Autowired
    CurrentResponseService currentResponseService;

    public String putResponses(String params, String body) {

        // Create a new JSONObject, pass the updatedResponses from the UI to the constructor
        JSONObject updatedResponsesJson = new JSONObject(body);
        JSONObject responseJsonToPassOn = new JSONObject();
        // Pass the JSONObject to the ResponseComparision class
        ResponseComparison responseComparison = new ResponseComparison(currentResponseService,updatedResponsesJson);
        // Get only the updated responses and not the responses that were already in the DB
        List<UpdatedFormData> responsesToPassTpPL = responseComparison.getOnlyUpdatedResponses();
        log.info("Updated responses JSON { }", responsesToPassTpPL.toString());
        responseJsonToPassOn.put("Updated Responses", responsesToPassTpPL);
        responseJsonToPassOn.put("user", updatedResponsesJson.getJSONObject("user").getString("user"));
        log.info("Response Json To PassOn { }", responseJsonToPassOn.toString());

        // Since this is a new JSONObject returned from responseComparision, append the username
        // responsesToPassTpPL.put("user", updatedResponsesJson.getJSONObject("user"));
        return questionResponseProxy.putResponses(params, responseJsonToPassOn.toString());
    }
}

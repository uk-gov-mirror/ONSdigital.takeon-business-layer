package uk.gov.ons.collection.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.collection.service.QuestionResponseService;
//import uk.gov.ons.collection.service.ResponseComparison;

import java.util.Map;

@RestController
// Endpoint for the Upsert API
@RequestMapping(value = "/Upsert")
public class QuestionResponseController {

    @Autowired
    QuestionResponseService questionResponseService;

    @PutMapping(value = "/CompareResponses/{args}")

    // Create a PutMapping for the UI to put data to
    public void createResponseJSON (@RequestBody String updatedResponses, @MatrixVariable Map<String, String> matrixVars){
        String reference = matrixVars.get("reference");
        String period = matrixVars.get("period");
        String survey = matrixVars.get("survey");

        String params = "reference=" + reference + ";" + "period=" + period + ";" + "survey=" + survey;
        // Make a PUT request on the Persistence layer
        questionResponseService.putResponses(params, updatedResponses);
    }
}

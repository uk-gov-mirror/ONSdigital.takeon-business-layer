package uk.gov.ons.collection.controller;


import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import lombok.extern.log4j.Log4j2;
import uk.gov.ons.collection.entity.ResponseData;
import uk.gov.ons.collection.service.CompareUiAndCurrentResponses;
import uk.gov.ons.collection.service.GraphQlService;
import uk.gov.ons.collection.utilities.UpsertResponse;

@Log4j2
@Api(value = "Response Controller")
@RestController
@RequestMapping(value = "/response")
public class ResponseController {
    @Autowired
    GraphQlService qlService;

    @ApiOperation(value = "Save question responses", response = String.class)
    @RequestMapping(value = "/save/{vars}", method = {RequestMethod.POST, RequestMethod.PUT})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful in saving of all question responses", response = String.class)})
    @ResponseBody
    public void saveQuestionResponses(@RequestBody String updatedResponses) {

        log.info("API CALL!! --> /response/save :: " + updatedResponses);

        List<ResponseData>  currentResponseEntities;
        JSONObject updatedResponsesJson = new JSONObject(updatedResponses);
        CompareUiAndCurrentResponses responseComparison;

        try {
            var upsertResponse = new UpsertResponse(updatedResponses);
            var currentResponseQuery = upsertResponse.buildRetrieveOldResponseQuery();
            log.info("Current Response GraphQL Query :: " + currentResponseQuery);
            String qlResponseOutput = qlService.qlSearch(currentResponseQuery);
            log.info("Output from GraphQL Current Response  :: " + qlResponseOutput);
            JSONObject qlResponseOutputJson = new JSONObject(qlResponseOutput);
            var outputArray = new JSONArray();
            outputArray = qlResponseOutputJson.getJSONObject("data").getJSONObject("allResponses").getJSONArray("nodes");
            log.info("Output JSON Array  :: " + outputArray);
            currentResponseEntities = upsertResponse.buildCurrentResponseEntities(outputArray);

            //Call Compare Responses
            responseComparison = new CompareUiAndCurrentResponses(currentResponseEntities, updatedResponsesJson);
            // Get only the updated responses and not the responses that were already in the DB
            List<ResponseData> responsesToPassToDatabase = responseComparison.getFinalConsolidatedResponses();
            JSONArray jsonArray = new JSONArray(responsesToPassToDatabase);
            var upsertSaveResponse = new UpsertResponse(jsonArray);
            //Constructing GraphQL query for Save
            var saveQuery = upsertSaveResponse.buildConsolidateUpsertByArrayQuery();
            String qlSaveResponseOutput = qlService.qlSearch(saveQuery);
            log.info("Output after saving the responses {}", qlSaveResponseOutput);

            //Finally Updating the Form Status
            var contributorStatusQuery = upsertResponse.updateContributorStatus();
            String qlStatusOutput = qlService.qlSearch(contributorStatusQuery);
            log.info("Output after updating the form status {}", qlStatusOutput);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in saving :: " + e.getMessage());
        }


    }
}

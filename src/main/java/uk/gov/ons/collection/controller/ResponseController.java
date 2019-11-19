package uk.gov.ons.collection.controller;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.log4j.Log4j2;
import uk.gov.ons.collection.service.GraphQlService;
import uk.gov.ons.collection.utilities.UpsertResponse;
import uk.gov.ons.collection.utilities.CalculateDerivedValuesResponse;
import uk.gov.ons.collection.utilities.CalculateDerivedValuesQuery;
import org.json.JSONArray;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.MatrixVariable;
import uk.gov.ons.collection.entity.ResponseData;
import uk.gov.ons.collection.service.CompareUiAndCurrentResponses;

@Log4j2
@Api(value = "Response Controller")
@RestController
@RequestMapping(value = "/response")
public class ResponseController {
    @Autowired
    GraphQlService qlService;

    @ApiOperation(value = "Calculate derived question formulas", response = String.class)
    @RequestMapping(value = "/calculateDerivedQuestions/{vars}", method = { RequestMethod.POST, RequestMethod.PUT })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful calculation of all derived question responses", response = String.class) })
    @ResponseBody
    public String calculateDerivedValues(@MatrixVariable Map<String, String> searchParameters) {

        String formQuery = new String();
        String responseQuery = new String();
        String qlFormResponse;
        String qlResponsesResponse;
        JSONObject updatedResponses = new JSONObject();

        // Build queries
        try {
            formQuery = new CalculateDerivedValuesQuery(searchParameters).buildFormDefinitionQuery();
            responseQuery = new CalculateDerivedValuesQuery(searchParameters).buildGetResponsesQuery();
        } catch (Exception err) {
            log.error("Exception: " + err);
            return "{\"error\":\"Failed to build Form Defintion and/or Response Query for calculating derived values\"}";
        }

        // Send query to graphQL, get response and pass to update values
        try {
            qlFormResponse = qlService.qlSearch(formQuery);
            qlResponsesResponse = qlService.qlSearch(responseQuery);
            JSONObject qlFormResponseObject = new JSONObject(qlResponsesResponse);
            log.info("qlFormResponseObject = " + qlFormResponseObject.toString());
            if (qlFormResponseObject.getJSONObject("data").getJSONObject("allResponses").getJSONArray("nodes").isEmpty()) {
                return "{\"error\":\"No response data for this Reference/Period/Survey combination\"}";
            } else {
                updatedResponses = new CalculateDerivedValuesResponse(qlFormResponse, qlResponsesResponse)
                .updateDerivedQuestionResponses();
            }
        } catch (Exception err) {
            log.error("Exception: " + err);
            return "{\"error\":\"Failed to update Derived Question responses\"}";
        }
        
        // If no derived responses to calculate, dont't update responses
        if (updatedResponses.getJSONArray("responses").isEmpty()) {
            return "{\"continue\":\"No derived formulas to calculate\"}";
        };

        // Create new object with reference, period, survey and user included before saving
        JSONObject upsertResponses = new JSONObject();
        upsertResponses.put("reference", searchParameters.get("reference"));
        upsertResponses.put("period", searchParameters.get("period"));
        upsertResponses.put("survey", searchParameters.get("survey"));
        upsertResponses.put("responses", updatedResponses.getJSONArray("responses"));
        log.info("Upsert Responses: " + upsertResponses.toString());

        try {
            // Call to save updated derived responses
            //var upsertSaveResponse = new UpsertResponse(upsertResponses.toString());
            //var saveQuery = upsertSaveResponse.buildUpsertByArrayQuery();
            //qlService.qlSearch(saveQuery);
            saveResponses(upsertResponses.toString());
        } catch (Exception err) {
            log.error("Exception: " + err);
            return "{\"error\":\"Failed to save derived Question responses\"}";
        }
        return "{\"Success\":\"Successfully saved derived Question responses\"}";
        
    }
   
    @ApiOperation(value = "Save validation outputs", response = String.class)
    @RequestMapping(value = "/saveResponses", method = {RequestMethod.POST, RequestMethod.PUT})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful save of all question responses", response = String.class)})
    @ResponseBody
    public String saveResponses(String jsonString) {

        try {
            var upsertSaveResponse = new UpsertResponse(jsonString);
            var saveQuery = upsertSaveResponse.buildUpsertByArrayQuery();
            String saveResponseOutput = qlService.qlSearch(saveQuery);
            log.info("Output after saving the responses {}", saveResponseOutput);
        } catch (Exception err) {
            log.error("Failed to save responses: " + err);
            return "{\"error\":\"Failed to save Question responses\"}";
        }
        return "{\"Success\":\"Question responses saved successfully\"}";
    }

    @ApiOperation(value = "Save question responses", response = String.class)
    @RequestMapping(value = "/save/{vars}", method = {RequestMethod.POST, RequestMethod.PUT})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful in saving of all question responses", response = String.class)})
    @ResponseBody
    public void saveQuestionResponses(@RequestBody String updatedResponses) {

        log.info("API CALL!! --> /response/save :: Updated UI Responses" + updatedResponses);

        List<ResponseData>  currentResponseEntities;
        JSONObject updatedResponsesJson = new JSONObject(updatedResponses);
        CompareUiAndCurrentResponses responseComparison;

        try {
            var upsertResponse = new UpsertResponse(updatedResponses);
            var currentResponseQuery = upsertResponse.buildRetrieveOldResponseQuery();
            log.info("GraphQL Query for getting old responses:: " + currentResponseQuery);
            String qlResponseOutput = qlService.qlSearch(currentResponseQuery);
            log.info("Old Responses from GraphQL after database execution  :: " + qlResponseOutput);
            JSONObject qlResponseOutputJson = new JSONObject(qlResponseOutput);
            var outputArray = new JSONArray();
            outputArray = qlResponseOutputJson.getJSONObject("data").getJSONObject("allResponses")
                    .getJSONArray("nodes");
            log.info("Old Responses JSON Array :: " + outputArray);
            currentResponseEntities = upsertResponse.buildCurrentResponseEntities(outputArray);

            // Call Compare Responses
            responseComparison = new CompareUiAndCurrentResponses(currentResponseEntities, updatedResponsesJson);
            // Get only the updated responses and not the responses that were already in the
            // DB
            List<ResponseData> responsesToPassToDatabase = responseComparison.getFinalConsolidatedResponses();
            JSONArray jsonArray = new JSONArray(responsesToPassToDatabase);
            var upsertSaveResponse = new UpsertResponse(jsonArray);
            // Constructing GraphQL query for Save
            var saveQuery = upsertSaveResponse.buildConsolidateUpsertByArrayQuery();
            log.info("GraphQL query for save {}", saveQuery);
            // *** Extract out this call ? *** //
            String qlSaveResponseOutput = qlService.qlSearch(saveQuery);
            log.info("Output after saving the responses {}", qlSaveResponseOutput);
            // Updating the Form Status
            var contributorStatusQuery = upsertResponse.updateContributorStatus();
            log.info("GraphQL Query for updating Form Status {}", contributorStatusQuery);
            String qlStatusOutput = qlService.qlSearch(contributorStatusQuery);
            log.info("Output after updating the form status {}", qlStatusOutput);
            // Finally call to calculate derived values
            Map<String,String> refPerSur = new HashMap<>();
            refPerSur.put("reference", updatedResponsesJson.getString("reference"));
            refPerSur.put("period", updatedResponsesJson.getString("period"));
            refPerSur.put("survey", updatedResponsesJson.getString("survey"));
            calculateDerivedValues(refPerSur);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in saving :: " + e.getMessage());
        }
    }
}

package uk.gov.ons.collection.controller;

import java.util.Map;
import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import lombok.extern.log4j.Log4j2;
import uk.gov.ons.collection.service.BatchDataIngest;
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
import uk.gov.ons.collection.exception.ResponsesNotSavedException;
import uk.gov.ons.collection.service.ApiRequest;
import uk.gov.ons.collection.service.CompareUiAndCurrentResponses;

@Log4j2
@Api(value = "Response Controller")
@RestController
@RequestMapping(value = "/response")
public class ResponseController {

    final String businessLayerServicePort = "8088";
    final String protocol = "http://";

    @Autowired
    GraphQlService qlService;

    @ApiOperation(value = "Calculate derived question formulas", response = String.class)
    @RequestMapping(value = "/calculateDerivedQuestions/{vars}", method = { RequestMethod.POST, RequestMethod.PUT })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful calculation of all derived question responses", response = String.class) })
    @ResponseBody
    public String calculateDerivedValues(@MatrixVariable Map<String, String> searchParameters) throws IOException {

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
            if (qlFormResponseObject.getJSONObject("data").getJSONObject("allResponses").getJSONArray("nodes")
                    .isEmpty()) {
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
        }

        // Create new object with reference, period, survey and user included before
        // saving
        JSONObject upsertResponses = new JSONObject();
        upsertResponses.put("reference", searchParameters.get("reference"));
        upsertResponses.put("period", searchParameters.get("period"));
        upsertResponses.put("survey", searchParameters.get("survey"));
        upsertResponses.put("responses", updatedResponses.getJSONArray("responses"));
        log.info("Upsert Responses: " + upsertResponses.toString());

        try {
            // Call to save API to save updated derived responses
            InetAddress inetAddress = InetAddress.getLocalHost();
            log.info("IP Address:" + inetAddress.getHostAddress());
            String businessLayerAddress = inetAddress.getHostAddress();
            StringBuilder url = new StringBuilder(protocol).append(businessLayerAddress).append(":")
                    .append(businessLayerServicePort).append("/response/saveResponses");
            log.info("Request Url: " + url.toString());
            ApiRequest request = new ApiRequest(url.toString(), upsertResponses.toString());
            request.apiPostJson();
        } catch (Exception err) {
            log.error("Exception: " + err);
            return "{\"error\":\"Failed to save derived Question responses\"}";
        }
        return "{\"Success\":\"Successfully saved derived Question responses\"}";
    }

  
    @ApiOperation(value = "Save all responses", response = String.class)
    @RequestMapping(value = "/saveResponses", method = { RequestMethod.POST, RequestMethod.PUT })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful save of all question responses", response = String.class) })
    @ResponseBody
    public String saveResponses(@RequestBody String jsonString) throws ResponsesNotSavedException {
        try {
            var upsertSaveResponse = new UpsertResponse(jsonString);
            var saveQuery = upsertSaveResponse.buildUpsertByArrayQuery();
            log.info("GraphQL query for save {}", saveQuery);
            String saveResponseOutput = qlService.qlSearch(saveQuery);
            log.info("Output after saving the responses {}", saveResponseOutput);
        } catch (Exception err) {
            log.error("Failed to save responses: " + err);
            throw new ResponsesNotSavedException("Failed to save responses" + err);
        }
        return "{\"Success\":\"Question responses saved successfully\"}";
    }

    @ApiOperation(value = "Save question responses", response = String.class)
    @RequestMapping(value = "/save/{vars}", method = { RequestMethod.POST, RequestMethod.PUT })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful in saving of all question responses", response = String.class) })
    @ResponseBody
    public String saveQuestionResponses(@RequestBody String updatedResponses) {

        log.info("API CALL!! --> /response/save :: Updated UI Responses" + updatedResponses);

        List<ResponseData> currentResponseEntities;
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
            // If no responses to pass to database, don't call save function
            if (responsesToPassToDatabase.isEmpty()) {
                return "{\"continue\":\"No question responses to save\"}";
            }
            // Calling common Save
            InetAddress inetAddress = InetAddress.getLocalHost();
            log.info("IP Address:" + inetAddress.getHostAddress());
            String businessLayerAddress = inetAddress.getHostAddress();
            StringBuilder url = new StringBuilder(protocol).append(businessLayerAddress).append(":")
                    .append(businessLayerServicePort).append("/response/saveResponses");
            log.info("Request Url: " + url.toString());
            ApiRequest request = new ApiRequest(url.toString(),
                    upsertResponse.processConsolidatedJsonList(responsesToPassToDatabase, updatedResponses));
            request.apiPostJson();
            // Updating the Form Status
            var contributorStatusQuery = upsertResponse.updateContributorStatus();
            log.info("GraphQL Query for updating Form Status {}", contributorStatusQuery);
            String qlStatusOutput = qlService.qlSearch(contributorStatusQuery);
            log.info("Output after updating the form status {}", qlStatusOutput);
            // Finally call to calculate derived values
            StringBuilder derivedUrl = new StringBuilder(protocol).append(businessLayerAddress).append(":")
                    .append(businessLayerServicePort).append("/response/calculateDerivedQuestions/")
                    .append("reference=").append(updatedResponsesJson.getString("reference")).append(";period=")
                    .append(updatedResponsesJson.getString("period")).append(";survey=")
                    .append(updatedResponsesJson.getString("survey")).append(";");
            ApiRequest derivedRequest = new ApiRequest(derivedUrl.toString());
            log.info("Request Url: " + derivedUrl.toString());
            derivedRequest.apiPostParameters();

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error in saving :: " + e.getMessage());
            return "{\"error\":\"Failed to save Question responses\"}";
        }
        return "{\"Success\":\"Question responses saved successfully\"}";
    }


    @ApiOperation(value = "Save batch/PCK responses", response = String.class)
    @RequestMapping(value = "/saveBatchResponses", method = { RequestMethod.POST, RequestMethod.PUT })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful save of all Batch question responses", response = String.class) })
    @ResponseBody
    public String saveBatchResponses(@RequestBody String batchResponses) {

        var outcomesObj = new JSONObject();
        var outcomesArr = new JSONArray();
        try {
            BatchDataIngest batchData = new BatchDataIngest(batchResponses, qlService);
            batchData.processBatchData(outcomesArr);
        } catch (Exception e) {
            log.info("Can't build Batch Data Query / Invalid Response from GraphQL: " + e);
            return "{\"error\":\"Failed to save Batch Question responses\"}";
        }
        outcomesObj.put("outcomes", outcomesArr);
        return  outcomesObj.toString();
    }
}

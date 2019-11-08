package uk.gov.ons.collection.controller;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.log4j.Log4j2;
import uk.gov.ons.collection.service.GraphQlService;
import uk.gov.ons.collection.utilities.UpsertResponse;
import uk.gov.ons.collection.utilities.calculateDerivedValuesQuery;
import uk.gov.ons.collection.utilities.calculateDerviedValuesResponse;
@Log4j2
@Api(value = "Response Controller")
@RestController
@RequestMapping(value = "/response")
public class ResponseController {
   @Autowired
   GraphQlService qlService;

   @ApiOperation(value = "Save validation outputs", response = String.class)
   @RequestMapping(value = "/calculateDerivedQuestions/{vars}", method = {RequestMethod.POST, RequestMethod.PUT})
   @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful calculation of all derived question responses", response = String.class)})
   @ResponseBody
    public String calculateDerivedValues(@MatrixVariable Map<String, String> searchParameters) {

        String formQuery = new String();
        String responseQuery = new String();
        String qlFormResponse;
        String qlResponsesResponse;
        JSONObject updatedResponses = new JSONObject();

        // Build queries
        try {
            formQuery = new calculateDerivedValuesQuery(searchParameters).buildFormDefinitionQuery();
            responseQuery = new calculateDerivedValuesQuery(searchParameters).buildGetResponsesQuery();
        } catch (Exception e) {
            return "{\"error\":\"Failed to build Form Defintion and/or Response Query for calculating derived values\"}";
        }

        // Send query to graphQL, get response and pass to update values
        try {
            qlFormResponse = qlService.qlSearch(formQuery);
            qlResponsesResponse = qlService.qlSearch(responseQuery);
            updatedResponses = new calculateDerviedValuesResponse(qlFormResponse, qlResponsesResponse).updateDerivedQuestionResponses();
        } catch (Exception err) {
            return "{\"error\":\"Failed to update Derived Question responses\"}";
        }

        
        // Create new object with reference, period, survey and user included before sending over?
        return updatedResponses.toString();

    }
   
}
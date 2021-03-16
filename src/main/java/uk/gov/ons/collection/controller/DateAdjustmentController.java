package uk.gov.ons.collection.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.MatrixVariable;

import uk.gov.ons.collection.entity.DateAdjustmentQuery;
import uk.gov.ons.collection.entity.DateAdjustmentResponse;
import uk.gov.ons.collection.service.GraphQlService;

import java.util.Map;

@Log4j2
@Api(value = "Date Adjustment Controller", description = "Entry points primarily involving date adjustment")
@RestController
@RequestMapping(value = "/dateadjustment")
public class DateAdjustmentController {

    @Autowired
    GraphQlService qlService;

    @ApiOperation(value = "Get all date adjustment data", response = String.class)
    @GetMapping(value = "/loadconfigdata/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful retrieval of selective editing data", response = String.class)})
    public String loadDateAdjustmentData(@MatrixVariable Map<String, String> params) {
        log.info("API CALL!! --> /dateadjustment/loadconfigdata/{vars} :: " + params);
        String response = "";
        DateAdjustmentQuery dateAdjustmentQuery = null;

        try {
            dateAdjustmentQuery = new DateAdjustmentQuery(params);
            String queryStr = dateAdjustmentQuery.buildDateAdjustmentConfigQuery();
            log.debug("Date Adjustment configuration GraphQL query: " + queryStr);
            String dateAdjustmentQueryOutput = qlService.qlSearch(queryStr);
            log.debug("Date Adjustment Query Output: " + dateAdjustmentQueryOutput);
            DateAdjustmentResponse dateAdjustmentResponse = new DateAdjustmentResponse(dateAdjustmentQueryOutput);
            response = dateAdjustmentResponse.parseDateAdjustmentQueryResponse();
            log.info("Date Adjustment Response before sending to lambda: " + response);
        } catch (Exception err) {
            log.error("Exception found in Date Adjustment: " + err.getMessage());
            String message = processJsonErrorMessage(err);
            response = "{\"error\":\"Unable to load DateAdjustment config data " + message + "\"}";
        }

        log.info("API Complete!! --> /dateadjustment/loadconfigdata");

        return response;
    }

    @ApiOperation(value = "Save date adjustment calculation output", response = String.class)
    @RequestMapping(value = "/saveOutput", method = { RequestMethod.POST, RequestMethod.PUT })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful save of date adjustment calculation outputs", response = String.class) })
    @ResponseBody
    public String saveDateAdjustmentOutput(@RequestBody String jsonString)  {

        try {
            log.info("API CALL!! --> /dateadjustment/saveOutput :: " + jsonString);
            DateAdjustmentResponse dateAdjustmentResponse = new DateAdjustmentResponse(jsonString);
            String saveQuery = dateAdjustmentResponse.buildSaveDateAdjustmentQuery();
            log.debug("GraphQL query for Date Adjustment save {}", saveQuery);
            String saveResponseOutput = qlService.qlSearch(saveQuery);
            log.debug("Output after saving the Date Adjustment outputs {}", saveResponseOutput);
            log.info("API Complete!! --> /selectiveediting/saveOutput");
        } catch (Exception err) {
            log.error("Exception found in Date Adjustment: " + err.getMessage());
            String message = processJsonErrorMessage(err);
            return "{\"error\":\"Unable to save or update date adjustment data " + message + "\"}";
        }
        return "{\"Success\":\"Date Adjustment data saved successfully\"}";

    }

    private String processJsonErrorMessage(Exception err) {

        String message = err.getMessage() != null ? err.getMessage().replace("\"","'") : "";
        return message;
    }
}

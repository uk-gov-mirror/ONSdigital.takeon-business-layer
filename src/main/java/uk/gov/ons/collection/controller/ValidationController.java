package uk.gov.ons.collection.controller;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;

import uk.gov.ons.collection.entity.ContributorStatus;
import uk.gov.ons.collection.entity.DataPrepConfig;
import uk.gov.ons.collection.entity.ValidationOutputs;
import uk.gov.ons.collection.exception.InvalidJsonException;
import uk.gov.ons.collection.service.GraphQlService;
import uk.gov.ons.collection.utilities.QlQueryBuilder;
import uk.gov.ons.collection.utilities.QlQueryResponse;


@Log4j2
@Api(value = "Validation Controller", description = "Entry points primarily involving validation queries")
@RestController
@RequestMapping(value = "/validation")
public class ValidationController {

    @Autowired
    GraphQlService qlService;

    @ApiOperation(value = "Get all validation config & response data", response = String.class)
    @GetMapping(value = "/getAllConfiguration/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful retrieval of all details", response = String.class)})
    public String dataPrepConfig(@MatrixVariable Map<String, String> params) {

        log.info("API CALL!! --> /validation/getAllConfiguration/{vars} :: " + params);
        String response;
        try {
            response = new DataPrepConfig(params.get("reference"), params.get("period"), params.get("survey"), qlService).load();
        } catch (InvalidJsonException err) {
            log.info("Exception found: " + err);
            response = "{\"error\":\"Unable to determine or construct configuration data\"}";
        }
        log.info("API Complete!! --> /validation/getAllConfiguration/{vars}");
        return response;
    }


    @ApiOperation(value = "Save validation outputs", response = String.class)
    @RequestMapping(value = "/saveOutputs", method = {RequestMethod.POST, RequestMethod.PUT})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful saving of all validation outputs", response = String.class)})
    @ResponseBody
    public String saveValidationOutputs(@RequestBody String validationOutputsJson, Errors errors) {
        log.info("API CALL!! --> /validation/saveOutputs :: " + validationOutputsJson);

        ValidationOutputs outputs;
        String period;
        String reference;
        String survey;
        String statusText;
        String deleteQuery;
        String insertQuery;

        // 1 - Convert params to JSON Object and extract Reference | Period | Survey
        try {
            outputs = new ValidationOutputs(validationOutputsJson);
            reference = outputs.getReference();
            period = outputs.getPeriod();
            survey = outputs.getSurvey();
            statusText = outputs.getStatusText();
            deleteQuery = outputs.buildDeleteOutputQuery();
            insertQuery = outputs.buildInsertByArrayQuery();
        } catch (Exception e) {
            log.info("Exception caught: " + e);
            return "{\"error\":\"Unable to resolve validation output JSON\"}";
        }

        String qlResponse = new String();

        // 1: Delete outputs
        try {
            qlResponse = qlService.qlSearch(deleteQuery);
        } catch (Exception e) {
            log.info("Exception: " + e);
            log.info("Delete: " + deleteQuery);
            log.info("QL Response: " + qlResponse);
            return "{\"error\":\"Error removing existing validation outputs\"}";
        }

        // 2: Insert outputs
        try {
            qlResponse = qlService.qlSearch(insertQuery);
        } catch (Exception e) {
            log.info("Exception: " + e);
            log.info("Insert: " + insertQuery);
            log.info("QL Response: " + qlResponse);
            return "{\"error\":\"Error saving validation outputs\"}";
        }

        // 3: Update status
        String updateStatusQuery = "";
        try {
            updateStatusQuery = new ContributorStatus(reference, period, survey, statusText).buildUpdateQuery();
            qlResponse = qlService.qlSearch(updateStatusQuery);
        } catch (Exception e) {
            log.info("Exception: " + e);
            log.info("Update: " + insertQuery);
            log.info("QL Response: " + updateStatusQuery);
            return "{\"error\":\"Error updating contributor status\"}";
        }

        log.info("API CALL!! --> /validation/saveOutputs :: Complete");
        return "{\"status\": \"Success\"}";
    }

    @ApiOperation(value = "Validation output to UI", response = String.class)
    @GetMapping(value = "/validationoutput/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful", response = String.class)})
    public String validationoutput(@MatrixVariable Map<String, String> searchParameters) {
        String validationOutputsQuery = "";
        JSONObject validationOutputs = new JSONObject();
        try {
            validationOutputsQuery = new QlQueryBuilder(searchParameters).buildValidationOutputQuery();
            QlQueryResponse response = new QlQueryResponse(qlService.qlSearch(validationOutputsQuery));
            validationOutputs = response.parseValidationOutputs();
        } catch (Exception e) {
            log.info("Exception: " + e);
            log.info("Validation Outputs Query: " + validationOutputsQuery);
            return "{\"error\":\"Error building Validation Outputs Query\"}";
        }
        log.info("Validation Outputs Query: " + validationOutputs.toString());
        return validationOutputs.toString();
    }

}
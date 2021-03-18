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
import uk.gov.ons.collection.entity.ValidationOutputData;
import uk.gov.ons.collection.entity.ValidationOutputs;
import uk.gov.ons.collection.exception.InvalidJsonException;
import uk.gov.ons.collection.service.GraphQlService;
import uk.gov.ons.collection.service.ValidationOverrideService;
import uk.gov.ons.collection.utilities.QlQueryBuilder;
import uk.gov.ons.collection.utilities.QlQueryResponse;
import java.util.List;


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
            log.error("Exception found: " + err.getMessage());
            response = "{\"error\":\"Unable to determine or construct configuration data\"}";
        }
        log.debug("getAllConfiguration response: " + response);
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
        String qlResponse;

        try {
            outputs = new ValidationOutputs(validationOutputsJson);
            reference = outputs.getReference();
            period = outputs.getPeriod();
            survey = outputs.getSurvey();
            String validationOutputQuery = outputs.buildValidationOutputQuery();
            qlResponse = qlService.qlSearch(validationOutputQuery);
            log.debug("Output from Validation Output table " + qlResponse);
            List<ValidationOutputData> validationOutputData = outputs.extractValidationDataFromDatabase(qlResponse);
            List<ValidationOutputData> lambdaValidationOutputData = outputs.extractValidationDataFromLambda();
            int overriddenTrueCount =
                    outputs.getOverriddenTrueCount(lambdaValidationOutputData, validationOutputData);
            log.debug("Override True Count " + overriddenTrueCount);
            int triggeredTrueCount = outputs.getTriggerTrueCount();
            log.debug("Triggered True Count " + triggeredTrueCount);
            statusText = outputs.getStatusText(triggeredTrueCount, overriddenTrueCount);
            log.debug("Status Text after evaluation of trigger count and overridden count" + statusText);

            List<ValidationOutputData> validationOutputDeleteData =
                    outputs.getDeleteValidationOutputList(lambdaValidationOutputData, validationOutputData);
            List<ValidationOutputData> validationOutputInsertData =
                    outputs.getValidationOutputInsertList(lambdaValidationOutputData, validationOutputData);
            List<ValidationOutputData> validationOutputModifiedData =
                    outputs.getValidationOutputModifiedList(lambdaValidationOutputData, validationOutputData);
            List<ValidationOutputData> validationOutputUpsertData =
                    outputs.getValidationOutputUpsertList(validationOutputModifiedData, validationOutputInsertData);
            String upsertAndDeleteQuery = outputs.buildUpsertByArrayQuery(validationOutputUpsertData, validationOutputDeleteData);
            qlResponse = qlService.qlSearch(upsertAndDeleteQuery);
            log.debug("Upsert Query response " + qlResponse);
        } catch (Exception e) {
            log.error("Exception caught: " + e.getMessage());
            return "{\"error\":\"Unable to save ValidationOutputs\"}";
        }

        // 3: Update status
        String updateStatusQuery = "";
        try {
            updateStatusQuery = new ContributorStatus(reference, period, survey, statusText).buildUpdateQuery();
            qlResponse = qlService.qlSearch(updateStatusQuery);
            log.debug("Update Status Query response " + qlResponse);
        } catch (Exception e) {
            log.error("Exception: " + e.getMessage());
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
        log.info("API CALL!! --> /validation/validationoutput :: ");
        try {
            validationOutputsQuery = new QlQueryBuilder(searchParameters).buildValidationOutputQuery();
            log.debug("Validation Outputs Query: " + validationOutputsQuery);
            QlQueryResponse response = new QlQueryResponse(qlService.qlSearch(validationOutputsQuery));
            validationOutputs = response.parseValidationOutputs();
        } catch (Exception e) {
            log.error("Exception in Validation Output: " + e.getMessage());
            return "{\"error\":\"Error building Validation Outputs Query\"}";
        }
        log.info("API CALL!! --> /validation/validationoutput :: Complete");
        return validationOutputs.toString();
    }

    @ApiOperation(value = "Save all overrides", response = String.class)
    @RequestMapping(value = "/saveOverrides", method = { RequestMethod.POST, RequestMethod.PUT })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful save of all validation overrides", response = String.class) })
    @ResponseBody
    public String saveOverrides(@RequestBody String jsonString)  {

        log.info("API CALL!! --> /validation/saveOverrides :: Save Validation overrides" + jsonString);
        String response = "";
        try {

            ValidationOverrideService validationService = new ValidationOverrideService(jsonString, qlService);
            response = validationService.processValidationDataAndSave();
            log.debug("Response after saving Overrides {} ", response);
        } catch (Exception err) {
            log.error("Failed to save validation overrides: " + err.getMessage());
            return "{\"error\":\"Error in saving validation overrides\"}";
        }
        log.info("API CALL!! --> /validation/saveOverrides :: Complete");
        return response;
    }

}
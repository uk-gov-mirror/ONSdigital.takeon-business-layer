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
import uk.gov.ons.collection.entity.HistoryDetailsQuery;
import uk.gov.ons.collection.entity.HistoryDetailsResponse;
import uk.gov.ons.collection.entity.SelectiveEditingQuery;
import uk.gov.ons.collection.entity.SelectiveEditingResponse;
import uk.gov.ons.collection.service.GraphQlService;

import java.util.List;
import java.util.Map;

@Log4j2
@Api(value = "Selective Editing Controller", description = "Entry points primarily involving selective editing")
@RestController
@RequestMapping(value = "/selectiveediting")
public class SelectiveEditingController {
    @Autowired
    GraphQlService qlService;

    @ApiOperation(value = "Get all selective editing data", response = String.class)
    @GetMapping(value = "/loadconfigdata/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful retrieval of selective editing data", response = String.class)})
    public String loadSelectiveEditingData(@MatrixVariable Map<String, String> params) {

        log.info("API CALL!! --> /selectiveediting/loadconfigdata/{vars} :: " + params);
        String response = "";
        SelectiveEditingQuery selectiveEditingQuery = null;
        HistoryDetailsQuery historyDetailsQuery = null;
        String periodicityStr = "";
        String currentPeriod = "";
        HistoryDetailsResponse responsePeriodicity = null;

        try {
            historyDetailsQuery = new HistoryDetailsQuery(params);
            //periodicity
            String periodicityQuery = historyDetailsQuery.buildSurveyPeriodicityQuery();
            log.debug(" Periodicity GraphQL Query: " + periodicityQuery);
            String periodicityQueryOutput = qlService.qlSearch(periodicityQuery);
            log.debug(" GraphQL Output for Periodicity: " + periodicityQueryOutput);
            responsePeriodicity = new HistoryDetailsResponse(periodicityQueryOutput);
            periodicityStr = responsePeriodicity.parsePeriodicityFromSurvey();
            log.debug(" Periodicity from Survey table: " + periodicityStr);
            currentPeriod = historyDetailsQuery.retrieveCurrentPeriod();
            log.debug("Current Period from UI: " + currentPeriod);
        } catch (Exception err) {
            log.error("Exception found in loading Periodicity: " + err.getMessage());
            String message = processJsonErrorMessage(err);
            response = "{\"error\":\"Unable to load Periodicity data from database " + message + "\"}";
        }

        try {

            List<String> historyPeriodList = responsePeriodicity.getCurrentAndPreviousHistoryPeriod(currentPeriod, periodicityStr);
            log.debug("Final History Periods: " + historyPeriodList.toString());
            if (historyPeriodList.size() > 0) {
                selectiveEditingQuery = new SelectiveEditingQuery(params);
                String queryStr = selectiveEditingQuery.buildSelectiveEditingLoadConfigQuery(historyPeriodList);
                log.debug("Selective Editing configuration GraphQL query: " + queryStr);
                String selectiveEditingQueryOutput = qlService.qlSearch(queryStr);
                log.debug("Selective Editing Query Output: " + selectiveEditingQueryOutput);
                SelectiveEditingResponse selectiveEditingResponse = new SelectiveEditingResponse(selectiveEditingQueryOutput);
                response = selectiveEditingResponse.parseSelectiveEditingQueryResponse();
                log.info("Selective Editing Response before sending to lambda: " + response);
            }

        } catch (Exception err) {
            log.error("Exception found in Selective Editing: " + err.getMessage());
            String message = processJsonErrorMessage(err);
            response = "{\"error\":\"Unable to load selective editing config data " + message + "\"}";
        }
        log.info("API Complete!! --> /selectiveediting/loadconfigdata");
        return response;
    }

    @ApiOperation(value = "Save selective editing calculation outputs", response = String.class)
    @RequestMapping(value = "/saveOutput", method = { RequestMethod.POST, RequestMethod.PUT })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful save of selective editing calculation outputs", response = String.class) })
    @ResponseBody
    public String saveSelectiveEditingOutput(@RequestBody String jsonString)  {


        try {
            log.info("API CALL!! --> /selectiveediting/saveOutput :: " + jsonString);
            SelectiveEditingResponse selectiveEditingResponse = new SelectiveEditingResponse(jsonString);
            String saveQuery = selectiveEditingResponse.buildUpsertQuery();
            log.debug("GraphQL query for selective editing save {}", saveQuery);
            String saveResponseOutput = qlService.qlSearch(saveQuery);
            log.debug("Output after saving the selective editing outputs {}", saveResponseOutput);
            log.info("API Complete!! --> /selectiveediting/saveOutput");
        } catch (Exception err) {
            log.error("Exception found in Selective Editing: " + err.getMessage());
            String message = processJsonErrorMessage(err);
            return "{\"error\":\"Unable to save or update selective editing data " + message + "\"}";
        }
        return "{\"Success\":\"Selective editing outputs saved successfully\"}";
    }

    private String processJsonErrorMessage(Exception err) {

        String message = err.getMessage() != null ? err.getMessage().replace("\"","'") : "";
        return message;
    }
}

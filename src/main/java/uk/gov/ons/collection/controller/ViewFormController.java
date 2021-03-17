package uk.gov.ons.collection.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.collection.entity.HistoryDetailsQuery;
import uk.gov.ons.collection.entity.HistoryDetailsResponse;
import uk.gov.ons.collection.entity.ViewFormQuery;
import uk.gov.ons.collection.entity.ViewFormResponse;
import uk.gov.ons.collection.service.GraphQlService;

import java.util.List;
import java.util.Map;

@Log4j2
@Api(value = "View Form Controller", description = "Entry point for View Form")
@RestController
@RequestMapping(value = "/viewform")
public class ViewFormController {
   
@Autowired
GraphQlService qlService;

    @GetMapping(value = "/responses/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful retrieval of Contributor details", response = String.class)})
    public String viewFormDetails(@MatrixVariable Map<String, String> searchParameters) {
    String qlQuery = new ViewFormQuery(searchParameters).buildViewFormQuery();
    String responseText;
    log.debug("Query sent to service: " + qlQuery);
    log.info("Calling viewFormDetails API: " + searchParameters);
    try {
        var response = new ViewFormResponse(qlService.qlSearch(qlQuery));
        responseText = response.combineFormAndResponseData();
    } catch (Exception e) {
        log.error("Exception caught: " + e.getMessage());
        responseText = "{\"error\":\"Invalid response from graphQL\"}";
    }
    log.debug("Query sent to service: " + qlQuery);
    log.info("ViewFormDetails API Complete: ");
    return responseText;
    }

    @GetMapping(value = "/historydata/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of History details", response = String.class)})
    public String viewHistoryDetails(@MatrixVariable Map<String, String> searchParameters) {
        log.info("Calling History details API: " + searchParameters);
        String periodicityStr = "";
        String responseText = "";
        HistoryDetailsResponse responsePeriodicity = null;
        HistoryDetailsQuery  detailsQuery = null;
        String currentPeriod = "";
        try {
            detailsQuery = new HistoryDetailsQuery(searchParameters);
            String qlPeriodicityQuery = detailsQuery.buildSurveyPeriodicityQuery();
            log.debug("Survey Periodicity Query: " + qlPeriodicityQuery);
            String qlResponsePeriodicity = qlService.qlSearch(qlPeriodicityQuery);
            log.debug("Graph QL Response for periodicity: " + qlResponsePeriodicity);
            responsePeriodicity = new HistoryDetailsResponse(qlResponsePeriodicity);
            periodicityStr = responsePeriodicity.parsePeriodicityFromSurvey();
            log.debug(" Periodicity from Survey table: " + periodicityStr);
            currentPeriod = detailsQuery.retrieveCurrentPeriod();
            log.debug("Current Period from UI: " + currentPeriod);
        } catch (Exception e) {
            e.printStackTrace();
            responseText = "{\"error\":\"Problem in getting Periodicity " + e.getMessage() + "\"}";
        }
        try {
            List<String> historyPeriodList = responsePeriodicity.getHistoryPeriods(currentPeriod, periodicityStr);
            log.debug("Final History Periods: " + historyPeriodList.toString());
            if (historyPeriodList.size() > 0) {
                String historyQuery = detailsQuery.buildHistoryDetailsQuery(historyPeriodList);
                log.debug("History Details Query: " + historyQuery);
                String historyDetailsResponse = qlService.qlSearch(historyQuery);
                log.debug("History Details Response: " + historyDetailsResponse);
                responseText = responsePeriodicity.parseHistoryDataResponses(historyDetailsResponse);
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseText = "{\"error\":\"Problem in getting History data " + e.getMessage() + "\"}";
        }
        log.debug("History data before sending to UI: " + responseText);
        log.info("History details API Complete: " );
        return responseText;
    }


}



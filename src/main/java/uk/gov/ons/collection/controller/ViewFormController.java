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
    log.info("Query sent to service: " + qlQuery);
    try {
        var response = new ViewFormResponse(qlService.qlSearch(qlQuery));
        responseText = response.combineFormAndResponseData();
    } catch (Exception e) {
        responseText = "{\"error\":\"Invalid response from graphQL\"}";
    }
    log.info("Query sent to service: " + qlQuery);     
    return responseText;
    }

    @GetMapping(value = "/historydata/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of History details", response = String.class)})
    public String viewHistoryDetails(@MatrixVariable Map<String, String> searchParameters) {
        HistoryDetailsQuery  detailsQuery = new HistoryDetailsQuery(searchParameters);
        String qlPeriodicityQuery = detailsQuery.buildSurveyPeriodicityQuery();
        String periodicityStr;
        String responseText = "";
        log.info("Survey Periodicity Query: " + qlPeriodicityQuery);
        try {
            String qlResponsePeriodicity = qlService.qlSearch(qlPeriodicityQuery);
            log.info ("Graph QL Response for periodicity: " +qlResponsePeriodicity);
            HistoryDetailsResponse responsePeriodicity = new HistoryDetailsResponse(qlResponsePeriodicity);
            periodicityStr = responsePeriodicity.parsePeriodicityFromSurvey();
            log.info(" Periodicity from Survey table: "+periodicityStr);
            String currentPeriod = detailsQuery.retrieveCurrentPeriod();
            log.info ("Current Period from UI: " + currentPeriod);

            List<String> historyPeriodList = responsePeriodicity.getHistoryPeriods(currentPeriod, periodicityStr);
            log.info ("Final History Periods: " + historyPeriodList.toString());
        } catch (Exception e) {
            e.printStackTrace();
            responseText = "{\"error\":\"Invalid response from graphQL\"}";
        }
        log.info("History data before sending to UI: " + responseText);
        return responseText;
    }


}



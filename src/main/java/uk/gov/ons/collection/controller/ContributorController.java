package uk.gov.ons.collection.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.collection.entity.ContributorSelectiveEditingStatusQuery;
import uk.gov.ons.collection.entity.ContributorSelectiveEditingStatusResponse;
import uk.gov.ons.collection.entity.FullDataExport;
import uk.gov.ons.collection.service.GraphQlService;
import uk.gov.ons.collection.utilities.QlQueryBuilder;
import uk.gov.ons.collection.utilities.QlQueryResponse;
import uk.gov.ons.collection.utilities.SelectionFileQuery;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;


@Log4j2
@Api(value = "Contributor Controller", description = "End point for the repository data related to contributors")
@RestController
@RequestMapping(value = "/contributor")
public class ContributorController {

    @Autowired
    GraphQlService qlService;

    @GetMapping(value = "/qlSearch/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of Contributor details")})
    public String searchContributor(@MatrixVariable Map<String, String> searchParameters) {
        final String qlQuery = new QlQueryBuilder(searchParameters).buildContributorSearchQuery();
        String responseText;
        try {
            log.info("API CALL!! --> /contributor/qlSearch/{vars} :: " + searchParameters);
            final QlQueryResponse response = new QlQueryResponse(qlService.qlSearch(qlQuery));
            responseText = response.parse();
            log.debug("Contributor search response before sending to UI {} ", responseText);
            log.info("API Complete!! --> /contributor/qlSearch/{vars}");
        } catch (Exception e) {
            log.error("There is a problem in contributor search and the cause is {} ", e.getMessage());
            responseText = "{\"error\":\"Invalid response from graphQL\"}";
        }
        return responseText;
    }


    @ApiOperation(value = "Initial export of all database contents for results consumption", response = String.class)
    @RequestMapping(value = "/dbExport", produces = MediaType.APPLICATION_JSON_VALUE, method = { RequestMethod.POST, RequestMethod.PUT })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful export of database contents", response = String.class) })
    @ResponseBody
    public String fullDataExport(@RequestBody String snapshotInputJson) {
        String response = "";
        log.info("API CALL!! --> /contributor/dbExport:: " + snapshotInputJson);
        Map<String, List<String>> snapshotMap = null;
        FullDataExport dataExport = null;
        Set<String> uniqueSurveyList = null;
        String snapshotQuery = null;
        try {
            log.info("-------Start Memory Details before calling snapshot-----");
            verifyMemoryFootPrint();
            log.info("-------End Memory Details before calling snapshot-----");
            dataExport = new FullDataExport(snapshotInputJson);
            uniqueSurveyList = dataExport.getUniqueSurveyList();
            log.debug("Unique Survey List: " + uniqueSurveyList.toString());
            snapshotMap = dataExport.retrieveSurveyAndPeriodListFromSnapshotInput(uniqueSurveyList);
            log.debug("Hash Map containing survey and period list: " + snapshotMap.toString());
            snapshotQuery = dataExport.buildMultipleSurveyPeriodSnapshotQuery(uniqueSurveyList, snapshotMap);
            response = qlService.qlSearch(snapshotQuery);
            dataExport.verifyEmptySnapshot(response);

        } catch (Exception e) {
            log.error("Exception in loading data for db Export " + e.getMessage());
            String message = e.getMessage() != null ? e.getMessage().replace("\"","'") : "";
            log.debug("Error message after parsing:" + message);
            response =  "{\"error\":\"Error loading data for db Export " + message + "\"}";
        } finally {

            snapshotQuery = null;
            if (snapshotMap != null) {
                snapshotMap.clear();
            }
            if (uniqueSurveyList != null) {
                uniqueSurveyList.clear();
            }

            if (dataExport != null) {
                JSONObject jsonObject = dataExport.getJsonSurveySnapshotInput();
                if (jsonObject != null) {
                    jsonObject = null;
                }
                dataExport = null;
            }

            log.info("-------Start Memory Details after calling snapshot-----");
            try {
                verifyMemoryFootPrint();
            } catch (Exception exceptionmemoryprint) {
                log.error(exceptionmemoryprint.getMessage());
            }
            log.info("-------End Memory Details after calling snapshot-----");

        }
        return response;
    }

    private void verifyMemoryFootPrint() throws Exception {
        int mb = 1024 * 1024;

        // get Runtime instance
        Runtime instance = Runtime.getRuntime();

        log.info("***** Heap utilization statistics [MB] *****\n");

        // available memory
        log.info("Total Memory: " + instance.totalMemory() / mb);

        // free memory
        log.info("Free Memory: " + instance.freeMemory() / mb);

        // used memory
        log.info("Used Memory: "
                + (instance.totalMemory() - instance.freeMemory()) / mb);

        // Maximum available memory
        log.info("Max Memory: " + instance.maxMemory() / mb);
    }


    @ApiOperation(value = "Load Selection File for a Survey/Period", response = String.class)
    @RequestMapping(value = "/loadSelectionFile", produces = MediaType.APPLICATION_JSON_VALUE, method = { RequestMethod.POST, RequestMethod.PUT })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful load of Selection file", response = String.class) })
    @ResponseBody
    public String loadSelectionFile(@RequestBody String selectionData) {
        log.info("API CALL!! --> /contributor/loadSelectionFile:: " + selectionData);
        try {
            SelectionFileQuery fileQuery =  new SelectionFileQuery(selectionData, qlService);
            String loadQuery = fileQuery.buildSaveSelectionFileQuery();
            String  response = qlService.qlSearch(loadQuery);
            //Process if any GraphQL exception
            String message = fileQuery.processGraphQlErrorMessage(response);
            if (message != null && message.length() > 0) {
                return "{\"error\":\"Failed to load Selection File " + message + " \"}";
            }
            log.info("API Complete!! --> /contributor/loadSelectionFile");

        } catch (Exception e) {
            log.error("Can't build Batch Selection Load Query / Invalid Response from GraphQL: " + e.getMessage());
            return "{\"error\":\"Failed to load Selection File " + e.getMessage() + " \"}";
        }

        return "{\"Success\":\"Successfully loaded Selection File\"}";
    }

    @GetMapping(value = "/delayResponse", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of Survey/Period details")})
    public String delayResponse() {
        String delayResponseQuery = new QlQueryBuilder().buildDelayResponseQuery();
        //log.info("GraphQL Query sent to service: " + delayResponseQuery);
        JSONObject responseText;
        String response;
        String output;
        try {
            log.info("API CALL!! --> /contributor/delayResponse:: ");
            response = qlService.qlSearch(delayResponseQuery);
            responseText = new QlQueryResponse().buildDelayResponseOutput(response);
            output = responseText.toString();
            log.debug("Output - Survey Period details {}", output);
            log.info("API Complete!! --> /contributor/delayResponse");
        } catch (Exception e) {
            output = "{\"error\":\"Invalid response from graphQL\"}";
        }
        return output;
    }

    @GetMapping(value = "/statuses/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of Contributor statuses")})
    public String getContributorStatuses(@MatrixVariable Map<String, String> params) {

        log.info("API CALL!! --> /contributor/statuses/{vars} :: " + params);
        String response = "";
        ContributorSelectiveEditingStatusQuery contributorStatusQuery = null;

        try {
            contributorStatusQuery = new ContributorSelectiveEditingStatusQuery(params);
            String queryStr = contributorStatusQuery.buildContributorSelectiveEditingStatusQuery();
            log.debug("ContributorstatusQuery GraphQL query: " + queryStr);
            String contributorstatusQueryOutput = qlService.qlSearch(queryStr);
            log.debug("Contributor Status Query Output: " + contributorstatusQueryOutput);
            ContributorSelectiveEditingStatusResponse contributorStatusResponse =
                    new ContributorSelectiveEditingStatusResponse(contributorstatusQueryOutput);
            JSONObject contributorJsonObject = contributorStatusResponse.parseContributorStatusQueryResponse();
            response = contributorJsonObject.toString();
            log.debug("Contributor Status before sending to lambda: " + response);
        } catch (Exception err) {
            log.error("Exception found in Contributor Status API: " + err.getMessage());
            String message = processJsonErrorMessage(err);
            response = "{\"error\":\"Unable to Contributor Status data " + message + "\"}";
        }

        log.info("API Complete!! --> /contributor/statuses/{vars}");
        return response;

    }

    private String processJsonErrorMessage(Exception err) {

        String message = err.getMessage() != null ? err.getMessage().replace("\"","'") : "";
        return message;
    }
}

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
import uk.gov.ons.collection.entity.FullDataExport;
import uk.gov.ons.collection.service.GraphQlService;
import uk.gov.ons.collection.utilities.QlQueryBuilder;
import uk.gov.ons.collection.utilities.QlQueryResponse;
import uk.gov.ons.collection.utilities.SelectionFileQuery;

import java.util.List;
import java.util.Map;
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
        log.info("Query sent to service: " + qlQuery);
        try {
            final QlQueryResponse response = new QlQueryResponse(qlService.qlSearch(qlQuery));
            responseText = response.parse();
        } catch (Exception e) {
            responseText = "{\"error\":\"Invalid response from graphQL\"}";
        }
        log.info("Query sent to service: " + qlQuery);
        return responseText;
    }


    @ApiOperation(value = "Initial export of all database contents for results consumption", response = String.class)
    @RequestMapping(value = "/dbExport", produces = MediaType.APPLICATION_JSON_VALUE, method = { RequestMethod.GET})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful export of database contents", response = String.class) })
    @ResponseBody
    public String fullDataExport(@RequestBody String snapshotInputJson) {
        var response = "";
        log.info("API CALL!! --> /contributor/dbExport:: " + snapshotInputJson);
        try {
            FullDataExport dataExport = new FullDataExport(snapshotInputJson);
            List<String> periodList = dataExport.retrievePeriodFromSnapshotInput();
            String queryStr = dataExport.buildSnapshotSurveyPeriodQuery(periodList);
            log.info("GraphQL Query: " + queryStr);
            response = qlService.qlSearch(queryStr);
            log.info("GraphQL Query Response: " + response);
        } catch (Exception e) {
            log.error("Exception in loading data for db Export "+e.getMessage());
            return "{\"error\":\"Error loading data for db Export " + e.getMessage() + "\"}";
        }
        return response;
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
            log.info("Load Selection Query: " + loadQuery);
            String  response = qlService.qlSearch(loadQuery);
            log.info("Load Selection File response: " + response);
            //Process if any GraphQL exception
            String message = fileQuery.processGraphQlErrorMessage(response);
            log.info("GraphQL response message: " + response);
            if(message != null && message.length() > 0) {
                return "{\"error\":\"Failed to load Selection File " + message + " \"}";
            }

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
        log.info("GraphQL Query sent to service: " + delayResponseQuery);
        JSONObject responseText;
        String response;
        String output;
        try {
            response = qlService.qlSearch(delayResponseQuery);
            log.info("Response from GraphQL Query: " + response);
            responseText = new QlQueryResponse().buildDelayResponseOutput(response);
            output = responseText.toString();
        } catch (Exception e) {
            output = "{\"error\":\"Invalid response from graphQL\"}";
        }
        log.info("Response output before sending to Lambda: " + output);
        return output;
    }
}

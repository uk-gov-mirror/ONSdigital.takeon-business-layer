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

import java.util.Map;


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
    @GetMapping(value = "/dbExport", produces = MediaType.APPLICATION_JSON_VALUE)
    public String fullDataExport() {
        var response = "";
        try {
            response = qlService.qlSearch(new FullDataExport().buildQuery());
        } catch (Exception e) {
            log.info("Exception: " + e);
            log.info("QL Response: " + response);
            return "{\"error\":\"Error loading data for db Export\"}";
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
            var loadQuery = new SelectionFileQuery(selectionData, qlService).buildSaveSelectionFileQuery();
            log.info("Load Selection Query: " + loadQuery);
            var response = qlService.qlSearch(loadQuery);
            log.info("Load Selection File response: " + response.toString());



        } catch (Exception e) {
            log.info("Can't build Batch Selection Load Query / Invalid Response from GraphQL: " + e);
            e.printStackTrace();
            return "{\"error\":\"Failed to load Selection File\"}";
        }

        return "{\"Success\":\"Successfully loaded Selection File\"}";
    }
}

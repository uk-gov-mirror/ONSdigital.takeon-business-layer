package uk.gov.ons.collection.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;

import org.apache.commons.configuration.interpol.ExprLookup.Variables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.collection.entity.FullDataExport;
import uk.gov.ons.collection.service.GraphQlService;
import uk.gov.ons.collection.utilities.QlQueryBuilder;
import uk.gov.ons.collection.utilities.QlQueryResponse;
import uk.gov.ons.collection.utilities.SelectionFileQuery;
import uk.gov.ons.collection.utilities.SelectionFileResponse;

import java.util.Map;
import java.util.StringJoiner;

import com.jayway.jsonpath.InvalidJsonException;

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
        String qlQuery = new QlQueryBuilder(searchParameters).buildContributorSearchQuery();
        String responseText;
        log.info("Query sent to service: " + qlQuery);
        try {
            QlQueryResponse response = new QlQueryResponse(qlService.qlSearch(qlQuery));
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
    

    @Autowired
    @ApiOperation(value = "Check IDBR table and get existing FormID and Type", response = String.class)
    @GetMapping(value = "/getFormid/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful retrieval of form id from IDBR form mapping table", response = String.class)})
    public String getFormid(@MatrixVariable Map<String, String> params) {

        log.info("API CALL!! --> /contributor/getFormid/{vars} :: " + params);
        String formIdQuery = "";
        Integer formId;
        try {
            formIdQuery = new SelectionFileQuery(params).buildCheckIDBRFormidQuery();
            SelectionFileResponse response = new SelectionFileResponse(qlService.qlSearch(formIdQuery));
            formId = response.parseFormidResponse();
        } catch (InvalidJsonException err) {
            log.info("Exception found: " + err);
            return "{\"error\":\"Unable to determine selection data\"}";
        }
        log.info("API Complete --> /contributor/getExistingFormid/{vars}");
        return formId.toString();
    }

    @Autowired
    @ApiOperation(value = "Check IDBR table and get existing FormID and Type", response = String.class)
    @GetMapping(value = "/loadSelectionFile", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful retrieval of all details", response = String.class)})
    public String loadSelectionFile(@RequestBody String selectionData) {
        log.info("API CALL!! --> /contributor/loadSelectionFile:: ");
        try {
            var loadQuery = new SelectionFileQuery(selectionData).buildSaveSelectionFileQuery();
            qlService.qlSearch(loadQuery);

        } catch (Exception e) {
            log.info("Can't build Batch Selection Load Query / Invalid Response from GraphQL: " + e);
            return "{\"error\":\"Failed to load Selection File\"}";
        }

        return "{\"Success\":\"Successfully loaded Selection File\"}";
        }
}

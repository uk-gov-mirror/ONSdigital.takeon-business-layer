package uk.gov.ons.collection.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.collection.entity.ContributorEntity;
import uk.gov.ons.collection.exception.DataNotFondException;
import uk.gov.ons.collection.service.ContributorService;
import uk.gov.ons.collection.service.GraphQLService;

import java.util.*;

@Log4j2
@Api(value = "Contributor Controller", description = "Main (and so far only) end point for the connection between the UI and persistance layer")
@RestController
@RequestMapping(value = "/contributor")
public class ContributorController {

    private List<String> defaultValidSearchColumns = new ArrayList<>(Arrays.asList("reference", "period", "survey",
            "status", "formid"));

    private static final String NO_RECORDS_MESSAGE = "Persistance Layer is returning Zero records";

    @Autowired
    ContributorService service;
    @ApiOperation(value = "Search contributor table by arbitrary parameters", response = String.class)
    @GetMapping(value = "/search/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of Contributor details", response = ContributorEntity.class),
            @ApiResponse(code = 404, message = "Contributor does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
    public Iterable<ContributorEntity> getSearch(@MatrixVariable Map<String, String> matrixVars) throws Exception {
        Iterable<ContributorEntity> contributorEntities = null;
        String filteredSearchParameters = filterAndPrepareSearchParameters(matrixVars, this.defaultValidSearchColumns);
        log.info("Filtered search parameters { }", filteredSearchParameters);
        contributorEntities = service.generalSearch(filteredSearchParameters);
        log.info("Contributor Entities after calling Persistance Layer { }", filteredSearchParameters);

        if (contributorEntities == null) {
            throw new Exception();
        } else {
            if (contributorEntities instanceof Collection) {
                int size =  ((Collection<?>) contributorEntities).size();
                log.info("Contributor Entities Elements size {}", size);
                if(size == 0) {
                    throw new DataNotFondException(NO_RECORDS_MESSAGE);
                }
            }
        }

        return contributorEntities;

    }

    public String filterAndPrepareSearchParameters(Map<String, String> inputParameters, List<String> allowedParameters) {
        Map<String,String> filteredParameters = UrlParameterBuilder.filter(inputParameters, allowedParameters);
        return UrlParameterBuilder.buildParameterString(filteredParameters);
    }

    @ApiOperation(value = "Get contributor details", response = String.class)
    @GetMapping(value = "/searchBy/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of Contributor details", response = ContributorEntity.class)})
    @ResponseBody
    public Iterable<ContributorEntity> searchBy(@MatrixVariable Map<String, String> matrixVars) throws Exception {
        Iterable<ContributorEntity> contributorEntities = null;
        String filteredSearchParameters = filterAndPrepareSearchParameters(matrixVars, this.defaultValidSearchColumns);
        log.info("Filtered search parameters { }", filteredSearchParameters);
        contributorEntities = service.generalSearch(filteredSearchParameters);
        log.info("Contributor Entities after calling Persistance Layer { }", filteredSearchParameters);

        if (contributorEntities == null) {
            throw new Exception();
        } else {
            if (contributorEntities instanceof Collection) {
                int size =  ((Collection<?>) contributorEntities).size();
                log.info("Contributor Entities Elements size {}", size);
                if(size == 0) {
                    throw new DataNotFondException(NO_RECORDS_MESSAGE);
                }
            }
        }

        return contributorEntities;

    }

    @Autowired
    GraphQLService qlService;

    @GetMapping(value = "/qlSearch/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of Contributor details", response = ContributorEntity.class)})
    public String searchContributor(@MatrixVariable Map <String, String> searchParameters){
        String qlQuery = new qlQueryBuilder(searchParameters).buildContributorSearchQuery();
        String responseText;
        log.info("Query sent to service: " + qlQuery);
        try {
            qlQueryResponse response = new qlQueryResponse(qlService.qlSearch(qlQuery));
            responseText = response.parse();
        }
        catch(Exception e){
            responseText = "{\"error\":\"Invalid response from graphQL\"}";
        }
        log.info("Query sent to service: " + qlQuery);     
        return responseText;
    }

    @GetMapping(value = "/dbExport", produces = MediaType.APPLICATION_JSON_VALUE)
    public String validationDbExport(){
        qlQueryBuilder query = new qlQueryBuilder(null);
        String response = qlService.qlSearch(query.buildExportDBQuery());
        return response;
    }
}

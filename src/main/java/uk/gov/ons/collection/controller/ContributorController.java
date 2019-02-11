package uk.gov.ons.collection.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.collection.entity.ContributorEntity;
import uk.gov.ons.collection.service.ContributorService;

import java.util.*;
@Api(value = "Contributor Controller", description = "Main (and so far only) end point for the connection between the UI and persistance layer")
@RestController
@RequestMapping(value = "/contributor")
public class ContributorController {

    private List<String> defaultValidSearchColumns = new ArrayList<>(Arrays.asList("reference", "period", "survey",
            "status", "formid"));

    @Autowired
    ContributorService service;
    @ApiOperation(value = "Search contributor table by arbitrary parameters", response = String.class)
    @GetMapping(value = "/search/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of Contributor details", response = ContributorEntity.class),
            @ApiResponse(code = 404, message = "Contributor does not exist"),
            @ApiResponse(code = 500, message = "Internal server error")})
    @ResponseBody
    public Iterable<ContributorEntity> getSearch(@MatrixVariable Map<String, String> matrixVars) {
        String filteredSearchParameters = filterAndPrepareSearchParameters(matrixVars, this.defaultValidSearchColumns);
        return service.generalSearch(filteredSearchParameters);
    }

    public String filterAndPrepareSearchParameters(Map<String, String> inputParameters, List<String> allowedParameters) {
        Map<String,String> filteredParameters = UrlParameterBuilder.filter(inputParameters, allowedParameters);
        return UrlParameterBuilder.buildParameterString(filteredParameters);
    }
}
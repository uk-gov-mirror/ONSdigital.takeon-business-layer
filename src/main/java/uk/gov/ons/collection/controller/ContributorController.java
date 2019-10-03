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
import uk.gov.ons.collection.utilities.RelativePeriod;

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
	    System.out.println(response.toString());
            responseText = response.parse();
        }
        catch(Exception e){
            responseText = "{\"error\":\"Invalid response from graphQL\"}";
        }
	System.out.println(responseText.toString());
        return responseText;
    }

    @ApiOperation(value = "Initial export of all database contents for results consumption", response = String.class)
    @GetMapping(value = "/dbExport", produces = MediaType.APPLICATION_JSON_VALUE)
    public String validationDbExport(){
        qlQueryBuilder query = new qlQueryBuilder(null);
        String response = qlService.qlSearch(query.buildExportDBQuery());
        return response;
    }

    @ApiOperation(value = "Get all validation config & response data", response = String.class)
    @GetMapping(value="/validationPrepConfig/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful retrieval of all details", response = String.class)})
    public String dataPrepConfig(@MatrixVariable Map <String, String> searchParameters){

        // TODO: This method is rancid. Refactor when it's all working.

        // TODO: Validate all 3 parameters have been passed through
        String period = searchParameters.get("period");
        String reference = searchParameters.get("reference");
        String survey = searchParameters.get("survey");
 
        // Step 1 - Get a unique list of all period offsets
        ArrayList<Integer> uniqueOffsets;
        try {
            log.info("\n\n\n\nLoading period offsets");
            String query = new qlQueryBuilder(null).buildOffsetPeriodQuery();
            log.info("Query: " + query);
            qlQueryResponse response = new qlQueryResponse(qlService.qlSearch(query));
            log.info("\n\nResponse: " + response );
            uniqueOffsets = response.parseForPeriodOffset();
        }
        catch(Exception e){
            return "{\"error\":\"Invalid response getting period offsets from graphQL\"}";
        }

        int formID;
        String periodicity;
        // Step 1b - Get the formID and periodicity of the given reference/period/survey
        try {
            log.info("\n\n\n\nLoading formID/periodicity");
            String query = new qlQueryBuilder(searchParameters).buildContributorQuery();
            log.info("Query: " + query);
            qlQueryResponse response = new qlQueryResponse(qlService.qlSearch(query));
            log.info("\n\nResponse: " + response );

            formID = response.getFormID();
            periodicity = response.getPeriodicity();
        }
        catch(Exception e){
            return "{\"error\":\"Invalid response form and periodicity from graphQL\"}";
        }

        // Step 2 - Convert the period offsets to a list of IDBR periods
        List<String> outputPeriods = new ArrayList<>();
        try { RelativePeriod rp = new RelativePeriod(periodicity);
            for ( int i = 0; i < uniqueOffsets.size(); i++) {
                String idbrPeriod = rp.calculateRelativePeriod(uniqueOffsets.get(i).intValue(), period);
                outputPeriods.add(idbrPeriod);
            }
        }
        catch (Exception e) {
            return "{\"error\":\"Error processing periods\"}";
        }

        log.info("FormID/Periodicity: " + formID + "/" + periodicity);
        log.info("IDBR Periods: " + outputPeriods);

        // Step 3 - Load Validation Config

        // Step 4 - Load each contrib/response/form for each idbrPeriod above
        try {
            log.info("\n\n\n\nLoading contributor/response/form details");
           
            for (int i = 0; i < outputPeriods.size(); i++) {
                HashMap<String,String> spr = new HashMap<>();
                spr.put("reference", reference);
                spr.put("survey", survey);
                spr.put("period", outputPeriods.get(i));
                String query = new qlQueryBuilder(spr).buildContribResponseFormDetailsQuery();
                log.info("Query: " + i + "---" + query);
                qlQueryResponse response = new qlQueryResponse(qlService.qlSearch(query));
                log.info("\n\nResponse: " + i + "---" + response );
            }
        }
        catch(Exception e){
            return "{\"error\":\"Invalid contrib/response/form from graphQL\"}";
        }

        // Step 5 - munge

        // Parse response to expected structure
        // return parsed response

        return "{\"error\":\"Error worked correctly\"}";
    }
}

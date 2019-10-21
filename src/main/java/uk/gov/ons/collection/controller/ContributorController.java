package uk.gov.ons.collection.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.collection.entity.ContributorEntity;
import uk.gov.ons.collection.entity.DbExport;
import uk.gov.ons.collection.entity.PeriodOffsetQuery;
import uk.gov.ons.collection.entity.PeriodOffsetResponse;
import uk.gov.ons.collection.entity.ValidationConfigQuery;
import uk.gov.ons.collection.exception.DataNotFoundException;
import uk.gov.ons.collection.service.ContributorService;
import uk.gov.ons.collection.service.GraphQlService;
import uk.gov.ons.collection.utilities.RelativePeriod;
import uk.gov.ons.collection.utilities.UrlParameterBuilder;
import uk.gov.ons.collection.utilities.QlQueryBuilder;
import uk.gov.ons.collection.utilities.QlQueryResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                if (size == 0) {
                    throw new DataNotFoundException(NO_RECORDS_MESSAGE);
                }
            }
        }

        return contributorEntities;

    }

    public String filterAndPrepareSearchParameters(Map<String, String> inputParameters, List<String> allowedParameters) {
        Map<String,String> filteredParameters = UrlParameterBuilder.filter(inputParameters, allowedParameters);
        return UrlParameterBuilder.buildParameterString(filteredParameters);
    }

    @Autowired
    GraphQlService qlService;

    @GetMapping(value = "/qlSearch/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful retrieval of Contributor details", response = ContributorEntity.class)})
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
    public String validationDbExport() {
        var response = "";
        try {
            response = qlService.qlSearch(new DbExport().buildQuery());
        } catch (Exception e) {
            log.info("Exception: " + e);     
            log.info("QL Response: " + response);
            return "{\"error\":\"Error loading data for db Export\"}";
        }
        return response;
    }

    
    @ApiOperation(value = "Get all validation config & response data", response = String.class)
    @GetMapping(value = "/validationPrepConfig/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful retrieval of all details", response = String.class)})
    public String dataPrepConfig(@MatrixVariable Map<String, String> searchParameters) {

        // TODO: Validate all 3 parameters have been passed through
        log.info("DEPRECATED!!!! API CALL!! --> /validationPrepConfig/{vars} :: " + searchParameters);
        String period = searchParameters.get("period");
        String reference = searchParameters.get("reference");
        String survey = searchParameters.get("survey");
 
        // Step 1 - Get a unique list of all IDBR periods to query
        int formId;
        String periodicity;
        List<String> idbrPeriods = new ArrayList<>();
        try {
            var qlQuery = new PeriodOffsetQuery(searchParameters);
            var qlResponse = new PeriodOffsetResponse(qlService.qlSearch(qlQuery.getQlQuery()));
            formId = qlResponse.parseFormId();
            periodicity = qlResponse.parsePeriodicity();
            idbrPeriods = new RelativePeriod(periodicity).getIdbrPeriods(qlResponse.parsePeriodOffset(), period);
        } catch (Exception e) {
            log.info("Exception caught: " + e);
            return "{\"error\":\"Unable to resolve unique list of IDBR periods\"}";
        }
        
        // Step 2 - Load Validation Config for the given formId
        JSONArray validationConfig = new JSONArray();
        try {
            var qlQuery = new ValidationConfigQuery(formId).getQlQuery();
            var response = new QlQueryResponse(qlService.qlSearch(qlQuery));            
            validationConfig = response.parseValidationConfig();
        } catch (Exception e) {
            log.info("Exception caught: " + e);
            return "{\"error\":\"Error obtaining validation config query\"}";
        }


        JSONArray responses = new JSONArray();
        JSONArray contributors = new JSONArray();
        JSONArray forms = new JSONArray();

        // Step 3 - Load each contrib/response/form for each idbrPeriod above
        try {
            for (int i = 0; i < idbrPeriods.size(); i++) {
                HashMap<String,String> spr = new HashMap<>();
                spr.put("reference", reference);
                spr.put("survey", survey);
                spr.put("period", idbrPeriods.get(i));

                String query = new QlQueryBuilder(spr).buildContribResponseFormDetailsQuery();
                QlQueryResponse queryResponse = new QlQueryResponse(qlService.qlSearch(query));
                
                JSONArray responseArray = queryResponse.getResponses();
                for (int j = 0; j < responseArray.length(); j++) {
                    responses.put(responseArray.getJSONObject(j));
                }

                JSONArray formArray = queryResponse.getForm(survey, period);
                if (formArray.length() > 0) {
                    forms.put(formArray);
                }

                JSONObject contributor = queryResponse.getContributors();
                if (contributor.length() > 0) {
                    contributors.put(contributor);
                }
            }
        } catch (Exception e) {
            log.info("Exception: " + e);
            return "{\"error\":\"Invalid contrib/response/form from graphQL\"}";
        }

        // log.info("\n\nFinal OutputResponseArray: " + responses);
        // log.info("\n\nFinal OutputContributorArray: " + contributors);
        // log.info("\n\nFinal form definition: " + forms);
        // log.info("\n\nFinal validation config: " + validationConfig);        

        log.info("API CALL!! --> /validationPrepConfig/{vars} :: Complete");
        var outputJson = new JSONObject().put("contributor",contributors).put("validation_config",validationConfig)
                                .put("response",responses).put("question_schema",forms).put("reference", reference)
                                .put("period",period).put("survey",survey).put("periodicity", periodicity);
        return outputJson.toString();
    }

}

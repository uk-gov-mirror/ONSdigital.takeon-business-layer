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
import uk.gov.ons.collection.entity.FullDataExport;
import uk.gov.ons.collection.entity.PeriodOffsetQuery;
import uk.gov.ons.collection.entity.PeriodOffsetResponse;
import uk.gov.ons.collection.entity.ValidationConfigQuery;
import uk.gov.ons.collection.exception.DataNotFoundException;
import uk.gov.ons.collection.service.ContributorService;
import uk.gov.ons.collection.service.GraphQlService;
import uk.gov.ons.collection.utilities.QlQueryBuilder;
import uk.gov.ons.collection.utilities.QlQueryResponse;
import uk.gov.ons.collection.utilities.RelativePeriod;
import uk.gov.ons.collection.utilities.UrlParameterBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@Api(value = "View Form Controller", description = "Entry point for View Form using Graphql")
@RestController
@RequestMapping(value = "/viewform")
public class ViewFormController {
   
@Autowired
GraphQlService qlService;

    @GetMapping(value = "/qlSearch/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
        @ApiResponse(code = 200, message = "Successful retrieval of Contributor details", response = String.class)})
    public String viewFormDetails(@MatrixVariable Map<String, String> searchParameters) {
    String qlQuery = new QlQueryBuilder(searchParameters).buildViewFormQuery();
        String responseText;
    log.info("Query sent to service: " + qlQuery);
    try {
        QlQueryResponse response = new QlQueryResponse(qlService.qlSearch(qlQuery));
        responseText = response.getViewForm();
    } catch (Exception e) {
        responseText = "{\"error\":\"Invalid response from graphQL\"}";
    }
    log.info("Query sent to service: " + qlQuery);     
    return responseText;
    }
}




    //     log.info("API CALL!! --> /getAllViewForm/{vars} :: " + searchParameters);
        // String period = searchParameters.get("period");
        // String reference = searchParameters.get("reference");
        // String survey = searchParameters.get("survey");
        
        // String view_form;
        // List<String> viewForm = new ArrayList<>();
        // JSONArray viewforms = new JSONArray();

        // try { 
            // for (int i = 0; i < viewForm.size(); i++) {
            //     HashMap<String,String> spr = new HashMap<>();
            //     spr.put("reference", reference);
            //     spr.put("survey", survey);
            //     spr.put("period", viewForm.get(i));

            //     String query = new QlQueryBuilder(spr).buildViewFormQuery();
            //     QlQueryResponse queryResponse = new QlQueryResponse(qlService.qlSearch(query));
                
            //     JSONArray viewform = queryResponse.getViewForm();
            //     if (viewform.length() > 0) {
            //         viewforms.put(viewform);
            //     }
        //     }
        // } catch (Exception e) {
        //     log.info("Exception: " + e);
        //     return "{\"error\":\"Invalid contributor/response/form definition details from graphQL\"}";
        // }
        // String qlQuery = new QlQueryBuilder(searchParameters).buildViewFormQuery();
        // log.info("\n\nFinal view form: " + viewforms);
        // log.info("API CALL!! --> /viewform/getAllViewForm :: Complete");
        // var outputJson = new JSONObject().put("viewform", viewforms).put("reference", reference).put("period", period).put("survey", survey);
        // return qlQuery.toString();



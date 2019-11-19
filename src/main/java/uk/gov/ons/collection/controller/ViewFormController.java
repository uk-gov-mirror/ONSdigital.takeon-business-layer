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

import uk.gov.ons.collection.entity.ViewFormQuery;
import uk.gov.ons.collection.entity.ViewFormResponse;
import uk.gov.ons.collection.service.GraphQlService;
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
    String qlQuery = new ViewFormQuery().buildViewFormQuery(searchParameters);
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
}



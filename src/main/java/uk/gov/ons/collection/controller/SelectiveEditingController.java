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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.collection.entity.SelectiveEditingQuery;
import uk.gov.ons.collection.entity.SelectiveEditingResponse;
import uk.gov.ons.collection.service.GraphQlService;

import java.util.Map;

@Log4j2
@Api(value = "Selective Editing Controller", description = "Entry points primarily involving selective editing")
@RestController
@RequestMapping(value = "/selectiveediting")
public class SelectiveEditingController {
    @Autowired
    GraphQlService qlService;

    @ApiOperation(value = "Get all selective editing data", response = String.class)
    @GetMapping(value = "/loadconfigdata/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful retrieval of selective editing data", response = String.class)})
    public String loadSelectiveEditingData(@MatrixVariable Map<String, String> params) {

        log.info("API CALL!! --> /selectiveediting/loadconfigdata/{vars} :: " + params);
        String response = "";
        SelectiveEditingQuery selectiveEditingQuery = null;

        try {
            /*selectiveEditingQuery = new SelectiveEditingQuery(params);
            String queryStr = selectiveEditingQuery.buildViewFormQuery();
            String selectiveEditingQueryOutput = qlService.qlSearch(queryStr);
            log.info("Selective Editing Query Output: "+selectiveEditingQueryOutput);
            SelectiveEditingResponse selectiveEditingResponse = new SelectiveEditingResponse(selectiveEditingQueryOutput);
            response = selectiveEditingResponse.parseSelectiveEditingQueryResponse();
            */
            response = "{\"reference\":\"49900534932\",\"designweight\":2,\"resultscellnumber\":1,\"period\":\"201904\",\"domain\":1,\"survey\":\"023\",\"domainconfig\":[{\"currentresponse\":\"1\",\"questioncode\":\"20\",\"estimate\":100000000,\"threshold\":0.001,\"previousresponse\":\"3\"}]}";
            log.info("Selective Editing Response before sending to lambda: "+response);

        } catch (Exception err) {
            log.error("Exception found in Selective Editing: " + err.getMessage());
            String message = err.getMessage() != null ? err.getMessage().replace("\"","'") : "";
            response = "{\"error\":\"Unable to load selective editing config data " + message + "\"}";
        }
        log.info("API Complete!! --> /selectiveediting/loadconfigdata");
        return response;
    }
}

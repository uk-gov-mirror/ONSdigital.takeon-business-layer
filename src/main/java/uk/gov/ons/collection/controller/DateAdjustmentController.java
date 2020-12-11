package uk.gov.ons.collection.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.MatrixVariable;

import uk.gov.ons.collection.service.GraphQlService;
import java.util.Map;

@Log4j2
@Api(value = "Date Adjustment Controller", description = "Entry points primarily involving date adjustment")
@RestController
@RequestMapping(value = "/dateadjustment")
public class DateAdjustmentController {

    @Autowired
    GraphQlService qlService;

    @ApiOperation(value = "Get all date adjustment data", response = String.class)
    @GetMapping(value = "/loadconfigdata/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful retrieval of selective editing data", response = String.class)})
    public String loadDateAdjustmentData(@MatrixVariable Map<String, String> params) {
        log.info("API CALL!! --> /dateadjustment/loadconfigdata/{vars} :: " + params);
        String response = "{\n" +
                "  \"reference\": \"49900613746\",\n" +
                "  \"period\": \"201903\",\n" +
                "  \"survey\": \"023\",\n" +
                "  \"formid\": \"6\",\n" +
                "  \"periodstart\": \"20190304\",\n" +
                "  \"periodend\": \"20190331\",\n" +
                "  \"frozensic\": \"41100\",\n" +
                "  \"domain\": 1,\n" +
                "  \"cellnumber\": 1,\n" +
                "  \"returnedstartdate\": \"20190401\",\n" +
                "  \"returnedenddate\": \"20190430\",\n" +
                "  \"longperiodparameter\": 35,\n" +
                "  \"shortperiodparameter\": 27,\n" +
                "  \"averagewweekly\": true,\n" +
                "  \"settomidpoint\": false,\n" +
                "  \"settoequalweighted\": false,\n" +
                "  \"usecalendardays\": false,\n" +
                "  \"responses\": [\n" +
                "    {\n" +
                "      \"questioncode\": \"20\",\n" +
                "      \"response\": \"10000\",\n" +
                "      \"instance\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"questioncode\": \"21\",\n" +
                "      \"response\": \"500\",\n" +
                "      \"instance\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"questioncode\": \"22\",\n" +
                "      \"response\": \"1000\",\n" +
                "      \"instance\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"questioncode\": \"23\",\n" +
                "      \"response\": \"300\",\n" +
                "      \"instance\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"questioncode\": \"24\",\n" +
                "      \"response\": \"900\",\n" +
                "      \"instance\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"questioncode\": \"25\",\n" +
                "      \"response\": \"800\",\n" +
                "      \"instance\": \"0\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"questioncode\": \"26\",\n" +
                "      \"response\": \"200\",\n" +
                "      \"instance\": \"0\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"weights\": [\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190304\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.1,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190305\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.15,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190306\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.12,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190307\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.17,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190308\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.23,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190309\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.1,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190310\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.13,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190311\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.145,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190312\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.105,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190313\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.15,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190314\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.17,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190315\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.18,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190316\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.17,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190317\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.08,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190318\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.14,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190319\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.14,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190320\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.14,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190321\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.14,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190322\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.15,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190323\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.15,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190324\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.14,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190325\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.13,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190326\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.14,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190327\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.16,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190328\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.15,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190329\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.13,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190330\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.16,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"tradingdate\": \"20190331\",\n" +
                "          \"domain\": 1,\n" +
                "          \"weight\": 0.13,\n" +
                "          \"period\": \"201903\",\n" +
                "          \"periodstart\": \"20190304\",\n" +
                "          \"periodend\": \"20190331\"\n" +
                "        }\n" +
                "      ]\n" +
                "}";

        log.info("API Complete!! --> /dateadjustment/loadconfigdata");

        return response;
    }

    @ApiOperation(value = "Save date adjustment calculation output", response = String.class)
    @RequestMapping(value = "/saveOutput", method = { RequestMethod.POST, RequestMethod.PUT })
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful save of date adjustment calculation outputs", response = String.class) })
    @ResponseBody
    public String saveDateAdjustmentOutput(@RequestBody String jsonString)  {

        try {
            log.info("API CALL!! --> /dateadjustment/saveOutput :: " + jsonString);

        } catch (Exception err) {
            log.error("Exception found in Date Adjustment: " + err.getMessage());
            String message = processJsonErrorMessage(err);
            return "{\"error\":\"Unable to save or update date adjustment data " + message + "\"}";
        }
        return "{\"Success\":\"Date Adjustment data saved successfully\"}";

    }

    private String processJsonErrorMessage(Exception err) {

        String message = err.getMessage() != null ? err.getMessage().replace("\"","'") : "";
        return message;
    }
}

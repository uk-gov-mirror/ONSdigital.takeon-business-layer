package uk.gov.ons.collection.test;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.ValidationOutputData;
import uk.gov.ons.collection.entity.ValidationOutputs;
import uk.gov.ons.collection.exception.InvalidJsonException;
import uk.gov.ons.collection.utilities.QlQueryResponse;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Log4j2
public class ValidationOutputsTest {


    String graphQLOutput = "{\n" +
            "    \"data\": {\n" +
            "      \"allValidationoutputs\": {\n" +
            "        \"nodes\": [\n" +
            "          {\n" +
            "            \"reference\": \"12345678012\",\n" +
            "            \"period\": \"201801\",\n" +
            "            \"survey\": \"999A\",\n" +
            "            \"validationoutputid\": 33,\n" +
            "            \"triggered\": true,\n" +
            "            \"instance\": 0,\n" +
            "            \"formula\": \"abs(40000 - 10000) > 20000 AND 400000 > 0 AND 10000 > 0\",\n" +
            "            \"validationid\": \"10\",\n" +
            "            \"overridden\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"reference\": \"12345678012\",\n" +
            "            \"period\": \"201801\",\n" +
            "            \"survey\": \"999A\",\n" +
            "            \"validationoutputid\": 34,\n" +
            "            \"triggered\": true,\n" +
            "            \"instance\": 0,\n" +
            "            \"formula\": \"2 = 2\",\n" +
            "            \"validationid\": \"20\",\n" +
            "            \"overridden\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"reference\": \"12345678012\",\n" +
            "            \"period\": \"201801\",\n" +
            "            \"survey\": \"999A\",\n" +
            "            \"validationoutputid\": 36,\n" +
            "            \"triggered\": true,\n" +
            "            \"instance\": 0,\n" +
            "            \"formula\": \"'0' != ''\",\n" +
            "            \"validationid\": \"30\",\n" +
            "            \"overridden\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"reference\": \"12345678012\",\n" +
            "            \"period\": \"201801\",\n" +
            "            \"survey\": \"999A\",\n" +
            "            \"validationoutputid\": 39,\n" +
            "            \"triggered\": true,\n" +
            "            \"instance\": 0,\n" +
            "            \"formula\": \"543 != 5143\",\n" +
            "            \"validationid\": \"100\",\n" +
            "            \"overridden\": false\n" +
            "          }  \n" +
            "        ]\n" +
            "      }\n" +
            "    }\n" +
            "  }";

    String graphQLOutputTest = "{\n" +
            "    \"data\": {\n" +
            "      \"allValidationoutputs\": {\n" +
            "        \"nodes\": [\n" +
            "          {\n" +
            "            \"reference\": \"12345678012\",\n" +
            "            \"period\": \"201801\",\n" +
            "            \"survey\": \"999A\",\n" +
            "            \"validationoutputid\": 33,\n" +
            "            \"triggered\": true,\n" +
            "            \"instance\": 0,\n" +
            "            \"formula\": \"abs(40000 - 10000) > 20000 AND 400000 > 0 AND 10000 > 0\",\n" +
            "            \"validationid\": \"10\",\n" +
            "            \"overridden\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"reference\": \"12345678012\",\n" +
            "            \"period\": \"201801\",\n" +
            "            \"survey\": \"999A\",\n" +
            "            \"validationoutputid\": 34,\n" +
            "            \"triggered\": true,\n" +
            "            \"instance\": 0,\n" +
            "            \"formula\": \"2 = 2\",\n" +
            "            \"validationid\": \"11\",\n" +
            "            \"overridden\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"reference\": \"12345678012\",\n" +
            "            \"period\": \"201801\",\n" +
            "            \"survey\": \"999A\",\n" +
            "            \"validationoutputid\": 34,\n" +
            "            \"triggered\": true,\n" +
            "            \"instance\": 0,\n" +
            "            \"formula\": \"2 = 2\",\n" +
            "            \"validationid\": \"12\",\n" +
            "            \"overridden\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"reference\": \"12345678012\",\n" +
            "            \"period\": \"201801\",\n" +
            "            \"survey\": \"999A\",\n" +
            "            \"validationoutputid\": 34,\n" +
            "            \"triggered\": true,\n" +
            "            \"instance\": 0,\n" +
            "            \"formula\": \"2 = 2\",\n" +
            "            \"validationid\": \"13\",\n" +
            "            \"overridden\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"reference\": \"12345678012\",\n" +
            "            \"period\": \"201801\",\n" +
            "            \"survey\": \"999A\",\n" +
            "            \"validationoutputid\": 34,\n" +
            "            \"triggered\": true,\n" +
            "            \"instance\": 0,\n" +
            "            \"formula\": \"2 = 2\",\n" +
            "            \"validationid\": \"14\",\n" +
            "            \"overridden\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"reference\": \"12345678012\",\n" +
            "            \"period\": \"201801\",\n" +
            "            \"survey\": \"999A\",\n" +
            "            \"validationoutputid\": 34,\n" +
            "            \"triggered\": true,\n" +
            "            \"instance\": 0,\n" +
            "            \"formula\": \"2 = 2\",\n" +
            "            \"validationid\": \"120\",\n" +
            "            \"overridden\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"reference\": \"12345678012\",\n" +
            "            \"period\": \"201801\",\n" +
            "            \"survey\": \"999A\",\n" +
            "            \"validationoutputid\": 36,\n" +
            "            \"triggered\": true,\n" +
            "            \"instance\": 0,\n" +
            "            \"formula\": \"'0' != ''\",\n" +
            "            \"validationid\": \"15\",\n" +
            "            \"overridden\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"reference\": \"12345678012\",\n" +
            "            \"period\": \"201801\",\n" +
            "            \"survey\": \"999A\",\n" +
            "            \"validationoutputid\": 36,\n" +
            "            \"triggered\": true,\n" +
            "            \"instance\": 0,\n" +
            "            \"formula\": \"'0' != ''\",\n" +
            "            \"validationid\": \"15\",\n" +
            "            \"overridden\": false\n" +
            "          },\n" +
            "          {\n" +
            "            \"reference\": \"12345678012\",\n" +
            "            \"period\": \"201801\",\n" +
            "            \"survey\": \"999A\",\n" +
            "            \"validationoutputid\": 39,\n" +
            "            \"triggered\": true,\n" +
            "            \"instance\": 0,\n" +
            "            \"formula\": \"543 != 5143\",\n" +
            "            \"validationid\": \"100\",\n" +
            "            \"overridden\": false\n" +
            "          }  \n" +
            "        ]\n" +
            "      }\n" +
            "    }\n" +
            "  }";

    String lambdaoutputTest = "{\"validation_outputs\": [\n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 1, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"2 = 2\", \"triggered\": true, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 2, \"bpmid\": \"0\", \"instance\": 0},\n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 3, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 4, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 5, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 6, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 7, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 8, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 9, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 10, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 11, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 12, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 13, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 14, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 15, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 16, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 17, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 18, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 19, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 20, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 21, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 22, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 23, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 24, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 25, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 26, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 27, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 28, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 29, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 30, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 31, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 32, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 33, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 34, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 35, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 36, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 37, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 38, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 39, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 40, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 41, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 42, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 43, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 44, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 45, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 46, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 47, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 48, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 49, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 50, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 51, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 52, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 53, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 54, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 55, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 56, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 57, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 58, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 59, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 60, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 61, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 62, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 63, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 64, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 65, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 66, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 67, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 68, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 69, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 70, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 71, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 72, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 73, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 74, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 75, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 76, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 77, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 78, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 79, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 80, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 81, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 82, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 83, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 84, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 85, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 86, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 87, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 88, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 89, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 90, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 91, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 92, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 93, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 94, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 95, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 96, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 97, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 98, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 99, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 100, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 101, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 102, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 103, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 104, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 105, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 106, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 107, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"2 = 2\", \"triggered\": true, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 108, \"bpmid\": \"0\", \"instance\": 0},\n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 109, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 110, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 111, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 112, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 113, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 114, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 115, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 116, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 117, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 118, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 119, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 120, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 121, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 122, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 123, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 124, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 125, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 126, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 127, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 128, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 129, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 130, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 131, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 132, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 133, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 134, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 135, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 136, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 137, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 138, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 139, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 140, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 141, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 142, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 143, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 144, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 145, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 146, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 147, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 148, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 150, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 151, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 152, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 153, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 154, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 155, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 156, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 157, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 158, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 159, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 160, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 161, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 162, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 163, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 164, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"2 = 2\", \"triggered\": true, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 165, \"bpmid\": \"0\", \"instance\": 0},\n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 166, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 167, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 168, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 169, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 170, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 171, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 172, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 173, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 174, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 175, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 176, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 177, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 178, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 179, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 180, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 181, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 182, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 183, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 184, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 185, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 186, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 187, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 188, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 189, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 190, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 191, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 192, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 193, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 194, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 195, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 196, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 197, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 198, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 199, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 200, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 201, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 202, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 203, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 204, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 205, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 206, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 207, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 208, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 209, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 210, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 211, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 212, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 213, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 214, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 215, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 216, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 217, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 218, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 219, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 220, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 221, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 222, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 223, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 224, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 225, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 226, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 227, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 228, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 229, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 230, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 231, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 232, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 233, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 234, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 235, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 236, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 237, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 238, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 239, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 240, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 241, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 242, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 243, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 244, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 245, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 246, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 247, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 248, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 249, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 250, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 251, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 253, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 254, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 255, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 256, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 257, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 258, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 259, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 260, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 261, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 262, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 263, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 264, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 265, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 266, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 267, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 268, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 269, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 270, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 271, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 272, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 273, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 274, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 275, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 276, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 277, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 278, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 279, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 280, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 281, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 282, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 283, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 284, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 285, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 286, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 287, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 288, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 289, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 290, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 291, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 292, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 293, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 294, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 295, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 296, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 297, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 298, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 299, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 300, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 301, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 302, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 303, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 304, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 305, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 306, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 307, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 308, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 309, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 310, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 311, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 312, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 313, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 314, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 315, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 316, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 317, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 318, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 319, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 320, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 321, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 322, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 323, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 324, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 325, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 326, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 327, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 328, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 329, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 330, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 331, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 332, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 333, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"0 != 0\", \"triggered\": false, \"validation\": \"QVDQ\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 334, \"bpmid\": \"0\", \"instance\": 0}]}";

    String lambdaoutput = "{\"validation_outputs\": [\n" +
            "      {\"formula\": \"999999 = 2\", \"triggered\": false, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 70, \"bpmid\": \"0\", \"instance\": 0}, \n" +
            "      {\"formula\": \"2 = 2\", \"triggered\": true, \"validation\": \"CPBMI\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 20, \"bpmid\": \"0\", \"instance\": 0},\n" +
            "      {\"formula\": \"0 != 0\", \"triggered\": false, \"validation\": \"QVDQ\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 30, \"bpmid\": \"0\", \"instance\": 0}]}";

    @Test
    void class_invalidJson_throwsExeption() {
        assertThrows(InvalidJsonException.class, () -> new ValidationOutputs("Dummy"));
    }


    @Test
    void getReference_validJson_returnsGivenReference() {
        var inputJson = "{\"validation_outputs\": [{\"reference\": \"ABC\"}]}";
        var expectedReference = "ABC";
        try {
            var reference = new ValidationOutputs(inputJson).getReference();
            assertEquals(expectedReference, reference);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void getReference_missingReference_throwsException() {
        assertThrows(InvalidJsonException.class,
                () -> new ValidationOutputs("{\"validation_outputs\": []}").getReference());
    }

    @Test
    void getPeriod_missingAttribute_throwsException() {
        assertThrows(InvalidJsonException.class,
                () -> new ValidationOutputs("{\"validation_outputs\": []}").getPeriod());
    }

    @Test
    void getSurvey_missingAttribute_throwsException() {
        assertThrows(InvalidJsonException.class,
                () -> new ValidationOutputs("{\"validation_outputs\": []}").getSurvey());
    }

    @Test
    void getPeriod_periodInJson_returnsGivenPeriod() {
        var inputJson = "{\"validation_outputs\": [{\"period\": \"ABC\"}]}";
        var expectedPeriod = "ABC";
        try {
            var period = new ValidationOutputs(inputJson).getPeriod();
            assertEquals(expectedPeriod, period);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void getSurvey_surveyInJson_returnsGivenSurvey() {
        var inputJson = "{\"validation_outputs\": [{\"survey\": \"ABC\"}]}";
        var expectedPeriod = "ABC";
        try {
            var survey = new ValidationOutputs(inputJson).getSurvey();
            assertEquals(expectedPeriod, survey);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void getStatusText_missingTriggeredAttribute_throwsException() {
        assertThrows(InvalidJsonException.class,
                () -> new ValidationOutputs("{\"validation_outputs\": [{\"a\":\"b\"}]}").getStatusText());
    }

    @Test
    void getStatusText_1triggered_Triggered() {
        var inputJson = "{\"validation_outputs\": [{\"triggered\": true}]}";
        var expectedStatus = "Validations Triggered";
        try {
            var status = new ValidationOutputs(inputJson).getStatusText();
            assertEquals(expectedStatus, status);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void getStatusText_1of3triggeredInJson_returnsTriggered() {
        var inputJson = "{\"validation_outputs\": [{\"triggered\": false},{\"triggered\": true},{\"triggered\": false}]}";
        var expectedStatus = "Validations Triggered";
        try {
            var status = new ValidationOutputs(inputJson).getStatusText();
            assertEquals(expectedStatus, status);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void getStatusText_0triggeredInJson_returnsClear() {
        var inputJson = "{\"validation_outputs\": [{\"triggered\": false}]}";
        var expectedStatus = "Clear";
        try {
            var status = new ValidationOutputs(inputJson).getStatusText();
            assertEquals(expectedStatus, status);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void getStatusText_0of5triggeredInJson_returnsClear() {
        var inputJson = "{\"validation_outputs\": [{\"triggered\": false},{\"triggered\": false},"
                + "{\"triggered\": false},{\"triggered\": false},{\"triggered\": false}]}";
        var expectedStatus = "Clear";
        try {
            var status = new ValidationOutputs(inputJson).getStatusText();
            assertEquals(expectedStatus, status);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void testParseValidationOutputs_returnsExpectedFormat() {
        String inputString = "{\"data\": {\"allValidationoutputs\": {\"nodes\": [" + "{\"formula\": \"1=1\","
                + "\"triggered\": true, \"lastupdatedby\": null, \"lastupdateddate\": null, \"instance\": 0,"
                + "\"overridden\": true, \"validationoutputid\": 35,"
                + "\"validationformByValidationid\": {\"severity\": \"W\", \"validationid\": 10, \"rule\": VP,"
                + "\"primaryquestion\": \"3000\", \"validationruleByRule\": {\"name\": \"Value Present\"}}}]}}}";

        String expectedOutput = "{\"validation_outputs\":[{\"severity\":\"W\",\"primaryquestion\":\"3000\",\"triggered\":true,"
                + "\"instance\":0,\"validationoutputid\":35,\"validationid\":10,\"lastupdateddate\":null,\"lastupdatedby\":null,\"name\":\"Value Present\","
                + "\"formula\":\"1=1\",\"rule\":\"VP\",\"overridden\":true"
                + "}]}";

        QlQueryResponse response = new QlQueryResponse(inputString);
        System.out.println("Actual output " + response.parseValidationOutputs().toString());
        System.out.println("Expected output " + expectedOutput);
        assertEquals(expectedOutput, response.parseValidationOutputs().toString());
    }

    @Test
    void test_validationOutput_verifyInsertUpdateDelete_performance() {

        try {

            long startTime = System.currentTimeMillis();

            ValidationOutputs outputs = new  ValidationOutputs(lambdaoutputTest);

            List<ValidationOutputData> validationData = outputs.extractValidationDataFromDatabase(graphQLOutputTest);
            List<ValidationOutputData> lambdaData = outputs.extractValidationDataFromLambda();

            List<ValidationOutputData> insertList = outputs.getValidationOutputInsertList(lambdaData, validationData);
            log.info("No of Validation output inserts" + insertList.size());


            List<ValidationOutputData> modifiedList = outputs.getValidationOutputModifiedList(lambdaData, validationData);
            log.info("No of modified validation output elements" + modifiedList.size());

            List<ValidationOutputData> upsertList = outputs.getValidationOutputUpsertList(modifiedList, insertList);
            log.info("No of upsert validation output elements" + upsertList.size());


            List<ValidationOutputData> deleteData = outputs.getDeleteValidationOutputList(lambdaData, validationData);
            log.info("No of Delete validation output elements" + deleteData.size());

            NumberFormat formatter = new DecimalFormat("#0.00000");

            long endTime = System.currentTimeMillis();
            log.info("Execution time is " + formatter.format((endTime - startTime) / 1000d) + " seconds");



        } catch (Exception exp) {
            exp.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    void test_validationOutput_verifyInsertUpdateDeleteElements() {

        try {

            ValidationOutputs outputs = new  ValidationOutputs(lambdaoutput);

            List<ValidationOutputData> validationData = outputs.extractValidationDataFromDatabase(graphQLOutput);
            List<ValidationOutputData> lambdaData = outputs.extractValidationDataFromLambda();

            List<ValidationOutputData> insertList = outputs.getValidationOutputInsertList(lambdaData, validationData);
            //check the element validationId=70 exists in Insert list or not. This ValidationId doesn't exists in ValidationOutput table and has to be inserted into database
            String expectedInsertElement = "validationId=70";
            assertTrue(insertList.toString().contains(expectedInsertElement));

            //check the element validationId=30 exists in Modified list or not. The formula is changed for this id when sending data from Lambda and this should be in the list
            List<ValidationOutputData> modifiedList = outputs.getValidationOutputModifiedList(lambdaData, validationData);
            System.out.println("Modified Data : "+modifiedList);
            String expectedModifiedElement = "validationId=30";
            assertTrue(modifiedList.toString().contains(expectedModifiedElement));
            List<ValidationOutputData> upsertList = outputs.getValidationOutputUpsertList(modifiedList, insertList);
            //Upsert contains both insert and modified elements. Verifying below whether both elements exists or not
            assertTrue(upsertList.toString().contains(expectedModifiedElement) && upsertList.toString().contains(expectedInsertElement));
            //validationId=10 is deprecated and needs to be deleted from database. Check this element exists in deleted list or not.
            List<ValidationOutputData> deleteData = outputs.getDeleteValidationOutputList(lambdaData, validationData);
            String expectedDeleteElement = "validationId=10";
            assertTrue(deleteData.toString().contains(expectedDeleteElement));


            String graphQLQuery = outputs.buildUpsertByArrayQuery(upsertList, deleteData);
            String expectedGraphQlQuery = "{\"query\": \"mutation upsertOutputArray{upsertDeleteValidationoutput(input: {arg0:[{reference: \\\"12345678012\\\"," +
                    "period: \\\"201801\\\",survey: \\\"999A\\\",formula: \\\"0 != 0\\\",validationid: \\\"30\\\"," +
                    "instance: \\\"0\\\",triggered: false,overridden: false,createdby: \\\"fisdba\\\",createddate: \\\""+outputs.getTime()+"\\\","+
                    "lastupdatedby: \\\"fisdba\\\",lastupdateddate: \\\"" +outputs.getTime()+"\\\"},{reference: \\\"12345678012\\\",period: \\\"201801\\\"," +
                    "survey: \\\"999A\\\",formula: \\\"999999 = 2\\\",validationid: \\\"70\\\",instance: \\\"0\\\",triggered: false,overridden: false,createdby: \\\"fisdba\\\"," +
                    "createddate: \\\"" +outputs.getTime()+"\\\",lastupdatedby: null,lastupdateddate: null}], " +
                    "arg1:[{reference: \\\"12345678012\\\",period: \\\"201801\\\",survey: \\\"999A\\\",formula: \\\"abs(40000 - 10000) > 20000 AND 400000 > 0 AND 10000 > 0\\\"," +
                    "validationid: \\\"10\\\",instance: \\\"0\\\",triggered: true,overridden: false,createdby: \\\"fisdba\\\",createddate: \\\""+outputs.getTime()+"\\\"," +
                    "lastupdatedby: null,lastupdateddate: null},{reference: \\\"12345678012\\\",period: \\\"201801\\\",survey: \\\"999A\\\"," +
                    "formula: \\\"543 != 5143\\\",validationid: \\\"100\\\",instance: \\\"0\\\",triggered: true,overridden: false,createdby: \\\"fisdba\\\",createddate: \\\""+outputs.getTime()+"\\\",lastupdatedby: null," +
                    "lastupdateddate: null}]}){clientMutationId}}\"}";

            assertEquals(expectedGraphQlQuery, graphQLQuery);

        } catch (Exception exp) {
            exp.printStackTrace();
            assertTrue(false);
        }
    }

    @Test
    void test_validationOutput_validGraphQlMissingAttribute_throwsException() throws Exception {
        String graphQLOutput1 = "{\n" +
                "  \"data\": {\n" +
                "    \"allValidationoutputs\": {\n" +
                "      \"nodes\": [" +
                "          {\n" +
                "            \"reference\": \"12345678012\",\n" +
                "            \"period\": \"201801\",\n" +
                "            \"survey\": \"999A\",\n" +
                "            \"validationoutputid\": 33,\n" +
                "            \"instance\": 0,\n" +
                "            \"formula\": \"abs(40000 - 10000) > 20000 AND 400000 > 0 AND 10000 > 0\",\n" +
                "            \"validationid\": \"10\",\n" +
                "            \"overridden\": false\n" +
                "          },\n" +
                "]\n" +
                "    }\n" +
                "  }\n" +
                "}";


        ValidationOutputs outputs = new  ValidationOutputs(lambdaoutput);

        assertThrows(InvalidJsonException.class, () -> outputs.extractValidationDataFromDatabase(graphQLOutput1));

    }

    @Test
    void test_validationOutput_lambdaOutputInvalidJson_throwsException()  {

        String lambdaoutput1 = "{\"validation_outputs1\": [\n" +
                "      {\"formula\": \"0 != 0\",  \"validation\": \"QVDQ\", \"reference\": \"12345678012\", \"period\": \"201801\", \"survey\": \"999A\", \"validationid\": 30, \"bpmid\": \"0\", \"instance\": 0}]}";

        assertThrows(InvalidJsonException.class, () ->new  ValidationOutputs(lambdaoutput1));

    }

}
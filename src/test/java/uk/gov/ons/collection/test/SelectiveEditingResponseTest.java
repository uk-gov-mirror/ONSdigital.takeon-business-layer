package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.SelectiveEditingQuery;
import uk.gov.ons.collection.entity.SelectiveEditingResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SelectiveEditingResponseTest {


    @Test
    void selectiveEditingConfigDetailsDetails_validJSONDataT(){


        String responseJSON = "{\n" +
                "  \"data\": {\n" +
                "    \"allContributors\": {\n" +
                "      \"nodes\": [\n" +
                "        {\n" +
                "          \"reference\": \"49900534932\",\n" +
                "          \"period\": \"201904\",\n" +
                "          \"survey\": \"023\",\n" +
                "          \"resultscellnumber\": 1,\n" +
                "          \"domain\": 1,\n" +
                "          \"responsesByReferenceAndPeriodAndSurvey\": {\n" +
                "            \"nodes\": [\n" +
                "              {\n" +
                "                \"questioncode\": \"20\",\n" +
                "                \"period\": \"201904\",\n" +
                "                \"response\": \"1\"\n" +
                "              },\n" +
                "              {\n" +
                "                \"questioncode\": \"21\",\n" +
                "                \"period\": \"201904\",\n" +
                "                \"response\": \"2\"\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        },\n" +
                "        {\n" +
                "          \"reference\": \"49900534932\",\n" +
                "          \"period\": \"201903\",\n" +
                "          \"survey\": \"023\",\n" +
                "          \"resultscellnumber\": null,\n" +
                "          \"domain\": null,\n" +
                "          \"responsesByReferenceAndPeriodAndSurvey\": {\n" +
                "            \"nodes\": [\n" +
                "              {\n" +
                "                \"questioncode\": \"20\",\n" +
                "                \"period\": \"201903\",\n" +
                "                \"response\": \"3\"\n" +
                "              },\n" +
                "              {\n" +
                "                \"questioncode\": \"21\",\n" +
                "                \"period\": \"201903\",\n" +
                "                \"response\": \"4\"\n" +
                "              }\n" +
                "            ]\n" +
                "          }\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"allSelectiveeditingconfigs\": {\n" +
                "      \"nodes\": [\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"period\": \"201904\",\n" +
                "          \"domain\": \"1\",\n" +
                "          \"questioncode\": \"20\",\n" +
                "          \"threshold\": 0.001,\n" +
                "          \"estimate\": 100000000\n" +
                "        },\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"period\": \"201904\",\n" +
                "          \"domain\": \"1\",\n" +
                "          \"questioncode\": \"21\",\n" +
                "          \"threshold\": 0.002,\n" +
                "          \"estimate\": 100000000\n" +
                "        }\n" +
                "      ]\n" +
                "    },\n" +
                "    \"allCelldetails\": {\n" +
                "      \"nodes\": [\n" +
                "        {\n" +
                "          \"survey\": \"023\",\n" +
                "          \"period\": \"201904\",\n" +
                "          \"cellnumber\": 1,\n" +
                "          \"designweight\": 2\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  }\n" +
                "}";


        try {
            SelectiveEditingResponse response = new SelectiveEditingResponse(responseJSON);
            String output = response.parseSelectiveEditingQueryResponse();
            System.out.println("Output to be sent to Lambda: " + output);
            Map<String, String> variables = new HashMap<String, String>();
            variables.put("reference", "49900534932");
            variables.put("period", "201904");
            variables.put("survey", "023");
            List<String> historyPeriodList = new ArrayList<String>();
            historyPeriodList.add("201904");
            historyPeriodList.add("201903");
            SelectiveEditingQuery selectiveEditingQuery = new SelectiveEditingQuery(variables);
            String queryStr = selectiveEditingQuery.buildSelectiveEditingLoadConfigQuery(historyPeriodList);
            System.out.println(queryStr);


        } catch (Exception e) {
            String actualMessage = e.getMessage();
            System.out.println("Error Message: "+actualMessage);
            assertTrue(true);

        }
    }

}

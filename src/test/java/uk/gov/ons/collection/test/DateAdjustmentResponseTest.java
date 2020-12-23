package uk.gov.ons.collection.test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.DateAdjustmentResponse;

public class DateAdjustmentResponseTest {


    String lambdaOutput = "{\n" +
            "  \"reference\": \"49900613746\",\n" +
            "  \"period\": \"201903\",\n" +
            "  \"survey\": \"023\",\n" +
            "  \"errorflag\": \"E\",\n" +
            "  \"dateadjustmenterrorflag\": \"E\",\n" +
            "  \"daysreturnedperiod\": 25,\n" +
            "  \"sumtradingweightsoverreturnedperiod\": 29.1,\n" +
            "  \"actualdaysreturnedperiod\": 25,\n" +
            "  \"sumtradingweightsoveractualreturnedperiod\": 30.1,\n" +
            "  \"dateadjustments\": [\n" +
            "    {\n" +
            "      \"questioncode\": \"20\",\n" +
            "      \"adjusted_value\": \"10000\",\n" +
            "      \"average_weekly_value\": \"25000\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"questioncode\": \"21\",\n" +
            "      \"adjusted_value\": \"10000\",\n" +
            "      \"average_weekly_value\": \"25000\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"questioncode\": \"22\",\n" +
            "      \"adjusted_value\": \"10000\",\n" +
            "      \"average_weekly_value\": \"25000\"\n" +
            "      \n" +
            "    },\n" +
            "    {\n" +
            "      \"questioncode\": \"23\",\n" +
            "      \"adjusted_value\": \"10000\",\n" +
            "      \"average_weekly_value\": \"25000\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"questioncode\": \"24\",\n" +
            "      \"adjusted_value\": \"10000\",\n" +
            "      \"average_weekly_value\": \"25000\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"questioncode\": \"25\",\n" +
            "      \"adjusted_value\": \"10000\",\n" +
            "      \"average_weekly_value\": \"25000\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"questioncode\": \"26\",\n" +
            "      \"adjusted_value\": \"10000\",\n" +
            "      \"average_weekly_value\": \"25000\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    String graphQLOutput = "{\n" +
            "  \"data\": {\n" +
            "    \"allContributors\": {\n" +
            "      \"nodes\": [\n" +
            "        {\n" +
            "          \"reference\": \"49900748571\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"survey\": \"023\",\n" +
            "          \"formid\": 7,\n" +
            "          \"status\": \"Form saved\",\n" +
            "          \"frozensic\": \"41100\",\n" +
            "          \"resultscellnumber\": 1,\n" +
            "          \"domain\": 1,\n" +
            "          \"formByFormid\": {\n" +
            "            \"survey\": \"023\",\n" +
            "            \"formid\": 7,\n" +
            "            \"formdefinitionsByFormid\": {\n" +
            "              \"nodes\": [\n" +
            "                {\n" +
            "                  \"questioncode\": \"20\",\n" +
            "                  \"dateadjustment\": true\n" +
            "                },\n" +
            "                {\n" +
            "                  \"questioncode\": \"21\",\n" +
            "                  \"dateadjustment\": true\n" +
            "                },\n" +
            "                {\n" +
            "                  \"questioncode\": \"22\",\n" +
            "                  \"dateadjustment\": true\n" +
            "                },\n" +
            "                {\n" +
            "                  \"questioncode\": \"23\",\n" +
            "                  \"dateadjustment\": true\n" +
            "                },\n" +
            "                {\n" +
            "                  \"questioncode\": \"24\",\n" +
            "                  \"dateadjustment\": true\n" +
            "                },\n" +
            "                {\n" +
            "                  \"questioncode\": \"25\",\n" +
            "                  \"dateadjustment\": true\n" +
            "                },\n" +
            "                {\n" +
            "                  \"questioncode\": \"26\",\n" +
            "                  \"dateadjustment\": true\n" +
            "                },\n" +
            "                {\n" +
            "                  \"questioncode\": \"27\",\n" +
            "                  \"dateadjustment\": true\n" +
            "                }\n" +
            "              ]\n" +
            "            }\n" +
            "          },\n" +
            "          \"responsesByReferenceAndPeriodAndSurvey\": {\n" +
            "            \"nodes\": [\n" +
            "              {\n" +
            "                \"reference\": \"49900748571\",\n" +
            "                \"period\": \"201903\",\n" +
            "                \"survey\": \"023\",\n" +
            "                \"questioncode\": \"20\",\n" +
            "                \"instance\": 0,\n" +
            "                \"response\": \"20000\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"reference\": \"49900748571\",\n" +
            "                \"period\": \"201903\",\n" +
            "                \"survey\": \"023\",\n" +
            "                \"questioncode\": \"21\",\n" +
            "                \"instance\": 0,\n" +
            "                \"response\": \"15000\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"reference\": \"49900748571\",\n" +
            "                \"period\": \"201903\",\n" +
            "                \"survey\": \"023\",\n" +
            "                \"questioncode\": \"22\",\n" +
            "                \"instance\": 0,\n" +
            "                \"response\": \"1500\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"reference\": \"49900748571\",\n" +
            "                \"period\": \"201903\",\n" +
            "                \"survey\": \"023\",\n" +
            "                \"questioncode\": \"23\",\n" +
            "                \"instance\": 0,\n" +
            "                \"response\": \"1200\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"reference\": \"49900748571\",\n" +
            "                \"period\": \"201903\",\n" +
            "                \"survey\": \"023\",\n" +
            "                \"questioncode\": \"24\",\n" +
            "                \"instance\": 0,\n" +
            "                \"response\": \"1800\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"reference\": \"49900748571\",\n" +
            "                \"period\": \"201903\",\n" +
            "                \"survey\": \"023\",\n" +
            "                \"questioncode\": \"25\",\n" +
            "                \"instance\": 0,\n" +
            "                \"response\": \"600\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"reference\": \"49900748571\",\n" +
            "                \"period\": \"201903\",\n" +
            "                \"survey\": \"023\",\n" +
            "                \"questioncode\": \"26\",\n" +
            "                \"instance\": 0,\n" +
            "                \"response\": \"1200\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"reference\": \"49900748571\",\n" +
            "                \"period\": \"201903\",\n" +
            "                \"survey\": \"023\",\n" +
            "                \"questioncode\": \"27\",\n" +
            "                \"instance\": 0,\n" +
            "                \"response\": \"900\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"reference\": \"49900748571\",\n" +
            "                \"period\": \"201903\",\n" +
            "                \"survey\": \"023\",\n" +
            "                \"questioncode\": \"7034\",\n" +
            "                \"instance\": 0,\n" +
            "                \"response\": \"6300\"\n" +
            "              }\n" +
            "            ]\n" +
            "          }\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    \"allDateadjustmentweightconfigs\": {\n" +
            "      \"nodes\": [\n" +
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
            "    },\n" +
            "    \"allContributordateadjustmentconfigs\": {\n" +
            "      \"nodes\": [\n" +
            "        {\n" +
            "          \"reference\": \"49900748571\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"survey\": \"023\",\n" +
            "          \"returnedstartdate\": \"20190301\",\n" +
            "          \"returnedenddate\": \"20190331\",\n" +
            "          \"longperiodparameter\": 35,\n" +
            "          \"shortperiodparameter\": 27,\n" +
            "          \"averageweekly\": true,\n" +
            "          \"settomidpoint\": false,\n" +
            "          \"settoequalweighted\": false,\n" +
            "          \"usecalendardays\": false\n" +
            "        }\n" +
            "      ]\n" +
            "    }\n" +
            "  }\n" +
            "}";
    @Test
    void date_adjustment_ExpectedJSONDataEqualsActualJSONData_ParsedData(){
        try {
            DateAdjustmentResponse dateAdjustmentResponse = new DateAdjustmentResponse(graphQLOutput);
            String output = dateAdjustmentResponse.parseDateAdjustmentQueryResponse();
            System.out.println(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void date_adjustment_verify_save_query(){
        try {
            DateAdjustmentResponse dateAdjustmentResponse = new DateAdjustmentResponse(lambdaOutput);

            JSONArray dateAdjustmentsArray = dateAdjustmentResponse.getDateAdjustments();
            System.out.println(dateAdjustmentsArray.toString());
            JSONObject responseObject = dateAdjustmentResponse.getJsonQlResponse();
            System.out.println("Reference: "+ responseObject.getString("reference"));
            System.out.println("Period: "+ responseObject.getString("period"));
            System.out.println("Survey: "+ responseObject.getString("survey"));
            String dateAdjustmentSaveQuery = dateAdjustmentResponse.buildSaveDateAdjustmentQuery();
            System.out.println("Save Query: " + dateAdjustmentSaveQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

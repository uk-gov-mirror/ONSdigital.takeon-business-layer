package uk.gov.ons.collection.test;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.DateAdjustmentResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DateAdjustmentResponseTest {


    String lambdaOutput = "{\n" +
            "  \"reference\": \"49900613746\",\n" +
            "  \"period\": \"201903\",\n" +
            "  \"survey\": \"023\",\n" +
            "  \"dateadjustmentlengthflag\": \"S\",\n" +
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
    String noContributorGraphQLOutput = "{\n" +
            "  \"data\": {\n" +
            "    \"allContributors\": {\n" +
            "      \"nodes\": []\n" +
            "    }\n" +
            "  }\n" +
            "}";
    String graphQLOutput = "{\n" +
            "  \"data\": {\n" +
            "    \"allContributors\": {\n" +
            "      \"nodes\": [\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"reference\": \"49900534932\",\n" +
            "          \"formid\": 5,\n" +
            "          \"status\": \"Form saved\",\n" +
            "          \"frozensic\": \"41100\",\n" +
            "          \"resultscellnumber\": 1,\n" +
            "          \"domain\": 1,\n" +
            "          \"formByFormid\": {\n" +
            "            \"survey\": \"023\",\n" +
            "            \"formid\": 5,\n" +
            "            \"formdefinitionsByFormid\": {\n" +
            "              \"nodes\": [\n" +
            "                {\n" +
            "                  \"questioncode\": \"11\",\n" +
            "                  \"dateadjustment\": false\n" +
            "                },\n" +
            "                {\n" +
            "                  \"questioncode\": \"12\",\n" +
            "                  \"dateadjustment\": false\n" +
            "                },\n" +
            "                {\n" +
            "                  \"questioncode\": \"146\",\n" +
            "                  \"dateadjustment\": false\n" +
            "                },\n" +
            "                {\n" +
            "                  \"questioncode\": \"146a\",\n" +
            "                  \"dateadjustment\": false\n" +
            "                },\n" +
            "                {\n" +
            "                  \"questioncode\": \"146b\",\n" +
            "                  \"dateadjustment\": false\n" +
            "                },\n" +
            "                {\n" +
            "                  \"questioncode\": \"146c\",\n" +
            "                  \"dateadjustment\": false\n" +
            "                },\n" +
            "                {\n" +
            "                  \"questioncode\": \"146d\",\n" +
            "                  \"dateadjustment\": false\n" +
            "                },\n" +
            "                {\n" +
            "                  \"questioncode\": \"146e\",\n" +
            "                  \"dateadjustment\": false\n" +
            "                },\n" +
            "                {\n" +
            "                  \"questioncode\": \"146f\",\n" +
            "                  \"dateadjustment\": false\n" +
            "                },\n" +
            "                {\n" +
            "                  \"questioncode\": \"146g\",\n" +
            "                  \"dateadjustment\": false\n" +
            "                },\n" +
            "                {\n" +
            "                  \"questioncode\": \"146h\",\n" +
            "                  \"dateadjustment\": false\n" +
            "                },\n" +
            "                {\n" +
            "                  \"questioncode\": \"20\",\n" +
            "                  \"dateadjustment\": true\n" +
            "                },\n" +
            "                {\n" +
            "                  \"questioncode\": \"21\",\n" +
            "                  \"dateadjustment\": true\n" +
            "                }\n" +
            "              ]\n" +
            "            },\n" +
            "            \"dateadjustmentreturndateconfigsByFormid\": {\n" +
            "              \"nodes\": [\n" +
            "                {\n" +
            "                  \"formid\": 5,\n" +
            "                  \"questioncode\": \"11\",\n" +
            "                  \"returndatetype\": \"S\"\n" +
            "                },\n" +
            "                {\n" +
            "                  \"formid\": 5,\n" +
            "                  \"questioncode\": \"12\",\n" +
            "                  \"returndatetype\": \"E\"\n" +
            "                }\n" +
            "              ]\n" +
            "            }\n" +
            "          },\n" +
            "          \"responsesByReferenceAndPeriodAndSurvey\": {\n" +
            "            \"nodes\": [\n" +
            "              {\n" +
            "                \"reference\": \"49900534932\",\n" +
            "                \"period\": \"201903\",\n" +
            "                \"survey\": \"023\",\n" +
            "                \"questioncode\": \"11\",\n" +
            "                \"instance\": 0,\n" +
            "                \"response\": \"20190401\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"reference\": \"49900534932\",\n" +
            "                \"period\": \"201903\",\n" +
            "                \"survey\": \"023\",\n" +
            "                \"questioncode\": \"12\",\n" +
            "                \"instance\": 0,\n" +
            "                \"response\": \"20190501\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"reference\": \"49900534932\",\n" +
            "                \"period\": \"201903\",\n" +
            "                \"survey\": \"023\",\n" +
            "                \"questioncode\": \"20\",\n" +
            "                \"instance\": 0,\n" +
            "                \"response\": \"20000\"\n" +
            "              },\n" +
            "              {\n" +
            "                \"reference\": \"49900534932\",\n" +
            "                \"period\": \"201903\",\n" +
            "                \"survey\": \"023\",\n" +
            "                \"questioncode\": \"21\",\n" +
            "                \"instance\": 0,\n" +
            "                \"response\": \"10000\"\n" +
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
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190301\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.1,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190302\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.1,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190303\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.1,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190304\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.1,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190305\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.15,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190306\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.12,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190307\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.17,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190308\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.23,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190309\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.1,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190310\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.13,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190311\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.145,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190312\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.105,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190313\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.15,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190314\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.17,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190315\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.18,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190316\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.17,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190317\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.08,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190318\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.14,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190319\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.14,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190320\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.14,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190321\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.14,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190322\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.15,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190323\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.15,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190324\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.14,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190325\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.13,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190326\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.14,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190327\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.16,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190328\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.15,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190329\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.13,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190330\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.16,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"survey\": \"023\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"tradingdate\": \"20190331\",\n" +
            "          \"domain\": 1,\n" +
            "          \"weight\": 0.13,\n" +
            "          \"periodstart\": \"20190301\",\n" +
            "          \"periodend\": \"20190331\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    \"allContributordateadjustmentconfigs\": {\n" +
            "      \"nodes\": [\n" +
            "        {\n" +
            "          \"reference\": \"49900534932\",\n" +
            "          \"period\": \"201903\",\n" +
            "          \"survey\": \"023\",\n" +
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
            String expectedConfig = "{\"usecalendardays\":false,\"returnedenddate\":\"20190501\",\"period\":\"201903\",\"weights\":[{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190301\",\"weight\":0.1},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190302\",\"weight\":0.1},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190303\",\"weight\":0.1},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190304\",\"weight\":0.1},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190305\",\"weight\":0.15},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190306\",\"weight\":0.12},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190307\",\"weight\":0.17},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190308\",\"weight\":0.23},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190309\",\"weight\":0.1},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190310\",\"weight\":0.13},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190311\",\"weight\":0.145},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190312\",\"weight\":0.105},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190313\",\"weight\":0.15},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190314\",\"weight\":0.17},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190315\",\"weight\":0.18},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190316\",\"weight\":0.17},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190317\",\"weight\":0.08},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190318\",\"weight\":0.14},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190319\",\"weight\":0.14},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190320\",\"weight\":0.14},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190321\",\"weight\":0.14},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190322\",\"weight\":0.15},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190323\",\"weight\":0.15},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190324\",\"weight\":0.14},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190325\",\"weight\":0.13},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190326\",\"weight\":0.14},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190327\",\"weight\":0.16},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190328\",\"weight\":0.15},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190329\",\"weight\":0.13},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190330\",\"weight\":0.16},{\"period\":\"201903\",\"domain\":1,\"survey\":\"023\",\"tradingdate\":\"20190331\",\"weight\":0.13}],\"reference\":\"49900534932\",\"longperiodparameter\":35,\"returnedstartdate\":\"20190401\",\"settomidpoint\":false,\"cellnumber\":1,\"settoequalweighted\":false,\"domain\":1,\"survey\":\"023\",\"responses\":[{\"instance\":0,\"response\":\"20000\",\"questioncode\":\"20\"},{\"instance\":0,\"response\":\"10000\",\"questioncode\":\"21\"}],\"shortperiodparameter\":27,\"averageweekly\":true,\"frozensic\":\"41100\",\"periodstart\":\"20190301\",\"periodend\":\"20190331\"}";
            String actualConfig = dateAdjustmentResponse.parseDateAdjustmentQueryResponse();
            assertEquals(expectedConfig, actualConfig);
        } catch (Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void dateAdjustmentConfigDetails_ThrowsAnException_when_no_contributor(){

        String expectedErrorMessage = "Problem in parsing Selective Editing GraphQL responses There is no contributor for a given survey, reference and periods. Please verify";
        try {
            DateAdjustmentResponse dateAdjustmentResponse = new DateAdjustmentResponse(noContributorGraphQLOutput);
            dateAdjustmentResponse.parseDateAdjustmentQueryResponse();
        } catch (Exception e) {
            String actualMessage = e.getMessage();
            assertEquals(expectedErrorMessage, actualMessage);
            assertTrue(true);

        }
    }

    @Test
    void dateAdjustmentConfigDetails_ThrowsAnException__when_domain_is_null(){

        String expectedErrorMessage = "Problem in parsing Selective Editing GraphQL responses There is no contributor for a given survey, reference and periods. Please verify";
        try {
            DateAdjustmentResponse dateAdjustmentResponse = new DateAdjustmentResponse(noContributorGraphQLOutput);
            dateAdjustmentResponse.parseDateAdjustmentQueryResponse();
        } catch (Exception e) {
            String actualMessage = e.getMessage();
            assertEquals(expectedErrorMessage, actualMessage);
            assertTrue(true);

        }
    }

    @Test
    void date_adjustment_verify_save_query(){
        try {
            DateAdjustmentResponse dateAdjustmentResponse = new DateAdjustmentResponse(lambdaOutput);

            String expectedOutput = "{\"query\": \"mutation upsertDateAdjustment{savedateadjustment(input: {arg0:[{reference: \\\"49900613746\\\",period: \\\"201903\\\",survey: \\\"023\\\",questioncode: \\\"20\\\",instance: 0,adjustedresponse: \\\"10000\\\",averageweeklyadjustedresponse: \\\"25000\\\",createdby: \\\"fisdba\\\"";
            String actualOutput = dateAdjustmentResponse.buildSaveDateAdjustmentQuery();
            assertTrue(actualOutput.contains(expectedOutput));
        } catch (Exception e) {
            assertTrue(false);
        }
    }
}

package uk.gov.ons.collection.entity;

import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uk.gov.ons.collection.exception.InvalidJsonException;

import java.sql.Timestamp;
import java.util.Date;
import java.util.StringJoiner;

@Log4j2
public class DateAdjustmentResponse {



    private JSONObject jsonQlResponse;
    private static final String REFERENCE = "reference";
    private static final String PERIOD = "period";
    private static final String SURVEY = "survey";
    private static final String RESULTS_CELL_NUMBER = "resultscellnumber";
    private static final String CELL_NUMBER = "cellnumber";

    private static final String DOMAIN = "domain";
    private static final String QUESTION_CODE = "questioncode";
    private static final String RESPONSE = "response";
    private static final String RESPONSES = "responses";
    private static final String INSTANCE = "instance";
    private static final String WEIGHT_CONFIG = "weights";
    private static final String FROZENSIC = "frozensic";
    private static final String PERIOD_START = "periodstart";
    private static final String PERIOD_END   = "periodend";
    private static final String TRADING_DATE = "tradingdate";
    private static final String WEIGHT = "weight";
    private static final String RETURNED_START_DATE = "returnedstartdate";
    private static final String RETURNED_END_DATE = "returnedenddate";
    private static final String LONG_PERIOD_PARAMETER = "longperiodparameter";
    private static final String SHORT_PERIOD_PARAMETER = "shortperiodparameter";
    private static final String AVERAGE_WEEKLY = "averageweekly";
    private static final String SET_MID_POINT = "settomidpoint";
    private static final String SET_EQUAL_WEIGHTED = "settoequalweighted";
    private static final String USE_CALENDAR_DAYS = "usecalendardays";
    private static final String RETURNED_DATE_TYPE = "returndatetype";
    private static final String EMPTY_SPACE = "";


    private static final String EMPTY_RESPONSE = "";
    private final Timestamp time = new Timestamp(new Date().getTime());

    public DateAdjustmentResponse(String inputJson) throws InvalidJsonException {
        try {
            jsonQlResponse = new JSONObject(inputJson);
        } catch (JSONException e) {
            log.error("Error in processing Date Adjustment Config Response: " + e.getMessage());
            throw new InvalidJsonException("Given string could not be converted/processed: " + e);
        }
    }



    public JSONArray getDateAdjustments() throws JSONException {

        return jsonQlResponse.getJSONArray("dateadjustments");

    }

    public String buildSaveDateAdjustmentQuery() throws InvalidJsonException {
        var queryJson = new StringBuilder();
        queryJson.append("{\"query\": \"mutation upsertDateAdjustment{savedateadjustment(input: {arg0:");
        queryJson.append("[" + getDateAdjustmentResponses() + "], arg1:");
        queryJson.append("[{" + extractContributorDateAdjustment() + "}]");
        queryJson.append("}){clientMutationId}}\"}");
        log.info("Upsert And DeleteQuery " + queryJson.toString());
        return queryJson.toString();
    }

    private String getDateAdjustmentResponses() throws InvalidJsonException {
        JSONArray dateAdjustmentArray = getDateAdjustments();
        StringJoiner joiner = new StringJoiner(",");
        for (int i = 0; i < dateAdjustmentArray.length(); i++) {
            JSONObject eachQuestionDateAdjustment = dateAdjustmentArray.getJSONObject(i);
            joiner.add("{" + extractDateAdjustmentRowForSave(eachQuestionDateAdjustment) + "}");
        }
        return joiner.toString();
    }


    // Convert a row for the given index and provide it in graphQL desired format
    private String extractDateAdjustmentRowForSave(JSONObject data) throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");
        try {

            joiner.add("reference: \\\"" + jsonQlResponse.getString("reference") + "\\\"");
            joiner.add("period: \\\"" + jsonQlResponse.getString("period") + "\\\"");
            joiner.add("survey: \\\"" + jsonQlResponse.getString("survey") + "\\\"");
            joiner.add("questioncode: \\\"" + data.getString("questioncode") + "\\\"");
            joiner.add("instance: 0");

            joiner = (data.get("adjusted_value") == null || data.get("adjusted_value").toString().equals("null"))
                    ? (joiner.add("adjustedresponse: \\\"" + EMPTY_SPACE + "\\\""))
                    : (joiner.add("adjustedresponse: \\\"" + data.get("adjusted_value") + "\\\""));
            joiner = (data.get("average_weekly_value") == null || data.get("average_weekly_value").toString().equals("null"))
                    ? (joiner.add("averageweeklyadjustedresponse: \\\"" + EMPTY_SPACE + "\\\""))
                    : (joiner.add("averageweeklyadjustedresponse: \\\"" + data.get("average_weekly_value") + "\\\""));
            joiner.add("createdby: \\\"fisdba\\\"");
            joiner.add("createddate: \\\"" + time.toString() + "\\\"");
            joiner.add("lastupdatedby: \\\"fisdba\\\"");
            joiner.add("lastupdateddate: \\\"" +   time.toString() + "\\\"");
            return joiner.toString();

        } catch (Exception err) {
            throw new InvalidJsonException("Error processing validation output json structure: " + err + " JSON: ", err);
        }
    }

    // Convert a row for the given index and provide it in graphQL desired format
    private String extractContributorDateAdjustment() throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");
        try {

            joiner.add("reference: \\\"" + jsonQlResponse.getString("reference") + "\\\"");
            joiner.add("period: \\\"" + jsonQlResponse.getString("period") + "\\\"");
            joiner.add("survey: \\\"" + jsonQlResponse.getString("survey") + "\\\"");
            joiner.add("dateadjustmenterrorflag: \\\"" + jsonQlResponse.get("dateadjustmenterrorflag") + "\\\"");
            joiner.add("dateadjustmentlengthflag: \\\"" + jsonQlResponse.get("dateadjustmentlengthflag") + "\\\"");
            joiner.add("actualdaysreturnedperiod: " + jsonQlResponse.get("actualdaysreturnedperiod"));
            joiner.add("daysreturnedperiod: " + jsonQlResponse.get("daysreturnedperiod"));
            joiner.add("sumtradingweightsoverreturnedperiod: " + jsonQlResponse.get("actualdaysreturnedperiod"));
            joiner.add("sumtradingweightsoveractualreturnedperiod: " + jsonQlResponse.get("sumtradingweightsoveractualreturnedperiod"));
            joiner.add("createdby: \\\"fisdba\\\"");
            joiner.add("createddate: \\\"" + time.toString() + "\\\"");
            joiner.add("lastupdatedby: \\\"fisdba\\\"");
            joiner.add("lastupdateddate: \\\"" +   time.toString() + "\\\"");
            return joiner.toString();

        } catch (Exception err) {
            throw new InvalidJsonException("Error processing validation output json structure: " + err + " JSON: ", err);
        }
    }

    public String parseDateAdjustmentQueryResponse() throws InvalidJsonException {
        JSONArray contribArray;
        JSONObject dateAdjustmentResultObj = new JSONObject();
        int domain = 0;
        int cellNumber = 0;
        try {
            contribArray = jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes");
            if (contribArray.length() > 0) {
                JSONObject contributorObject = contribArray.getJSONObject(0);
                dateAdjustmentResultObj.put(REFERENCE, contributorObject.getString(REFERENCE));
                dateAdjustmentResultObj.put(PERIOD, contributorObject.getString(PERIOD));
                dateAdjustmentResultObj.put(SURVEY, contributorObject.getString(SURVEY));
                dateAdjustmentResultObj.put(FROZENSIC, contributorObject.get(FROZENSIC));
                log.info("Domain Object for a given contributor : " + contributorObject.get(DOMAIN));
                log.info("Results Cell Number Object for a given contributor: " + contributorObject.get(RESULTS_CELL_NUMBER));

                if (contributorObject.get(DOMAIN).toString().equals("null") || contributorObject.get(RESULTS_CELL_NUMBER).toString().equals("null")) {
                    log.info("Into domain null");
                    throw new InvalidJsonException("Either Domain or Results Cell Number is null in Contributor table. Please verify");
                }
                domain = contributorObject.getInt(DOMAIN);
                cellNumber = contributorObject.getInt(RESULTS_CELL_NUMBER);
                log.info("Domain for a given contributor: " + domain);
                log.info("Results Cell Number for a given contributor: " + cellNumber);
                dateAdjustmentResultObj.put(CELL_NUMBER, cellNumber);
                dateAdjustmentResultObj.put(DOMAIN, domain);
                processDateAdjustmentWeightConfiguration(domain, dateAdjustmentResultObj);
                JSONArray contributorDateAdjustmentConfigArray = jsonQlResponse.getJSONObject("data")
                        .getJSONObject("allContributordateadjustmentconfigs").getJSONArray("nodes");
                processContributorDateAdjustmentConfiguration(dateAdjustmentResultObj);

                processResponses(contributorObject, dateAdjustmentResultObj);

            } else {
                throw new InvalidJsonException("There is no contributor for a given survey, " +
                        "reference and periods. Please verify");
            }

        } catch (Exception e) {
            throw new InvalidJsonException("Problem in parsing Selective Editing " +
                    "GraphQL responses " + e.getMessage(), e);
        }
        return dateAdjustmentResultObj.toString();
    }


    private void processResponses(JSONObject contributorObject, JSONObject selectiveEditingResultObj) throws InvalidJsonException {

        JSONArray responseResultArr = new JSONArray();
        JSONArray formDefinitionArray = contributorObject.getJSONObject("formByFormid").getJSONObject("formdefinitionsByFormid").getJSONArray("nodes");
        JSONArray returnedDateConfigArray = contributorObject.getJSONObject("formByFormid")
                .getJSONObject("dateadjustmentreturndateconfigsByFormid").getJSONArray("nodes");
        JSONArray responseArray = contributorObject.getJSONObject("responsesByReferenceAndPeriodAndSurvey").getJSONArray("nodes");
        if (formDefinitionArray.length() > 0) {
            for (int i = 0; i < formDefinitionArray.length(); i++) {
                JSONObject eachFormDefinitionObject = formDefinitionArray.getJSONObject(i);
                String questionCode = eachFormDefinitionObject.getString(QUESTION_CODE);
                var eachResponseObject = new JSONObject();
                boolean dateAdjustmentFlag = eachFormDefinitionObject.getBoolean("dateadjustment");
                boolean matchFound = false;
                for (int j = 0; j < responseArray.length(); j++) {
                    // Performed null check
                    String response = (responseArray.getJSONObject(j).isNull(RESPONSE))
                            ? EMPTY_SPACE : responseArray.getJSONObject(j).getString(RESPONSE);
                    if (questionCode.equals(responseArray.getJSONObject(j).getString(QUESTION_CODE)) && dateAdjustmentFlag) {
                        eachResponseObject.put(QUESTION_CODE, questionCode);
                        eachResponseObject.put(RESPONSE, response);
                        eachResponseObject.put(INSTANCE, responseArray.getJSONObject(j).get(INSTANCE));
                        responseResultArr.put(eachResponseObject);
                        matchFound = true;
                    }
                }
                if (!matchFound && dateAdjustmentFlag) {
                    eachResponseObject.put(QUESTION_CODE, questionCode);
                    eachResponseObject.put(RESPONSE, EMPTY_RESPONSE);
                    eachResponseObject.put(INSTANCE, EMPTY_RESPONSE);
                    responseResultArr.put(eachResponseObject);
                }
            }
            selectiveEditingResultObj.put(RESPONSES, responseResultArr);

        } else {
            throw new InvalidJsonException("There is no FormDefinition for a given survey. Please verify");
        }

        if (returnedDateConfigArray.length() > 0 && responseArray.length() > 0) {
            for (int i = 0; i < returnedDateConfigArray.length(); i++) {
                String returnQuestionCode = returnedDateConfigArray.getJSONObject(i).getString(QUESTION_CODE);
                String returnType = returnedDateConfigArray.getJSONObject(i).getString(RETURNED_DATE_TYPE);
                for (int j = 0; j < responseArray.length(); j++) {
                    if (returnQuestionCode.equals(responseArray.getJSONObject(j).getString(QUESTION_CODE))) {
                        if (returnType.equals("S")) {
                            selectiveEditingResultObj.put(RETURNED_START_DATE, responseArray.getJSONObject(j).getString(RESPONSE));

                        } else if (returnType.equals("E")) {
                            selectiveEditingResultObj.put(RETURNED_END_DATE, responseArray.getJSONObject(j).getString(RESPONSE));
                        }
                        break;
                    }
                }
            }

        } else {
            throw new InvalidJsonException("There is no Returned Start Date and End date configuration. Please verify");
        }

    }

    public JSONObject getJsonQlResponse() {
        return jsonQlResponse;
    }

    private void processDateAdjustmentWeightConfiguration(int domain, JSONObject selectiveEditingResultObj) throws InvalidJsonException {

        JSONArray tradingConfigResultArr = new JSONArray();
        JSONArray tradingWeightConfigArray = jsonQlResponse.getJSONObject("data").getJSONObject("allDateadjustmentweightconfigs").getJSONArray("nodes");
        if (tradingWeightConfigArray.length() > 0) {
            for (int i = 0; i < tradingWeightConfigArray.length(); i++) {
                JSONObject eachTradingConfigObject = tradingWeightConfigArray.getJSONObject(i);
                if (eachTradingConfigObject.getInt(DOMAIN) == domain) {
                    //Match Found
                    var eachResultTradingObject = new JSONObject();
                    eachResultTradingObject.put(SURVEY, eachTradingConfigObject.getString(SURVEY));
                    eachResultTradingObject.put(TRADING_DATE, eachTradingConfigObject.getString(TRADING_DATE));
                    eachResultTradingObject.put(DOMAIN, eachTradingConfigObject.getInt(DOMAIN));
                    eachResultTradingObject.put(WEIGHT, eachTradingConfigObject.get(WEIGHT));
                    eachResultTradingObject.put(PERIOD, eachTradingConfigObject.getString(PERIOD));
                    selectiveEditingResultObj.put(PERIOD_START, eachTradingConfigObject.get(PERIOD_START));
                    selectiveEditingResultObj.put(PERIOD_END, eachTradingConfigObject.get(PERIOD_END));
                    tradingConfigResultArr.put(eachResultTradingObject);
                }
            }
            if (tradingConfigResultArr.length() > 0) {
                selectiveEditingResultObj.put(WEIGHT_CONFIG, tradingConfigResultArr);
            } else {
                throw new InvalidJsonException("There are no trading weights for a given survey, " +
                        "period and domain in the trading weight contributor. Please verify");
            }
        } else {
            throw new InvalidJsonException("There is no trading weight configuration. Please verify");
        }

    }

    private void processContributorDateAdjustmentConfiguration(JSONObject selectiveEditingResultObj) throws InvalidJsonException {
        JSONArray contributorDateAdjustmentConfigArray = jsonQlResponse.getJSONObject("data")
                .getJSONObject("allContributordateadjustmentconfigs").getJSONArray("nodes");
        if (contributorDateAdjustmentConfigArray.length() > 0) {
            JSONObject contributorConfigObject = contributorDateAdjustmentConfigArray.getJSONObject(0);
            selectiveEditingResultObj.put(LONG_PERIOD_PARAMETER, contributorConfigObject.get(LONG_PERIOD_PARAMETER));
            selectiveEditingResultObj.put(SHORT_PERIOD_PARAMETER, contributorConfigObject.get(SHORT_PERIOD_PARAMETER));
            selectiveEditingResultObj.put(AVERAGE_WEEKLY, contributorConfigObject.get(AVERAGE_WEEKLY));
            selectiveEditingResultObj.put(SET_MID_POINT, contributorConfigObject.get(SET_MID_POINT));
            selectiveEditingResultObj.put(SET_EQUAL_WEIGHTED, contributorConfigObject.get(SET_EQUAL_WEIGHTED));
            selectiveEditingResultObj.put(USE_CALENDAR_DAYS, contributorConfigObject.get(USE_CALENDAR_DAYS));
        } else {
            throw new InvalidJsonException("There is no contributor date adjustment configuration. Please verify");
        }

    }
}

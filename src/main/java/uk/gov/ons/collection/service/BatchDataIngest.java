package uk.gov.ons.collection.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lombok.extern.log4j.Log4j2;
import uk.gov.ons.collection.entity.BatchDataQuery;
import uk.gov.ons.collection.exception.InvalidJsonException;
import uk.gov.ons.collection.utilities.UpsertResponse;

import java.net.InetAddress;
import java.util.HashMap;


@Log4j2
public class BatchDataIngest {


    private GraphQlService qlService;
    private JSONArray referenceArray;
    private static final String BUSINESS_SERVICE_PORT = "8088";
    private static final String PROTOCOL = "http://";
    private static final String OUTCOME = "outcome";
    private static final String FAILURE = "Failure";
    private static final String CONTRIBUTOR_ERROR = "Contributor doesn't exist in database ";
    private static final String ERROR = "error";
    private static final String REFERENCE = "reference";
    private static final String PERIOD = "period";
    private static final String SURVEY = "survey";
    private static final String TAKEON_SUCCESSFUL = "TakeOn Successful";
    private static final String BATCH_DATA = "batch_data";
    private static final String FORM_SENT_OUT = "Form Sent Out";
    private static final String DUPLICATE_RECORD_ERROR = "Duplicate Record";


    public BatchDataIngest() {

    }

    public BatchDataIngest(String batchResponses, GraphQlService qlGraphService) throws InvalidJsonException {

        JSONObject inputJson;
        qlService = qlGraphService;
        try {
            inputJson = new JSONObject(batchResponses);
            referenceArray = inputJson.getJSONArray(BATCH_DATA);
            log.info("Reference Array:" + referenceArray);
        } catch (JSONException excep) {
            log.error("Batch responses are not valid JSON" + excep);
            throw new InvalidJsonException("Batch responses cannot be processed: " + batchResponses, excep);
        }
    }

    public void processBatchData(JSONArray outcomesArray) {

        HashMap<String, String> variables = new HashMap<>();
        String referenceExistsResponse;
        // Extract each ref/period/survey and check if exists
        for (int i = 0; i < referenceArray.length(); i++) {
            var outcomeObject = new JSONObject();
            try {

                JSONObject individualObject = referenceArray.getJSONObject(i);
                String reference = individualObject.getString(REFERENCE);
                String period = individualObject.getString(PERIOD);
                String survey = individualObject.getString(SURVEY);
                variables.put(REFERENCE, reference);
                variables.put(PERIOD, period);
                variables.put(SURVEY, survey);
                outcomeObject.put(REFERENCE, reference);
                outcomeObject.put(PERIOD, period);
                outcomeObject.put(SURVEY, survey);
                referenceExistsResponse = qlService
                            .qlSearch(new BatchDataQuery(variables).buildCheckReferenceExistsQuery());
                buildOutcomeJson(referenceExistsResponse, outcomeObject, individualObject);

            } catch (Exception e) {
                log.error("Can't process Batch data responses: " + e);
                outcomeObject.put(OUTCOME, FAILURE);
                outcomeObject.put(ERROR, e.getMessage());
                e.printStackTrace();
            }
            outcomesArray.put(outcomeObject);

        }

        log.info("Outcomes Array object " + outcomesArray.toString());

    }


    private void buildOutcomeJson(String referenceExistsResponse, JSONObject outcomeObject, JSONObject individualObject)
            throws Exception {
        log.info("Reference exists response:" + referenceExistsResponse);
        JSONArray contributorArray = getContributorArray(referenceExistsResponse);
        if (contributorArray != null && contributorArray.isEmpty()) {
            outcomeObject.put(OUTCOME, FAILURE);
            outcomeObject.put(ERROR, CONTRIBUTOR_ERROR);
        } else {
            //Retrieve the Contributor Status
            String contributorStatus = getContributorStatus(contributorArray);
            log.info("Contributor Status {}", contributorStatus);
            if(!contributorStatus.equals(FORM_SENT_OUT)) {
                outcomeObject.put(OUTCOME, FAILURE);
                outcomeObject.put(ERROR, DUPLICATE_RECORD_ERROR);
            } else {
                //Perform Form Tpe Error Checks here
                //Call to Save Responses
                invokeSaveResponsesRequest(individualObject);
                // Call to Update the Form Status
                invokeFormUpdate(individualObject.toString());
                // Finally call to calculate derived values
                invokeDerivedFormulaCalculationRequest(individualObject);
                outcomeObject.put(OUTCOME, TAKEON_SUCCESSFUL);

            }

        }

    }

    private void invokeFormUpdate(String individualObjectJsonStr) throws InvalidJsonException {
        try {
            var upsertResponse = new UpsertResponse(individualObjectJsonStr);
            var contributorStatusQuery = upsertResponse.updateContributorStatus();
            log.info("GraphQL Query for updating Form Status {}", contributorStatusQuery);
            String qlStatusOutput = qlService.qlSearch(contributorStatusQuery);
            log.info("Output after updating the form status {}", qlStatusOutput);
        } catch (JSONException e) {
            log.error("Invalid JSON from contributor exists query response " + e);
            throw new InvalidJsonException("Invalid JSON from contributor exists query response: " + individualObjectJsonStr, e);
        }

    }


    public boolean isContributorEmpty(String referenceExistsResponse) throws InvalidJsonException {
        JSONArray checkArray;
        try {
            JSONObject referenceExistsObject = new JSONObject(referenceExistsResponse);
            checkArray = referenceExistsObject.getJSONObject("data")
                    .getJSONObject("allContributors").getJSONArray("nodes");
        } catch (JSONException e) {
            log.error("Invalid JSON from contributor exists query response " + e);
            throw new InvalidJsonException("Invalid JSON from contributor exists query response: " + referenceExistsResponse, e);
        }

        return checkArray != null && checkArray.isEmpty();
    }

    private String getBusinessLayerAddress() throws Exception {
        InetAddress inetAddress = InetAddress.getLocalHost();
        String businessLayerAddress = inetAddress.getHostAddress();
        log.info("IP Address:" + businessLayerAddress);
        return businessLayerAddress;
    }

    private void invokeSaveResponsesRequest(JSONObject individualObject) throws Exception {
        String businessLayerAddress = getBusinessLayerAddress();
        StringBuilder url = new StringBuilder(PROTOCOL).append(businessLayerAddress).append(":")
                .append(BUSINESS_SERVICE_PORT).append("/response/saveResponses");
        log.info("Request Url: " + url.toString());
        log.info("Individual Object: " + individualObject.toString());
        ApiRequest request = new ApiRequest(url.toString(), individualObject.toString());
        request.apiPostJson();

    }


    private void invokeDerivedFormulaCalculationRequest(JSONObject responseJsonObject) throws Exception {
        StringBuilder derivedUrl = new StringBuilder();
        String businessLayerAddress = getBusinessLayerAddress();
        derivedUrl.append(PROTOCOL).append(businessLayerAddress).append(":")
                    .append(BUSINESS_SERVICE_PORT).append("/response/calculateDerivedQuestions/")
                    .append("reference=").append(responseJsonObject.getString("reference")).append(";period=")
                    .append(responseJsonObject.getString("period")).append(";survey=")
                    .append(responseJsonObject.getString("survey")).append(";");
        ApiRequest derivedRequest = new ApiRequest(derivedUrl.toString());
        log.info("Request Url: " + derivedUrl.toString());
        derivedRequest.apiPostParameters();
    }

    public JSONArray getContributorArray(String referenceExistsResponse) throws InvalidJsonException {

        JSONArray contributorArray;
        try {
            JSONObject referenceExistsObject = new JSONObject(referenceExistsResponse);
            contributorArray = referenceExistsObject.getJSONObject("data")
                    .getJSONObject("allContributors").getJSONArray("nodes");
        } catch (JSONException e) {
            log.error("Invalid JSON from contributor exists query response " + e);
            throw new InvalidJsonException("Invalid JSON from contributor exists query response: " + referenceExistsResponse, e);
        }
        return contributorArray;
    }

    public String getContributorStatus(JSONArray contributorArray) throws InvalidJsonException {

        String contributorStatus = "";
        JSONObject referenceExistsObject;
        try {
            referenceExistsObject = contributorArray.getJSONObject(0);
            contributorStatus = referenceExistsObject.getString("status");
        } catch (JSONException e) {
            log.error("Invalid JSON from contributor query response " + e);
            throw new InvalidJsonException("Invalid JSON from contributor query response: ", e);
        }

        return  contributorStatus;
    }


}


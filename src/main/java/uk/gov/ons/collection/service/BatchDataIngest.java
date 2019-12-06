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

    public BatchDataIngest(String batchResponses, GraphQlService qlGraphService) throws InvalidJsonException {

        JSONObject inputJson;
        try {
            inputJson = new JSONObject(batchResponses);
            referenceArray = inputJson.getJSONArray("batch_data");
            qlService = qlGraphService;

            log.info("Reference Array:" + referenceArray);
        } catch (JSONException excep) {
            log.error("Batch responses are not valid JSON" + excep);
            throw new InvalidJsonException("Batch responses cannot be processed: " + batchResponses, excep);
        }
    }

    public String processBatchData() {

        HashMap<String, String> variables = new HashMap<>();
        String referenceExistsResponse;
        // Extract each ref/period/survey and check if exists
        try {
            for (int i = 0; i < referenceArray.length(); i++) {
                JSONObject individualObject = referenceArray.getJSONObject(i);
                String reference = individualObject.getString("reference");
                String period = individualObject.getString("period");
                String survey = individualObject.getString("survey");
                variables.put("reference", reference);
                variables.put("period", period);
                variables.put("survey", survey);
                referenceExistsResponse = qlService
                        .qlSearch(new BatchDataQuery(variables).buildCheckReferenceExistsQuery());
                if (isContributorEmpty(referenceExistsResponse)) {
                    StringBuilder sbContribError = new StringBuilder("{\"error\":\"")
                            .append("Contributor doesn't exist in database ");
                    sbContribError.append("Reference ").append(reference).append(" Period ").append(period)
                            .append(" Survey ").append(survey).append("\"}");
                    log.info(sbContribError.toString());
                    //Commenting return statement as we want to continue in processing the next response
                    //return sbContribError.toString();
                } else {
                    //Call to Save Responses
                    invokeSaveResponsesRequest(individualObject);
                    // Call to Update the Form Status
                    invokeFormUpdate(individualObject.toString());
                    // Finally call to calculate derived values
                    invokeDerivedFormulaCalculationRequest(individualObject);
                }
            }
        } catch (Exception e) {
            log.error("Can't process Batch data responses: " + e);
            e.printStackTrace();
            return "{\"error\":\"Failed to save Batch Question responses\"}";
        }
        return "{\"Success\":\"Batch Question responses saved successfully\"}";
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


    private boolean isContributorEmpty(String referenceExistsResponse) throws InvalidJsonException {

        JSONObject referenceExistsObject;
        JSONArray checkArray;
        try {
            referenceExistsObject = new JSONObject(referenceExistsResponse);
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


}


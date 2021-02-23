package uk.gov.ons.collection.service;

import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import uk.gov.ons.collection.entity.ContributorSelectiveEditingStatusQuery;
import uk.gov.ons.collection.entity.ContributorSelectiveEditingStatusResponse;
import uk.gov.ons.collection.entity.ValidationData;
import uk.gov.ons.collection.entity.ValidationOverride;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class ValidationOverrideService {

    private GraphQlService qlService;
    private String inputJson;

    private static final String REFERENCE = "reference";
    private static final String PERIOD = "period";
    private static final String SURVEY = "survey";
    private static final String STATUS = "status";
    private static final String FLAG = "flag";


    public ValidationOverrideService(String validationOutputStr, GraphQlService qlGraphService) {
        qlService = qlGraphService;
        inputJson = validationOutputStr;
    }

    public String processValidationDataAndSave() throws Exception {
        ValidationOverride overrideObject = new ValidationOverride(inputJson);
        List<ValidationData> validationDataUiList = overrideObject.extractValidationDataFromUI();

        log.info("Validation Output Data from UI " + validationDataUiList.toString());
        String validationQuery = overrideObject.buildValidationOutputQuery();
        String validationOutputResponse = qlService.qlSearch(validationQuery);
        log.info("Output after executing ValidationOutput GraphQl query " + validationOutputResponse);
        List<ValidationData>  validationDatabaseList = overrideObject.extractValidationDataFromDatabase(validationOutputResponse);
        List<ValidationData> validationUpdatedList = overrideObject.extractUpdatedValidationOutputData(validationDataUiList, validationDatabaseList);
        log.info("Updated List " + validationUpdatedList.toString());
        if (validationUpdatedList.size() > 0) {
            String updateQuery = overrideObject.buildUpdateByArrayQuery(validationUpdatedList);
            log.info("GraphQl query for batch update " + updateQuery);
            String validationOutputUpdateResponse = qlService
                    .qlSearch(updateQuery);
            log.info("Output after executing ValidationOutput UpdateGraphQl query " + validationOutputUpdateResponse);
        }

        // Updating the Form Status for Override
        int triggerCount = validationDatabaseList.size();
        log.info("Trigger Count {}", triggerCount);
        String statusText = "";
        if (triggerCount > 0) {
            statusText = overrideObject.processStatusMessage(triggerCount);
            String contributorStatusQuery = overrideObject.buildContributorStatusQuery(statusText);
            log.info("GraphQL Query for updating Override Form Status {}", contributorStatusQuery);
            String qlStatusOutput = qlService.qlSearch(contributorStatusQuery);
            log.info("Output after updating the Override form status {}", qlStatusOutput);
        }
        log.info("Before Processing BMP contract ");
        log.info("Reference: {}", overrideObject.getReference());
        log.info("Period: {}", overrideObject.getPeriod());
        log.info("Survey: {}", overrideObject.getSurvey());
        //Process BPM Contract
        Map<String, String> variables = new HashMap<String, String>();

        variables.put(REFERENCE, overrideObject.getReference());
        variables.put(PERIOD, overrideObject.getPeriod());
        variables.put(SURVEY, overrideObject.getSurvey());

        String response = "";
        ContributorSelectiveEditingStatusQuery contributorStatusQuery = null;

        try {
            contributorStatusQuery = new ContributorSelectiveEditingStatusQuery(variables);
            String queryStr = contributorStatusQuery.buildContributorSelectiveEditingStatusQuery();
            log.info("ContributorstatusQuery GraphQL query: " + queryStr);
            String contributorstatusQueryOutput = qlService.qlSearch(queryStr);
            log.info("Contributor Status Query Output: " + contributorstatusQueryOutput);
            ContributorSelectiveEditingStatusResponse contributorStatusResponse =
                    new ContributorSelectiveEditingStatusResponse(contributorstatusQueryOutput);
            JSONObject contributorJsonObject = contributorStatusResponse.parseContributorStatusQueryResponse();
            contributorJsonObject.put(STATUS, overrideObject.processBPMStatusMessage(statusText));
            response = contributorJsonObject.toString();
            log.info("BAW contract before sending to UI: " + response);
        } catch (Exception err) {
            log.error("Exception found in Contributor Status API: " + err.getMessage());
        }

        return response;

    }

}

package uk.gov.ons.collection.service;

import lombok.extern.log4j.Log4j2;
import uk.gov.ons.collection.entity.ValidationData;
import uk.gov.ons.collection.entity.ValidationOverride;

import java.util.List;

@Log4j2
public class ValidationOverrideService {

    private GraphQlService qlService;
    private String inputJson;


    public ValidationOverrideService(String validationOutputStr, GraphQlService qlGraphService) {
        qlService = qlGraphService;
        inputJson = validationOutputStr;
    }

    public void processValidationDataAndSave() throws Exception {
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
        if (triggerCount > 0) {
            String contributorStatusQuery = overrideObject.buildContributorStatusQuery(triggerCount);
            log.info("GraphQL Query for updating Override Form Status {}", contributorStatusQuery);
            String qlStatusOutput = qlService.qlSearch(contributorStatusQuery);
            log.info("Output after updating the Override form status {}", qlStatusOutput);
        }

    }

}

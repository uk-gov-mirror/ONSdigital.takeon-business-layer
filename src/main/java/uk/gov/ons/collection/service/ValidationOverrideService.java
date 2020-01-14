package uk.gov.ons.collection.service;

import lombok.extern.log4j.Log4j2;
import uk.gov.ons.collection.entity.BatchDataQuery;
import uk.gov.ons.collection.entity.ValidationData;
import uk.gov.ons.collection.entity.ValidationOverride;
import uk.gov.ons.collection.exception.InvalidJsonException;

import java.util.List;

@Log4j2
public class ValidationOverrideService {

    private GraphQlService qlService;
    private String inputJson;

    public ValidationOverrideService() {

    }

    public ValidationOverrideService(String validationOutputStr, GraphQlService qlGraphService) {
        qlService = qlGraphService;
        inputJson = validationOutputStr;
    }

    public void processValidationDataAndSave() throws Exception {
        ValidationOverride overrideObject = new ValidationOverride(inputJson);
        List<ValidationData> validationDataUIList = overrideObject.extractValidationDataFromUI();

        log.info("Reference " + overrideObject.getReference());
        log.info("Period " + overrideObject.getPeriod());
        log.info("Survey " + overrideObject.getSurvey());
        log.info("Validation Output Data " + validationDataUIList.toString());

        String validationQuery = overrideObject.buildValidationOutputQuery();
        log.info(" GraphQl Service " + qlService);
        String validationOutputResponse = qlService
                .qlSearch(validationQuery);
        log.info("Output ValidationOutput table " + validationOutputResponse);
        List<ValidationData>  validationDatabaseList = overrideObject.extractValidationDataFromDatabase(validationOutputResponse);

        List<ValidationData> validationUpdatedList = overrideObject.extractUpdatedValidationOutputData(validationDataUIList, validationDatabaseList);

        log.info ("Updated List " + validationUpdatedList.toString());

    }




}

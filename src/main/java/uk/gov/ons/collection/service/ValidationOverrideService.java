package uk.gov.ons.collection.service;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ValidationOverrideService {

    private GraphQlService qlService;

    public ValidationOverrideService() {

    }

    public ValidationOverrideService(GraphQlService qlGraphService ) {
        qlService = qlGraphService;
    }

    public void processValidationDataAndSave() {

    }


}

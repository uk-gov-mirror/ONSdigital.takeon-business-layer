package uk.gov.ons.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.*;
import uk.gov.ons.collection.utilities.Helpers;

@Service
public class ApiCallerSQL implements ApiCaller {

    @Autowired
    ValidationConfigService validationConfigService;

    @Autowired
    ContributorService contributorService;

    @Autowired
    FormDefinitionService formDefinitionService;

    @Autowired
    CurrentResponseService currentResponseService;

    @Autowired
    RunValidationService validationService;

    @Autowired
    ValidationApiCallerService validationApiCallerService;

    @Override
    public Iterable<ContributorEntity> loadContributors(String reference, String period, String survey) {
        return contributorService.generalSearch(new Helpers().buildUriParameters(reference, period, survey));
    }

    @Override
    public Iterable<ValidationFormEntity> loadValidationConfig(int formId) {
        return validationConfigService.getValidationConfig("FormID="+formId);
    }

    @Override
    public Iterable<QuestionResponseEntity> loadResponses(String reference, String period, String survey) {
        return currentResponseService.getCurrentResponses(new Helpers().buildUriParameters(reference, period, survey));
    }

    @Override
    public Iterable<FormDefintionEntity> loadFormDefinition(String reference, String period, String survey){
        return formDefinitionService.getForm(new Helpers().buildUriParameters(reference, period, survey));
    }

    @Override
    public Iterable<ReturnedValidationOutputs> callValidationApi(String ruleName, String reference, String period, String survey) {
        if(ruleName.equals("VP")){
              return validationApiCallerService.callValuePresentApi(new Helpers().buildUriParameters(reference, period, survey));
        }
        else if (ruleName.equals("POPM")) {
            return validationApiCallerService.callValueChangeApi(new Helpers().buildUriParameters(reference, period, survey));
        }
        return null;
    }
}

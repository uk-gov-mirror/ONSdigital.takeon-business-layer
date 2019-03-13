package uk.gov.ons.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.ons.collection.entity.ContributorEntity;
import uk.gov.ons.collection.entity.FormDefintionEntity;
import uk.gov.ons.collection.entity.QuestionResponseEntity;
import uk.gov.ons.collection.entity.ValidationFormEntity;
import uk.gov.ons.collection.utilities.Helpers;

import java.util.List;

public class DataLoaderSQL implements DataLoader {

    @Autowired
    ValidationConfigService validationConfigService;

    @Autowired
    ContributorService contributorService;

    @Autowired
    FormDefinitionService formDefinitionService;

    @Autowired
    CurrentResponseService currentResponseService;

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
}

package uk.gov.ons.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.*;
import uk.gov.ons.collection.utilities.Helpers;

@Service
public class ApiCallerSQL implements ApiCaller {

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
    public Iterable<QuestionResponseEntity> loadResponses(String reference, String period, String survey) {
        return currentResponseService.getCurrentResponses(new Helpers().buildUriParameters(reference, period, survey));
    }

    @Override
    public Iterable<FormDefinitionEntity> loadFormDefinition(String reference, String period, String survey){
        return formDefinitionService.getForm(new Helpers().buildUriParameters(reference, period, survey));
    }

}

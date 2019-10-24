package uk.gov.ons.collection.service;

import uk.gov.ons.collection.entity.ContributorEntity;
import uk.gov.ons.collection.entity.FormDefinitionEntity;
import uk.gov.ons.collection.entity.QuestionResponseEntity;

public interface ApiCaller {

    Iterable<ContributorEntity> loadContributors(String reference, String period, String survey);

    Iterable<QuestionResponseEntity> loadResponses(String reference, String period, String survey);

    Iterable<FormDefinitionEntity> loadFormDefinition(String reference, String period, String survey);
    
}

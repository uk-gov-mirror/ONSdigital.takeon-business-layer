package uk.gov.ons.collection.service;

import uk.gov.ons.collection.entity.*;

public interface ApiCaller {
    Iterable<ContributorEntity> loadContributors(String reference, String period, String survey);
    Iterable<QuestionResponseEntity> loadResponses(String reference, String period, String survey);
    Iterable<FormDefinitionEntity> loadFormDefinition(String reference, String period, String survey);
}

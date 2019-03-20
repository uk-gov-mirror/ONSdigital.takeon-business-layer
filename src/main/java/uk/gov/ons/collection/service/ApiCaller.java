package uk.gov.ons.collection.service;

import uk.gov.ons.collection.entity.*;

public interface ApiCaller {
    Iterable<ContributorEntity> loadContributors(String reference, String period, String survey);
    Iterable<ValidationFormEntity> loadValidationConfig(int formId);
    Iterable<QuestionResponseEntity> loadResponses(String reference, String period, String survey);
    Iterable<FormDefintionEntity> loadFormDefinition(String reference, String period, String survey);
    Iterable<ReturnedValidationOutputs> callValidationApi(String ruleName, String reference, String period, String survey);
}

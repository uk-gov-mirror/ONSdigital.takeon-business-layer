package uk.gov.ons.collection.service;

import uk.gov.ons.collection.entity.ContributorEntity;
import uk.gov.ons.collection.entity.FormDefintionEntity;
import uk.gov.ons.collection.entity.QuestionResponseEntity;
import uk.gov.ons.collection.entity.ValidationFormEntity;

interface DataLoader {

    public Iterable<ContributorEntity> loadContributors(String reference, String period, String survey);
    public <T> Iterable<T> loadValidationConfig(int formId);
    public Iterable<QuestionResponseEntity> loadResponses(String reference, String period, String survey);
    Iterable<FormDefintionEntity> loadFormDefinition(String reference, String period, String survey);
}

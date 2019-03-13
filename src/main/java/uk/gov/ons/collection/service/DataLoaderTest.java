package uk.gov.ons.collection.service;

import uk.gov.ons.collection.entity.ContributorEntity;
import uk.gov.ons.collection.entity.QuestionResponseEntity;
import uk.gov.ons.collection.entity.ValidationFormEntity;

public class DataLoaderTest implements DataLoader {

    private Iterable<ContributorEntity> contributors;
    private Iterable<ValidationFormEntity> validationConfig;

    public DataLoaderTest(Iterable<ContributorEntity> contributors, Iterable<ValidationFormEntity> validationConfig) {
        this.contributors = contributors;
        this.validationConfig = validationConfig;
    }

    public DataLoaderTest() {
    }

    @Override
    public Iterable<ContributorEntity> loadContributors(String reference, String period, String survey) {
        return contributors;
    }

    @Override
    public Iterable<ValidationFormEntity> loadValidationConfig(int formId) {
        return validationConfig;
    }

    @Override
    public Iterable<QuestionResponseEntity> loadResponses() {
        return null;
    }
}

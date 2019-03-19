package uk.gov.ons.collection.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import uk.gov.ons.collection.entity.*;

@Builder
@AllArgsConstructor
public class ApiCallerTest implements ApiCaller {

    private Iterable<ContributorEntity> contributors;
    private Iterable<ValidationFormEntity> validationConfig;
    private Iterable<QuestionResponseEntity> questionResponse;
    private Iterable<FormDefintionEntity> defintionEntities;
    private Iterable<ValidationFormEntity> returnedValidationOutputs;

    public ApiCallerTest(Iterable<ContributorEntity> contributors, Iterable<ValidationFormEntity> validationConfig) {
        this.contributors = contributors;
        this.validationConfig = validationConfig;
    }

    public ApiCallerTest() {
    }

    public ApiCallerTest(Iterable<QuestionResponseEntity> questionResponse, Iterable<FormDefintionEntity> defintionEntities, String blank) {
        this.questionResponse = questionResponse;
        this.defintionEntities = defintionEntities;
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
    public Iterable<QuestionResponseEntity> loadResponses(String reference, String period, String survey) {
        return questionResponse;
    }

    @Override
    public Iterable<FormDefintionEntity> loadFormDefinition(String reference, String period, String survey) {
        return defintionEntities;
    }

    @Override
    public Iterable<ValidationFormEntity> callValidationApi(String ruleName, String reference, String period, String survey) {
        return returnedValidationOutputs;
    }
}

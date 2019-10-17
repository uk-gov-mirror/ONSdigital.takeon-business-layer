package uk.gov.ons.collection.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import uk.gov.ons.collection.entity.*;

@Builder
@AllArgsConstructor
public class ApiCallerTest implements ApiCaller {

    private Iterable<ContributorEntity> contributors;
    private Iterable<QuestionResponseEntity> questionResponse;
    private Iterable<FormDefinitionEntity> definitionEntities;

    public ApiCallerTest() {}

    @Override
    public Iterable<ContributorEntity> loadContributors(String reference, String period, String survey) {
        return contributors;
    }

    @Override
    public Iterable<QuestionResponseEntity> loadResponses(String reference, String period, String survey) {
        return questionResponse;
    }

    @Override
    public Iterable<FormDefinitionEntity> loadFormDefinition(String reference, String period, String survey) {
        return definitionEntities;
    }

}

package uk.gov.ons.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.*;
import uk.gov.ons.collection.utilities.Helpers;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

public class ValuePresentWrangler {


    private String reference;
    private String period;
    private String survey;
    private String uri;
    private Iterable<ContributorEntity> contributorEntities;
    private Iterable<QuestionResponseEntity> questionResponseEntities;
    private Iterable<ValidationFormEntity> validationConfig;
    private List<ValidationFormEntity> filteredValidationFormEntities;
    List<ValidationFormEntity> validationConfigEntitiesToReturn = new ArrayList<>();
    private final String algo = "https://api.algpoc.com/v1/algo/dllmorgan/ValidationValuePresent/1.0.0";
    private ApiCaller apiCaller;

    public ValuePresentWrangler(String reference, String period, String survey,  ApiCaller caller) {
        this.reference = reference;
        this.period = period;
        this.survey = survey;
        this.apiCaller = caller;
    }

    public void getContributor() {
        this.contributorEntities = apiCaller.loadContributors(reference, period, survey);
    }

    public void getResponses(){
        this.questionResponseEntities = apiCaller.loadResponses(reference, period, survey);
    }

    public void getValidationConfig(){
        List<ContributorEntity> contributor = new Helpers.ParseIterable().parseIterable(contributorEntities);
        this.validationConfig = apiCaller.loadValidationConfig(contributor.get(0).formid);
    }

    public void filterConfig(){
        Iterable<ValidationFormEntity> validationFormEntity = validationConfig;
        List<ValidationFormEntity> formEntityList = new Helpers.ParseIterable().parseIterable(validationFormEntity);
        filteredValidationFormEntities = formEntityList.stream().filter(element -> element.getValidationCode().equals("VP"))
                .collect(Collectors.toList());
    }

    public Iterable<QuestionResponseEntity> checkAllQcodesPresent() {
        return new Helpers().checkAllQuestionsPresent(apiCaller, reference, period, survey);
    }

    public void matchResponsesToConfig(){
        for(QuestionResponseEntity questionResponseEntity: questionResponseEntities){
            for (ValidationFormEntity validationFormEntity: validationConfig){
                if(Objects.equals(questionResponseEntity.getQuestionCode().trim(), validationFormEntity.getQuestionCode())){
                    validationFormEntity.setCurrentResponse(questionResponseEntity.getResponse());
                    validationConfigEntitiesToReturn.add(validationFormEntity);
                }
            }
        }
        System.out.println("Current Config list: "+ validationConfigEntitiesToReturn);
    }

//    public Iterable<ValidationFormEntity> runAlgoValidation(Iterable<ValidationFormEntity> validationConfig){
//        for(ValidationFormEntity validationFormEntity: validationConfig){
//            System.out.println(validationFormEntity.toString());
//            CallAlgorithmia callAlgorithmia = new CallAlgorithmia(algo, validationFormEntity.getPayload());
//            validationFormEntity.setIsTriggered(callAlgorithmia.callService());
//        }
//        return runValidation();
//    }

    public Iterable<ValidationFormEntity> setPayloadAndReturnFormEntities(){
        List<ValidationFormEntity> validationFormEntitiesWithOutputs = new ArrayList<>();
        for(ValidationFormEntity entity: validationConfigEntitiesToReturn){
            entity.setPayload(generateJson(entity.getCurrentResponse()));
            validationFormEntitiesWithOutputs.add(entity);
        }
        return validationFormEntitiesWithOutputs;
    }

    public String generateJson(String response){
        return "{\"value\":" + "\""+response+"\""+"}";
    }

    public void runVpWrangler(){
        getContributor();
        getResponses();
        getValidationConfig();
        matchResponsesToConfig();
    }

}

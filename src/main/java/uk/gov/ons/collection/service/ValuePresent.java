package uk.gov.ons.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.collection.controller.ValidationJunction;
import uk.gov.ons.collection.entity.*;
import uk.gov.ons.collection.utilities.Helpers;

import java.util.*;

@Service
public class ValuePresent {

    @Autowired
    ContributorService contributorService;

    @Autowired
    CurrentResponseService currentResponseService;

    @Autowired
    ValidationConfigService validationConfigService;

    @Autowired
    RunValidationService runValidationService;

    private String reference;
    private String period;
    private String survey;
    private String uri;
    private Iterable<ContributorEntity> contributorEntities;
    private Iterable<QuestionResponseEntity> questionResponseEntities;
    private Iterable<ValidationFormEntity> validationConfig;
    private List<ValidationFormEntity> validationConfigEntitiesToReturn = new ArrayList<>();
    private final String algo = "https://api.algpoc.com/v1/algo/dllmorgan/ValidationValuePresent/1.0.0";

//    public ValuePresent(){}
//
//    public ValuePresent(Map<String, String> parameters){
//        period = parameters.get("period");
//        reference = parameters.get("reference");
//        survey = parameters.get("survey");
//        uri = new Helpers().buildUriParameters(reference, period, survey);
//    }

    public void getContributor() {
        contributorEntities = contributorService.generalSearch(uri);
    }

    public Iterable<QuestionResponseEntity> getResponses(){
        return currentResponseService.getCurrentResponses(uri);
    }

    public Iterable<ValidationFormEntity> getValidationConfig(){
        for(ContributorEntity element: contributorEntities) {
            return validationConfigService.getValidationConfig("FormID="+element.getFormId().toString());
        }
        return null;
    }

    public void buildUri(Map<String, String> parameters){
        period = parameters.get("period");
        reference = parameters.get("reference");
        survey = parameters.get("survey");
        uri = new Helpers().buildUriParameters(reference, period, survey);
    }

    public void prepareData(){
        getContributor();
        getResponses();
        getValidationConfig();
        for(QuestionResponseEntity responseEntity: questionResponseEntities){
            for(ValidationFormEntity validationFormEntity: validationConfig){
                if(responseEntity.getQuestionCode().equals(validationFormEntity.getQuestionCode())){
                    System.out.println(responseEntity.toString());
                    System.out.println(validationFormEntity.toString()+"\n");
                    validationFormEntity.setCurrentResponse(responseEntity.getResponse());
                    validationConfigEntitiesToReturn.add(validationFormEntity);
                }
            }
        }
    }

    public Iterable<ValidationFormEntity> matchResponsesToConfig(Iterable<QuestionResponseEntity> responses,
                                                   Iterable<ValidationFormEntity> config){
        System.out.println("match responses: "+responses);
        System.out.println("match responses: "+config);
        for(QuestionResponseEntity questionResponseEntity: responses){
            for (ValidationFormEntity validationFormEntity: config){
                if(Objects.equals(questionResponseEntity.getQuestionCode().trim(), validationFormEntity.getQuestionCode())){
                    validationFormEntity.setCurrentResponse(questionResponseEntity.getResponse());
                    validationConfigEntitiesToReturn.add(validationFormEntity);
                }
            }
        }
        return validationConfigEntitiesToReturn;
    }

    public Iterable<ValidationFormEntity> runAlgoValidation(){
        for(ValidationFormEntity validationFormEntity: validationConfigEntitiesToReturn){
            System.out.println(validationFormEntity.toString());
            CallAlgorithmia callAlgorithmia = new CallAlgorithmia(algo, validationFormEntity.getPayload());
            // System.out.println("Calling service: "+callAlgorithmia.callService());
            validationFormEntity.setIsTriggered(callAlgorithmia.callService());
        }
        System.out.println(validationConfigEntitiesToReturn);
        return validationConfigEntitiesToReturn;
    }

    public Iterable<ValidationFormEntity> runValidation(){
        System.out.println("RUNNING VALIDATION");
        for(ValidationFormEntity validationFormEntity: validationConfigEntitiesToReturn){
            System.out.println(validationFormEntity.getCurrentResponse());
            String payload = "{\"value\":" + "\""+validationFormEntity.getCurrentResponse()+"\""+"}";
            System.out.println(payload);
            ReturnedValidationOutputs returnedValidationOutputs = runValidationService.runValidation(payload);
            System.out.println(returnedValidationOutputs.toString());
            validationFormEntity.setIsTriggered(returnedValidationOutputs.isTriggered());
        }
        System.out.println(validationConfigEntitiesToReturn);
        return validationConfigEntitiesToReturn;
    }
}

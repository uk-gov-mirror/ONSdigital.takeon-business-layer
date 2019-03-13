package uk.gov.ons.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.*;
import uk.gov.ons.collection.utilities.Helpers;
import uk.gov.ons.collection.utilities.ParseIterable;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ValuePresentWrangler {

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
    private final String algo = "https://api.algpoc.com/v1/algo/dllmorgan/ValidationValuePresent/1.0.0";

//    public ValuePresentWrangler(){}
//
//    public ValuePresentWrangler(Map<String, String> parameters){
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

    public Iterable<ValidationFormEntity> filterConfig(){
        Iterable<ValidationFormEntity> validationFormEntity = validationConfig;
        List<ValidationFormEntity> formEntityList = new ParseIterable().parseIterable(validationFormEntity);
        return formEntityList.stream().filter(element -> element.getValidationCode().equals("VP"))
                .collect(Collectors.toList());
    }

    public void getValidationConfig(){
        for(ContributorEntity element: contributorEntities) {
            validationConfig =
                    validationConfigService.getValidationConfig("FormID="+element.getFormId().toString());
        }
    }

    public void buildUri(Map<String, String> parameters){
        period = parameters.get("period");
        reference = parameters.get("reference");
        survey = parameters.get("survey");
        uri = new Helpers().buildUriParameters(reference, period, survey);
    }

    public Iterable<ValidationFormEntity> matchResponsesToConfig(Iterable<QuestionResponseEntity> responses,
                                                   Iterable<ValidationFormEntity> config){

        List<ValidationFormEntity> validationConfigEntitiesToReturn = new ArrayList<>();
        for(QuestionResponseEntity questionResponseEntity: responses){
            for (ValidationFormEntity validationFormEntity: config){
                if(Objects.equals(questionResponseEntity.getQuestionCode().trim(), validationFormEntity.getQuestionCode())){
                    validationFormEntity.setCurrentResponse(questionResponseEntity.getResponse());
                    validationConfigEntitiesToReturn.add(validationFormEntity);
                }
            }
        }
        System.out.println("Current Config list: "+validationConfigEntitiesToReturn);
        return validationConfigEntitiesToReturn;
    }

    public Iterable<ValidationFormEntity> runAlgoValidation(Iterable<ValidationFormEntity> validationConfig){
        for(ValidationFormEntity validationFormEntity: validationConfig){
            System.out.println(validationFormEntity.toString());
            CallAlgorithmia callAlgorithmia = new CallAlgorithmia(algo, validationFormEntity.getPayload());
            validationFormEntity.setIsTriggered(callAlgorithmia.callService());
        }
        return runValidation(validationConfig);
    }

    public Iterable<ValidationFormEntity> runValidation(Iterable<ValidationFormEntity> validationConfig){
        for(ValidationFormEntity validationFormEntity: validationConfig){
            String payload = "{\"value\":" + "\""+validationFormEntity.getCurrentResponse()+"\""+"}";
            ReturnedValidationOutputs returnedValidationOutputs = runValidationService.runValidation(payload);
            validationFormEntity.setIsTriggered(returnedValidationOutputs.isTriggered());
        }
        return validationConfig;
    }
}

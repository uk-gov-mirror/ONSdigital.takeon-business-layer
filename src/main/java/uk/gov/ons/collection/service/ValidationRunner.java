package uk.gov.ons.collection.service;

import com.google.common.collect.Lists;
import org.springframework.http.ResponseEntity;
import uk.gov.ons.collection.entity.ContributorEntity;
import uk.gov.ons.collection.entity.QuestionResponseEntity;
import uk.gov.ons.collection.entity.ReturnedValidationOutputs;
import uk.gov.ons.collection.entity.ValidationFormEntity;
import uk.gov.ons.collection.utilities.Helpers;

import java.util.*;
import java.util.stream.Collectors;

public class ValidationRunner {

    private String reference;
    private String period;
    private String survey;
    private ApiCaller apiCaller;
    private Iterable<QuestionResponseEntity> responseEntities;
    private Iterable<ValidationFormEntity> validationConfig;
    private Map<String, List<QuestionResponseEntity>> validationsMap = new HashMap<>();

    public ValidationRunner(String reference, String period, String survey, ApiCaller apiCaller) {
        this.reference = reference;
        this.period = period;
        this.survey = survey;
        this.apiCaller = apiCaller;
    }

    // We should only ever be given 1 contributor here, but if more than 1 is given we get the formID of the first one
    public int getFormIdFromForm(){
        Iterable<ContributorEntity> contributors = apiCaller.loadContributors(reference, period, survey);
        for(ContributorEntity entity: contributors){
            return entity.getFormid();
        }
        return 0;
    }

    public List<String> getUniqueListOfRules(int formId){
        List<String> rules = new ArrayList<>();
        validationConfig = apiCaller.loadValidationConfig(formId);
        for(ValidationFormEntity validationForms: validationConfig){
            if(!rules.contains(validationForms.getValidationCode())){
                rules.add(validationForms.getValidationCode());
            }
        }
        return rules;
    }

    // Check we have a full set of questions. If not add the questionCode and initialise the response to an empty string
    public List<ReturnedValidationOutputs> callEachValidationApi(List<String> rules){
        List<ReturnedValidationOutputs> outputs = new ArrayList<>();
        for(String rule: rules) {
            Iterable<ReturnedValidationOutputs> response = apiCaller.callValidationApi(rule, reference, period, survey);
            outputs.addAll(Lists.newArrayList(response));
        }
        return outputs;
    }

    public Iterable<ReturnedValidationOutputs> runValidations(){
        int formId = getFormIdFromForm();
        List<String> listOfRules = getUniqueListOfRules(formId);
//        createMap(listOfRules);
//        createValidationBatches();
        return callEachValidationApi(listOfRules);
    }

    public void createValidationBatches(){
        List<QuestionResponseEntity> responseEntitiesList = new Helpers().checkAllQuestionsPresent(apiCaller, reference, period, survey);
        for(String rule: validationsMap.keySet()) {
            List<ValidationFormEntity> filteredForms = filterForms(rule);
            Collections.sort(filteredForms, new SortByQuestionCode());
            for (QuestionResponseEntity responseEntity : responseEntitiesList) {
                for (ValidationFormEntity formEntity : filteredForms) {
                    if (responseEntity.getQuestionCode().equals(formEntity.getQuestionCode())) {
                        validationsMap.get(rule).add(responseEntity);
                    }
                }
            }
        }
    }

    public void createBatchesTwo(){
        Collections.sort(new Helpers.ParseIterable().parseIterable(validationConfig), new SortByQuestionCode());
        for(ValidationFormEntity element: validationConfig){
            Map<String, String> tempHold = new HashMap<>();
            tempHold.put(element.getValidationCode(), element.getQuestionCode());
        }
    }

    public List<ValidationFormEntity> filterForms(String validationCode){
        return new Helpers.ParseIterable().parseIterable(validationConfig).stream()
                .filter(element -> element.getValidationCode().equals(validationCode.trim()))
                .collect(Collectors.toList());
    }

    public void createMap(List<String> rules){
        for(String rule: rules){
            validationsMap.put(rule, new ArrayList<>());
        }
    }

}

class SortByQuestionCode implements Comparator<ValidationFormEntity>{
    public int compare(ValidationFormEntity a, ValidationFormEntity b){
        return Integer.parseInt(a.getQuestionCode()) - Integer.parseInt(b.getQuestionCode());
    }
}
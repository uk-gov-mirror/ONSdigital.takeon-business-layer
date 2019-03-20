package uk.gov.ons.collection.service;

import com.google.common.collect.Lists;
import uk.gov.ons.collection.entity.ContributorEntity;
import uk.gov.ons.collection.entity.ReturnedValidationOutputs;
import uk.gov.ons.collection.entity.ValidationFormEntity;

import java.util.ArrayList;
import java.util.List;

public class ValidationRunner {

    private String reference;
    private String period;
    private String survey;
    private ApiCaller apiCaller;

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
            System.out.println(entity.toString());
            return entity.getFormid();
        }
        return 0;
    }

    public List<String> getUniqueListOfRule(int formId){
        List<String> rules = new ArrayList<>();
        Iterable<ValidationFormEntity> validationConfig = apiCaller.loadValidationConfig(formId);
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
        List<String> listOfRules = getUniqueListOfRule(formId);
        return callEachValidationApi(listOfRules);
    }
}

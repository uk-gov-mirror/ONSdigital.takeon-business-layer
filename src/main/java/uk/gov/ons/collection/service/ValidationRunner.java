package uk.gov.ons.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.ContributorEntity;
import uk.gov.ons.collection.entity.QuestionResponseEntity;
import uk.gov.ons.collection.entity.ReturnedValidationOutputs;
import uk.gov.ons.collection.entity.ValidationFormEntity;
import uk.gov.ons.collection.utilities.Helpers;

import java.util.ArrayList;
import java.util.List;

@Service
public class ValidationRunner {

    private String reference;
    private String period;
    private String survey;
    private ApiCaller apiCaller;
    private Iterable<ContributorEntity> contributors;
    private Iterable<ValidationFormEntity> config;
    private Helpers helpers = new Helpers();

    public ValidationRunner(String reference, String period, String survey, ApiCaller apiCaller) {
        this.reference = reference;
        this.period = period;
        this.survey = survey;
        this.apiCaller = apiCaller;
    }


    // We should only ever be given 1 contributor here, but if more than 1 is given we get the formID of the first one
    public int getFormIdFromForm(){
        contributors = apiCaller.loadContributors(reference, period, survey);
        for(ContributorEntity entity: contributors){
            System.out.println(entity.toString());
            return entity.getFormid();
        }
        return 0;
    }


    public List<String> getUniqueListOfRule(int formId){
        List<String> rules = new ArrayList<>();
        config = apiCaller.loadValidationConfig(formId);
        for(ValidationFormEntity validationForms: config){
            if(!rules.contains(validationForms.getValidationCode())){
                rules.add(validationForms.getValidationCode());
            }
        }
        return rules;
    }
    // Check we have a full set of questions. If not add the questionCode and initialise the response to an empty string


    public Iterable<ReturnedValidationOutputs> pickRulesToRun(List<String> rules){
        List<ReturnedValidationOutputs> outputs = new ArrayList<>();
        for(String rule: rules) {
            outputs.add(apiCaller.callValidationApi(rule, reference, period, survey));
        }
        return outputs;
    }
l
    public void runValidations(){
        int formId = getFormIdFromForm();
        // Iterable<QuestionResponseEntity> completeIterableOfQcodes = checkAllQcodesPresent();
        List<String> listOfRules = getUniqueListOfRule(formId);
        pickRulesToRun(listOfRules);

    }
}

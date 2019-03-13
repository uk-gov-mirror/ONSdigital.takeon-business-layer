package uk.gov.ons.collection.service;

import uk.gov.ons.collection.entity.ContributorEntity;
import uk.gov.ons.collection.entity.FormDefintionEntity;
import uk.gov.ons.collection.entity.QuestionResponseEntity;
import uk.gov.ons.collection.entity.ValidationFormEntity;
import uk.gov.ons.collection.utilities.ParseIterable;

import java.util.ArrayList;
import java.util.List;

public class ValidationRunner {

    private List<ValidationFormEntity> validationFormEntity;
    private String reference;
    private String period;
    private String survey;
    private DataLoader dataLoader;

    public ValidationRunner(String reference, String period, String survey, DataLoader loader) {
        this.reference = reference;
        this.period = period;
        this.survey = survey;
        this.dataLoader = loader;
    }

    // We should only ever be given 1 contributor here, but if more than 1 is given we get the formID of the first one
    public int getFormId(){
        Iterable<ContributorEntity> contributors = dataLoader.loadContributors(reference, period, survey);
        for(ContributorEntity entity: contributors){
            return entity.getFormId();
        }
        return 0;
    }


    public List<String> getUniqueListOfRule(int formId){
        List<String> rules = new ArrayList<>();
        Iterable<ValidationFormEntity> config = dataLoader.loadValidationConfig(formId);
        for(ValidationFormEntity validationForms: validationFormEntity){
            if(!rules.contains(validationForms.getValidationCode())){
                rules.add(validationForms.getValidationCode());
            }
        }
        return rules;
    }

    public List<QuestionResponseEntity> addResponses(){
        Iterable<FormDefintionEntity> formDefintionEntities = dataLoader.loadFormDefinition(reference, period, survey);
        List<FormDefintionEntity> defintionEntityList = new ParseIterable().parseIterable(formDefintionEntities);
        Iterable<QuestionResponseEntity> questionResponseEntities = dataLoader.loadResponses(reference, period, survey);
        List<QuestionResponseEntity> questionResponseEntityList = new ParseIterable().parseIterable(questionResponseEntities);
        return null;
    }


    public void pickRulesToRun(List<String> rules){
        if(rules.contains("VP")){
            ValuePresentWrangler valuePresentWrangler = new ValuePresentWrangler();
        }
        else if(rules.contains("POPM")){
           // RunPopmService;
        }
        else if(rules.contains("POPZC")){
            // RunPopzcService;
        }
    }

    public void runValidation(){
        int formId = getFormId();
        List<String> listOfRules = getUniqueListOfRule(formId);
        pickRulesToRun(listOfRules);
    }
}

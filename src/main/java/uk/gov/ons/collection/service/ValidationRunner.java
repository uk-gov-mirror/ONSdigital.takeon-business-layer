package uk.gov.ons.collection.service;

import uk.gov.ons.collection.entity.ContributorEntity;
import uk.gov.ons.collection.entity.FormDefintionEntity;
import uk.gov.ons.collection.entity.QuestionResponseEntity;
import uk.gov.ons.collection.entity.ValidationFormEntity;
import uk.gov.ons.collection.utilities.Helpers;
import uk.gov.ons.collection.utilities.ParseIterable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    // Make sure we have a full set of questions and not just the ones that currently have responses.
    public List<QuestionResponseEntity> checkAllQuestionsPresent(){
        Iterable<QuestionResponseEntity> questionResponseEntities = dataLoader.loadResponses(reference, period, survey);
        Map<Integer, List<String>> mapOfInstancesAndResponses = new Helpers().placeIntoMap(new ParseIterable().parseIterable(questionResponseEntities));
        return null;
    }

    public boolean checkQuestionCodeAndInstnaceIsPresent(int instance, String questionCode, Map<Integer, List<String>> questionResponses){
        if(questionResponses.get(instance).contains(questionCode)){
            return true;
        }
        return false;
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

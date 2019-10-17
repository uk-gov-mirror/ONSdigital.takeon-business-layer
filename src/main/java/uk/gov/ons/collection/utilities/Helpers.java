package uk.gov.ons.collection.utilities;

import uk.gov.ons.collection.entity.FormDefinitionEntity;
import uk.gov.ons.collection.entity.QuestionResponseEntity;
import uk.gov.ons.collection.service.ApiCaller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Helpers {

    public String buildUriParameters(String reference, String period, String survey){
        return "reference="+reference+";"+"period="+period+";"+"survey="+survey;
    }

    public Map<Integer, List<String>> placeIntoMap(List<QuestionResponseEntity> questionResponseEntities){
        Map<Integer, List<String>> mapOfInstance = new HashMap<>();
        for(QuestionResponseEntity element: questionResponseEntities){
            if(!mapOfInstance.containsKey(element.getInstance())){
                mapOfInstance.put(element.getInstance(), new ArrayList<String>());
            }
        }
        for(QuestionResponseEntity element: questionResponseEntities){
            mapOfInstance.get(element.getInstance()).add(element.getQuestionCode());
        }
        return mapOfInstance;
    }

    public static class ParseIterable {

        public <T> List<T> parseIterable(Iterable<T> iterable){
            List<T> target = new ArrayList<>();
            iterable.forEach(element -> target.add(element));
            return target;
        }
    }

    public List<QuestionResponseEntity> checkAllQuestionsPresent(ApiCaller apiCaller, String reference, String period, String survey){
        List<QuestionResponseEntity> questionResponseEntities = new Helpers.ParseIterable().parseIterable(apiCaller.loadResponses(reference, period, survey));
        Iterable<FormDefinitionEntity> formDefinitionEntities = apiCaller.loadFormDefinition(reference, period, survey);
        for(FormDefinitionEntity element: formDefinitionEntities){
            if(!checkQuestionCodePresent(element.getQuestionCode(), questionResponseEntities)){
                QuestionResponseEntity entityToAdd = new QuestionResponseEntity();
                entityToAdd.setQuestionCode(element.getQuestionCode());
                entityToAdd.setResponse("");
                questionResponseEntities.add(entityToAdd);
            }
        }
        return questionResponseEntities;
    }


    public boolean checkQuestionCodePresent(String questionCode, List<QuestionResponseEntity> questionResponses){
        for(QuestionResponseEntity element: questionResponses){
            if(element.getQuestionCode().equals(questionCode))
                return true;
        }
        return false;
    }
}

package uk.gov.ons.collection.utilities;

import uk.gov.ons.collection.entity.QuestionResponseEntity;

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
        List<String> listOfQuestionCodes = new ArrayList<>();
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

}

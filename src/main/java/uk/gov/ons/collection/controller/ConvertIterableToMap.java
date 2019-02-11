package uk.gov.ons.collection.controller;

import uk.gov.ons.collection.entity.FormDefintionEntity;
import uk.gov.ons.collection.entity.QuestionResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertIterableToMap {

    public Map<String, List<String>> ConvertToMap(Iterable<QuestionResponseEntity> questionResponseEntities, Iterable<FormDefintionEntity> formDefinitionEntities){
        List<String> questionCodes = new ArrayList<>();
        List<String> responses = new ArrayList<>();
        List<String> instances = new ArrayList<>();
        Map<String, List<String>> questionDetailMap = new HashMap<>();

        formDefinitionEntities.forEach(formDefintionEntity -> questionCodes.add(formDefintionEntity.getquestionCode()));
        questionResponseEntities.forEach(questionResponseEntity -> responses.add(questionResponseEntity.getResponse()));
        //questionResponseEntities.forEach(questionResponseEntity -> instances.add(questionResponseEntity.getInstance().toString()));

        for(int i = 0 ; i < questionCodes.size(); i++){
            List<String> detailContainer = new ArrayList<>();
            detailContainer.add(responses.get(i));
            //detailContainer.add(instances.get(i));
            questionDetailMap.put(questionCodes.get(i), detailContainer);

        }

        return questionDetailMap;
    }
}

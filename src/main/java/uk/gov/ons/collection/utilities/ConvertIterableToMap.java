package uk.gov.ons.collection.utilities;

import uk.gov.ons.collection.entity.FormDefinitionEntity;
import uk.gov.ons.collection.entity.QuestionResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConvertIterableToMap {

    public Map<String, List<String>> convertToMap(Iterable<QuestionResponseEntity> questionResponseEntities,
                                                  Iterable<FormDefinitionEntity> formDefinitionEntities) {
        List<String> questionCodes = new ArrayList<>();
        List<String> responses = new ArrayList<>();
        Map<String, List<String>> questionDetailMap = new HashMap<>();

        formDefinitionEntities.forEach(formDefintionEntity -> questionCodes.add(formDefintionEntity.getQuestionCode()));
        questionResponseEntities.forEach(questionResponseEntity -> responses.add(questionResponseEntity.getResponse()));

        for (int i = 0; i < questionCodes.size(); i++) {
            List<String> detailContainer = new ArrayList<>();
            detailContainer.add(responses.get(i));
            //detailContainer.add(instances.get(i));
            questionDetailMap.put(questionCodes.get(i), detailContainer);
        }
        return questionDetailMap;
    }
}

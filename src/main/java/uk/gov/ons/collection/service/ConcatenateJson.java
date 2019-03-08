package uk.gov.ons.collection.service;

import uk.gov.ons.collection.entity.UpdatedFormData;
import uk.gov.ons.collection.entity.ValidationOutputEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ConcatenateJson {

    private Iterable<ValidationOutputEntity> heldValidationOutputs;
    private Iterable<UpdatedFormData> formDataFromUi;

    public ConcatenateJson(Iterable<ValidationOutputEntity> validationOutputEntities,
                                                            Iterable<UpdatedFormData> updatedFormData){
        heldValidationOutputs = validationOutputEntities;
        formDataFromUi = updatedFormData;
    }

    public List<ValidationOutputEntity> setQuestionCodeAndResponse(){
        List<ValidationOutputEntity> validationOutputEntitiesToReturn = new ArrayList<>();
        for(ValidationOutputEntity element: heldValidationOutputs){
            for(UpdatedFormData question: formDataFromUi){
                if(Objects.equals(element.getQuestionCode(), question.getQuestionCode())){
                    element.setQuestionCode(question.getQuestionCode());
                    element.setCurrentResponse(question.getResponse());
                    validationOutputEntitiesToReturn.add(element);
                }
            }
        }
        return validationOutputEntitiesToReturn;
    }
}

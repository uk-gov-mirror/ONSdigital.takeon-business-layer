package uk.gov.ons.collection.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import uk.gov.ons.collection.entity.UpdatedFormData;
import uk.gov.ons.collection.entity.ValidationOutputEntity;

import java.util.*;

public class ConcatenateJson {

    private Iterable<ValidationOutputEntity> heldValidationOutputs;
    private String formDataFromUi;

    public ConcatenateJson(Iterable<ValidationOutputEntity> validationOutputEntities,
                                                           String updatedFormData){
        heldValidationOutputs = validationOutputEntities;
        formDataFromUi = updatedFormData;
    }

//    public List<UpdatedFormData> setFormQcodeAndInstance() {
//        List<UpdatedFormData> outputFormData = new ArrayList<>();
//    }
//    public List<ValidationOutputEntity> setQuestionCodeAndResponse(){
//        List<ValidationOutputEntity> validationOutputEntitiesToReturn = new ArrayList<>();
//        for(ValidationOutputEntity element: heldValidationOutputs){
//            for(UpdatedFormData question: formDataFromUi){
//                if(Objects.equals(element.getQuestionCode(), question.getQuestionCode())){
//                    element.setQuestionCode(question.getQuestionCode());
//                    element.setCurrentResponse(question.getResponse());
//                    validationOutputEntitiesToReturn.add(element);
//                }
//            }
//        }
//        return validationOutputEntitiesToReturn;
//    }
}

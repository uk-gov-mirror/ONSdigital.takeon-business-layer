package uk.gov.ons.collection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.ValidationEntity;
import uk.gov.ons.collection.entity.ValidationFormEntity;
import uk.gov.ons.collection.service.ValuePresent;

import java.util.*;

@Service
public class ValidationJunction {
    private String validationCode;
    private Map<String, String> urlParameters;

    @Autowired
    ValuePresent valuePresent;

//    public ValidationJunction(ValidationFormEntity validationFormEntity, Map<String, String> parameters){
//        validationCode = validationFormEntity.getValidationCode();
//        urlParameters = parameters;
//    }

    public Iterable<ValidationFormEntity> pickValidationApi(ValidationFormEntity validationFormEntity, Map<String, String> parameters){
        validationCode = validationFormEntity.getValidationCode();
        urlParameters = parameters;
        List<ValidationFormEntity> validationFormEntityList = new ArrayList<>(Arrays.asList(validationFormEntity));
        System.out.println("List: "+validationFormEntityList);
        switch(validationCode){
            case "VP":
                valuePresent.buildUri(urlParameters);
                valuePresent.getContributor();
                return valuePresent.matchResponsesToConfig(valuePresent.getResponses(),  validationFormEntityList);
                //valuePresent.runValidation();
            case "POPM":
                break;
            case "POPZC":
                break;
        }
        return null;
    }
}

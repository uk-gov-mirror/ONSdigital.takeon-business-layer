package uk.gov.ons.collection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.ValidationEntity;
import uk.gov.ons.collection.entity.ValidationFormEntity;
import uk.gov.ons.collection.service.ValuePresent;

import java.util.Map;
import java.util.Objects;
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
        switch(validationCode){
            case "VP":
                valuePresent.buildUri(urlParameters);
                valuePresent.getContributor();
                valuePresent.matchResponsesToConfig(valuePresent.getResponses(), valuePresent.getValidationConfig());
                return valuePresent.runValidation();
            case "POPM":
                break;
            case "POPZC":
                break;
        }
        return null;
    }
}

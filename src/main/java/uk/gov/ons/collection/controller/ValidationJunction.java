package uk.gov.ons.collection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.ValidationFormEntity;
import uk.gov.ons.collection.service.ValuePresentWrangler;

import java.util.*;

@Service
public class ValidationJunction {
    private String validationCode;
    private Map<String, String> urlParameters;

    @Autowired
    ValuePresentWrangler valuePresentWrangler;

    public Iterable<ValidationFormEntity> pickValidationApi(ValidationFormEntity validationFormEntity, Map<String, String> parameters){
        validationCode = validationFormEntity.getValidationCode();
        urlParameters = parameters;
        List<ValidationFormEntity> validationFormEntityList = new ArrayList<>(Arrays.asList(validationFormEntity));
        System.out.println("List: "+validationFormEntityList);
        switch(validationCode){
            case "VP":
                valuePresentWrangler.buildUri(urlParameters);
                valuePresentWrangler.getContributor();
                return valuePresentWrangler.matchResponsesToConfig(valuePresentWrangler.getResponses(),  validationFormEntityList);
                //valuePresentWrangler.runValidation();
            case "POPM":
                break;
            case "POPZC":
                break;
        }
        return null;
    }
}

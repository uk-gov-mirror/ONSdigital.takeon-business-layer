package uk.gov.ons.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.ValidationFormEntity;
import uk.gov.ons.collection.entity.ValidationOutputEntity;

import java.util.List;

@Service
public class ValidationConfigService {

    @Autowired
    ValidationConfigProxy validationConfigProxy;

    public List<ValidationFormEntity> getValidationConfig(String parameters){
        System.out.printf("parameters: %s\n", parameters);
        return validationConfigProxy.getConfig(parameters);
    }
}

package uk.gov.ons.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.FormDefintionEntity;
import uk.gov.ons.collection.entity.ReturnedValidationOutputs;
import uk.gov.ons.collection.entity.ValidationFormEntity;

import java.util.ArrayList;
import java.util.List;

@Service
public class ValuePresentRunner {

    @Autowired
    RunValidationService validationService;

    private ValidationFormEntity setTriggered(ReturnedValidationOutputs validationOutputs, ValidationFormEntity formEntity){
        formEntity.setIsTriggered(validationOutputs.getTriggered());
        formEntity.setMetaData(validationOutputs.getMetaData());
        return formEntity;
    }

    public Iterable<ValidationFormEntity> callValidationService(Iterable<ValidationFormEntity> validationFormEntities){
        List<ValidationFormEntity> processedValidations = new ArrayList<>();
        for(ValidationFormEntity validation: validationFormEntities){
            ValidationFormEntity validationFormEntity = new ValidationFormEntity();
            ReturnedValidationOutputs validationOutputs = validationService.runValidation(validation.getPayload());
            validationFormEntity = setTriggered(validationOutputs, validation);
            processedValidations.add(validationFormEntity);
        }
        return processedValidations;
    }
}

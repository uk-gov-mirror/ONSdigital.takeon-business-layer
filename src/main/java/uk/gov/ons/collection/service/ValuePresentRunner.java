package uk.gov.ons.collection.service;

import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.FormDefintionEntity;
import uk.gov.ons.collection.entity.ValidationFormEntity;

@Service
public class ValuePresentRunner {
    private Iterable<ValidationFormEntity> validationsToRun;
    public ValuePresentRunner(Iterable<ValidationFormEntity> validationFormEntities){
        this.validationsToRun = validationFormEntities;
    }
    
}

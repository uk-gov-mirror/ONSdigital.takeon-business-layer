package uk.gov.ons.collection.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.ValidationFormEntity;
import uk.gov.ons.collection.entity.ValidationOutputEntity;

import java.util.List;

@Log4j2
@Service
public class ValidationConfigService {

    @Autowired
    ValidationConfigProxy validationConfigProxy;

    public List<ValidationFormEntity> getValidationConfig(String parameters){
        log.info("Validation Form Parameters { }", parameters);
        return validationConfigProxy.getConfig(parameters);
    }
}

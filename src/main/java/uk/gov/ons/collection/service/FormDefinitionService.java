package uk.gov.ons.collection.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.FormDefinitionEntity;

@Log4j2
@Service
public class FormDefinitionService {

    @Autowired
    FormDefinitionProxy formDefinitionProxy;

    public Iterable<FormDefinitionEntity> getForm(String parameters){
        log.info("Form Parameters { } ", parameters);
        return formDefinitionProxy.getFormDefinition(parameters);
    }

}

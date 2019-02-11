package uk.gov.ons.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.ContributorEntity;
import uk.gov.ons.collection.entity.FormDefintionEntity;

@Service
public class FormDefinitionService {

    @Autowired
    FormDefinitionProxy formDefinitionProxy;

    public Iterable<FormDefintionEntity> getForm(String parameters){
        System.out.println(parameters);
        return formDefinitionProxy.getFormDefinition(parameters);
    }

}

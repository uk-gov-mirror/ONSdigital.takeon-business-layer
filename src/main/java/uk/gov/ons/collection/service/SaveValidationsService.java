package uk.gov.ons.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SaveValidationsService {

    @Autowired
    SaveValidationsProxy saveValidationsProxy;

    public void saveValidations(String body){
        saveValidationsProxy.putValidations(body);
    }
}

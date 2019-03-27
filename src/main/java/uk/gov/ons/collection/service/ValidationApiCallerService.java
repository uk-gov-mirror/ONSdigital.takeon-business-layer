package uk.gov.ons.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.ReturnedValidationOutputs;

@Service
public class ValidationApiCallerService {

    @Autowired
    ValidationApiCallerProxy validationApiCallerProxy;

    public Iterable<ReturnedValidationOutputs> callValuePresentApi(String params){
        return validationApiCallerProxy.runValuePresentApi(params);
    }

}

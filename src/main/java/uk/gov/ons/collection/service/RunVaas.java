package uk.gov.ons.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.ReturnedValidationOutputs;

@Service
public class RunVaas {

    @Autowired
    RunVaasProxy runVaasProxy;

    public ReturnedValidationOutputs runValidation(String body){
        return runVaasProxy.runVaas(body);
    }
}

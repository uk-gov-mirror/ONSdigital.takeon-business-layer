package uk.gov.ons.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.ReturnedValidationOutputs;

@Service
public class RunValidationService {

    @Autowired
    RunValidationServiceProxy runValidationServiceProxy;

    public ReturnedValidationOutputs runValidation(String body){
        System.out.println("Body to pass: " + body);
        ReturnedValidationOutputs proxy =  runValidationServiceProxy.runVaas(body);
        System.out.println("Return: " + proxy);
        return proxy;
    }
}

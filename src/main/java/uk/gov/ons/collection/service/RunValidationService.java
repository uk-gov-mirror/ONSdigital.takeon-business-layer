package uk.gov.ons.collection.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.ReturnedValidationOutputs;

@Log4j2
@Service
public class RunValidationService {

    @Autowired
    RunValidationServiceProxy runValidationServiceProxy;

    public ReturnedValidationOutputs runValidation(String body){
        log.info("Body to pass { }", body);
        ReturnedValidationOutputs proxy =  runValidationServiceProxy.runVaas(body);
        log.info("Returned Validation Output { }", proxy);
        return proxy;
    }
}

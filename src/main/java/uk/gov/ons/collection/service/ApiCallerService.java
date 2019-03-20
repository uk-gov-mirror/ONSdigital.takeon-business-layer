package uk.gov.ons.collection.service;

import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.ReturnedValidationOutputs;
import uk.gov.ons.collection.entity.ValidationFormEntity;

@Service
public class ApiCallerService {

    @Autowired
    ApiCallerProxy apiCallerProxy;

    public Iterable<ReturnedValidationOutputs> callValidationApi(String params){
        return apiCallerProxy.runApi(params);
    }

}

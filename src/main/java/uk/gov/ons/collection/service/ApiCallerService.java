package uk.gov.ons.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.ons.collection.entity.ReturnedValidationOutputs;
import uk.gov.ons.collection.entity.ValidationFormEntity;

public class ApiCallerService {

    @Autowired
    ApiCallerProxy apiCallerProxy;

    public Iterable<ValidationFormEntity> callValidationApi(String params){
        return apiCallerProxy.runApi(params);
    }

}

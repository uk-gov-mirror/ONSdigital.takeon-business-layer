package uk.gov.ons.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.ons.collection.entity.ReturnedValidationOutputs;

public class ApiCallerService {

    @Autowired
    ApiCallerProxy apiCallerProxy;

    public Iterable<ReturnedValidationOutputs> callValidationApi(String params){
        return apiCallerProxy.runApi(params);
    }

}

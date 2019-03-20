package uk.gov.ons.collection.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.gov.ons.collection.entity.QuestionResponseEntity;
import uk.gov.ons.collection.entity.ReturnedValidationOutputs;
import uk.gov.ons.collection.entity.ValidationFormEntity;

import java.util.List;

@FeignClient(name="BusinessLogicLayer")
public interface ApiCallerProxy {

    @GetMapping("/validation-bl/value-present/{args}")
    public Iterable<ReturnedValidationOutputs> runApi(@PathVariable(value="args") String args);
}

package uk.gov.ons.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.ReturnedValidationOutputs;

import java.util.ArrayList;
import java.util.List;

@Service
public class ValuePresentRunner {

    @Autowired
    RunValidationService validationService;

    public Iterable<ReturnedValidationOutputs> callValidationService(List<String> validationJson) {
        List<ReturnedValidationOutputs> validationOutputs = new ArrayList<>();
        for(String json: validationJson) {

            ReturnedValidationOutputs output = validationService.runValidation(json);
            validationOutputs.add(output);
        }
        return validationOutputs;
    }
}

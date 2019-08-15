package uk.gov.ons.collection.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.ReturnedValidationOutputs;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
public class ValidationServiceRunner {

    @Autowired
    RunValidationService validationService;

    public Iterable<ReturnedValidationOutputs> callValidationService(List<String> validationJson) {
        List<ReturnedValidationOutputs> validationOutputs = new ArrayList<>();
        for(String json: validationJson) {

            ReturnedValidationOutputs output = validationService.runValidation(json);
            if(output != null) {
                log.info("Returned Validation { }", output.toString());
            }

            validationOutputs.add(output);
        }
        return validationOutputs;
    }
}

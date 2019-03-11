package uk.gov.ons.collection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uk.gov.ons.collection.entity.ValidationOutputEntity;
import uk.gov.ons.collection.service.RunValidationService;
import uk.gov.ons.collection.service.ValidationConfigService;
import uk.gov.ons.collection.utilities.Helpers;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/validation-bl")
public class ValidationWrangling {

    @Autowired
    ValidationConfigService validationConfigService;

    @Autowired
    RunValidationService runValidationService;

    @GetMapping(value = "/configuration/{vars}")
    public Iterable<ValidationOutputEntity> getConfig(@MatrixVariable Map<String, String> matrixVars){
        String reference = matrixVars.get("reference");
        String period = matrixVars.get("period");
        String survey = matrixVars.get("survey");

        List<ValidationOutputEntity> validationOutputEntities =
                validationConfigService.getValidationConfig(new Helpers().buildUriParameters(reference, period, survey));



        for(int index = 0; index < validationOutputEntities.size(); index++){
            String jsonToSend = "{\"value\":" + validationOutputEntities.get(index).getPrimaryValue() +"}";
            String triggered = runValidationService.runValidation(jsonToSend).toString();
            validationOutputEntities.get(index).setIsTriggered(triggered);
        }
        return null;
    }
}

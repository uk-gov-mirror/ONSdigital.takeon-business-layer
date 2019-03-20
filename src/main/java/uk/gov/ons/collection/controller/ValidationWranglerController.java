package uk.gov.ons.collection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.collection.entity.ReturnedValidationOutputs;
import uk.gov.ons.collection.entity.ValidationFormEntity;
import uk.gov.ons.collection.service.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/validation-bl")
public class ValidationWranglerController {

    @Autowired
    ApiCallerSQL loaderSQL;

    @Autowired
    RunValidationService validationService;

    @Autowired
    ValuePresentRunner valuePresentRunner;

    @GetMapping(value = "/run-all/{vars}")
    public Iterable<Iterable<ValidationFormEntity>> runAllValidationRules(@MatrixVariable Map<String, String> matrixVars){

        String reference = matrixVars.get("reference");
        String period = matrixVars.get("period");
        String survey = matrixVars.get("survey");

        ValidationRunner validationRunner = new ValidationRunner(reference, period, survey, loaderSQL);
        return validationRunner.runValidations();
    }

    @GetMapping("/value-present/{args}")
    public Iterable<ValidationFormEntity> runValuePresent(@MatrixVariable Map<String, String> matrixVars){

        String reference = matrixVars.get("reference");
        String period = matrixVars.get("period");
        String survey = matrixVars.get("survey");


        ValuePresentWrangler valuePresentWrangler = new ValuePresentWrangler(reference, period, survey, loaderSQL);
        valuePresentWrangler.runVpWrangler();
        Iterable<ValidationFormEntity> validationFormEntities = valuePresentWrangler.setPayloadAndReturnFormEntities();

        return valuePresentRunner.callValidationService(validationFormEntities);
    }

}

package uk.gov.ons.collection.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping(value = "/run-all/{vars}")
    public void runAllValidationRules(@MatrixVariable Map<String, String> matrixVars){

        String reference = matrixVars.get("reference");
        String period = matrixVars.get("period");
        String survey = matrixVars.get("survey");

        ValidationRunner validationRunner = new ValidationRunner(reference, period, survey, loaderSQL);
        validationRunner.runValidations();
    }

    @GetMapping("/value-present/{args}")
    public Iterable<ValidationFormEntity> runValuePresent(@MatrixVariable Map<String, String> matrixVars){

        String reference = matrixVars.get("reference");
        String period = matrixVars.get("period");
        String survey = matrixVars.get("survey");

        ValuePresentWrangler valuePresentWrangler = new ValuePresentWrangler(reference, period, survey, new ApiCallerSQL());
        valuePresentWrangler.runVpWrangler();
        Iterable<ValidationFormEntity> validationFormEntities = valuePresentWrangler.setPayloadAndReturnFormEntities();

        return new ValuePresentRunner(validationFormEntities).callValidationService();
    }

}

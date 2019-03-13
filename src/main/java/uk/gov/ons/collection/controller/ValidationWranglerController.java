package uk.gov.ons.collection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.collection.entity.ValidationFormEntity;
import uk.gov.ons.collection.service.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/validation-bl")
public class ValidationWranglerController {

    @Autowired
    ValidationConfigService validationConfigService;

    @Autowired
    RunValidationService runValidationService;

    @Autowired
    ContributorService contributorService;

    @Autowired
    ValidationJunction validationJunction;

    @GetMapping(value = "/configuration/{vars}")
    public List<Iterable<ValidationFormEntity>> getConfig(@MatrixVariable Map<String, String> matrixVars){

        String reference = matrixVars.get("reference");
        String period = matrixVars.get("period");
        String survey = matrixVars.get("survey");

        ValidationRunner validationRunner = new ValidationRunner(reference, period, survey, new DataLoaderSQL());
        validationRunner.runValidation();
        return null;
    }

}

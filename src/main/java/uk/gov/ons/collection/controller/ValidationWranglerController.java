package uk.gov.ons.collection.controller;

import jdk.vm.ci.code.site.Call;
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

    @Autowired
    SaveValidationsService saveValidationsService;

    @GetMapping(value = "/run-all/{vars}")
    public void runAllValidationRules(@MatrixVariable Map<String, String> matrixVars){

        String reference = matrixVars.get("reference");
        String period = matrixVars.get("period");
        String survey = matrixVars.get("survey");

        ValidationRunner validationRunner = new ValidationRunner(reference, period, survey, loaderSQL);
        System.out.println(validationRunner.runValidations().toString());
        saveValidationsService.saveValidations(validationRunner.runValidations().toString());
    }

    @GetMapping("/value-present/{args}")
    public Iterable<ReturnedValidationOutputs> runValuePresent(@MatrixVariable Map<String, String> matrixVars){

        String reference = matrixVars.get("reference");
        String period = matrixVars.get("period");
        String survey = matrixVars.get("survey");


        ValuePresentWrangler valuePresentWrangler = new ValuePresentWrangler(reference, period, survey, loaderSQL);
        List<String> valuePresentJson = valuePresentWrangler.parseDataAndGenerateJson();
        return valuePresentRunner.callValidationService(valuePresentJson);
    }

    @GetMapping("/value-present-lambda/{args}")
    public void runValuePresentLambda(@MatrixVariable Map<String, String> matrixVars){
        String reference = matrixVars.get("reference");
        String period = matrixVars.get("period");
        String survey = matrixVars.get("survey");


        ValuePresentWrangler valuePresentWrangler = new ValuePresentWrangler(reference, period, survey, loaderSQL);
        List<String> valuePresentJson = valuePresentWrangler.parseDataAndGenerateJson();
        CallRemoteService remoteService = new CallRemoteService("https://anlk9csza4.execute-api.eu-west-2.amazonaws.com/dev/value-present", valuePresentJson.toString());
        remoteService.callLambda();
        System.out.println(remoteService.getResponse());
    }
}

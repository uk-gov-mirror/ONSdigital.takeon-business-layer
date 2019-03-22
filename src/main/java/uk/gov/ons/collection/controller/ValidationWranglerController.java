package uk.gov.ons.collection.controller;

import jdk.vm.ci.code.site.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.collection.entity.ReturnedValidationOutputs;
import uk.gov.ons.collection.entity.Status;
import uk.gov.ons.collection.entity.ValidationFormEntity;
import uk.gov.ons.collection.service.*;
import uk.gov.ons.collection.utilities.Helpers;

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

    @Autowired
    UpdateFormStatusService updateFormStatusService;

    @GetMapping(value = "/run-all/{vars}")
    public void runAllValidationRules(@MatrixVariable Map<String, String> matrixVars){

        String reference = matrixVars.get("reference");
        String period = matrixVars.get("period");
        String survey = matrixVars.get("survey");

        ValidationRunner validationRunner = new ValidationRunner(reference, period, survey, loaderSQL);

        System.out.println(validationRunner.runValidations().toString());
        Iterable<ReturnedValidationOutputs> returnedValidationOutputs = validationRunner.runValidations();
        Status status = new CheckIfAnyTriggered().checkIfTriggered(returnedValidationOutputs);
        updateFormStatusService.updateStatus(new Helpers().buildUriParameters(reference, period, survey), status.toString());
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

        Long start = System.currentTimeMillis();
        ValuePresentWrangler valuePresentWrangler = new ValuePresentWrangler(reference, period, survey, loaderSQL);
        List<String> valuePresentJson = valuePresentWrangler.parseDataAndGenerateJson();
        for (String element: valuePresentJson) {
            CallRemoteService remoteService = new CallRemoteService("https://s5p8bg98v4.execute-api.eu-west-2.amazonaws.com/dev/value-present", element);
            remoteService.callLambda();
            System.out.println(remoteService.getResponse());
        }
        Long finish = System.currentTimeMillis();
        System.out.println(finish - start);
    }

    @GetMapping("/value-present-algo/{args}")
    public void runValuePresentAlgo(@MatrixVariable Map<String, String> matrixVars){
        String reference = matrixVars.get("reference");
        String period = matrixVars.get("period");
        String survey = matrixVars.get("survey");

        Long start = System.currentTimeMillis();
        ValuePresentWrangler valuePresentWrangler = new ValuePresentWrangler(reference, period, survey, loaderSQL);
        List<String> valuePresentJson = valuePresentWrangler.parseDataAndGenerateJson();
        for(String element: valuePresentJson) {
            CallRemoteService remoteService = new CallRemoteService("https://api.algpoc.com/v1/algo/ons/ValidationValuePresent/0.1.0", element);
            remoteService.callAlgoService();
            System.out.println(remoteService.getResponse());
        }
        Long finish = System.currentTimeMillis();
        System.out.println(finish - start);
    }
}

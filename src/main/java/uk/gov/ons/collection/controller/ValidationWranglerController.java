package uk.gov.ons.collection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.collection.entity.ErrorMessage;
import uk.gov.ons.collection.entity.ReturnedValidationOutputs;
import uk.gov.ons.collection.entity.Status;
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
    ValidationServiceRunner validationServiceRunner;

    @Autowired
    SaveValidationsService saveValidationsService;

    @Autowired
    UpdateFormStatusService updateFormStatusService;

    @Autowired
    QuestionResponseController questionResponseController;


    @PutMapping(value = "/run-all/{vars}")
    public ErrorMessage runAllValidationRules(@RequestBody String updatedResponses, @MatrixVariable Map<String, String> matrixVars) {

        ErrorMessage errorMessage = new ErrorMessage();

        try {
            questionResponseController.createResponseJSON(updatedResponses, matrixVars);
        }

        catch (Exception e){
            errorMessage.setError(e.toString());
            return errorMessage;
        }



        String reference = matrixVars.get("reference");
        String period = matrixVars.get("period");
        String survey = matrixVars.get("survey");

        ValidationRunner validationRunner = new ValidationRunner(reference, period, survey, loaderSQL);

        System.out.println(validationRunner.runValidations().toString());
        Iterable<ReturnedValidationOutputs> returnedValidationOutputs = validationRunner.runValidations();
        Status status = new CheckIfAnyTriggered().checkIfTriggered(returnedValidationOutputs);
        updateFormStatusService.updateStatus(new Helpers().buildUriParameters(reference, period, survey), status.toString());
        saveValidationsService.saveValidations(validationRunner.runValidations().toString());
        errorMessage.setError("");
        System.out.println(errorMessage.getError());
        return errorMessage;
    }

    @GetMapping("/value-present/{args}")
    public Iterable<ReturnedValidationOutputs> runValuePresent(@MatrixVariable Map<String, String> matrixVars){

        String reference = matrixVars.get("reference");
        String period = matrixVars.get("period");
        String survey = matrixVars.get("survey");


        ValidationRuleWrangler validationRuleWrangler = new ValidationRuleWrangler(reference, period, survey, loaderSQL);
        List<String> valuePresentJson = validationRuleWrangler.parseDataAndGenerateJson();
        return validationServiceRunner.callValidationService(valuePresentJson);
    }

    @GetMapping("/value-present-lambda/{args}")
    public void runValuePresentLambda(@MatrixVariable Map<String, String> matrixVars){
        String reference = matrixVars.get("reference");
        String period = matrixVars.get("period");
        String survey = matrixVars.get("survey");

        ValidationRuleWrangler validationRuleWrangler = new ValidationRuleWrangler(reference, period, survey, loaderSQL);
        List<String> valuePresentJson = validationRuleWrangler.parseDataAndGenerateJson();
        for (String element: valuePresentJson) {
            CallRemoteService remoteService = new CallRemoteService("https://s5p8bg98v4.execute-api.eu-west-2.amazonaws.com/dev/value-present", element);
            remoteService.callLambda();
            System.out.println(remoteService.getResponse());
        }
    }

    @GetMapping("/value-present-algo/{args}")
    public void runValuePresentAlgo(@MatrixVariable Map<String, String> matrixVars){
        String reference = matrixVars.get("reference");
        String period = matrixVars.get("period");
        String survey = matrixVars.get("survey");

        ValidationRuleWrangler validationRuleWrangler = new ValidationRuleWrangler(reference, period, survey, loaderSQL);
        List<String> valuePresentJson = validationRuleWrangler.parseDataAndGenerateJson();
        for(String element: valuePresentJson) {
            CallRemoteService remoteService = new CallRemoteService("https://api.algpoc.com/v1/algo/ons/ValidationValuePresent/0.1.0", element);
            remoteService.callAlgoService();
            System.out.println(remoteService.getResponse());
        }
    }
}

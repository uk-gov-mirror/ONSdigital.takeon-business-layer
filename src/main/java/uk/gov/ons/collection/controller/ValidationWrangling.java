package uk.gov.ons.collection.controller;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.collection.entity.ContributorEntity;
import uk.gov.ons.collection.entity.QuestionResponseEntity;
import uk.gov.ons.collection.entity.ValidationFormEntity;
import uk.gov.ons.collection.entity.ValidationOutputEntity;
import uk.gov.ons.collection.service.*;
import uk.gov.ons.collection.utilities.Helpers;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/validation-bl")
public class ValidationWrangling {

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

        Iterable<ContributorEntity> questionResponseEntities = contributorService
                .generalSearch(new Helpers().buildUriParameters(reference, period, survey));


        List<Iterable<ValidationFormEntity>> validationFormEntitiesToReturn = new ArrayList<>();
        List<ValidationFormEntity> validationFormEntities = new ArrayList<>();
        ValidationFormEntity validationFormEntity;

        for (ContributorEntity element: questionResponseEntities) {
            validationFormEntities = validationConfigService.
                    getValidationConfig("FormID="+element.getFormId().toString());
            System.out.println(element.getFormId());
        }

        for(ValidationFormEntity element: validationFormEntities){
           // System.out.println(element.getValidationCode());
           // System.out.println(element);
            //ValidationJunction validationJunction = new ValidationJunction(element, matrixVars);
            validationFormEntitiesToReturn.add(validationJunction.pickValidationApi(element, matrixVars));
            return validationFormEntitiesToReturn;
        }
        return null;
    }

}

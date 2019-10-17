package uk.gov.ons.collection.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.collection.entity.ContributorEntity;
import uk.gov.ons.collection.entity.FormDefinitionEntity;
import uk.gov.ons.collection.exception.DataNotFoundException;
import uk.gov.ons.collection.service.FormDefinitionService;

import java.util.*;

@Log4j2
@Api(value = "Form Definition Controller", description = "End point for the connection between the UI and persistence layer, controls access to form definitions")
@RestController
@RequestMapping(value = "/FormDefinition")

public class FormDefinitionController {

        private List<String> defaultValidSearchColumns = new ArrayList<>(Arrays.asList("reference", "period", "survey",
                "status", "formid"));
        private static final String NO_RECORDS_MESSAGE = "Persistance Layer is returning Zero records for Form Definition";

        @Autowired
        FormDefinitionService service;
        @ApiOperation(value = "Search contributor table by arbitrary parameters", response = String.class)
        @GetMapping(value = "/GetFormDefinition/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
        @ApiResponses(value = {
                @ApiResponse(code = 200, message = "Successful retrieval of Contributor details", response = ContributorEntity.class),
                @ApiResponse(code = 404, message = "Contributor does not exist"),
                @ApiResponse(code = 500, message = "Internal server error")})
        @ResponseBody
        public Iterable<FormDefinitionEntity> getFormDefintion(@MatrixVariable Map<String, String> matrixVars) throws Exception {
            Iterable<FormDefinitionEntity> formDefinitionEntities = null;

            String filteredSearchParameters = filterAndPrepareSearchParameters(matrixVars, this.defaultValidSearchColumns);
            log.info("Filtered Search Parameters { }", filteredSearchParameters);
            formDefinitionEntities = service.getForm(filteredSearchParameters);
            log.info("Form Definition Entities after calling Persistance Layer{ }", formDefinitionEntities);
            if (formDefinitionEntities == null) {
                throw new Exception();
            } else {
                if (formDefinitionEntities instanceof Collection) {
                    int size =  ((Collection<?>) formDefinitionEntities).size();
                    if(size == 0) {
                        throw new DataNotFoundException(NO_RECORDS_MESSAGE);
                    }
                }
            }


            return formDefinitionEntities;
        }

        public String filterAndPrepareSearchParameters(Map<String, String> inputParameters, List<String> allowedParameters) {
            Map<String,String> filteredParameters = UrlParameterBuilder.filter(inputParameters, allowedParameters);
            return UrlParameterBuilder.buildParameterString(filteredParameters);
        }


}

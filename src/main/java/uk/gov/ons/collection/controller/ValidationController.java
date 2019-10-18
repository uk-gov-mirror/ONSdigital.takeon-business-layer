package uk.gov.ons.collection.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import uk.gov.ons.collection.entity.ContributorStatus;
import uk.gov.ons.collection.entity.PeriodOffsetQuery;
import uk.gov.ons.collection.entity.PeriodOffsetResponse;
import uk.gov.ons.collection.entity.ValidationConfigQuery;
import uk.gov.ons.collection.entity.ValidationOutputs;
import uk.gov.ons.collection.service.GraphQLService;
import uk.gov.ons.collection.utilities.RelativePeriod;
import uk.gov.ons.collection.utilities.qlQueryBuilder;
import uk.gov.ons.collection.utilities.qlQueryResponse;

@Log4j2
@Api(value = "Validation Controller", description = "Entry points primarily involving validation queries")
@RestController
@RequestMapping(value = "/validation")
public class ValidationController {
   
    @Autowired
    GraphQLService qlService;

    @ApiOperation(value = "Get all validation config & response data", response = String.class)
    @GetMapping(value="/getAllConfiguration/{vars}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful retrieval of all details", response = String.class)})
    public String dataPrepConfig(@MatrixVariable Map <String, String> searchParameters){

        // TODO: Validate all 3 parameters have been passed through
        log.info("API CALL!! --> /validationPrepConfig/{vars} :: " + searchParameters );
        String period = searchParameters.get("period");
        String reference = searchParameters.get("reference");
        String survey = searchParameters.get("survey");
 
        // Step 1 - Get a unique list of all IDBR periods to query
        int formId;
        String periodicity;
        List<String> idbrPeriods = new ArrayList<>();
        try {
            var qlQuery = new PeriodOffsetQuery(searchParameters);
            var qlResponse = new PeriodOffsetResponse(qlService.qlSearch(qlQuery.getQlQuery()));
            formId = qlResponse.parseFormId();
            periodicity = qlResponse.parsePeriodicity();
            idbrPeriods = new RelativePeriod(periodicity).getIdbrPeriods(qlResponse.parsePeriodOffset(), period);
        }
        catch(Exception e) {
            log.info("Exception caught: " + e);
            return "{\"error\":\"Unable to resolve unique list of IDBR periods\"}";
        }
        
        // Step 2 - Load Validation Config for the given formId
        JSONArray validationConfig = new JSONArray();
        try {
            var qlQuery = new ValidationConfigQuery(formId).getQlQuery();
            var response = new qlQueryResponse(qlService.qlSearch(qlQuery));            
            validationConfig = response.parseValidationConfig();
        }
        catch (Exception e) {
            log.info("Exception caught: " + e);
            return"{\"error\":\"Error obtaining validation config query\"}";
        }


        JSONArray responses = new JSONArray();
        JSONArray contributors = new JSONArray();
        JSONArray forms = new JSONArray();

        // Step 3 - Load each contrib/response/form for each idbrPeriod above
        try {
            for (int i = 0; i < idbrPeriods.size(); i++) {
                HashMap<String,String> spr = new HashMap<>();
                spr.put("reference", reference);
                spr.put("survey", survey);
                spr.put("period", idbrPeriods.get(i));

                String query = new qlQueryBuilder(spr).buildContribResponseFormDetailsQuery();
                qlQueryResponse queryResponse = new qlQueryResponse(qlService.qlSearch(query));
                
                JSONArray responseArray = queryResponse.getResponses();
                for (int j = 0; j < responseArray.length(); j++) {
                    responses.put(responseArray.getJSONObject(j));
                }

                JSONArray formArray = queryResponse.getForm(survey, period);
                if (formArray.length() > 0) {
                    forms.put(formArray);
                }

                JSONObject contributor = queryResponse.getContributors();
                if (contributor.length() > 0) {
                    contributors.put(contributor);
                }
            }
        }
        catch(Exception e){
            log.info("Exception: " + e);
            return "{\"error\":\"Invalid contrib/response/form from graphQL\"}";
        }

        // log.info("\n\nFinal OutputResponseArray: " + responses);
        // log.info("\n\nFinal OutputContributorArray: " + contributors);
        // log.info("\n\nFinal form definition: " + forms);
        // log.info("\n\nFinal validation config: " + validationConfig);        

        log.info("API CALL!! --> /validationPrepConfig/{vars} :: Complete" );
        var outputJson = new JSONObject().put("contributor",contributors).put("validation_config",validationConfig).put("response",responses).put("question_schema",forms).put("reference", reference).put("period",period).put("survey",survey).put("periodicity", periodicity);
        return outputJson.toString();
    }


    @ApiOperation(value = "Save validation outputs", response = String.class)
    @RequestMapping(value="/saveOutputs", method = {RequestMethod.POST, RequestMethod.PUT})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Successful saving of all validation outputs", response = String.class)})
    @ResponseBody
    public String saveValidationOutputs(@RequestBody String validationOutputsJson, Errors errors){        
        log.info("API CALL!! --> /validation/saveOutputs :: " + validationOutputsJson );
        
        ValidationOutputs outputs;
        String period;
        String reference;
        String survey;
        String statusText;
        String deleteQuery;
        String insertQuery;

        // 1 - Convert params to JSON Object and extract Reference | Period | Survey
        try {
            outputs = new ValidationOutputs(validationOutputsJson);
            reference = outputs.getReference();
            period = outputs.getPeriod();
            survey = outputs.getSurvey();
            statusText = outputs.getStatusText();
            deleteQuery = outputs.buildDeleteOutputQuery();
            insertQuery = outputs.buildInsertByArrayQuery();
        }
        catch(Exception e) {
            log.info("Exception caught: " + e);
            return "{\"error\":\"Unable to resolve validation output JSON\"}";
        }
        
        String qlResponse = new String();

        // 1: Delete outputs
        try {        
            log.info("Delete: " + deleteQuery); 
            qlResponse = qlService.qlSearch(deleteQuery);
        }
        catch(Exception e){
            log.info("Exception: " + e);
            log.info("QL Response: " + qlResponse);
            return "{\"error\":\"Error removing existing validation outputs\"}";
        }

        // 2: Insert outputs
        try {        
            qlResponse = qlService.qlSearch(insertQuery);
        }
        catch(Exception e){
            log.info("Exception: " + e);     
            log.info("QL Response: " + qlResponse);
            return "{\"error\":\"Error saving validation outputs\"}";
        }

        // 3: Update status
        try {
            var updateStatusQuery = new ContributorStatus(reference, period, survey, statusText).buildUpdateQuery();
            qlResponse = qlService.qlSearch(updateStatusQuery);
        }
        catch(Exception e){
            log.info("Exception: " + e);
            log.info("QL Response: " + qlResponse);
            return "{\"error\":\"Error updating contributor status\"}";
        }        

        log.info("API CALL!! --> /validation/saveOutputs :: Complete" );
        return "{\"status\": \"Success\"}";
    }

}

// query contrib {
//     allContributors{
//       nodes{
//         reference
//         period
//         survey
//         status
//       }
//     }
//   }

// mutation updateStatus($period: String!, $reference: String!, $survey: String!, $status: String!) {
//     updateContributorByReferenceAndPeriodAndSurvey(input: {reference: $reference, period: $period, survey: $survey, contributorPatch: {status: $status}}) {
//       contributor {
//         reference
//         period
//         survey
//         status
//       }
//     }
//   }

//    Custom graphQL queries temporary storage

//    create or replace function dev01.deleteOutput(reference text, period text, survey text)
//        returns void as $$
//        DELETE
//        FROM dev01.validationoutput
//        where
//        reference = $1 and
//        period    = $2 and
//        survey = $3
//        $$ language sql VOLATILE;
//
//        Create function dev01.InsertValidationOutputByArray (dev01.validationoutput[]) returns dev01.validationoutput as $$
//        Insert Into dev01.validationoutput (reference, period, survey, validationid, instance, triggered, formula, createdBy, createdDate)
//        Select  reference, period, survey, validationid, instance, triggered, formula, createdBy, createdDate
//        From    unnest($1)
//        Returning *;
//        $$ LANGUAGE sql VOLATILE STRICT SECURITY DEFINER;
  
  
// public class updateStatusTest {

//     @Test
//     public void determineStatus_validInput_triggeredBlank() {
//         String response = "{\"validationoutput\":[{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"\",\"validationid\":\"\",\"bpmid\":\"\"}]}";
//         assertEquals(false, new updateStatus().determineStatus(response));
//     }

//     @Test
//     public void determineStatus_validInput_triggeredFalse() {
//         String response = "{\"validationoutput\":[{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"false\",\"validationid\":\"\",\"bpmid\":\"\"}]}";
//         assertEquals(false, new updateStatus().determineStatus(response));
//     }

//     @Test
//     public void determineStatus_validInput_triggeredTrue() {
//         String response = "{\"validationoutput\":[{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"true\",\"validationid\":\"\",\"bpmid\":\"\"}]}";
//         assertEquals(true, new updateStatus().determineStatus(response));
//     }

//     @Test
//     public void determineStatus_multiArray_triggeredTrue() {
//         String response = "{\"validationoutput\":[{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"\",\"validationid\":\"\",\"bpmid\":\"\"},{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"true\",\"validationid\":\"\",\"bpmid\":\"\"}]}";
//         assertEquals(true, new updateStatus().determineStatus(response));
//     }

//     @Test
//     public void determineStatus_multiArray_triggeredFalse() {
//         String response = "{\"validationoutput\":[{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"\",\"validationid\":\"\",\"bpmid\":\"\"},{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"false\",\"validationid\":\"\",\"bpmid\":\"\"}]}";
//         assertEquals(false, new updateStatus().determineStatus(response));
//     }

//     @Test
//     public void updateStatus_validInput_triggeredTrue() {
//         String response = "{\"validationoutput\":[{\"formula\":\"\", \"reference\":\"12345678000\",\"period\":\"201801\",\"survey\":\"999A\",\"triggered\":\"\",\"validationid\":\"\",\"bpmid\":\"\"},{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"false\",\"validationid\":\"\",\"bpmid\":\"\"}]}";
//         String updateStatusQuery = "{\"query\" : \"mutation updateStatus {updateContributorByReferenceAndPeriodAndSurvey(input: {reference: \\\"12345678000\\\", period: \\\"201801\\\", survey: \\\"999A\\\", contributorPatch: {status: \\\"ValidationsTriggered\\\"}}) {clientMutationId}}\"}";
//         assertEquals(updateStatusQuery, new updateStatus().updateStatusQuery(response));
//     }


// }

// public void buildDeleteQuery_validInput(){
//     String response = "{\"validationoutput\":[{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"true\",\"validationid\":\"\",\"bpmid\":\"\"}]}";
//     String deleteQuery = "{\"query\" : \"mutation deleteOutput{deleteoutput(input: {reference:\\\"\\\",period:\\\"\\\",survey:\\\"\\\"}){clientMutationId}}\"}";
//     // assertEquals(deleteQuery, new deleteInsertValidationOutput().buildDeleteOutputQuery(response));
// }

// @Test
// public void buildInsertQuery_validInput(){
//     String response = "{\"validationoutput\":[{\"formula\":\"1=1\", \"reference\":\"12345678000\",\"period\":\"201801\",\"survey\":\"999A\",\"triggered\":\"true\",\"validationid\":\"10\",\"bpmid\":\"\"}]}";
//     String insertQuery = "{\"query\" : \"mutation insertOutputArray{insertvalidationoutputbyarray(input: {arg0:[{reference:\\\"12345678000\\\",period:\\\"201801\\\",survey:\\\"999A\\\",validationid:\\\"10\\\",instance:\\\"0\\\",triggered:\\\"true\\\",formula:\\\"1=1\\\", createdby:\\\"User\\\", createddate:\\\"2016-06-22 22:10:25-04\\\"}]}){clientMutationId}}\"}";
//    // assertEquals(insertQuery, new deleteInsertValidationOutput().buildInsertByArrayQuery(response));
// }
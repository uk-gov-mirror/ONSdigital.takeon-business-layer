package uk.gov.ons.collection.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.log4j.Log4j2;
import uk.gov.ons.collection.service.GraphQLService;

@Log4j2
@Api(value = "Status Controller")
@RestController
@RequestMapping(value = "/updateStatus")
public class StatusController {

    @Autowired
    GraphQLService qlService;

    @GetMapping(value = "/updateStatus", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful Status Update")})
    public String searchContributor(String response) {
        if (new updateStatus().determineStatus(response) == true) {
            String qlQuery = new updateStatus().updateStatusQuery(response);
            return qlQuery;
        }
       return null;
    }
}
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

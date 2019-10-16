package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import uk.gov.ons.collection.controller.deleteInsertValidationOutput;

class deleteInsertValidationOutputTest {

    @Test
    public void buildDeleteQuery_validInput(){
        String response = "{\"validationoutput\":[{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"true\",\"validationid\":\"\",\"bpmid\":\"\"}]}";
        String deleteQuery = "{\"query\" : \"mutation deleteOutput{deleteoutput(input: {reference:\\\"\\\",period:\\\"\\\",survey:\\\"\\\"}){clientMutationId}}\"}";
        // assertEquals(deleteQuery, new deleteInsertValidationOutput().buildDeleteOutputQuery(response));
    }

    @Test
    public void buildInsertQuery_validInput(){
        String response = "{\"validationoutput\":[{\"formula\":\"1=1\", \"reference\":\"12345678000\",\"period\":\"201801\",\"survey\":\"999A\",\"triggered\":\"true\",\"validationid\":\"10\",\"bpmid\":\"\"}]}";
        String insertQuery = "{\"query\" : \"mutation insertOutputArray{insertvalidationoutputbyarray(input: {arg0:[{reference:\\\"12345678000\\\",period:\\\"201801\\\",survey:\\\"999A\\\",validationid:\\\"10\\\",instance:\\\"0\\\",triggered:\\\"true\\\",formula:\\\"1=1\\\", createdby:\\\"User\\\", createddate:\\\"2016-06-22 22:10:25-04\\\"}]}){clientMutationId}}\"}";
       // assertEquals(insertQuery, new deleteInsertValidationOutput().buildInsertByArrayQuery(response));
    }

}
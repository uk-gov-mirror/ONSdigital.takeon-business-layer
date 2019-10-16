package uk.gov.ons.collection.test;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;
import uk.gov.ons.collection.controller.updateStatus;

public class updateStatusTest {

    @Test
    public void determineStatus_validInput_triggeredBlank() {
        String response = "{\"validationoutput\":[{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"\",\"validationid\":\"\",\"bpmid\":\"\"}]}";
        assertEquals(false, new updateStatus().determineStatus(response));
    }

    @Test
    public void determineStatus_validInput_triggeredFalse() {
        String response = "{\"validationoutput\":[{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"false\",\"validationid\":\"\",\"bpmid\":\"\"}]}";
        assertEquals(false, new updateStatus().determineStatus(response));
    }

    @Test
    public void determineStatus_validInput_triggeredTrue() {
        String response = "{\"validationoutput\":[{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"true\",\"validationid\":\"\",\"bpmid\":\"\"}]}";
        assertEquals(true, new updateStatus().determineStatus(response));
    }

    @Test
    public void determineStatus_multiArray_triggeredTrue() {
        String response = "{\"validationoutput\":[{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"\",\"validationid\":\"\",\"bpmid\":\"\"},{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"true\",\"validationid\":\"\",\"bpmid\":\"\"}]}";
        assertEquals(true, new updateStatus().determineStatus(response));
    }

    @Test
    public void determineStatus_multiArray_triggeredFalse() {
        String response = "{\"validationoutput\":[{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"\",\"validationid\":\"\",\"bpmid\":\"\"},{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"false\",\"validationid\":\"\",\"bpmid\":\"\"}]}";
        assertEquals(false, new updateStatus().determineStatus(response));
    }

    @Test
    public void updateStatus_validInput_triggeredTrue() {
        String response = "{\"validationoutput\":[{\"formula\":\"\", \"reference\":\"12345678000\",\"period\":\"201801\",\"survey\":\"999A\",\"triggered\":\"\",\"validationid\":\"\",\"bpmid\":\"\"},{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"false\",\"validationid\":\"\",\"bpmid\":\"\"}]}";
        String updateStatusQuery = "{\"query\" : \"mutation updateStatus {updateContributorByReferenceAndPeriodAndSurvey(input: {reference: \\\"12345678000\\\", period: \\\"201801\\\", survey: \\\"999A\\\", contributorPatch: {status: \\\"ValidationsTriggered\\\"}}) {clientMutationId}}\"}";
        assertEquals(updateStatusQuery, new updateStatus().updateStatusQuery(response));
    }


}
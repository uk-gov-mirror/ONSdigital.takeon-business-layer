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
}
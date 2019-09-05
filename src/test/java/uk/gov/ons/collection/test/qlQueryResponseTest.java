package uk.gov.ons.collection.test;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.controller.qlQueryResponse;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Ql Response tests")

public class qlQueryResponseTest {

    @Test
    void filterQuery_correctFormat_dataAvailable() {
        String inputString = "{" +
                "\"data\": {" +
                "\"allContributors\": {" +
                "\"nodes\": [" +
                "{" +
                "\"reference\": \"4990012\"," +
                "\"period\": \"201211\"," +
                "\"survey\": \"066 \"" +
                "}," +
                "{" +
                "\"reference\": \"4990012\"," +
                "\"period\": \"201212\"," +
                "\"survey\": \"066 \"" +
                "}" +
                "]" +
                "}" +
                "}" +
                "}";

        String outputString = "{" +
                "\"data\": [" +
                "{" +
                "\"reference\": \"4990012\"," +
                "\"period\": \"201211\"," +
                "\"survey\": \"066 \"" +
                "}," +
                "{" +
                "\"reference\": \"4990012\"," +
                "\"period\": \"201212\"," +
                "\"survey\": \"066 \"" +
                "}" +
                "]" +
                "}";

        qlQueryResponse response = new qlQueryResponse(inputString);
        assertEquals(outputString, response.filter());
    }

    @Test
    void filterQuery_correctFormat_dataUnavailable() {
        String inputString = "{" +
                "\"data\": {" +
                "\"allContributors\": {" +
                "\"nodes\": []" +
                "}" +
                "}" +
                "}";

        String outputString = "{\"data\": []}";

        qlQueryResponse response = new qlQueryResponse(inputString);
        assertEquals(outputString, response.filter());
    }

    @Test
    void attributeCheck_attributePresent(){
        String inputString = "{" +
                "\"data\": {" +
                "\"allContributors\": {" +
                "\"nodes\": []" +
                "}" +
                "}" +
                "}";
        qlQueryResponse response = new qlQueryResponse(inputString);
        assertEquals(true, response.attributeCheck());
    }

    @Test
    void attributeCheck_attributeNotPresent(){
        String inputString = "{" +
                "\"data\": {" +
                "\"nodes\": []" +
                "}" +
                "}" +
                "}";
        qlQueryResponse response = new qlQueryResponse(inputString);
        assertEquals(false, response.attributeCheck());
    }

    @Test
    void stringConvert_attributesPresent() {
        String inputString = "{" +
                "\"data\": {" +
                "\"allContributors\": {" +
                "\"nodes\": [" +
                "{" +
                "\"reference\": \"4990012\"," +
                "\"period\": \"201211\"," +
                "\"survey\": \"066 \"" +
                "}," +
                "{" +
                "\"reference\": \"4990012\"," +
                "\"period\": \"201212\"," +
                "\"survey\": \"066 \"" +
                "}" +
                "]" +
                "}" +
                "}" +
                "}";

        String outputString = "{" +
                "\"data\": [" +
                "{" +
                "\"reference\": \"4990012\"," +
                "\"period\": \"201211\"," +
                "\"survey\": \"066 \"" +
                "}," +
                "{" +
                "\"reference\": \"4990012\"," +
                "\"period\": \"201212\"," +
                "\"survey\": \"066 \"" +
                "}" +
                "]" +
                "}";


        qlQueryResponse response = new qlQueryResponse(inputString);
        assertEquals(outputString, response.stringConvert());
    }

    @Test
    void stringConvert_attributeNotPresent() {
        String inputString = "{" +
                "\"data\": {" +
                "\"nodes\": []" +
                "}" +
                "}" +
                "}";
        qlQueryResponse response = new qlQueryResponse(inputString);
        assertEquals("", response.stringConvert());
    }


}
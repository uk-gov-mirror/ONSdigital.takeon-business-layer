package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.controller.qlQueryResponse;

import static org.junit.jupiter.api.Assertions.*;

class qlQueryResponseTest {

    @Test
    void filter() {
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
}
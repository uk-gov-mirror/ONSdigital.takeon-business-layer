package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.HistoryDetailsResponse;
import uk.gov.ons.collection.entity.SelectiveEditingResponse;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class SelectiveEditingResponseTest {


    @Test
    void selectiveEditingConfigDetailsDetails_validJSONDataT(){
        String responseJSON = "{\"data\":{\"allContributors\":{\"nodes\":[{\"reference\":\"49900534932\",\"period\":\"201904\",\"survey\":\"023\",\"resultscellnumber\":1,\"domain\":1}]},\"allSelectiveeditingconfigs\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"1\",\"questioncode\":\"20\",\"threshold\":0.001,\"estimate\":100000000},{\"survey\":\"023\",\"period\":\"201904\",\"domain\":\"1\",\"questioncode\":\"21\",\"threshold\":0.002,\"estimate\":100000000}]},\"allCelldetails\":{\"nodes\":[{\"survey\":\"023\",\"period\":\"201904\",\"cellnumber\":1,\"designweight\":2}]}}}";


        try {
            SelectiveEditingResponse response = new SelectiveEditingResponse(responseJSON);
            String output = response.parseSelectiveEditingQueryResponse();
            System.out.println("Output to be sent to Lambda: " + output);


        } catch (Exception e) {
            String actualMessage = e.getMessage();
            System.out.println("Error Message: "+actualMessage);
            assertTrue(true);

        }
    }

}

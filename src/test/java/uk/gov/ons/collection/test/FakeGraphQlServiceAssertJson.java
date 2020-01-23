package uk.gov.ons.collection.test;

import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import uk.gov.ons.collection.service.ServiceInterface;

public class FakeGraphQlServiceAssertJson implements ServiceInterface {

    private String queryOutput;

    public FakeGraphQlServiceAssertJson(String queryOutput) {
        this.queryOutput = queryOutput;
    }

    public String runQuery(String query) {
        JSONAssert.assertEquals("{}", query, JSONCompareMode.LENIENT);
        return queryOutput;
    }
}
package uk.gov.ons.collection.test;

import uk.gov.ons.collection.service.ServiceInterface;

public class FakeGraphQlService implements ServiceInterface {

    private String queryOutput;

    public FakeGraphQlService(String queryOutput) {
        this.queryOutput = queryOutput;
    }

    public String runQuery(String query) {
        return queryOutput;
    }
}
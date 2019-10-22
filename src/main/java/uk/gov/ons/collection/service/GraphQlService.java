package uk.gov.ons.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GraphQlService {

    @Autowired
    GraphQlProxy graphQlProxy;

    public String qlSearch(String queryQlJson) {
        return graphQlProxy.runQuery(queryQlJson);
    }
}

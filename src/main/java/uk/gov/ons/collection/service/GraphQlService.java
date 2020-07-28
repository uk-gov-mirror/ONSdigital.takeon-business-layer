package uk.gov.ons.collection.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class GraphQlService implements ServiceInterface {

    @Autowired
    GraphQlProxy graphQlProxy;

    public String qlSearch(String queryQlJson) {
        log.debug("QlQuery invoked :: " + queryQlJson);
        return graphQlProxy.runQuery(queryQlJson);
    }

    public String runQuery(String query) {
        log.debug("QlQuery invoked :: " + query);
        var response = graphQlProxy.runQuery(query);
        log.debug("QlQuery response :: " + response);
        return response;
    }
}

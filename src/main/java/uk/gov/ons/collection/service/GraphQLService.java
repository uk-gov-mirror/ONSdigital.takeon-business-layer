package uk.gov.ons.collection.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class GraphQLService {

    @Autowired
    GraphQLProxy GraphQLProxy;

    public String qlSearch(String queryQlJSON){
        return GraphQLProxy.runQuery(queryQlJSON);
    }
}

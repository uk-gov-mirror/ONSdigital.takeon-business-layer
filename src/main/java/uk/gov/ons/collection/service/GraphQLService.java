package uk.gov.ons.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GraphQLService {

    @Autowired
    GraphQLProxy GraphQLProxy;

    public String qlSearch(String queryQlJSON){
        return GraphQLProxy.runQuery(queryQlJSON);
    }
}

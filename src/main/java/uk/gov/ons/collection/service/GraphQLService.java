package uk.gov.ons.collection.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.gov.ons.collection.entity.ContributorEntity;

@Log4j2
@Service
public class GraphQLService {

    @Autowired
    GraphQLProxy GraphQLProxy;

    public Iterable qlSearch(String parameters){
        log.info("Reached GraphQL service");
        System.out.println("query to graphql: " + parameters);
        return GraphQLProxy.searchContributor(parameters);
    }
}

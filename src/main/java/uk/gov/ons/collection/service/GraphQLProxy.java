package uk.gov.ons.collection.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "graphql")
public interface GraphQLProxy {

    @PostMapping(value = "/graphql", consumes = MediaType.APPLICATION_JSON_VALUE)
    String runQuery(@RequestBody String query);
}
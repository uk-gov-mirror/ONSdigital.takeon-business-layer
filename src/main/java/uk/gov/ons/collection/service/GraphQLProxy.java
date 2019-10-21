package uk.gov.ons.collection.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "graphql")
public interface GraphQlProxy {

    @PostMapping(value = "/graphql", consumes = MediaType.APPLICATION_JSON_VALUE)
    String runQuery(@RequestBody String query);
}
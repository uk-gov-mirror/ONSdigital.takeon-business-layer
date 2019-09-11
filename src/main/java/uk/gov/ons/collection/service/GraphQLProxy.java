package uk.gov.ons.collection.service;

import feign.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "graphql")
public interface GraphQLProxy {

    @RequestMapping(method = RequestMethod.POST, value = "/graphql", produces = "application/json")
//    @Headers("Content-Type: application/json")
    Iterable searchContributor(@RequestBody String searchVars);
}
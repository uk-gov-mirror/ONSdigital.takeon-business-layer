package uk.gov.ons.collection.service;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@FeignClient(name="graphql")
public interface GraphQLProxy {

    @RequestMapping(method = RequestMethod.POST, value = "/graphql")
    @Headers("Content-Type: application/json")
    @ResponseBody
    public ResponseEntity searchContributor(@RequestBody String searchVars);

}
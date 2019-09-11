package uk.gov.ons.collection.service;

import feign.Headers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RibbonClient(name = "graphql")
public interface GraphQLProxy {

    @RequestMapping(method = RequestMethod.POST, value = "/graphql")
    @Headers("Content-Type: application/json")
    public ResponseEntity searchContributor(@RequestBody String searchVars);
}
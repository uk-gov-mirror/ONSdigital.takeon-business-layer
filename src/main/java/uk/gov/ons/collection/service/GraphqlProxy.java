package uk.gov.ons.collection.service;

import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.collection.entity.ContributorEntity;

//@FeignClient(name="persistence-layer")
//public interface ContributorProxy {
//
//
//    @GetMapping("/contributor/searchByLike/{searchVars}")
//    public Iterable<ContributorEntity> searchContributor(@PathVariable(value="searchVars") String searchVars);
//
//}

@FeignClient(name="graphql")
public interface GraphqlProxy {

    @RequestMapping(method = RequestMethod.POST, value = "/graphql")
    @Headers("Content-Type: application/json")
    public String searchContributor(@RequestBody String searchVars);

}
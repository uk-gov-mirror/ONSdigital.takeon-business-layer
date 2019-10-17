package uk.gov.ons.collection.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.collection.entity.ContributorEntity;

@FeignClient(name="persistence-layer")
public interface ContributorProxy {


    @GetMapping("/contributor/searchByLike/{searchVars}")
    public Iterable<ContributorEntity> searchContributor(@PathVariable(value="searchVars") String searchVars);

}
package uk.gov.ons.collection.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.gov.ons.collection.entity.ContributorEntity;

@FeignClient(name="PersistenceLayerApp")
public interface ContributorProxy {

    @GetMapping("/contributor/searchByLike/{searchVars}")
    public Iterable<ContributorEntity> searchContributor(@PathVariable(value="searchVars") String searchVars);

}

package uk.gov.ons.collection.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.gov.ons.collection.entity.FormDefintionEntity;

@FeignClient(name="PersistenceLayerApp")
public interface FormDefinitionProxy {

    @GetMapping("/FormDefinition/GetFormDefinition/{searchVars}")
    public Iterable<FormDefintionEntity> getFormDefinition(@PathVariable(value="searchVars") String searchVars);

}
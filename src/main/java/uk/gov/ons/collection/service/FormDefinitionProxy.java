package uk.gov.ons.collection.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.gov.ons.collection.entity.FormDefinitionEntity;

@FeignClient(name = "persistence-layer")
public interface FormDefinitionProxy {
    @GetMapping("/FormDefinition/GetFormDefinition/{searchVars}")
    public Iterable<FormDefinitionEntity> getFormDefinition(@PathVariable(value = "searchVars") String searchVars);
}
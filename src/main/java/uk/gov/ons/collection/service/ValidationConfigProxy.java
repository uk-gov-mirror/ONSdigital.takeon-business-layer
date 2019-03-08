package uk.gov.ons.collection.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.gov.ons.collection.entity.ValidationOutputEntity;

import java.util.List;

@FeignClient(name="ValidationPersistenceLayerApp")
public interface ValidationConfigProxy {

    @GetMapping(value = "/validation-pl/validations/configurationFromContributor/{parameters}")
    public List<ValidationOutputEntity> getConfig(@PathVariable(value = "parameters") String parameters);

}

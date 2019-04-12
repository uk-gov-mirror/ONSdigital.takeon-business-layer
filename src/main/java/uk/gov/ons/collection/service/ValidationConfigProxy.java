package uk.gov.ons.collection.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.gov.ons.collection.entity.ValidationFormEntity;
import uk.gov.ons.collection.entity.ValidationOutputEntity;

import java.util.List;

@FeignClient(name="validation-persistence-layer")
public interface ValidationConfigProxy {

    @GetMapping(value = "/validation-pl/validations/configuration/form/{params}")
    public List<ValidationFormEntity> getConfig(@PathVariable(value = "params") String params);

}

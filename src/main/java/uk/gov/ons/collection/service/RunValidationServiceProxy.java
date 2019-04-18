package uk.gov.ons.collection.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import uk.gov.ons.collection.entity.ReturnedValidationOutputs;

@FeignClient(name="validation-engine")
public interface RunValidationServiceProxy {

    @PostMapping(value = "/validation/valuepresent/", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ReturnedValidationOutputs runVaas(@RequestBody String body);

}

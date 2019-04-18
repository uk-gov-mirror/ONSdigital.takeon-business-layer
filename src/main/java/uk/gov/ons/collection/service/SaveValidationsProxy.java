package uk.gov.ons.collection.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="validation-persistence-layer")
public interface SaveValidationsProxy {

    @PutMapping(value = "/validation-pl/validations/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String putValidations(@RequestBody String body );

}

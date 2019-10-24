package uk.gov.ons.collection.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "persistence-layer")
public interface UpdateFormStatusProxy {

    @PutMapping(value = "/Update/Status/{args}",  consumes = MediaType.APPLICATION_JSON_VALUE)
    public String updateStatus(@PathVariable(value = "args") String args, @RequestBody String body);
}

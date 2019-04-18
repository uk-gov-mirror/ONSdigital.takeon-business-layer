package uk.gov.ons.collection.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="persistence-layer")
public interface QuestionResponseProxy {

    @PutMapping(value = "/Upsert/UpdateResponses/{args}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String putResponses(@PathVariable(value="args") String args, @RequestBody String body );

}

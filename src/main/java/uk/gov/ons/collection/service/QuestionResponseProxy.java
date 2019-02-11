package uk.gov.ons.collection.service;

import feign.Headers;
import org.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.ons.collection.entity.ContributorEntity;
import uk.gov.ons.collection.entity.QuestionResponseEntity;

import java.awt.*;
import java.net.URLEncoder;

@FeignClient(name="PersistenceLayerApp")
public interface QuestionResponseProxy {

    @PutMapping(value = "/Upsert/UpdateResponses/{args}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String putResponses(@PathVariable(value="args") String args, @RequestBody String body );

}

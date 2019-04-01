package uk.gov.ons.collection.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import uk.gov.ons.collection.entity.QuestionResponseEntity;

import java.util.List;

@FeignClient(name="PersistenceLayerApp")
public interface CurrentResponsesProxy {

    @GetMapping("/responses/CurrentResponses/{searchVars}")
    public List<QuestionResponseEntity> responses(@PathVariable(value="searchVars") String searchVars);
}

package uk.gov.ons.collection.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.gov.ons.collection.entity.QuestionResponseEntity;

import java.util.List;

@Service
public class CurrentResponseService {

    @Autowired
    CurrentResponsesProxy currentResponsesProxy;

    @ResponseBody
    public List<QuestionResponseEntity> getCurrentResponses(String params){
        System.out.printf("parameters recieved: %s\n", params);
        return currentResponsesProxy.responses(params);
    }
}

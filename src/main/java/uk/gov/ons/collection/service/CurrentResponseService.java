package uk.gov.ons.collection.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;
import uk.gov.ons.collection.entity.QuestionResponseEntity;

import java.util.List;

@Log4j2
@Service
public class CurrentResponseService {

    @Autowired
    CurrentResponsesProxy currentResponsesProxy;

    @ResponseBody
    public List<QuestionResponseEntity> getCurrentResponses(String params) {
        log.info("Parameters Received { }", params);
        return currentResponsesProxy.responses(params);
    }
}

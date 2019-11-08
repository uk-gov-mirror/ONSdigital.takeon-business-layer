package uk.gov.ons.collection.service;

import org.json.JSONArray;
import org.json.JSONObject;
import uk.gov.ons.collection.entity.ResponseData;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.log4j.Log4j2;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Log4j2
public class CompareUIAndCurrentResponses {

    private String user;
    private String reference;
    private String period;
    private String survey;
    private JSONArray updatedResponseData;
    private List<ResponseData> currentHeldResponses;
    Timestamp time = new Timestamp(new Date().getTime());

    public CompareUIAndCurrentResponses(List<ResponseData> currentResponses, JSONObject responses) {

        this.currentHeldResponses = currentResponses;
        this.updatedResponseData = responses.getJSONArray("responses");
        this.user = responses.getString("user");
        this.reference = responses.getString("reference");
        this.period = responses.getString("period");
        this. survey = responses.getString("survey");

    }

    public List<ResponseData> getUIResponseData() {
        List<ResponseData> uiDataResponses = new ArrayList<ResponseData>();
        for (int i = 0; i< updatedResponseData.length(); i++) {
            ResponseData uiResponseData = new ResponseData();
            uiResponseData.setReference(reference);
            uiResponseData.setPeriod(period);
            uiResponseData.setSurvey(survey);
            uiResponseData.setUser(user);
            uiResponseData.setInstance(updatedResponseData.getJSONObject(i).getInt("instance"));
            final String question = updatedResponseData.getJSONObject(i).getString("question");
            uiResponseData.setQuestionCode(question);
            final String response = updatedResponseData.getJSONObject(i).getString("response");
            uiResponseData.setResponse(response);
            uiDataResponses.add(uiResponseData);
        }

        ObjectMapper mapper = new ObjectMapper();
        uiDataResponses = mapper.readValue(updatedResponseData, new TypeReference<List>(){});

        return uiDataResponses;
    }
    public List<ResponseData> getFinalConsolidatedResponses() {
        List<ResponseData> finalOutputData = new ArrayList<ResponseData>();

        for(ResponseData element: getUIResponseData()) {

            if (!checkIfExists(element) & !Objects.equals(element.getResponse(), "")) {
                element.setCreatedDate(time.toString());
                element.setCreatedBy(user);
                element.setLastUpdatedBy(user);
                element.setLastUpdateDate(time.toString());
                finalOutputData.add(element);
            } else {
                log.info("element exists");
                if (checkIfChanged(element)) {
                    log.info("element has also changed");
                    ResponseData heldEntity = getResponseEntity(element);
                    element.setCreatedDate(heldEntity.getCreatedDate());
                    element.setCreatedBy(heldEntity.getCreatedBy());
                    element.setLastUpdatedBy(user);
                    element.setLastUpdateDate(time.toString());
                    finalOutputData.add(element);
                }
            }


        }

        return finalOutputData;
    }


    private boolean checkIfChanged(ResponseData entityToCheck) {
        for (ResponseData element: currentHeldResponses) {
            if (Objects.equals(element.getQuestionCode(), entityToCheck.getQuestionCode())
                    && Objects.equals(element.getInstance(), entityToCheck.getInstance())
                    && !Objects.equals(element.getResponse(), entityToCheck.getResponse())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIfExists(ResponseData entityToCheck) {
        for (ResponseData element: currentHeldResponses) {
            if (Objects.equals(element.getQuestionCode(), entityToCheck.getQuestionCode())
                    && Objects.equals(element.getInstance(), entityToCheck.getInstance())) {
                return true;
            }
        }
        return false;
    }

    private ResponseData getResponseEntity(ResponseData entity) {
        for (ResponseData element: currentHeldResponses) {
            if (Objects.equals(element.getInstance().toString(), entity.getInstance().toString())
                    && Objects.equals(element.getQuestionCode(), entity.getQuestionCode())) {
                return element;
            }
        }
        return null;
    }


}

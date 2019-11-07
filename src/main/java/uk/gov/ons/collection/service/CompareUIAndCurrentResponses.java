package uk.gov.ons.collection.service;

import org.json.JSONArray;
import org.json.JSONObject;
import uk.gov.ons.collection.entity.CurrentResponseData;
import uk.gov.ons.collection.entity.UIResponseData;

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
    private List<CurrentResponseData> currentHeldResponses;
    Timestamp time = new Timestamp(new Date().getTime());

    public CompareUIAndCurrentResponses(List<CurrentResponseData> currentResponses, JSONObject responses) {

        this.currentHeldResponses = currentResponses;
        this.updatedResponseData = responses.getJSONArray("responses");
        this.user = responses.getString("user");
        this.reference = responses.getString("reference");
        this.period = responses.getString("period");
        this. survey = responses.getString("survey");

    }

    public List<UIResponseData> getUIResponseData() {
        List<UIResponseData> uiDataResponses = new ArrayList<UIResponseData>();
        for (int i = 0; i< updatedResponseData.length(); i++) {
            UIResponseData uiResponseData = new UIResponseData();
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

        return uiDataResponses;
    }
    public List<UIResponseData> getFinalConsolidatedResponses() {
        List<UIResponseData> finalOutputData = new ArrayList<UIResponseData>();

        for(UIResponseData element: getUIResponseData()) {

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
                    CurrentResponseData heldEntity = getResponseEntity(element);
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


    private boolean checkIfChanged(UIResponseData entityToCheck) {
        for (CurrentResponseData element: currentHeldResponses) {
            if (Objects.equals(element.getQuestionCode(), entityToCheck.getQuestionCode())
                    && Objects.equals(element.getInstance(), entityToCheck.getInstance())
                    && !Objects.equals(element.getResponse(), entityToCheck.getResponse())) {
                return true;
            }
        }
        return false;
    }

    private boolean checkIfExists(UIResponseData entityToCheck) {
        for (CurrentResponseData element: currentHeldResponses) {
            if (Objects.equals(element.getQuestionCode(), entityToCheck.getQuestionCode())
                    && Objects.equals(element.getInstance(), entityToCheck.getInstance())) {
                return true;
            }
        }
        return false;
    }

    private CurrentResponseData getResponseEntity(UIResponseData entity) {
        for (CurrentResponseData element: currentHeldResponses) {
            if (Objects.equals(element.getInstance().toString(), entity.getInstance().toString())
                    && Objects.equals(element.getQuestionCode(), entity.getQuestionCode())) {
                return element;
            }
        }
        return null;
    }


}

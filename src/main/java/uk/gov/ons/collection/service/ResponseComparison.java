package uk.gov.ons.collection.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import uk.gov.ons.collection.entity.OriginalFormData;
import uk.gov.ons.collection.entity.QuestionResponseEntity;
import uk.gov.ons.collection.entity.UpdatedFormData;

import java.sql.Timestamp;
import java.util.*;

public class ResponseComparison {

    private JSONArray updatedResponseData;
    private String user;
    private String reference;
    private String period;
    private String survey;
    private List<UpdatedFormData> updatedFormDataList = new ArrayList<>();
    private List<UpdatedFormData> constructedObjects = new ArrayList<>();
    private List<QuestionResponseEntity> currentlyHeldResponses;
    Timestamp time = new Timestamp(new Date().getTime());

    CurrentResponseService currentResponseService;

    public ResponseComparison(CurrentResponseService currentResponseService, JSONObject responses){
        this.currentResponseService = currentResponseService;
        updatedResponseData = responses.getJSONArray("Updated Responses");
        user = responses.getJSONObject("user").getString("user");
        reference = responses.getString("reference");
        period = responses.getString("period");
        survey = responses.getString("survey");
        currentlyHeldResponses = getCurrentlyHeldResponses();
    }

    public List<UpdatedFormData> getOnlyUpdatedResponses(){
        DeserialiseFromData deserialisedFormData = new DeserialiseFromData(updatedResponseData);
        updatedFormDataList = deserialisedFormData.parseJsonObjects();
        constructedObjects = deserialisedFormData.constructUpdatedObjectsFromKey(updatedFormDataList);
        return getUpdatedResponses();
    }

    public List<UpdatedFormData> getUpdatedResponses(){
        List<UpdatedFormData> outputData = new ArrayList<>();
        for(UpdatedFormData element: constructedObjects){
            System.out.println(checkIfExists(element));
            if(!checkIfExists(element) & !Objects.equals(element.getResponse(), "" )){
                    element.setCreatedDate(time.toString());
                    element.setCreatedBy(user);
                    element.setLastUpdatedBy(user);
                    element.setLastUpdatedDate(time.toString());
                    outputData.add(element);

            }
            else {
                System.out.println("element exists");
                if(checkIfChanged(element)){
                    System.out.println("element has also changed");
                    QuestionResponseEntity heldEntity =  getResponseEntity(element);
                    System.out.println(heldEntity.toString());
                    System.out.println(heldEntity.getCreatedBy());
                    System.out.println(heldEntity.getCreatedDate());
                    element.setCreatedDate(heldEntity.getCreatedDate().toString());
                    element.setCreatedBy(heldEntity.getCreatedBy());
                    element.setLastUpdatedBy(user);
                    element.setLastUpdatedDate(time.toString());
                    outputData.add(element);
                }
            }
        }
        System.out.println(outputData.toString());
        return outputData;
    }

    public boolean checkIfChanged(UpdatedFormData entityToCheck){
        for(QuestionResponseEntity element: currentlyHeldResponses){
            if(Objects.equals(element.getQuestionCode(), entityToCheck.getQuestionCode())
                    && Objects.equals(element.getInstance().toString(), entityToCheck.getInstance().toString())
                    && !Objects.equals(element.getResponse().toString(), entityToCheck.getResponse().toString())){
                return true;
            }
        }
        return false;
    }

    private boolean checkIfExists(UpdatedFormData entityToCheck){
        for(QuestionResponseEntity element: currentlyHeldResponses){
            if(Objects.equals(element.getQuestionCode(), entityToCheck.getQuestionCode()) &&
                    Objects.equals(Integer.toString(element.getInstance()), entityToCheck.getInstance())){
                return true;
            }
        }
        return false;
    }

    public QuestionResponseEntity getResponseEntity(UpdatedFormData entity){
        for(QuestionResponseEntity element: currentlyHeldResponses) {
            if (Objects.equals(element.getInstance().toString(), entity.getInstance())
                    && Objects.equals(element.getQuestionCode(), entity.getQuestionCode())) {
                return element;
            }
        }
        return null;
    }

    public List<QuestionResponseEntity> getCurrentlyHeldResponses(){
        String params = "reference=" + reference +";period=" + period + ";survey=" + survey;
        return currentResponseService.getCurrentResponses(params);
    }
}

package uk.gov.ons.collection.service;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.json.JSONArray;
import org.json.JSONObject;
import uk.gov.ons.collection.entity.QuestionResponseEntity;
import uk.gov.ons.collection.entity.UpdatedFormData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeserialiseFromData {

    private JSONArray updatedFormData;
    private UpdatedFormData newFormData;
    private JSONArray updatedResponseData;

    private List<UpdatedFormData> updatedFormDataList = new ArrayList<>();
    private List<UpdatedFormData> constructedObjects = new ArrayList<>();
    private List<QuestionResponseEntity> currentlyHeldResponses;

    public DeserialiseFromData(JSONArray formData){
        updatedFormData = formData;
    }

    public List<UpdatedFormData> parseJsonObjects(){
        for(int index = 0; index < updatedResponseData.length(); index++){
            UpdatedFormData updatedFormData = new UpdatedFormData();
            String jsonKey = updatedResponseData.getJSONObject(index).keys().next();
            updatedFormData.setKey(jsonKey);
            updatedFormData.setResponse(updatedResponseData.getJSONObject(index).getString(jsonKey));
            updatedFormDataList.add(updatedFormData);
        }
        return updatedFormDataList;
    }

    public List<UpdatedFormData> constructUpdatedObjectsFromKey(List<UpdatedFormData> updatedFormDataList){
        for(UpdatedFormData element: updatedFormDataList){
            Map<String, String> tempHolder;
            tempHolder = element.getQcodeAndInstance(element.getKey());
            element.setQuestionCode(tempHolder.get("questionCode"));
            element.setInstance(tempHolder.get("instance"));
            constructedObjects.add(element);
        }

        return constructedObjects;
    }
}

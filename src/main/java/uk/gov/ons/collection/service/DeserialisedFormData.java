package uk.gov.ons.collection.service;

import com.fasterxml.jackson.annotation.JsonSetter;
import org.json.JSONArray;
import org.json.JSONObject;
import uk.gov.ons.collection.entity.QuestionResponseEntity;
import uk.gov.ons.collection.entity.UpdatedFormData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DeserialisedFormData {

    private JSONArray updatedFormData;
    private UpdatedFormData newFormData;
    private JSONArray updatedResponseData;

    private List<UpdatedFormData> updatedFormDataList = new ArrayList<>();
    private List<UpdatedFormData> constructedObjects = new ArrayList<>();
    private List<QuestionResponseEntity> currentlyHeldResponses;

    public DeserialisedFormData(JSONArray formData){
        updatedFormData = formData;
    }

    public List<UpdatedFormData> parseJsonObjects(){
        for(int index = 0; index < updatedFormData.length(); index++){
            UpdatedFormData updatedFormDataObject = new UpdatedFormData();
            String jsonKey = updatedFormData.getJSONObject(index).keys().next();
            updatedFormDataObject.setKey(jsonKey);
            updatedFormDataObject.setResponse(updatedFormData.getJSONObject(index).getString(jsonKey));
            updatedFormDataList.add(updatedFormDataObject);
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

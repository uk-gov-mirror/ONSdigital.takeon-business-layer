package uk.gov.ons.collection.service;

import org.json.JSONArray;
import org.json.JSONObject;
import uk.gov.ons.collection.entity.UpdatedFormData;
import uk.gov.ons.collection.entity.ValidationOutputEntity;

import java.util.*;

public class ConcatenateJson {

    private Iterable<ValidationOutputEntity> heldValidationOutputs;
    private JSONArray formDataFromUi;
    private List<UpdatedFormData> updatedFormDataList = new ArrayList<>();
    private List<UpdatedFormData> constructedObjects = new ArrayList<>();

    public ConcatenateJson(Iterable<ValidationOutputEntity> validationOutputEntities,
                           JSONObject formData){
        heldValidationOutputs = validationOutputEntities;
        formDataFromUi = formData.getJSONArray("Updated Responses");
    }

    public Iterable<ValidationOutputEntity> setFormFields(){
        DeserialisedFormData deserialisedFormData = new DeserialisedFormData(formDataFromUi);
        updatedFormDataList = deserialisedFormData.parseJsonObjects();
        constructedObjects = deserialisedFormData.constructUpdatedObjectsFromKey(updatedFormDataList);
        for(ValidationOutputEntity element: heldValidationOutputs){
            for(UpdatedFormData form: constructedObjects){
                if (Objects.equals(element.getQuestionCode(), form.getQuestionCode())){
                    element.setCurrentResponse(form.getResponse());
                }
            }
        }
        return heldValidationOutputs;
    }

}

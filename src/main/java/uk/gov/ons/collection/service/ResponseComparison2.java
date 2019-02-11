package uk.gov.ons.collection.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class ResponseComparison2 {

    private JSONArray updatedResponses;
    private JSONArray originalResponses;
    private JSONObject reindexedUpdatedResponses;
    private JSONObject reindexedOriginalResponses;
    private JSONObject reindexedUpdatedResponsesExtended;
    private boolean blankFormFlag = false;
    private boolean partialFormFlag = false;

    public ResponseComparison2(JSONObject responses){
        updatedResponses = responses.getJSONArray("Updated Responses");
        originalResponses = responses.getJSONArray("Original Responses");
        reindexedUpdatedResponses = reindex(updatedResponses);

        if(isFormBlank(originalResponses)){
            System.out.println(originalResponses);
            blankFormFlag = true;
        }

        else if(isFormPartial(updatedResponses, originalResponses)){
            partialFormFlag = true;
            reindexedOriginalResponses = reindex(createIndex(originalResponses));
        }

        else {
            System.out.println("Form is complete");
            originalResponses = createIndex(originalResponses);
            reindexedOriginalResponses = reindex(originalResponses);
            reindexedUpdatedResponsesExtended = addToUpdatedResponses();
        }
    }

    public JSONObject getUpdatedResponses(){
        JSONObject outputJson = new JSONObject();
        if(blankFormFlag){
            return outputJson.put("Updated Responses", processBlankForm(reindexedUpdatedResponses));
        }
        else if(partialFormFlag){
            return outputJson.put("Updated Responses", processPartialForm(reindexedUpdatedResponses));
        }
        else {
            return outputJson.put("Updated Responses", getOnlyUpdatedResponses(reindexedUpdatedResponsesExtended, reindexedOriginalResponses));
        }
    }

    public boolean isFormPartial(JSONArray updatedResponses, JSONArray originalResponses){
        if(updatedResponses.length() > originalResponses.length() && !blankFormFlag){
            System.out.println("form is partial");
            return true;
        }
        return false;
    }

    public JSONObject processPartialForm(JSONObject inputObject){
        JSONArray outputArray = new JSONArray();
        JSONObject outputObject = new JSONObject();
        JSONObject newPartialResponsesToProcess = new JSONObject();
        JSONObject partialResponseToProcess = new JSONObject();

        System.out.println(inputObject.toString());
        for(String key: inputObject.keySet()){
            JSONObject tempObject = new JSONObject();
            if(inputObject.getJSONObject(key).keySet().contains("newCreatedBy")){
                newPartialResponsesToProcess.put(key, inputObject.getJSONObject(key));
            }
            else{
                Map<String, String> qcodeAndInstance = getQcodeAndInstance(key);
                tempObject.put("instance", qcodeAndInstance.get("instance"));
                tempObject.put("questionCode", qcodeAndInstance.get("questionCode"));
                tempObject.put("response", inputObject.getJSONObject(key).getString("response"));
                partialResponseToProcess.put(key, tempObject);
            }
        }
        System.out.printf("New records to insert: %s\n", processBlankForm(newPartialResponsesToProcess).toString());
        System.out.printf("Records to update: %s\n", getOnlyUpdatedResponses(partialResponseToProcess, reindexedOriginalResponses).toString());
        JSONObject processedNewPartialResponses = processBlankForm(newPartialResponsesToProcess);
        JSONObject processedPartialResponses = getOnlyUpdatedResponses(partialResponseToProcess, reindexedOriginalResponses);
        for(String key: processedNewPartialResponses.keySet()){
            outputObject.put(key, processedNewPartialResponses.getJSONObject(key));
        }

        for (String key: processedPartialResponses.keySet()){
            outputObject.put(key, processedPartialResponses.getJSONObject(key));
        }
        return outputObject;
    }

    public JSONObject processBlankForm(JSONObject inputObject){
        Map<String, String> keyAtoms;
        JSONObject outputObject = new JSONObject();
        for(String key: inputObject.keySet()){
            JSONObject tempObject = new JSONObject();
            keyAtoms = getQcodeAndInstance(key);
            System.out.println(keyAtoms.toString());
            if(Objects.equals(inputObject.getJSONObject(key).getString("response"), "")){
                continue;
            }
            else{
                tempObject.put("instance", keyAtoms.get("instance"));
                tempObject.put("questionCode", keyAtoms.get("questionCode"));
                tempObject.put("newCreatedBy", inputObject.getJSONObject(key).getString("newCreatedBy"));
                tempObject.put("newCreatedDate", inputObject.getJSONObject(key).getString("newCreatedDate"));
                tempObject.put("response", inputObject.getJSONObject(key).getString("response"));
                outputObject.put(key, tempObject);
            }
        }
        return outputObject;
    }

    public boolean isFormBlank (JSONArray inputArray){
        System.out.println(inputArray.getJSONObject(0).keySet());
        if(!inputArray.getJSONObject(0).keySet().contains("createdBy")){
            return true;
        }
        else{
            return false;
        }
    }

    public Map<String, String> getQcodeAndInstance(String key){
        String[] parts;
        String[] atoms;
        Map<String, String> atomMap = new HashMap<>();
        parts = key.split("\\|");
        System.out.println(key);
            for (String part: parts){
                atoms = part.split(":");
                atomMap.put(atoms[0], atoms[1]);
            }
        return atomMap;
    }

    public JSONArray createIndex(JSONArray inputArray){
        JSONArray outputArray = new JSONArray();
        for(int index = 0; index < inputArray.length(); index++){
            JSONObject getJsonFromArray = inputArray.getJSONObject(index);
            System.out.printf("Blank form original responses: %s\n", getJsonFromArray);
            String newKey = concatenate(getJsonFromArray, "questionCode", "instance");
            getJsonFromArray.put(newKey, getJsonFromArray.getString("response"));
            outputArray.put(getJsonFromArray);
        }
        return outputArray;
    }

    public JSONObject reindex(JSONArray responseArray){
        JSONObject outputObject = new JSONObject();
        JSONObject relabledResponses;
        String combinedKey;
        for (int index = 0; index < responseArray.length(); index++){
            JSONObject tempObject = responseArray.getJSONObject(index);
            combinedKey = getCombinedKey(tempObject);
            relabledResponses = relabelResponses(combinedKey, tempObject);
            outputObject.put(combinedKey, relabledResponses);
        }
        return outputObject;
    }

    public String getCombinedKey(JSONObject responseObject){
        for(String key: responseObject.keySet()){
            if(key.matches("(.+\\|.+)")){
                return key;
            }
        }
        return null;
    }

    public JSONObject relabelResponses(String key, JSONObject inputObject){
        return inputObject.put("response", inputObject.getString(key));
    }

    public JSONObject getOnlyUpdatedResponses(JSONObject updatedResponses, JSONObject originalResponses){
        JSONObject outputJson = new JSONObject();
        for(String key: updatedResponses.keySet()){
            if(!Objects.equals(updatedResponses.getJSONObject(key).getString("response"), originalResponses.getJSONObject(key).getString("response"))){
                System.out.printf("json: %s \n", updatedResponses.getJSONObject(key));
                outputJson.put(key, updatedResponses.getJSONObject(key));
            }
        }
        return outputJson;
    }

    private JSONObject addToUpdatedResponses(){
        JSONObject outputUpdatedResponses = new JSONObject();
        for(String key: reindexedUpdatedResponses.keySet()){
            JSONObject tempObject = reindexedOriginalResponses.getJSONObject(key);
            JSONObject holderObject = new JSONObject();
            if(tempObject.keySet().contains("newCreatedBy")) {
                holderObject.put("newCreatedBy", tempObject.getString("newCreatedBy"));
                holderObject.put("newCreatedDate", tempObject.getString("newCreatedDate"));
            }
            else{
                holderObject.put("createdBy", tempObject.getString("createdBy"));
                holderObject.put("createdDate", tempObject.getString("createdDate"));
            }
            holderObject.put("instance", tempObject.getString("instance"));
            holderObject.put("response", reindexedUpdatedResponses.getJSONObject(key).getString("response"));
            holderObject.put("questionCode", tempObject.getString("questionCode"));
            outputUpdatedResponses.put(key, holderObject);
        }
        return outputUpdatedResponses;
    }

    private String concatenate(JSONObject inputObject, String firstAttribute, String secondAttribute){
        return firstAttribute+":"+inputObject.get(firstAttribute)+"|"+secondAttribute+":"+inputObject.get(secondAttribute);
    }
}

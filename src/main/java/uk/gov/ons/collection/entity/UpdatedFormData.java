package uk.gov.ons.collection.entity;

import java.util.HashMap;
import java.util.Map;

public class UpdatedFormData {
    private String key;
    private String instance;
    private String questionCode;
    private String response;
    private String createdBy;
    private String createdDate;
    private String lastUpdatedDate;
    private String lastUpdatedBy;



    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
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

    @Override
    public String toString() {
        return "UpdatedFormData{" +
                "key='" + key + '\'' +
                ", instance='" + instance + '\'' +
                ", questionCode='" + questionCode + '\'' +
                ", response='" + response + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", lastUpdatedDate='" + lastUpdatedDate + '\'' +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                '}';
    }
}

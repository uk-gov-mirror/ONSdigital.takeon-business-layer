package uk.gov.ons.collection.entity;

import javax.persistence.Column;
import javax.persistence.Id;

public class FormDefintionEntity {

    private Integer formId;

    private String questionCode;

    private String displayQuestionNumber;

    private String displayText;
    
    private String type;

    private String createdBy;

    private String createdDate;

    private String lastUpadatedBy;

    private String lastUpdatedDate;

    public String getquestionCode() {
        return questionCode;
    }

    public void setquestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    public Integer getFormid() {
        return formId;
    }

    public void setFormid(Integer formid) {
        this.formId = formid;
    }

    public String getdisplayQuestionNumber() {
        return displayQuestionNumber;
    }

    public void setdisplayQuestionNumber(String displayQuestionNumber) {
        this.displayQuestionNumber = displayQuestionNumber;
    }

    public String getdisplayText() {
        return displayText;
    }

    public void setdisplayText(String displayText) {
        this.displayText = displayText;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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

    public String getLastUpadatedBy() {
        return lastUpadatedBy;
    }

    public void setLastUpadatedBy(String lastUpadatedBy) {
        this.lastUpadatedBy = lastUpadatedBy;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }
}

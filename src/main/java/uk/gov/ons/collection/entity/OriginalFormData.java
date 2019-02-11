package uk.gov.ons.collection.entity;

public class OriginalFormData {
    public String createdDate;
    public String createdBy;
    public String instance;
    public String response;
    public String questionCode;

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getQuestionCode() {
        return questionCode;
    }

    public void setQuestionCode(String questionCode) {
        this.questionCode = questionCode;
    }

    @Override
    public String toString() {
        return "OriginalFormData{" +
                "createdDate='" + createdDate + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", instance='" + instance + '\'' +
                ", response='" + response + '\'' +
                ", questionCode='" + questionCode + '\'' +
                '}';
    }
}

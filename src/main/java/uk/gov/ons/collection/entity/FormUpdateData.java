package uk.gov.ons.collection.entity;
import java.util.ArrayList;

public class FormUpdateData {
    String username;
    ArrayList<QuestionResponseEntity> originalResponses;
    ArrayList<QuestionResponseEntity> updatedResponses;

    public FormUpdateData(String username, ArrayList<QuestionResponseEntity> originalResponses, ArrayList<QuestionResponseEntity> updatedResponses) {
        this.username = username;
        this.originalResponses = originalResponses;
        this.updatedResponses = updatedResponses;
    }

    public String getUsername() {
        return username;
    }

    public ArrayList<QuestionResponseEntity> getOriginalResponses() {
        return originalResponses;
    }

    public ArrayList<QuestionResponseEntity> getUpdatedResponses() {
        return updatedResponses;
    }

    public String getOnlyChangedResponses(){
        // Determine only changed respponses
        // Convert to Json String and return it
        return "{responses:[]}";
    }

    @Override
    public String toString() {
        return "FormUpdateData{" +
                "username='" + username + '\'' +
                ", originalResponses=" + originalResponses +
                ", updatedResponses=" + updatedResponses +
                '}';
    }
}

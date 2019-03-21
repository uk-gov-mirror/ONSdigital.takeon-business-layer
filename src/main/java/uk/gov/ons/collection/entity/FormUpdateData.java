package uk.gov.ons.collection.entity;
import lombok.*;

import java.util.ArrayList;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FormUpdateData {
    public String username;
    public ArrayList<QuestionResponseEntity> originalResponses;
    public ArrayList<QuestionResponseEntity> updatedResponses;

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

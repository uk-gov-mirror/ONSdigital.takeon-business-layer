package uk.gov.ons.collection.entity;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdatedFormData {
    private String key;
    private String instance;
    private String questionCode;
    private String response;
    private String createdBy;
    private String createdDate;
    private String lastUpdatedDate;
    private String lastUpdatedBy;

    public Map<String, String> getQuestionCodeAndInstance(String key) {
        String[] parts;
        String[] atoms;
        Map<String, String> atomMap = new HashMap<>();
        parts = key.split("\\|");
        for (String part: parts) {
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

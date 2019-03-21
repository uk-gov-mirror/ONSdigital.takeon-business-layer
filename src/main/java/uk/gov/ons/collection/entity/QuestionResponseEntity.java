package uk.gov.ons.collection.entity;

import lombok.*;

import java.sql.Timestamp;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class QuestionResponseEntity {
    private String reference;
    private String period;
    private String survey;
    private String questionCode;
    private Integer instance;
    private String response;
    private String createdBy;
    private Timestamp createdDate;
    private Timestamp lastUpdateDate;
    private String lastUpdatedBy;

    @Override
    public String toString() {
        return "QuestionResponseEntity{" +
                "reference='" + reference + '\'' +
                ", period='" + period + '\'' +
                ", survey='" + survey + '\'' +
                ", questionCode='" + questionCode + '\'' +
                ", instance=" + instance +
                ", response='" + response + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdDate=" + createdDate +
                ", lastUpdateDdate=" + lastUpdateDate +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                '}';
    }
}

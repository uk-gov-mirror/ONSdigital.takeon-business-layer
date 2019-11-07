package uk.gov.ons.collection.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UIResponseData {
    private String reference;
    private String period;
    private String survey;
    private String questionCode;
    private Integer instance;
    private String response;
    private String user;
    private String createdBy;
    private String createdDate;
    private String lastUpdateDate;
    private String lastUpdatedBy;

    @Override
    public String toString() {
        return "UIResponseData{" +
                "reference='" + reference + '\'' +
                ", period='" + period + '\'' +
                ", survey='" + survey + '\'' +
                ", questionCode='" + questionCode + '\'' +
                ", instance='" + instance + '\'' +
                ", response='" + response + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", createdDate='" + createdDate + '\'' +
                ", lastUpdateDate='" + lastUpdateDate + '\'' +
                ", lastUpdatedBy='" + lastUpdatedBy + '\'' +
                '}';
    }

}

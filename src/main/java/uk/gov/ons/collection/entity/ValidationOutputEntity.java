package uk.gov.ons.collection.entity;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ValidationOutputEntity {
    private String reference;
    private String period;
    private String survey;
    private Integer validationID;
    private Integer instance;
    private String primaryValue;
    private String formula;
    private String createdBy;
    private String createdDate;
    private String lastUpdatedBy;
    private String lastUpdatedDate;
}

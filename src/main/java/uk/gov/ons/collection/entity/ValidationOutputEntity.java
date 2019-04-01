package uk.gov.ons.collection.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ValidationOutputEntity {
    @JsonManagedReference
    private ValidationFormEntity validationFormEntity;
    private Integer validationOutputID;
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

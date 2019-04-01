package uk.gov.ons.collection.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ValidationFormEntity {
    @JsonManagedReference
    private ValidationRuleEntity validationRuleEntity;
    @JsonManagedReference
    private ValidationParameterEntity validationParameterEntity;
    @JsonBackReference
    private ValidationOutputEntity validationOutputEntity;
    private Integer validationid;
    private Integer formID;
    private String validationCode;
    private String questionCode;
    private String preCalculationFormula;
    private String severity;
    private String createdBy;
    private String createdDate;
    private String lastUpdatedBy;
    private String lastUpdatedDate;
}

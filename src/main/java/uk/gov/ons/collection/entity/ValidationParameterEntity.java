package uk.gov.ons.collection.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ValidationParameterEntity {
    @JsonBackReference
    private ValidationFormEntity validationFormEntity;
    private Integer validationID;
    private String attributeValue;
    private String parameter;
    private String value;
    private String createdBy;
    private String createdDate;
    private String lastUpdatedBy;
    private String lastUpdatedDate;
}

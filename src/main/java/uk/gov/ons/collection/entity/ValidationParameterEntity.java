package uk.gov.ons.collection.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
public class ValidationParameterEntity {
    @JsonBackReference
    private ValidationFormEntity validationFormEntity;
    private Integer validationID;
    private String attributeValue;
    private String parameter;
    private String value;
    private String createdBy;
    private Timestamp createdDate;
    private String lastUpdatedBy;
    private Timestamp lastUpdatedDate;
}

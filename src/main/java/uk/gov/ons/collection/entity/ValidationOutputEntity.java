package uk.gov.ons.collection.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
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
    private Timestamp createdDate;
    private String lastUpdatedBy;
    private Timestamp lastUpdatedDate;
}

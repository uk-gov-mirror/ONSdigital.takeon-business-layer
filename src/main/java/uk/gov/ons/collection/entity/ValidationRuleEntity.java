package uk.gov.ons.collection.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@ToString
@Builder
public class ValidationRuleEntity {
    @JsonBackReference
    private ValidationFormEntity validationFormEntity;
    private String validationRule;
    private String name;
    private String description;
    private String createdBy;
    private String createdDate;
    private String lastUpdatedBy;
    private String lastUpdatedDate;
}

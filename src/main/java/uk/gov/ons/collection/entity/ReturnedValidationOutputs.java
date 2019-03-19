package uk.gov.ons.collection.entity;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class ReturnedValidationOutputs {
    private String triggered;
    private String valueFormula;
    private String metaData;
}

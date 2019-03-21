package uk.gov.ons.collection.entity;

import lombok.*;
import java.util.Map;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ReturnedValidationOutputs {
    private String triggered;
    private String valueFormula;
    private Map<String, String> metaData;
    private String error;
}



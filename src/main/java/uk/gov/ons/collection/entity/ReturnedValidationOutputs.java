package uk.gov.ons.collection.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
public class ReturnedValidationOutputs {
    private String triggered;
    private String valueFormula;
    private Map<String, String> metaData;
    private String error;

    public ReturnedValidationOutputs() {}
}



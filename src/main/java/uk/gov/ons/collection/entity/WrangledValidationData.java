package uk.gov.ons.collection.entity;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
public class WrangledValidationData  {
    private String value;
    private String metaData;
}

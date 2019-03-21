package uk.gov.ons.collection.entity;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OriginalFormData {
    private String createdDate;
    private String createdBy;
    private String instance;
    private String response;
    private String questionCode;
}

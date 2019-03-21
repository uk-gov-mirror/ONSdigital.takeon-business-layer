package uk.gov.ons.collection.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Id;
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FormDefinitionEntity {
    public Integer formId;
    public String questionCode;
    public String displayQuestionNumber;
    public String displayText;
    public String type;
    public String createdBy;
    public String createdDate;
    public String lastUpdatedBy;
    public String lastUpdatedDate;
}

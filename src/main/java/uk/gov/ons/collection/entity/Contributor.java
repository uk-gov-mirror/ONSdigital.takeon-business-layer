package uk.gov.ons.collection.entity;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Contributor {
    private String reference;
    private String period;
    private String survey;
    private int formid;
    private String status;
    private String receiptDate;
    private String lockedBy;
    private String lockedDate;
    private IdbrSelection idbrSelection;
    private String createdBy;
    private String createdDate;
    private String lastUpdatedBy;
    private String lastUpdatedDate;
}
package uk.gov.ons.collection.entity;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Contributor {

    public String reference;
    public String period;
    public String survey;
    public int formid;
    public String status;
    public String receiptDate;
    public String lockedBy;
    public String lockedDate;
    public IdbrSelection idbrSelection;
    public String createdBy;
    public String createdDate;
    public String lastUpdatedBy;
    public String lastUpdatedDate;

    @Override
    public String toString() {
        return String.format("Contributor{reference='%s', period='%s', survey='%s', status='%s'}", reference, period, survey, status);
    }

}
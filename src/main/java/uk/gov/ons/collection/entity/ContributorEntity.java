package uk.gov.ons.collection.entity;

import lombok.*;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ContributorEntity {
    public Integer formid;
    public String reference;
    public String period;
    public String survey;
    public String status;
    public String receiptDate;
    public String lockedBy;
    public String lockedDate;
    public String createdBy;
    public String createdDate;
    public String lastUpdatedBy;
    public String lastUpdatedDate;
    public String formType;
    public String checkletter;
    public String frozenSicOutdated;
    public String ruSicOutdated;
    public String frozenSic;
    public String ruSic;
    public Integer frozenEmployees;
    public Integer employees;
    public Integer frozenEmployment;
    public Integer employment;
    public BigDecimal frozenFteEmployment;
    public BigDecimal fteEmployment;
    public Integer frozenTurnover;
    public Integer turnover;
    public String enterpriseReference;
    public String wowEnterpriseReference;
    public String cellNumber;
    public String currency;
    public String vatReference;
    public String payeReference;
    public String companyRegistrationNumber;
    public Integer numberLiveLocalUnits;
    public Integer numberLiveVat;
    public Integer numberLivepaye;
    public String legalStatus;
    public String reportingUnitMarker;
    public String region;
    public String birthDate;
    public String enterpriseName;
    public String name;
    public String address;
    public String postcode;
    public String tradingStyle;
    public String contact;
    public String telephone;
    public String fax;
    public String selectionType;
    public String inclusionExclusion;
}

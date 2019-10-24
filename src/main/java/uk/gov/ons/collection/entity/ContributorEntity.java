package uk.gov.ons.collection.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ContributorEntity {
    private Integer formid;
    private String reference;
    private String period;
    private String survey;
    private String status;
    private String receiptDate;
    private String lockedBy;
    private String lockedDate;
    private String createdBy;
    private String createdDate;
    private String lastUpdatedBy;
    private String lastUpdatedDate;
    private String formType;
    private String checkletter;
    private String frozenSicOutdated;
    private String ruSicOutdated;
    private String frozenSic;
    private String ruSic;
    private Integer frozenEmployees;
    private Integer employees;
    private Integer frozenEmployment;
    private Integer employment;
    private BigDecimal frozenFteEmployment;
    private BigDecimal fteEmployment;
    private Integer frozenTurnover;
    private Integer turnover;
    private String enterpriseReference;
    private String wowEnterpriseReference;
    private String cellNumber;
    private String currency;
    private String vatReference;
    private String payeReference;
    private String companyRegistrationNumber;
    private Integer numberLiveLocalUnits;
    private Integer numberLiveVat;
    private Integer numberLivepaye;
    private String legalStatus;
    private String reportingUnitMarker;
    private String region;
    private String birthDate;
    private String enterpriseName;
    private String name;
    private String address;
    private String postcode;
    private String tradingStyle;
    private String contact;
    private String telephone;
    private String fax;
    private String selectionType;
    private String inclusionExclusion;
}

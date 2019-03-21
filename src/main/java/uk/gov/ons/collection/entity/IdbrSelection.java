package uk.gov.ons.collection.entity;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IdbrSelection {
    public String formType;
    public String checkletter;
    public String frozenSicOutdated;
    public String ruSicOutdated;
    public String frozenSic;
    public String ruSic;
    public int frozenEmployees;
    public int employees;
    public int frozenEmployment;
    public int employment;
    public BigDecimal frozenFteEmployment;
    public BigDecimal fteEmployment;
    public int frozenTurnover;
    public int turnover;
    public String enterpriseReference;
    public String wowEnterpriseReference;
    public String cellNumber;
    public String currency;
    public String vatReference;
    public String payeReference;
    public String companyRegistrationNumber;
    public int numberLiveLocalUnits;
    public int numberLiveVat;
    public int numberLivePaye;
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
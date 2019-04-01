package uk.gov.ons.collection.entity;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class IdbrSelection {
    private String formType;
    private String checkletter;
    private String frozenSicOutdated;
    private String ruSicOutdated;
    private String frozenSic;
    private String ruSic;
    private int frozenEmployees;
    private int employees;
    private int frozenEmployment;
    private int employment;
    private BigDecimal frozenFteEmployment;
    private BigDecimal fteEmployment;
    private int frozenTurnover;
    private int turnover;
    private String enterpriseReference;
    private String wowEnterpriseReference;
    private String cellNumber;
    private String currency;
    private String vatReference;
    private String payeReference;
    private String companyRegistrationNumber;
    private int numberLiveLocalUnits;
    private int numberLiveVat;
    private int numberLivePaye;
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
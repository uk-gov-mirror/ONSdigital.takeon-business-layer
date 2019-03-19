package uk.gov.ons.collection.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@ToString
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

    public ContributorEntity(Integer formId, String reference, String period, String survey, String status, String receiptDate, String lockedBy, String lockedDate, String createdBy, String createdDate, String lastUpdatedBy, String lastUpdatedDate, String formType, String checkletter, String frozenSicOutdated, String ruSicOutdated, String frozenSic, String ruSic, Integer frozenEmployees, Integer employees, Integer frozenEmployment, Integer employment, BigDecimal frozenFteEmployment, BigDecimal fteEmployment, Integer frozenTurnover, Integer turnover, String enterpriseReference, String wowEnterpriseReference, String cellNumber, String currency, String vatReference, String payeReference, String companyRegistrationNumber, Integer numberLiveLocalUnits, Integer numberLiveVat, Integer numberLivepaye, String legalStatus, String reportingUnitMarker, String region, String birthDate, String enterpriseName, String name, String address, String postcode, String tradingStyle, String contact, String telephone, String fax, String selectionType, String inclusionExclusion) {
        this.formid = formId;
        this.reference = reference;
        this.period = period;
        this.survey = survey;
        this.status = status;
        this.receiptDate = receiptDate;
        this.lockedBy = lockedBy;
        this.lockedDate = lockedDate;
        this.createdBy = createdBy;
        this.createdDate = createdDate;
        this.lastUpdatedBy = lastUpdatedBy;
        this.lastUpdatedDate = lastUpdatedDate;
        this.formType = formType;
        this.checkletter = checkletter;
        this.frozenSicOutdated = frozenSicOutdated;
        this.ruSicOutdated = ruSicOutdated;
        this.frozenSic = frozenSic;
        this.ruSic = ruSic;
        this.frozenEmployees = frozenEmployees;
        this.employees = employees;
        this.frozenEmployment = frozenEmployment;
        this.employment = employment;
        this.frozenFteEmployment = frozenFteEmployment;
        this.fteEmployment = fteEmployment;
        this.frozenTurnover = frozenTurnover;
        this.turnover = turnover;
        this.enterpriseReference = enterpriseReference;
        this.wowEnterpriseReference = wowEnterpriseReference;
        this.cellNumber = cellNumber;
        this.currency = currency;
        this.vatReference = vatReference;
        this.payeReference = payeReference;
        this.companyRegistrationNumber = companyRegistrationNumber;
        this.numberLiveLocalUnits = numberLiveLocalUnits;
        this.numberLiveVat = numberLiveVat;
        this.numberLivepaye = numberLivepaye;
        this.legalStatus = legalStatus;
        this.reportingUnitMarker = reportingUnitMarker;
        this.region = region;
        this.birthDate = birthDate;
        this.enterpriseName = enterpriseName;
        this.name = name;
        this.address = address;
        this.postcode = postcode;
        this.tradingStyle = tradingStyle;
        this.contact = contact;
        this.telephone = telephone;
        this.fax = fax;
        this.selectionType = selectionType;
        this.inclusionExclusion = inclusionExclusion;
    }

    public ContributorEntity(){}

    // Getters & Setters
/*
    public String getReference() {
        return reference;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSurvey() {
        return survey;
    }

    public void setSurvey(String survey) {
        this.survey = survey;
    }

    public Integer getFormId() {
        return formId;
    }

    public void setFormid(Integer formId) {
        this.formId = formId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        this.receiptDate = receiptDate;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public void setLockedBy(String lockedBy) {
        this.lockedBy = lockedBy;
    }

    public String getLockedDate() {
        return lockedDate;
    }

    public void setLockedDate(String lockedDate) {
        this.lockedDate = lockedDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(String lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public String getCheckletter() {
        return checkletter;
    }

    public void setCheckletter(String checkletter) {
        this.checkletter = checkletter;
    }

    public String getFrozenSicOutdated() {
        return frozenSicOutdated;
    }

    public void setFrozenSicOutdated(String frozenSicOutdated) {
        this.frozenSicOutdated = frozenSicOutdated;
    }

    public String getRuSicOutdated() {
        return ruSicOutdated;
    }

    public void setRuSicOutdated(String ruSicOutdated) {
        this.ruSicOutdated = ruSicOutdated;
    }

    public String getFrozenSic() {
        return frozenSic;
    }

    public void setFrozenSic(String frozenSic) {
        this.frozenSic = frozenSic;
    }


    public String getRuSic() {
        return ruSic;
    }

    public void setRuSic(String ruSic) {
        this.ruSic = ruSic;
    }

    public Integer getFrozenEmployees() {
        return frozenEmployees;
    }
    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setFrozenEmployees(Integer frozenEmployees) {
        this.frozenEmployees = frozenEmployees;
    }

    public Integer getEmployees() {
        return employees;
    }


    public void setEmployees(Integer employees) {
        this.employees = employees;
    }

    public void setFormId(int formid) {
        formId = formid;

    }

    public Integer getFrozenEmployment() {
        return frozenEmployment;
    }

    public void setFrozenEmployment(Integer frozenEmployment) {
        this.frozenEmployment = frozenEmployment;
    }

    public Integer getEmployment() {
        return employment;
    }

    public void setEmployment(Integer employment) {
        this.employment = employment;
    }

    public BigDecimal getFrozenFteEmployment() {
        return frozenFteEmployment;
    }

    public void setFrozenFteEmployment(BigDecimal frozenFteEmployment) {
        this.frozenFteEmployment = frozenFteEmployment;
    }

    public BigDecimal getFteEmployment() {
        return fteEmployment;
    }

    public void setFteEmployment(BigDecimal fteEmployment) {
        this.fteEmployment = fteEmployment;
    }

    public Integer getFrozenTurnover() {
        return frozenTurnover;
    }

    public void setFrozenTurnover(Integer frozenTurnover) {
        this.frozenTurnover = frozenTurnover;
    }

    public Integer getTurnover() {
        return turnover;
    }

    public void setTurnover(Integer turnover) {
        this.turnover = turnover;
    }

    public String getEnterpriseReference() {
        return enterpriseReference;
    }

    public void setEnterpriseReference(String enterpriseReference) {
        this.enterpriseReference = enterpriseReference;
    }

    public String getWowEnterpriseReference() {
        return wowEnterpriseReference;
    }

    public void setWowEnterpriseReference(String wowEnterpriseReference) {
        this.wowEnterpriseReference = wowEnterpriseReference;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getVatReference() {
        return vatReference;
    }

    public void setVatReference(String vatReference) {
        this.vatReference = vatReference;
    }

    public String getPayeReference() {
        return payeReference;
    }

    public void setPayeReference(String payeReference) {
        this.payeReference = payeReference;
    }

    public String getCompanyRegistrationNumber() {
        return companyRegistrationNumber;
    }

    public void setCompanyRegistrationNumber(String companyRegistrationNumber) {
        this.companyRegistrationNumber = companyRegistrationNumber;
    }

    public Integer getNumberLiveLocalUnits() {
        return numberLiveLocalUnits;
    }

    public void setNumberLiveLocalUnits(Integer numberLiveLocalUnits) {
        this.numberLiveLocalUnits = numberLiveLocalUnits;
    }

    public Integer getNumberLiveVat() {
        return numberLiveVat;
    }

    public void setNumberLiveVat(Integer numberLiveVat) {
        this.numberLiveVat = numberLiveVat;
    }

    public Integer getNumberLivepaye() {
        return numberLivepaye;
    }

    public void setNumberLivepaye(Integer numberLivepaye) {
        this.numberLivepaye = numberLivepaye;
    }

    public String getLegalStatus() {
        return legalStatus;
    }

    public void setLegalStatus(String legalStatus) {
        this.legalStatus = legalStatus;
    }

    public String getReportingUnitMarker() {
        return reportingUnitMarker;
    }

    public void setReportingUnitMarker(String reportingUnitMarker) {
        this.reportingUnitMarker = reportingUnitMarker;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public void setEnterpriseName(String enterpriseName) {
        this.enterpriseName = enterpriseName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getTradingStyle() {
        return tradingStyle;
    }

    public void setTradingStyle(String tradingStyle) {
        this.tradingStyle = tradingStyle;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getSelectionType() {
        return selectionType;
    }

    public void setSelectionType(String selectionType) {
        this.selectionType = selectionType;
    }

    public String getInclusionExclusion() {
        return inclusionExclusion;
    }

    public void setInclusionExclusion(String inclusionExclusion) {
        this.inclusionExclusion = inclusionExclusion;
    }
    */
}

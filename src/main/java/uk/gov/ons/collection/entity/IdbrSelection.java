package uk.gov.ons.collection.entity;

import java.math.BigDecimal;

public class IdbrSelection {

    private final String formType;
    private final String checkletter;
    private final String frozenSicOutdated;
    private final String ruSicOutdated;
    private final String frozenSic;
    private final String ruSic;
    private final int frozenEmployees;
    private final int employees;
    private final int frozenEmployment;
    private final int employment;
    private final BigDecimal frozenFteEmployment;
    private final BigDecimal fteEmployment;
    private final int frozenTurnover;
    private final int turnover;
    private final String enterpriseReference;
    private final String wowEnterpriseReference;
    private final String cellNumber;
    private final String currency;
    private final String vatReference;
    private final String payeReference;
    private final String companyRegistrationNumber;
    private final int numberLiveLocalUnits;
    private final int numberLiveVat;
    private final int numberLivePaye;
    private final String legalStatus;
    private final String reportingUnitMarker;
    private final String region;
    private final String birthDate;
    private final String enterpriseName;
    private final String name;
    private final String address;
    private final String postcode;
    private final String tradingStyle;
    private final String contact;
    private final String telephone;
    private final String fax;
    private final String selectionType;
    private final String inclusionExclusion;

    public String getFormType() {
        return formType;
    }

    public String getCheckletter() {
        return checkletter;
    }

    public String getFrozenSicOutdated() {
        return frozenSicOutdated;
    }

    public String getRuSicOutdated() {
        return ruSicOutdated;
    }

    public String getFrozenSic() {
        return frozenSic;
    }

    public String getRuSic() {
        return ruSic;
    }

    public int getFrozenEmployees() {
        return frozenEmployees;
    }

    public int getEmployees() {
        return employees;
    }

    public int getFrozenEmployment() {
        return frozenEmployment;
    }

    public int getEmployment() {
        return employment;
    }

    public BigDecimal getFrozenFteEmployment() {
        return frozenFteEmployment;
    }

    public BigDecimal getFteEmployment() {
        return fteEmployment;
    }

    public int getFrozenTurnover() {
        return frozenTurnover;
    }

    public int getTurnover() {
        return turnover;
    }

    public String getEnterpriseReference() {
        return enterpriseReference;
    }

    public String getWowEnterpriseReference() {
        return wowEnterpriseReference;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public String getCurrency() {
        return currency;
    }

    public String getVatReference() {
        return vatReference;
    }

    public String getPayeReference() {
        return payeReference;
    }

    public String getCompanyRegistrationNumber() {
        return companyRegistrationNumber;
    }

    public int getNumberLiveLocalUnits() {
        return numberLiveLocalUnits;
    }

    public int getNumberLiveVat() {
        return numberLiveVat;
    }

    public int getNumberLivePaye() {
        return numberLivePaye;
    }

    public String getLegalStatus() {
        return legalStatus;
    }

    public String getReportingUnitMarker() {
        return reportingUnitMarker;
    }

    public String getRegion() {
        return region;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getEnterpriseName() {
        return enterpriseName;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getTradingStyle() {
        return tradingStyle;
    }

    public String getContact() {
        return contact;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getFax() {
        return fax;
    }

    public String getSelectionType() {
        return selectionType;
    }

    public String getInclusionExclusion() {
        return inclusionExclusion;
    }

    public static class Builder {

        // Optional parameters
        private String formType = "";
        private String checkletter = "";
        private String frozenSicOutdated = "";
        private String ruSicOutdated = "";
        private String frozenSic = "";
        private String ruSic = "";
        private int frozenEmployees = 0;
        private int employees = 0;
        private int frozenEmployment = 0;
        private int employment = 0;
        private BigDecimal frozenFteEmployment = BigDecimal.valueOf(0);
        private BigDecimal fteEmployment = BigDecimal.valueOf(0);
        private int frozenTurnover = 0;
        private int turnover = 0;
        private String enterpriseReference = "";
        private String wowEnterpriseReference = "";
        private String cellNumber = "";
        private String currency = "";
        private String vatReference = "";
        private String payeReference = "";
        private String companyRegistrationNumber = "";
        private int numberLiveLocalUnits = 0;
        private int numberLiveVat = 0;
        private int numberLivePaye = 0;
        private String legalStatus = "";
        private String reportingUnitMarker = "";
        private String region = "";
        private String birthDate = "";
        private String enterpriseName = "";
        private String name = "";
        private String address = "";
        private String postcode = "";
        private String tradingStyle = "";
        private String contact = "";
        private String telephone = "";
        private String fax = "";
        private String selectionType = "";
        private String inclusionExclusion = "";

        public Builder() {}

        public Builder formType(String formType) {
            this.formType = ( formType == null) ? "" : formType;
            return this;
        }

        public Builder checkletter(String checkletter) {
            this.checkletter = ( checkletter == null) ? "" : checkletter;
            return this;
        }

        public Builder frozenSicOutdated(String frozenSicOutdated) {
            this.frozenSicOutdated = ( frozenSicOutdated == null) ? "" : frozenSicOutdated;
            return this;
        }

        public Builder ruSicOutdated(String ruSicOutdated) {
            this.ruSicOutdated = ( ruSicOutdated == null) ? "" : ruSicOutdated;
            return this;
        }

        public Builder frozenSic(String frozenSic) {
            this.frozenSic = ( frozenSic == null) ? "" : frozenSic;
            return this;
        }

        public Builder ruSic(String ruSic) {
            this.ruSic = ( ruSic == null) ? "" : ruSic;
            return this;
        }

        public Builder frozenEmployees(int frozenEmployees) {
            this.frozenEmployees = frozenEmployees;
            return this;
        }

        public Builder employees(int employees) {
            this.employees = employees;
            return this;
        }

        public Builder frozenEmployment(int frozenEmployment) {
            this.frozenEmployment = frozenEmployment;
            return this;
        }

        public Builder employment(int employment) {
            this.employment = employment;
            return this;
        }

        public Builder frozenFteEmployment(BigDecimal frozenFteEmployment) {
            this.frozenFteEmployment = frozenFteEmployment;
            return this;
        }

        public Builder fteEmployment(BigDecimal fteEmployment) {
            this.fteEmployment = fteEmployment;
            return this;
        }

        public Builder frozenTurnover(int frozenTurnover) {
            this.frozenTurnover = frozenTurnover;
            return this;
        }

        public Builder turnover(int turnover) {
            this.turnover = turnover;
            return this;
        }

        public Builder enterpriseReference(String enterpriseReference) {
            this.enterpriseReference = ( enterpriseReference == null) ? "" : enterpriseReference;
            return this;
        }

        public Builder wowEnterpriseReference(String wowEnterpriseReference) {
            this.wowEnterpriseReference = ( wowEnterpriseReference == null) ? "" : wowEnterpriseReference;
            return this;
        }

        public Builder cellNumber(String cellNumber) {
            this.cellNumber = ( cellNumber == null) ? "" : cellNumber;
            return this;
        }

        public Builder currency(String currency) {
            this.currency = ( currency == null) ? "" : currency;
            return this;
        }

        public Builder vatReference(String vatReference) {
            this.vatReference = ( vatReference == null) ? "" : vatReference;
            return this;
        }

        public Builder payeReference(String payeReference) {
            this.payeReference = ( payeReference == null) ? "" : payeReference;
            return this;
        }

        public Builder companyRegistrationNumber(String companyRegistrationNumber) {
            this.companyRegistrationNumber = ( companyRegistrationNumber == null) ? "" : companyRegistrationNumber;
            return this;
        }

        public Builder numberLiveLocalUnits(int numberLiveLocalUnits) {
            this.numberLiveLocalUnits = numberLiveLocalUnits;
            return this;
        }

        public Builder numberLiveVat(int numberLiveVat) {
            this.numberLiveVat = numberLiveVat;
            return this;
        }

        public Builder numberLivePaye(int numberLivePaye) {
            this.numberLivePaye = numberLivePaye;
            return this;
        }

        public Builder legalStatus(String legalStatus) {
            this.legalStatus = ( legalStatus == null) ? "" : legalStatus;
            return this;
        }

        public Builder reportingUnitMarker(String reportingUnitMarker) {
            this.reportingUnitMarker = ( reportingUnitMarker == null) ? "" : reportingUnitMarker;
            return this;
        }

        public Builder region(String region) {
            this.region = ( region == null) ? "" : region;
            return this;
        }

        public Builder birthDate(String birthDate) {
            this.birthDate = ( birthDate == null) ? "" : birthDate;
            return this;
        }

        public Builder enterpriseName(String enterpriseName) {
            this.enterpriseName = ( enterpriseName == null) ? "" : enterpriseName;
            return this;
        }

        public Builder name(String name) {
            this.name = ( name == null) ? "" : name;
            return this;
        }

        public Builder address(String address) {
            this.address = ( address == null) ? "" : address;
            return this;
        }

        public Builder postcode(String postcode) {
            this.postcode = ( postcode == null) ? "" : postcode;
            return this;
        }

        public Builder tradingStyle(String tradingStyle) {
            this.tradingStyle = ( tradingStyle == null) ? "" : tradingStyle;
            return this;
        }

        public Builder contact(String contact) {
            this.contact = ( contact == null) ? "" : contact;
            return this;
        }

        public Builder telephone(String telephone) {
            this.telephone = ( telephone == null) ? "" : telephone;
            return this;
        }

        public Builder fax(String fax) {
            this.fax = ( fax == null) ? "" : fax;
            return this;
        }

        public Builder selectionType(String selectionType) {
            this.selectionType = ( selectionType == null) ? "" : selectionType;
            return this;
        }

        public Builder inclusionExclusion(String inclusionExclusion) {
            this.inclusionExclusion = ( inclusionExclusion == null) ? "" : inclusionExclusion;
            return this;
        }

        public IdbrSelection build() {
            return new IdbrSelection(this);
        }

    }

    private IdbrSelection (Builder builder) {
        this.formType = builder.formType;
        this.checkletter = builder.checkletter;
        this.frozenSicOutdated = builder.frozenSicOutdated;
        this.ruSicOutdated = builder.ruSicOutdated;
        this.frozenSic = builder.frozenSic;
        this.ruSic = builder.ruSic;
        this.frozenEmployees = builder.frozenEmployees;
        this.employees = builder.employees;
        this.frozenEmployment = builder.frozenEmployment;
        this.employment = builder.employment;
        this.frozenFteEmployment = builder.frozenFteEmployment;
        this.fteEmployment = builder.fteEmployment;
        this.frozenTurnover = builder.frozenTurnover;
        this.turnover = builder.turnover;
        this.enterpriseReference = builder.enterpriseReference;
        this.wowEnterpriseReference = builder.wowEnterpriseReference;
        this.cellNumber = builder.cellNumber;
        this.currency = builder.currency;
        this.vatReference = builder.vatReference;
        this.payeReference = builder.payeReference;
        this.companyRegistrationNumber = builder.companyRegistrationNumber;
        this.numberLiveLocalUnits = builder.numberLiveLocalUnits;
        this.numberLiveVat = builder.numberLiveVat;
        this.numberLivePaye = builder.numberLivePaye;
        this.legalStatus = builder.legalStatus;
        this.reportingUnitMarker = builder.reportingUnitMarker;
        this.region = builder.region;
        this.birthDate = builder.birthDate;
        this.enterpriseName = builder.enterpriseName;
        this.name = builder.name;
        this.address = builder.address;
        this.postcode = builder.postcode;
        this.tradingStyle = builder.tradingStyle;
        this.contact = builder.contact;
        this.telephone = builder.telephone;
        this.fax = builder.fax;
        this.selectionType = builder.selectionType;
        this.inclusionExclusion = builder.inclusionExclusion;
    }
}
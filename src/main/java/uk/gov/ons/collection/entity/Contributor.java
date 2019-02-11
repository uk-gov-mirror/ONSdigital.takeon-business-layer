package uk.gov.ons.collection.entity;

public class Contributor {

    private final String reference;
    private final String period;
    private final String survey;
    private final int formid;
    private final String status;
    private final String receiptDate;
    private final String lockedBy;
    private final String lockedDate;
    private final IdbrSelection idbrSelection;
    private final String createdBy;
    private final String createdDate;
    private final String lastUpdatedBy;
    private final String lastUpdatedDate;

    public String getReference() {
        return reference;
    }

    public String getPeriod() {
        return period;
    }

    public String getSurvey() {
        return survey;
    }

    public int getFormId() {
        return formid;
    }

    public String getStatus() {
        return status;
    }

    public String getReceiptDate() {
        return receiptDate;
    }

    public String getLockedBy() {
        return lockedBy;
    }

    public String getLockedDate() {
        return lockedDate;
    }

    public IdbrSelection getIdbrSelection() {
        return idbrSelection;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public String getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    @Override
    public String toString() {
        return String.format("Contributor{reference='%s', period='%s', survey='%s', status='%s'}", reference, period, survey, status);
    }

    public static class Builder {

        // Required parameters
        private final String reference;
        private final String period;
        private final String survey;

        // Optional parameters
        private int formid = 0;
        private String status = "";
        private String receiptDate = "";
        private String lockedBy = "";
        private String lockedDate = "";
        private String formType = "";
        private IdbrSelection idbrSelection;
        private String createdBy = "";
        private String createdDate = "";
        private String lastUpdatedBy = "";
        private String lastUpdatedDate = "";

        public Builder(String reference, String period, String survey) {
            this.reference = reference;
            this.period = period;
            this.survey = survey;
        }

        public Builder formid(int formid) {
            this.formid = formid;
            return this;
        }

        public Builder status(String status) {
            this.status = (status == null) ? "" : status;
            return this;
        }

        public Builder receiptDate(String receiptDate) {
            this.receiptDate = (receiptDate == null) ? "" : receiptDate;
            return this;
        }

        public Builder IdbrSelection(IdbrSelection idbrSelection) {
            this.idbrSelection = idbrSelection;
            return this;
        }

        public Builder lockedBy(String lockedBy) {
            this.lockedBy = ( lockedBy == null) ? "" : lockedBy;
            return this;
        }

        public Builder lockedDate(String lockedDate) {
            this.lockedDate =  ( lockedDate == null) ? "" : lockedDate;
            return this;
        }

        public Builder createdBy(String createdBy) {
            this.createdBy = ( createdBy == null) ? "" : createdBy;
            return this;
        }

        public Builder createdDate(String createdDate) {
            this.createdDate = ( createdDate == null) ? "" : createdDate;
            return this;
        }

        public Builder lastUpdatedBy(String lastUpdatedBy) {
            this.lastUpdatedBy = ( lastUpdatedBy == null) ? "" : lastUpdatedBy;
            return this;
        }

        public Builder lastUpdatedDate(String lastUpdatedDate) {
            this.lastUpdatedDate = ( lastUpdatedDate == null) ? "" : lastUpdatedDate;
            return this;
        }

        public Contributor build() {
            return new Contributor(this);
        }

    }

    private Contributor (Builder builder) {
        this.reference = builder.reference;
        this.period = builder.period;
        this.survey = builder.survey;
        this.formid = builder.formid;
        this.status = builder.status;
        this.receiptDate = builder.receiptDate;
        this.lockedBy = builder.lockedBy;
        this.lockedDate = builder.lockedDate;
        this.idbrSelection = builder.idbrSelection;
        this.createdBy = builder.createdBy;
        this.createdDate = builder.createdDate;
        this.lastUpdatedBy = builder.lastUpdatedBy;
        this.lastUpdatedDate = builder.lastUpdatedDate;
    }

}
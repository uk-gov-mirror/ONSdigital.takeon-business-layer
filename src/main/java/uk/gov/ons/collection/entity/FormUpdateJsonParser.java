package uk.gov.ons.collection.entity;

public class FormUpdateJsonParser {

    private FormUpdateData formData;
    private String sourceJson;

    public FormUpdateJsonParser(String sourceJson) {
        this.sourceJson = sourceJson;
    }

    public FormUpdateData parseFormData() {
        // Parse and return the data

        return formData;
    }
}

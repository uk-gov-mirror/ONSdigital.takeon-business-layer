package uk.gov.ons.collection.entity;

public class ValidationConfigQuery {

    private int formId;
    private final String baseQuery = 
        "query valconfig($formid: Int) {" +
        "allValidationrules {nodes {" +
            "rule baseformula " +
            "validationformsByRule(condition: {formid: $formid}) {nodes {" +
                "validationid primaryquestion defaultvalue " + 
                "validationparametersByValidationid {nodes {" +                    
                    "parameter value source periodoffset" +
                "}}}}}}}";
    private String qlQuery;

    public ValidationConfigQuery(int formId) {
        this.formId = formId;
        qlQuery = buildQuery();
    }

    private String buildQuery(){
        StringBuilder validationConfigQuery = new StringBuilder();
        validationConfigQuery.append("{\"query\": \"" + baseQuery + "\"");
        validationConfigQuery.append(",\"variables\": {\"formid\": " + Integer.toString(formId) + "}}");
        return validationConfigQuery.toString();
    }    

    public String getQlQuery() {
        return qlQuery;
    }

}
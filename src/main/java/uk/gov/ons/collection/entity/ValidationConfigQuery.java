package uk.gov.ons.collection.entity;

import uk.gov.ons.collection.service.ServiceInterface;

/**
 * This class is responsible for constructing valid graphQL JSON queries to obtain validation configuration data.
 * It provides the configuration for a single (given) form.
*/
public class ValidationConfigQuery {

    private final int formId;
    private final ServiceInterface service;
    private final String baseQuery = "query config($formid: Int) {" +
        "allValidationforms(condition: {formid: $formid}) { nodes {" +
            "id " +
            "validationid " +
            "formid " +
            "rule " +
            "primaryquestion " +
            "defaultvalue " +
            "severity " +
            "createdby " +
            "createddate " +
            "lastupdatedby " +
            "lastupdateddate " +
            "validationparametersByValidationid { nodes {" +
                "id " +
                "validationid " +
                "attributename " +
                "attributevalue " +
                "parameter " +
                "value " +
                "source " +
                "periodoffset " +
                "createdby " +
                "createddate " +
                "lastupdatedby " +
                "lastupdateddate " +
                "}}" +
            "validationruleByRule {" +
              "rule " +
              "name " +
              "baseformula " +
              "createdby " +
              "createddate " +
              "lastupdatedby " +
              "lastupdateddate " +
              "validationperiodsByRule { nodes {" +
                  "id " +
                  "rule " +
                  "periodoffset " +
                  "createdby " +
                  "createddate " +
                  "lastupdatedby " +
                  "lastupdateddate " +
                "}}" +
            "} " +
        "}}}";

    /**
     * Constructor.
     * @param   formId  Unique number that identifies the form we are obtaining the configuration for
     * @param   service This class determines graphQL query target
    */
    public ValidationConfigQuery(int formId, ServiceInterface service) {
        this.formId = formId;
        this.service = service;
    }

    /**
     * Provide the complete JSON query that will be sent to the graphQL service.
     * @return  A graphQL query JSON structure as a String
    */    
    public String getQuery() {
        return "{\"query\": \"" + baseQuery + "\",\"variables\": {\"formid\": " + Integer.toString(formId) + "}}";
    }

    /**
     * Invokes the graphQL service to obtain a string JSON response of the validation configuration data.
     * @return  A JSON data structure as a String
     * @see     ServiceInterface
    */
    public String load() {
        return service.runQuery(this.getQuery());
    }

}
package uk.gov.ons.collection.utilities;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class SelectionFileQuery {
    private HashMap<String, String> variables;
    private List<String> intVariables = new ArrayList<>(Arrays.asList("first", "last", "formid"));

    public SelectionFileQuery(Map<String, String> variables){
        this.variables = (variables == null) ? new HashMap<>() : new HashMap<>(variables);
    }

    public String buildVariables() {
        StringJoiner joiner = new StringJoiner(",");
        variables.forEach((key,value) -> {
            if (intVariables.contains(key)) {
                joiner.add("\"" + key + "\": " + value);
            } else {
                joiner.add("\"" + key + "\": \"" + value + "\"");
            }
        });
        return joiner.toString();
    }
    
    public String buildCheckIDBRFormidQuery() {
        StringBuilder checkIDBRFormidQuery = new StringBuilder();
        checkIDBRFormidQuery.append("{\"query\": \"query checkidbrformid($formtype: String) " +
                "{ checkidbrformid (condition: {formtype: $formtype}) {" +
                "nodes { formid survey formtype periodstart periodend }}}}}\"," +
                "\"variables\": {"); 
        checkIDBRFormidQuery.append(buildVariables());
        checkIDBRFormidQuery.append("}}");
        return checkIDBRFormidQuery.toString();
    }
}
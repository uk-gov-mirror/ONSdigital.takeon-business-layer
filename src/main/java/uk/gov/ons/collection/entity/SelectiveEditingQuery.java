package uk.gov.ons.collection.entity;

import lombok.extern.log4j.Log4j2;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

@Log4j2
public class SelectiveEditingQuery {

    private HashMap<String, String> variables;

    public SelectiveEditingQuery(Map<String, String> variables) {
        this.variables = (variables == null) ? new HashMap<>() : new HashMap<>(variables);
    }

    public String buildViewFormQuery() {

        StringBuilder viewFormQuery = new StringBuilder();
        String s = "";
        viewFormQuery.append("{\"query\": \"query loadselectiveeditingconfig($period: String, $reference: String, $survey: String) {" +
                "  allContributors(condition: {reference: $reference, period: $period, survey: $survey}) { " +
                "    nodes { " +
                "      reference " +
                "      period " +
                "      survey " +
                "      resultscellnumber " +
                "      domain " +
                "    } " +
                "  } " +
                "  allSelectiveeditingconfigs(condition: {period: $period, survey: $survey}) { " +
                "    nodes { " +
                "      survey " +
                "      period " +
                "      domain " +
                "      questioncode " +
                "      threshold " +
                "      estimate " +
                "    } " +
                "  } " +
                "  allCelldetails(condition: {period: $period, survey: $survey}) { " +
                "    nodes { " +
                "      survey " +
                "      period " +
                "      cellnumber " +
                "      designweight " +
                "    } " +
                "  } " +
                "} \"," +
                "\"variables\": {");

        viewFormQuery.append(buildVariables());
        viewFormQuery.append("}}");
        log.info("Selective Editing Query: "+viewFormQuery.toString());
        return viewFormQuery.toString();
    }

    public String buildVariables() {
        StringJoiner joiner = new StringJoiner(",");
        variables.forEach((key,value) -> {
            joiner.add("\"" + key + "\": \"" + value + "\"");
        });
        return joiner.toString();
    }
}

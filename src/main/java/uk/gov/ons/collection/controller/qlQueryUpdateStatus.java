package uk.gov.ons.collection.controller;

import org.json.JSONArray;
import org.json.JSONObject;

public class qlQueryUpdateStatus{

    private JSONObject jsonResponse;

    public String buildUpdateStatusRead(){
        StringBuilder query = new StringBuilder();
        query.append("{\"query\" : \"query contributor {allContributors{nodes{validationoutputsByReferenceAndPeriodAndSurvey(condition: {triggered:\"true\"}){nodes{reference,period,survey,triggered}}}}}\"}");
        return query.toString();
    }

    public String updateStatusResponse(){
        String response = "{\n" +
                "  \"data\": {\n" +
                "    \"allContributors\": {\n" +
                "      \"nodes\": [\n" +
                "        {\n" +
                "          \"validationoutputsByReferenceAndPeriodAndSurvey\": {\n" +
                "            \"nodes\": [\n" +
                "              {\n" +
                "                \"reference\": \"4990012\",\n" +
                "                \"period\": \"201211\",\n" +
                "                \"survey\": \"066   \",\n" +
                "                \"triggered\": \"true\"\n" +
                "              }]}}]}}}";
        jsonResponse = new JSONObject(response);
        JSONArray nodes = jsonResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes");
        String reference;
        String period;
        String survey;
        for (int i = 0; i < nodes.length(); i++){
            if(nodes.getJSONObject(i).getJSONArray("nodes") != null){
                JSONArray data = nodes.getJSONObject(i).getJSONArray("nodes");
                reference = data.getJSONObject(0).toString();
                period = data.getJSONObject(1).toString();
                survey = data.getJSONObject(2).toString();

            }
        }
        return response;
    }
}
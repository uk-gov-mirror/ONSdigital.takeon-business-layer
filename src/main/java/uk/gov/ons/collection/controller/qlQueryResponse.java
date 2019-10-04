package uk.gov.ons.collection.controller;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class qlQueryResponse {

    private JSONObject jsonQlResponse;

    // If we receive invalid json we consume the error and instead instantiate the current response object with valid but empty Json
    public qlQueryResponse(String jsonString) {
        try {
            jsonQlResponse = new JSONObject(jsonString);
        }
        catch(Exception e) {
            jsonString = "{}";
            jsonQlResponse = new JSONObject(jsonString);
        }
    }

    public String toString() {
        return jsonQlResponse.toString();
    }


    // Conversion from the QL response JSON structure to remove some nested attributes
    public String parse(){                      
        JSONObject parsedJsonQlResponse = new JSONObject();
        try {       
            JSONObject pageInfo = new JSONObject();     
            pageInfo.put("pageInfo", jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONObject("pageInfo"));
            pageInfo.getJSONObject("pageInfo").put("totalCount", jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getInt("totalCount"));
            parsedJsonQlResponse.put("data", jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes"));
            parsedJsonQlResponse.put("pageInfo", pageInfo.getJSONObject("pageInfo"));
        }
        catch(JSONException e){
            return "{\"error\":\"Invalid response from graphQL\"}";
        }
        return parsedJsonQlResponse.toString().replaceAll(":", ": ");
    }


    public ArrayList<Integer> parseForPeriodOffset(){                      
        ArrayList<Integer> uniqueOffsets = new ArrayList<>();
        JSONArray offsets = jsonQlResponse.getJSONObject("data").getJSONObject("allValidationperiods").getJSONArray("nodes");
        for (int i=0; i < offsets.length(); i++) {    
            Integer offset = Integer.valueOf(offsets.getJSONObject(i).getInt("periodoffset"));            
            if (!uniqueOffsets.contains(offset)) {
                uniqueOffsets.add(offset);
            }
        }
        return uniqueOffsets;
    }

    public int getFormID() {
        // {"data":{"allContributors":{"nodes":[
            //{"reference":"12345678001","formid":1,"period":"201801","surveyBySurvey":{"periodicity":"Monthly"},"survey":"999A"}]}}}
        return jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").getJSONObject(0).getInt("formid");
    }

    public String getPeriodicity() {
        return jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").getJSONObject(0).getJSONObject("surveyBySurvey").getString("periodicity");     
    }

    public JSONArray getResponses() {
        var outputArray = new JSONArray();
        if (jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").length() > 0) {
            outputArray = jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").getJSONObject(0)
                                        .getJSONObject("responsesByReferenceAndPeriodAndSurvey").getJSONArray("nodes");
        }
        return outputArray;        
    }

    // "responsesByReferenceAndPeriodAndSurvey":{"nodes":[{"reference":"12345678003","period":"201801","instance":0,"response":"",
    // "questioncode":"1001","survey":"999A"},{"reference":"12345678003","period":"201801","instance":0,"response":"0","questioncode":"4001","survey":"999A"}]}

    public JSONObject getContributors() {
        JSONObject output = new JSONObject();
        if (jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").length() > 0) {
            output = jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").getJSONObject(0);
            output.remove("surveyBySurvey");
            output.remove("formByFormid");
            output.remove("responsesByReferenceAndPeriodAndSurvey");
        }
        return output;              
    }
    //"{\"data\":{\"allContributors\":{\"nodes\":[{\"formid\":1,\"birthdate\":\"\",\"selectiontype\":\" \",\"createddate\":\"2019-10-04T09:17:36.061812+00:00\",\"responsesByReferenceAndPeriodAndSurvey\":{\"nodes\":[{\"reference\":\"12345678003\",\"period\":\"201801\",\"instance\":0,\"response\":\"\",\"questioncode\":\"1001\",\"survey\":\"999A\"},{\"reference\":\"12345678003\",\"period\":\"201801\",\"instance\":0,\"response\":\"0\",\"questioncode\":\"4001\",\"survey\":\"999A\"}]},\"surveyBySurvey\":{\"periodicity\":\"Monthly\"},\"checkletter\":\" \",\"rusicoutdated\":\"     \",\"tradingstyle\":\"\",\"frozenemployees\":\"0\",\"companyregistrationnumber\":\"\",\"reference\":\"12345678003\",\"reportingunitmarker\":\" \",\"inclusionexclusion\":\" \",\"legalstatus\":\" \",\"createdby\":\"fisdba\",\"lastupdateddate\":null,\"rusic\":\"     \",\"contact\":\"\",\"lastupdatedby\":null," +
    //"\"formByFormid\":{\"survey\":\"999A\",\"formdefinitionsByFormid\":{\"nodes\":[{\"questioncode\":\"1000\",\"type\":\"NUMERIC\"},{\"questioncode\":\"1001\",\"type\":\"NUMERIC\"},{\"questioncode\":\"2000\",\"type\":\"TICKBOX-Yes\"},{\"questioncode\":\"3000\",\"type\":\"Text\"},{\"questioncode\":\"4000\",\"type\":\"NUMERIC\"},{\"questioncode\":\"4001\",\"type\":\"NUMERIC\"}]}},\"frozenturnover\":\"0\",\"currency\":\"S\",\"receiptdate\":\"2019-10-04T09:17:36.061812+00:00\",\"frozensicoutdated\":\"     \",\"fax\":\"\",\"frozenemployment\":\"0\",\"turnover\":\"0\",\"payereference\":\"\",\"period\":\"201801\",\"wowenterprisereference\":\"\",\"numberlivevat\":\"0\",\"telephone\":\"\",\"employment\":\"0\",\"numberlivepaye\":\"0\",\"vatreference\":\"\",\"lockedby\":null,\"frozenfteemployment\":\"0.000\",\"cellnumber\":0,\"fteemployment\":\"0.000\",\"lockeddate\":null,\"survey\":\"999A\",\"enterprisereference\":\"          \",\"numberlivelocalunits\":\"0\",\"employees\":\"0\",\"region\":\"  \",\"frozensic\":\"     \",\"status\":\"Status\"}]}}}"
    public JSONArray getForm(String survey, String period) {
        var outputArray = new JSONArray();
        if (jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").length() > 0) {
            outputArray = jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").getJSONObject(0)
                                        .getJSONObject("formByFormid").getJSONObject("formdefinitionsByFormid").getJSONArray("nodes");
        }
        for (int i = 0; i < outputArray.length(); i++) {
            outputArray.getJSONObject(i).put("survey",survey);
            outputArray.getJSONObject(i).put("period",period);
        }
        return outputArray;
    }

//{"data":{"allValidationrules":{"nodes":[{"baseformula":"abs(question - comparison_question) > threshold AND question > 0 AND comparison_question > 0","rule":"POPM",
//"validationformsByRule":{"nodes":[{"primaryquestion":"1000","validationid":20,"defaultvalue":"0",
//"validationparametersByValidationid":{"nodes":[{"periodoffset":1,"parameter":"comparison_question","source":"response","value":"1000"},
        //{"periodoffset":0,"parameter":"question","source":"response","value":"1000"},{"periodoffset":0,"parameter":"threshold","source":"","value":"20000"}]}},
        //{"primaryquestion":"1001","validationid":21,"defaultvalue":"0",
        //"validationparametersByValidationid":{"nodes":[{"periodoffset":1,"parameter":"comparison_question","source":"response","value":"1001"},{"periodoffset":0,"parameter":"question","source":"response","value":"1001"},{"periodoffset":0,"parameter":"threshold","source":"","value":"0"}]}}]}},{"baseformula":"question != comparison_question AND ( question = 0 OR comparison_question = 0 ) AND abs(question - comparison_question) > Threshold","rule":"POPZC","validationformsByRule":{"nodes":[{"primaryquestion":"1000","validationid":40,"defaultvalue":"0","validationparametersByValidationid":{"nodes":[{"periodoffset":1,"parameter":"comparison_question","source":"response","value":"1000"},{"periodoffset":0,"parameter":"question","source":"response","value":"1000"},{"periodoffset":0,"parameter":"threshold","source":"","value":"30000"}]}},{"primaryquestion":"1001","validationid":41,"defaultvalue":"0","validationparametersByValidationid":{"nodes":[{"periodoffset":1,"parameter":"comparison_question","source":"response","value":"1000"},{"periodoffset":0,"parameter":"question","source":"response","value":"1000"},{"periodoffset":0,"parameter":"threshold","source":"","value":"0"}]}}]}},{"baseformula":"question != comparison_question","rule":"QVDQ","validationformsByRule":{"nodes":[{"primaryquestion":"1000","validationid":30,"defaultvalue":"0","validationparametersByValidationid":{"nodes":[{"periodoffset":0,"parameter":"comparison_question","source":"response","value":"4000"},{"periodoffset":0,"parameter":"question","source":"response","value":"1000"}]}},{"primaryquestion":"1001","validationid":31,"defaultvalue":"0","validationparametersByValidationid":{"nodes":[{"periodoffset":0,"parameter":"comparison_question","source":"response","value":"4001"},{"periodoffset":0,"parameter":"question","source":"response","value":"1001"}]}}]}},{"baseformula":"question != \"\"","rule":"VP","validationformsByRule":{"nodes":[{"primaryquestion":"3000","validationid":10,"defaultvalue":"","validationparametersByValidationid":{"nodes":[{"periodoffset":0,"parameter":"question","source":"response","value":"3000"}]}},{"primaryquestion":"2000","validationid":11,"defaultvalue":"","validationparametersByValidationid":{"nodes":[{"periodoffset":0,"parameter":"question","source":"response","value":"2000"}]}},{"primaryquestion":"1000","validationid":12,"defaultvalue":"","validationparametersByValidationid":{"nodes":[{"periodoffset":0,"parameter":"question","source":"response","value":"1000"}]}},{"primaryquestion":"4000","validationid":13,"defaultvalue":"","validationparametersByValidationid":{"nodes":[{"periodoffset":0,"parameter":"question","source":"response","value":"4000"}]}}]}}]}}}

    public JSONArray parseValidationConfig() {                      
        var outputArray = new JSONArray();        
        if (jsonQlResponse.getJSONObject("data").getJSONObject("allValidationrules").getJSONArray("nodes").length() > 0) {
            outputArray = jsonQlResponse.getJSONObject("data").getJSONObject("allValidationrules").getJSONArray("nodes");
        }
        return outputArray;

    }

}

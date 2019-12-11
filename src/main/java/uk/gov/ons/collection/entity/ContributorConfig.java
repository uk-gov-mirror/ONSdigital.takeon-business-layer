package uk.gov.ons.collection.entity;

import lombok.extern.log4j.Log4j2;
import uk.gov.ons.collection.exception.InvalidJsonException;
import uk.gov.ons.collection.service.ServiceInterface;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Log4j2
/**
 * This class is responsible for constructing valid graphQL JSON queries to obtain contributor configuration data.
 * It provides the configuration for a single contributor (reference|period|survey)
*/
public class ContributorConfig {

    private final ServiceInterface service;
    private final String reference;
    private final List<String> periods;
    private final String survey;

    private final String loadQuery = "query contributordetails($period: String!, $reference: String!, $survey: String!)" +
            "{ contributorByReferenceAndPeriodAndSurvey(reference: $reference, period: $period, survey: $survey) " +
                "{reference period survey surveyBySurvey {periodicity} " +
                "formid status receiptdate lockedby lockeddate checkletter frozensicoutdated rusicoutdated frozensic " +
                "rusic frozenemployees employees frozenemployment employment frozenfteemployment fteemployment frozenturnover " +
                "turnover enterprisereference wowenterprisereference cellnumber currency vatreference payereference " +
                "companyregistrationnumber numberlivelocalunits numberlivevat numberlivepaye legalstatus " +
                "reportingunitmarker region birthdate tradingstyle contact telephone fax selectiontype inclusionexclusion " +
                "createdby createddate lastupdatedby lastupdateddate " +
                "formByFormid {survey formdefinitionsByFormid {nodes {questioncode type}}}" +
                "responsesByReferenceAndPeriodAndSurvey {nodes {reference period survey instance questioncode response }}}}";

    public ContributorConfig(String reference, List<String> idbrPeriods, String survey, ServiceInterface service) {
        this.reference = reference;
        this.periods = idbrPeriods;
        this.survey = survey;
        this.service = service;
    }

    private String buildLoadQuery(String period) {
        return "{\"query\": \"" + this.loadQuery + "\",\"variables\": {"
            + "\"reference\": \"" + reference
            + "\",\"period\": \"" + period
            + "\",\"survey\": \"" + survey + "\"}}";
    }

    public String load() throws InvalidJsonException {

        var configResponses = new ArrayList<String>();
        for (int i = 0; i < periods.size(); i++) {
            var period = periods.get(i);
            var response = service.runQuery(this.buildLoadQuery(period));
            configResponses.add(response);
        }

        try {
            return parseJsonResponses(configResponses);
        } catch (JSONException e) {
            log.info("Error parsing contributor config JSON: " + configResponses);
            throw new InvalidJsonException("Error processing responses within contributor json: " + configResponses, e);
        } catch (NullPointerException e) {
            log.info("Error parsing contributor config JSON: " + configResponses);
            throw new InvalidJsonException("Error processing responses within contributor json: " + configResponses, e);
        }
    }

    // We now have an array of configuration JSON responses. Each contains form responses, the form definition and the contributor details
    // We restructure this to simplify the format
    // All exceptions are passed back up to the calling method
    private String parseJsonResponses(List<String> jsonList) {

        var responses = new JSONArray();
        var contributors = new JSONArray();
        var forms = new JSONArray();

        for (String config : jsonList) {

            var contributor = new JSONObject(config).getJSONObject("data").optJSONObject("contributorByReferenceAndPeriodAndSurvey");

            // A contributor may not have been selected for the given period. Skip if they don't exist
            if (contributor == null) {
                continue;
            }

            var responseArray = contributor.getJSONObject("responsesByReferenceAndPeriodAndSurvey").getJSONArray("nodes");
            responseArray.forEach(item -> {
                responses.put((JSONObject) item);
            });

            // Extract the form definition and add in any desired attributes (some flattening of the structure)
            var formArray = contributor.getJSONObject("formByFormid").getJSONObject("formdefinitionsByFormid").getJSONArray("nodes");
            for (int j = 0; j < formArray.length(); j++) {
                formArray.getJSONObject(j).put("survey",survey)
                                          .put("period",contributor.getString("period"));
                forms.put(formArray.getJSONObject(j));
            }

            // Remove any sub-array data brought in with the graphQL query. Retain everything else for the contributor
            contributor.remove("surveyBySurvey");
            contributor.remove("responsesByReferenceAndPeriodAndSurvey");
            contributor.remove("formByFormid");
            contributors.put(contributor);

        }

        var parsedConfig = new JSONObject().put("contributor",contributors)
                                           .put("response",responses)
                                           .put("question_schema",forms);

        return parsedConfig.toString();
    }

}
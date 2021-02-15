package uk.gov.ons.collection.entity;

import lombok.extern.log4j.Log4j2;
import uk.gov.ons.collection.exception.InvalidJsonException;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

@Log4j2
/**
 * This class is responsible for accepting graphQL JSON query responses and parsing/providing
 * the configuration for a single contributor (reference|period|survey) in a suitable format
*/
public class ContributorConfig {

    private final List<String> responses;
    private static final String QUESTION_CODE = "questioncode";
    private static final String RESPONSE = "response";
    private static final String EMPTY_SPACE = "";
    private static final String DATE_ADJUSTMENT = "dateadjustment";
    private static final String REFERENCE = "reference";
    private static final String PERIOD = "period";
    private static final String SURVEY = "survey";
    private static final String INSTANCE = "instance";
    private static final String ADJUSTED_RESPONSE = "adjustedresponse";

    public ContributorConfig(List<String> responses) {
        this.responses = responses;
    }

    public String getContributorConfig() throws InvalidJsonException {
        try {
            return parseJsonResponses(responses);
        } catch (JSONException e) {
            log.info("Error parsing contributor config JSON: " + responses);
            throw new InvalidJsonException("Error processing responses within contributor json: " + responses, e);
        } catch (NullPointerException e) {
            log.info("Error parsing contributor config JSON: " + responses);
            throw new InvalidJsonException("Error processing responses within contributor json: " + responses, e);
        }
    }

    // We now have an array of configuration JSON responses. Each contains form responses, the form definition and the contributor details
    // We restructure this to simplify the format
    // All exceptions are passed back up to the calling method
    private String parseJsonResponses(List<String> jsonList) throws InvalidJsonException {

        var responses = new JSONArray();
        var contributors = new JSONArray();
        var forms = new JSONArray();

        JSONArray responseResultArr = new JSONArray();

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
            if (formArray.isEmpty()) {
                throw new InvalidJsonException("Form defininition has no responses: " + config);
            }

            for (int j = 0; j < formArray.length(); j++) {
                formArray.getJSONObject(j).put(SURVEY,contributor.getString(SURVEY))
                                          .put(PERIOD,contributor.getString(PERIOD));
                forms.put(formArray.getJSONObject(j));
            }

            // Generate responses including dateAdjustmentFlag by getting it from formArray
            // and other fields from responseArray
            if (formArray.length() > 0) {
                for (int i = 0; i < formArray.length(); i++) {
                    JSONObject eachFormDefinitionObject = formArray.getJSONObject(i);
                    String questionCode = eachFormDefinitionObject.getString(QUESTION_CODE);
                    var eachResponseObject = new JSONObject();
                    boolean dateAdjustmentFlag = eachFormDefinitionObject.getBoolean(DATE_ADJUSTMENT);
                    try {
                        for (int j = 0; j < responseArray.length(); j++) {
                            // Performed null check
                            String response = (responseArray.getJSONObject(j).isNull(RESPONSE))
                                    ? EMPTY_SPACE : responseArray.getJSONObject(j).getString(RESPONSE);
                            if (questionCode.equals(responseArray.getJSONObject(j).getString(QUESTION_CODE))) {
                                eachResponseObject.put(REFERENCE, responseArray.getJSONObject(j).getString(REFERENCE));
                                eachResponseObject.put(PERIOD, responseArray.getJSONObject(j).getString(PERIOD));
                                eachResponseObject.put(SURVEY, responseArray.getJSONObject(j).getString(SURVEY));
                                eachResponseObject.put(QUESTION_CODE, questionCode);
                                eachResponseObject.put(RESPONSE, response);
                                eachResponseObject.put(INSTANCE, responseArray.getJSONObject(j).get(INSTANCE));
                                eachResponseObject.put(DATE_ADJUSTMENT, dateAdjustmentFlag);
                                String adjustedResponse = (responseArray.getJSONObject(j).isNull(ADJUSTED_RESPONSE))
                                        ? EMPTY_SPACE : responseArray.getJSONObject(j).getString(ADJUSTED_RESPONSE);
                                eachResponseObject.put(ADJUSTED_RESPONSE, adjustedResponse);
                                responseResultArr.put(eachResponseObject);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("Error in processing responses for Validation Date Adjustment: " + e.getMessage());
                        throw new InvalidJsonException( "Error in processing responses for Validation Date Adjustment: " + e.getMessage());
                    }
                }
            }


            // Remove any sub-array data brought in with the graphQL query. Retain everything else for the contributor
            contributor.remove("surveyBySurvey");
            contributor.remove("responsesByReferenceAndPeriodAndSurvey");
            contributor.remove("formByFormid");
            contributors.put(contributor);
        }

        if (contributors.isEmpty()) {
            throw new InvalidJsonException("Error processing responses within contributor json: " + jsonList);
        }

        var parsedConfig = new JSONObject().put("contributor",contributors)
                                           .put("response",responseResultArr)
                                           .put("question_schema",forms);

        return parsedConfig.toString();
    }

}
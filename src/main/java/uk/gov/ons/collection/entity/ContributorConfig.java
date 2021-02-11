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
                formArray.getJSONObject(j).put("survey",contributor.getString("survey"))
                                          .put("period",contributor.getString("period"));
                forms.put(formArray.getJSONObject(j));
            }

            log.info("formArray count: " + formArray.length());
            log.info("responseArray count: " + responseArray.length());

            if (formArray.length() > 0) {
                for (int i = 0; i < formArray.length(); i++) {
                    log.info("formArray " + i);
                    JSONObject eachFormDefinitionObject = formArray.getJSONObject(i);
                    String questionCode = eachFormDefinitionObject.getString(QUESTION_CODE);
                    log.info("questionCode:: " + questionCode);
                    var eachResponseObject = new JSONObject();
                    boolean dateAdjustmentFlag = eachFormDefinitionObject.getBoolean("dateadjustment");
                    log.info("dateAdjustmentFlag:: " );
                    log.info(dateAdjustmentFlag);
                    try {
                        for (int j = 0; j < responseArray.length(); j++) {
                            // Performed null check
                            log.info("responseArray " + j);
                            String response = (responseArray.getJSONObject(j).isNull(RESPONSE))
                                    ? EMPTY_SPACE : responseArray.getJSONObject(j).getString(RESPONSE);
                            if (questionCode.equals(responseArray.getJSONObject(j).getString(QUESTION_CODE))) {
                                log.info("Questions Equal");
                                eachResponseObject.put("reference", responseArray.getJSONObject(j).getString("reference"));
                                log.info(responseArray.getJSONObject(j).getString("reference"));
                                eachResponseObject.put("period", responseArray.getJSONObject(j).getString("period"));
                                log.info(responseArray.getJSONObject(j).getString("period"));
                                eachResponseObject.put("survey", responseArray.getJSONObject(j).getString("survey"));
                                log.info(responseArray.getJSONObject(j).getString("survey"));
                                eachResponseObject.put(QUESTION_CODE, questionCode);
                                log.info(questionCode);
                                eachResponseObject.put(RESPONSE, response);
                                log.info(response);
                                eachResponseObject.put("instance", responseArray.getJSONObject(j).get("instance"));
                                log.info(responseArray.getJSONObject(j).get("instance"));
                                eachResponseObject.put("dateadjustment", dateAdjustmentFlag);
                                log.info( dateAdjustmentFlag);
                                String adjustedResponse = (responseArray.getJSONObject(j).isNull("adjustedresponse"))
                                        ? EMPTY_SPACE : responseArray.getJSONObject(j).getString("adjustedresponse");
                                eachResponseObject.put("adjustedresponse", adjustedResponse);
                                responseResultArr.put(eachResponseObject);
                                log.info("Done responseResultArr.put(eachResponseObject)");
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error(e.getMessage());
                    }
                }
            }

            log.info("Done formArray.length()");

            // Remove any sub-array data brought in with the graphQL query. Retain everything else for the contributor
            contributor.remove("surveyBySurvey");
            contributor.remove("responsesByReferenceAndPeriodAndSurvey");
            contributor.remove("formByFormid");
            contributors.put(contributor);
        }

        if (contributors.isEmpty()) {
            throw new InvalidJsonException("Error processing responses within contributor json: " + jsonList);
        }

        log.info("Going to put parsedConfig");
        var parsedConfig = new JSONObject().put("contributor",contributors)
                                           .put("response",responseResultArr)
                                           .put("question_schema",forms);

        return parsedConfig.toString();
    }

}
package uk.gov.ons.collection.entity;

import lombok.extern.log4j.Log4j2;
import org.json.JSONException;
import org.json.JSONObject;
import uk.gov.ons.collection.exception.InvalidIdbrPeriodException;
import uk.gov.ons.collection.exception.InvalidJsonException;
import uk.gov.ons.collection.exception.InvalidPeriodicityException;
import uk.gov.ons.collection.service.ServiceInterface;
import uk.gov.ons.collection.utilities.RelativePeriod;

import java.util.ArrayList;
import java.util.List;


@Log4j2
public class DataPrepConfig {

    private final ServiceInterface service;
    private final String reference;
    private final String period;
    private final String survey;

    private int formId;
    private String periodicity;

    public DataPrepConfig(String reference, String period, String survey, ServiceInterface service) {
        this.reference = reference;
        this.period = period;
        this.survey = survey;
        this.service = service;
    }

    private ValidationConfig loadValidationConfig(int formId) throws InvalidJsonException {
        var validationConfigQuery = new ValidationConfigQuery(formId, service);
        return new ValidationConfig(validationConfigQuery.load());
    }

    private void loadFormIdAndPeriodicity() throws InvalidJsonException {
        var currentContributor = new Contributor(reference, period, survey, service);
        currentContributor.load();
        this.formId = currentContributor.getFormId();
        this.periodicity = currentContributor.getSurveyPeriodicity();
    }

    private JSONObject loadContributorResponseConfig(List<String> idbrPeriods) throws InvalidJsonException {
        var responses = new ContributorConfigQuery(this.reference, idbrPeriods, this.survey, this.service).load();
        // log.info("ContributorConfigQuery-responses :: " + responses);
        var respConfig = new ContributorConfig(responses).getContributorConfig();
        // log.info("respConfig :: " + respConfig);
        return new JSONObject(respConfig);
    }

    private List<String> getContributorPeriodsToLoad(List<Integer> offsets) {
        try {
            return new RelativePeriod(this.periodicity).getIdbrPeriods(offsets, this.period);
        } catch (InvalidPeriodicityException e) {
            log.info("Error determining periods to load contributor data (bad periodicity) :: " + e, e);
        } catch (InvalidIdbrPeriodException e) {
            log.info("Error determining periods to load contributor data (invalid IDBR period) :: " + e, e);
        }
        return new ArrayList<String>();
    }

    // Entry point -> Handles all the loading and combining of the necessary configuration data
    public String load() throws InvalidJsonException {

        loadFormIdAndPeriodicity();
        var validationConfig = loadValidationConfig(this.formId);
        var offsets = validationConfig.getUniqueOffsets();
        var validationConfigJson = validationConfig.getValidationConfig();
        var idbrPeriods = getContributorPeriodsToLoad(offsets);
        var contributorResponseConfig = loadContributorResponseConfig(idbrPeriods);
        try {
            var outputJson = new JSONObject();
            outputJson.put("reference", this.reference)
                      .put("period",this.period)
                      .put("survey",this.survey)
                      .put("periodicity", this.periodicity)
                      .put("validation_config", new JSONObject(validationConfigJson).get("validation_config"))
                      .put("contributor",contributorResponseConfig.getJSONArray("contributor"))
                      .put("response",contributorResponseConfig.getJSONArray("response"))
                      .put("question_schema",contributorResponseConfig.getJSONArray("question_schema"));
            return outputJson.toString();
        } catch (JSONException e) {
            throw new InvalidJsonException("Error building data prep JSON: ", e);
        }
    }

}
package uk.gov.ons.collection.entity;

import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uk.gov.ons.collection.exception.InvalidJsonException;

@Log4j2
public class ContributorSelectiveEditingStatusResponse {

    private JSONObject jsonQlResponse;
    private static final String REFERENCE = "reference";
    private static final String PERIOD = "period";
    private static final String SURVEY = "survey";
    private static final String STATUS = "status";
    private static final String VALIDATION_PASSED = "validationPassed";
    private static final String FLAG = "selective_editing_flag";
    private static final String DC_CHECK_NEEDED = "Check needed";
    private static final String DC_CLEAR = "Clear";
    private static final String DC_CLEAR_OVERRIDDEN = "Clear - overridden";
    private static final String DC_CLEAR_OVERRIDDEN_SE = "Clear overridden SE";
    private static final String DC_FORM_SAVED = "Form saved";
    private static final String DC_FORM_SENT_OUT = "Form sent out";
    private static final String BPM_FORM_SENT_OUT = "FORM_SENT_OUT";

    private static final String BPM_CHECK_NEEDED = "CHECK_NEEDED";
    private static final String BPM_CLEAR = "CLEAR";
    private static final String BPM_CLEAR_OVERRIDDEN = "OVERRIDDEN";
    private static final String BPM_CLEAR_OVERRIDDEN_SE = "OVERRIDDEN_SELECTIVE_EDITING";
    private static final String BPM_FORM_SAVED = "FORM_SAVED";
    private static final String DC_SE_PASSED = "P";
    private static final String DC_SE_FAILED = "F";
    private static final String DC_SE_MISSING = "M";
    private static final String BPM_SE_PASSED = "PASSED";
    private static final String BPM_SE_FAILED = "FAILED";
    private static final String BPM_SE_MISSING = "MISSING";
    private static final String EMPTY = "";
    private static final String BPM_VALIDATION_CALL_ID = "BPMvalidationCallID";
    private static final String ZERO = "0";



    public ContributorSelectiveEditingStatusResponse(String inputJson) throws InvalidJsonException {
        try {
            jsonQlResponse = new JSONObject(inputJson);
        } catch (JSONException e) {
            log.error("Error in processing Contributor Status Query Response: " + e.getMessage());
            throw new InvalidJsonException("Given string could not be converted/processed: " + e);
        }
    }

    public JSONObject parseContributorStatusQueryResponse() throws InvalidJsonException {
        JSONArray contribArray;
        JSONObject contributorStatusResultObj = new JSONObject();

        try {
            contribArray = jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes");
            if (contribArray.length() > 0) {
                String flag = "";
                JSONObject contributorObject = contribArray.getJSONObject(0);
                contributorStatusResultObj.put(REFERENCE, contributorObject.getString(REFERENCE));
                contributorStatusResultObj.put(PERIOD, contributorObject.getString(PERIOD));
                contributorStatusResultObj.put(SURVEY, contributorObject.getString(SURVEY));
                contributorStatusResultObj.put(BPM_VALIDATION_CALL_ID, ZERO);

                // Setting dummy value and BPM transform Lambda will replace with actual value in looping Validation Outputs
                // containing trigger count
                contributorStatusResultObj.put(VALIDATION_PASSED, true);
                String contributorStatus = contributorObject.getString(STATUS);
                log.info("Contributor status before processing :" + contributorStatus);
                contributorStatus = (contributorStatus != null && contributorStatus.equals(DC_CHECK_NEEDED)) ? (BPM_CHECK_NEEDED)
                        : ((contributorStatus.equals(DC_CLEAR)) ? (BPM_CLEAR)
                        : ((contributorStatus.equals(DC_CLEAR_OVERRIDDEN))
                        ? (BPM_CLEAR_OVERRIDDEN) : ((contributorStatus.equals(DC_CLEAR_OVERRIDDEN_SE))
                        ? (BPM_CLEAR_OVERRIDDEN_SE) : ((contributorStatus.equals(DC_FORM_SAVED)) ? (BPM_FORM_SAVED)
                        : ((contributorStatus.equals(DC_FORM_SENT_OUT)) ? BPM_FORM_SENT_OUT : EMPTY)))));
                contributorStatusResultObj.put(STATUS, contributorStatus);
                log.info("Contributor status after processing :" + contributorStatus);
                contributorStatusResultObj.put(FLAG, EMPTY);

                JSONObject selectiveEditingObject = contributorObject.getJSONObject("contributorselectiveeditingByReferenceAndPeriodAndSurvey");
                if (selectiveEditingObject != null) {
                    flag = selectiveEditingObject.getString(FLAG);
                    log.info("Selective Editing Flag before processing :" + flag);
                    flag = (flag != null && flag.equals(DC_SE_PASSED)) ? (BPM_SE_PASSED) :
                            ((contributorStatus.equals(DC_SE_FAILED)) ? (BPM_SE_FAILED) :
                                    ((contributorStatus.equals(DC_SE_MISSING)) ? (BPM_SE_MISSING) :
                                            (EMPTY)));
                    log.info("Selective Editing Flag after processing :" + flag);
                    contributorStatusResultObj.put(FLAG, flag);
                }
            } else {
                throw new InvalidJsonException("There is no contributor for a given survey, " +
                        "reference and periods. Please verify");
            }

        } catch (Exception e) {
            throw new InvalidJsonException("Problem in parsing Contributor Status " +
                    "GraphQL responses " + e.getMessage(), e);
        }
        return contributorStatusResultObj;
    }


}

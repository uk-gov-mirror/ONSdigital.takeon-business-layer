package uk.gov.ons.collection.entity;

import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import uk.gov.ons.collection.exception.InvalidJsonException;

@Log4j2
public class SelectiveEditingResponse {

    private JSONObject jsonQlResponse;
    private static final String REFERENCE = "reference";
    private static final String PERIOD = "period";
    private static final String SURVEY = "survey";
    private static final String RESULTS_CELL_NUMBER = "resultscellnumber";
    private static final String CELL_NUMBER = "cellnumber";
    private static final String DOMAIN = "domain";
    private static final String QUESTION_CODE = "questioncode";
    private static final String THRESHOLD = "threshold";
    private static final String ESTIMATE = "estimate";
    private static final String DESIGN_WEIGHT = "designweight";
    private static final String DOMAIN_CONFIG = "domainconfig";




    public SelectiveEditingResponse(String inputJSON) throws InvalidJsonException {
        try {
            jsonQlResponse = new JSONObject(inputJSON);
        } catch (JSONException e) {
            log.error("Error in processing Selective Editing Response: " + e.getMessage());
            throw new InvalidJsonException("Given string could not be converted/processed: " + e);
        }
    }

    public String parseSelectiveEditingQueryResponse() throws InvalidJsonException {
        JSONArray contribArray;
        JSONObject selectiveEditingResultObj = new JSONObject();
        String domain = "";
        String cellNumber = "";
        try {
            if (jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes").length() > 0) {
                contribArray = jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes");
                JSONObject contributorObject = contribArray.getJSONObject(0);

                selectiveEditingResultObj.put(REFERENCE, contributorObject.getString(REFERENCE));
                selectiveEditingResultObj.put(PERIOD, contributorObject.getString(PERIOD));
                selectiveEditingResultObj.put(SURVEY, contributorObject.getString(SURVEY));
                domain = contributorObject.getString(DOMAIN);
                cellNumber = contributorObject.getString(RESULTS_CELL_NUMBER);
                selectiveEditingResultObj.put(RESULTS_CELL_NUMBER, cellNumber);
                selectiveEditingResultObj.put(DOMAIN, domain);
                JSONArray domainConfigArr = new JSONArray();

                if (jsonQlResponse.getJSONObject("data").getJSONObject("allSelectiveeditingconfigs").getJSONArray("nodes").length() > 0) {
                    JSONArray domainConfigArray = jsonQlResponse.getJSONObject("data").getJSONObject("allSelectiveeditingconfigs").getJSONArray("nodes");
                    for (int i=0; i < domainConfigArray.length(); i++) {
                        JSONObject eachDomainConfigObject = domainConfigArray.getJSONObject(i);
                        if(eachDomainConfigObject.getString(DOMAIN).equals(domain)) {
                            //Match Found
                            var eachResultDomainObject = new JSONObject();
                            eachResultDomainObject.put(QUESTION_CODE, eachDomainConfigObject.getString(QUESTION_CODE));
                            eachResultDomainObject.put(THRESHOLD, eachDomainConfigObject.getString(THRESHOLD));
                            eachResultDomainObject.put(ESTIMATE, eachDomainConfigObject.getString(ESTIMATE));
                            domainConfigArr.put(eachResultDomainObject);
                        }
                    }
                    if (domainConfigArr.length() > 0) {
                        selectiveEditingResultObj.put(DOMAIN_CONFIG, domainConfigArr);
                    } else {
                        throw new InvalidJsonException("There are no thresholds for a given domain in the contributor. Please verify");
                    }
                } else {
                    throw new InvalidJsonException("There is no domain config. Please verify");
                }
                if (jsonQlResponse.getJSONObject("data").getJSONObject("allCelldetails").getJSONArray("nodes").length() > 0) {
                    JSONArray cellDetailConfigArray = jsonQlResponse.getJSONObject("data").getJSONObject("allCelldetails").getJSONArray("nodes");

                    boolean isCellNumberFound = false;
                    for (int i=0; i < cellDetailConfigArray.length(); i++) {
                        JSONObject eachDomainConfigObject = cellDetailConfigArray.getJSONObject(i);
                        if(eachDomainConfigObject.getString(CELL_NUMBER).equals(cellNumber)) {
                            //Match Found
                            selectiveEditingResultObj.put(DESIGN_WEIGHT, eachDomainConfigObject.getString(DESIGN_WEIGHT));
                            isCellNumberFound = true;
                            break;
                        }
                    }
                    if (!isCellNumberFound ) {
                        throw new InvalidJsonException("There are no design weight for a given cell number . Please verify");
                    }
                }

            } else {
                throw new InvalidJsonException("There is no contributor for a given survey, reference and periods. Please verify");
            }

        } catch(Exception e) {
            throw new InvalidJsonException("Problem in parsing Selective Editing GraphQL responses " + e.getMessage(), e);
        }
        return selectiveEditingResultObj.toString();
    }



}

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
            contribArray = jsonQlResponse.getJSONObject("data").getJSONObject("allContributors").getJSONArray("nodes");
            if (contribArray.length() > 0) {
                JSONObject contributorObject = contribArray.getJSONObject(0);
                selectiveEditingResultObj.put(REFERENCE, contributorObject.getString(REFERENCE));
                selectiveEditingResultObj.put(PERIOD, contributorObject.getString(PERIOD));
                selectiveEditingResultObj.put(SURVEY, contributorObject.getString(SURVEY));
                domain = contributorObject.get(DOMAIN).toString();
                cellNumber = contributorObject.get(RESULTS_CELL_NUMBER).toString();
                selectiveEditingResultObj.put(RESULTS_CELL_NUMBER, cellNumber);
                selectiveEditingResultObj.put(DOMAIN, domain);
                JSONArray domainConfigResultArr = new JSONArray();

                JSONArray domainConfigArray = jsonQlResponse.getJSONObject("data").getJSONObject("allSelectiveeditingconfigs").getJSONArray("nodes");
                if (domainConfigArray.length() > 0) {
                    for (int i=0; i < domainConfigArray.length(); i++) {
                        JSONObject eachDomainConfigObject = domainConfigArray.getJSONObject(i);
                        if(eachDomainConfigObject.getString(DOMAIN).equals(domain)) {
                            //Match Found
                            var eachResultDomainObject = new JSONObject();
                            eachResultDomainObject.put(QUESTION_CODE, eachDomainConfigObject.getString(QUESTION_CODE));
                            eachResultDomainObject.put(THRESHOLD, eachDomainConfigObject.get(THRESHOLD).toString());
                            eachResultDomainObject.put(ESTIMATE, eachDomainConfigObject.get(ESTIMATE).toString());
                            domainConfigResultArr.put(eachResultDomainObject);
                        }
                    }
                    if (domainConfigResultArr.length() > 0) {
                        selectiveEditingResultObj.put(DOMAIN_CONFIG, domainConfigResultArr);
                    } else {
                        throw new InvalidJsonException("There are no thresholds for a given domain in the contributor. Please verify");
                    }
                } else {
                    throw new InvalidJsonException("There is no domain config. Please verify");
                }
                JSONArray cellDetailConfigArray = jsonQlResponse.getJSONObject("data").getJSONObject("allCelldetails").getJSONArray("nodes");
                if (cellDetailConfigArray.length() > 0) {
                    boolean isCellNumberFound = false;
                    for (int i=0; i < cellDetailConfigArray.length(); i++) {
                        JSONObject eachDomainConfigObject = cellDetailConfigArray.getJSONObject(i);
                        if(eachDomainConfigObject.get(CELL_NUMBER).toString().equals(cellNumber)) {
                            //Match Found
                            selectiveEditingResultObj.put(DESIGN_WEIGHT, eachDomainConfigObject.get(DESIGN_WEIGHT).toString());
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

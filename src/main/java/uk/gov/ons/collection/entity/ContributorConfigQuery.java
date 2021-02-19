package uk.gov.ons.collection.entity;

import uk.gov.ons.collection.exception.InvalidJsonException;
import uk.gov.ons.collection.service.ServiceInterface;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.log4j.Log4j2;

@Log4j2
/**
 * This class is responsible for constructing valid graphQL JSON queries to obtain contributor configuration data.
 * It provides the configuration for a single contributor (reference|period|survey)
*/
public class ContributorConfigQuery {

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
                "formByFormid {survey formdefinitionsByFormid {nodes {questioncode type dateadjustment}}}" +
                "responsesByReferenceAndPeriodAndSurvey {nodes {reference period survey instance questioncode response adjustedresponse}}}}";

    public ContributorConfigQuery(String reference, List<String> idbrPeriods, String survey, ServiceInterface service) {
        this.reference = reference;
        this.periods = idbrPeriods;
        this.survey = survey;
        this.service = service;
    }

    private String buildLoadQuery(String period) {
        return "{\"query\": \"" + this.loadQuery + "\",\"variables\": {" +
            "\"reference\": \"" + reference + "\"," +
            "\"period\": \"" + period + "\"," +
            "\"survey\": \"" + survey + "\"}}";
    }

    public List<String> load() throws InvalidJsonException {
        var configResponses = new ArrayList<String>();
        for (int i = 0; i < periods.size(); i++) {
            var period = periods.get(i);
            log.info("ContributorConfigQuery: " + this.buildLoadQuery(period));
            var response = service.runQuery(this.buildLoadQuery(period));
            configResponses.add(response);
        }
        return configResponses;
    }
}
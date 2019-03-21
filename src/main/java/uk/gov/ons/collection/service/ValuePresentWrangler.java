package uk.gov.ons.collection.service;

import uk.gov.ons.collection.entity.*;
import uk.gov.ons.collection.utilities.Helpers;
import java.util.*;
import java.util.stream.Collectors;

public class ValuePresentWrangler {

    private String reference;
    private String period;
    private String survey;
    private int currentFormId;
    private Iterable<ContributorEntity> contributorEntities;
    private Iterable<QuestionResponseEntity> questionResponseEntities;
    private List<ValidationFormEntity> validationConfig;
    // private List<ValidationFormEntity> filteredValidationFormEntities;
    private List<ReturnedValidationOutputs> validationConfigEntitiesToReturn = new ArrayList<>();
    private final String algo = "https://api.algpoc.com/v1/algo/dllmorgan/ValidationValuePresent/1.0.0";
    private ApiCaller apiCaller;

    public ValuePresentWrangler(String reference, String period, String survey,  ApiCaller caller) {
        this.reference = reference;
        this.period = period;
        this.survey = survey;
        this.apiCaller = caller;
    }

    public void loadContributor() {
        contributorEntities = apiCaller.loadContributors(reference, period, survey);
    }

    public void loadResponses(){
        questionResponseEntities = apiCaller.loadResponses(reference, period, survey);
    }

    private void determineContributorFormID() {
        List<ContributorEntity> contributor = new Helpers.ParseIterable().parseIterable(contributorEntities);
        currentFormId = contributor.get(0).formid;
    }

    public void loadValidationConfig(){
        determineContributorFormID();
        Iterable<ValidationFormEntity> unfilteredValidationConfig = apiCaller.loadValidationConfig(currentFormId);
        List<ValidationFormEntity> formEntityList = new Helpers.ParseIterable().parseIterable(unfilteredValidationConfig);
        validationConfig = formEntityList.stream().filter(element -> element.getValidationCode().equals("VP"))
                .collect(Collectors.toList());
    }

    public Iterable<QuestionResponseEntity> GenerateCompleteResponseDataset() {
        return new Helpers().checkAllQuestionsPresent(apiCaller, reference, period, survey);
    }

    public List<WrangledValidationData> generateValidationData(Iterable<QuestionResponseEntity> responses) {
        List<WrangledValidationData> dataForValidationAPI = new ArrayList<>();
        for(QuestionResponseEntity questionResponseEntity: responses){
            for (ValidationFormEntity validationFormEntity: validationConfig){
                if(Objects.equals(questionResponseEntity.getQuestionCode().trim(), validationFormEntity.getQuestionCode())){
                    String metaData = buildMetaData(questionResponseEntity, validationFormEntity);
                    WrangledValidationData inputData = new WrangledValidationData().builder().value(questionResponseEntity.getResponse()).metaData(metaData).build();
                    dataForValidationAPI.add(inputData);
                }
            }
        }
        return dataForValidationAPI;
    }

    private String buildMetaData(QuestionResponseEntity questionResponseEntity, ValidationFormEntity validationFormEntity) {
        return "\"reference\":" + "\"" + questionResponseEntity.getReference()
                + "\",\"period\":\"" + questionResponseEntity.getPeriod()
                + "\",\"survey\":\"" + questionResponseEntity.getSurvey()
                + "\",\"questionCode\":\"" + questionResponseEntity.getQuestionCode()
                + "\",\"validationId\":\"" + validationFormEntity.getValidationid()
                + "\",\"primaryValue\":\"" + questionResponseEntity.getResponse()
                + "\",\"instance\":\"" + questionResponseEntity.getInstance();
    }

    public List<String> setPayloadAndReturnFormEntities(Iterable<WrangledValidationData> inputData){
        List<String> outputs = new ArrayList<>();
        for(WrangledValidationData input: inputData){
            outputs.add(generateJson(input));
        }
        return outputs;
    }

    private String generateJson(WrangledValidationData input){
        return "{\"value\":\"" + input.getValue() + "\",\"metaData\":{" + input.getMetaData() + "\"}}";
    }

    public List<String> parseDataAndGenerateJson() {
        loadData();
        Iterable<QuestionResponseEntity> allResponses = GenerateCompleteResponseDataset();
        List<WrangledValidationData> wrangledData = generateValidationData(allResponses);
        return setPayloadAndReturnFormEntities(wrangledData);
    }

    private void loadData() {
        loadContributor();
        loadResponses();
        loadValidationConfig();
    }

}

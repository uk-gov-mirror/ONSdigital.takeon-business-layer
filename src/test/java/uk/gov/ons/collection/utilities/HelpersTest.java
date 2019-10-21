package uk.gov.ons.collection.utilities;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import uk.gov.ons.collection.entity.FormDefinitionEntity;
import uk.gov.ons.collection.entity.QuestionResponseEntity;
import uk.gov.ons.collection.service.ApiCallerTest;

class HelpersTest {

    @Test
    void placeIntoMap_twoInstances_OneQuestionCode() {
        Helpers helpers = new Helpers();
        List<QuestionResponseEntity> testResponses = new ArrayList<>();
        QuestionResponseEntity questionResponseEntity = QuestionResponseEntity.builder().instance(3).questionCode("143").build();
        QuestionResponseEntity questionResponseEntity_2 = QuestionResponseEntity.builder().instance(0).questionCode("143").build();
        testResponses.add(questionResponseEntity);
        testResponses.add(questionResponseEntity_2);

        List<String> testList_one = new ArrayList<>();
        testList_one.add(questionResponseEntity.getQuestionCode());

        List<String> testList_two = new ArrayList<>();
        testList_two.add(questionResponseEntity_2.getQuestionCode());

        Map<Integer, List<String>> outputMap = new HashMap<>();
        outputMap.put(3, testList_one);
        outputMap.put(0, testList_two);
        assertEquals(helpers.placeIntoMap(testResponses), outputMap);
    }

    @Test
    void placeIntoMap_oneInstances_TwoQuestionCode() {
        Helpers helpers = new Helpers();
        // List that gets passed to helper function
        List<QuestionResponseEntity> testResponses = new ArrayList<>();
        // Instantiate entities
        QuestionResponseEntity questionResponseEntity = QuestionResponseEntity.builder().instance(0).questionCode("146").build();
        QuestionResponseEntity questionResponseEntity_2 = QuestionResponseEntity.builder().instance(0).questionCode("143").build();
        //Add to list
        testResponses.add(questionResponseEntity);
        testResponses.add(questionResponseEntity_2);


        List<String> testList_one = new ArrayList<>();
        testList_one.add(questionResponseEntity.getQuestionCode());
        testList_one.add(questionResponseEntity_2.getQuestionCode());
        Map<Integer, List<String>> outputMap = new HashMap<>();
        outputMap.put(0, testList_one);

        assertEquals(helpers.placeIntoMap(testResponses), outputMap);
    }

    @Test
    void placeIntoMap_threeInstances_TwoQuestionCode() {
        Helpers helpers = new Helpers();
        // List that gets passed to helper function
        List<QuestionResponseEntity> testResponses = new ArrayList<>();
        // Instantiate entities
        QuestionResponseEntity questionResponseEntity = QuestionResponseEntity.builder().instance(0).questionCode("146").build();
        QuestionResponseEntity questionResponseEntity_2 = QuestionResponseEntity.builder().instance(1).questionCode("143").build();
        QuestionResponseEntity questionResponseEntity_3 = QuestionResponseEntity.builder().instance(2).questionCode("143").build();
        //Add to list
        testResponses.add(questionResponseEntity);
        testResponses.add(questionResponseEntity_2);
        testResponses.add(questionResponseEntity_3);

        List<String> testList_one = new ArrayList<>();
        testList_one.add(questionResponseEntity.getQuestionCode());

        List<String> testList_two = new ArrayList<>();
        testList_two.add(questionResponseEntity_2.getQuestionCode());

        List<String> testList_three = new ArrayList<>();
        testList_three.add(questionResponseEntity_3.getQuestionCode());

        Map<Integer, List<String>> outputMap = new HashMap<>();
        outputMap.put(0, testList_one);
        outputMap.put(1, testList_two);
        outputMap.put(2, testList_three);

        assertEquals(helpers.placeIntoMap(testResponses), outputMap);
    }

    @Test
    void placeIntoMap_threeInstances_SixQuestionCode() {
        Helpers helpers = new Helpers();
        // List that gets passed to helper function
        List<QuestionResponseEntity> testResponses = new ArrayList<>();
        // Instantiate entities
        QuestionResponseEntity questionResponseEntity = QuestionResponseEntity.builder().instance(0).questionCode("146").build();
        QuestionResponseEntity questionResponseEntity_2 = QuestionResponseEntity.builder().instance(1).questionCode("143").build();
        QuestionResponseEntity questionResponseEntity_3 = QuestionResponseEntity.builder().instance(2).questionCode("143").build();
        QuestionResponseEntity questionResponseEntity_4 = QuestionResponseEntity.builder().instance(2).questionCode("001").build();
        QuestionResponseEntity questionResponseEntity_5 = QuestionResponseEntity.builder().instance(2).questionCode("002").build();
        //Add to list
        testResponses.add(questionResponseEntity);
        testResponses.add(questionResponseEntity_2);
        testResponses.add(questionResponseEntity_3);
        testResponses.add(questionResponseEntity_4);
        testResponses.add(questionResponseEntity_5);

        List<String> testList_one = new ArrayList<>();
        testList_one.add(questionResponseEntity.getQuestionCode());

        List<String> testList_two = new ArrayList<>();
        testList_two.add(questionResponseEntity_2.getQuestionCode());

        List<String> testList_three = new ArrayList<>();
        testList_three.add(questionResponseEntity_3.getQuestionCode());

        testList_three.add(questionResponseEntity_4.getQuestionCode());
        testList_three.add(questionResponseEntity_5.getQuestionCode());

        Map<Integer, List<String>> outputMap = new HashMap<>();
        outputMap.put(0, testList_one);
        outputMap.put(1, testList_two);
        outputMap.put(2, testList_three);
        outputMap.put(2, testList_three);
        outputMap.put(2, testList_three);

        assertEquals(helpers.placeIntoMap(testResponses), outputMap);
    }

    List<QuestionResponseEntity> testResponses = new ArrayList<>();
    Iterable<FormDefinitionEntity> testFormDef = new ArrayList<>();
    List<QuestionResponseEntity> testOutputs = new ArrayList<>();
    ApiCallerTest dataLoaderTest = ApiCallerTest.builder().questionResponse(testResponses).definitionEntities(testFormDef).build();

    void setup_checkAllQuestionsPresent() {

        // List of test responses
        testResponses.add(QuestionResponseEntity.builder().questionCode("132").response("Response for 132").build());
        testResponses.add(QuestionResponseEntity.builder().questionCode("032").response("Response for 032").build());
        testResponses.add(QuestionResponseEntity.builder().questionCode("001").response("Response for 001").build());
        // Basic form definition
        ((ArrayList<FormDefinitionEntity>) testFormDef).add(FormDefinitionEntity.builder().questionCode("132").build());
        ((ArrayList<FormDefinitionEntity>) testFormDef).add(FormDefinitionEntity.builder().questionCode("032").build());
        ((ArrayList<FormDefinitionEntity>) testFormDef).add(FormDefinitionEntity.builder().questionCode("001").build());
        ((ArrayList<FormDefinitionEntity>) testFormDef).add(FormDefinitionEntity.builder().questionCode("999").build());
        // Setup output list of responses
        testOutputs.add(QuestionResponseEntity.builder().questionCode("132").response("Response for 132").build());
        testOutputs.add(QuestionResponseEntity.builder().questionCode("032").response("Response for 032").build());
        testOutputs.add(QuestionResponseEntity.builder().questionCode("001").response("Response for 001").build());
        testOutputs.add(QuestionResponseEntity.builder().questionCode("999").response("").build());

    }

    @Test
    void checkAllQuestionsPresent_incompleteListOfResponses() {
        setup_checkAllQuestionsPresent();
        assertEquals(new Helpers().checkAllQuestionsPresent(dataLoaderTest, "","","").toString(), testOutputs.toString());
    }

    @Test
    void checkQuestionCodePresent_shouldBePresent_returnsTrue() {
        setup_checkAllQuestionsPresent();
        assertTrue(new Helpers().checkQuestionCodePresent("132", testResponses));
    }

    @Test
    void checkQuestionCodePresent_shouldNotBePresent_returnsFalse() {
        setup_checkAllQuestionsPresent();
        assertFalse(new Helpers().checkQuestionCodePresent("999", testResponses));
    }

    @Test
    void checkQuestionCodePresent_nullShouldNotBePresent_returnsFalse() {
        setup_checkAllQuestionsPresent();
        assertFalse(new Helpers().checkQuestionCodePresent(null, testResponses));
    }
}
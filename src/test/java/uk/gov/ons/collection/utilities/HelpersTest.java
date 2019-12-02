package uk.gov.ons.collection.utilities;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.FormDefinitionEntity;
import uk.gov.ons.collection.entity.QuestionResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HelpersTest {
/*
    @Test
    void placeIntoMap_twoInstances_OneQuestionCode() {
        List<QuestionResponseEntity> testResponses = new ArrayList<>();
        QuestionResponseEntity questionResponseEntity = QuestionResponseEntity.builder().instance(3).questionCode("143").build();
        QuestionResponseEntity questionResponseEntity2 = QuestionResponseEntity.builder().instance(0).questionCode("143").build();
        testResponses.add(questionResponseEntity);
        testResponses.add(questionResponseEntity2);

        List<String> testListOne = new ArrayList<>();
        testListOne.add(questionResponseEntity.getQuestionCode());

        List<String> testListTwo = new ArrayList<>();
        testListTwo.add(questionResponseEntity2.getQuestionCode());

        Map<Integer, List<String>> outputMap = new HashMap<>();
        outputMap.put(3, testListOne);
        outputMap.put(0, testListTwo);
        assertEquals(new Helpers().placeIntoMap(testResponses), outputMap);
    }

    @Test
    void placeIntoMap_oneInstances_TwoQuestionCode() {
        // List that gets passed to helper function
        List<QuestionResponseEntity> testResponses = new ArrayList<>();
        // Instantiate entities
        QuestionResponseEntity questionResponseEntity = QuestionResponseEntity.builder().instance(0).questionCode("146").build();
        QuestionResponseEntity questionResponseEntity2 = QuestionResponseEntity.builder().instance(0).questionCode("143").build();
        //Add to list
        testResponses.add(questionResponseEntity);
        testResponses.add(questionResponseEntity2);

        List<String> testListOne = new ArrayList<>();
        testListOne.add(questionResponseEntity.getQuestionCode());
        testListOne.add(questionResponseEntity2.getQuestionCode());
        Map<Integer, List<String>> outputMap = new HashMap<>();
        outputMap.put(0, testListOne);

        assertEquals(new Helpers().placeIntoMap(testResponses), outputMap);
    }

    @Test
    void placeIntoMap_threeInstances_TwoQuestionCode() {
        // List that gets passed to helper function
        List<QuestionResponseEntity> testResponses = new ArrayList<>();
        // Instantiate entities
        QuestionResponseEntity questionResponseEntity = QuestionResponseEntity.builder().instance(0).questionCode("146").build();
        QuestionResponseEntity questionResponseEntity2 = QuestionResponseEntity.builder().instance(1).questionCode("143").build();
        QuestionResponseEntity questionResponseEntity3 = QuestionResponseEntity.builder().instance(2).questionCode("143").build();
        //Add to list
        testResponses.add(questionResponseEntity);
        testResponses.add(questionResponseEntity2);
        testResponses.add(questionResponseEntity3);

        List<String> testListOne = new ArrayList<>();
        testListOne.add(questionResponseEntity.getQuestionCode());

        List<String> testListTwo = new ArrayList<>();
        testListTwo.add(questionResponseEntity2.getQuestionCode());

        List<String> testListThree = new ArrayList<>();
        testListThree.add(questionResponseEntity3.getQuestionCode());

        Map<Integer, List<String>> outputMap = new HashMap<>();
        outputMap.put(0, testListOne);
        outputMap.put(1, testListTwo);
        outputMap.put(2, testListThree);

        assertEquals(new Helpers().placeIntoMap(testResponses), outputMap);
    }

    @Test
    void placeIntoMap_threeInstances_SixQuestionCode() {
        // List that gets passed to helper function
        List<QuestionResponseEntity> testResponses = new ArrayList<>();
        // Instantiate entities
        QuestionResponseEntity questionResponseEntity = QuestionResponseEntity.builder().instance(0).questionCode("146").build();
        QuestionResponseEntity questionResponseEntity2 = QuestionResponseEntity.builder().instance(1).questionCode("143").build();
        QuestionResponseEntity questionResponseEntity3 = QuestionResponseEntity.builder().instance(2).questionCode("143").build();
        QuestionResponseEntity questionResponseEntity4 = QuestionResponseEntity.builder().instance(2).questionCode("001").build();
        QuestionResponseEntity questionResponseEntity5 = QuestionResponseEntity.builder().instance(2).questionCode("002").build();
        //Add to list
        testResponses.add(questionResponseEntity);
        testResponses.add(questionResponseEntity2);
        testResponses.add(questionResponseEntity3);
        testResponses.add(questionResponseEntity4);
        testResponses.add(questionResponseEntity5);

        List<String> testListOne = new ArrayList<>();
        testListOne.add(questionResponseEntity.getQuestionCode());

        List<String> testListTwo = new ArrayList<>();
        testListTwo.add(questionResponseEntity2.getQuestionCode());

        List<String> testListThree = new ArrayList<>();
        testListThree.add(questionResponseEntity3.getQuestionCode());

        testListThree.add(questionResponseEntity4.getQuestionCode());
        testListThree.add(questionResponseEntity5.getQuestionCode());

        Map<Integer, List<String>> outputMap = new HashMap<>();
        outputMap.put(0, testListOne);
        outputMap.put(1, testListTwo);
        outputMap.put(2, testListThree);
        outputMap.put(2, testListThree);
        outputMap.put(2, testListThree);

        assertEquals(new Helpers().placeIntoMap(testResponses), outputMap);
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
    */
}
package uk.gov.ons.collection.utilities;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.QuestionResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HelpersTest {

    @Test
    void twoInstances_OneQuestionCode() {
        Helpers helpers = new Helpers();
        List<QuestionResponseEntity> testResponses = new ArrayList<>();
        QuestionResponseEntity questionResponseEntity = new QuestionResponseEntity().builder().instance(3).questionCode("143").build();
        QuestionResponseEntity questionResponseEntity_2 = new QuestionResponseEntity().builder().instance(0).questionCode("143").build();
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
    void oneInstances_TwoQuestionCode() {
        Helpers helpers = new Helpers();
        // List that gets passed to helper function
        List<QuestionResponseEntity> testResponses = new ArrayList<>();
        // Instantiate entities
        QuestionResponseEntity questionResponseEntity = new QuestionResponseEntity().builder().instance(0).questionCode("146").build();
        QuestionResponseEntity questionResponseEntity_2 = new QuestionResponseEntity().builder().instance(0).questionCode("143").build();
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
    void threeInstances_TwoQuestionCode() {
        Helpers helpers = new Helpers();
        // List that gets passed to helper function
        List<QuestionResponseEntity> testResponses = new ArrayList<>();
        // Instantiate entities
        QuestionResponseEntity questionResponseEntity = new QuestionResponseEntity().builder().instance(0).questionCode("146").build();
        QuestionResponseEntity questionResponseEntity_2 = new QuestionResponseEntity().builder().instance(1).questionCode("143").build();
        QuestionResponseEntity questionResponseEntity_3 = new QuestionResponseEntity().builder().instance(2).questionCode("143").build();
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
    void threeInstances_SixQuestionCode() {
        Helpers helpers = new Helpers();
        // List that gets passed to helper function
        List<QuestionResponseEntity> testResponses = new ArrayList<>();
        // Instantiate entities
        QuestionResponseEntity questionResponseEntity = new QuestionResponseEntity().builder().instance(0).questionCode("146").build();
        QuestionResponseEntity questionResponseEntity_2 = new QuestionResponseEntity().builder().instance(1).questionCode("143").build();
        QuestionResponseEntity questionResponseEntity_3 = new QuestionResponseEntity().builder().instance(2).questionCode("143").build();
        QuestionResponseEntity questionResponseEntity_4 = new QuestionResponseEntity().builder().instance(2).questionCode("001").build();
        QuestionResponseEntity questionResponseEntity_5 = new QuestionResponseEntity().builder().instance(2).questionCode("002").build();
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
}
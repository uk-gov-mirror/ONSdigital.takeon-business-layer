package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.SurveyTask;
import uk.gov.ons.collection.exception.InvalidJsonException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SurveyTaskTest {

    private static final String MISSING_QUESTIONS_CHECK = "Missing Questions Check";

    @Test
    void verify_SurveyTask_inValidJson_throwsException() {

        var surveyTaskJson = "{\"data\":{\"allTasks\":{\"nods\":[]}}}";
        assertThrows(InvalidJsonException.class, () -> new SurveyTask("023",
                MISSING_QUESTIONS_CHECK).processSurveyTaskInfo(surveyTaskJson));
    }

    @Test
    void verify_SurveyTask_Missing_Question_Check_ForBMI() {

        SurveyTask surveyTaskObject = new SurveyTask("066",MISSING_QUESTIONS_CHECK);
        surveyTaskObject.buildSurveyTaskQuery();
        String surveyTaskResponseBMI = "{\"data\":{\"allTasks\":{\"nodes\":[{\"taskname\":\"Missing Questions Check\",\"taskdescription\":\"Ingest Check\",\"enabledbydefault\":true,\"surveytasksByTaskname\":{\"nodes\":[]}}]}}}";

        try {
            surveyTaskObject.processSurveyTaskInfo(surveyTaskResponseBMI);
            assertTrue(surveyTaskObject.isPerformTask(), "Test");
            System.out.println("Missing Question check to be performed for BMI"+surveyTaskObject.isPerformTask());
        } catch(Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void verify_SurveyTask_Missing_Question_Check_ForRSI() {

        SurveyTask surveyTaskObject = new SurveyTask("023", MISSING_QUESTIONS_CHECK);
        String surveyTaskResponseRSI = "{\"data\":{\"allTasks\":{\"nodes\":[{\"taskname\":\"Missing Questions Check\",\"taskdescription\":\"Ingest Check\"," +
                "\"enabledbydefault\":true,\"surveytasksByTaskname\":{\"nodes\":[{\"survey\":\"023\"," +
                "\"taskname\":\"Missing Questions Check\",\"enabled\":false}]}}]}}}";
        try {
            surveyTaskObject.processSurveyTaskInfo(surveyTaskResponseRSI);
            assertFalse(surveyTaskObject.isPerformTask());
            System.out.println("Missing Question check to be performed for RSI"+surveyTaskObject.isPerformTask());
        } catch(Exception e) {
            assertTrue(false);
        }
    }

    @Test
    void verify_SurveyTask_GraphQL_Query() {

        SurveyTask surveyTaskObject = new SurveyTask("066", MISSING_QUESTIONS_CHECK);
        String expectedGraphQLQueryBMI = "{\"query\":\"query surveytaskdetails {allTasks(condition: {taskname: \\\"Missing Questions Check\\\"})" +
                "{nodes {taskname taskdescription enabledbydefault surveytasksByTaskname(condition: " +
                "{survey: \\\"066\\\", taskname: \\\"Missing Questions Check\\\"})" +
                "{nodes {survey taskname enabled}}}}}\"}";
        String actualGraphQLQueryBMI = surveyTaskObject.buildSurveyTaskQuery();
        assertEquals(expectedGraphQLQueryBMI, actualGraphQLQueryBMI);
    }


}

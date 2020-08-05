package uk.gov.ons.collection.test;

import org.junit.jupiter.api.Test;
import uk.gov.ons.collection.entity.SurveyTask;
import uk.gov.ons.collection.exception.InvalidJsonException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class SurveyTaskTest {

    @Test
    void verify_SurveyTask_inValidJson_throwsException() {

        var contributorJson1 = "{\"data\":{\"allTasks\":{\"nods\":[]}}}";
        assertThrows(InvalidJsonException.class, () -> new SurveyTask("023","Missing Questions Check").processSurveyTaskInfo(contributorJson1));
    }
}

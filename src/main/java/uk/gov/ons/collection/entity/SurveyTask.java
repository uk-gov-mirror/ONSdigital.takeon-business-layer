package uk.gov.ons.collection.entity;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import lombok.extern.log4j.Log4j2;
import uk.gov.ons.collection.exception.InvalidJsonException;



@Log4j2
@Getter
@Setter
public class SurveyTask {

    private String survey;
    private String taskName;
    private boolean performTask = true;

    public SurveyTask(String survey, String taskName)  {
        this.survey = survey;
        this.taskName = taskName;
    }

    public String buildSurveyTaskQuery()  {
        StringBuilder surveyTaskQuery = new StringBuilder();

        surveyTaskQuery.append("{\"query\":\"query surveytaskdetails {");
        surveyTaskQuery.append("allTasks(condition: {");
        surveyTaskQuery.append("taskname: \\\"");
        surveyTaskQuery.append(this.taskName + "\\\"");
        surveyTaskQuery.append("}){nodes {taskname taskdescription enabledbydefault ");
        surveyTaskQuery.append("surveytasksByTaskname(condition: {");
        surveyTaskQuery.append("survey: \\\"");
        surveyTaskQuery.append(this.survey + "\\\"");
        surveyTaskQuery.append(", ");
        surveyTaskQuery.append("taskname: \\\""    + this.taskName    + "\\\"");
        surveyTaskQuery.append("}){nodes {survey taskname enabled}}}");
        surveyTaskQuery.append("}}\"}");
        log.info("Survey Task query {}", surveyTaskQuery.toString());

        return surveyTaskQuery.toString();
    }

    public void processSurveyTaskInfo(String surveyTaskResponse) throws InvalidJsonException {


        JSONObject surveyTaskResponseObject;

        try {
            surveyTaskResponseObject = new JSONObject(surveyTaskResponse);
            JSONArray taskArrayObject = surveyTaskResponseObject.getJSONObject("data")
                    .getJSONObject("allTasks").getJSONArray("nodes");
            if (taskArrayObject.length() > 0) {
                boolean enabledByDefault = taskArrayObject.getJSONObject(0).getBoolean("enabledbydefault");
                log.info("Task table enabledByDefault Flag {}", enabledByDefault);
                JSONArray taskSurveyArrayObject = taskArrayObject.getJSONObject(0)
                        .getJSONObject("surveytasksByTaskname").getJSONArray("nodes");
                if (enabledByDefault && taskSurveyArrayObject.length() > 0) {
                    this.performTask = taskSurveyArrayObject.getJSONObject(0).getBoolean("enabled");
                    log.info("Perform Task Flag {}", this.performTask);
                }
            }

        } catch (JSONException e) {
            log.error("Either Survey Task not found or Invalid JSON from Survey Task Output " + e);
            throw new InvalidJsonException("Either Survey Task not found or Invalid JSON from Survey Task Output: "
                    + surveyTaskResponse, e);
        }

    }



}

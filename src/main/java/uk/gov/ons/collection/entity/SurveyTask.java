package uk.gov.ons.collection.entity;

import lombok.Getter;
import lombok.Setter;
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
    private boolean enabledByDefault;
    private boolean taskEnabled;

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



}

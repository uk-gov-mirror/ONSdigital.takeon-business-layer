package uk.gov.ons.collection.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.StringJoiner;

import org.json.JSONException;
import org.json.JSONObject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import uk.gov.ons.collection.exception.InvalidJsonException;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ValidationOutputs {

    private JSONObject outputs;
    private final String arrayAttribute = "validation_outputs";
    private final String qlDeleteQuery = "mutation deleteOutput($period: String!, $reference: String!, $survey: String!){deleteoutput(input: {reference: $reference, period: $period, survey: $survey}){clientMutationId}}";
    private final Timestamp time = new Timestamp(new Date().getTime());

    public ValidationOutputs(String jsonString) throws InvalidJsonException {
        try {
            outputs = new JSONObject(jsonString);
        }
        catch (JSONException err) {
            throw new InvalidJsonException("Given string could not be converted/processed: " + jsonString, err);
        }
    }

    public String buildDeleteOutputQuery() throws InvalidJsonException {
        var queryJson = new StringBuilder();
        queryJson.append("{\"query\": \""); 
        queryJson.append(qlDeleteQuery); 
        queryJson.append("\",\"variables\":{\"reference\": \"" + GetReference() + "\",\"period\": \"" + GetPeriod() + "\",\"survey\": \"" + GetSurvey() + "\"}}");
        return queryJson.toString();
    }

    public String buildInsertByArrayQuery() throws InvalidJsonException {
        var queryJson = new StringBuilder();
        queryJson.append("{\"query\": \"mutation insertOutputArray{insertvalidationoutputbyarray(input: {arg0:");
        queryJson.append("[" + getValidationOutputs() + "]");
        queryJson.append("}){clientMutationId}}\"}");
        return queryJson.toString();
    }

    // Loop through the given validation output array json and convert it into a graphQL compatable format
    public String getValidationOutputs() throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");
        try {
            var outputArray = outputs.getJSONArray(arrayAttribute);
            for (int i=0; i < outputArray.length(); i++) {
                joiner.add("{" + extractValidationOutputRow(i) + "}" );
            }
            return joiner.toString();
        }
        catch (Exception err) {
            throw new InvalidJsonException("Error processing validation output json structure: " + outputs, err);
        }
    }

    // Convert a row for the given index and provide it in graphQL desired format
    public String extractValidationOutputRow(int index) throws InvalidJsonException {
        StringJoiner joiner = new StringJoiner(",");
        try {
            var outputRow = outputs.getJSONArray(arrayAttribute).getJSONObject(index);
            joiner.add("reference: \"" + outputRow.getString("reference") + "\"");   
            joiner.add("period: \"" + outputRow.getString("period") + "\"");
            joiner.add("survey: \"" + outputRow.getString("survey") + "\"");
            joiner.add("formula: \"" + outputRow.getString("formula") + "\"");
            joiner.add("validationid: \"" + outputRow.getInt("validationid") + "\"");
            joiner.add("instance: \"" + outputRow.getInt("instance") + "\",");
            joiner.add("triggered: \"" + outputRow.getBoolean("triggered") + "\"");
            joiner.add("createdby: \"fisdba\"");
            joiner.add("createddate: \"" + time.toString() + "\"");
            return joiner.toString();
        }
        catch (Exception err) {
            throw new InvalidJsonException("Error processing validation output json structure: " + outputs, err);
        }
    }


    public String GetReference() throws InvalidJsonException {
        try {   
            return outputs.getJSONArray(arrayAttribute).getJSONObject(0).getString("reference");
        }
        catch (Exception err) {
            throw new InvalidJsonException("Given JSON did not contain reference in the expected location: " + outputs, err);
        } 
    }

    public String GetPeriod() throws InvalidJsonException {
        try {   
            return outputs.getJSONArray(arrayAttribute).getJSONObject(0).getString("period");
        }
        catch (Exception err) {
            throw new InvalidJsonException("Given JSON did not contain period in the expected location: " + outputs, err);
        } 
    }

    public String GetSurvey() throws InvalidJsonException {
        try {   
            return outputs.getJSONArray(arrayAttribute).getJSONObject(0).getString("survey");
        }
        catch (Exception err) {
            throw new InvalidJsonException("Given JSON did not contain survey in the expected location: " + outputs, err);
        } 
    }

    public String GetStatusText() throws InvalidJsonException {
        if (isTriggeredFound()) {
            return "Validations Triggered";
        }
        return "Clear";
    }

    public boolean isTriggeredFound() throws InvalidJsonException {
        try {    
            var outputArray = outputs.getJSONArray(arrayAttribute);
            for (int i=0; i < outputArray.length(); i++) {
                if(outputArray.getJSONObject(i).getBoolean("triggered")){
                    return true;
                }
            }
        }
        catch (Exception err) {
            throw new InvalidJsonException("Given JSON did not contain triggered in the expected location(s): " + outputs, err);
        } 
        return false;
    }

}



// mutation insertOutputArray {
//   insertvalidationoutputbyarray(input: {arg0: [
//     {reference: "123", period: "123", survey: "123", formula: "123", validationid: "12", instance:"0",
//     triggered: true, createdby:"testuser",createddate:"2016-06-22 22:10:25-04"},
//     {reference: "123", period: "123", survey: "123", formula: "123", validationid: "12", instance:"0",
//     triggered: true, createdby:"testuser",createddate:"2016-06-22 22:10:25-04"}
//   ]}
//   ) {
//     clientMutationId
//   }
// }


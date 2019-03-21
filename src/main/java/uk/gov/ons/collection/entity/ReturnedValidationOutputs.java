package uk.gov.ons.collection.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ReturnedValidationOutputs {
    private String triggered;
    private String valueFormula;
    private Map<String, String> metaData;
    private String error;

    public ReturnedValidationOutputs() {}

    private String convertMapToJsonString(Map<String, String> mapToConvert){
        StringBuilder builder = new StringBuilder();
        builder.append('{');
        for(String key: mapToConvert.keySet()){
           builder.append( "\"" + key + "\":" + "\"" + mapToConvert.get(key) + "\",");
        }
        builder.reverse().replace(0,1,"").reverse().append('}');
        return  builder.toString();
    }

    @Override
    public String toString() {
        return "{" +
                "\"triggered\":\"" + triggered + '\"' +
                ", \"valueFormula\":\"" + valueFormula + '\"' +
                ", \"metaData\":" + convertMapToJsonString(metaData) +
                ", \"error\":\"" + error + '\"' +
                '}';
    }
}



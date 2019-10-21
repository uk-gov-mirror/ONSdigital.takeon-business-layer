package uk.gov.ons.collection.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UrlParameterBuilder {

    public static Map<String, String> filter(Map<String, String> parameters, List<String> allowedParameters) {
        Map<String,String> safeParameters = (parameters == null) ? new HashMap<>() : new HashMap<>(parameters);
        List<String> safeAllowedParameters = (allowedParameters == null) ? new ArrayList<>() : allowedParameters;
        safeParameters.keySet().retainAll(safeAllowedParameters);
        return safeParameters;
    }

    public static String buildParameterString(Map<String, String> parameterList) {
        Map<String,String> safeParameterList = (parameterList == null) ? new HashMap<>() :  parameterList;
        StringBuilder searchParameters = new StringBuilder();
        safeParameterList.forEach((key,value) -> searchParameters.append(key + "=" + value + ";"));
        return searchParameters.toString();
    }

}
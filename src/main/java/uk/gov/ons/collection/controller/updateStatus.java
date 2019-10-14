package uk.gov.ons.collection.controller;

import org.json.JSONArray;
import org.json.JSONObject;

public class updateStatus {

    private String response = "{\"validationoutput\":[{\"formula\":\"\", \"reference\":\"\",\"period\":\"\",\"survey\":\"\",\"triggered\":\"true\",\"validationid\":\"\",\"bpmid\":\"\"}]}";
    private JSONObject jsonResponse;

    public boolean determineStatus(String response) {
        JSONObject jsonResponse =  new JSONObject(response);
        JSONArray array = jsonResponse.getJSONArray("validationoutput");
        for (int i=0; i < array.length(); i++) {
            Object triggered = array.getJSONObject(i).get("triggered");
            System.out.println(triggered);
            if(triggered.equals("true")){
                System.out.println();
                return true;
            }
        }
        return false;
    }


}
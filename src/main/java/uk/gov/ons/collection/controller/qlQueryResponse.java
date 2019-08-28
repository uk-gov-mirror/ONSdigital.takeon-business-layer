package uk.gov.ons.collection.controller;

public class qlQueryResponse {

    private String response;

    public qlQueryResponse(String response) {
        this.response = response;
    }
    public String filter(){
        return "test";
    }
}

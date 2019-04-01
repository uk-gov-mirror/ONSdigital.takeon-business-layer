package uk.gov.ons.collection.service;

import uk.gov.ons.collection.entity.ReturnedValidationOutputs;
import uk.gov.ons.collection.entity.Status;

public class CheckIfAnyTriggered {
    public Status checkIfTriggered(Iterable<ReturnedValidationOutputs> outputsToCheck){
        Status status = new Status();
        for(ReturnedValidationOutputs element: outputsToCheck){
            if(element.getTriggered().equals("true")){
                status.setStatus("ValidationTriggered");
                break;
            }
        }
        if(isNull(status.getStatus())){
            status.setStatus("Form Clear");
        }
        return status;
    }

    private boolean isNull(String stringToCheck){
        if(stringToCheck == null){
            return true;
        }
        return false;
    }
}

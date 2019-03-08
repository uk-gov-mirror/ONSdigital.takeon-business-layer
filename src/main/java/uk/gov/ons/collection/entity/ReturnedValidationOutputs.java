package uk.gov.ons.collection.entity;

public class ReturnedValidationOutputs {
    private String triggered;

    public String isTriggered() {
        return triggered;
    }

    public void setTriggered(String triggered) {
        this.triggered = triggered;
    }

    @Override
    public String toString() {
        return "ReturnedValidationOutputs{" +
                "triggered='" + triggered + '\'' +
                '}';
    }
}

package uk.gov.ons.collection.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationData {

    private Integer validationOutputId;
    private boolean overridden;
    private String lastupdatedBy;
    private String lastupdatedDate;


    @Override
    public String toString() {
        return "ValidationOutData{" +
                "validationOutputId=" + validationOutputId +
                ", overridden=" + overridden +
                ", lastupdatedBy='" + lastupdatedBy + '\'' +
                ", lastupdatedDate='" + lastupdatedDate + '\'' +
                '}';
    }


}

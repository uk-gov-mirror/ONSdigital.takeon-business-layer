package uk.gov.ons.collection.entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationOutputData {

    private String reference;
    private String period;
    private String survey;
    private boolean triggered;
    private String formula;
    private Integer validationId;
    private Integer validationOutputId;
    private Integer instance;
    private boolean overridden;
    private String lastupdatedBy;
    private String lastupdatedDate;
    private String createdBy;
    private String createdDate;

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

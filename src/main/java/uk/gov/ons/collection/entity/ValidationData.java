package uk.gov.ons.collection.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValidationData {

    private Integer validationOutputId;
    private boolean overridden;
    private String overriddenBy;
    private String overriddenDate;

    @Override
    public String toString() {
        return "ValidationOutData{" +
                "validationOutputId=" + validationOutputId +
                ", overridden=" + overridden +
                ", overriddenBy='" + overriddenBy + '\'' +
                ", overriddenDate='" + overriddenDate + '\'' +
                '}';
    }


}

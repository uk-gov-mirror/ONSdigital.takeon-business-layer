package uk.gov.ons.collection.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class Status {
    private String status;

    @Override
    public String toString() {
        return "{" +
                "status:'" + status + '\'' +
                '}';
    }
}

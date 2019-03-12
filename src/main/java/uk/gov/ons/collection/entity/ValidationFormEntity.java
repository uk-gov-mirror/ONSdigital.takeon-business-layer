package uk.gov.ons.collection.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "validationform", schema = "dev01")
@ApiModel(value = "ValidationForm", description = "A ValidationForm entity, maps ValidationForm table to object")
public class ValidationFormEntity implements ValidationEntity {

    @JsonManagedReference
    @OneToOne
    @JoinColumn(updatable = false, insertable = false, name = "validationcode", referencedColumnName = "validationrule")
    private ValidationRuleEntity validationRuleEntity;

    @JsonManagedReference
    @OneToOne
    @JoinColumn(updatable = false, insertable = false, name = "validationid", referencedColumnName = "validationid")
    private ValidationParameterEntity validationParameterEntity;

    @JsonBackReference
    @OneToMany
    @JoinColumn(updatable = false, insertable = false, name = "validationid", referencedColumnName = "validationid")
    private ValidationOutputEntity validationOutputEntity;

    @Id
    @Column(name = "validationid", nullable = false)
    private @NonNull Integer validationid;

    @Column(name = "formid", nullable = false)
    private @NonNull Integer formID;

    @Column(name = "validationcode", length = 16, nullable = false)
    private @NonNull String validationCode;

    @Column(name = "questioncode", length = 4, nullable = false)
    private @NonNull String questionCode;

    @Column(name = "precalculationformula", length = 256, nullable = false)
    private @NonNull String preCalculationFormula;

    @Column(name = "severity", length = 16, nullable = false)
    private @NonNull String severity;

    @Column(name = "createdby", length = 16, nullable = false)
    private @NonNull String createdBy;

    @Column(name = "createddate", length = 7, nullable = false)
    private @NonNull Timestamp createdDate;

    @Column(name = "lastupdatedby", length = 16)
    private String lastUpdatedBy;

    @Column(name = "lastupdateddate", length = 7)
    private Timestamp lastUpdatedDate;

    @JsonIgnore
    private String isTriggered;

    @JsonIgnore
    private String currentResponse;

    @JsonIgnore
    private String payload = "{\"value\": " +"\"" + getCurrentResponse()+"\"" + "}";

}

package uk.gov.ons.collection.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.annotations.ApiModel;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "validationrule", schema = "dev01")
@ApiModel(value = "ValidationRule", description = "A ValidationRule entity, maps ValidationRule table to object")
public class ValidationRuleEntity {


    @JsonBackReference
    @OneToOne
    @JoinColumn(updatable = false, insertable = false, name = "validationid", referencedColumnName = "validationid")
    private ValidationFormEntity validationFormEntity;

    @Id
    @Column(name = "validationrule", length = 16, nullable = false)
    private @NonNull String validationRule;

    @Column(name = "name", length = 32, nullable = false)
    private @NonNull String name;

    @Column(name = "description", length = 128, nullable = false)
    private @NonNull String description;

    @Column(name = "createdby", length = 16, nullable = false)
    private @NonNull String createdBy;

    @Column(name = "createddate", length = 7, nullable = false)
    private @NonNull Timestamp createdDate;

    @Column(name = "lastupdatedby", length = 16)
    private String lastUpdatedBy;

    @Column(name = "lastupdateddate", length = 7)
    private Timestamp lastUpdatedDate;

}

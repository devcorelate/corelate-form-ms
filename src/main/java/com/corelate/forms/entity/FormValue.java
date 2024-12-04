package com.corelate.forms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class FormValue extends BaseEntity{

    @Id
    private String fieldValueId;

    private String formFieldId;

    private String formFieldValue;

    private String status;

}

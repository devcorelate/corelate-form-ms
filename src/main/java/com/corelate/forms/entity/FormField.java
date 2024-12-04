package com.corelate.forms.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class FormField extends BaseEntity{

    @Id
    private String formFieldId;

    private String formId;

    private String fieldType;

    private Long fieldOrder;

}

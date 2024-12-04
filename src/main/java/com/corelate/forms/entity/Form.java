package com.corelate.forms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class Form extends BaseEntity{

    @Id
    private String formId;

    private String processId;

    private String formName;

    private String formDescription;

    @Column(name = "communication_sw")
    private Boolean communicationSw;

}

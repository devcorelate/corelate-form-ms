package com.corelate.forms.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter @Setter @ToString @AllArgsConstructor @NoArgsConstructor
public class FormSchema {

    @Id
    private String schemaId;

    private String type;

    private int schemaVersion;

    private String formId;

}

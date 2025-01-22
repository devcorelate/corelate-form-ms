package com.corelate.forms.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SchemaComponent {

    @Id
    private String componentId;

    private String schemaId;

    private String row;

    private String columns;

    private String label;

    private String key;

    private String type;

    private boolean isValidated;
}
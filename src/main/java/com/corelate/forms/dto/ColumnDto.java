package com.corelate.forms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColumnDto {
    private String name;         // Column name
    private String type;         // Data type (e.g., "VARCHAR(255)", "INTEGER")
    private Boolean isPrimaryKey; // Is it a primary key?
}


package com.corelate.forms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableSchemaDto {
    private String tableName;
    private List<ColumnDto> columns;
}
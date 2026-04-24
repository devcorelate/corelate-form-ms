package com.corelate.forms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DataSourceConfig {

    @Id
    private String id;

    private String formId;

    private String schemaId;

    private String componentId;

    private String datasourceName;

    private String workflowId;

    private String workflowName;

    private String datasourceLabel;

    private String tableName;

    private String labelColumn;

    private String valueColumn;
}

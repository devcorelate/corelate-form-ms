package com.corelate.forms.dto;

import lombok.Data;

import java.util.List;

@Data
public class FormSchemaDto {
    private String id;
    private String type;
    private int schemaVersion;
    private String templateId;
    private List<Component> components;

    @Data
    public static class Component {
        private String label;
        private String key;
        private String type;
        private Validate validate;
        private DataSourceConfig dataSourceConfig;
        private String id;
        private Layout layout;

        @Data
        public static class DataSourceConfig {
            private String datasourceName;
            private String workflowId;
            private String workflowName;
            private String datasourceLabel;
            private String table;
            private String labelColumn;
            private String valueColumn;
        }

        @Data
        public static class Validate {
            private boolean required;
        }

        @Data
        public static class Layout {
            private String row;
            private String columns;
        }
    }
}

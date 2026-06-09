package com.corelate.forms.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
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
        private List<OptionValue> values;
        private List<Component> components;
        private Validate validate;
        @JsonAlias({"datasourceConfig", "dataSource"})
        private DataSourceConfig dataSourceConfig;
        private String optionSource;
        private String id;
        private Layout layout;

        @Data
        public static class OptionValue {
            private String label;
            private String value;
        }

        @Data
        public static class DataSourceConfig {
            @JsonProperty("dataSourceName")
            @JsonAlias({"datasourceName"})
            private String dataSourceName;
            private String workflowId;
            private String workflowName;
            @JsonProperty("dataSourceLabel")
            @JsonAlias({"datasourceLabel"})
            private String dataSourceLabel;
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

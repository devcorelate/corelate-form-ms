package com.corelate.forms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class FormFieldLabelsResponseDto {
    private String formId;
    private List<FieldLabelDto> labels;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldLabelDto {
        private String key;
        private String label;
    }
}

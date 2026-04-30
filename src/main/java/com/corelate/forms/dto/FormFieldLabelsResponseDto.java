package com.corelate.forms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FormFieldLabelsResponseDto {
    private String formId;
    private List<String> labels;
}

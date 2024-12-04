package com.corelate.forms.dto;

import lombok.Data;

import java.util.List;

@Data
public class FormDto {

    private String formId;

    private String formName;

    private String processId;

    private String formDescription;

    private List<FormFieldDto> fields;
}
// SAMPLE DTO
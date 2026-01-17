package com.corelate.forms.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class FormDto {

    private String formId;

    private String formName;

    private String formDescription;

    private LocalDateTime updatedDate;

    private LocalDateTime createdDate;

    private String createdBy;

    private String createdByEmail;

    private FormSchemaDto formSchemaDto;

}
// SAMPLE DTO
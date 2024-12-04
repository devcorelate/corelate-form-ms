package com.corelate.forms.dto;

import lombok.Data;

@Data
public class FormFieldDto {

    private String fieldId;

    private String fieldName;

    private String fieldType;

    private Long fieldOrder;

    private String fieldValue;

}

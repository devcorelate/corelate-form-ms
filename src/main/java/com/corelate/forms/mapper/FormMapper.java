package com.corelate.forms.mapper;


import com.corelate.forms.dto.FormDto;
import com.corelate.forms.dto.FormSelectionDto;
import com.corelate.forms.entity.Form;

public class FormMapper {

    public static Form mapToForm(FormDto formDto, Form form) {
        form.setFormName(formDto.getFormName());
        form.setFormDescription(formDto.getFormDescription());
        form.setFormId(formDto.getFormId());
        return form;
    }

    public static FormDto mapToFormDto(Form form, FormDto formDto) {
        formDto.setFormName(form.getFormName());
        formDto.setFormDescription(form.getFormDescription());
        formDto.setFormId(form.getFormId());

        return formDto;
    }

    public static FormSelectionDto mapToFormSelectionDto(Form form, FormSelectionDto formDto) {
        formDto.setFormName(form.getFormName());
        formDto.setFormDescription(form.getFormDescription());
        formDto.setFormId(form.getFormId());

        return formDto;
    }
}

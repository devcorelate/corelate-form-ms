package com.corelate.forms.mapper;


import com.corelate.forms.dto.FormDto;
import com.corelate.forms.entity.Form;

public class FormMapper {

    public static Form mapToForm(FormDto formDto, Form form) {

        form.setFormName(formDto.getFormName());
        form.setFormDescription(formDto.getFormDescription());
        form.setFormId(formDto.getFormId());
        form.setProcessId(formDto.getProcessId());

        return form;
    }

    public static FormDto mapToFormDto(Form form, FormDto formDto) {
        formDto.setFormName(form.getFormName());
        formDto.setFormDescription(form.getFormDescription());
        formDto.setFormId(form.getFormId());
        formDto.setProcessId(form.getProcessId());

        return formDto;
    }
}

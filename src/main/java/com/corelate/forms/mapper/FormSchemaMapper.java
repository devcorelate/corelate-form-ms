package com.corelate.forms.mapper;


import com.corelate.forms.dto.FormDto;
import com.corelate.forms.dto.FormSchemaDto;
import com.corelate.forms.entity.Form;
import com.corelate.forms.entity.FormSchema;
import com.corelate.forms.entity.SchemaComponent;
import com.corelate.forms.repository.FormSchemaRepository;
import com.corelate.forms.repository.SchemaComponentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FormSchemaMapper {



    public static FormSchema mapToFormSchema(FormDto formDto, FormSchema formSchema, SchemaComponentRepository schemaComponentRepository) {

        formSchema.setFormId(formDto.getFormId());
        formSchema.setSchemaId(formDto.getFormSchemaDto().getId());
        formSchema.setSchemaVersion(formDto.getFormSchemaDto().getSchemaVersion());
        formSchema.setType(formDto.getFormSchemaDto().getType());

        for (FormSchemaDto.Component component : formDto.getFormSchemaDto().getComponents()) {

            SchemaComponent mComponent = schemaComponentRepository.findByComponentId(component.getId())
                    .orElseGet(SchemaComponent::new);
            mComponent.setComponentId(component.getId());
            mComponent.setSchemaId(formDto.getFormSchemaDto().getId());
            mComponent.setLabel(component.getLabel());
            mComponent.setType(component.getType());
            mComponent.setRow(component.getLayout().getRow());
            mComponent.setKey(component.getKey());
            mComponent.setValidated(Optional.ofNullable(component.getValidate())
                    .map(FormSchemaDto.Component.Validate::isRequired)
                    .orElse(false));
            mComponent.setColumns(component.getLayout().getColumns());
            schemaComponentRepository.save(mComponent);
        }

        return formSchema;
    }

    public static FormDto mapToFormDtoAndSchema(Form form, FormSchema formSchema, SchemaComponentRepository schemaComponentRepository) {
        FormDto formDto = new FormDto();
        formDto.setFormId(form.getFormId());
        formDto.setFormDescription(form.getFormDescription());
        formDto.setFormName(form.getFormName());
        formDto.setCreatedBy(form.getCreatedBy());
        formDto.setCreatedByEmail(form.getCreatedByEmail());
        formDto.setCreatedDate(form.getCreatedAt());
        formDto.setUpdatedDate(form.getUpdatedAt());
        FormSchemaDto formSchemaDto = new FormSchemaDto();
        if(formSchema != null)  {
            formSchemaDto.setId(formSchema.getSchemaId());
            formSchemaDto.setSchemaVersion(formSchema.getSchemaVersion());
            formSchemaDto.setType(formSchema.getType());
            List<SchemaComponent> components = schemaComponentRepository.findAllBySchemaId(formSchema.getSchemaId());
            List<FormSchemaDto.Component> nComponents = new ArrayList<>();
            for (SchemaComponent component : components) {
                FormSchemaDto.Component nComponent = new FormSchemaDto.Component();
                nComponent.setId(component.getComponentId());
                nComponent.setLabel(component.getLabel());
                nComponent.setKey(component.getKey());
                nComponent.setType(component.getType());

                FormSchemaDto.Component.Layout layout = new FormSchemaDto.Component.Layout();

                layout.setColumns(component.getColumns());
                layout.setRow(component.getRow());

                nComponent.setLayout(layout);

                FormSchemaDto.Component.Validate isValidate = new FormSchemaDto.Component.Validate();
                isValidate.setRequired(component.isValidated());
                nComponent.setValidate(isValidate);
                nComponents.add(nComponent);
            }
            formSchemaDto.setComponents(nComponents);
            formDto.setFormSchemaDto(formSchemaDto);
        }
        return formDto;
    }

}

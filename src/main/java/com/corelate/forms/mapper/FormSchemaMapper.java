package com.corelate.forms.mapper;


import com.corelate.forms.dto.FormDto;
import com.corelate.forms.dto.FormSchemaDto;
import com.corelate.forms.entity.DataSourceConfig;
import com.corelate.forms.entity.Form;
import com.corelate.forms.entity.FormSchema;
import com.corelate.forms.entity.SchemaComponent;
import com.corelate.forms.repository.DataSourceConfigRepository;
import com.corelate.forms.repository.SchemaComponentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FormSchemaMapper {

    private static String buildComponentKey(String componentIdentifier, String componentKey) {
        if (componentIdentifier == null || componentIdentifier.isBlank()) {
            return componentKey;
        }

        if (componentKey == null || componentKey.isBlank()) {
            return componentIdentifier;
        }

        String prefix = componentIdentifier + "-";

        // Remove all repeated componentIdentifier prefixes
        while (componentKey.startsWith(prefix)) {
            componentKey = componentKey.substring(prefix.length());
        }

        // Add exactly one componentIdentifier prefix
        return prefix + componentKey;
    }

    public static FormSchema mapToFormSchema(FormDto formDto,
                                             FormSchema formSchema,
                                             SchemaComponentRepository schemaComponentRepository,
                                             DataSourceConfigRepository dataSourceConfigRepository) {

        formSchema.setFormId(formDto.getFormId());
        formSchema.setSchemaId(formDto.getFormSchemaDto().getId());
        formSchema.setSchemaVersion(formDto.getFormSchemaDto().getSchemaVersion());
        formSchema.setType(formDto.getFormSchemaDto().getType());

        for (FormSchemaDto.Component component : formDto.getFormSchemaDto().getComponents()) {
            String componentIdentifier = Optional.ofNullable(component.getId()).orElse(component.getKey());
            String componentKey = component.getKey();

            String combineKey = buildComponentKey(componentIdentifier, componentKey);
            SchemaComponent mComponent = schemaComponentRepository.findByComponentId(componentIdentifier)
                    .orElseGet(SchemaComponent::new);
            mComponent.setComponentId(componentIdentifier);
            mComponent.setSchemaId(formDto.getFormSchemaDto().getId());
            mComponent.setLabel(component.getLabel());
            mComponent.setType(component.getType());
            mComponent.setRow(component.getLayout().getRow());
            mComponent.setKey(combineKey);
            mComponent.setValidated(Optional.ofNullable(component.getValidate())
                    .map(FormSchemaDto.Component.Validate::isRequired)
                    .orElse(false));
            mComponent.setColumns(component.getLayout().getColumns());
            schemaComponentRepository.save(mComponent);

            if (component.getDataSourceConfig() != null) {
                DataSourceConfig dataSourceConfig = dataSourceConfigRepository
                        .findByFormIdAndComponentId(formDto.getFormId(), componentIdentifier)
                        .orElseGet(DataSourceConfig::new);

                dataSourceConfig.setId(Optional.ofNullable(dataSourceConfig.getId()).orElse(UUID.randomUUID().toString()));
                dataSourceConfig.setFormId(formDto.getFormId());
                dataSourceConfig.setSchemaId(formDto.getFormSchemaDto().getId());
                dataSourceConfig.setComponentId(componentIdentifier);
                dataSourceConfig.setDataSourceName(component.getDataSourceConfig().getDataSourceName());
                dataSourceConfig.setWorkflowId(component.getDataSourceConfig().getWorkflowId());
                dataSourceConfig.setWorkflowName(component.getDataSourceConfig().getWorkflowName());
                dataSourceConfig.setDataSourceLabel(component.getDataSourceConfig().getDataSourceLabel());
                dataSourceConfig.setTableName(component.getDataSourceConfig().getTable());
                dataSourceConfig.setLabelColumn(component.getDataSourceConfig().getLabelColumn());
                dataSourceConfig.setValueColumn(component.getDataSourceConfig().getValueColumn());

                dataSourceConfigRepository.save(dataSourceConfig);
            }
        }

        return formSchema;
    }

    public static FormDto mapToFormDtoAndSchema(Form form,
                                                 FormSchema formSchema,
                                                 SchemaComponentRepository schemaComponentRepository,
                                                 DataSourceConfigRepository dataSourceConfigRepository) {
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

            Map<String, DataSourceConfig> configByComponentId = dataSourceConfigRepository.findAllByFormId(form.getFormId())
                    .stream()
                    .collect(Collectors.toMap(DataSourceConfig::getComponentId, Function.identity(), (a, b) -> a));

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

                DataSourceConfig dataSourceConfig = configByComponentId.get(component.getComponentId());
                if (dataSourceConfig != null) {
                    FormSchemaDto.Component.DataSourceConfig dtoConfig = new FormSchemaDto.Component.DataSourceConfig();
                    dtoConfig.setDataSourceName(dataSourceConfig.getDataSourceName());
                    dtoConfig.setWorkflowId(dataSourceConfig.getWorkflowId());
                    dtoConfig.setWorkflowName(dataSourceConfig.getWorkflowName());
                    dtoConfig.setDataSourceLabel(dataSourceConfig.getDataSourceLabel());
                    dtoConfig.setTable(dataSourceConfig.getTableName());
                    dtoConfig.setLabelColumn(dataSourceConfig.getLabelColumn());
                    dtoConfig.setValueColumn(dataSourceConfig.getValueColumn());
                    nComponent.setDataSourceConfig(dtoConfig);
                }

                nComponents.add(nComponent);
            }
            formSchemaDto.setComponents(nComponents);
            formDto.setFormSchemaDto(formSchemaDto);
        }
        return formDto;
    }

}

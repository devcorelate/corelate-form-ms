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



    public static FormSchema mapToFormSchema(FormDto formDto,
                                             FormSchema formSchema,
                                             SchemaComponentRepository schemaComponentRepository,
                                             DataSourceConfigRepository dataSourceConfigRepository) {

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

            if (component.getDataSourceConfig() != null) {
                DataSourceConfig dataSourceConfig = dataSourceConfigRepository
                        .findByFormIdAndComponentId(formDto.getFormId(), component.getId())
                        .orElseGet(DataSourceConfig::new);

                dataSourceConfig.setId(Optional.ofNullable(dataSourceConfig.getId()).orElse(UUID.randomUUID().toString()));
                dataSourceConfig.setFormId(formDto.getFormId());
                dataSourceConfig.setSchemaId(formDto.getFormSchemaDto().getId());
                dataSourceConfig.setComponentId(component.getId());
                dataSourceConfig.setDatasourceName(component.getDataSourceConfig().getDatasourceName());
                dataSourceConfig.setWorkflowId(component.getDataSourceConfig().getWorkflowId());
                dataSourceConfig.setWorkflowName(component.getDataSourceConfig().getWorkflowName());
                dataSourceConfig.setDatasourceLabel(component.getDataSourceConfig().getDatasourceLabel());
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
                    dtoConfig.setDatasourceName(dataSourceConfig.getDatasourceName());
                    dtoConfig.setWorkflowId(dataSourceConfig.getWorkflowId());
                    dtoConfig.setWorkflowName(dataSourceConfig.getWorkflowName());
                    dtoConfig.setDatasourceLabel(dataSourceConfig.getDatasourceLabel());
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

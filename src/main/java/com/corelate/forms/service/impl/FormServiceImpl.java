package com.corelate.forms.service.impl;

import com.corelate.forms.dto.FormDto;
import com.corelate.forms.dto.FormSchemaDto;
import com.corelate.forms.dto.FormSelectionDto;
import com.corelate.forms.entity.Form;
import com.corelate.forms.entity.FormSchema;
import com.corelate.forms.entity.SchemaComponent;
import com.corelate.forms.exeption.ResourceNotFoundException;
import com.corelate.forms.mapper.FormMapper;
import com.corelate.forms.mapper.FormSchemaMapper;
import com.corelate.forms.repository.FormRepository;
import com.corelate.forms.repository.FormSchemaRepository;
import com.corelate.forms.repository.SchemaComponentRepository;
import com.corelate.forms.service.IFormService;
import com.corelate.forms.utility.JwtUtil;
import lombok.AllArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FormServiceImpl implements IFormService {

    private static final Logger log = LoggerFactory.getLogger(FormServiceImpl.class);

    private FormRepository formRepository;
    private FormSchemaRepository formSchemaRepository;
    private SchemaComponentRepository schemaComponentRepository;
    private final StreamBridge streamBridge;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public void createForm(FormDto formDto) {
        Form form = FormMapper.mapToForm(formDto, new Form());

        form.setCreatedAt(LocalDateTime.now());
        form.setCreatedBy("Admin");
        formRepository.save(form);
    }

    @Override
    public FormDto fetchForm(String formId) {
        Form form = formRepository.findByFormId(formId).orElseThrow(
                ()-> new ResourceNotFoundException("Form", "Id", formId)
        );

        FormSchema formSchema = formSchemaRepository.findByFormId(formId).orElse(null);
        if(formSchema == null) {
            return FormMapper.mapToFormDto(form, new FormDto());
        } else {
            return FormSchemaMapper.mapToFormDtoAndSchema(form, formSchema, schemaComponentRepository);
        }

    }

    @Override
    public List<FormDto> fetchAllForm() {
        List<FormDto> formDtos = new ArrayList<>();
        List<Form> forms = formRepository.findAll();

        for (Form form : forms) {

            FormSchema formSchema = formSchemaRepository.findByFormId(form.getFormId()).orElse(null);

//                FormSchemaMapper.mapToFormDtoAndSchema(form, formSchema, schemaComponentRepository);
                formDtos.add(FormSchemaMapper.mapToFormDtoAndSchema(form, formSchema, schemaComponentRepository));


        }

        return formDtos;
    }

    @Override
    public boolean updateForm(FormDto formDto) {
        Form form = formRepository.findByFormId(formDto.getFormId()).orElseThrow(
                ()-> new ResourceNotFoundException("Form", "Id", formDto.getFormId())
        );

        FormSchema formSchema = formSchemaRepository.findBySchemaId(formDto.getFormSchemaDto().getId()).orElseGet(FormSchema::new);

        FormSchemaMapper.mapToFormSchema(formDto, formSchema, schemaComponentRepository);
        formSchemaRepository.save(formSchema);
        FormMapper.mapToForm(formDto, form);
        form.setUpdatedAt(LocalDateTime.now());
        form.setUpdatedBy("Admin");
        formRepository.save(form);
        return true;
    }

    @Override
    public boolean deleteForm(String FormId) {
        return false;
    }

    @Override
    public boolean updateCommunicationStatus(String formId) {
        boolean isUpdated = false;
        if(formId !=null ){
            Form form = formRepository.findById(formId).orElseThrow(
                    () -> new ResourceNotFoundException("Form", "FormID", formId)
            );
            form.setCommunicationSw(true);
            formRepository.save(form);
            isUpdated = true;
        }
        return isUpdated;
    }

    /**
     * @param formId
     * @return
     */
    @Override
    public List<Object> fetchListByForm(String formId) {


        return List.of();
    }

    @Override
    public List<FormSelectionDto> fetchSelections() {
        List<FormSelectionDto> formDtos = new ArrayList<>();
        List<Form> forms = formRepository.findAll();
        for (Form form : forms) {

            formDtos.add(FormMapper.mapToFormSelectionDto(form, new FormSelectionDto()));
        }

        return formDtos;
    }
}

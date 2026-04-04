package com.corelate.forms.service.impl;

import com.corelate.forms.dto.ElementLabelResponseDto;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    public FormDto createForm(FormDto formDto) {
        formDto.setFormId(generateRandomId());
        Form form = FormMapper.mapToForm(formDto, new Form());

        form.setCreatedAt(LocalDateTime.now());
        form.setCreatedBy(formDto.getCreatedBy());
        form.setCreatedByEmail(formDto.getCreatedByEmail());
        formRepository.save(form);
        FormDto updatedFormDto = FormMapper.mapToFormDto(form, new FormDto());

        return updatedFormDto;
    }

    public static String generateRandomId() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder id = new StringBuilder();
        Random random = new Random();

        // Generate 8 random characters
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(characters.length());
            id.append(characters.charAt(index));
        }

        return id.toString();
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
        List<Form> forms = formRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

        for (Form form : forms) {

            FormSchema formSchema = formSchemaRepository.findByFormId(form.getFormId()).orElse(null);

//                FormSchemaMapper.mapToFormDtoAndSchema(form, formSchema, schemaComponentRepository);
            formDtos.add(FormSchemaMapper.mapToFormDtoAndSchema(form, formSchema, schemaComponentRepository));


        }

        return formDtos;
    }

    @Override
    public boolean updateForm(FormDto formDto) {
        Form form = formRepository.findByFormId(formDto.getFormId())
                .orElseThrow(() -> new ResourceNotFoundException("Form", "Id", formDto.getFormId()));

        // 🔥 Remove existing schemas linked to this formId (avoid duplicates)
        List<FormSchema> existingSchemas = formSchemaRepository.findAllByFormId(formDto.getFormId());
        if (!existingSchemas.isEmpty()) {
            formSchemaRepository.deleteAll(existingSchemas);
        }

        // Prepare new schema
        FormSchema formSchema = formSchemaRepository.findBySchemaId(formDto.getFormSchemaDto().getId())
                .orElseGet(FormSchema::new);

        FormSchemaMapper.mapToFormSchema(formDto, formSchema, schemaComponentRepository);
        formSchemaRepository.save(formSchema);

        // Update form metadata
        FormMapper.mapToForm(formDto, form);
        form.setUpdatedAt(LocalDateTime.now());
        form.setUpdatedBy("Admin");
        formRepository.save(form);

        return true;
    }

    @Override
    @Transactional
    public boolean deleteFormSchemas(String FormId) {

        List<FormSchema> existingSchemas = formSchemaRepository.findAllByFormId(FormId);
        if (!existingSchemas.isEmpty()) {
            for (FormSchema formSchema : existingSchemas) {
                schemaComponentRepository.deleteBySchemaId(formSchema.getSchemaId());
            }
            formSchemaRepository.deleteAll(existingSchemas);
        }

        return true;
    }

    @Override
    @Transactional
    public boolean deleteForm(String FormId) {
        Form form = formRepository.findByFormId(FormId).orElseThrow(
                () -> new ResourceNotFoundException("Form", "Id", FormId)
        );

        deleteFormSchemas(FormId);
        formRepository.delete(form);
        return true;
    }

    @Override
    @Transactional
    public List<String> deleteAllForms() {
        List<Form> forms = formRepository.findAll();
        List<String> deletedFormIds = new ArrayList<>();

        for (Form form : forms) {
            deletedFormIds.add(form.getFormId());
            deleteFormSchemas(form.getFormId());
        }

        formRepository.deleteAll(forms);
        return deletedFormIds;
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

    @Override
    public ElementLabelResponseDto fetchElementById(String elementId) {
        SchemaComponent schemaComponent = schemaComponentRepository.findByKey(elementId).orElseThrow(
                ()-> new ResourceNotFoundException("ElementId", "Id", elementId)
        );
        ElementLabelResponseDto elementLabelResponseDto = new ElementLabelResponseDto();
        elementLabelResponseDto.setLabel(schemaComponent.getLabel());
        elementLabelResponseDto.setElementId(elementId);

        return elementLabelResponseDto;
    }
}

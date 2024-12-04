package com.corelate.forms.service.impl;

import com.corelate.forms.dto.FormDto;
import com.corelate.forms.entity.Form;
import com.corelate.forms.exeption.ResourceNotFoundException;
import com.corelate.forms.mapper.FormMapper;
import com.corelate.forms.repository.FormRepository;
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
    public FormDto fetchForm(String FormId) {
        Form form = formRepository.findByFormId(FormId).orElseThrow(
                ()-> new ResourceNotFoundException("Form", "Id", FormId)
        );

        FormDto formDto = FormMapper.mapToFormDto(form, new FormDto());


        return formDto;
    }

    @Override
    public List<FormDto> fetchAllForm() {
        List<FormDto> formDtos = new ArrayList<>();
        List<Form> forms = formRepository.findByCreatedBy("Admin");

        for (Form form : forms) {
            FormDto formDto = FormMapper.mapToFormDto(form, new FormDto());
            formDtos.add(formDto);
        }

        return formDtos;
    }

    @Override
    public boolean updateForm(FormDto formDto) {
        return false;
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
}

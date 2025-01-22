package com.corelate.forms.service;
import com.corelate.forms.dto.FormDto;
import com.corelate.forms.dto.FormSelectionDto;

import java.util.List;

public interface IFormService {

    /**
     * @param formDto - FormDto Object
     * @return Form Details
     */
    void createForm(FormDto formDto);

    /**
     *     @param formId - Input Form ID
     *     @return Form Details
     */
    FormDto fetchForm(String formId);

    /**
     *
     *     @return List of FormDTOs Details
     */
    List<FormDto> fetchAllForm();

    /**
     *     @param formDto - FormDto Object
     *     @return boolean indicating if the update of Form detail is successful or not
     */
    boolean updateForm(FormDto formDto);

    /**
     *     @param FormId - FormId Object
     *     @return boolean indicating if the update of Account detail is successful or not
     */
    boolean deleteForm(String FormId);


    /**
     * @param formId
     * @return
     */
    boolean updateCommunicationStatus(String formId);

    /**
     * @param formId
     * @return
     */
    List<Object> fetchListByForm(String formId);

    List<FormSelectionDto> fetchSelections();
}

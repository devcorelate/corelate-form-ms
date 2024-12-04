package com.corelate.forms.repository;

import com.corelate.forms.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormRepository extends JpaRepository<Form,String> {

    /**
     * @param formId
     * @return
     */
    Optional<Form> findByFormId(String formId);


    /**
     * @param processId
     * @return
     */
    Optional<Form> findByProcessId(String processId);
    /**
     * @param formId
     */




    @Transactional
    @Modifying
    void deleteByFormId(String formId);

    List<Form> findByCreatedBy(String createBy);
}

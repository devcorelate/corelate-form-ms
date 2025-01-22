package com.corelate.forms.repository;

import com.corelate.forms.entity.FormSchema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormSchemaRepository extends JpaRepository<FormSchema,String> {

    /**
     * @return
     */
    Optional<FormSchema> findBySchemaId(String schemaId);

    /**
     * @return
     */
    Optional<FormSchema> findByFormId(String formId);

    @Transactional
    @Modifying
    void deleteBySchemaId(String schemaId);

    List<FormSchema> findAllByFormId(String formId);
}

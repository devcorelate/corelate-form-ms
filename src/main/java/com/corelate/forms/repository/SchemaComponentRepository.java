package com.corelate.forms.repository;

import com.corelate.forms.entity.FormSchema;
import com.corelate.forms.entity.SchemaComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface SchemaComponentRepository extends JpaRepository<SchemaComponent,String> {

    /**
     * @return
     */
    Optional<SchemaComponent> findByComponentId(String componentId);

    @Transactional
    @Modifying
    void deleteBySchemaId(String schemaId);

    List<SchemaComponent> findAllBySchemaId(String schemaId);
}
package com.corelate.forms.repository;

import com.corelate.forms.entity.DataSourceConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface DataSourceConfigRepository extends JpaRepository<DataSourceConfig, String> {

    Optional<DataSourceConfig> findByFormIdAndComponentId(String formId, String componentId);

    List<DataSourceConfig> findAllByFormId(String formId);

    @Transactional
    @Modifying
    void deleteByFormId(String formId);
}

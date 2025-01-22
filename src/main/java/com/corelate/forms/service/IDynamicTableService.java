package com.corelate.forms.service;

import com.corelate.forms.dto.TableSchemaDto;

public interface IDynamicTableService {

    /**
     * @param tableSchemaDto
     */
    void createTable(TableSchemaDto tableSchemaDto);

}

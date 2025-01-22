package com.corelate.forms.service.impl;


import com.corelate.forms.dto.ColumnDto;
import com.corelate.forms.dto.TableSchemaDto;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class DynamicTableService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void createTable(TableSchemaDto tableSchema) {
        StringBuilder sql = new StringBuilder("CREATE TABLE ");
        sql.append(tableSchema.getTableName()).append(" (");

        for (ColumnDto column : tableSchema.getColumns()) {
            sql.append(column.getName()).append(" ").append(column.getType());
            if (Boolean.TRUE.equals(column.getIsPrimaryKey())) {
                sql.append(" PRIMARY KEY");
            }
            sql.append(", ");
        }

        // Remove the last comma and space
        sql.delete(sql.length() - 2, sql.length());
        sql.append(")");

        System.out.println("Executing SQL: " + sql);
        entityManager.createNativeQuery(sql.toString()).executeUpdate();
    }

    @Transactional
    public void deleteTableById(String tableName) {
        String sql = "DROP TABLE IF EXISTS " + tableName;
        System.out.println("Executing SQL: " + sql);
        entityManager.createNativeQuery(sql).executeUpdate();
    }
}

package com.corelate.forms.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TableDataService {

    @PersistenceContext
    private final EntityManager entityManager;

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Retrieves table columns and data as JSON.
     */
    public List<ObjectNode> getTableDataAsJson(String tableName) {
        log.info("Retrieving data for table: {}", tableName);

        // 1. Get column names for the table
        List<String> columnNames = getTableColumns(tableName);

        // 2. Fetch all data from the table
        List<Object[]> tableData = getTableData(tableName);

        // 3. Convert result to JSON
        return convertToJson(columnNames, tableData);
    }

    /**
     * Retrieves column names for the specified table.
     */
    private List<String> getTableColumns(String tableName) {
        String sql = "SELECT column_name " +
                "FROM information_schema.columns " +
                "WHERE table_name = :tableName";
        log.debug("Executing column query: {}", sql);
        Query query = entityManager.createNativeQuery(sql);
        query.setParameter("tableName", tableName);

        return query.getResultList();
    }

    /**
     * Retrieves all data from the specified table.
     */
    private List<Object[]> getTableData(String tableName) {
        String sql = "SELECT * FROM " + tableName;
        log.debug("Executing data query: {}", sql);
        Query query = entityManager.createNativeQuery(sql);
        return query.getResultList();
    }

    /**
     * Converts column names and data into a list of JSON objects.
     */
    private List<ObjectNode> convertToJson(List<String> columnNames, List<Object[]> tableData) {
        List<ObjectNode> jsonList = new ArrayList<>();

        for (Object[] row : tableData) {
            ObjectNode jsonObject = objectMapper.createObjectNode();
            for (int i = 0; i < columnNames.size(); i++) {
                jsonObject.put(columnNames.get(i), row[i] != null ? row[i].toString() : null);
            }
            jsonList.add(jsonObject);
        }
        return jsonList;
    }
}


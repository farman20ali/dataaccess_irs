package org.irs.util;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.irs.database.Datasources;

import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestScoped
public class CommonMethods {


    public String readFile(String fileName) {
        String filePath = ConstantValues.filePath + fileName + ConstantValues.fileExtension;
        String lines = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            lines = "";
            String line;
            while ((line = reader.readLine()) != null) {
                lines += " " + line;

            }
            return lines;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return lines;

    }

    public List<Object> extractResultSet(ResultSet resultSet) throws Exception {
        List<Object> results = new ArrayList<>();
        String errorMessage = "";
        try {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(i);
                    row.put(columnName, columnValue);
                }
                results.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            errorMessage = e.getMessage();
            throw new Exception(errorMessage);
        }

        return results;
    }

    public String formatQuery(String query, String params) throws Exception {
        String result = null;

        try {
            result = String.format(query, params.split(","));

        } catch (Exception e) {
            String errorMessage = "formatting query: " + e.getMessage();
            throw new Exception(errorMessage);
        }
        return result;
    }

    public static String writeJson(Object object){
        ObjectMapper objectMapper=new ObjectMapper();
        try{
           return objectMapper.writeValueAsString(object);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}

package org.irs.service;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import org.irs.database.Datasources;
import org.irs.dto.RequestDto;
import org.irs.dto.ResponseDto;
import org.irs.util.CommonMethods;
import org.irs.util.ConstantValues;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@RequestScoped
public class UserService {
    @Inject
    Datasources datasourcesObj;
    @Inject
    CommonMethods commonMethods;
    public ResponseDto fetchData (RequestDto requestDto)throws Exception{
        ResponseDto responseDto=new ResponseDto();
        String query=null;
        if(ConstantValues.queryHashMap.containsKey(requestDto.fileName)){
            query=ConstantValues.queryHashMap.get(requestDto.fileName);
        }else{
            query = commonMethods.readFile(requestDto.fileName);
            if(query==null || query.trim().isEmpty()){
                responseDto.setFailure("No Criteria Found");
                return responseDto;
            }else{
                ConstantValues.queryHashMap.put(requestDto.fileName,query);
            }
        }
        
        String updateQuery=commonMethods.formatQuery(query,requestDto.params);
        List<Object> list=fnExecuteQuery(updateQuery);
        if(list.isEmpty()){
            responseDto.setFailure("No Record Found");
            return responseDto;
        }
        responseDto.data=list;
        return  responseDto;

    }


    public List<Object> fnExecuteQuery(String query) throws Exception {

        List<Object> results = new ArrayList<>();


        try (Connection connection = datasourcesObj
                .getConnection();

             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query);) {

            results = commonMethods.extractResultSet(resultSet);


        } catch (Exception e) {

            String errorMessage = "Error while executing query: " + e.getMessage();
            throw new Exception(errorMessage);
        }
        return results;
    }



}

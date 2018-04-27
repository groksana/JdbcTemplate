package com.gromoks.jdbctemplate;

import com.gromoks.jdbctemplate.mapper.RowMapper;
import com.gromoks.jdbctemplate.util.ParsedSql;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NamedParameterJdbcTemplate {
    private static final String INTEGER_TYPE = "Integer";
    private static final String STRING_TYPE = "String";
    private static final String DOUBLE_TYPE = "Double";
    private DataSource dataSource;

    public NamedParameterJdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> List<T> query(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper) throws SQLException {
        List<T> resultList = new ArrayList<>();
        ParsedSql parsedSql = new ParsedSql(sql);
        String substituteNamedParametersSql = parsedSql.getSubstituteNamedParameterSql();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(substituteNamedParametersSql)) {
            addStatementParameters(preparedStatement, sql, paramMap);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    T record = rowMapper.mapRow(resultSet);
                    resultList.add(record);
                }
            }
        }
        return resultList;
    }

    public <T> T queryForObject(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper) throws SQLException {
        ParsedSql parsedSql = new ParsedSql(sql);
        String substituteNamedParametersSql = parsedSql.getSubstituteNamedParameterSql();
        T extractedObject = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(substituteNamedParametersSql)) {
            addStatementParameters(preparedStatement, sql, paramMap);
            try (ResultSet resultSet = preparedStatement.executeQuery()){
                if (resultSet.next()) {
                    extractedObject = rowMapper.mapRow(resultSet);
                    if (resultSet.next()) {
                        throw new RuntimeException("Incorrect result");
                    }
                }
            }
        }
        return extractedObject;
    }

    public int update(String sql, Map<String, ?> paramMap) throws SQLException {
        int generatedKey;
        ParsedSql parsedSql = new ParsedSql(sql);
        String substituteNamedParametersSql = parsedSql.getSubstituteNamedParameterSql();

        try(Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(substituteNamedParametersSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            addStatementParameters(preparedStatement, sql, paramMap);
            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()){
                generatedKey = resultSet.next() ? resultSet.getInt(1) : 0;
            }
        }
        return generatedKey;
    }

    private void addStatementParameters(PreparedStatement preparedStatement, String sql, Map<String, ?> paramMap) throws SQLException {
        ParsedSql parsedSql = new ParsedSql(sql);
        List<String> orderedNamedParameter = parsedSql.getOrderedNamedParameter();

        int index = 1;
        for (String parameter : orderedNamedParameter) {
            Object parameterValue = paramMap.get(parameter);
            String simpleClassName = parameterValue.getClass().getSimpleName();
            switch (simpleClassName) {
                case INTEGER_TYPE:
                    preparedStatement.setInt(index, (Integer) parameterValue);
                    index++;
                    break;
                case STRING_TYPE:
                    preparedStatement.setString(index, (String) parameterValue);
                    index++;
                    break;
                case DOUBLE_TYPE:
                    preparedStatement.setDouble(index, (Double) parameterValue);
                    index++;
                    break;
                default:
                    throw new IllegalArgumentException("Unknown data type: " + simpleClassName);
            }
        }
    }

}

package com.gromoks.jdbctemplate;

import com.gromoks.jdbctemplate.generator.PreparedStatementGenerator;
import com.gromoks.jdbctemplate.generator.NamedPreparedStatementGenerator;
import com.gromoks.jdbctemplate.mapper.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NamedParameterJdbcTemplate {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> List<T> query(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper) throws SQLException {
        List<T> resultList = new ArrayList<>();
        PreparedStatementGenerator namedPreparedStatementGenerator = new NamedPreparedStatementGenerator(sql, paramMap);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = namedPreparedStatementGenerator.generatePreparedStatement(connection)) {
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
        T extractedObject = null;
        PreparedStatementGenerator namedPreparedStatementGenerator = new NamedPreparedStatementGenerator(sql, paramMap);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = namedPreparedStatementGenerator.generatePreparedStatement(connection)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
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
        PreparedStatementGenerator namedPreparedStatementGenerator = new NamedPreparedStatementGenerator(sql, paramMap, true);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = namedPreparedStatementGenerator.generatePreparedStatement(connection)) {
            preparedStatement.executeUpdate();

            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                generatedKey = resultSet.next() ? resultSet.getInt(1) : 0;
            }
        }
        return generatedKey;
    }

}

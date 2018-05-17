package com.gromoks.jdbctemplate;

import com.gromoks.jdbctemplate.generator.PreparedStatementGenerator;
import com.gromoks.jdbctemplate.generator.ArgumentPreparedStatementGenerator;
import com.gromoks.jdbctemplate.mapper.RowMapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplate {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object... args) throws SQLException {
        PreparedStatementGenerator argumentPreparedStatementGenerator = new ArgumentPreparedStatementGenerator(sql, args);

        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = argumentPreparedStatementGenerator.generatePreparedStatement(connection);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            List<T> resultList = new ArrayList<>();
                while (resultSet.next()) {
                    T record = rowMapper.mapRow(resultSet);
                    resultList.add(record);
                }
            return resultList;
        }
    }
}

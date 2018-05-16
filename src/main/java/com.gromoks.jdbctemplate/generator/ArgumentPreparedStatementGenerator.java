package com.gromoks.jdbctemplate.generator;

import com.gromoks.jdbctemplate.generator.util.ParameterSetter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ArgumentPreparedStatementGenerator implements PreparedStatementGenerator {
    private String sql;
    private Object[] args;

    public ArgumentPreparedStatementGenerator(String sql, Object[] args) {
        this.sql = sql;
        this.args = args;
    }

    @Override
    public PreparedStatement generatePreparedStatement(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        addStatementParameters(preparedStatement);

        return preparedStatement;
    }

    void addStatementParameters(PreparedStatement preparedStatement) throws SQLException {
        int index = 1;
        for (Object object : args) {
            ParameterSetter.setParameter(preparedStatement, object, index);
            index++;
        }
    }
}

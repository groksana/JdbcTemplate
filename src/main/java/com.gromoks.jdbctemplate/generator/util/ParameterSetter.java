package com.gromoks.jdbctemplate.generator.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ParameterSetter {
    public static void setParameter(PreparedStatement preparedStatement, Object parameterValue, int index) throws SQLException {
        if (Integer.class.equals(parameterValue.getClass())) {
            preparedStatement.setInt(index, (Integer) parameterValue);
        } else if (String.class.equals(parameterValue.getClass())) {
            preparedStatement.setString(index, (String) parameterValue);
        } else if (Double.class.equals(parameterValue.getClass())) {
            preparedStatement.setDouble(index, (Double) parameterValue);
        } else {
            throw new IllegalArgumentException("Unknown data type: " + parameterValue.getClass());
        }
    }
}

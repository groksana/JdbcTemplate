package com.gromoks.jdbctemplate.generator.impl.util;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ParameterSetter {
    public static void setParameter(PreparedStatement preparedStatement, Object parameterValue, int index) throws SQLException {
        if (Integer.class.equals(parameterValue.getClass())) {
            preparedStatement.setInt(index, (Integer) parameterValue);
            index++;
        } else if (String.class.equals(parameterValue.getClass())) {
            preparedStatement.setString(index, (String) parameterValue);
            index++;
        } else if (Double.class.equals(parameterValue.getClass())) {
            preparedStatement.setDouble(index, (Double) parameterValue);
            index++;
        } else {
            throw new IllegalArgumentException("Unknown data type: " + parameterValue.getClass());
        }
    }
}

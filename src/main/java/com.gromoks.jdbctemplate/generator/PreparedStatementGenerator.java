package com.gromoks.jdbctemplate.generator;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementGenerator {
    PreparedStatement generatePreparedStatement(Connection connection) throws SQLException;
}

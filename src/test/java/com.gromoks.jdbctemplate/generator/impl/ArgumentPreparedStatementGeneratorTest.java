package com.gromoks.jdbctemplate.generator.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArgumentPreparedStatementGeneratorTest {
    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    private static final String SELECT_SQL = "SELECT price FROM product WHERE name=?";

    @Before
    public void setUp() throws Exception {
        assertNotNull(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void generatePreparedStatementTest() throws SQLException {
        PreparedStatement expectedPreparedStatement = connection.prepareStatement(SELECT_SQL);
        when(connection.prepareStatement(any(String.class))).thenReturn(expectedPreparedStatement);

        ArgumentPreparedStatementGenerator argumentPreparedStatementGenerator = mock(ArgumentPreparedStatementGenerator.class);
        doNothing().when(argumentPreparedStatementGenerator).addStatementParameters(isA(PreparedStatement.class));
        when(argumentPreparedStatementGenerator.generatePreparedStatement(connection)).thenReturn(expectedPreparedStatement);

        PreparedStatement actualPreparedStatement = argumentPreparedStatementGenerator.generatePreparedStatement(connection);

        assertEquals(expectedPreparedStatement, actualPreparedStatement);
    }

}

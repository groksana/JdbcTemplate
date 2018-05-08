package com.gromoks.jdbctemplate.generator.impl;

import com.gromoks.jdbctemplate.util.NamedQueryParser;
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
public class NamedPreparedStatementGeneratorTest {
    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private NamedQueryParser namedQueryParser;

    private static final String SELECT_SQL = "SELECT price FROM product WHERE name=?";

    @Before
    public void setUp() throws Exception {
        assertNotNull(dataSource);
        when(dataSource.getConnection()).thenReturn(connection);
    }

    @Test
    public void generatePreparedStatementTest() throws SQLException {
        List<String> parameterNameList = new LinkedList<>();
        parameterNameList.add("name");

        PreparedStatement expectedPreparedStatement = connection.prepareStatement(SELECT_SQL);
        when(connection.prepareStatement(any(String.class))).thenReturn(expectedPreparedStatement);
        when(namedQueryParser.getOrderedNamedParameters()).thenReturn(parameterNameList);
        when(namedQueryParser.getSubstituteNamedParameterSql()).thenReturn(SELECT_SQL);

        NamedPreparedStatementGenerator namedPreparedStatementGenerator = mock(NamedPreparedStatementGenerator.class);
        doNothing().when(namedPreparedStatementGenerator).addStatementParameters(isA(PreparedStatement.class));
        when(namedPreparedStatementGenerator.generatePreparedStatement(connection)).thenReturn(expectedPreparedStatement);

        PreparedStatement actualPreparedStatement = namedPreparedStatementGenerator.generatePreparedStatement(connection);

        assertEquals(expectedPreparedStatement, actualPreparedStatement);
    }
}

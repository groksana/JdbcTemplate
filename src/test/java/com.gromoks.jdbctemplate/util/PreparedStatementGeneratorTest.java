package com.gromoks.jdbctemplate.util;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
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
public class PreparedStatementGeneratorTest {
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

        PreparedStatementGenerator preparedStatementGenerator = mock(PreparedStatementGenerator.class);
        doNothing().when(preparedStatementGenerator).addStatementParameters(isA(PreparedStatement.class));
        when(preparedStatementGenerator.generatePreparedStatement(connection)).thenReturn(expectedPreparedStatement);

        PreparedStatement actualPreparedStatement = preparedStatementGenerator.generatePreparedStatement(connection);

        assertEquals(expectedPreparedStatement, actualPreparedStatement);
    }

    @Test
    public void addStatementParametersTest() throws SQLException {
        PreparedStatement preparedStatement = mock(PreparedStatement.class);

        ArgumentCaptor stringArgumentCaptor = ArgumentCaptor.forClass(String.class);
        doNothing().when(preparedStatement).setString(any(Integer.class), (String) stringArgumentCaptor.capture());
        preparedStatement.setString(0, "name");

        ArgumentCaptor integerArgumentCaptor = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(preparedStatement).setInt(any(Integer.class), (Integer) integerArgumentCaptor.capture());
        preparedStatement.setInt(0, 200);

        ArgumentCaptor doubleArgumentCaptor = ArgumentCaptor.forClass(Double.class);
        doNothing().when(preparedStatement).setDouble(any(Integer.class), (Double) doubleArgumentCaptor.capture());
        preparedStatement.setDouble(0, 300.40);

        assertEquals("name", stringArgumentCaptor.getValue());
        assertEquals(200, integerArgumentCaptor.getValue());
        assertEquals(300.40, doubleArgumentCaptor.getValue());
    }
}

package com.gromoks.jdbctemplate.generator.impl.util;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

public class ParameterSetterTest {
    @Test
    public void setParameterTest() throws SQLException {
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

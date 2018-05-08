package com.gromoks.jdbctemplate.util;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class NamedQueryParserTest {
    private static final String SELECT_SQL = "SELECT price FROM product WHERE name=:name";
    private static final String INSERT_SQL = "INSERT INTO product(name, price) VALUES(:name, :price)";
    private NamedQueryParser namedQueryParser;

    @Test
    public void getSubstituteNamedParameterSqlSelectTest() {
        namedQueryParser = new NamedQueryParser(SELECT_SQL);
        String expectedSelectStatement = "SELECT price FROM product WHERE name=?";

        assertEquals(expectedSelectStatement, namedQueryParser.getSubstituteNamedParameterSql());
    }

    @Test
    public void getSubstituteNamedParameterSqlInsertTest() {
        namedQueryParser = new NamedQueryParser(INSERT_SQL);
        String expectedSelectStatement = "INSERT INTO product(name, price) VALUES(?, ?)";

        assertEquals(expectedSelectStatement, namedQueryParser.getSubstituteNamedParameterSql());
    }

    @Test
    public void getOrderedNamedParametersSelectTest() {
        namedQueryParser = new NamedQueryParser(SELECT_SQL);
        List<String> expectedParameterNameList = new LinkedList<>();
        expectedParameterNameList.add("name");

        assertEquals(expectedParameterNameList, namedQueryParser.getOrderedNamedParameters());
    }

    @Test
    public void getOrderedNamedParametersInsertTest() {
        namedQueryParser = new NamedQueryParser(INSERT_SQL);
        List<String> expectedParameterNameList = new LinkedList<>();
        expectedParameterNameList.add("name");
        expectedParameterNameList.add("price");

        assertEquals(expectedParameterNameList, namedQueryParser.getOrderedNamedParameters());
    }
}

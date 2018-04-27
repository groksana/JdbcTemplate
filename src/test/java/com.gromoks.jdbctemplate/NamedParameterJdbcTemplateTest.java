package com.gromoks.jdbctemplate;

import com.gromoks.jdbctemplate.mapper.RowMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NamedParameterJdbcTemplateTest {
    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @Mock
    private RowMapper rowMapper;

    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private Product product;

    @Before
    public void setUp() throws Exception {
        assertNotNull(dataSource);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(dataSource.getConnection()).thenReturn(connection);

        product = new Product();
        product.setId(1);
        product.setName("iPhone");
        product.setPrice(600);

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(product.getId());
        when(resultSet.getString(2)).thenReturn(product.getName());
        when(resultSet.getDouble(3)).thenReturn(product.getPrice());
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
    }

    @Test
    public void queryTest() {
        ProductRowMapper productRowMapper = new ProductRowMapper();
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("name", "iPhone");
        String sql = "select id, name from product where name=:name";
        List<Product> products = new ArrayList<>();
        products.add(product);

        when(namedParameterJdbcTemplate.query(sql, parameterMap, productRowMapper)).thenReturn(products);

        List<Product> actualProducts = namedParameterJdbcTemplate.query(sql, parameterMap, productRowMapper);
        assertEquals(product.getId(), actualProducts.get(0).getId());
        assertEquals(product.getName(), actualProducts.get(0).getName());
        assertEquals(product.getPrice(), actualProducts.get(0).getPrice(), 0);
    }

    @Test
    public void queryForObjectTest() {
        ProductRowMapper productRowMapper = new ProductRowMapper();
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("name", "iPhone");
        String sql = "select id, name from product where name=:name";

        when(namedParameterJdbcTemplate.queryForObject(sql, parameterMap, productRowMapper)).thenReturn(product);

        Product actualProduct = namedParameterJdbcTemplate.queryForObject(sql, parameterMap, productRowMapper);
        assertEquals(product.getId(), actualProduct.getId());
        assertEquals(product.getName(), actualProduct.getName());
        assertEquals(product.getPrice(), actualProduct.getPrice(), 0);
    }

    class Product {
        private int id;
        private String name;
        private double price;
        private String picturePath;
        private String description;

        public Product() {
        }

        public Product(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(double price) {
            this.price = price;
        }

        public String getPicturePath() {
            return picturePath;
        }

        public void setPicturePath(String picturePath) {
            this.picturePath = picturePath;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    class ProductRowMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet resultSet) throws SQLException {
            Product product = new Product();
            product.setId(resultSet.getInt("id"));
            product.setName(resultSet.getString("name"));
            product.setPrice(resultSet.getDouble("price"));
            product.setPicturePath(resultSet.getString("picturePath"));
            product.setDescription(resultSet.getString("description"));

            return product;
        }
    }


}

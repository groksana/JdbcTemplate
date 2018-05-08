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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JdbcTemplateTest {
    @Mock
    private DataSource dataSource;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private JdbcTemplate jdbcTemplate;

    private Product product;

    @Before
    public void setUp() throws Exception {
        ResultSet resultSet = mock(ResultSet.class);
        assertNotNull(dataSource);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(dataSource.getConnection()).thenReturn(connection);

        product = new Product();
        product.setId(1);
        product.setName("iPhone");
        product.setPrice(600);
        product.setPicturePath("https");
        product.setDescription("iPhone description");

        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(product.getId());
        when(resultSet.getString(2)).thenReturn(product.getName());
        when(resultSet.getDouble(3)).thenReturn(product.getPrice());
        when(resultSet.getString(4)).thenReturn(product.getPicturePath());
        when(resultSet.getString(5)).thenReturn(product.getDescription());
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
    }

    @Test
    public void queryTest() throws SQLException {
        ProductRowMapper productRowMapper = new ProductRowMapper();

        String sql = "select id, name, price, picturePath, description from product where name=? and price=?";
        List<Product> products = new ArrayList<>();
        products.add(product);

        when(jdbcTemplate.query(sql, productRowMapper, "iPhone", 600)).thenReturn(products);

        List<Product> actualProducts = jdbcTemplate.query(sql, productRowMapper, "iPhone", 600);
        assertEquals(product.getId(), actualProducts.get(0).getId());
        assertEquals(product.getName(), actualProducts.get(0).getName());
        assertEquals(product.getPrice(), actualProducts.get(0).getPrice(), 0.001);
        assertEquals(product.getPicturePath(), actualProducts.get(0).getPicturePath());
        assertEquals(product.getDescription(), actualProducts.get(0).getDescription());
    }

    private static class Product {
        private int id;
        private String name;
        private double price;
        private String picturePath;
        private String description;

        public Product() {
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

    private static class ProductRowMapper implements RowMapper<Product> {
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

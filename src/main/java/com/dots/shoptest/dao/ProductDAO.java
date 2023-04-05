package com.dots.shoptest.dao;

import com.dots.shoptest.db.ConnectionPool;
import com.dots.shoptest.db.DBConnection;
import com.dots.shoptest.model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ProductDAO {

  public static ArrayList<Product> getAllProducts() {
    ArrayList<Product> products = null;
    try {
      String GET_ALL_PRODUCTS =
        "SELECT product.id, product.name, product.description, product.price, product.img_url, product.stock, category.id as category_id, category.name as category_name FROM product, category WHERE product.category_id = category.id";
      Connection conn = ConnectionPool.getConnection();
      if (conn == null) {
        throw new SQLException("Connection is null");
      }
      PreparedStatement ps = conn.prepareStatement(GET_ALL_PRODUCTS);
      ResultSet rs = ps.executeQuery();
      products = new ArrayList<>();
      while (rs.next()) {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setDescription(rs.getString("description"));
        product.setImageUrl(rs.getString("img_url"));
        product.setCategoryName(rs.getString("category_name"));
        product.setCategoryId(rs.getInt("category_id"));
        product.setStock(rs.getInt("stock"));
        products.add(product);
      }
      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return products;
  }
}

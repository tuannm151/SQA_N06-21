package com.dots.shoptest.dao;

import com.dots.shoptest.db.ConnectionPool;
import com.dots.shoptest.db.DBConnection;
import com.dots.shoptest.model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CartDAO {

  public static boolean isCartEmpty(int userId) {
    boolean isEmpty = false;
    try {
      Connection conn = ConnectionPool.getConnection();
      String sql = "SELECT * FROM cart_item WHERE person_id = ?";
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1, userId);
      ResultSet resultSet = ps.executeQuery();
      if (!resultSet.next()) {
        isEmpty = true;
      }
      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return isEmpty;
  }

  public static int getCartItemsCount(int userId) {
    int count = -1;
    try {
      String GET_CART_ITEMS_COUNT_QUERY =
        "SELECT SUM(quantity) FROM cart_item WHERE person_id = ?";

      Connection conn = ConnectionPool.getConnection();

      PreparedStatement ps = conn.prepareStatement(GET_CART_ITEMS_COUNT_QUERY);
      ps.setInt(1, userId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        count = rs.getInt(1);
      }
      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return count;
  }

  public static int addToCart(int userId, int productId) {
    int result = 0;
    try {
      String UPSERT_CART_ITEM_QUERY =
        "INSERT INTO cart_item (product_id, quantity, person_id) VALUES (?, ?, ?) ON CONFLICT (product_id, person_id) DO UPDATE SET quantity = cart_item.quantity + 1";
      Connection conn = ConnectionPool.getConnection();
      PreparedStatement ps = conn.prepareStatement(UPSERT_CART_ITEM_QUERY);
      ps.setInt(1, productId);
      ps.setInt(2, 1);
      ps.setInt(3, userId);
      result = ps.executeUpdate();
      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  public static int reduceCartItemQuantity(int userId, int productId) {
    int result = 0;
    try {
      String REDUCE_CART_ITEM_QUANTITY_QUERY =
        "UPDATE cart_item SET quantity = quantity - 1 WHERE person_id = ? AND product_id = ? AND quantity > 1";
      Connection conn = ConnectionPool.getConnection();
      PreparedStatement ps = conn.prepareStatement(
        REDUCE_CART_ITEM_QUANTITY_QUERY
      );
      ps.setInt(1, userId);
      ps.setInt(2, productId);
      result = ps.executeUpdate();
      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  public static int deleteCartItem(int userId, int productId) {
    int result = 0;
    try {
      String DELETE_CART_ITEM_QUERY =
        "DELETE FROM cart_item WHERE person_id = ? AND product_id = ?";
      Connection conn = ConnectionPool.getConnection();
      PreparedStatement ps = conn.prepareStatement(DELETE_CART_ITEM_QUERY);
      ps.setInt(1, userId);
      ps.setInt(2, productId);
      result = ps.executeUpdate();
      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  public static Cart getCart(int userId) {
    Cart cart = null;
    try {
      String GET_CART_ITEMS_QUERY_POSTGRESQL =
        "SELECT cart_item.id, product_id, quantity, name, description, price, img_url from cart_item, product WHERE cart_item.product_id = product.id AND cart_item.person_id = ? ORDER BY id DESC";
      Connection conn = ConnectionPool.getConnection();
      PreparedStatement ps = conn.prepareStatement(
        GET_CART_ITEMS_QUERY_POSTGRESQL
      );
      ps.setInt(1, userId);
      ResultSet rs = ps.executeQuery();
      ArrayList<CartItem> cartItems = new ArrayList<>();
      while (rs.next()) {
        Product product = new Product();
        product.setId(rs.getInt("product_id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setImageUrl(rs.getString("img_url"));
        CartItem cartItem = new CartItem();
        cartItem.setId(rs.getInt("id"));
        cartItem.setProduct(product);
        cartItem.setQuantity(rs.getInt("quantity"));
        cartItems.add(cartItem);
      }
      cart = new Cart(cartItems);
      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return cart;
  }
}

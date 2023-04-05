package com.dots.shoptest.dao;

import com.dots.shoptest.db.ConnectionPool;
import com.dots.shoptest.db.DBConnection;
import com.dots.shoptest.dto.OrderDTO;
import com.dots.shoptest.model.CartItem;
import com.dots.shoptest.model.Order;
import com.dots.shoptest.model.OrderItem;
import com.dots.shoptest.model.Product;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class OrderDAO {

  public static Integer initOrder(OrderDTO orderDTO) {
    Integer orderId = null;
    try {
      String sql =
        "INSERT INTO person_order (person_id, address, phone, status, created_date) VALUES (?, ?, ?, ?, ?) RETURNING  id";
      Connection conn = ConnectionPool.getConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      ps.setInt(1, orderDTO.getUserId());
      ps.setString(2, orderDTO.getAddress());
      ps.setString(3, orderDTO.getPhone());
      ps.setString(4, orderDTO.getStatus());
      ps.setString(5, orderDTO.getCreatedDate());

      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        orderId = rs.getInt("id");
      }

      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return orderId;
  }

  public static int createOrder(Integer orderId, Integer userId) {
    int result = 0;
    try {
      Connection conn = ConnectionPool.getConnection();
      String GET_CART_ITEMS =
        "SELECT product_id, price, quantity FROM cart_item, product WHERE cart_item.product_id = product.id AND cart_item.person_id = ?";
      PreparedStatement ps = conn.prepareStatement(GET_CART_ITEMS);
      ps.setInt(1, userId);
      ResultSet rs = ps.executeQuery();
      ArrayList<CartItem> cartItems = new ArrayList<>();
      while (rs.next()) {
        CartItem cartItem = new CartItem();
        Product product = new Product();
        product.setId(rs.getInt("product_id"));
        product.setPrice(rs.getBigDecimal("price"));
        cartItem.setProduct(product);
        cartItem.setQuantity(rs.getInt("quantity"));
        cartItems.add(cartItem);
      }
      String INSERT_CART_ITEMS =
        "INSERT INTO order_item (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
      BigDecimal orderTotalPrice = new BigDecimal(0);
      for (CartItem cartItem : cartItems) {
        ps = conn.prepareStatement(INSERT_CART_ITEMS);
        ps.setInt(1, orderId);
        ps.setInt(2, cartItem.getProduct().getId());
        ps.setInt(3, cartItem.getQuantity());
        ps.setBigDecimal(4, cartItem.getProduct().getPrice());
        BigDecimal cartItemTotalPrice = cartItem.getTotalPrice();
        orderTotalPrice = orderTotalPrice.add(cartItemTotalPrice);
        result = ps.executeUpdate();
      }
      if (result == 1) {
        String UPDATE_ORDER_TOTAL_PRICE =
          "UPDATE person_order SET total = ? WHERE id = ?";
        ps = conn.prepareStatement(UPDATE_ORDER_TOTAL_PRICE);
        ps.setBigDecimal(1, orderTotalPrice);
        ps.setInt(2, orderId);
        result = ps.executeUpdate();
      }

      if (result == 1) {
        String DELETE_CART_ITEMS = "DELETE FROM cart_item WHERE person_id = ?";
        ps = conn.prepareStatement(DELETE_CART_ITEMS);
        ps.setInt(1, userId);
        result = ps.executeUpdate();
      }
      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  public static ArrayList<OrderDTO> getOrdersByUserId(Integer userId) {
    ArrayList<OrderDTO> orderDTOS = null;
    try {
      Connection conn = ConnectionPool.getConnection();

      String GET_ORDERS =
        "SELECT id, person_id, address, phone, status, created_date, total FROM person_order WHERE person_id = ? ORDER BY id DESC";
      PreparedStatement ps = conn.prepareStatement(GET_ORDERS);
      ps.setInt(1, userId);
      ResultSet ordersRs = ps.executeQuery();

      orderDTOS = new ArrayList<>();
      while (ordersRs.next()) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(ordersRs.getInt("id"));
        orderDTO.setUserId(ordersRs.getInt("person_id"));
        orderDTO.setAddress(ordersRs.getString("address"));
        orderDTO.setPhone(ordersRs.getString("phone"));
        orderDTO.setStatus(ordersRs.getString("status"));
        orderDTO.setCreatedDate(ordersRs.getString("created_date"));
        orderDTO.setTotalPrice(ordersRs.getBigDecimal("total"));

        String GET_ORDER_ITEMS_COUNT =
          "SELECT COUNT(*) FROM order_item WHERE order_id = ?";
        ps = conn.prepareStatement(GET_ORDER_ITEMS_COUNT);
        ps.setInt(1, orderDTO.getId());
        ResultSet orderItemsCountRs = ps.executeQuery();
        orderItemsCountRs.next();

        orderDTO.setItemCount(orderItemsCountRs.getInt(1));
        orderDTOS.add(orderDTO);
      }
      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return orderDTOS;
  }

  public static Order getOrderById(int orderId, int userId) {
    Order order = null;
    try {
      Connection conn = ConnectionPool.getConnection();

      String SQL_CHECK_ORDER_OWNER =
        "SELECT * FROM person_order WHERE id = ? AND person_id = ?";
      PreparedStatement ps = conn.prepareStatement(SQL_CHECK_ORDER_OWNER);
      ps.setInt(1, orderId);
      ps.setInt(2, userId);
      ResultSet rs = ps.executeQuery();
      if (!rs.next()) {
        conn.close();
        ps.close();
        return null;
      }
      String SQL_GET_ORDER_ITEMS =
        "SELECT img_url, product.name, order_item.quantity, order_item.price " +
        "FROM product,order_item " +
        "WHERE order_id = ? " +
        "AND order_item.product_id = product.id ";
      ps = conn.prepareStatement(SQL_GET_ORDER_ITEMS);
      ps.setInt(1, orderId);
      rs = ps.executeQuery();
      ArrayList<OrderItem> orderItems = new ArrayList<>();
      while (rs.next()) {
        OrderItem orderItem = new OrderItem();
        Product product = new Product();
        product.setImageUrl(rs.getString("img_url"));
        product.setName(rs.getString("name"));
        orderItem.setProduct(product);
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setPrice(rs.getBigDecimal("price"));
        orderItems.add(orderItem);
      }
      order = new Order();
      String SQL_GET_ORDER =
        "SELECT address, phone, status, total, created_date FROM person_order WHERE id = ?";
      ps = conn.prepareStatement(SQL_GET_ORDER);
      ps.setInt(1, orderId);
      rs = ps.executeQuery();
      rs.next();
      order.setId(orderId);
      order.setOrderItems(orderItems);
      order.setAddress(rs.getString("address"));
      order.setPhone(rs.getString("phone"));
      order.setStatus(rs.getString("status"));
      order.setTotalPrice(rs.getBigDecimal("total"));
      order.setCreatedDate(rs.getString("created_date"));
      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return order;
  }
}

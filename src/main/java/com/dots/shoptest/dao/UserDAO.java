package com.dots.shoptest.dao;

import com.dots.shoptest.db.ConnectionPool;
import com.dots.shoptest.model.User;
import com.dots.shoptest.utils.Auth;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

public class UserDAO {

  public static int registerUser(User user) {
    String CHECK_EMAIL_EXISTS_QUERY = "SELECT * FROM person WHERE LOWER(email) LIKE LOWER(?)";
    String INSERT_USER_QUERY =
      "INSERT INTO person (name, password, email) VALUES (?, ?, ?)";
    int result = 0;
    try {
      Connection conn = ConnectionPool.getConnection();
      if (conn == null) {
        throw new SQLException("Connection is null");
      }
      PreparedStatement ps = conn.prepareStatement(CHECK_EMAIL_EXISTS_QUERY);
      ps.setString(1, user.getEmail().toLowerCase());
      boolean emailIsExists = ps.executeQuery().next();
      if (emailIsExists) {
        result = -1;
      } else {
        ps = conn.prepareStatement(INSERT_USER_QUERY);
        ps.setString(1, user.getName());
        ps.setString(2, Auth.hashPassword(user.getPassword()));
        ps.setString(3, user.getEmail());
        result = ps.executeUpdate();
      }
      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return result;
  }

  public static User findUser(String email, String password) {
    User user = null;
    try {
      // get password from email
      String query =
        "SELECT id, name, email, password FROM person WHERE email = ?";
      Connection conn = ConnectionPool.getConnection();
      if (conn == null) {
        throw new SQLException("Connection is null");
      }
      PreparedStatement ps = conn.prepareStatement(
        query,
        ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_READ_ONLY
      );
      ps.setString(1, email);
      ResultSet resultSet = ps.executeQuery();
      resultSet.first();
      String dbPassword = resultSet.getString("password");

      if (dbPassword != null && BCrypt.checkpw(password, dbPassword)) {
        user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
      }
      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return user;
  }

  public static User findUserByEmail(String email) {
    User user = null;
    try {
      String GET_USER_BY_EMAIL =
        "SELECT id, name, email FROM person WHERE email = ?";
      Connection conn = ConnectionPool.getConnection();
      if (conn == null) {
        throw new SQLException("Connection is null");
      }
      PreparedStatement ps = conn.prepareStatement(
        GET_USER_BY_EMAIL,
        ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_READ_ONLY
      );
      ps.setString(1, email);
      ResultSet resultSet = ps.executeQuery();

      if (resultSet.first()) {
        user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
      }
      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return user;
  }
}

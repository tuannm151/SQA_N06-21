package com.dots.shoptest.dao;

import com.dots.shoptest.db.ConnectionPool;
import com.dots.shoptest.model.User;
import com.dots.shoptest.utils.Auth;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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

  public static List<User> getAllUsers() {
    List<User> users = null;
    try {
      String GET_ALL_USERS_QUERY = "SELECT id, name, email, role FROM person";
      Connection conn = ConnectionPool.getConnection();
      if (conn == null) {
        throw new SQLException("Connection is null");
      }
      PreparedStatement ps = conn.prepareStatement(
        GET_ALL_USERS_QUERY,
        ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_READ_ONLY
      );
      ResultSet resultSet = ps.executeQuery();
      users = new ArrayList<>();
      while (resultSet.next()) {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setRole(resultSet.getString("role"));
        users.add(user);
      }
      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return users;
  }

  public static User findUser(String email, String password) {
    User user = null;
    try {
      // get password from email
      String query =
        "SELECT id, name, email, password, role FROM person WHERE email = ?";
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
        user.setRole(resultSet.getString("role"));
      }
      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return user;
  }

  public static User getUserById(int id) {
    User user = null;
    try {
      String GET_USER_BY_ID =
        "SELECT id, name, email, role FROM person WHERE id = ?";
      Connection conn = ConnectionPool.getConnection();
      if (conn == null) {
        throw new SQLException("Connection is null");
      }
      PreparedStatement ps = conn.prepareStatement(
        GET_USER_BY_ID,
        ResultSet.TYPE_SCROLL_INSENSITIVE,
        ResultSet.CONCUR_READ_ONLY
      );
      ps.setInt(1, id);
      ResultSet resultSet = ps.executeQuery();

      if (resultSet.first()) {
        user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        user.setEmail(resultSet.getString("email"));
        user.setRole(resultSet.getString("role"));
      }
      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return user;
  }

  public static int updateUser(int id, User user) {
    String UPDATE_USER_QUERY =
      "UPDATE person SET name = ?, email = ?, role = ? WHERE id = ?";
    int result = 0;
    try {
      Connection conn = ConnectionPool.getConnection();
      if (conn == null) {
        throw new SQLException("Connection is null");
      }
      PreparedStatement ps = conn.prepareStatement(UPDATE_USER_QUERY);
      ps.setString(1, user.getName());
      ps.setString(2, user.getEmail());
      ps.setString(3, user.getRole());
      ps.setInt(4, id);
      result = ps.executeUpdate();
      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  public static int deleteUser(int id) {
    String DELETE_USER_QUERY = "DELETE FROM person WHERE id = ?";
    int result = 0;
    try {
      Connection conn = ConnectionPool.getConnection();
      if (conn == null) {
        throw new SQLException("Connection is null");
      }
      PreparedStatement ps = conn.prepareStatement(DELETE_USER_QUERY);
      ps.setInt(1, id);
      result = ps.executeUpdate();
      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }

  public static User findUserByEmail(String email) {
    User user = null;
    try {
      String GET_USER_BY_EMAIL =
        "SELECT id, name, email, role FROM person WHERE email = ?";
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
        user.setRole(resultSet.getString("role"));
      }
      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return user;
  }

    public static int addUser(User customer) {
        int result = 0;
        try {
            String ADD_USER_QUERY = "INSERT INTO person (name, email, password, role) VALUES (?, ?, ?, ?)";
            Connection conn = ConnectionPool.getConnection();
            if (conn == null) {
                throw new SQLException("Connection is null");
            }
            PreparedStatement ps = conn.prepareStatement(ADD_USER_QUERY);
            ps.setString(1, customer.getName());
            ps.setString(2, customer.getEmail());
            ps.setString(3, Auth.hashPassword(customer.getPassword()));
            ps.setString(4, customer.getRole());
            result = ps.executeUpdate();
            conn.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
  public static boolean hasUserByEmail(String email) {
    boolean result = false;
    try {
      String GET_USER_BY_EMAIL =
        "SELECT id, name, email, role FROM person WHERE email = ?";
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
        result = true;
      }
      conn.close();
      ps.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return result;
  }
}

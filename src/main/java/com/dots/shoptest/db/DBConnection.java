package com.dots.shoptest.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

  public static Connection getConnection()
    throws SQLException, ClassNotFoundException {
    // postgresql
    Class.forName("com.mysql.cj.jdbc.Driver");
    String url = "jdbc:mysql://100.101.215.116:3306/cdshop";
    String user = "root";
    String password =
      "05120101";

    Connection connection = DriverManager.getConnection(url, user, password);
    System.out.println("Connected to the PostgreSQL server successfully.");
    return connection;
  }
}

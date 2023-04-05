package com.dots.shoptest.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

  public static Connection getConnection()
    throws SQLException, ClassNotFoundException {
    // postgresql
    Class.forName("org.postgresql.Driver");
    String url =
      "jdbc:postgresql://" +
      "ec2-54-235-98-1.compute-1.amazonaws.com" +
      ":5432/dfeseieja52qb8?sslmode=require";
    String user = "nrprnwnrzwnulp";
    String password =
      "816876c29ce67b11f5cb61886c0f7eaa681cec992fec728f1da6aa0bf2d6add4";

    Connection connection = DriverManager.getConnection(url, user, password);
    System.out.println("Connected to the PostgreSQL server successfully.");
    return connection;
  }
}

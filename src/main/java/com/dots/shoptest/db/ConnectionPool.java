package com.dots.shoptest.db;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

  private static final BasicDataSource ds = new BasicDataSource();

  static {
    ds.setDriverClassName("org.postgresql.Driver");
    ds.setUrl("jdbc:postgresql://ec2-54-235-98-1.compute-1.amazonaws.com:5432/dfeseieja52qb8?sslmode=require");
    ds.setUsername("nrprnwnrzwnulp");
    ds.setPassword("816876c29ce67b11f5cb61886c0f7eaa681cec992fec728f1da6aa0bf2d6add4");
    ds.setMinIdle(3);
    ds.setMaxIdle(8);
    ds.setMaxWait(60000);
    ds.setMaxActive(20);
    ds.setMaxOpenPreparedStatements(100);
  }

  public static Connection getConnection() throws SQLException {
    return ds.getConnection();
  }

  private void DBCPDataSource(){ }
}

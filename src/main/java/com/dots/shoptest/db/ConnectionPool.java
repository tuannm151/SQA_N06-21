package com.dots.shoptest.db;

import org.apache.commons.dbcp.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPool {

  private static final BasicDataSource ds = new BasicDataSource();

  static {
    ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
    ds.setUrl("jdbc:mysql://100.101.215.116:3306/cdshop");
    ds.setUsername("root");
    ds.setPassword("05120101");
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

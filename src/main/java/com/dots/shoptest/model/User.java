package com.dots.shoptest.model;

import java.io.Serializable;

public class User implements Serializable {

  private static final long serialVersionUID = 1L;
  private Integer id;
  private String name;
  private String email;
  private String password;
  private String role;

  public User() {}

  public User(String name, String email, String password) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = "user";
  }

  public User(Integer id, String name, String email, String password) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.role = "user";
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public void setRole(String role) {
    this.role = role;
  }

    public String getRole() {
        return role;
    }
}

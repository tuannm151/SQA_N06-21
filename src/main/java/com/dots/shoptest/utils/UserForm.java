package com.dots.shoptest.utils;

public class UserForm {

  public final String name;
  public final String email;
  public final String password;
  public final String repassword;
  public String error;

  public UserForm(
    String name,
    String email,
    String password,
    String repassword
  ) {
    this.name = name;
    this.email = email;
    this.password = password;
    this.repassword = repassword;
  }


  public boolean isValid() {
    if (email == null || email.trim().isEmpty()) {
      error = "Email is required";
      return false;
    }
    if (password == null || password.trim().isEmpty()) {
      error = "Password is required";
      return false;
    }
    if (name == null || name.trim().isEmpty()) {
      error = "Name is required";
      return false;
    }
    if (repassword == null || repassword.trim().isEmpty()) {
      error = "Re-password is required";
      return false;
    }
    if (!password.equals(repassword)) {
      System.err.println(password + " " + repassword);
      error = "Password and Re-password must be same";
      return false;
    }
    return true;
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

  public String getError() {
    return error;
  }
}

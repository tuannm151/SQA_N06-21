package com.dots.shoptest.model;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Order {

  private int id;
  private ArrayList<OrderItem> orderItems;
  private String address;
  private String phone;
  private String status;
  private String createdDate;
  private BigDecimal totalPrice;

  public Order() {
    orderItems = new ArrayList<OrderItem>();
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public ArrayList<OrderItem> getOrderItems() {
    return orderItems;
  }

  public void setOrderItems(ArrayList<OrderItem> orderItems) {
    this.orderItems = orderItems;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(String createdDate) {
    this.createdDate = createdDate;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  public void addOrderItem(OrderItem orderItem) {
    orderItems.add(orderItem);
  }

  public int getOrderItemsCount() {
    int total = 0;
    for (OrderItem orderItem : orderItems) {
      total += orderItem.getQuantity();
    }
    return total;
  }
}

package com.dots.shoptest.model;

import java.math.BigDecimal;

public class CartItem {

  private Integer id;
  private Product product;
  private int quantity;

  public CartItem(Product product, int quantity) {
    this.product = product;
    this.quantity = quantity;
  }

  public CartItem() {}

  public Product getProduct() {
    return product;
  }

  public void setProduct(Product product) {
    this.product = product;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public BigDecimal getTotalPrice() {
    return product.getPrice().multiply(new BigDecimal(quantity));
  }
}

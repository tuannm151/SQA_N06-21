package com.dots.shoptest.model;

import java.math.BigDecimal;
import java.util.ArrayList;

public class Cart {

  private ArrayList<CartItem> cartItems;
  private BigDecimal totalPrice;
  private Integer totalSize;

  public Cart(ArrayList<CartItem> cartItems) {
    this.cartItems = cartItems;
    totalPrice = caculateTotalPrice();
    totalSize = calculateTotalSize();
  }

  public ArrayList<CartItem> getCartItems() {
    return cartItems;
  }

  public void setCartItems(ArrayList<CartItem> cartItems) {
    this.cartItems = cartItems;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  public Integer calculateTotalSize() {
    int totalSize = 0;
    for (CartItem cartItem : cartItems) {
      totalSize += cartItem.getQuantity();
    }
    return totalSize;
  }

  public Integer getTotalSize() {
    return totalSize;
  }

  public BigDecimal caculateTotalPrice() {
    BigDecimal totalPrice = new BigDecimal(0);
    for (CartItem cartItem : cartItems) {
      totalPrice = totalPrice.add(cartItem.getTotalPrice());
    }
    return totalPrice;
  }
}

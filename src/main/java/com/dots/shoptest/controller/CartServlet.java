package com.dots.shoptest.controller;

import com.dots.shoptest.dao.CartDAO;
import com.dots.shoptest.model.Cart;
import com.dots.shoptest.model.User;
import com.dots.shoptest.utils.Auth;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet(name = "CartServlet", value = "/cart")
public class CartServlet extends HttpServlet {

  @Override
  protected void doGet(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws ServletException, IOException {
    User user = Auth.getAuthenticatedUser(request);
    if (user == null) {
      response.sendRedirect(getServletContext().getContextPath() + "/login");
      return;
    }
    Cart cart = CartDAO.getCart(user.getId());
    if (cart != null) {
      request.setAttribute("cart", cart);
    } else {
      request.setAttribute("error", "Something went wrong");
    }
    getServletContext()
      .getRequestDispatcher("/cart.jsp")
      .forward(request, response);
  }

  @Override
  protected void doPost(
    HttpServletRequest request,
    HttpServletResponse response
  ) {
    User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
      return;
    }
    int productId = Integer.parseInt(request.getParameter("productId"));
    String action = request.getParameter("action");
    if (action != null) {
      if (action.equals("add")) {
        doPost_Add(user, productId);
      }
      if (action.equals("reduce")) {
        doPost_Reduce(user, productId);
      }
      if (action.equals("delete")) {
        doPost_Delete(user, productId);
      }
    }
  }

  protected void doPost_Add(User user, int productId) {
    int result = CartDAO.addToCart(user.getId(), productId);
    if (result == 1) {
      System.out.println("Added to cart");
    } else {
      System.out.println("Failed to add to cart");
    }
  }

  protected void doPost_Reduce(User user, int productId) {
    int result = CartDAO.reduceCartItemQuantity(user.getId(), productId);
    if (result == 1) {
      System.out.println("Reduced cart item quantity");
    } else {
      System.out.println("Failed to reduce cart item quantity");
    }
  }

  protected void doPost_Delete(User user, int productId) {
    int result = CartDAO.deleteCartItem(user.getId(), productId);
    if (result == 1) {
      System.out.println("Deleted from cart");
    } else {
      System.out.println("Failed to delete from cart");
    }
  }
}

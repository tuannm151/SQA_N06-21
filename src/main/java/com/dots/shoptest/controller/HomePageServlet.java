package com.dots.shoptest.controller;

import com.dots.shoptest.dao.CartDAO;
import com.dots.shoptest.dao.ProductDAO;
import com.dots.shoptest.model.Product;
import com.dots.shoptest.model.User;
import com.dots.shoptest.utils.Auth;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet(name = "HomePageServlet", value = "/index.jsp")
public class HomePageServlet extends HttpServlet {

  @Override
  protected void doGet(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws ServletException, IOException {
    String url = "/WEB-INF/index.jsp";

    ArrayList<Product> products = ProductDAO.getAllProducts();

    int cartCount = 0;
    User user = Auth.getAuthenticatedUser(request);
    if (user != null) {
      cartCount = CartDAO.getCartItemsCount(user.getId());
    }

    if (products != null) {
      request.setAttribute("products", products);
      request.setAttribute("cartCount", cartCount);
    } else {
      request.setAttribute("error", "Error while loading products");
    }
    getServletContext().getRequestDispatcher(url).forward(request, response);
  }

  @Override
  protected void doPost(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws ServletException, IOException {
    doGet(request, response);
  }
}

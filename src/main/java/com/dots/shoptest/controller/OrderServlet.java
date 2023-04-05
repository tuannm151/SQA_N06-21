package com.dots.shoptest.controller;

import com.dots.shoptest.dao.CartDAO;
import com.dots.shoptest.dao.OrderDAO;
import com.dots.shoptest.dto.OrderDTO;
import com.dots.shoptest.model.Order;
import com.dots.shoptest.model.User;
import com.dots.shoptest.utils.Auth;
import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;


@WebServlet(name = "OrderServlet", value = "/orders")
public class OrderServlet extends HttpServlet {

  private final Gson gson = new Gson();

  @Override
  protected void doGet(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws ServletException, IOException {
    String action = request.getParameter("action");

    if (Objects.equals(action, "getOrderJson")) {
      doGet_OrderJson(request, response);
      return;
    }

    User user = Auth.getAuthenticatedUser(request);
    if (user == null) {
      response.sendRedirect(getServletContext().getContextPath() + "/login");
      return;
    }

    request.setCharacterEncoding("UTF-8");
    ArrayList<OrderDTO> orderDTOS = OrderDAO.getOrdersByUserId(user.getId());
    String url = "/order.jsp";

    if (orderDTOS == null) {
      request.setAttribute("error", "Error while getting orders");
    } else if (orderDTOS.size() == 0) {
      request.setAttribute("isEmpty", true);
    } else {
      request.setAttribute("orders", orderDTOS);
      Order lastestOrder = OrderDAO.getOrderById(
        orderDTOS.get(0).getId(),
        user.getId()
      );
      request.setAttribute("lastestOrder", lastestOrder);
      request.setAttribute(
        "cartCount",
        CartDAO.getCartItemsCount(user.getId())
      );
    }

    getServletContext().getRequestDispatcher(url).forward(request, response);
  }

  protected void doGet_OrderJson(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws ServletException, IOException {
    User user = (User) request.getSession().getAttribute("user");
    if (user == null) {
      response.sendRedirect(getServletContext().getContextPath() + "/login");
      return;
    }
    String orderId = request.getParameter("orderId");
    Order order = OrderDAO.getOrderById(
      Integer.parseInt(orderId),
      user.getId()
    );
    if (order == null) {
      return;
    }
    String orderJsonString = gson.toJson(order);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    PrintWriter out = response.getWriter();
    out.print(orderJsonString);
    out.flush();
  }

  @Override
  protected void doPost(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    String address = request.getParameter("address");
    String phone = request.getParameter("phone");

    if (
      address == null ||
      address.trim().isEmpty() ||
      phone == null ||
      phone.trim().isEmpty()
    ) {
      response.sendRedirect(getServletContext().getContextPath() + "/cart");
    }

    User user = Auth.getAuthenticatedUser(request);
    if (user == null) {
      response.sendRedirect(
        getServletContext().getContextPath() + "/login.jsp"
      );
      return;
    }

    if (CartDAO.isCartEmpty(user.getId())) {
      response.sendRedirect(getServletContext().getContextPath() + "/cart");
      return;
    }

    OrderDTO newOrderDTO = new OrderDTO();
    newOrderDTO.setUserId(user.getId());
    newOrderDTO.setAddress(address);
    newOrderDTO.setPhone(phone);
    newOrderDTO.setStatus("pending");

    Integer orderId = OrderDAO.initOrder(newOrderDTO);
    int result = OrderDAO.createOrder(orderId, user.getId());
    String url = "/orders";
    response.sendRedirect(getServletContext().getContextPath() + url);
  }
}

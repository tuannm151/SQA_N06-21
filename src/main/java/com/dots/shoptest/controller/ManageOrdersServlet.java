package com.dots.shoptest.controller;

import com.dots.shoptest.dao.CartDAO;
import com.dots.shoptest.dao.OrderDAO;
import com.dots.shoptest.dto.OrderDTO;
import com.dots.shoptest.model.Order;
import com.dots.shoptest.model.User;
import com.dots.shoptest.utils.Auth;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "OrdersServlet", value = "/admin/orders")
public class ManageOrdersServlet extends HttpServlet {

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
        if (!user.getRole().equals("admin")) {
            response.sendRedirect(getServletContext().getContextPath() + "/login");
            return;
        }
        ArrayList<OrderDTO> orderDTOS = OrderDAO.getOrders();
        String url = "/admin/orders.jsp";

        if (orderDTOS == null) {
            request.setAttribute("error", "Error while getting orders");
        } else if (orderDTOS.size() == 0) {
            request.setAttribute("isEmpty", true);
        } else {
            request.setAttribute("orders", orderDTOS);
            Order lastestOrder = OrderDAO.getOrderByIdAndUserId(
                    orderDTOS.get(0).getId(),
                    user.getId()
            );
            request.setAttribute(
                    "cartCount",
                    CartDAO.getCartItemsCount(user.getId())
            );
        }
        request.getRequestDispatcher(url).forward(request, response);
    }


}

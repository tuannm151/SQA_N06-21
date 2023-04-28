package com.dots.shoptest.controller;

import com.dots.shoptest.dao.CartDAO;
import com.dots.shoptest.dao.UserDAO;
import com.dots.shoptest.model.JsonResponse;
import com.dots.shoptest.model.User;
import com.dots.shoptest.utils.Auth;
import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@WebServlet(name = "CustomerServlet", value = "/admin/customers")
public class CustomerServlet extends HttpServlet {
    private final Gson gson = new Gson();
    @Override
    public void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException, ServletException {
        User user = Auth.getAuthenticatedUser(request);
        if (user == null) {
            response.sendRedirect(getServletContext().getContextPath() + "/login");
            return;
        }
        String action = request.getParameter("action");

        if (Objects.equals(action, "getCustomerJson")) {
            doGet_CustomerJson(request, response);
            return;
        }

        if (user.getRole().equals("admin")) {
            List<User> users = UserDAO.getAllUsers();
            request.setAttribute("customers", users);
            request.setAttribute(
                    "cartCount",
                    CartDAO.getCartItemsCount(user.getId())
            );
            getServletContext()
                .getRequestDispatcher("/admin/customers.jsp")
                .forward(request, response);
        } else {
            response.sendRedirect(getServletContext().getContextPath() + "/login");
        }
    }

    private void doGet_CustomerJson(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws IOException {
        int userId = Integer.parseInt(request.getParameter("customerId"));
        User user = UserDAO.getUserById(userId);
        if(user == null) {
            response.setStatus(404);
            return;
        }
        String userJsonString = gson.toJson(user);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();
        out.print(userJsonString);
        out.flush();
    }

    @Override
    public void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException, ServletException {
        User user = Auth.getAuthenticatedUser(request);
        if (user == null) {
            response.sendRedirect(getServletContext().getContextPath() + "/login");
            return;
        }
        String action = request.getParameter("action");
        if(Objects.equals(action, "saveCustomer")) {
            Integer customerId = Integer.parseInt(request.getParameter("customerId"));
            BufferedReader reader = request.getReader();
            Gson gson = new Gson();
            User customer = gson.fromJson(reader, User.class);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();

            List<String> errors = new ArrayList<>();
            if(customer.getName().isEmpty()) {
                errors.add("Name is required");
            }
            if(customer.getEmail().isEmpty()) {
                errors.add("Email is required");
            }
            if(!customer.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                errors.add("Email is invalid");
            }
            String[] roles = {"admin", "user"};
            if(customer.getRole().isEmpty()) {
                errors.add("Role is required");
            }
            if(!Arrays.asList(roles).contains(customer.getRole())) {
                errors.add("Role is invalid");
            }
            if(errors.size() > 0) {
                String jsonResponseString = gson.toJson(JsonResponse.error(errors.get(0)));
                out.print(jsonResponseString);
                out.flush();
                return;
            }
            UserDAO.updateUser(customerId, customer);
            out.print(gson.toJson(JsonResponse.success("Customer updated successfully")));
            out.flush();
        }
        if(Objects.equals(action, "deleteCustomer")) {
            int customerId = Integer.parseInt(request.getParameter("customerId"));
            UserDAO.deleteUser(customerId);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(gson.toJson(JsonResponse.success("Customer deleted successfully")));
            out.flush();
        }
        if(Objects.equals(action, "addCustomer")) {
            BufferedReader reader = request.getReader();
            Gson gson = new Gson();
            User customer = gson.fromJson(reader, User.class);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();

            List<String> errors = new ArrayList<>();
            if(customer.getName().isEmpty()) {
                errors.add("Name is required");
            }
            if(customer.getEmail().isEmpty()) {
                errors.add("Email is required");
            }
            if(!customer.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                errors.add("Email is invalid");
            }
            String[] roles = {"admin", "user"};
            if(customer.getRole().isEmpty()) {
                errors.add("Role is required");
            }
            if(!Arrays.asList(roles).contains(customer.getRole())) {
                errors.add("Role is invalid");
            }
            if(customer.getPassword().isEmpty()) {
                errors.add("Password is required");
            }

            if(UserDAO.hasUserByEmail(customer.getEmail())) {
                errors.add("Email already exists");
            }

            if(errors.size() > 0) {
                String jsonResponseString = gson.toJson(JsonResponse.error(errors.get(0)));
                out.print(jsonResponseString);
                out.flush();
                return;
            }
            UserDAO.addUser(customer);
            out.print(gson.toJson(JsonResponse.success("Customer added successfully")));
            out.flush();
        }
    };
}

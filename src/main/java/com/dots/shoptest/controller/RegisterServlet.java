package com.dots.shoptest.controller;

import com.dots.shoptest.dao.UserDAO;
import com.dots.shoptest.model.User;
import com.dots.shoptest.utils.Auth;
import com.dots.shoptest.utils.UserForm;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet(name = "RegisterServlet", value = "/register")
public class RegisterServlet extends HttpServlet {

  @Override
  protected void doGet(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws ServletException, IOException {
    if (Auth.getAuthenticatedUser(request) == null) {
      getServletContext()
        .getRequestDispatcher("/register.jsp")
        .forward(request, response);
      return;
    }
    response.sendRedirect(getServletContext().getContextPath() + "/");
  }

  @Override
  protected void doPost(
    HttpServletRequest request,
    HttpServletResponse response
  ) throws ServletException, IOException {
    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    String url = "/register.jsp";

    String name = request.getParameter("name");
    String password = request.getParameter("password");
    String repassword = request.getParameter("repassword");
    String email = request.getParameter("email");
    UserForm userForm = new UserForm(name, email, password, repassword);

    boolean formIsValid = userForm.isValid();
    if (!formIsValid) {
      String error = userForm.getError();
      request.setAttribute("error", error);
      getServletContext().getRequestDispatcher(url).forward(request, response);
      return;
    }

    User newUser = new User(name, email, password);
    int responseCode = UserDAO.registerUser(newUser);
    if (responseCode == -1) {
      request.setAttribute("error", "Email is already registered");
      request.setAttribute("name", name);
      request.setAttribute("email", email);
      request.setAttribute("password", password);
    } else {
      request.setAttribute("success", "Registration successful");
      url = "/login";
    }
    getServletContext().getRequestDispatcher(url).forward(request, response);
  }
}

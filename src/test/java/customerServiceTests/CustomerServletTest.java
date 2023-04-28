package customerServiceTests;

import com.dots.shoptest.controller.CustomerServlet;
import com.dots.shoptest.dao.UserDAO;
import com.dots.shoptest.model.User;
import com.dots.shoptest.utils.Auth;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class CustomerServletTest {
    private HttpServletRequest request;
    private HttpServletResponse response;
    private StringWriter stringWriter;
    private CustomerServlet customerServlet;
    private Gson gson;

    @Mock
    private UserDAO UserDAO;

    @BeforeEach
    void setUp() throws IOException {
        request = Mockito.mock(HttpServletRequest.class);
        response = Mockito.mock(HttpServletResponse.class);
        stringWriter = new StringWriter();
        customerServlet = new CustomerServlet();
        gson = new Gson();
        when(response.getWriter()).thenReturn(new PrintWriter(stringWriter));
    }

    @Test
    void testDoGet_AdminRole() throws ServletException, IOException {
        User user = new User();
        user.setRole("admin");
        when(Auth.getAuthenticatedUser(request)).thenReturn(user);
        when(request.getParameter("action")).thenReturn(null);
        when(UserDAO.getAllUsers()).thenReturn(Arrays.asList(new User(), new User()));

        customerServlet.doGet(request, response);

        verify(request).setAttribute(eq("customers"), any());
        verify(request).setAttribute(eq("cartCount"), any());
        verify(request.getRequestDispatcher("/admin/customers.jsp")).forward(request, response);
    }

    @Test
    void testDoGet_NoAuth() throws ServletException, IOException {
        when(Auth.getAuthenticatedUser(request)).thenReturn(null);

        customerServlet.doGet(request, response);

        verify(response).sendRedirect("/login");
    }

    @Test
    void testDoGet_GetCustomerJson() throws ServletException, IOException {
        User user = new User();
        user.setId(1);
        user.setName("John");
        user.setEmail("john@example.com");
        when(request.getParameter("action")).thenReturn("getCustomerJson");
        when(request.getParameter("customerId")).thenReturn("1");
        when(UserDAO.getUserById(1)).thenReturn(user);

        customerServlet.doGet(request, response);
    }

    @Test
    void testDoPost_SaveCustomer_Success() throws ServletException, IOException {
        User user = new User();
        user.setId(1);
        user.setName("John");
        user.setEmail("john@example.com");
        user.setRole("user");

        when(Auth.getAuthenticatedUser(request)).thenReturn(new User());
        when(request.getParameter("action")).thenReturn("saveCustomer");
        when(request.getParameter("customerId")).thenReturn("1");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(gson.toJson(user))));
        when(UserDAO.updateUser(1, user)).thenReturn(1);

        customerServlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        String expectedJson = "{\"status\":\"success\",\"message\":\"Customer updated successfully\"}";
        verify(response.getWriter()).print(expectedJson);
    }

    @Test
    void testDoPost_SaveCustomer_InvalidData() throws ServletException, IOException {
        User user = new User();
        user.setId(1);
        user.setName("");
        user.setEmail("invalid-email");
        user.setRole("invalid-role");
        when(Auth.getAuthenticatedUser(request)).thenReturn(new User());
        when(request.getParameter("action")).thenReturn("saveCustomer");
        when(request.getParameter("customerId")).thenReturn("1");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(gson.toJson(user))));
        when(UserDAO.updateUser(1, user)).thenReturn(0);

        customerServlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        String expectedJson = "{\"status\":\"error\",\"message\":\"Invalid data\"}";
        verify(response.getWriter()).print(expectedJson);
    }
}



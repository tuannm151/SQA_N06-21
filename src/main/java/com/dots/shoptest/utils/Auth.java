package com.dots.shoptest.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dots.shoptest.dao.UserDAO;
import com.dots.shoptest.model.User;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.mindrot.jbcrypt.BCrypt;

public class Auth {

  private static final String SECRET = "2903qaz";

  public static boolean isEmailValid(String email) {
    final Pattern EMAIL_REGEX = Pattern.compile(
      "[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
      Pattern.CASE_INSENSITIVE
    );
    return !EMAIL_REGEX.matcher(email).matches();
  }

  public static String hashPassword(String password) {
    return BCrypt.hashpw(password, BCrypt.gensalt(12));
  }

  public static User getAuthenticatedUser(HttpServletRequest request) {
    User user = (User) request.getSession().getAttribute("user");
    if (user != null) {
      return user;
    }
    Cookie[] cookies = request.getCookies();
    if (cookies == null) {
      return null;
    }
    for (Cookie cookie : cookies) {
      if (cookie.getName().equals("token")) {
        String token = cookie.getValue();
        try {
          String email = JWT
            .require(Algorithm.HMAC256(SECRET.getBytes()))
            .build()
            .verify(token)
            .getClaim("email")
            .asString();
          user = UserDAO.findUserByEmail(email);
          if (user != null) {
            request.getSession().setAttribute("user", user);
            return user;
          }
        } catch (Exception e) {
          return null;
        }
      }
    }

    return null;
  }

  public static String createToken(String email) {
    Algorithm algorithm = Algorithm.HMAC256(SECRET.getBytes());
    return JWT.create().withClaim("email", email).sign(algorithm);
  }
}

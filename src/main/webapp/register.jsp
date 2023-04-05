<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
      href="https://fonts.googleapis.com/css2?family=Sora:wght@100;200;300;400;500;600;700&display=swap"
      rel="stylesheet"
    />
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css"
    />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/normalize.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" />
    <script defer src="${pageContext.request.contextPath}/js/auth.js"></script>
    <title>Register</title>
  </head>
  <body>
    <div class="wrapper">
      <div class="container">
        <h2 class="form-title">Register</h2>
        <div class="icon-box">
          <i class="bi bi-bootstrap icon"></i>
        </div>
        <form action="${pageContext.request.contextPath}/register" method="post" id="form">
          <div class="field-group">
            <input
              type="text"
              name="name"
              id="name"
              class="field-group__input ${name != null ? 'has-value' : ''}"
              value="${name != null ? name : ''}"
            />
            <label for="name" class="field-group__label">Full Name</label>
            <span class="field-group__error"></span>
          </div>
          <div class="field-group">
            <input
              type="text"
              name="email"
              id="email"
              class="field-group__input ${email != null ? 'has-value' : ''}"
              value="${email != null ? email : ''}"
            />
            <label for="email" class="field-group__label">Email</label>
            <span class="field-group__error"></span>
          </div>
          <div class="field-group">
            <input
              type="password"
              name="password"
              id="password"
              class="field-group__input ${password != null ? 'has-value' : ''}"
              value="${password != null ? password : ''}"
            />
            <label for="password" class="field-group__label">Password</label>
            <span class="field-group__error"></span>
          </div>
          <div class="field-group">
            <input
              type="password"
              name="repassword"
              id="repassword"
              class="field-group__input"
            />
            <label for="repassword" class="field-group__label">Re-type password</label>
            <span class="field-group__error"></span>
          </div>
          <c:if test="${success != null}">
            <span class="form-alert-text form-alert-text--success">
              <c:out value="${success}" />
            </span>
          </c:if>
          <c:if test="${error != null}">
            <span class="form-alert-text form-alert-text--error">
              <c:out value="${error}" />
            </span>
          </c:if>
          <button type="submit" class="btn btn--submit">Register</button>
        </form>

        <div class="form-footer">
          <a href="${pageContext.request.contextPath}/login" class="form-footer__link">Don't have an account? Login here</a>
        </div>
    </div>
    </div>
  </body>
</html>

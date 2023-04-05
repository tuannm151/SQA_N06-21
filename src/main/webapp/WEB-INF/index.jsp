<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css"
    />
    <link rel="preconnect" href="https://fonts.googleapis.com" />
    <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
    <link
      href="https://fonts.googleapis.com/css2?family=Sora:wght@100;200;300;400;500;600;700&display=swap"
      rel="stylesheet"
    />
    <script defer>const ctx = "${pageContext.request.contextPath}"</script>
    <script defer src="${pageContext.request.contextPath}/js/index.js"></script>
    <script defer src="${pageContext.request.contextPath}/js/sticky.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/normalize.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/index.css" />
    <title>ShopTest</title>
  </head>
  <body>
    <header class="header" id="header">
      <div class="header-left">
        <div class="logo">
          <i class="bi bi-bootstrap icon"></i>
        </div>
        <div class="hamburger">
          <span class="bar"></span>
          <span class="bar"></span>
          <span class="bar"></span>
        </div>
        <c:if test="${!empty sessionScope.user}">
          <span class="user-email">/ ${sessionScope.user.email} </span>
        </c:if>
      </div>

      <ul class="nav">
        <li class="nav__item">
          <a href="${pageContext.request.contextPath}/" class="nav__link"
          >Home</a
          >
        </li>
        <li class="nav__item">
          <a href="${pageContext.request.contextPath}/cart" class="nav__link"
          >Cart</a
          >
        </li>
        <li class="nav__item">
          <a href="${pageContext.request.contextPath}/orders" class="nav__link">My Orders</a>
        </li>
        <c:choose>
          <c:when test="${empty sessionScope.user}">
            <li class="nav__item">
              <a href="${pageContext.request.contextPath}/login" class="nav__link">Login</a>
            </li>
          </c:when>
          <c:otherwise>
            <li class="nav__item">
              <a href="${pageContext.request.contextPath}/logout" class="nav__link">Logout</a>
            </li>
          </c:otherwise>
        </c:choose>
      </ul>
      <a href="${pageContext.request.contextPath}/cart" class="cart-logo ${empty sessionScope.user ? "notAuth" : ""}">
        <i class="bi bi-cart-check cart__icon"></i>
        <span class="cart__count" id="cartCount" data-cart-count="${cartCount}">${cartCount}</span>
      </a>
    </header>
    <section class="products">
      <div class="products__header">
        <h2 class="products__title">All CD</h2>
      </div>
      <c:choose>
        <c:when test="${products.size() == 0}">
          <div class="error-message">
            <h3>No products</h3>
          </div>
        </c:when>
        <c:when test="${error != null}">
          <div class="error-message">
            <h3>${error}</h3>
          </div>
        </c:when>
        <c:otherwise>
          <div class="products-container">
            <c:forEach var ="product" items="${products}">

              <div class="product__card">
                <div class="product__image-box">
                  <img
                          src="${product.imageUrl}"
                          alt="product-1"
                  />
                </div>
                <div class="product__info" data-productId="${product.id}">
                  <div class="product__detail">
                    <span class="product__category ${product.getFormattedCategoryName()}">${product.categoryName}</span>
                    <h3 class="product__title">${product.name}</h3>
                  </div>

                  <div class="product__price">
                    <span class="product__price-text">Price:</span>
                    <span class="product__price-value">
                  <fmt:formatNumber
                          value="${product.price}" minFractionDigits="2"/>$
                </span>
                  </div>
                  <button class="btn-add">
                    <i class="bi bi-cart-plus"></i>
                  </button>
                </div>
              </div>
            </c:forEach>
          </div>
        </c:otherwise>
      </c:choose>
    </section>
  </body>
</html>

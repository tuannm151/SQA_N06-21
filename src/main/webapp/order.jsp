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

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/normalize.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/order.css" />

    <script defer>
      const ctx = "${pageContext.request.contextPath}";
    </script>
    <script defer src="${pageContext.request.contextPath}/js/sticky.js"></script>
    <script defer src="${pageContext.request.contextPath}/js/order.js"></script>
    <title>Orders</title>
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
        <span class="user-email">/ ${sessionScope.user.email}</span>
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
      <a href="${pageContext.request.contextPath}/cart" class="cart-logo">
        <i class="bi bi-cart-check cart__icon"></i>
        <span  class="cart__count" id="cartCount" data-cart-count="${cartCount}">${cartCount}</span>
      </a>
    </header>
    <section class="order-section">
      <div class="order-container">
        <div class="order__content">
          <h2 class="order__title">My Orders</h2>
          <div class="order__header">
            <span class="order__header-item">Number</span>
            <span class="order__header-item">Order</span>
            <span class="order__header-item">Date</span>
            <span class="order__header-item">Total</span>
            <span class="order__header-item">Status</span>
          </div>
          <ul class="order__items">
            <c:choose>
              <c:when test="${isEmpty eq true}">
                <h3 class="error-message">You don't have any orders yet.</h3>
              </c:when>
              <c:when test="${error ne null}">
                <h3 class="error-message">${error}</h3>
              </c:when>
              <c:otherwise>
                <c:forEach var="order" items="${orders}">
                  <li class="order__item" data-order-id="${order.id}">
                    <span class="order__item-info">OD${order.id}</span>
                    <span class="order__item-info">${order.itemCount} ${order.itemCount > 1 ? 'items' : 'item'}</span>
                    <span class="order__item-info">${order.createdDate}</span>
                    <span class="order__item-info">$<fmt:formatNumber
                            value="${order.totalPrice}" minFractionDigits="2"/></span>
                    <span class="order__item-info">${order.status}</span>
                    <button class="order__item-action">
                      <i class="bi bi-eye"></i> See detail
                    </button>
                  </li>
                </c:forEach>
              </c:otherwise>
            </c:choose>

          </ul>
          <span class="order-instruction">Click at an order to show detail.</span>
        </div>
        <div class="order__detail">
          <c:choose>
            <c:when test="${isEmpty eq true}">
            </c:when>
            <c:when test="${error ne null}">
            </c:when>
            <c:otherwise>
              <div class="order__detail-header">
                <h3 class="order__detail-title">Order Summary</h3>
                <span class="order__detail-id">#OD${lastestOrder.id}</span>
              </div>

              <div class="order__detail-content">
                <c:forEach var="orderItem" items="${lastestOrder.orderItems}">
                  <div class="order__detail-item">
                    <div class="order__detail-img">
                      <img src="${orderItem.product.imageUrl}" alt="A product" />
                    </div>
                    <div class="order__detail-info">
                      <div class="order__detail-name">
                        <span>${orderItem.product.name}</span>
                      </div>
                      <div class="order__detail-price">
                    <span>$<fmt:formatNumber
                            value="${orderItem.totalPrice}" minFractionDigits="2"/></span>
                      </div>
                      <div class="order__detail-quantity">
                        <span>Quantity: ${orderItem.quantity}</span>
                      </div>
                    </div>
                  </div>
                </c:forEach>

              </div>
              <div class="order__detail-contact">
                <h3 class="order__detail-title order__detail-title--contact">
                  Shipping
                </h3>
                <div class="order__detail-contact-item">
                  <i class="bi bi-geo-alt"></i>
                  <span class="order__detail-contact-text"
                  >${lastestOrder.address}
                  </span>
                </div>
                <div class="order__detail-contact-item">
                  <i class="bi bi-telephone"></i>
                  <span class="order__detail-contact-text">${lastestOrder.phone}</span>
                </div>
              </div>
              <div class="checkout__cart-info">
            <span class="checkout__cart-info-item-label"
            >Subtotal (<span id="checkout-cart-count">${lastestOrder.getOrderItemsCount()}</span>)</span
            >
                <span class="checkout__cart-info-item-value">$<fmt:formatNumber
                        value="${lastestOrder.totalPrice}" minFractionDigits="2"/></span>
                <span class="checkout__cart-info-item-label">Shipping</span>
                <span class="checkout__cart-info-item-value">$0</span>
                <span class="checkout__cart-info-item-label item-text--total"
                >Total price</span
                >
                <span class="checkout__cart-info-item-value item-text--total"
                >$<fmt:formatNumber
                        value="${lastestOrder.totalPrice}" minFractionDigits="2"/></span
                >
              </div>
            </c:otherwise>
          </c:choose>

        </div>
      </div>
    </section>
  </body>
</html>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">

        <head>
            <meta charset="UTF-8" />
            <meta http-equiv="X-UA-Compatible" content="IE=edge" />
            <meta name="viewport" content="width=device-width, initial-scale=1.0" />
            <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.1/font/bootstrap-icons.css" />
            <link rel="preconnect" href="https://fonts.googleapis.com" />
            <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
            <link href="https://fonts.googleapis.com/css2?family=Sora:wght@100;200;300;400;500;600;700&display=swap"
                rel="stylesheet" />

            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css" />
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/normalize.css" />
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css" />
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/order.css" />
            <link rel="stylesheet" href="${pageContext.request.contextPath}/css/modal.css" />
            <link href="https://unpkg.com/tabulator-tables@5.4.4/dist/css/tabulator.min.css" rel="stylesheet">

            <script defer>
                const ctx = "${pageContext.request.contextPath}";
            </script>

            <script defer type="text/javascript"
                src="https://unpkg.com/tabulator-tables@5.4.4/dist/js/tabulator.min.js"></script>
            <script defer src="${pageContext.request.contextPath}/js/sticky.js"></script>
            <script defer src="${pageContext.request.contextPath}/js/admin/customers.js"></script>
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
                        <a href="${pageContext.request.contextPath}/" class="nav__link">Home</a>
                    </li>
                    <li class="nav__item">
                        <a href="${pageContext.request.contextPath}/cart" class="nav__link">Cart</a>
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
                            <c:if test="${sessionScope.user.role == 'admin'}">
                                <li class="nav__item">
                                    <a href="${pageContext.request.contextPath}/admin/customers" class="nav__link">Manage Customer</a>
                                </li>
                                <li class="nav__item">
                                    <a href="${pageContext.request.contextPath}/admin/orders" class="nav__link">Manage Orders</a>
                                </li>
                            </c:if>
                            <li class="nav__item">
                                <a href="${pageContext.request.contextPath}/logout" class="nav__link">Logout</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
                <a href="${pageContext.request.contextPath}/cart" class="cart-logo">
                    <i class="bi bi-cart-check cart__icon"></i>
                    <span class="cart__count" id="cartCount" data-cart-count="${cartCount}">${cartCount}</span>
                </a>
            </header>
            <section class="order-section">
                <div class="order-container">
                    <div class="order__content">
                        <div class="table__header-main">
                            <h2 class="order__title">All Customers</h2>
                            <button type="submit" id="btnAdd" class="btn btn-primary">Add user</button>
                        </div>
                        <div class="order__header customer">
                            <span class="order__header-item">Customer Id</span>
                            <span class="order__header-item">Name</span>
                            <span class="order__header-item">Email</span>
                            <span class="order__header-item">Role</span>
                        </div>
                        <ul class="order__items">
                            <c:choose>
                                <c:when test="${isEmpty eq true}">
                                    <h3 class="error-message">You don't have any customers yet.</h3>
                                </c:when>
                                <c:when test="${error ne null}">
                                    <h3 class="error-message">${error}</h3>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="customer" items="${customers}">
                                        <li class="order__item customer" data-customer-id="${customer.id}">
                                            <span class="order__item-info">C${customer.id}</span>
                                            <span class="order__item-info">${customer.name}</span>
                                            <span class="order__item-info">${customer.email}</span>
                                            <span class="order__item-info">${customer.role}</span>
                                        </li>
                                    </c:forEach>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </div>
                </div>
            </section>
            <div class="form-popup-wrapper">
                <div class="form-popup-overlay"></div>
                <form class="form-popup">
                    <h3 class="form-title">
                        Edit customer
                    </h3>
                    <div class="form-group">
                        <div class="form-field-group">
                            <label for="name">Name</label>
                            <input type="text" name="name" id="name" required>
                        </div>
                        <div class="form-field-group">
                            <label for="email">Email</label>
                            <input type="email" name="email" id="email" required>
                        </div>
                        <div id="field-password" class="form-field-group">
                            <label for="password">Password</label>
                            <input type="password" name="password" id="password" required>
                        </div>
                        <label for="role">Role</label>
                        <select name="role" id="role" required>
                            <option value="">Select Role</option>
                            <option value="user">User</option>
                            <option value="admin">Admin</option>
                        </select>
                        <h3 class="form-error-message"></h3>
                    </div>
                    <div class="form-action">
                        <button type="button" id="btnCancel" class="btn btn-secondary">Cancel</button>
                        <div class="form-action-right">
                            <button type="submit" id="btnDelete" class="btn btn-primary">Delete</button>
                            <button type="submit" id="btnSave" class="btn btn-primary">Save</button>
                        </div>
                    </div>
                </form>
            </div>
        </body>
</html>
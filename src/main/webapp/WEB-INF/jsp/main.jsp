<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.jwd.web.model.UserRole" %>
<%@ page import="java.time.LocalDate" %>
<html>
<head>
    <title>Welcome</title>
</head>
<body>
<h3>Welcome</h3>
<hr/>
<c:choose>
    <c:when test="${not empty sessionScope.user}">
        Hello, ${sessionScope.user.login} , Role ${sessionScope.user.role.name}
        <a href="controller?command=logout">Logout</a>
        <a href="controller?command=show_user_orders">My orders</a>
        <a href="controller?command=show_account">My account</a>
        <c:if test="${sessionScope.user.role eq UserRole.ADMIN}">
            <a href="controller?command=show_users">Manage users</a>
            <a href="controller?command=show_books">Manage books</a>
            <a href="controller?command=show_orders">Manager orders</a>
        </c:if>
        <c:if test="${sessionScope.user.role eq UserRole.LIBRARIAN}">
            <a href="controller?command=show_orders">Manager orders</a>
        </c:if>

    </c:when>
    <c:otherwise>
        <a href="controller?command=show_login">Login</a>
        <a href="controller?command=show_register">Register</a>
    </c:otherwise>
</c:choose>
<hr/>
<c:if test="${not empty books }">
    <ul>
        <c:forEach var="order" items="${books}">
            <ul>
                <c:if test="${order.copiesAmount gt 0}">
                    <li>
                            ${order.name}, ${order.author.name}, ${order.genre.name}, ${order.date}, ${order.pagesAmount}, ${order.copiesAmount}, ${order.description}
                        <c:if test="${not empty user}">
                            <c:choose>
                                <c:when test="${empty user.subscription}">
                                    <a href="controller?command=order_book&id=${order.id}">Order book</a>
                                </c:when>
                                <c:when test="${LocalDate.now() ge user.subscription.startDate and LocalDate.now() le user.subscription.endDate}">
                                    <a href="controller?command=read&id=${order.id}">Read book</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="controller?command=order_book?id=${order.id}">Order book</a>
                                </c:otherwise>
                            </c:choose>
                        </c:if>
                    </li>
                </c:if>
            </ul>
        </c:forEach>
    </ul>
</c:if>
</body>
</html>
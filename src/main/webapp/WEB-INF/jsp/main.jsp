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
${sessionScope.commandResult}
<hr/>
<c:if test="${not empty sessionScope.user}">
    <form name="find_by_name" action="controller?command=find_by_name" method="POST">
        Find book by name <input type="text" name="name">
        <input type="submit" value="find">
    </form>
    <form name="find_by_author" action="controller?command=find_by_author" method="POST">
        Find Books by author <input type="text" name="name">
        <input type="submit" value="find">
    </form>
    <form name="find_by_genre" action="controller?command=find_by_genre" method="POST">
        Find books by genre <input type="text" name="name">
        <input type="submit" value="find">
    </form>
    <a href="controller?command=main">All books</a>
</c:if>
<hr/>
<c:if test="${not empty books }">
        <ul>
            <c:forEach var="book" items="${books}">
                <li>
                        ${book}
                    <c:if test="${not empty sessionScope.user}">
                            <a href="controller?command=order_book&id=${book.id}">Order</a>
                    </c:if>
                </li>
            </c:forEach>
        </ul>
</c:if>
</body>
</html>
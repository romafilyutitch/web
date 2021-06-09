<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.jwd.web.model.UserRole" %>
<html>
<head>
    <title>Welcome</title>
</head>
<body>
<h3>Welcome</h3>
<hr/>
<c:choose>
    <c:when test="${not empty sessionScope.user}">
        Hello, ${sessionScope.user}
        <a href="controller?command=logout">Logout</a>
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
<c:if test="${not empty books }" >
    <ul>
    <c:forEach var="elem" items="${books}">
        <li><c:out value="${elem.name},
         ${elem.author.name},
         ${elem.genre.name},
         ${elem.date},
         ${elem.pagesAmount},
         ${elem.booksAmount},
         ${elem.description}"/></li>
    </c:forEach>
    </ul>
</c:if>
</body>
</html>

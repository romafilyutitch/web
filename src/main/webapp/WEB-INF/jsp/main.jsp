<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: roma0
  Date: 08.06.2021
  Time: 1:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
    </c:when>
    <c:otherwise>
        <a href="controller?command=show_login">Login</a>
    </c:otherwise>
</c:choose>
<hr/>
<a href="controller?command=show_users">Users list</a>
<a href="controller?command=show_register">Register</a>
<c:if test="${not empty books }" >
    <ul>
    <c:forEach var="elem" items="${books}">
        <li><c:out value="${elem.name},
         ${elem.author.name},
         ${elem.genre.name},
         ${elem.date},
         ${elem.pagesAmount},
         ${elem.description}"/></li>
    </c:forEach>
    </ul>
</c:if>
</body>
</html>

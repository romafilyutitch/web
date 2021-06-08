<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: roma0
  Date: 08.06.2021
  Time: 18:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Register user page</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty error}">
        <c:out value="${error}"/>
        <a href="controller?command=show_register">Try again</a>
    </c:when>
    <c:otherwise>
        <form name = "loginForm" method="POST" action="controller?command=register">
            Login:<br/>
            <input type="text" name = "login" value=""/>
            <br/>Password:<br/>
            <input type="password" name="password" value=""/>
            <br/>
                ${error}
            <input type="submit" value="Register"/>
        </form>
    </c:otherwise>
</c:choose>
<a href ="controller?command=main">Main page</a>
</body>
</html>

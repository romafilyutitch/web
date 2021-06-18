<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="main"/>
<html>
<head>
    <title>Register page</title>
</head>
<body>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        ${requestScope.error}
        <a href="controller?command=show_register"><fmt:message key="register.tryAgain"/></a>
    </c:when>
    <c:otherwise>
        <form name = "loginForm" method="POST" action="controller">
            <input type="hidden" name="command" value="register">
            <label>Login:<input type="text" required name = "login" value=""/></label>
            <label>Password:<input type="password" required name="password" value=""/></label>
            <input type="submit" value="Register"/>
        </form>
    </c:otherwise>
</c:choose>
<a href ="controller?command=main">Main page</a>
</body>
</html>

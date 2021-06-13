<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="main"/>
<html>
<head>
    <title><fmt:message key="register.title"/></title>
</head>
<body>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <c:out value="${requestScope.error}"/>
        <a href="controller?command=show_register"><fmt:message key="register.tryAgain"/></a>
    </c:when>
    <c:otherwise>
        <form name = "loginForm" method="POST" action="controller?command=register">
            <label>Login:<input type="text" name = "login" value=""/></label>
            <label>Password:<input type="password" name="password" value=""/></label>
            <input type="submit" value="Register"/>
        </form>
    </c:otherwise>
</c:choose>
<a href ="controller?command=main"><fmt:message key="main"/></a>
</body>
</html>

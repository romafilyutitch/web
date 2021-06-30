<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="register"/>
<html>
<head>
    <title><fmt:message key="title"/></title>
</head>
<body>
<c:choose>
    <c:when test="${not empty requestScope.error}">
        <fmt:message key="${requestScope.error}"/>
        <a href="controller?command=show_register"><fmt:message key="try"/></a>
    </c:when>
    <c:otherwise>
        <form name = "loginForm" method="POST" action="controller">
            <input type="hidden" name="command" value="register">
            <label><fmt:message key="login"/><input type="text" required pattern="^\S[A-Za-z\s]+$" name = "login" value=""/></label>
            <label><fmt:message key="password"/><input type="password" required pattern="^\S[A-Za-z\s]+$" name="password" value=""/></label>
            <input type="submit" value="Register"/>
        </form>
    </c:otherwise>
</c:choose>
<a href ="controller?command=main"><fmt:message key="main"/></a>
</body>
</html>

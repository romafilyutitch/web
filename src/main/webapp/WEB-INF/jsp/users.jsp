<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="by.epam.jwd.web.model.UserRole" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="main"/>
<html>
<head>
    <title><fmt:message key="users.title"/></title>
</head>
<body>
<h1><fmt:message key="users.list"/></h1>
<c:if test="${not empty requestScope.users}">
    <ul>
    <c:forEach var="book" items="${requestScope.users}">
        <li> <c:if test="${sessionScope.user.id eq book.id}">YOU</c:if>
        ${book.login} ,${book.role.name}, ${book.subscription}
            <c:if test="${book.role ne UserRole.ADMIN}">
                <form name="promote role" method="POST" action="controller?command=promote_role">
                    <input type="hidden" name="id" value="${book.id}">
                    <input type="submit" value="Promote user role">
                </form>
            </c:if>
            <c:if test="${book.role ne UserRole.READER}">
                <form name="demote role" method="POST" action="controller?command=demote_role">
                    <input type="hidden" name="id" value="${book.id}">
                    <input type="submit" value="Demote user role">
                </form>
            </c:if>
        <a href = "controller?command=show_set_subscription_page&id=${book.id}"><fmt:message key="users.setSubscription"/></a></li>
    </c:forEach>
    </ul>
</c:if>
<a href="controller?command=main"><fmt:message key="main"/></a>
</body>
</html>

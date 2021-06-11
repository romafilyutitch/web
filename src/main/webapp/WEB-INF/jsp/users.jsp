<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: roma0
  Date: 08.06.2021
  Time: 12:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.jwd.web.model.UserRole" %>
<html>
<head>
    <title>Users list</title>
</head>
<body>
<h1>Users list</h1>
<c:if test="${not empty users}">
    <ul>
    <c:forEach var="elem" items="${users}">
        <li> <c:if test="${sessionScope.user.id eq elem.id}">YOU</c:if>
        ${elem}
            <c:if test="${elem.role ne UserRole.ADMIN}">
                <a href = "controller?command=promote_role&id=${elem.id}">Promote user role</a>
            </c:if>
            <c:if test="${elem.role ne UserRole.READER}">
                <a href = "controller?command=demote_role&id=${elem.id}">Demote user role</a>
            </c:if>
        <a href = "controller?command=show_set_subscription_page&id=${elem.id}">Set subscription</a>
        <a href = "controller?command=delete_user&id=${elem.id}">Delete user</a></li>
    </c:forEach>
    </ul>
</c:if>
<a href="controller?command=main">Main Page</a>
</body>
</html>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: roma0
  Date: 08.06.2021
  Time: 12:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Users list</title>
</head>
<body>
<h1>Users list</h1>
<c:if test="${not empty users}">
    <ul>
    <c:forEach var="elem" items="${users}">
        <li><c:out value="${elem.login},
        ${elem.password},
        ${elem.role.roleName},
        ${elem.userSubscription.startDate},
        ${elem.userSubscription.endDate}"/></li>
    </c:forEach>
    </ul>
</c:if>
<a href="controller?command=main">MainPage</a>
</body>
</html>

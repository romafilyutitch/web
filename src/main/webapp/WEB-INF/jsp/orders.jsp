<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: roma0
  Date: 09.06.2021
  Time: 18:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.jwd.web.model.Status" %>
<html>
<head>
    <title>Orders management</title>
</head>
<body>
    <c:if test="${not empty orders}">
        <ul>
        <c:forEach var="order" items="${orders}">
            <li>
                ${order.user.login}, ${order.book.name}, ${order.status.name}
                <c:if test="${order.status eq Status.ORDERED}">
                    <a href="controller?command=approve&id=${order.id}">Approve order</a>
                </c:if>
            </li>
        </c:forEach>
        </ul>
    </c:if>
<a href="controller?command=main">Main Page</a>
</body>
</html>

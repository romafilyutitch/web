<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: roma0
  Date: 11.06.2021
  Time: 22:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="by.epam.jwd.web.model.Status" %>
<html>
<head>
    <title>My orders</title>
</head>
<body>
    <c:if test="${not empty orders}">
        <ul>
            <c:forEach var="order" items="${orders}">
                <li>
                        ${order.book.name}
                    <c:if test="${order.status eq Status.APPROVED}">
                        <a href="controller?command=read&id=${order.book.id}">Read book</a>
                        <a href="controller?command=delete_order&id=${order.id}">Return book</a>
                    </c:if>
                </li>
            </c:forEach>
        </ul>
    </c:if>
<a href="controller?command=main">Main page</a>
</body>
</html>

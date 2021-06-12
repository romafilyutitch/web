<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: roma0
  Date: 09.06.2021
  Time: 18:07
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Books menegment</title>
</head>
<body>
<c:if test="${not empty books }" >
    <ul>
        <c:forEach var="order" items="${books}">
            <li>${order.id}, ${order.name}, ${order.author.name}, ${order.genre.name}, ${order.date}, ${order.pagesAmount}, ${order.copiesAmount}
                <a href="controller?command=add_copy&id=${order.id}">Add copy</a>
                <c:if test="${order.copiesAmount gt 0}">
                    <a href="controller?command=remove_copy&id=${order.id}">Remove copy</a>
                </c:if>
                <c:if test="${order.copiesAmount eq 0}">
                    <a href="controller?command=delete_book&id=${order.id}">Delete book</a>
                </c:if>
            </li>
        </c:forEach>
    </ul>
</c:if>
<a href="controller?command=show_add_book_page">Add new Book</a>
<a href="controller?command=main">Main Page</a>
</body>
</html>

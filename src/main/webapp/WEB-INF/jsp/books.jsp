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
    <title>Books managment</title>
</head>
<body>
<c:if test="${not empty books }" >
    <ul>
        <c:forEach var="elem" items="${books}">
            <li>${elem}
                <a href="controller?command=add_copy&id=${elem.id}">Add copy</a>
                <c:if test="${elem.booksAmount gt 1}">
                    <a href="controller?command=remove_copy&id=${elem.id}">Remove copy</a>
                </c:if>
                <c:if test="${elem.booksAmount eq 1}">
                    <a href="controller?command=delete_book&id=${elem.id}">Delete book</a>
                </c:if>
            </li>
        </c:forEach>
    </ul>
</c:if>
<a href="controller?command=show_add_book_page">Add new Book</a>
<a href="controller?command=main">Main Page</a>
</body>
</html>

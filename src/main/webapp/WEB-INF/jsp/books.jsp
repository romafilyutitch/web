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
            <li><c:out value="${elem.name},
         ${elem.author.name},
         ${elem.genre.name},
         ${elem.date},
         ${elem.pagesAmount},
         ${elem.booksAmount},
         ${elem.description}"/>
                <a href="controller?command=add_copy&id=${elem.id}">Add copy</a>
                <a href="controller?command=remove_copy&id=${elem.id}">Remove copy</a></li>
        </c:forEach>
    </ul>
</c:if>
</body>
</html>

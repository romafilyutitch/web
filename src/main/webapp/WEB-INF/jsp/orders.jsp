<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: roma0
  Date: 09.06.2021
  Time: 18:22
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Orders management</title>
</head>
<body>
    <c:if test="${not empty orders}">
        <ul>
        <c:forEach var="elem" items="${orders}">
            <li>
                <c:out value="${elem.id}"/>
            </li>
        </c:forEach>
        </ul>
    </c:if>
<a href="controller?command=main">Main Page</a>
</body>
</html>

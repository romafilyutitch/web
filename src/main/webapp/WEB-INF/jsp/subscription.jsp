<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: roma0
  Date: 10.06.2021
  Time: 15:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Subscription page</title>
</head>
<body>
<c:if test="${not empty error}">
    ${error}
</c:if>
<c:if test="${not empty user}">
    ${user}
</c:if>
    <form action="controller?command=set_subscription&id=${user.id}" method="POST">
        <label>Start date:
            <input type="date" name="start_date"/>
        </label>
        <label>End date:
            <input type="date" name="end_date"/>
        </label>
        <input type="submit" value="set subscription">
    </form>
<a href="controller?command=show_users">Users</a>
</body>
</html>

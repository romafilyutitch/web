<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: roma0
  Date: 12.06.2021
  Time: 1:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>My account</title>
</head>
<body>
  <c:if test="${not empty sessionScope.user}">
    ${user.login}
    <hr/>
    ${user.password}
    <hr/>
    ${user.role.name}
    <hr/>
    ${user.subscription}
    <hr/>
    <form name="changeForm" method="Post" action="controller?command=change_account">
      <label>
        Change login:
        <input type="text" name="login">
      </label>
      <label>
        Change password:
        <input type="password" name="password">
      </label>
      <input type="submit" name="submit" value="change account">
    </form>
  </c:if>
<a href="controller?command=main">Main page</a>
</body>
</html>

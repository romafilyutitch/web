<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="main"/>
<html>
<head>
    <title>Account page</title>
</head>
<body>
  <c:if test="${not empty sessionScope.user}">
    <div>
    ${sessionScope.user}
    </div>
    <div>
    <form method="post" action="controller">
      <input type="hidden" name="command" value="change_login">
      <label>Change Login<input type="text" name="login"></label>
      <input type="submit" name="submit" value="change login">
    </form>
    <form method="post" action="controller">
      <input type="hidden" name="command" value="change_password">
      <label>Change password<input type="password" name="password"></label>
      <input type="submit" value="change password">
    </form>
    <form name="delete account" method="POST" action="controller?command=delete_user">
      <input type="hidden" name="id" value="${sessionScope.user.id}">
      <input type="submit" value="Delete account">
    </form>
    </div>
  </c:if>
  <a href="controller?command=main">Main Page</a>
</body>
</html>

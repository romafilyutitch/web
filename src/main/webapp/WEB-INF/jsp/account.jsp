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
<c:choose>
  <c:when test="${not empty requestScope.error}">
    ${requestScope.error}
    <a href="controller?command=show_account">Account</a>
  </c:when>
  <c:otherwise>
    <div>
      Login : ${sessionScope.user.login}
      Role : ${sessionScope.user.role}
      Subscription : ${sessionScope.user.subscription}
    </div>
    <div>
      <form method="post" action="controller">
        <input type="hidden" name="command" value="change_login">
        <input type="hidden" name="id" value="${sessionScope.user.id}">
        <label>Change Login<input type="text" required name="login"></label>
        <input type="submit" name="submit" value="change login">
      </form>
      <form method="post" action="controller">
        <input type="hidden" name="command" value="change_password">
        <input type="hidden" name="id" value="${sessionScope.user.id}">
        <label>Change password<input type="password" required name="password"></label>
        <input type="submit" value="change password">
      </form>
      <form name="delete account" method="POST" action="controller">
        <input type="hidden" name="command" value="delete_user">
        <input type="hidden" name="id" value="${sessionScope.user.id}">
        <input type="submit" value="Delete account">
      </form>
    </div>
  </c:otherwise>
</c:choose>
  <a href="controller?command=main">Main Page</a>
</body>
</html>

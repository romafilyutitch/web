<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="account"/>
<html>
<head>
    <title><fmt:message key="title"/></title>
</head>
<body>
<c:choose>
  <c:when test="${not empty requestScope.error}">
    <fmt:message key="${requestScope.error}"/>
    <a href="controller?command=show_account"><fmt:message key="try"/></a>
  </c:when>
  <c:otherwise>
    <div>
      <fmt:message key="login"/> ${sessionScope.user.login}
      <fmt:message key="role"/> ${sessionScope.user.role}
      <fmt:message key="subscription"/> ${sessionScope.user.subscription}
    </div>
    <div>
      <form method="post" action="controller">
        <input type="hidden" name="command" value="change_login">
        <input type="hidden" name="id" value="${sessionScope.user.id}">
        <label><fmt:message key="changeLogin"/><input type="text" required pattern="^\S[A-Za-z\s]+$" name="login"></label>
        <input type="submit" name="submit" value="change login">
      </form>
      <form method="post" action="controller">
        <input type="hidden" name="command" value="change_password">
        <input type="hidden" name="id" value="${sessionScope.user.id}">
        <label><fmt:message key="changePassword"/><input type="password" required pattern="^\S[A-Za-z\s]+$" name="password"></label>
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
  <a href="controller?command=main"><fmt:message key="main"/></a>
</body>
</html>

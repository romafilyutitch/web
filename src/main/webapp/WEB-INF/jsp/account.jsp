<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="main"/>
<html>
<head>
    <title><fmt:message key="account.title"/></title>
</head>
<body>
  <c:if test="${not empty sessionScope.user}">
    ${sessionScope.user}
    <form name="changeForm" method="Post" action="controller?command=change_account">
      <label><fmt:message key="account.changeLogin"/><input type="text" name="login"></label>
      <label><fmt:message key="account.changePassword"/><input type="password" name="password"></label>
      <input type="submit" name="submit" value="change account">
    </form>
    <form name="delete account" method="POST" action="controller?command=delete_user">
      <input type="hidden" name="id" value="${sessionScope.user.id}">
      <input type="submit" value="Delete account">
    </form>
  </c:if>
  <a href="controller?command=main"><fmt:message key="main"/></a>
</body>
</html>

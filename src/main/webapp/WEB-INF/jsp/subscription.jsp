<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="main"/>
<html>
<head>
    <title><fmt:message key="subscription.title"/></title>
</head>
<body>
<c:if test="${not empty requestScope.error}">
    ${requestScope.error}
</c:if>
<c:if test="${not empty requestScope.user}">
    ${requestScope.user}
</c:if>
    <form action="controller?command=set_subscription" method="POST">
        <input type="hidden" name="id" value="${requestScope.user.id}">
        <label><fmt:message key="subscription.statDate"/><input type="date" name="start_date"/></label>
        <label><fmt:message key="subscription.endDate"/><input type="date" name="end_date"/></label>
        <input type="submit" value="set subscription">
    </form>
<a href="controller?command=show_users"><fmt:message key="manageUsers"/></a>
</body>
</html>

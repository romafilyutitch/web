<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="by.epam.jwd.web.model.UserRole" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="users"/>
<html>
<head>
    <title><fmt:message key="title"/></title>
</head>
<body>
<h1><fmt:message key="list"/></h1>
<c:if test="${not empty requestScope.users}">
    <c:forEach var="order" items="${requestScope.users}">
        <div>
            <c:if test="${sessionScope.user.id eq order.id}"><b><fmt:message key="you"/></b></c:if>
            <fmt:message key="login"/> ${order.login},
            <fmt:message key="role"/> ${order.role},
            <fmt:message key="subscription"/> ${order.subscription}
            <c:if test="${order.role ne UserRole.ADMIN}">
                <form name="promote role" method="POST" action="controller">
                    <input type="hidden" name="command" value="promote_role">
                    <input type="hidden" name="id" value="${order.id}">
                    <input type="submit" value="Promote user role">
                </form>
            </c:if>
            <c:if test="${order.role ne UserRole.READER}">
                <form name="demote role" method="POST" action="controller?command=demote_role">
                    <input type="hidden" name="command" value="demote_role">
                    <input type="hidden" name="id" value="${order.id}">
                    <input type="submit" value="Demote user role">
                </form>
            </c:if>
            <form action="controller" method="post">
                <input type="hidden" name="command" value="set_subscription">
                <input type="hidden" name="id" value="${order.id}">
                <label><fmt:message key="startDate"/><input type="date" required min ="${sessionScope.currentDate}" name="start_date"></label>
                <label><fmt:message key="endDate"/><input type="date" required min="${sessionScope.currentDate}" name="end_date"></label>
                <input type="submit" value="Set subscription">
            </form>
        </div>
    </c:forEach>
    <c:if test="${requestScope.currentPageNumber ne 1}">
        <a href="controller?command=show_users&page=${requestScope.currentPageNumber - 1}"><fmt:message key="previous"/></a>
    </c:if>
    <c:forEach begin="1" end="${requestScope.pagesAmount}" var="i">
        <a href="controller?command=show_users&page=${i}">${i}</a>
    </c:forEach>
    <c:if test="${requestScope.currentPageNumber lt requestScope.pagesAmount}">
        <a href="controller?command=show_users&page=${requestScope.currentPageNumber + 1}"><fmt:message key="next"/></a>
    </c:if>
</c:if>
<a href="controller?command=main"><fmt:message key="main"/></a>
</body>
</html>

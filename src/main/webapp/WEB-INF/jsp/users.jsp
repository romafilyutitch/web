<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="by.epam.jwd.web.model.UserRole" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="main"/>
<html>
<head>
    <title>Users page</title>
</head>
<body>
<h1>Users list</h1>
<c:if test="${not empty requestScope.users}">
    <ul>
    <c:forEach var="book" items="${requestScope.users}">
        <div> <c:if test="${sessionScope.user.id eq book.id}">YOU</c:if>
        ${book.login} ,${book.role.name}, ${book.subscription}
            <c:if test="${book.role ne UserRole.ADMIN}">
                <form name="promote role" method="POST" action="controller?command=promote_role">
                    <input type="hidden" name="id" value="${book.id}">
                    <input type="submit" value="Promote user role">
                </form>
            </c:if>
            <c:if test="${book.role ne UserRole.READER}">
                <form name="demote role" method="POST" action="controller?command=demote_role">
                    <input type="hidden" name="id" value="${book.id}">
                    <input type="submit" value="Demote user role">
                </form>
            </c:if>
            <form action="controller" method="post">
                <input type="hidden" name="command" value="set_subscription">
                <input type="date" name="start_date">
                <input type="date" name="end_date">
                <input type="submit" value="Set subscription">
            </form>
        </div>
    </c:forEach>
    </ul>
</c:if>
<a href="controller?command=main">Main page</a>
</body>
</html>

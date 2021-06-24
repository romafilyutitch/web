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
    <c:forEach var="book" items="${requestScope.users}">
        <div> <c:if test="${sessionScope.user.id eq book.id}">YOU</c:if>
        ${book.login} ,${book.role}, ${book.subscription}
            <c:if test="${book.role ne UserRole.ADMIN}">
                <form name="promote role" method="POST" action="controller">
                    <input type="hidden" name="command" value="promote_role">
                    <input type="hidden" name="id" value="${book.id}">
                    <input type="submit" value="Promote user role">
                </form>
            </c:if>
            <c:if test="${book.role ne UserRole.READER}">
                <form name="demote role" method="POST" action="controller?command=demote_role">
                    <input type="hidden" name="command" value="demote_role">
                    <input type="hidden" name="id" value="${book.id}">
                    <input type="submit" value="Demote user role">
                </form>
            </c:if>
            <form action="controller" method="post">
                <input type="hidden" name="command" value="set_subscription">
                <input type="hidden" name="id" value="${book.id}">
                <label>Start date<input type="date" required min ="${sessionScope.currentDate}" name="start_date"></label>
                <label>End date<input type="date" required min="${sessionScope.currentDate}" name="end_date"></label>
                <input type="submit" value="Set subscription">
            </form>
        </div>
    </c:forEach>
    <c:if test="${requestScope.currentPageNumber ne 1}">
        <a href="controller?command=show_users&page=${requestScope.currentPageNumber - 1}">Previous</a>
    </c:if>
    <c:forEach begin="1" end="${requestScope.pagesAmount}" var="i">
        <a href="controller?command=show_users&page=${i}">${i}</a>
    </c:forEach>
    <c:if test="${requestScope.currentPageNumber lt requestScope.pagesAmount}">
        <a href="controller?command=show_users&page=${requestScope.currentPageNumber + 1}">Next</a>
    </c:if>
</c:if>
<a href="controller?command=main">Main page</a>
</body>
</html>

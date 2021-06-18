<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="by.epam.jwd.web.model.Status" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="main"/>
<html>
<head>
    <title>Orders page</title>
</head>
<body>
    <c:if test="${not empty requestScope.orders}">
        <c:forEach var="book" items="${requestScope.orders}">
            <div>
                ${book.user.login}, ${book.book.name}, ${book.status}
                <c:if test="${book.status eq Status.ORDERED}">
                    <form  method="POST" action="controller">
                        <input type="hidden" name="command" value="approve_order">
                        <input type="hidden" name="id" value="${book.id}">
                        <input type="submit" value="Approve order">
                    </form>
                </c:if>
                <c:if test="${book.status eq Status.RETURNED}">
                    <form method="POST" action="controller">
                        <input type="hidden" name="command" value="delete_order">
                        <input type="hidden" name="id" value="${book.id}">
                        <input type="submit" value="Delete order">
                    </form>
                </c:if>
            </div>
        </c:forEach>
    </c:if>
<a href="controller?command=main">Main Page</a>
</body>
</html>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="by.epam.jwd.web.model.Status" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="userOrders"/>
<html>
<head>
    <title><fmt:message key="title"/></title>
</head>
<body>
    <c:if test="${not empty requestScope.orders}">
            <c:forEach var="book" items="${requestScope.orders}">
                <div>
                    <fmt:message key="book"/> ${book.book.name},
                    <fmt:message key="author"/> ${book.book.author.name},
                    <fmt:message key="genre"/> ${book.book.genre},
                    <fmt:message key="pages"/> ${book.book.pagesAmount},
                    <fmt:message key="status"/> ${book.status}
                    <c:if test="${book.status eq Status.APPROVED}">
                        <fmt:message key="description"/> ${book.book.description}
                        <form name="return" action="controller" method="POST">
                            <input type="hidden" name="command" value="return_book">
                            <input type="hidden" name="id" value="${book.id}">
                            <input type="submit" value="Return book">
                        </form>
                    </c:if>
                </div>
            </c:forEach>
    </c:if>
<a href="controller?command=main"><fmt:message key="main"/></a>
</body>
</html>

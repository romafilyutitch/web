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
            <c:forEach var="user" items="${requestScope.orders}">
                <div>
                    <fmt:message key="book"/> ${user.book.name},
                    <fmt:message key="author"/> ${user.book.author.name},
                    <fmt:message key="genre"/> ${user.book.genre},
                    <fmt:message key="pages"/> ${user.book.pagesAmount},
                    <fmt:message key="status"/> ${user.status}
                    <c:if test="${user.status eq Status.APPROVED}">
                        <fmt:message key="description"/> ${user.book.description}
                        <form name="return" action="controller" method="POST">
                            <input type="hidden" name="command" value="return_book">
                            <input type="hidden" name="id" value="${user.id}">
                            <input type="submit" value="Return book">
                        </form>
                    </c:if>
                </div>
            </c:forEach>
    </c:if>
<a href="controller?command=main"><fmt:message key="main"/></a>
</body>
</html>

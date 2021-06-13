<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="by.epam.jwd.web.model.Status" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="main"/>
<html>
<head>
    <title><fmt:message key="orders.title"/></title>
</head>
<body>
    <c:if test="${not empty requestScope.orders}">
        <ul>
        <c:forEach var="book" items="${requestScope.orders}">
            <li>
                ${book.user.login}, ${book.book.name}, ${book.status.name}
                <c:if test="${book.status eq Status.ORDERED}">
                    <form name="approve user order" method="POST" action="controller?command=approve">
                        <input type="hidden" name="id" value="${book.id}">
                        <input type="submit" value="Approve order">
                    </form>
                </c:if>
            </li>
        </c:forEach>
        </ul>
    </c:if>
<a href="controller?command=main"><fmt:message key="main"/></a>
</body>
</html>

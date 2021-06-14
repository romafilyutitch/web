<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="by.epam.jwd.web.model.Status" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="main"/>
<html>
<head>
    <title><fmt:message key="myorders.title"/></title>
</head>
<body>
    <c:if test="${not empty requestScope.orders}">
            <c:forEach var="book" items="${requestScope.orders}">
                <div> ${book.book.name} ${book.status.name}
                    <c:if test="${book.status eq Status.APPROVED}">
                        <a href="controller?command=read&id=${book.book.id}"><fmt:message key="myorders.readBook"/></a>
                        <form name="return" action="controller?return_book" method="POST">
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

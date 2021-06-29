<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="by.epam.jwd.web.model.Status" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="main"/>
<html>
<head>
    <title>My orders</title>
</head>
<body>
    <c:if test="${not empty requestScope.orders}">
            <c:forEach var="book" items="${requestScope.orders}">
                <div>Name : ${book.book.name},
                    Author : ${book.book.author.name},
                    Genre : ${book.book.genre},
                    Pages : ${book.book.pagesAmount},
                    Status : ${book.status}
                    <c:if test="${book.status eq Status.APPROVED}">
                        Description : ${book.book.description}
                        <form name="return" action="controller" method="POST">
                            <input type="hidden" name="command" value="return_book">
                            <input type="hidden" name="id" value="${book.id}">
                            <input type="submit" value="Return book">
                        </form>
                    </c:if>
                </div>
            </c:forEach>
    </c:if>
<a href="controller?command=main">Main page</a>
</body>
</html>

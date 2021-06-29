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
                User : ${book.user.login},
                Book : ${book.book.name},
                Status : ${book.status}
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
        <c:if test="${requestScope.currentPageNumber ne 1}" >
            <a href="controller?command=show_orders&page=${requestScope.currentPageNumber - 1}">Previous</a>
        </c:if>
        <c:forEach begin="1" end="${requestScope.pagesAmount}" var="i">
            <a href="controller?command=show_orders&page=${i}">${i}</a>
        </c:forEach>
        <c:if test="${requestScope.currentPageNumber lt requestScope.pagesAmount}">
            <a href="controller?command=show_orders&page=${requestScope.currentPageNumber + 1}">Next</a>
        </c:if>
    </c:if>
<a href="controller?command=main">Main Page</a>
</body>
</html>

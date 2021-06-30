<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="customtags"%>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="books"/>
<html>
<head>
    <title>Book page</title>
</head>
<body>
<c:if test="${not empty requestScope.books }" >
    <div>
        <c:forEach var="order" items="${requestScope.books}">
            <div>
                <fmt:message key="name"/> ${order.name}
                <fmt:message key="author"/> ${order.author.name}
                <fmt:message key="genre"/> ${order.genre}
                <fmt:message key="date"/> ${ctg:localDateParser(order.date, sessionScope.locale)}
                <fmt:message key="pages"/> ${order.pagesAmount}
                <fmt:message key="copies"/> ${order.copiesAmount}
                <fmt:message key="description"/> ${order.description}
                <form name="add one copy" action="controller" method="POST">
                    <input type="hidden" name="command" value="add_copy">
                    <input type="hidden" name="id" value="${order.id}">
                    <input type="submit" value="Add one copy">
                </form>
                <c:if test="${order.copiesAmount gt 0}">
                    <form name="remove one copy" action="controller" method="POST">
                        <input type="hidden" name="command" value="remove_copy">
                        <input type="hidden" name="id" value="${order.id}">
                        <input type="submit" value="Remove one copy">
                    </form>
                </c:if>
                <c:if test="${order.copiesAmount eq 0}">
                    <form name="delete book" action="controller" method="POST">
                        <input type="hidden" name="command" value="delete_book">
                        <input type="hidden" name="id" value="${order.id}">
                        <input type="submit" value="Delete book">
                    </form>
                </c:if>
            </div>
        </c:forEach>
    </div>
    <div>
        <c:if test="${requestScope.currentPageNumber ne 1}">
            <a href="controller?command=show_books&page=${requestScope.currentPageNumber - 1}"><fmt:message key="previous"/></a>
        </c:if>
        <c:forEach begin="1" end="${requestScope.pagesAmount}" var="i">
            <a href="controller?command=show_books&page=${i}">${i}</a>
        </c:forEach>
        <c:if test="${requestScope.currentPageNumber lt requestScope.pagesAmount}">
            <a href="controller?command=show_books&page=${requestScope.currentPageNumber + 1}"><fmt:message key="next"/></a>
        </c:if>
    </div>
</c:if>
<form action="controller" method="post">
    <input type="hidden" name="command" value="add_book">
    <label><fmt:message key="name"/><input type="text" required pattern="^\S[A-Za-z\s]+$" name="name"></label>
    <label><fmt:message key="author"/><input type="text" required pattern="^\S[A-Za-z\s]+$" name="author"></label>
    <label><fmt:message key="genre"/>
        <select name="genre" required >
                <option value="FICTION"><fmt:message key="fiction"/></option>
                <option value="FANTASY"><fmt:message key="fantasy"/></option>
                <option value="SCIENCE"><fmt:message key="science"/></option>
        </select>
    </label>
    <label><fmt:message key="date"/><input type="date" required max = "${sessionScope.currentDate}" name="date"></label>
    <label><fmt:message key="pages"/><input type="number" required min = "1" step = "1" name="pages"></label>
    <label><fmt:message key="description"/><input type="text" required pattern="^\S[A-Za-z\s]+$" name="description"></label>
    <input type="submit" required value="add new book">
</form>
<a href="controller?command=main"><fmt:message key="main"/></a>
</body>
</html>

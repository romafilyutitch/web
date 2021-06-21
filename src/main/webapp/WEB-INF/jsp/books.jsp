<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="main"/>
<html>
<head>
    <title>Book page</title>
</head>
<body>
<c:if test="${not empty requestScope.books }" >
    <div>
        <c:forEach var="book" items="${requestScope.books}">
            <div>${book}
                <form name="add one copy" action="controller" method="POST">
                    <input type="hidden" name="command" value="add_copy">
                    <input type="hidden" name="id" value="${book.id}">
                    <input type="submit" value="Add one copy">
                </form>
                <c:if test="${book.copiesAmount gt 0}">
                    <form name="remove one copy" action="controller" method="POST">
                        <input type="hidden" name="command" value="remove_copy">
                        <input type="hidden" name="id" value="${book.id}">
                        <input type="submit" value="Remove one copy">
                    </form>
                </c:if>
                <c:if test="${book.copiesAmount eq 0}">
                    <form name="delete book" action="controller" method="POST">
                        <input type="hidden" name="command" value="delete_book">
                        <input type="hidden" name="id" value="${book.id}">
                        <input type="submit" value="Delete book">
                    </form>
                </c:if>
            </div>
        </c:forEach>
    </div>
    <div>
        <c:if test="${requestScope.currentPageNumber ne 1}">
            <a href="controller?command=show_books&page=${requestScope.currentPageNumber - 1}">Previous</a>
        </c:if>
        <c:forEach begin="1" end="${requestScope.pagesAmount}" var="i">
            <a href="controller?command=show_books&page=${i}">${i}</a>
        </c:forEach>
        <c:if test="${requestScope.currentPageNumber lt requestScope.pagesAmount}">
            <a href="controller?command=show_books&page=${requestScope.currentPageNumber + 1}">Next</a>
        </c:if>
    </div>
</c:if>
<form action="controller" method="post">
    <input type="text" name="name">
    <input type="text" name="author">
    <select name="genre" required >
        <c:forEach var="genre" items="${requestScope.genres}">
            <option value="${genre}">${genre}</option>
        </c:forEach>
    </select>
    <input type="date" name="date">
    <input type="number" name="pages_amount">
    <input type="text" name="description">
    <input type="submit" value="add new book">
</form>
<a href="controller?command=main">Main page</a>
</body>
</html>

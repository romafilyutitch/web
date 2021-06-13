<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="main"/>
<html>
<head>
    <title><fmt:message key="books.title"/></title>
</head>
<body>
<c:if test="${not empty requestScope.books }" >
    <ul>
        <c:forEach var="book" items="${requestScope.books}">
            <li>${book.id}, ${book.name}, ${book.author.name}, ${book.genre.name}, ${book.date}, ${book.pagesAmount}, ${book.copiesAmount}
                <form name="add one copy" action="controller?command=add_copy" method="POST">
                    <input type="hidden" name="id" value="${book.id}">
                    <input type="submit" value="Add one copy">
                </form>
                <c:if test="${book.copiesAmount gt 0}">
                    <form name="remove one copy" action="controller?command=remove_copy" method="POST">
                        <input type="hidden" name="id" value="${book.id}">
                        <input type="submit" value="Remove one copy">
                    </form>
                </c:if>
                    <form name="delete book" action="controller?command=delete_book" method="POST">
                        <input type="hidden" name="id" value="${book.id}">
                        <input type="submit" value="Delete book">
                    </form>
            </li>
        </c:forEach>
    </ul>
</c:if>
<a href="controller?command=show_add_book_page"><fmt:message key="books.addNew"/></a>
<a href="controller?command=main"><fmt:message key="main"/></a>
</body>
</html>

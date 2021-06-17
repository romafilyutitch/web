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
            <li>${book}
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
                <c:if test="${book.copiesAmount eq 0}">
                    <form name="delete book" action="controller?command=delete_book" method="POST">
                        <input type="hidden" name="id" value="${book.id}">
                        <input type="submit" value="Delete book">
                    </form>
                </c:if>
            </li>
        </c:forEach>
    </div>
</c:if>
<form action="controller" method="post">
    <input type="text" name="name">
    <input type="text" name="author">
    <select name="genre" required >
        <option value="FICTION">FICTION</option>
        <option value="FANTASY">FANTASY</option>
        <option value="SCIENCE">SCIENCE</option>
    </select>
    <input type="date" name="date">
    <input type="number" name="pages_amount">
    <input type="text" name="description">
    <input type="submit" value="add new book">
</form>
<a href="controller?command=main">Main page</a>
</body>
</html>

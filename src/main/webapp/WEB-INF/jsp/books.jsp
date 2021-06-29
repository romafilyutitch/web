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
            <div>
                Name : ${book.name}
                Author : ${book.author.name}
                Genre : ${book.genre}
                Date : ${book.date}
                Pages : ${book.pagesAmount}
                Copies : ${book.copiesAmount}
                Description : ${book.description}
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
    <input type="hidden" name="command" value="add_book">
    <label>Name<input type="text" required name="name"></label>
    <label>Author<input type="text" required name="author"></label>
    <label>Genre
        <select name="genre" required >
            <c:forEach var="genre" items="${requestScope.genres}">
                <option value="${genre}">${genre}</option>
            </c:forEach>
        </select>
    </label>
    <label>Date<input type="date" required max = "${sessionScope.currentDate}" name="date"></label>
    <label>Pages<input type="number" required min = "1" step = "1" name="pages"></label>
    <label>Description<input type="text" required name="description"></label>
    <input type="submit" required value="add new book">
</form>
<a href="controller?command=main">Main page</a>
</body>
</html>

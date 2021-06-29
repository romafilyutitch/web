<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="by.epam.jwd.web.model.UserRole" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="main"/>
<html>
<head>
    <title>Library application</title>
</head>
<body>
<div>
<h3>Welcome</h3>
<ctg:time/>
</div>
<div>
<a href="controller?command=set_locale&locale=en_US">EN</a>
<a href="controller?command=set_locale&locale=ru_RU">RU</a>
</div>
<div>
<c:choose>
    <c:when test="${not empty sessionScope.user}">
        Hello ${sessionScope.user.login}
        <a href="controller?command=logout">Logout</a>
        <a href="controller?command=show_user_orders">My Orders</a>
        <a href="controller?command=show_account">My Account</a>
        <c:if test="${sessionScope.user.role eq UserRole.ADMIN}">
            <a href="controller?command=show_users">Users</a>
            <a href="controller?command=show_books">Books</a>
            <a href="controller?command=show_orders">Orders</a>
        </c:if>
        <c:if test="${sessionScope.user.role eq UserRole.LIBRARIAN}">
            <a href="controller?command=show_orders">Orders</a>
        </c:if>
    </c:when>
    <c:otherwise>
        <a href="controller?command=show_login">Login</a>
        <a href="controller?command=show_register">Register</a>
    </c:otherwise>
</c:choose>
</div>
<div>
${sessionScope.success}
</div>
<div>
${sessionScope.fail}
</div>
<div>
    <form name="find" method="get" action="controller">
        <input type="hidden"  name="command"  value="find_book_by_name">
        <label>Book name<input type="text" required name="name"></label>
        <input type="submit" name="find" value="find">
    </form>
    <a href="controller?command=find_fiction">FICTION</a>
    <a href="controller?command=find_fantasy">FANTASY</a>
    <a href="controller?command=find_science">SCIENCE</a>
    <a href="controller?command=main">All books</a>
</div>
<div>
    ${requestScope.findResult}
</div>
<c:if test="${not empty requestScope.books }">
            <c:forEach var="book" items="${requestScope.books}">
                <c:if test="${book.copiesAmount ge 1}">
                    <div>
                        Name : ${book.name}
                        Author : ${book.author.name}
                        Genre : ${book.genre}
                        <c:if test="${not empty sessionScope.user}">
                            <form name="order" method="POST" action="controller">
                                <input type="hidden" name="command" value="order_book">
                                <input type="hidden" name="id" value="${book.id}">
                                <input type="submit" value="Order book">
                            </form>
                        </c:if>
                    </div>
                </c:if>
            </c:forEach>
    <c:if test="${not empty requestScope.currentPageNumber and requestScope.currentPageNumber ne 1}">
        <a href="controller?command=main&page=${requestScope.currentPageNumber - 1}">Previous</a>
    </c:if>
    <c:forEach begin="1" end="${requestScope.pagesAmount}" var="i">
        <a href="controller?command=main&page=${i}">${i}</a>
    </c:forEach>
    <c:if test="${not empty requestScope.currentPageNumber and requestScope.currentPageNumber lt requestScope.pagesAmount}">
        <a href="controller?command=main&page=${requestScope.currentPageNumber + 1}">Next</a>
    </c:if>
</c:if>
</body>
</html>
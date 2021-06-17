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
${sessionScope.commandResult}
</div>
<div>
<c:if test="${not empty sessionScope.user}">
    <form name="find" method="get" action="controller">
        <input type="hidden" name="command" value="find_book_by_name">
        <label>Book name <input type="text" name="name"></label>
        <input type="submit" name="find" value="find">
    </form>
    <a href="controller?command=find_fiction">FICTION</a>
    <a href="controller?command=find_fantasy">FANTASY</a>
    <a href="controller?command=find_science">SCIENCE</a>
    <a href="controller?command=main">All books</a>
</c:if>
</div>
<c:if test="${not empty requestScope.books }">
            <c:forEach var="book" items="${requestScope.books}">
                <div>
                        Name ${book.name} <br/>
                        Author ${book.author.name}<br/>
                        Genre ${book.genre}<br/>
                        Date <fmt:parseDate var="parsed" value="${book.date}" type="date" pattern="yyyy-MM-dd"/><fmt:formatDate value="${parsed}"/><br/>
                        Pages <fmt:formatNumber value="${book.pagesAmount}"/><br/>
                        Copies <fmt:formatNumber value="${book.copiesAmount}"/><br/>
                        Description ${book.description}<br/>
                    <c:if test="${not empty sessionScope.user}">
                        <form name="order" method="POST" action="controller">
                            <input type="hidden" name="command" value="order_book">
                            <input type="hidden" name="id" value="${book.id}">
                            <input type="submit" value="Order book">
                        </form>
                    </c:if>
                </div>
            </c:forEach>
</c:if>
</body>
</html>
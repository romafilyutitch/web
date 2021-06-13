<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="by.epam.jwd.web.model.UserRole" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="main"/>
<html>
<head>
    <title><fmt:message key="welcome"/></title>
</head>
<body>
<h3><fmt:message key="welcome"/></h3>
<form name="locale choose" action="controller?command=set_locale" method="POST">
    <label><fmt:message key="russian"/><input type="radio" name="locale" value="ru_RU"></label>
    <label><fmt:message key="english"/><input type="radio" name="locale" value="en_US"></label>
    <input type="submit" value="choose language">
</form>
<hr/>
<c:choose>
    <c:when test="${not empty sessionScope.user}">
        <fmt:message key="hello"/> ${sessionScope.user.login}
        <a href="controller?command=logout"><fmt:message key="logout"/></a>
        <a href="controller?command=show_user_orders"><fmt:message key="myOrders"/></a>
        <a href="controller?command=show_account"><fmt:message key="myAccount"/></a>
        <c:if test="${sessionScope.user.role eq UserRole.ADMIN}">
            <a href="controller?command=show_users"><fmt:message key="manageUsers"/></a>
            <a href="controller?command=show_books"><fmt:message key="manageBooks"/></a>
            <a href="controller?command=show_orders"><fmt:message key="manageOrders"/></a>
        </c:if>
        <c:if test="${sessionScope.user.role eq UserRole.LIBRARIAN}">
            <a href="controller?command=show_orders"><fmt:message key="manageOrders"/></a>
        </c:if>
    </c:when>
    <c:otherwise>
        <a href="controller?command=show_login"><fmt:message key="login"/></a>
        <a href="controller?command=show_register"><fmt:message key="register"/></a>
    </c:otherwise>
</c:choose>
<hr/>
${sessionScope.commandResult}
<hr/>
<c:if test="${not empty sessionScope.user}">
    <form name="find" method="POST" action="controller?command=find">
        <label><fmt:message key="findBooks"/><input type="text" name="name"></label>
        <label><fmt:message key="findByName"/><input type="radio" name="criteria" value="name"></label>
        <label><fmt:message key="findByAuthor"/><input type="radio" name="criteria" value="author"></label>
        <label><fmt:message key="findByGenre"/><input type="radio" name="criteria" value="genre"></label>
        <input type="submit" name="find" value="find">
    </form>
    <a href="controller?command=main"><fmt:message key="allBooks"/></a>
</c:if>
<hr/>
<c:if test="${not empty requestScope.books }">
        <ul>
            <c:forEach var="book" items="${requestScope.books}">
                <li>
                        ${book}
                    <c:if test="${not empty sessionScope.user}">
                        <form name="order" method="POST" action="controller?command=order_book">
                            <input type="hidden" name="id" value="${book.id}">
                            <input type="submit" value="Order book">
                        </form>
                    </c:if>
                </li>
            </c:forEach>
        </ul>
</c:if>
</body>
</html>
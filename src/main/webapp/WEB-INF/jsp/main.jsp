<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="by.epam.jwd.web.model.UserRole" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="main"/>
<html>
<head>
    <title><fmt:message key="title"/></title>
</head>
<body>
<div>
<h3><fmt:message key="welcome"/></h3>
<ctg:time/>
</div>
<div>
<a href="controller?command=set_locale&locale=en"><fmt:message key="english"/></a>
<a href="controller?command=set_locale&locale=ru"><fmt:message key="russian"/></a>
</div>
<div>
<c:choose>
    <c:when test="${not empty sessionScope.user}">
        <fmt:message key="hello"/> ${sessionScope.user.login}
        <a href="controller?command=logout"><fmt:message key="logout"/></a>
        <a href="controller?command=show_user_orders"><fmt:message key="myOrders"/></a>
        <a href="controller?command=show_account"><fmt:message key="myAccount"/></a>
        <c:if test="${sessionScope.user.role eq UserRole.ADMIN or sessionScope.user.role eq UserRole.LIBRARIAN}">
            <a href="controller?command=show_users"><fmt:message key="users"/></a>
            <a href="controller?command=show_books"><fmt:message key="books"/></a>
            <a href="controller?command=show_orders"><fmt:message key="orders"/></a>
        </c:if>
    </c:when>
    <c:otherwise>
        <a href="controller?command=show_login"><fmt:message key="login"/></a>
        <a href="controller?command=show_register"><fmt:message key="register"/></a>
    </c:otherwise>
</c:choose>
</div>
<div>
    <c:if test="${not empty sessionScope.success}">
        <fmt:message key="${sessionScope.success}"/>
    </c:if>
</div>
<div>
    <c:if test="${not empty sessionScope.fail}">
        <fmt:message key="${sessionScope.fail}"/>
    </c:if>
</div>
<div>
    <form name="find" method="get" action="controller">'
        <input type="hidden"  name="command" value="find_book_by_name">
        <label><fmt:message key="name"/><input type="search" required pattern="^\S[A-Za-z\s]+$" name="name"></label>
        <input type="submit" name="find" value="find">
    </form>
    <a href="controller?command=find_fiction"><fmt:message key="fiction"/></a>
    <a href="controller?command=find_fantasy"><fmt:message key="fantasy"/></a>
    <a href="controller?command=find_science"><fmt:message key="science"/></a>
    <a href="controller?command=main"><fmt:message key="all"/></a>
</div>
<div>
    <c:if test="${not empty requestScope.findResult}">
        <fmt:message key="${requestScope.findResult}"/>
    </c:if>
</div>
<c:if test="${not empty requestScope.books }">
            <c:forEach var="order" items="${requestScope.books}">
                <c:if test="${order.copiesAmount ge 1}">
                    <div>
                        <fmt:message key="name"/> ${order.name}
                       <fmt:message key="author"/> ${order.author.name}
                        <fmt:message key="genre"/> ${order.genre}
                        <c:if test="${not empty sessionScope.user}">
                            <form name="order" method="POST" action="controller">
                                <input type="hidden" name="command" value="order_book">
                                <input type="hidden" name="id" value="${order.id}">
                                <input type="submit" value="Order book">
                            </form>
                        </c:if>
                    </div>
                </c:if>
            </c:forEach>
    <c:if test="${not empty requestScope.currentPageNumber and requestScope.currentPageNumber ne 1}">
        <a href="controller?command=main&page=${requestScope.currentPageNumber - 1}"><fmt:message key="previous"/></a>
    </c:if>
    <c:forEach begin="1" end="${requestScope.pagesAmount}" var="i">
        <a href="controller?command=main&page=${i}">${i}</a>
    </c:forEach>
    <c:if test="${not empty requestScope.currentPageNumber and requestScope.currentPageNumber lt requestScope.pagesAmount}">
        <a href="controller?command=main&page=${requestScope.currentPageNumber + 1}"><fmt:message key="next"/></a>
    </c:if>
</c:if>
</body>
</html>
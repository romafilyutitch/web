<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="by.epam.jwd.web.model.Status" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="orders"/>
<html>
<head>
    <title><fmt:message key="title"/></title>
    <link rel="stylesheet" href="webjars/bootstrap/5.0.1/css/bootstrap.css"/>
    <script type="text/javascript" src="webjars/jquery/2.1.1/jquery.js"></script>
    <script type="text/javascript" src="webjars/bootstrap/5.0.1/js/bootstrap.js"></script>
</head>
<body class="text-center">
<div class="container">
    <div class="row justify-content-center">
        <div class="col">
            <h2 class="text text-info"><fmt:message key="orders"/></h2>
        </div>
    </div>
    <c:choose>
        <c:when test="${not empty requestScope.orders}">
            <div class="row align-items-center justify-content-center">
                <div class="col">
                    <table class="table table-striped table-hover">
                        <thead>
                        <tr>
                            <td><fmt:message key="user"/></td>
                            <td><fmt:message key="book"/></td>
                            <td><fmt:message key="date"/></td>
                            <td><fmt:message key="status"/></td>
                            <td><fmt:message key="action"/></td>
                        </tr>
                        </thead>
                        <c:forEach var="book" items="${requestScope.orders}">
                            <tr>
                                <td>${book.user.login}</td>
                                <td>${book.book.name}</td>
                                <td>${ctg:localDateParser(book.orderDate, sessionScope.locale)}</td>
                                <td><fmt:message key="${book.status}"/></td>
                                <td><c:choose>
                                    <c:when test="${book.status eq Status.ORDERED}">
                                        <form method="POST" action="controller">
                                            <input type="hidden" name="command" value="approve_order">
                                            <input type="hidden" name="id" value="${book.id}">
                                            <button class="btn btn-outline-success" type="submit"><fmt:message key="approveOrder"/></button>
                                        </form>
                                    </c:when>
                                    <c:when test="${book.status eq Status.RETURNED}">
                                        <form method="POST" action="controller">
                                            <input type="hidden" name="command" value="delete_order">
                                            <input type="hidden" name="id" value="${book.id}">
                                            <button class="btn btn-outline-danger" type="submit"><fmt:message key="deleteOrder"/></button>
                                        </form>
                                    </c:when>
                                </c:choose></td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </div>
            <div class="row align-items-center justify-content-center">
                <div class="col">
                    <nav>
                        <ul class="pagination pagination-lg justify-content-center">
                            <c:if test="${requestScope.currentPageNumber ne 1}">
                                <li class="page-item"><a class="page-link" href="controller?command=show_orders&page=${requestScope.currentPageNumber - 1}"><fmt:message key="previous"/></a></li>
                            </c:if>
                            <c:forEach begin="1" end="${requestScope.pagesAmount}" var="i">
                                <c:choose>
                                    <c:when test="${i eq requestScope.currentPageNumber}">
                                        <li class="page-item active">
                                        <a class="page-link" href="controller?command=show_orders&page=${i}">${i}</a>
                                        </li>
                                    </c:when>
                                    <c:otherwise>
                                        <li class="page-item"><a class="page-link" href="controller?command=show_orders&page=${i}">${i}</a></li>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                            <c:if test="${requestScope.currentPageNumber lt requestScope.pagesAmount}">
                                <li class="page-item"><a class="page-link" href="controller?command=show_orders&page=${requestScope.currentPageNumber + 1}"><fmt:message
                                        key="next"/></a></li>
                            </c:if>
                        </ul>
                    </nav>
                </div>
            </div>
        </c:when>
        <c:otherwise>
            <div class="row align-items-center justify-content-center">
                <div class="col">
                    <h2 class="text-center text-info"><fmt:message key="noOrders"/></h2>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
    <div class="row align-items-center justify-content-center">
        <div class="col justify-content-center">
            <a class="btn btn-primary" href="controller?command=main"><fmt:message key="main"/></a>
        </div>
    </div>
</div>
</body>
</html>

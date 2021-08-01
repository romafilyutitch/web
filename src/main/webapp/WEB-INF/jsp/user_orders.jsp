<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="by.epam.jwd.web.model.Status" %>
<%@ page import="by.epam.jwd.web.model.Genre" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="userOrders"/>
<html>
<head>
    <title><fmt:message key="title"/></title>
    <link rel="stylesheet" href="webjars/bootstrap/5.0.1/css/bootstrap.css"/>
    <script type="text/javascript" src="webjars/jquery/2.1.1/jquery.js"></script>
    <script type="text/javascript" src="webjars/bootstrap/5.0.1/js/bootstrap.js"></script>
</head>
<body class="text-center">
<div class="container align-items-center">
    <c:if test="${not empty requestScope.message}">
        <div class="alert alert-info">
            ${requestScope.message}
        </div>
    </c:if>
    <c:choose>
        <c:when test="${not empty requestScope.orders}">
            <div class="row justify-content-center">
                <div class="col">
                    <h2 class="text text-info"><fmt:message key="yourOrders"/></h2>
                </div>
            </div>
            <div class="row row-cols-auto">
                <c:forEach var="user" items="${requestScope.orders}">
                    <div class="card col-md-2 offset-md-1" style="width: 20rem; margin-bottom: 5rem">
                        <div class="card-body">
                            <h5 class="card-title text-success text-center"> ${user.book.name}</h5>
                            <h6 class="card-subtitle mb-2 text-info"><fmt:message key="bookAuthor"/> ${user.book.author.name}</h6>
                            <h6 class="card-subtitle mb-2 text-info"><fmt:message key="bookGenre"/>
                                <c:choose>
                                    <c:when test="${user.book.genre eq Genre.FICTION}">
                                        <fmt:message key="fiction"/>
                                    </c:when>
                                    <c:when test="${user.book.genre eq Genre.FANTASY}">
                                        <fmt:message key="fantasy"/>
                                    </c:when>
                                    <c:when test="${user.book.genre eq Genre.SCIENCE}">
                                        <fmt:message key="science"/>
                                    </c:when>
                                </c:choose>
                            </h6>
                            <h6 class="card-subtitle mb-2 text-info"><fmt:message key="orderStatus"/>
                                <c:choose>
                                    <c:when test="${user.status eq Status.ORDERED}">
                                        <fmt:message key="ordered"/>
                                    </c:when>
                                    <c:when test="${user.status eq Status.APPROVED}">
                                        <fmt:message key="approved"/>
                                    </c:when>
                                    <c:when test="${user.status eq Status.RETURNED}">
                                        <fmt:message key="returned"/>
                                    </c:when>
                                </c:choose>
                            </h6>
                            <h6 class="card-subtitle mb-2 text-info"><fmt:message key="bookPages"/> ${user.book.pagesAmount}</h6>
                            <c:if test="${user.status eq Status.APPROVED}">
                                <button class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#readModal${user.id}">
                                    <fmt:message key="read"/>
                                </button>
                                <div class="modal fade" id="readModal${user.id}" tabindex="-1" aria-labelledby="modalLabel" aria-hidden="true">
                                    <div class="modal-dialog">
                                        <div class="modal-content">
                                            <div class="modal-header">
                                                <h5 class="modal-title" id="modalLabel">${user.book.name}</h5>
                                                <button class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                            </div>
                                            <div class="modal-body">
                                                ${user.book.description}
                                            </div>
                                            <div class="modal-footer">
                                                <button class="btn btn-primary" data-bs-dismiss="modal"><fmt:message key="close"/></button>
                                                <button class="btn btn-secondary" onclick="returnOrder(${user.id})"><fmt:message key="returnBook"/></button>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:when>
        <c:otherwise>
            <h2 class="text text-info"><fmt:message key="noOrders"/></h2>
        </c:otherwise>
    </c:choose>
    <a class="btn btn-primary" href="controller?command=main"><fmt:message key="main"/></a>
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/userOrders.js"></script>
</body>
</html>

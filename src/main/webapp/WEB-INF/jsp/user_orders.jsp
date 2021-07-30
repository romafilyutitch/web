<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="by.epam.jwd.web.model.Status" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
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
    <c:choose>
        <c:when test="${not empty requestScope.orders}">
            <div class="row justify-content-center">
                <div class="col">
                    <h2 class="text text-info"><fmt:message key="yourOrders"/></h2>
                </div>
            </div>
            <div class="row row-cols-auto">
                <c:forEach var="order" items="${requestScope.orders}">
                    <div class="card col-md-2 offset-md-1" style="width: 20rem; margin-bottom: 5rem">
                        <div class="card-body">
                            <h5 class="card-title text-success text-center"> ${order.book.name}</h5>
                            <h6 class="card-subtitle mb-2 text-info"><fmt:message key="bookAuthor"/> ${order.book.author.name}</h6>
                            <h6 class="card-subtitle mb-2 text-info"><fmt:message key="bookGenre"/> <fmt:message key="${order.book.genre}"/></h6>
                            <h6 class="card-subtitle mb-2 text-info"><fmt:message key="orderStatus"/> <fmt:message key="${order.status}"/></h6>
                            <h6 class="card-subtitle mb-2 text-info"><fmt:message key="bookPages"/> ${order.book.pagesAmount}</h6>
                            <c:if test="${order.status eq Status.APPROVED}">
                                <button class="btn btn-primary" onclick="returnOrder(${order.id})">
                                    <fmt:message key="returnBook"/>
                                </button>
                                <p>
                                    <button class="btn btn-outline-success" type="button" data-bs-toggle="collapse"
                                            data-bs-target="#collapse${order.id}" aria-expanded="false" aria-controls="collapse">
                                        <fmt:message key="read"/>
                                    </button>
                                </p>
                                <div class="collapse" id="collapse${order.id}">
                                    <div class="card card-body">
                                            ${order.book.description}
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

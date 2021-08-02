<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="by.epam.jwd.web.model.UserRole" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="users"/>
<html>
<head>
    <title><fmt:message key="title"/></title>
    <link rel="stylesheet" href="webjars/bootstrap/5.0.1/css/bootstrap.css"/>
    <link rel="stylesheet" href="webjars/bootstrap-datepicker/1.7.1/css/bootstrap-datepicker.css">
    <script type="text/javascript" src="webjars/jquery/2.1.1/jquery.js"></script>
    <script type="text/javascript" src="webjars/bootstrap/5.0.1/js/bootstrap.js"></script>
    <script type="text/javascript" src="webjars/momentjs/2.29.1/min/moment-with-locales.js"></script>
    <script type="text/javascript" src="webjars/bootstrap-datepicker/1.7.1/js/bootstrap-datepicker.js"></script>
    <script type="text/javascript" src="webjars/bootstrap-datepicker/1.7.1/locales/bootstrap-datepicker.ru.min.js"></script>
    <script type="text/javascript" src="webjars/bootstrap-datepicker/1.7.1/locales/bootstrap-datepicker.en-GB.min.js"></script>
</head>
<body class="text-center">
<div class="container">
    <div class="row">
        <div class="col justify-content-center">
            <h1><fmt:message key="list"/></h1>
        </div>
    </div>
    <c:if test="${not empty requestScope.message}">
        <div calss="row">
            <div class="alert alert-info">
                ${requestScope.message}
            </div>
        </div>
    </c:if>
    <div class="row align-items-center">
        <div class="col justify-content-center">
            <c:if test="${not empty requestScope.users}">
                <table class="table table-striped table-hover">
                    <thead>
                    <tr>
                        <td><fmt:message key="login"/></td>
                        <td><fmt:message key="role"/></td>
                        <td><fmt:message key="startDate"/></td>
                        <td><fmt:message key="endDate"/></td>
                        <td><fmt:message key="setSubscription"/></td>
                        <td><fmt:message key="setRole"/></td>
                    </tr>
                    </thead>
                    <c:forEach var="order" items="${requestScope.users}">
                        <tr>
                            <td>${order.login}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${order.role eq UserRole.READER}">
                                        <fmt:message key="reader"/>
                                    </c:when>
                                    <c:when test="${order.role eq UserRole.LIBRARIAN}">
                                        <fmt:message key="librarian"/>
                                    </c:when>
                                    <c:when test="${order.role eq UserRole.ADMIN}">
                                        <fmt:message key="admin"/>
                                    </c:when>
                                </c:choose>
                            </td>
                            <td><c:if test="${not empty order.subscription}">
                                ${ctg:localDateParser(order.subscription.startDate)}
                            </c:if></td>
                            <td><c:if test="${not empty order.subscription}">
                                ${ctg:localDateParser(order.subscription.endDate)}
                            </c:if></td>
                            <td>
                                <form class="needs-validation" action="controller" method="post" novalidate>
                                    <input type="hidden" name="command" value="set_subscription">
                                    <input type="hidden" id="locale" name="locale" value="${sessionScope.language}">
                                    <input type="hidden" name="id" value="${order.id}">
                                    <label class="form-label" for="startDate"><fmt:message key="startDate"/></label>
                                    <input class="form-control datepicker" id="startDate" type="text" name="start_date" required pattern="\d{2}.\d{2}.\d{4}">
                                    <div class="valid-feedback"><fmt:message key="validStartDate"/></div>
                                    <div class="invalid-feedback"><fmt:message key="invalidStartDate"/></div>
                                    <label class="form-label" for="endDate"><fmt:message key="endDate"/></label>
                                    <input class="form-control datepicker" id="endDate" type="text" name="end_date" required pattern="\d{2}.\d{2}.\d{4}">
                                    <div class="valid-feedback"><fmt:message key="validEndDate"/></div>
                                    <div class="invalid-feedback"><fmt:message key="invalidEndDate"/></div>
                                    <button class="btn btn-outline-success" type="submit"><fmt:message key="setSubscription"/></button>
                                </form>
                            </td>
                            <td>
                                <c:if test="${order.role eq UserRole.READER}">
                                    <button class="btn btn-outline-success" onclick="promoteUserRole(${order.id})"><fmt:message key="promoteRole"/></button>
                                </c:if>
                                <c:if test="${order.role eq UserRole.LIBRARIAN}">
                                    <button class="btn btn-outline-danger" onclick="demoteUserRole(${order.id})"><fmt:message key="demoteRole"/></button>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <nav>
                    <ul class="pagination pagination-lg justify-content-center">
                        <c:if test="${not empty requestScope.currentPageNumber and requestScope.currentPageNumber ne 1}">
                            <li class="page-item">
                                <button class="page-link" onclick="findPage(${requestScope.currentPageNumber - 1})"><fmt:message key="previous"/></button>
                            </li>
                        </c:if>
                        <c:forEach begin="1" end="${requestScope.pagesAmount}" var="i">
                            <c:choose>
                                <c:when test="${i eq requestScope.currentPageNumber}">
                                    <li class="page-item active">
                                        <button class="page-link" onclick="findPage(${i})">${i}</button>
                                    </li>
                                </c:when>
                                <c:otherwise>
                                    <li class="page-item">
                                        <button class="page-link" onclick="findPage(${i})">${i}</button>
                                    </li>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${not empty requestScope.currentPageNumber and requestScope.currentPageNumber lt requestScope.pagesAmount}">
                            <li class="page-item">
                                <button class="page-link" onclick="findPage(${requestScope.currentPageNumber + 1})"><fmt:message key="next"/></button>
                            </li>
                        </c:if>
                    </ul>
                </nav>
            </c:if>
        </div>
    </div>
    <div class="row justify-content-center">
        <div class="col">
            <a class="btn btn-primary" href="controller?command=main"><fmt:message key="main"/></a>
        </div>
    </div>
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/users.js"></script>
</body>
</html>

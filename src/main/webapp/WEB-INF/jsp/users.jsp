<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="by.epam.jwd.web.model.UserRole" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="users"/>
<html>
<head>
    <title><fmt:message key="title"/></title>
    <link rel="stylesheet" href="webjars/bootstrap/5.0.1/css/bootstrap.css"/>
    <script type="text/javascript" src="webjars/jquery/2.1.1/jquery.js"></script>
    <script type="text/javascript" src="webjars/bootstrap/5.0.1/js/bootstrap.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/formValidation.js"></script>
</head>
<body class="text-center">
<div class="container">
    <div class="row">
        <div class="col justify-content-center">
            <h1><fmt:message key="list"/></h1>
        </div>
    </div>
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
                    <c:forEach var="book" items="${requestScope.users}">
                        <tr>
                            <td>${book.login}</td>
                            <td>${book.role}</td>
                            <td><c:if test="${not empty book.subscription}">
                                ${ctg:localDateParser(book.subscription.startDate, sessionScope.locale)}
                            </c:if></td>
                            <td><c:if test="${not empty book.subscription}">
                                ${ctg:localDateParser(book.subscription.endDate, sessionScope.locale)}
                            </c:if></td>
                            <td>
                                <form class="needs-validation" action="controller" method="post" novalidate>
                                    <input type="hidden" name="command" value="set_subscription">
                                    <input type="hidden" name="id" value="${book.id}">
                                    <label class="form-label" for="startDate"><fmt:message key="startDate"/></label>
                                    <input class="date" id="startDate" type="date" required
                                           min="${sessionScope.currentDate}" name="start_date">
                                    <div class="valid-feedback"><fmt:message key="validStartDate"/></div>
                                    <div class="invalid-feedback"><fmt:message key="invalidStartDate"/></div>
                                    <label class="form-label" for="endDate"><fmt:message key="endDate"/></label>
                                    <input class="datepicker" id="endDate" type="date" required
                                           min="${sessionScope.currentDate}" name="end_date">
                                    <div class="valid-feedback"><fmt:message key="validEndDate"/></div>
                                    <div class="invalid-feedback"><fmt:message key="invalidEndDate"/></div>
                                    <button class="btn btn-outline-success" type="submit"><fmt:message
                                            key="setSubscription"/></button>
                                </form>
                            </td>
                            <td>
                                <c:if test="${book.role ne UserRole.ADMIN}">
                                    <form name="promote role" method="POST" action="controller">
                                        <input type="hidden" name="command" value="promote_role">
                                        <input type="hidden" name="id" value="${book.id}">
                                        <button class="btn btn-outline-success" type="submit"><fmt:message
                                                key="promoteRole"/></button>
                                    </form>
                                </c:if>
                                <c:if test="${book.role ne UserRole.READER}">
                                    <form name="demote role" method="POST" action="controller?command=demote_role">
                                        <input type="hidden" name="command" value="demote_role">
                                        <input type="hidden" name="id" value="${book.id}">
                                        <button class="btn btn-outline-danger" type="submit"><fmt:message
                                                key="demoteRole"/></button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <nav>
                    <ul class="pagination pagination-lg justify-content-center">
                        <c:if test="${not empty requestScope.currentPageNumber and requestScope.currentPageNumber ne 1}">
                            <li class="page-item">
                                <a class="page-link"
                                   href="controller?command=show_users&page=${requestScope.currentPageNumber - 1}"><fmt:message
                                        key="previous"/></a>
                            </li>
                        </c:if>
                        <c:forEach begin="1" end="${requestScope.pagesAmount}" var="i">
                            <c:choose>
                                <c:when test="${i eq requestScope.currentPageNumber}">
                                    <li class="page-item active">
                                        <a class="page-link" href="controller?command=show_users&page=${i}">${i}</a>
                                    </li>
                                </c:when>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${not empty requestScope.currentPageNumber and requestScope.currentPageNumber lt requestScope.pagesAmount}">
                            <li class="page-item">
                                <a href="controller?command=show_users&page=${requestScope.currentPageNumber + 1}"><fmt:message
                                        key="next"/></a>
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
</body>
</html>

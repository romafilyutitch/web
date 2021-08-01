<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="account"/>
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
            <h2 class="text text-info"><fmt:message key="yourAccount"/></h2>
        </div>
    </div>
    <c:if test="${not empty requestScope.message}">
        <div class="row justify-content-center">
            <div class="col">
                ${requestScope.message}
            </div>
        </div>
    </c:if>
    <div class="row align-items-center justify-content-center">
        <table class="table table-striped table-hover">
            <thead>
            <tr>
                <td><fmt:message key="login"/></td>
                <td><fmt:message key="role"/></td>
                <c:if test="${not empty sessionScope.user.subscription}">
                    <td><fmt:message key="subscriptionStartDate"/></td>
                    <td><fmt:message key="subscriptionEndDate"/></td>
                </c:if>
            </tr>
            </thead>
            <tr>
                <td>${sessionScope.user.login}</td>
                <td>${sessionScope.user.role}</td>
                <c:if test="${not empty sessionScope.user.subscription}">
                    <td>${ctg:localDateParser(sessionScope.user.subscription.startDate)}</td>
                    <td>${ctg:localDateParser(sessionScope.user.subscription.endDate)}</td>
                </c:if>
            </tr>
        </table>
    </div>
    <div class="row justify-content-center">
        <div class="col-5 justify-content-center">
            <form id="changeLoginForm" class="needs-validation" method="post" action="controller" novalidate>
                <input type="hidden" name="command" value="change_login">
                <input type="hidden" name="id" value="${sessionScope.user.id}">
                <label class="form-label" for="loginInput"><fmt:message key="login"/></label>
                <input class="form-control" id="loginInput" type="text" name="login" required pattern="[A-Za-z0-9]+">
                <div class="valid-feedback"><fmt:message key="validLogin"/></div>
                <div class="invalid-feedback"><fmt:message key="invalidLogin"/></div>
                <button class="btn btn-primary" type="submit"><fmt:message key="changeLogin"/></button>
            </form>
        </div>
        <div class="col-5 justify-content-center">
            <form id="changePasswordForm" class="needs-validation" method="post" action="controller" novalidate>
                <input type="hidden" name="command" value="change_password">
                <input type="hidden" name="id" value="${sessionScope.user.id}">
                <label class="form-label" for="password"><fmt:message key="changePassword"/></label>
                <input class="form-control" id="password" type="password" name="password" required
                       pattern="[A-Za-z0-9]+">
                <div class="valid-feedback"><fmt:message key="validPassword"/></div>
                <div class="invalid-feedback"><fmt:message key="invalidPassword"/></div>
                <button class="btn btn-primary" type="submit"><fmt:message key="changePassword"/></button>
            </form>
        </div>
    </div>
    <div class="row justify-content-center">
        <div class="col">
            <button class="btn btn-danger" onclick="deleteAccount(${sessionScope.user.id})"><fmt:message
                    key="deleteAccount"/></button>
        </div>
        <div class="col">
            <a class="btn btn-primary" href="controller?command=main"><fmt:message key="main"/></a>
        </div>
    </div>
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/account.js"></script>
</body>
</html>

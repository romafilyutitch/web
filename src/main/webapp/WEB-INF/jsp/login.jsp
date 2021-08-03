<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="login"/>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="webjars/bootstrap/5.0.1/css/bootstrap.css"/>
    <script type="text/javascript" src="webjars/jquery/2.1.1/jquery.js"></script>
    <script type="text/javascript" src="webjars/bootstrap/5.0.1/js/bootstrap.js"></script>
</head>
<body class="text-center">
<c:choose>
   <c:when test="${not empty requestScope.message}">
       <div class="alert alert-info">
           ${requestScope.message}
           <a class="link-info" href="controller?command=main"><fmt:message key="main"/></a>
       </div>
   </c:when>
    <c:otherwise>
        <div class="container">
            <div class="row justify-content-center align-items-center">
                <div class="col-md-6">
                    <div class="col-md-12">
                        <form id="loginForm" name="loginForm" method="POST" action="controller" novalidate>
                            <input type="hidden" name="command" value="login">
                            <div class="mb-3">
                                <label for="login" class="form-label"><fmt:message key="login"/></label>
                                <input id="login" type="text" class="form-control" name="login" required pattern="\w{1,10}">
                                <div class="valid-feedback"><fmt:message key="validLogin"/></div>
                                <div class="invalid-feedback"><fmt:message key="invalidLogin"/></div>
                            </div>
                            <div class="mb-3">
                                <label for="password" class="form-label"><fmt:message key="password"/></label>
                                <input id="password" type="password" class="form-control" name="password" required pattern="\w{1,10}">
                                <div class="valid-feedback"><fmt:message key="validPassword"/></div>
                                <div class="invalid-feedback"><fmt:message key="invalidPassword"/></div>
                            </div>
                            <button id="loginBtn" class="btn btn-primary" type="submit"><fmt:message key="login"/></button>
                            <a class="btn btn-primary" href="controller?command=main"><fmt:message key="main"/></a>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </c:otherwise>
</c:choose>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/login.js"></script>
</body>
</html>

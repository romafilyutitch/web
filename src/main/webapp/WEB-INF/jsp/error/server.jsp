<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="server"/>
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
        <div class="col text-center">
            <div class="alert alert-danger">
                <h2 class="text-danger">
                    <fmt:message key="message"/>
                </h2>
            </div>
        </div>
    </div>
    <div class="row justify-content-center">
        <div class="col text-center">
            <a class="link-danger" href="controller?command=main"><fmt:message key="main"/></a>
        </div>
    </div>
</div>
</body>
</html>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="by.epam.jwd.web.model.UserRole" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="main"/>
<html>
<head>
    <title>Navbar</title>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
        <a class="navbar-brand" href="controller?command=main"><fmt:message key="welcome"/></a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarSupportedContent">
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <li class="nav-item">
                    <a class="nav-link active" aria-current="page" href="#"><ctg:date/></a>
                </li>
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <li class="nav-item"><a class="nav-link" href="controller?command=show_user_orders"><fmt:message key="myOrders"/></a></li>
                        <li class="nav-item"><a class="nav-link" href="controller?command=show_account"><fmt:message key="myAccount"/></a></li>
                        <c:if test="${sessionScope.user.role eq UserRole.ADMIN or sessionScope.user.role eq UserRole.LIBRARIAN}">
                            <c:if test="${sessionScope.user.role eq UserRole.ADMIN}">
                                <li class="nav-item"><a class="nav-link" href="controller?command=show_users"><fmt:message key="users"/></a></li>
                            </c:if>
                            <li class="nav-item"><a class="nav-link" href="controller?command=show_books"><fmt:message key="books"/></a></li>
                            <li class="nav-item"><a class="nav-link" href="controller?command=show_orders"><fmt:message key="orders"/></a></li>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <li class="nav-item"><a class="nav-link" href="controller?command=show_login"><fmt:message key="login"/></a></li>
                        <li class="nav-item"><a class="nav-link" href="controller?command=show_register"><fmt:message key="register"/></a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </div>
</nav>
</body>
</html>

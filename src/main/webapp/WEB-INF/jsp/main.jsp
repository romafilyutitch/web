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
    <link rel="stylesheet" href="webjars/bootstrap/5.0.1/css/bootstrap.css"/>
    <script type="text/javascript" src="webjars/jquery/2.1.1/jquery.js"></script>
    <script type="text/javascript" src="webjars/bootstrap/5.0.1/js/bootstrap.js"></script>

</head>
<body>
<nav class="navbar navbar-light navbar-expand-lg bg-light">
    <div class="container-fluid">
        <fmt:message key="welcome"/>
        <ctg:time/>
        <div class="collapse navbar-collapse">
            <div class="navbar-nav">
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <a class="nav-link" href="#"><fmt:message key="hello"/>${sessionScope.user.login}</a>
                        <a class="nav-link" href="controller?command=logout"><fmt:message key="logout"/></a>
                        <a class="nav-link" href="controller?command=show_user_orders"><fmt:message key="myOrders"/></a>
                        <a class="nav-link" href="controller?command=show_account"><fmt:message key="myAccount"/></a>
                        <c:if test="${sessionScope.user.role eq UserRole.ADMIN or sessionScope.user.role eq UserRole.LIBRARIAN}">
                            <c:if test="${sessionScope.user.role eq UserRole.ADMIN}">
                                <a class="nav-link" href="controller?command=show_users"><fmt:message key="users"/></a>
                            </c:if>
                            <a class="nav-link" href="controller?command=show_books"><fmt:message key="books"/></a>
                            <a class="nav-link" href="controller?command=show_orders"><fmt:message key="orders"/></a>
                        </c:if>
                    </c:when>
                    <c:otherwise>
                        <a class="nav-link" href="controller?command=show_login"><fmt:message key="login"/></a>
                        <a class="nav-link" href="controller?command=show_register"><fmt:message key="register"/></a>
                    </c:otherwise>
                </c:choose>
                <a class="nav-link" href="controller?command=set_locale&locale=en"><fmt:message key="english"/></a>
                <a class="nav-link" href="controller?command=set_locale&locale=ru"><fmt:message key="russian"/></a>
            </div>
        </div>
    </div>
</nav>
<div class="container">
    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success">
            <fmt:message key="${sessionScope.success}"/>
        </div>
    </c:if>
    <c:if test="${not empty sessionScope.fail}">
        <div class="alert alert-danger">
            <fmt:message key="${sessionScope.fail}"/>
        </div>
    </c:if>
    <div>
        <a class="btn btn-primary" href="controller?command=find_fiction"><fmt:message key="FICTION"/></a>
        <a class="btn btn-primary" href="controller?command=find_fantasy"><fmt:message key="FANTASY"/></a>
        <a class="btn btn-primary" href="controller?command=find_science"><fmt:message key="SCIENCE"/></a>
        <a class="btn btn-primary" href="controller?command=main"><fmt:message key="all"/></a>
    </div>
    <form class="needs-validation" name="find" method="get" action="controller" novalidate>
        <input type="hidden" name="command" value="find_book_by_name">
        <label for="bookName"><fmt:message key="name"/></label>
        <input type="search" id="bookName" class="form-control" required pattern="[A-Za-z\s]+" name="name">
        <button type="submit" class="btn btn-outline-primary"><fmt:message key="search"/></button>
        <div class="invalid-feedback"><fmt:message key="invalidSearch"/></div>
    </form>
    <div>
        <c:if test="${not empty requestScope.findResult}">
            <div class="alert alert-info">
                <fmt:message key="${requestScope.findResult}"/>
            </div>
        </c:if>
    </div>
    <c:if test="${not empty requestScope.books }">
        <div class="row row-cols-auto">
            <c:forEach var="book" items="${requestScope.books}">
                <div class="col-md-2 card offset-md-1" style="width: 20rem; margin-bottom: 5rem">
                    <div class="card-body">
                        <h5 class="card-title text-center text-success">${book.name}</h5>
                        <h6 class="card-subtitle mb-2 text-info"><fmt:message key="author"/> ${book.author.name}</h6>
                        <h6 class="card-subtitle mb-2 text-info"><fmt:message key="genre"/> <fmt:message key="${book.genre}"/></h6>
                        <c:if test="${not empty sessionScope.user and sessionScope.user.role ne UserRole.UNAUTHORIZED}">
                            <form action="controller" method="POST">
                                <input type="hidden" name="command" value="order_book">
                                <input type="hidden" name="id" value="${book.id}">
                                <c:choose>
                                    <c:when test="${book.copiesAmount eq 0}">
                                        <button class="btn btn-outline-primary" type="submit" disabled>
                                            <fmt:message key="order"/>
                                            <span class="badge bg-danger">${book.copiesAmount}</span>
                                        </button>
                                    </c:when>
                                    <c:otherwise>
                                        <button class="btn btn-outline-primary" type="submit">
                                            <fmt:message key="order"/>
                                            <span class="badge bg-success">${book.copiesAmount}</span></button>
                                    </c:otherwise>
                                </c:choose>
                            </form>
                            <a class="btn card-link btn-outline-success" href="#"><fmt:message key="comments"/></a>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
        </div>
    </c:if>
    <div class="row align-items-center">
        <div class="col">
            <nav>
                <ul class="pagination pagination-lg justify-content-center">
                    <c:if test="${not empty requestScope.currentPageNumber and requestScope.currentPageNumber ne 1}">
                        <li class="page-item"><a class="page-link"
                                                 href="controller?command=main&page=${requestScope.currentPageNumber - 1}"><fmt:message
                                key="previous"/></a></li>
                    </c:if>
                    <c:forEach begin="1" end="${requestScope.pagesAmount}" var="i">
                        <c:choose>
                            <c:when test="${i eq requestScope.currentPageNumber}">
                                <li class="page-item active"><a class="page-link"
                                                                href="controller?command=main&page=${i}">${i}</a></li>
                            </c:when>
                            <c:otherwise>
                                <li class="page-item"><a class="page-link"
                                                         href="controller?command=main&page=${i}">${i}</a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <c:if test="${not empty requestScope.currentPageNumber and requestScope.currentPageNumber lt requestScope.pagesAmount}">
                        <li class="page-item"><a class="page-link"
                                                 href="controller?command=main&page=${requestScope.currentPageNumber + 1}"><fmt:message
                                key="next"/></a></li>
                    </c:if>
                </ul>
            </nav>
        </div>
    </div>
</div>
<script type="text/javascript">
        // Example starter JavaScript for disabling form submissions if there are invalid fields
        (function () {
            'use strict'
            // Fetch all the forms we want to apply custom Bootstrap validation styles to
            const forms = document.querySelectorAll('.needs-validation')
            // Loop over them and prevent submission
            Array.from(forms)
                .forEach(function (form) {
                    form.addEventListener('submit', function (event) {
                        if (!form.checkValidity()) {
                            event.preventDefault()
                            event.stopPropagation()
                        }

                        form.classList.add('was-validated')
                    }, false)
                })
        })()
</script>
</body>
</html>
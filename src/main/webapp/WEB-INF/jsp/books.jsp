<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="customtags"%>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="books"/>
<html>
<head>
    <title>Book page</title>
    <link rel="stylesheet" href="webjars/bootstrap/5.0.1/css/bootstrap.css"/>
    <script type="text/javascript" src="webjars/jquery/2.1.1/jquery.js"></script>
    <script type="text/javascript" src="webjars/bootstrap/5.0.1/js/bootstrap.js"></script>
</head>
<body class="text-center">
<div class="container">
    <div class="row justify-content-center">
        <div class="col">
            <h2 class="text text-info"><fmt:message key="books"/></h2>
        </div>
    </div>
    <div class="row">
        <div class="col">
            <c:if test="${not empty requestScope.books }" >
                <table class="table table-hover table-striped">
                    <thead>
                    <tr>
                        <td><fmt:message key="name"/></td>
                        <td><fmt:message key="author"/></td>
                        <td><fmt:message key="genre"/></td>
                        <td><fmt:message key="date"/></td>
                        <td><fmt:message key="pages"/></td>
                        <td><fmt:message key="copies"/></td>
                        <td><fmt:message key="description"/></td>
                        <td><fmt:message key="addCopy"/></td>
                        <td><fmt:message key="removeCopy"/></td>
                        <td><fmt:message key="deleteBook"/></td>
                    </tr>
                    </thead>
                    <c:forEach var="book" items="${requestScope.books}">
                        <tr>
                            <td>${book.name}</td>
                            <td>${book.author.name}</td>
                            <td><fmt:message key="${book.genre}"/></td>
                            <td>${ctg:localDateParser(book.date, sessionScope.locale)}</td>
                            <td>${book.pagesAmount}</td>
                            <td>${book.copiesAmount}</td>
                            <td>${book.description}</td>
                            <td>
                                <form name="add one copy" action="controller" method="POST">
                                    <input type="hidden" name="command" value="add_copy">
                                    <input type="hidden" name="id" value="${book.id}">
                                    <button class="btn btn-outline-primary" type="submit"><fmt:message key="addCopy"/></button>
                                </form>
                            </td>
                            <td>
                                <c:if test="${book.copiesAmount gt 0}">
                                    <form name="remove one copy" action="controller" method="POST">
                                        <input type="hidden" name="command" value="remove_copy">
                                        <input type="hidden" name="id" value="${book.id}">
                                        <button class="btn btn-outline-primary" type="submit"><fmt:message key="removeCopy"/></button>
                                    </form>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${book.copiesAmount eq 0}">
                                    <form name="delete book" action="controller" method="POST">
                                        <input type="hidden" name="command" value="delete_book">
                                        <input type="hidden" name="id" value="${book.id}">
                                        <button class="btn btn-danger" type="submit"><fmt:message key="deleteBook"/></button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:if>
        </div>
    </div>
    <div class="row align-items-center">
        <div class="col">
            <nav>
                <ul class="pagination pagination-lg justify-content-center">
                    <c:if test="${requestScope.currentPageNumber ne 1}">
                        <li class="page-item"><a class="page-link" href="controller?command=show_books&page=${requestScope.currentPageNumber - 1}"><fmt:message key="previous"/></a></li>
                    </c:if>
                    <c:forEach begin="1" end="${requestScope.pagesAmount}" var="i">
                        <c:choose>
                            <c:when test="${i eq requestScope.currentPageNumber}">
                                <li class="page-item active">
                                    <a class="page-link" href="controller?command=show_books&page=${i}">${i}</a>
                                </li>
                            </c:when>
                            <c:otherwise>
                                <li class="page-item">
                                    <a class="page-link" href="controller?command=show_books&page=${i}">${i}</a>
                                </li>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <c:if test="${requestScope.currentPageNumber lt requestScope.pagesAmount}">
                        <li class="page-item"><a class="page-link" href="controller?command=show_books&page=${requestScope.currentPageNumber + 1}"><fmt:message key="next"/></a></li>
                    </c:if>
                </ul>
            </nav>
        </div>
    </div>
</div>
<div class="container">
    <div class="row row-cols-auto justify-content-center">
            <form class="needs-validation" action="controller" method="post" novalidate>
                <input type="hidden" name="command" value="add_book">
                <div class="col">
                    <label for="name" class="form-label"><fmt:message key="name"/></label>
                    <input id="name" class="form-control" type="text" name="name" required pattern="^\S[A-Za-z0-9\s]+$">
                    <div class="valid-feedback"><fmt:message key="validName"/></div>
                    <div class="invalid-feedback"><fmt:message key="invalidName"/></div>
                </div>
                <div class=col">
                    <label for="author" class="form-label"><fmt:message key="author"/></label>
                    <input id="author" class="form-control" type="text" name="author" required pattern="^\S[A-Za-z\s]+$">
                    <div class="valid-feedback"><fmt:message key="validAuthor"/></div>
                    <div class="invalid-feedback"><fmt:message key="invalidAuthor"/></div>
                </div>
                <div class="col">
                    <label for="genre" class="form-label"><fmt:message key="genre"/></label>
                        <select id="genre" class="form-select" name="genre" required>
                            <option value="FICTION"><fmt:message key="FICTION"/></option>
                            <option value="FANTASY"><fmt:message key="FANTASY"/></option>
                            <option value="SCIENCE"><fmt:message key="SCIENCE"/></option>
                        </select>
                </div>
                <div class="col">
                    <label for="date" class="form-label"><fmt:message key="date"/></label>
                    <input id="date" class="form-control" type="date" required max="${sessionScope.currentDate}" name="date">
                    <div class="valid-feedback"><fmt:message key="validDate"/></div>
                    <div class="invalid-feedback"><fmt:message key="invalidDate"/></div>
                </div>
                <div class="col">
                    <label for="pages" class="form-label"><fmt:message key="pages"/></label>
                    <input id="pages" class="form-control" type="number" required min = "1" step = "1" name="pages">
                    <div class="valid-feedback"><fmt:message key="validPages"/></div>
                    <div class="invalid-feedback"><fmt:message key="invalidPages"/></div>
                </div>
                <div class="col">
                    <label for="description" class="form-label"><fmt:message key="description"/></label>
                    <input id="description" class="form-control" type="text" required pattern="^\S[A-Za-z0-9.,-'\s]+$" name="description">
                    <div class="valid-feedback"><fmt:message key="validDescription"/></div>
                    <div class="invalid-feedback"><fmt:message key="invalidDescription"/></div>
                </div>
                <button class="btn btn-outline-success" type="submit"><fmt:message key="addNewBook"/></button>
            </form>
        </div>
</div>
<a class="btn btn-primary" href="controller?command=main"><fmt:message key="main"/></a>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/formValidation.js"></script>
</body>
</html>

<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="ctg" uri="customtags"%>
<%@ page import="by.epam.jwd.web.model.Genre" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="books"/>
<html>
<head>
    <title>Books</title>
    <link rel="stylesheet" href="webjars/bootstrap/5.0.1/css/bootstrap.css"/>
    <script type="text/javascript" src="webjars/jquery/2.1.1/jquery.js"></script>
    <script type="text/javascript" src="webjars/bootstrap/5.0.1/js/bootstrap.js"></script>
</head>
<body class="text-center">
<jsp:include page="navbar.jsp"/>
<div class="container">
    <div class="row justify-content-center">
        <div class="col">
            <h2 class="text text-info"><fmt:message key="books"/></h2>
        </div>
    </div>
    <c:if test="${not empty requestScope.message}">
        <div class="alert alert-warning alert-dismissible fade show">
            <strong><fmt:message key="message"/></strong> ${requestScope.message}
            <button class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>
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
                        <td><fmt:message key="addCopy"/></td>
                        <td><fmt:message key="removeCopy"/></td>
                        <td><fmt:message key="deleteBook"/></td>
                    </tr>
                    </thead>
                    <c:forEach var="order" items="${requestScope.books}">
                        <tr>
                            <td>${order.name}</td>
                            <td>${order.author.name}</td>
                            <td>
                                <c:choose>
                                    <c:when test="${order.genre eq Genre.FICTION}">
                                        <fmt:message key="fiction"/>
                                    </c:when>
                                    <c:when test="${order.genre eq Genre.FANTASY}">
                                        <fmt:message key="fantasy"/>
                                    </c:when>
                                    <c:when test="${order.genre eq Genre.SCIENCE}">
                                        <fmt:message key="science"/>
                                    </c:when>
                                </c:choose>
                            </td>
                            <td>${ctg:localDateFormatter(order.date)}</td>
                            <td>${order.pagesAmount}</td>
                            <td>${order.copiesAmount}</td>
                            <td>
                                <button class="btn btn-outline-primary" onclick="addOneCopyOfBook(${order.id})"><fmt:message key="addCopy"/></button>
                            </td>
                            <td>
                                <c:if test="${order.copiesAmount gt 1}">
                                    <button class="btn btn-outline-primary" onclick="removeOneCopyOfBook(${order.id})"><fmt:message key="removeCopy"/></button>
                                </c:if>
                            </td>
                            <td>
                                <c:if test="${order.copiesAmount eq 1}">
                                    <button class="btn btn-danger" onclick="deleteBook(${order.id})"><fmt:message key="deleteBook"/></button>
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
                    <c:if test="${requestScope.currentPageNumber lt requestScope.pagesAmount}">
                        <li class="page-item">
                            <button class="page-link" onclick="findPage(${requestScope.currentPageNumber + 1})"><fmt:message key="next"/></button>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </div>
    </div>
</div>
    <div class="row row-cols-auto justify-content-center">
        <button type="button" class="btn btn-primary" data-bs-toggle="modal" data-bs-target="#addBookModal">
            <fmt:message key="addNewBook"/>
        </button>
        <div class="modal fade" id="addBookModal" tabindex="-1" aria-labelledby="addBookModalLabel" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title" id="addBookModalLabel"><fmt:message key="addNewBook"/></h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <form id="addNewBookForm" action="controller" method="post" novalidate>
                            <input type="hidden" name="command" value="add_book">
                            <div class="col">
                                <label for="name" class="form-label"><fmt:message key="name"/></label>
                                <input id="name" class="form-control" type="text" name="name" required pattern="\w+(\w|\s)*">
                                <div class="valid-feedback"><fmt:message key="validName"/></div>
                                <div class="invalid-feedback"><fmt:message key="invalidName"/></div>
                            </div>
                            <div class=col">
                                <label for="author" class="form-label"><fmt:message key="author"/></label>
                                <input id="author" class="form-control" type="text" name="author" required pattern="\w+(\w|\s)*">
                                <div class="valid-feedback"><fmt:message key="validAuthor"/></div>
                                <div class="invalid-feedback"><fmt:message key="invalidAuthor"/></div>
                            </div>
                            <div class="col">
                                <label for="genre" class="form-label"><fmt:message key="genre"/></label>
                                <select id="genre" class="form-select" name="genre" required>
                                    <option value="FICTION"><fmt:message key="fiction"/></option>
                                    <option value="FANTASY"><fmt:message key="fantasy"/></option>
                                    <option value="SCIENCE"><fmt:message key="science"/></option>
                                </select>
                            </div>
                            <div class="col">
                                <label for="pages" class="form-label"><fmt:message key="pages"/></label>
                                <input id="pages" class="form-control" type="number" required min = "1" step = "1" name="pages">
                                <div class="valid-feedback"><fmt:message key="validPages"/></div>
                                <div class="invalid-feedback"><fmt:message key="invalidPages"/></div>
                            </div>
                            <div class="col">
                                <label for="text" class="form-label"><fmt:message key="text"/></label>
                                <textarea id="text" class="form-control" name="text" required></textarea>
                                <div class="valid-feedback"><fmt:message key="validText"/></div>
                                <div class="invalid-feedback"><fmt:message key="invalidText"/></div>
                            </div>
                            <button class="btn btn-outline-success" type="submit"><fmt:message key="addNewBook"/></button>
                        </form>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"><fmt:message key="close"/></button>
                    </div>
                </div>
            </div>
        </div>
        <a class="btn btn-primary" href="controller?command=main"><fmt:message key="main"/></a>
    </div>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/books.js"></script>
</body>
</html>

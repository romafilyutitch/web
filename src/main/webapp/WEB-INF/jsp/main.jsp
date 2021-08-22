<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="by.epam.jwd.web.model.UserRole" %>
<%@ page import="by.epam.jwd.web.model.Genre" %>
<%@ taglib prefix="ctg" uri="customtags" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.language}"/>
<fmt:setBundle basename="main"/>
<html>
<head>
    <title>Library application</title>
    <link rel="stylesheet" href="webjars/bootstrap/5.0.1/css/bootstrap.css"/>
    <script type="text/javascript" src="webjars/jquery/2.1.1/jquery.js"></script>
    <script type="text/javascript" src="webjars/bootstrap/5.0.1/js/bootstrap.js"></script>
</head>
<body>
<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <div class="container-fluid">
        <a class="navbar-brand" href="#"><fmt:message key="welcome"/></a>
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
                        <li class="nav-item"><a class="nav-link" href="#"><fmt:message key="hello"/> ${sessionScope.user.login}</a></li>
                        <li class="nav-item"><a class="nav-link" href="#" onclick="logout()"><fmt:message key="logout"/></a></li>
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
                <li class="nav-item">
                    <a class="nav-link" href="#" data-bs-toggle="modal" data-bs-target="#languageModal">
                        <fmt:message key="language"/>
                    </a>
                    <div class="modal fade" id="languageModal" tabindex="-1" aria-labelledby="languageModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="languageModalLabel"><fmt:message key="chooseLanguage"/></h5>
                                    <button class="btn btn-close" data-bs-dimiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <div class="list-group">
                                        <button class="list-group-item list-group-item-action" onclick="setLanguage('en')"><fmt:message key="english"/></button>
                                        <button class="list-group-item list-group-item-action" onclick="setLanguage('ru')"><fmt:message key="russian"/></button>
                                        <button class="list-group-item list-group-item-action" onclick="setLanguage('zh')"><fmt:message key="chinese"/></button>
                                        <button class="list-group-item list-group-item-action" onclick="setLanguage('hi')"><fmt:message key="indian"/></button>
                                        <button class="list-group-item list-group-item-action" onclick="setLanguage('ar')"><fmt:message key="arabic"/></button>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button class="btn btn-secondary" data-bs-dismiss="modal"><fmt:message key="close"/></button>
                                </div>
                            </div>
                        </div>
                    </div>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#" data-bs-toggle="modal" data-bs-target="#genreModal">
                        <fmt:message key="findByGenre"/>
                    </a>
                    <div class="modal fade" id="genreModal" tabindex="-1" aria-labelledby="genreModalLabel" aria-hidden="true">
                        <div class="modal-dialog">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h5 class="modal-title" id="genreModalLabel"><fmt:message key="chooseGenre"/></h5>
                                    <button class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                </div>
                                <div class="modal-body">
                                    <div class="list-group">
                                        <button class="list-group-item list-group-item-action" onclick="findByGenre('fiction')"><fmt:message key="fiction"/></button>
                                        <button class="list-group-item list-group-item-action" onclick="findByGenre('fantasy')"><fmt:message key="fantasy"/></button>
                                        <button class="list-group-item list-group-item-action" onclick="findByGenre('science')"><fmt:message key="science"/></button>
                                    </div>
                                </div>
                                <div class="modal-footer">
                                    <button class="btn btn-secondary" data-bs-dismiss="modal"><fmt:message key="close"/></button>
                                </div>
                            </div>
                        </div>
                    </div>
                </li>
                <li class="nav-item">
                    <a class="nav-link" href="#" onclick="findAllBooks()"><fmt:message key="all"/></a>
                </li>
            </ul>
            <form id="findBookByNameForm" name="find" method="get" action="controller" class="d-flex" novalidate>
                <input type="hidden" name="command" value="find_book_by_name">
                <input class="form-control me-2" type="text" id="bookName" name="name" aria-label="Search" required pattern="[\w\s]+">
                <div class="invalid-feedback"><fmt:message key="invalidFind"/></div>
                <div class="valid-feedback"><fmt:message key="validFind"/></div>
                <button class="btn btn-outline-success" type="submit"><fmt:message key="find"/></button>
            </form>
        </div>
    </div>
</nav>
<div class="container">
    <c:if test="${not empty requestScope.message}">
        <div class="alert alert-warning alert-dismissible fade show">
            <strong><fmt:message key="message"/></strong> ${requestScope.message}
            <button class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    </c:if>
    <c:if test="${not empty requestScope.books }">
        <div class="row row-cols-auto">
            <c:forEach var="book" items="${requestScope.books}">
                <div class="col-md-2 card offset-md-1" style="width: 20rem; margin-bottom: 5rem">
                    <div class="card-body">
                        <h5 class="card-title text-center text-success">${book.name}</h5>
                        <h6 class="card-subtitle mb-2 text-info"><fmt:message key="author"/> ${book.author}</h6>
                        <h6 class="card-subtitle mb-2 text-info"><fmt:message key="genre"/>
                            <c:choose>
                                <c:when test="${book.genre eq Genre.FICTION}">
                                    <fmt:message key="fiction"/>
                                </c:when>
                                <c:when test="${book.genre eq Genre.FANTASY}">
                                    <fmt:message key="fantasy"/>
                                </c:when>
                                <c:when test="${book.genre eq Genre.SCIENCE}">
                                    <fmt:message key="science"/>
                                </c:when>
                            </c:choose>
                        </h6>
                        <c:if test="${not empty sessionScope.user and sessionScope.user.role ne UserRole.UNAUTHORIZED}">
                            <c:choose>
                                <c:when test="${book.copiesAmount eq 0}">
                                    <button class="btn btn-outline-primary" disabled>
                                        <fmt:message key="order"/>
                                        <span class="badge bg-danger">${book.copiesAmount}</span>
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <button class="btn btn-outline-primary" onclick="orderBook(${book.id})">
                                        <fmt:message key="order"/>
                                        <span class="badge bg-success">${book.copiesAmount}</span>
                                    </button>
                                </c:otherwise>
                            </c:choose>
                            <button class="btn btn-outline-primary" onclick="addLike(${book.id})">
                                <fmt:message key="addLike"/>
                                <span class="badge bg-success">${book.likesAmount}</span>
                            </button>
                            <button class="btn btn-outline-success" type="button" data-bs-toggle="modal" data-bs-target="#modal${book.id}">
                                <fmt:message key="comments"/>
                                <span class="badge bg-success">${book.commentsAmount}</span>
                            </button>
                            <div class="modal fade" id="modal${book.id}" tabindex="-1" aria-labelledby="ModalLabel" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="ModalLabel"><fmt:message key="commentsForBook"/> ${book.name}</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                                        </div>
                                        <div class="modal-body">
                                            <c:forEach items="${requestScope.comments}" var="comment">
                                                <c:if test="${comment.book.id eq book.id}">
                                                    <div class="card card-body">
                                                        <p class="text text-info"><fmt:message key="commentAuthor"/> ${comment.user.login}</p>
                                                        <p class="text text-info"><fmt:message key="commentDate"/> ${ctg:localDateFormatter(comment.date)}</p>
                                                        <p class="text">${comment.text}</p>
                                                        <c:if test="${comment.user.id eq sessionScope.user.id}">
                                                            <button class="btn btn-danger" onclick="deleteComment(${comment.id})"><fmt:message key="deleteComment"/></button>
                                                        </c:if>
                                                    </div>
                                                </c:if>
                                            </c:forEach>
                                        </div>
                                        <button class="btn btn-primary" type="button" data-bs-toggle="collapse" data-bs-target="#comment${book.id}" aria-expanded="false" aria-controls="comment${book.id}">
                                            <fmt:message key="comment"/>
                                        </button>
                                        <div class="collapse" id="comment${book.id}">
                                            <div class="card card-body">
                                                <div class="mb-3">
                                                    <form class="addCommentForm" action="controller" method="POST" novalidate>
                                                        <input type="hidden" name="command" value="add_comment">
                                                        <input type="hidden" name="bookId" value="${book.id}">
                                                        <label for="comment" class="form-label"><fmt:message key="comment"/></label>
                                                        <textarea class="form-control" id="comment" name="text" rows="3" required></textarea>
                                                        <div class="valid-feedback"><fmt:message key="validComment"/></div>
                                                        <div class="invalid-feedback"><fmt:message key="invalidComment"/></div>
                                                        <button class="btn btn-primary" type="submit"><fmt:message key="addComment"/></button>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                                                <fmt:message key="close"/></button>
                                        </div>
                                    </div>
                                </div>
                            </div>
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
                        <li class="page-item">
                            <button class="page-link" onclick="findPage(${requestScope.currentPageNumber - 1})">
                                <fmt:message key="previous"/></button>
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
                            <button class="page-link" onclick="findPage(${requestScope.currentPageNumber + 1})">
                                <fmt:message key="next"/></button>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </div>
    </div>
</div>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
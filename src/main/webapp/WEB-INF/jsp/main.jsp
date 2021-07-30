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
        <ctg:date/>
        <div class="collapse navbar-collapse">
            <div class="navbar-nav">
                <c:choose>
                    <c:when test="${not empty sessionScope.user}">
                        <a class="nav-link" href="#"><fmt:message key="hello"/>${sessionScope.user.login}</a>
                        <a class="nav-link" href="#" onclick="logout()"><fmt:message key="logout"/></a>
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
                <a class="nav-link" href="#" onclick="setLocale('en')"><fmt:message key="english"/></a>
                <a class="nav-link" href="#" onclick="setLocale('ru')"><fmt:message key="russian"/></a>
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
        <button class="btn btn-primary" onclick="findFiction()"><fmt:message key="FICTION"/></button>
        <button class="btn btn-primary" onclick="findFantasy()"><fmt:message key="FANTASY"/></button>
        <button class="btn btn-primary" onclick="findScience()"><fmt:message key="SCIENCE"/></button>
        <button class="btn btn-primary" onclick="findAllBooks()"><fmt:message key="all"/></button>
    </div>
    <form id="findBookByNameForm" name="find" method="get" action="controller" novalidate>
        <input type="hidden" name="command" value="find_book_by_name">
        <label for="bookName"><fmt:message key="name"/></label>
        <input type="search" id="bookName" class="form-control" required pattern="[A-Za-z\s]+" name="name">
        <div class="invalid-feedback"><fmt:message key="invalidSearch"/></div>
        <button type="submit" class="btn btn-outline-primary"><fmt:message key="search"/></button>
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
            <c:forEach var="order" items="${requestScope.books}">
                <div class="col-md-2 card offset-md-1" style="width: 20rem; margin-bottom: 5rem">
                    <div class="card-body">
                        <h5 class="card-title text-center text-success">${order.name}</h5>
                        <h6 class="card-subtitle mb-2 text-info"><fmt:message key="author"/> ${order.author.name}</h6>
                        <h6 class="card-subtitle mb-2 text-info"><fmt:message key="genre"/> <fmt:message
                                key="${order.genre}"/></h6>
                        <c:if test="${not empty sessionScope.user and sessionScope.user.role ne UserRole.UNAUTHORIZED}">
                            <c:choose>
                                <c:when test="${order.copiesAmount eq 0}">
                                    <button class="btn btn-outline-primary" disabled>
                                        <fmt:message key="order"/>
                                        <span class="badge bg-danger">${order.copiesAmount}</span>
                                    </button>
                                </c:when>
                                <c:otherwise>
                                    <button class="btn btn-outline-primary" onclick="orderBook(${order.id})">
                                        <fmt:message key="order"/>
                                        <span class="badge bg-success">${order.copiesAmount}</span>
                                    </button>
                                </c:otherwise>
                            </c:choose>
                            <button class="btn btn-outline-primary" onclick="addLike(${order.id})">
                                <fmt:message key="addLike"/>
                                <span class="badge bg-success">${order.likesAmount}</span>
                            </button>
                            <button class="btn btn-outline-success" type="button" data-bs-toggle="modal"
                                    data-bs-target="#modal${order.id}">
                                <fmt:message key="comments"/>
                                <span class="badge bg-success">${order.commentsAmount}</span>
                            </button>
                            <div class="modal fade" id="modal${order.id}" tabindex="-1"
                                 aria-labelledby="exampleModalLabel" aria-hidden="true">
                                <div class="modal-dialog">
                                    <div class="modal-content">
                                        <div class="modal-header">
                                            <h5 class="modal-title" id="exampleModalLabel"><fmt:message
                                                    key="commentsForBook"/> ${order.name}</h5>
                                            <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                    aria-label="Close"></button>
                                        </div>
                                        <div class="modal-body">
                                            <c:forEach items="${requestScope.comments}" var="comment">
                                                <c:if test="${comment.book.id eq order.id}">
                                                    <div class="card card-body">
                                                        <p class="text text-info"><fmt:message
                                                                key="commentAuthor"/> ${comment.user.login}</p>
                                                        <p class="text text-info"><fmt:message
                                                                key="commentDate"/> ${ctg:localDateParser(comment.date, sessionScope.locale)}</p>
                                                        <p class="text">${comment.text}</p>
                                                        <c:if test="${comment.user.id eq sessionScope.user.id}">
                                                            <button class="btn btn-danger"
                                                                    onclick="deleteComment(${comment.id})"><fmt:message
                                                                    key="deleteComment"/></button>
                                                        </c:if>
                                                    </div>
                                                </c:if>
                                            </c:forEach>
                                        </div>
                                        <button class="btn btn-primary" type="button" data-bs-toggle="collapse"
                                                data-bs-target="#comment${order.id}" aria-expanded="false"
                                                aria-controls="comment${order.id}">
                                            <fmt:message key="comment"/>
                                        </button>
                                        <div class="collapse" id="comment${order.id}">
                                            <div class="card card-body">
                                                <div class="mb-3">
                                                    <form id="addCommentForm" action="controller" method="POST"
                                                          novalidate>
                                                        <input type="hidden" name="command" value="add_comment">
                                                        <input type="hidden" name="bookId" value="${order.id}">
                                                        <label for="comment" class="form-label"><fmt:message
                                                                key="comment"/></label>
                                                        <textarea class="form-control" id="comment" name="text" rows="3"
                                                                  required></textarea>
                                                        <div class="valid-feedback"><fmt:message
                                                                key="validComment"/></div>
                                                        <div class="invalid-feedback"><fmt:message
                                                                key="invalidComment"/></div>
                                                        <button class="btn btn-primary" type="submit"><fmt:message
                                                                key="addComment"/></button>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="modal-footer">
                                            <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                                                <fmt:message key="closeComments"/></button>
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
                                <fmt:message
                                        key="previous"/></button>
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
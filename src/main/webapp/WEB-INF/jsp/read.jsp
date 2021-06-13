<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="main"/>
<html>
<head>
    <title><fmt:message key="read.title"/></title>
</head>
<body>
<fmt:message key="read.info"/>${requestScope.book.name}, ${requestScope.book.author.name}, ${requestScope.book.genre.name}
<hr/>
<a href="controller?command=show_user_orders">
<hr/>
${requestScope.book.text}
<fmt:message key="myOrders"/></a>
<hr/>
</body>
</html>

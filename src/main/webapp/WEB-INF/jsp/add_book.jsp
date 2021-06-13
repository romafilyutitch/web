<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="main"/>
<html>
<head>
    <title><fmt:message key="addBook.title"/></title>
</head>
<body>
${requestScope.error}
    <form action="controller?command=add_book" method = "POST">
        <label><fmt:message key="addBook.name"/><input type="text" name="bookName"/></label>
        <label><fmt:message key="addBook.author"/><input type="text" name="authorName"/></label>
        <label><fmt:message key="addBook.genre"/><input type="text" name="genreName"/></label>
        <label><fmt:message key="addBook.date"/><input type="date" name = "date"/></label>
        <label><fmt:message key="addBook.pages"/><input type="number" name="pages"/></label>
        <label><fmt:message key="addBook.description"/><input type="text" name="description"/></label>
        <label><fmt:message key="addBook.text"/><input type="text" name="text"/></label>
        <input type="submit" value="Save new book"/>
    </form>
<a href="controller?command=show_books"><fmt:message key="addBook.return"/></a>
</body>
</html>

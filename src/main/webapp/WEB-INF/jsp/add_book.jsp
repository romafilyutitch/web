<%--
  Created by IntelliJ IDEA.
  User: roma0
  Date: 09.06.2021
  Time: 23:39
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add book page</title>
</head>
<body>
${requestScope.error}
    <form action="controller?command=add_book" method = "POST">
        <label> Book name:
            <input type="text" name="bookName"/>
        </label>
        <label> Author name:
            <input type="text" name="authorName"/>
        </label>
        <label> Genre name:
            <input type="text" name="genreName"/>
        </label>
        <label>
            Date
            <input type="date" name = "date"/>
        </label>
        <label>
            Pages amount
            <input type="number" name="pages"/>
        </label>
        <label>
            Description
            <input type="text" name="description"/>
        </label>
        <label>
            Text
            <input type="text" name="text"/>
        </label>
        <input type="submit" value="Save new book"/>
    </form>
<a href="controller?command=show_books">return to books</a>
</body>
</html>

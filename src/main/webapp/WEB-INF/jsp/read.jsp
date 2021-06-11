<%--
  Created by IntelliJ IDEA.
  User: roma0
  Date: 11.06.2021
  Time: 2:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Read book page</title>
</head>
<body>
    Book info : ${book}
<hr/>
    ${book.text}
<hr/>
<a href="controller?command=return&id=${book.id}">Return book</a>
</body>
</html>

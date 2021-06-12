<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: roma0
  Date: 11.06.2021
  Time: 2:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.time.LocalDate" %>
<html>
<head>
    <title>Read book page</title>
</head>
<body>
Book info : ${book.name}, ${book.author.name}, ${book.genre.name}
<hr/>
${book.text}
<hr/>
<a href="controller?command=show_user_orders">My Orders</a>
</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: roma0
  Date: 09.06.2021
  Time: 13:35
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Not found Page</title>
</head>
<body>
    <h1>Status code : ${pageContext.errorData.statusCode} Not found</h1>
    <h2> Page was not found</h2>
    <a href="controller?command=main">Main page</a>
</body>
</html>

<%--
  Created by IntelliJ IDEA.
  User: roma0
  Date: 08.06.2021
  Time: 1:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Forbidden Page</title>
</head>
<body>
    <h1>Request is forbidden</h1>
    <h2>Status code: ${pageContext.errorData.statusCode} Forbidden</h2>
    <h2>Message: you don't have enough rights to perform chosen request</h2>
<a href="controller?command=main">Main Page</a>
</body>
</html>

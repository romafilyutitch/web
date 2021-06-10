<%--
  Created by IntelliJ IDEA.
  User: roma0
  Date: 09.06.2021
  Time: 13:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Server error</title>
</head>
<body>
    <h1>Oop, something went wrong with the server</h1>
    ${pageContext.errorData.throwable}
    <a href="controller?command=main">Main page</a>
</body>
</html>

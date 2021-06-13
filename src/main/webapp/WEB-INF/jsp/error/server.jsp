<%@ page contentType="text/html;charset=UTF-8" %>
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

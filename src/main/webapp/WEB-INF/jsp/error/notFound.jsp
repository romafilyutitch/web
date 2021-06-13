<%@ page contentType="text/html;charset=UTF-8" %>
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

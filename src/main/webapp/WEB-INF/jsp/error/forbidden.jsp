<%@ page contentType="text/html;charset=UTF-8" %>
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

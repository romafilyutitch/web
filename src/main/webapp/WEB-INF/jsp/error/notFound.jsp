<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="notFound"/>
<html>
<head>
    <title><fmt:message key="title"/></title>
</head>
<body>
    <h1><fmt:message key="status"/> ${pageContext.errorData.statusCode}</h1>
    <h2><fmt:message key="message"/></h2>
    <a href="controller?command=main"><fmt:message key="main"/> </a>
</body>
</html>

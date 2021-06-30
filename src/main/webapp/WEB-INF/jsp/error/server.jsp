<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="server"/>
<html>
<head>
    <title><fmt:message key="title"/></title>
</head>
<body>
    <h1><fmt:message key="message"/></h1>
    ${pageContext.errorData.throwable}
    <a href="controller?command=main"><fmt:message key="main"/></a>
</body>
</html>

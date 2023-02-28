<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String contextPath = request.getScheme() + "://" + request.getServerName() + ":"
+ request.getServerPort() + request.getContextPath() + "/";
%>
<html>
<head>
	<meta charset="UTF-8">
	<base href="<%=contextPath%>">
<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>

</head>
<body>
	<%--首页就一张图片，后续可以自己优化--%>
	<img src="image/home.png" style="position: relative;top: -10px; left: -10px;"/>
</body>
</html>
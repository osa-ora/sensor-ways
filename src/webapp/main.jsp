<%@page import="osa.ora.iot.beans.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
if(request.getSession().getAttribute("USER")==null) {
	response.sendRedirect("/error.jsp");
}
User user = (User) request.getSession().getAttribute("USER");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title> Smart IoT, Home Page</title>
</head>
<body>
	<h1>Welcome to  Smart IoT, <%=user.getUsername() %></h1>
	<br>
	You can now control your Smart IoT devices and check the different sensors data.
	<br><br>
	<table>
	<tr><td><a href='/IoTWebServlet?action=10'>Display All Devices</a></td></tr>
	<tr><td><a href='settings.jsp'>IoT Platform Preferences</a></td></tr>
	</table>
</body>
</html>
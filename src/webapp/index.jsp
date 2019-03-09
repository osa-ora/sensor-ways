<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login to  Smart IoT</title>
</head>
<body>
	<h1>Welcome to  Smart IoT</h1>
	<br>
	Before you can use the Smart IoT application, you need to login first.
	<br><br>
	<form method="post" action="/Login">
	<table>
	<%
		if(session.getAttribute("LOGIN_FAILED")!=null){
			out.println("<tr style='color:red'><td colspan=2>Invalid Username or Password!</td></tr>");
			session.removeAttribute("LOGIN_FAILED");
		}
	%>
	<tr><td>Username:</td><td><input type="text" name="username" size="20" maxlength="20"/></td></tr>
	<tr><td>Password:</td><td><input type="password" name="password" size="20" maxlength="20"/></td></tr>
	<tr><td colspan=2><input value="Login" type="submit"/></td></tr>
	</table>
	</form>
</body>
</html>
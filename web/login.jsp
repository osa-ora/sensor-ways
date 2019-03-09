<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login to SensorWays IoT</title>
        <link rel="stylesheet" href="css/w3.css">
        <link rel='stylesheet' href='css/style.css'>
        <link rel='stylesheet' href='css/style2.css'>
</head>
<body>
        <div class="navbar">
            <a href="index.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/home.png"> Home Page</a>
            <a href="information.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/humidity.png"> Supported Devices</a>
            <a href="contact.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/notification.png"> Contact Us</a>
            <a class="active" href="login.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/members.png"> Members Login</a>
        </div> 
        &nbsp;&nbsp;<h3>&nbsp;Welcome to SensorWays IoT</h3>
	<br>&nbsp;Before you can use the SensorWays IoT application, you need to login first.
	<br>
	<form method="post" action="Login">
	<table>
	<%
            if(session.getAttribute("LOGIN_FAILED")!=null){
                    out.println("<tr style='color:red'><td colspan=2>Invalid Username, Password or Identity Identifier!</td></tr>");
                    session.removeAttribute("LOGIN_FAILED");
            }
	%>
	<tr><td>User Email</td><td><input type="text" name="username" size="40" maxlength="40"/></td></tr>
	<tr><td>Password</td><td><input type="password" name="password" size="40" maxlength="40"/></td></tr>
        <tr><td>Account Identifier</td><td><input type="text" name="identity" size="7" maxlength="7"/></td></tr>
	<tr><td colspan=2><input value="Login" type="submit"/></td></tr>
	</table>
	</form>
    <%@include file="footer.jsp" %>
</body>
</html>
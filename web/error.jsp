<%@page import="osa.ora.iot.beans.IConstant"%>
<%@page import="osa.ora.iot.db.beans.Users"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String userName="Guest";
String notificationCount="0";
if(session.getAttribute("USER")!=null) {
    Users user = (Users) session.getAttribute("USER");
    notificationCount=(String)session.getAttribute("NOTIFICATION_COUNT");
    userName=user.getUsername();
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Login to SensorWays IoT</title>
        <link rel="stylesheet" href="css/w3.css">
        <link rel='stylesheet' href='css/style.css'>
<% if(session.getAttribute("USER")!=null) { %>            
        <script src="js/ajax_check_notifications.js"></script>  
<% } %>
</head>
<body>
        <div class="navbar">
            <a href="index.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/home.png"> Home Page</a>
            <a href="information.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/humidity.png"> Supported Devices</a>
            <a href="contact.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/notification.png"> Contact Us</a>
<% if(session.getAttribute("USER")==null) { %>            
            <a href="login.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/members.png"> Members Login</a>
<% } else { %>
            <a href="main.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/menu.png"> Main Menu</a>
            <a href="NotificationServlet?action=<%=IConstant.ACTION_WEB_LIST_NOTIFICATIONS%>"><i style="float:right;"></i><img src="images/alert.png" style="width: 30px; height: 25px;"> Alerts <span id="total">(<%=notificationCount%>)</span></a>
            <a href="LogoutServlet"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/members.png"> Logout</a>
<% } %>
         </div> 
	<h1>&nbsp;Welcome to SensorWays IoT, <%=userName%></h1>
	<br>&nbsp;You have encountered unexpected error due to missing privileges or session timeout.
        <% if(session.getAttribute("ERROR_MSG")!=null) { %>            
        <br><br>&nbsp;&nbsp;Error Message: <font color="red"><%=session.getAttribute("ERROR_MSG") %></font>
        <%  session.removeAttribute("ERROR_MSG");
           } %>
        <br><br>
        &nbsp;Check with the system administrator or contact our support team.
	<br><br>
<% if(session.getAttribute("USER")==null) { %>            
            <tr><td colspan=2>Login from <a href="login.jsp">Here</a></td></tr>
<% } else { %>
            <tr><td colspan=2>Go To Main Menu from <a href="main.jsp">Here</a></td></tr>
<% } %>            

    <%@include file="footer.jsp" %>
</body>
</html>
<%@page import="osa.ora.iot.db.beans.TenantSettings"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.TimeZone"%>
<%@page import="osa.ora.iot.db.beans.DeviceModel"%>
<%@page import="java.util.List"%>
<%@page import="osa.ora.iot.beans.IConstant"%>
<%@page import="osa.ora.iot.db.beans.Users"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
if(session.getAttribute("USER")==null) {
	response.sendRedirect("error.jsp");
        return;
}
Users user = (Users) session.getAttribute("USER");
if(user.getUserRole()!=IConstant.ROLE_READ_WRITE || user.getSystemAdmin()==0) { 
    response.sendRedirect("error.jsp");
    return;
}
String notificationCount=(String)session.getAttribute("NOTIFICATION_COUNT");
TenantSettings setting= (TenantSettings) session.getAttribute("USER_SETTINGS");
DateFormat formatter = new SimpleDateFormat("EEE dd MMM HH:mm");
formatter.setTimeZone(TimeZone.getTimeZone(setting.getTimezone()));
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SensorWays IoT, Add New Account</title>
        <link rel="stylesheet" href="css/w3.css">
        <link rel='stylesheet' href='css/style.css'>
        <link rel='stylesheet' href='css/style2.css'>
        <script src="js/ajax_check_notifications.js"></script>  
</head>
<body>
        <div class="navbar">
            <a href="index.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/home.png"> Home Page</a>
            <a href="information.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/humidity.png"> Supported Devices</a>
            <a href="contact.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/notification.png"> Contact Us</a>
            <a href="main.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/menu.png"> Main Menu</a>
            <a href="NotificationServlet?action=<%=IConstant.ACTION_WEB_LIST_NOTIFICATIONS%>"><i style="float:right;"></i><img src="images/alert.png" style="width: 30px; height: 25px;"> Alerts <span id="total">(<%=notificationCount%>)</span></a>
            <a href="LogoutServlet"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/members.png"> Logout</a>
        </div> 
	&nbsp;&nbsp;<%=formatter.format(new Date())%><h3>&nbsp;Welcome <%=user.getUsername()%> [<i><font color="red"><%=setting.getIdentityName()%></font></i>]</h3>
        &nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > <a href='ManageAccountsServlet?action=28'>Manage Accounts</a> > Register New Account<br>
        &nbsp;You can register one or more account for the same customer.	
        <br><br>
	<form method="post" action="ManageAccountsServlet">
        <input type="hidden" name="action" value="29"/>
	<table>
        <tr><td>Account Name</td><td><input style="width: 300px;" type="text" name="name" size="30" maxlength="30" value=""/></td></tr>
        <tr><td>Admin Username</td><td><input style="width: 300px;" type="text" name="username" size="30" maxlength="30" value=""/></td></tr>
	<tr><td>Admin Email Address</td><td>
                <input style="width: 300px;" type="text" name="emailAddress" id="emailAddress" size="40" maxlength="40" value=""/>
        </td></tr>
	<tr><td>Max Users</td><td>
                <select style="width: 300px;" name="max_users">
                    <option value='5' selected="true">5</option>
                    <option value='10'>10</option>
                    <option value='15'>15</option>
		</select>
	</td></tr>
	<tr><td>Max Devices</td><td>
                <select style="width: 300px;" name="max_devices">
                    <option value='10' selected="true">10</option>
                    <option value='20'>20</option>
                    <option value='30'>30</option>
                    <option value='50'>50</option>
		</select>
	</td></tr>
	<tr><td>Max SMS per Month</td><td>
                <select style="width: 300px;" name="max_sms">
                    <option value='0' selected="true">0</option>
                    <option value='100'>100</option>
                    <option value='500'>500</option>
                    <option value='1000'>1000</option>
                    <option value='2000'>2000</option>
		</select>
	</td></tr>
	<tr><td>Initial Status</td><td>
                <select style="width: 300px;" name="status">
                    <option value='1'>Active</option>
                    <option value='2'>Inactive</option>
		</select>
	</td></tr>        
        <tr>
            <td>Enable Workflow Access</td><td><input style="width: 300px;" type="checkbox" name="workflow" value="1"/></td>
        </tr>
        <tr>
            <td>Enable Scheduler Access</td><td><input style="width: 300px;" type="checkbox" name="scheduler" value="1"/></td>
        </tr>
        <tr>
            <td>Enable User Management Access</td><td><input style="width: 300px;" type="checkbox" name="users" value="1"/></td>
        </tr>
        <tr>
            <td>Enable Dashboard Access</td><td><input style="width: 300px;" type="checkbox" name="dashboard" value="1"/></td>
        </tr>
        <tr>
            <td>Enable Device Simulation Access</td><td><input style="width: 300px;" type="checkbox" name="simulation" value="1"/></td>
        </tr>
        <tr><td colspan=2><input name="submitButton" id="submitButton" value="Register Account" type="submit"/></td></tr>
	</table>
	<br><a class="w3-btn w3-ripple w3-red" href='ManageAccountsServlet?action=28'>Back to Manage Accounts Page</a><br><br>
	</form>
    <%@include file="footer.jsp" %>
</body>
</html>
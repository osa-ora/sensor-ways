<%@page import="osa.ora.iot.db.beans.TenantSettings"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.TimeZone"%>
<%@page import="java.util.ArrayList"%>
<%@page import="osa.ora.iot.db.beans.Devices"%>
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
    if(user.getUserRole()!=IConstant.ROLE_READ_WRITE || user.getWorkflowCreation()==0) { 
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
<title>SensorWays IoT, Generate Reports</title>
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
        &nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > Report Extraction<br>
        &nbsp;You can build and extract different (.csv) file reports to import them into other systems.
	<br>
	<form method="post" action="IoTWebServlet">
        <input type="hidden" name="action" value="<%=IConstant.ACTION_WEB_EXECUTE_REPORT%>"/>
	<table>
        <tr><td>Report Type</td>
            <td><select name="source" style="width: 200pt;">
                <option value="0" selected>All Devices</option>
                <option value="1">Active Devices only</option>
                <option value="2">Inactive Devices only</option>
                <option value="3">Devices Pending Login only</option>
            <%
            List<Devices> deviceList = null;
            if(session.getAttribute("DEVICE_LIST")!=null){
                    deviceList = (List<Devices>)session.getAttribute("DEVICE_LIST");
            }else{
                    deviceList = new ArrayList<Devices>(); 
            }
            for (int i = 0; i < deviceList.size(); i++) {
                %> <option value="<%=deviceList.get(i).getDeviceId() %>"><%=deviceList.get(i).getFriendlyName()%></option>
            <% } %>
            </select>     
            </td></tr>
        <tr><td>Report Scope</td><td>
                <select name="event" style="width: 200pt;">
                  <option value="1">Devices Basic Information Only</option>
		  <option value="2">All Devices' Messages</option>
		  <option value="3">High Alert Messages</option>
                  <option value="4">Low Alert Messages</option>
                  <option value="5">Login Messages</option>
                  <option value="6">Upgrade Messages</option>
                  <option value="7">Error Messages</option>
		</select>
	</td></tr>
        <tr><td>Report Format</td><td>
                <select name="format" style="width: 200pt;">
                  <option value="1">(.csv) File Format with headers</option>
                  <option value="2">(.csv) File Format without headers</option>
		</select>
	</td></tr>
        <tr><td>Report Duration</td><td>
                <select name="duration" style="width: 200pt;">
                    <option value="1">Last 1 day</option>
                    <option value="2">Last 1 week</option>
                    <option value="3">Last 1 month</option>
                    <option value="4">No duration</option>
		</select>
                <br>Device Last Update Time or Message Time.
	</td></tr>
        <tr><td>Max Rows</td><td>
                <select name="rows" style="width: 200pt;">
                    <option value="1">2000 rows</option>
		</select>
	</td></tr>        
	<tr><td colspan=2><input value="Create Report" type="submit"/></td></tr>
	</table>
	<br><a class="w3-btn w3-ripple w3-red" href='main.jsp'>Back to Main Menu</a><br><br>
	</form>
    <%@include file="footer.jsp" %>
</body>
</html>
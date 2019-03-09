<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.TimeZone"%>
<%@page import="osa.ora.iot.db.beans.TenantSettings"%>
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
if(user.getUserRole()!=IConstant.ROLE_READ_WRITE) { 
    response.sendRedirect("error.jsp");
    return;
}
TenantSettings setting= (TenantSettings) session.getAttribute("USER_SETTINGS");
String notificationCount=(String)session.getAttribute("NOTIFICATION_COUNT");
DateFormat formatter = new SimpleDateFormat("EEE dd MMM HH:mm");
formatter.setTimeZone(TimeZone.getTimeZone(setting.getTimezone()));

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SensorWays IoT, Settings Page</title>
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
        &nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > Account Preferences<br>
        &nbsp;These settings will apply to all your users and devices.
	<br>
	<form method="post" action="SettingServlet">
	<table>
	<tr><td>Max Displayed Message Per Device</td><td>
            <select name="messages"/>
              <option value="50">50</option>
              <option value="100">100</option>
              <option value="150" selected="true">150</option>
              <option value="200">200</option>
              <option value="250">250</option>
            </select>
        </td></tr>
	<tr><td>HeartBeat Interval (in seconds)</td><td>
            <select name="heartbeats" value="<%=setting.getPingInterval()%>"/>
              <option value="15">15 seconds</option>
            </select>
            </td></tr>
	<tr><td>Update Message Interval (per heartbeats)</td><td>
            <select name="intervals"/>
            <% if(setting.getUpdateInterval()==20){%>
                <option value="20" selected="true">20 heartbeats</option>
                <option value="50">50 heartbeats</option>
            <% }else {%>
                <option value="20">20 heartbeats</option>
                <option value="50"  selected="true">50 heartbeats</option>
            <% } %>
            </select>
        </td></tr>
	<tr><td>Email Notification for <b>Device Message Payload</b> Alerts (*)</td><td>
	<% if(setting.getAlertEmailMessage()==IConstant.EMAIL_ENABLED){%>
		<input type="radio" name="alertMesg" value="1" checked="checked"> Enabled
		<input type="radio" name="alertMesg" value="0"> Disabled
	<% }else{ %>
		<input type="radio" name="alertMesg" value="1"> Enabled
		<input type="radio" name="alertMesg" value="0" checked="checked"> Disabled
	<% } %>
	</td></tr>
	<tr><td>Email Notification for <b>Fault Device Message</b> (**)</td><td>
	<% if(setting.getAlertEmailMessage()==IConstant.EMAIL_ENABLED){%>
		<input type="radio" name="faultMesg" value="1" checked="checked"> Enabled
		<input type="radio" name="faultMesg" value="0"> Disabled
	<% }else{ %>
		<input type="radio" name="faultMesg" value="1"> Enabled
		<input type="radio" name="faultMesg" value="0" checked="checked"> Disabled
	<% } %>
	</td></tr>
        <tr><td>Email Notification for <b>New User Registration</b> (User Welcome Email)</td><td>
		<input type="radio" name="newUsers" value="1" checked="checked"> Always Enabled :)
	</td></tr>
        <tr><td>Email Notification for <b>Device Offline Status</b> Alerts (***)</td><td>
	<% if(setting.getAlertEmailOffline()==IConstant.EMAIL_ENABLED){%>
		<input type="radio" name="alertOffline" value="1" checked="checked"> Enabled
		<input type="radio" name="alertOffline" value="0"> Disabled
	<% }else{ %>
		<input type="radio" name="alertOffline" value="1"> Enabled
		<input type="radio" name="alertOffline" value="0" checked="checked"> Disabled
	<% } %>
	</td></tr>
	<tr><td>Email Notification for <b>Device Online Status</b> Alerts (****)</td><td>
	<% if(setting.getAlertEmailOnline()==IConstant.EMAIL_ENABLED){%>
		<input type="radio" name="alertOnline" value="1" checked="checked"> Enabled
		<input type="radio" name="alertOnline" value="0"> Disabled
	<% }else{ %>
		<input type="radio" name="alertOnline" value="1"> Enabled
		<input type="radio" name="alertOnline" value="0" checked="checked"> Disabled
	<% } %>
	</td></tr>
	<tr><td>Email Notification for <b>New Device Registration</b> Alerts</td><td>
	<% if(setting.getDeviceRegistrationEmail()==IConstant.EMAIL_ENABLED){%>
		<input type="radio" name="alertDeviceReg" value="1" checked="checked"> Enabled
		<input type="radio" name="alertDeviceReg" value="0"> Disabled
	<% }else{ %>
		<input type="radio" name="alertDeviceReg" value="1"> Enabled
		<input type="radio" name="alertDeviceReg" value="0" checked="checked"> Disabled
	<% } %>
	</td></tr>
	<tr><td>Email Notification for <b>Device Info Update</b> Alerts</td><td>
	<% if(setting.getDeviceUpdateEmail()==IConstant.EMAIL_ENABLED){%>
		<input type="radio" name="alertDeviceUpd" value="1" checked="checked"> Enabled
		<input type="radio" name="alertDeviceUpd" value="0"> Disabled
	<% }else{ %>
		<input type="radio" name="alertDeviceUpd" value="1"> Enabled
		<input type="radio" name="alertDeviceUpd" value="0" checked="checked"> Disabled
	<% } %>
	</td></tr>
	<tr><td>Email Notification for <b>Device IP Address Change</b> Alerts</td><td>
	<% if(setting.getAlertDeviceIpChange()==IConstant.EMAIL_ENABLED){%>
		<input type="radio" name="alertDeviceIp" value="1" checked="checked"> Enabled
		<input type="radio" name="alertDeviceIp" value="0"> Disabled
	<% }else{ %>
		<input type="radio" name="alertDeviceIp" value="1"> Enabled
		<input type="radio" name="alertDeviceIp" value="0" checked="checked"> Disabled
	<% } %>
	</td></tr>
	<tr><td>Email Notification for <b>Device Firmware Update Available</b> Alerts</td><td>
	<% if(setting.getAlertFirmwareAvailable()==IConstant.EMAIL_ENABLED){%>
		<input type="radio" name="alertDeviceFirmware" value="1" checked="checked"> Enabled
		<input type="radio" name="alertDeviceFirmware" value="0"> Disabled
	<% }else{ %>
		<input type="radio" name="alertDeviceFirmware" value="1"> Enabled
		<input type="radio" name="alertDeviceFirmware" value="0" checked="checked"> Disabled
	<% } %>
	</td></tr>
	<tr><td>Alert Email Language</td><td>
	<% if(setting.getDeviceUpdateEmail()==IConstant.ENGLISH){%>
		<input type="radio" name="language" value="2" checked="checked"> English
		<input type="radio" name="language" value="1"> Arabic
	<% }else{ %>
		<input type="radio" name="language" value="2"> English
		<input type="radio" name="language" value="1" checked="checked"> Arabic
	<% } %>
	</td></tr>
	<tr><td>Alert Grace Period (Stop Same Alert For a Duration (for *,**, *** and ****))</td><td>
            <select name="grace"/>
            <% if(setting.getAlertGracePeriod()==5){%>
                <option value="5" selected="true">5 minutes</option>
                <option value="10">10 minutes</option>
            <% }else {%>
                <option value="5">5 minutes</option>
                <option value="10"  selected="true">10 minutes</option>
            <% } %>
            </select>
        </td></tr>        
	<tr><td>Display Timezone</td><td>
		<select name="timezone">
                  <option value="Asia/Qatar" selected="true">Asia/Qatar</option>
		</select>
	</td></tr>
	<tr><td>Automatic Purge of Device Messages & Notification Alerts After</td><td>
		<select name="purge">
                  <option value="<%=setting.getPurgeAfter()%>" selected="true"><%=setting.getPurgeAfter()%> Days</option>
		</select>
	</td></tr>
        <tr><td>Max Number of Users in this Account</td><td>
                <input type="text" disabled="true" value="<%=setting.getMaxUsers()%>"></option>
	</td></tr>
        <tr><td>Max Number of IoT Devices in this Account</td><td>
                <input type="text" disabled="true" value="<%=setting.getMaxDevices()%>"></option>
	</td></tr>
        <tr><td>Admin User Email</td><td>
                <input type="text" disabled="true" value="<%=setting.getAdminUserEmail()%>"></option>
	</td></tr>        
        <tr><td colspan=2><input value="Save Settings" type="submit"/></td></tr>
	</table>
	<br><a class="w3-btn w3-ripple w3-red" href='main.jsp'>Back to Main Menu</a>
	</form>
    <%@include file="footer.jsp" %>
</body>
</html>
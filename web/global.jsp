<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.TimeZone"%>
<%@page import="osa.ora.iot.db.beans.SystemConfig"%>
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
if(user.getSystemAdmin()!=1){
    request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
    response.sendRedirect("error.jsp");
    return;                    
}
SystemConfig globalSettings= (SystemConfig) session.getAttribute("SYS_CONFIG");
String notificationCount=(String)session.getAttribute("NOTIFICATION_COUNT");
TenantSettings setting= (TenantSettings) session.getAttribute("USER_SETTINGS");
DateFormat formatter = new SimpleDateFormat("EEE dd MMM HH:mm");
formatter.setTimeZone(TimeZone.getTimeZone(setting.getTimezone()));
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SensorWays IoT, System Configuration Page</title>
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
        &nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > Global Platform Settings<br>
	&nbsp;These global settings control the whole system behavior so be careful when updating them.
	<br>
	<form method="post" action="IoTWebServlet">
        <input type="hidden" name="action" value="<%=IConstant.ACTION_WEB_SAVE_SYS_CONFIG%>"/>
	<table>
	<tr><td>Platform Host For Devices</td><td>
            <input type="text" name="host" size="50" maxlength="50" value="<%=globalSettings.getPlatformHost()%>"/>
        </td></tr>
	<tr><td>Platform HTTP Port For Devices</td><td>
            <input type="text" name="http" size="50" maxlength="50" value="<%=globalSettings.getPlatformHttpPort()%>"/>
        </td></tr>
	<tr><td>Platform HTTPS Port For Devices</td><td>
            <input type="text" name="https" size="50" maxlength="50" value="<%=globalSettings.getPlatformHttpsPort()%>"/>
        </td></tr>
	<tr><td>Email Enabled</td><td>
	<% if(globalSettings.getEmailEnabled()==IConstant.EMAIL_ENABLED){%>
		<input type="radio" name="email_enabled" value="1" checked="checked"> Enabled
		<input type="radio" name="email_enabled" value="0"> Disabled
	<% }else{ %>
		<input type="radio" name="email_enabled" value="1"> Enabled
		<input type="radio" name="email_enabled" value="0" checked="checked"> Disabled
	<% } %>
	</td></tr>
	<tr><td>Default Email Language</td><td>
	<% if(globalSettings.getDefaultEmailLanguage()==IConstant.ENGLISH){%>
		<input type="radio" name="language" value="2" checked="checked"> English
		<input type="radio" name="language" value="1"> Arabic
	<% }else{ %>
		<input type="radio" name="language" value="2"> English
		<input type="radio" name="language" value="1" checked="checked"> Arabic
	<% } %>
	</td></tr>
        <tr><td>Email Username</td><td>
            <input type="text" name="email" size="50" maxlength="50" value="<%=globalSettings.getEmailUser()%>"/>
        </td></tr>
	<tr><td>Email Password</td><td>
            <input type="password" name="password" size="50" maxlength="50" value="<%=globalSettings.getEmailPassword()%>"/>
        </td></tr>
	<tr><td>Email Server</td><td>
            <input type="text" name="server" size="50" maxlength="50" value="<%=globalSettings.getSmtpServerIp()%>"/>
        </td></tr>
        <tr><td>Use SSL with Mail Server</td><td>
	<% if(globalSettings.getUseSSL()==1){%>
		<input type="radio" name="ssl" value="1" checked="checked"> Yes
		<input type="radio" name="ssl" value="2"> No
	<% }else{ %>
		<input type="radio" name="ssl" value="1"> Yes
		<input type="radio" name="ssl" value="2" checked="checked"> No
	<% } %>
	</td></tr>
        <tr><td>Email Port</td><td>
            <input type="text" name="port" size="50" maxlength="50" value="<%=globalSettings.getSmtpPort()%>"/>
        </td></tr>
	<tr><td>REST APIs Version</td><td>
            <input type="number" name="rest" size="50" maxlength="50" value="<%=globalSettings.getApiVersion()%>"/>
        </td></tr>
	<tr><td>Android Version</td><td>
            <input type="number" name="android" size="50" maxlength="50" value="<%=globalSettings.getAndroidVersion()%>"/>
        </td></tr>
	<tr><td>Android Version</td><td>
            <input type="number" name="iOs" size="50" maxlength="50" value="<%=globalSettings.getIosVersion()%>"/>
        </td></tr>
	<tr><td>Default Timezone</td><td>
		<select name="timezone">
                  <option value="Asia/Qatar" selected="true">Asia/Qatar</option>
		</select>
	</td></tr>        
        <tr><td colspan=2><input value="Save Settings" type="submit"/></td></tr>
	</table>
	<br><a class="w3-btn w3-ripple w3-red" href='main.jsp'>Back to Main Menu</a>
	</form>
    <%@include file="footer.jsp" %>
</body>
</html>
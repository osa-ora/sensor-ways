<%@page import="osa.ora.iot.beans.Settings"%>
<%@page import="osa.ora.iot.util.UserUtil"%>
<%@page import="osa.ora.iot.beans.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
if(request.getSession().getAttribute("USER")==null) {
	response.sendRedirect("/error.jsp");
}
User user = (User) request.getSession().getAttribute("USER");
Settings settings=UserUtil.identitySettings.get(user.getIdentity());
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title> Smart IoT, Settings Page</title>
</head>
<body>
	<h1>Welcome to  Smart IoT, <%=user.getUsername() %></h1>
	<br>
	These settings will apply to all your users and devices.
	<br><br>
	<form method="post" action="/SettingServlet">
	<table>
	<tr><td>Max Retained Per Device</td><td><input type="text" name="messages" size="5" maxlength="5" value="<%=settings.getMaxRetainedMessages()%>"/> message(s)</td></tr>
	<tr><td>HeartBeat Interval</td><td><input type="text" name="heartbeats" size="3" maxlength="3" value="<%=settings.getHeartbeatInterval()%>"/> seconds</td></tr>
	<tr><td>Update Message Interval</td><td><input type="text" name="intervals" size="3" maxlength="3" value="<%=settings.getUpdatesInterval()%>"/> heartbeat(s)</td></tr>
	<tr><td>Alert Email Notification</td><td>
	<% if(settings.isAlertEmailNotification()){%>
		<input type="radio" name="notification" value="true" checked="checked"> Enabled
		<input type="radio" name="notification" value="false"> Disabled
	<% }else{ %>
		<input type="radio" name="notification" value="true"> Enabled
		<input type="radio" name="notification" value="false" checked="checked"> Disabled
	<% } %>
	</td></tr>
	<tr><td>Display Timezone</td><td>
		<select name="timezone" value="<%=settings.getTimezone()%>">
		  <option value="GMT">GMT</option>
		  <option value="Africa/Cairo">Africa/Cairo</option>
		  <option value="Asia/Qatar">Asia/Qatar</option>
		  <option value="Asia/Dubai">Asia/Dubai</option>
		</select>
	</td></tr>
	<tr><td>Restart all devices for the new settings</td><td>
		<input type="radio" name="restart" value="true"> YES
		<input type="radio" name="restart" value="false" checked="checked"> No
	</td></tr>
	<tr><td colspan=2><input value="Save Settings" type="submit"/></td></tr>
	</table>
	<br><a href='main.jsp'>Back to Main Menu</a>
	</form>
</body>
</html>
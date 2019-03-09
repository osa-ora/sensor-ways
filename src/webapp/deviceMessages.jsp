<%@page import="osa.ora.iot.util.UserUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.TimeZone"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="osa.ora.iot.beans.IConstant"%>
<%@page import="java.util.Date"%>
<%@page import="osa.ora.iot.beans.DeviceMessage"%>
<%@page import="osa.ora.iot.util.IoTUtil"%>
<%@page import="osa.ora.iot.beans.User"%>
<%@page import="osa.ora.iot.beans.Device"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
if(request.getSession().getAttribute("USER")==null) {
	response.sendRedirect("/error.jsp");
}
User user = (User) request.getSession().getAttribute("USER");
IoTUtil ioTUtil = IoTUtil.getInstance();
DateFormat formatter = new SimpleDateFormat("EEE dd MMM yyyy HH:mm:ss zzz");
formatter.setTimeZone(TimeZone.getTimeZone("Asia/Qatar"));
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title> Smart IoT, Device Message List Page</title>
<style>
	table {width: 100%;}
	th, td {text-align: left;padding: 8px;}
	tr:nth-child(even){background-color: #f2f2f2}
	th {background-color: #4CAF50;color: white;}
</style>
</head>
<body>
	<h1>Welcome to  Smart IoT, <%=user.getUsername() %></h1>
	<br>
	<h1>Device Details</h1><br>
	<table>
	<tr><th>Device Id</th><th>Location</th><th>Status</th><th>Last Action</th>
	<th>Last Message</th><th>Online?</th><th>Last Ping Time</th><th>Action</th></tr>
	<%
	Device deviceDetails = null;
	if(session.getAttribute("DEVICE_DETAILS")!=null){
		deviceDetails = (Device)session.getAttribute("DEVICE_DETAILS");
	}
			String deviceId = deviceDetails.getDeviceId();
			%><tr><td><%=deviceId%>
			</td><td><%=deviceDetails.getLocation()%>
			</td><td><%=ioTUtil.getDisplayDeviceStatus(deviceDetails.getStatus())%>
			</td><td><%=ioTUtil.getDisplayAction(deviceDetails.getLastAction(),deviceDetails.getDevice1Name(),deviceDetails.getDevice2Name())%>
			</td>
			<%
			if (!deviceDetails.getMessages().isEmpty()) {
				DeviceMessage message = deviceDetails.getMessages()
						.get(deviceDetails.getMessages().size() - 1);
				%><td><%= ioTUtil.payloadExtractor(message.getPayload(),deviceDetails.getDevice1Name(),deviceDetails.getDevice2Name())%>
				 <br>On <% if(message.getMesageTime()!=null) {%><%=formatter.format(message.getMesageTime())%><% } else { %>N/A<%} %></td>
				<% if (new Date().getTime() - deviceDetails.getLastPingTime().getTime() 
						< (UserUtil.identitySettings.get(deviceDetails.getIdentity()).getHeartbeatInterval()*1000)+2000) { 
					%><td>Online</td>
				<%} else { %>
					<td>Disconnected!</td>
				<%} %>
			<%
			} else {
				%><td>N/A</td><td>N/A</td><%
			}%>
			<td><% 
				if(deviceDetails.getLastPingTime()!=null) { 
					long timeSeconds=(new Date().getTime()-deviceDetails.getLastPingTime().getTime())/1000;
					out.print(timeSeconds+" seconds"); 
					} else { %>N/A<%} %>
			</td><%
			String actionClick = "/IoTWebServlet?deviceId=" + deviceId + "&action=";
			if (user.getRole() == IConstant.ROLE_READ_ONLY) {
				%><td><a href="<%=actionClick + IConstant.ACTION_WEB_LIST_DEVICE_MESSAGES %>">Show Device Messages</a><%
			} else {
				%><td><a href="<%=actionClick + IConstant.ACTION_ON_DEVICE1%>">ON <%=deviceDetails.getDevice1Name()%></a><br>
					  <a href="<%=actionClick + IConstant.ACTION_OFF_DEVICE1%>">OFF <%=deviceDetails.getDevice1Name()%></a><br>
					  <a href="<%=actionClick + IConstant.ACTION_RESTART%>">RESTART Device</a><br> 
					  <a href="<%=actionClick + IConstant.ACTION_WEB_LIST_DEVICE_MESSAGES%>">Show Device Messages</a><br><%
				if (deviceDetails.getNumberOfDevices() == 2) {
					%><a href="<%=actionClick + IConstant.ACTION_ON_DEVICE2%>">ON <%=deviceDetails.getDevice2Name()%></a><br>
					  <a href="<%=actionClick + IConstant.ACTION_OFF_DEVICE2%>">OFF <%=deviceDetails.getDevice2Name()%></a><%
				}
			}%>
			</td></tr><%
		%></table>
	<a href='/IoTWebServlet?action=10'>Back to Device List Page</a>
	<h1>Messages Details</h1><br><h3>Total messages: <%=deviceDetails.getMessages().size()%></h3>
	<table>
		<%if (!deviceDetails.getMessages().isEmpty()) {%>
			<tr><th>Message Payload</th><th>Received Time</th><th>Message Type</th></tr>
			<%for (int n = deviceDetails.getMessages().size() - 1; n >= 0; n--) {
				DeviceMessage message = deviceDetails.getMessages().get(n);
				if (message.getMessageType() == 2){
					%><tr style='color:red'><%
				} else{
					%><tr><%} %>
				<td><%=ioTUtil.payloadExtractor(message.getPayload(),deviceDetails.getDevice1Name(),deviceDetails.getDevice2Name())%>
				</td><td><%=formatter.format(message.getMesageTime())%>
				</td><td><%=ioTUtil.getDisplayMessageType(message.getMessageType())%>
				</td></tr><%
			}
		} else {%>
			<tr><td>N/A</td></tr><%
		}%>
	</table>
	<br><a href='/IoTWebServlet?action=10'>Back to Device List Page</a><br><br>
</body>
</html>
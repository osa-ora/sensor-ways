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
<title> Smart IoT, Device List Page</title>
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
	<h1>List of Devices</h1><br>
	<table>
	<tr><th>Device Id</th><th>Location</th><th>Status</th><th>Last Action</th>
	<th>Last Message</th><th>Online?</th><th>Last Ping Time</th><th>Action</th></tr>
	<%
	List<Device> deviceList = null;
	if(session.getAttribute("DEVICE_LIST")!=null){
		deviceList = (List<Device>)session.getAttribute("DEVICE_LIST");
	}else{
		deviceList = new ArrayList<Device>(); 
	}
	for (int i = 0; i < deviceList.size(); i++) {
			String deviceId = deviceList.get(i).getDeviceId();
			%><tr><td><%=deviceId%>
			</td><td><%=deviceList.get(i).getLocation()%>
			</td><td><%=ioTUtil.getDisplayDeviceStatus(deviceList.get(i).getStatus())%>
			</td><td><%=ioTUtil.getDisplayAction(deviceList.get(i).getLastAction(),deviceList.get(i).getDevice1Name(),deviceList.get(i).getDevice2Name())%>
			</td>
			<%
			if (!deviceList.get(i).getMessages().isEmpty()) {
				DeviceMessage message = deviceList.get(i).getMessages()
						.get(deviceList.get(i).getMessages().size() - 1);
				%><td><%= ioTUtil.payloadExtractor(message.getPayload(),deviceList.get(i).getDevice1Name(),deviceList.get(i).getDevice2Name())%>
				 <br>On <% if(message.getMesageTime()!=null) {%><%=formatter.format(message.getMesageTime())%><% } else { %>N/A<%} %></td>
				<% if (new Date().getTime() - deviceList.get(i).getLastPingTime().getTime() 
						< (UserUtil.identitySettings.get(deviceList.get(i).getIdentity()).getHeartbeatInterval()*1000)+2000) { 
					
					%><td>Online</td>
				<%} else { %>
					<td>Disconnected!</td>
				<%} %>
			<%
			} else {
				%><td>N/A</td><td>N/A</td><%
			}%>
			<td><% 
				if(deviceList.get(i).getLastPingTime()!=null) { 
					long timeSeconds=(new Date().getTime()-deviceList.get(i).getLastPingTime().getTime())/1000;
					out.print(timeSeconds+" seconds"); 
					} else { %>N/A<%} %>
			</td><%
			String actionClick = "/IoTWebServlet?deviceId=" + deviceId + "&action=";
			if (user.getRole() == IConstant.ROLE_READ_ONLY) {
				%><td><a href="<%=actionClick + IConstant.ACTION_WEB_LIST_DEVICE_MESSAGES %>">Show Device Messages</a><br><%
			} else {
				%><td><a href="<%=actionClick + IConstant.ACTION_ON_DEVICE1%>">ON <%=deviceList.get(i).getDevice1Name()%></a><br>
					  <a href="<%=actionClick + IConstant.ACTION_OFF_DEVICE1%>">OFF <%=deviceList.get(i).getDevice1Name()%></a><br>
					  <a href="<%=actionClick + IConstant.ACTION_RESTART%>">RESTART Device</a><br> 
					  <a href="<%=actionClick + IConstant.ACTION_WEB_LIST_DEVICE_MESSAGES%>">Show Device Messages</a><br><%
				if (deviceList.get(i).getNumberOfDevices() == 2) {
					%><a href="<%=actionClick + IConstant.ACTION_ON_DEVICE2%>">ON <%=deviceList.get(i).getDevice2Name()%></a><br>
					  <a href="<%=actionClick + IConstant.ACTION_OFF_DEVICE2%>">OFF <%=deviceList.get(i).getDevice2Name()%></a><%
				}
			}%>
			</td></tr><%
		}
		%></table>
		<br><a href='main.jsp'>Back to Main Menu</a><br><br>
</body>
</html>
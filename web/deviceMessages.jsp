<%@page import="osa.ora.iot.db.beans.TenantSettings"%>
<%@page import="javax.ejb.EJB"%>
<%@page import="osa.ora.iot.db.session.UserSessionBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.TimeZone"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="osa.ora.iot.beans.IConstant"%>
<%@page import="java.util.Date"%>
<%@page import="osa.ora.iot.db.beans.Messages"%>
<%@page import="osa.ora.iot.util.IoTUtil"%>
<%@page import="osa.ora.iot.db.beans.Users"%>
<%@page import="osa.ora.iot.db.beans.Devices"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
if(session.getAttribute("USER")==null) {
	response.sendRedirect("error.jsp");
        return;
}
Users user = (Users) session.getAttribute("USER");
String notificationCount=(String)session.getAttribute("NOTIFICATION_COUNT");
TenantSettings setting= (TenantSettings) session.getAttribute("USER_SETTINGS");
DateFormat formatter = new SimpleDateFormat("EEE dd MMM HH:mm:ss");
formatter.setTimeZone(TimeZone.getTimeZone(setting.getTimezone()));
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SensorWays IoT, Device Message List Page</title>
        <link rel="stylesheet" href="css/w3.css">
        <link rel='stylesheet' href='css/style.css'>
        <link rel='stylesheet' href='css/style2.css'>        
        <link rel='stylesheet' href='css/style3.css'>        
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
        &nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > <a href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_LIST_DEVICES%>'>Device List</a> > Device Details
	<table>
	<tr><th>Device Name</th><th>Location</th><th>Status</th><th>Last Action</th><th>Last Action Source</th>
	<th>Online?</th><th>Last Ping Since</th><th>Graphs</th><th>Action</th></tr>
	<%
	Devices deviceDetails = null;
        List<Messages> messages = null;
	if(session.getAttribute("DEVICE_DETAILS")!=null){
		deviceDetails = (Devices)session.getAttribute("DEVICE_DETAILS");
	}
        if(session.getAttribute("MESSAGES_LIST")!=null){
            messages = (List<Messages>)session.getAttribute("MESSAGES_LIST");
        }
			String deviceId = deviceDetails.getDeviceId();
			%><tr><td><%=deviceDetails.getFriendlyName()%>
			</td><td><%=deviceDetails.getLocation()%>
			</td><td><%=IoTUtil.getDisplayDeviceStatus(deviceDetails.getDeviceStatus())%>
                        </td><td><%=IoTUtil.getDisplayTargetAction(deviceDetails.getLastAction(),deviceDetails.getDevice1(),deviceDetails.getDevice2())%>
                                </td><td><%=IoTUtil.getDisplayActionBy(deviceDetails.getLastActionBy())%>
                               </td>
                        <% if (deviceDetails.getOfflineFlag()==IConstant.STATUS_ONLINE){
                                //(deviceDetails.getLastPingTime()!=null && new Date().getTime() - deviceDetails.getLastPingTime().getTime() < 60000) {
                                       // use 1 min similar to the device list < (setting.getPingInterval()*1000)+2000) { 
                                %><td>Online</td>
                        <%} else { %>
                                <td>Disconnected!</td>
                        <%} %>			
			<td><% 
				if(deviceDetails.getLastPingTime()!=null) { 
					long timeSeconds=(new Date().getTime()-deviceDetails.getLastPingTime().getTime())/1000;
					out.print(IoTUtil.lastseen(timeSeconds)); 
					} else { %>N/A<%} %>
                        </td>
                        <td>
                        <%if (messages!=null && !messages.isEmpty() && messages.size()>10) {%>
                                <a href="IoTWebServlet?deviceId=<%=deviceId%>&action=<%=IConstant.ACTION_WEB_MESSAGE_GRAPH%>&type=<%=deviceDetails.getModel().getSensor1Graph()%>&sensor=1">
                                <img src="images/graph.png"></a>
                            <br>
                            <% if(deviceDetails.getModel().getSensor2Name()!=null && !"".equals(deviceDetails.getModel().getSensor2Name())) { %>
                                <a href="IoTWebServlet?deviceId=<%=deviceId%>&action=<%=IConstant.ACTION_WEB_MESSAGE_GRAPH%>&type=<%=deviceDetails.getModel().getSensor2Graph()%>&sensor=2">
                                <img src="images/graph.png"></a>
                            <% } %>
                        <% } %>
                        </td>
                        <%
			String actionClick = "IoTWebServlet?deviceId=" + deviceId + "&action=";
			if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
				%><td><a style="width: 200px" class="w3-btn w3-ripple w3-teal" href="<%=actionClick + IConstant.ACTION_WEB_LIST_DEVICE_MESSAGES %>">Show Device Messages</a></td><%
			} else {
                                %><td><%
                                if(deviceDetails.getDeviceModel()>IConstant.ONE_DEVICE_THRESHOLD){
				%><a style="width: 200px" class="w3-btn w3-ripple w3-green" href="<%=actionClick + IConstant.ACTION_ON_DEVICE1%>">ON <%=deviceDetails.getDevice1()%></a><br>
					  <a style="width: 200px" class="w3-btn w3-ripple w3-red" href="<%=actionClick + IConstant.ACTION_OFF_DEVICE1%>">OFF <%=deviceDetails.getDevice1()%></a><br>
                                <%}                                       
				if (deviceDetails.getDeviceModel()>IConstant.TWO_DEVICE_THRESHOLD) {
					%><a style="width: 200px" class="w3-btn w3-ripple w3-green" href="<%=actionClick + IConstant.ACTION_ON_DEVICE2%>">ON <%=deviceDetails.getDevice2()%></a><br>
					  <a style="width: 200px" class="w3-btn w3-ripple w3-red" href="<%=actionClick + IConstant.ACTION_OFF_DEVICE2%>">OFF <%=deviceDetails.getDevice2()%></a><br><%
				}
				if (deviceDetails.getModel()!=null &&
                                        deviceDetails.getModel().getCustomActionName()!=null) {%>
					<!--a href="< =actionClick + deviceDetails.getModel().getCustomActionValue()%>">< =deviceDetails.getModel().getCustomActionName()%></a><br!--><%
				}%>
                                <a style="width: 200px" class="w3-btn w3-ripple w3-yellow" href="<%=actionClick +  IConstant.ACTION_GET_MESSAGE%>">Get New Message</a><br>
                                <a style="width: 200px" class="w3-btn w3-ripple w3-blue" href="<%=actionClick +  IConstant.ACTION_RESTART%>">Device Restart</a><br>
                                <a style="width: 200px" class="w3-btn w3-ripple w3-teal" href="<%=actionClick + IConstant.ACTION_WEB_LIST_DEVICE_MESSAGES%>">Show Device Messages</a><br><%
			}%>
			</tr><%
		%></table>
                    <button class="collapsible">Additional Device Details</button>
                    <div class="content">
                    <table>
                        <tr><td>Device Id</td><td><%=deviceDetails.getDeviceId()%></td></tr>
                        <tr><td>Device Group Id</td><td><%=deviceDetails.getGroupId()==null?"Not Assigned":deviceDetails.getGroupId()%></td></tr>
                        <tr><td>Last Connection IP</td><td><%=deviceDetails.getIpAddress()==null?"N/A":deviceDetails.getIpAddress()%></td></tr>
                        <tr><td>Tags</td><td><%=deviceDetails.getTags()%></td></tr>
                        <% if(deviceDetails.getCustomName()!=null && !"".equals(deviceDetails.getCustomName().trim())){%>
                            <tr><td><%=deviceDetails.getCustomName()%></td><td><%=deviceDetails.getCustomValue()%></td></tr>
                        <% }else{ %>
                            <tr><td>No Custom Field</td><td></td></tr>
                        <% } %>
                        <tr><td>Device Model</td><td><%=deviceDetails.getModel().getDeviceName()%></td></tr>
                        <tr><td>Device HW Type</td><td><%=deviceDetails.getModel().gethWType().getType()%></td></tr>
                        <tr><td>Device Firmware Version</td><td><%=deviceDetails.getFirmwareVersion()==null?"?":deviceDetails.getFirmwareVersion()%></td></tr>
                        <tr><td>Latest Available Firmware Version</td><td><%=deviceDetails.getModel().getFirmwareVersion()%></td></tr>
                        <% if(deviceDetails.getModel().getNoOfSensors()>0){%>
                            <tr><td>1st Sensor High/Low Alert Value</td><td><%=deviceDetails.getHighAlertValue1()%> / <%=deviceDetails.getLowAlertValue1()%></td></tr>
                        <% } %>
                        <% if(deviceDetails.getModel().getNoOfSensors()>1){%>
                            <tr><td>2nd Sensor High/Low Alert Value</td><td><%=deviceDetails.getHighAlertValue2()%> / <%=deviceDetails.getLowAlertValue2()%></td></tr>
                        <% } %>
                        <tr><td>Notification Email</td><td><%=deviceDetails.getNotificationEmail()%></td></tr>
                        <tr><td>Notification Mobile</td><td><%=deviceDetails.getNotificationMobile()==null?"N/A":deviceDetails.getNotificationMobile()%></td></tr>
                        <tr><td>Device Management Option for New Firmware</td><td>
                        <% if(deviceDetails.getDeviceManagement()==0){ %>
                        Wait for device next restart/login
                        <% } else if(deviceDetails.getDeviceManagement()==1){ %>
                        Wait for next restart/login but send notification to the device contact
                        <% } else { %>
                        Restart Device to Apply it Immediately                
                        <% } %>
                        <td></tr>
                        <tr><td>Inbound Traffic (this month)</td><td><%=deviceDetails.getTotalInbound()==null?"N/A":IoTUtil.formatTrafficData(deviceDetails.getTotalInbound())%> (<%=deviceDetails.getTotalInboundMonth()==null?"N/A":IoTUtil.formatTrafficData(deviceDetails.getTotalInboundMonth())%>)</td></tr>
                        <tr><td>OutBound Traffic (this month)</td><td><%=deviceDetails.getTotalOutbound()==null?"N/A":IoTUtil.formatTrafficData(deviceDetails.getTotalOutbound())%> (<%=deviceDetails.getTotalOutboundMonth()==null?"N/A":IoTUtil.formatTrafficData(deviceDetails.getTotalOutboundMonth())%>)</td></tr>
                        <tr><td>Total Messages (this month)</td><td><%=deviceDetails.getTotalMessages()==null?"N/A":deviceDetails.getTotalMessages()%> (<%=deviceDetails.getTotalMessagesMonth()==null?"N/A":deviceDetails.getTotalMessagesMonth()%>)</td></tr>
                        <tr><td>Total Alerts</td><td><%=deviceDetails.getTotalAlerts()==null?"N/A":deviceDetails.getTotalAlerts()%></td></tr>
                        <%if(deviceDetails.getDeviceModel()>IConstant.ONE_DEVICE_THRESHOLD) {%>
                        <tr><td>Smart Rules for device 1 :"<%=deviceDetails.getDevice1()%>"</td>
                            <td>On High Alert : 
                            <% if(deviceDetails.getSmartRules1()==1 || deviceDetails.getSmartRules1()==11 ||
                            deviceDetails.getSmartRules1()==21){%>
                                Switch ON <%=deviceDetails.getDevice1()%>
                            <% } else if(deviceDetails.getSmartRules1()==2 || deviceDetails.getSmartRules1()==12 ||
                                    deviceDetails.getSmartRules1()==22){%>
                                Switch OFF <%=deviceDetails.getDevice1()%>
                            <%} else { %>
                                No Local Smart Rule Rule                
                            <% } %>
                            <br>
                            On Low Alert :
                            <% if(deviceDetails.getSmartRules1()==10 || deviceDetails.getSmartRules1()==11 ||
                                    deviceDetails.getSmartRules1()==12){%>
                                Switch ON <%=deviceDetails.getDevice1()%>
                            <% } else if(deviceDetails.getSmartRules1()==20 || deviceDetails.getSmartRules1()==21 ||
                                    deviceDetails.getSmartRules1()==22){%>
                                Switch OFF <%=deviceDetails.getDevice1()%>
                            <%} else { %>
                                No Local Smart Rule Rule
                            <% } %>
                            </td></tr>
                        <% } %>
                        <%if(deviceDetails.getDeviceModel()>IConstant.TWO_DEVICE_THRESHOLD) {%>
                        <tr><td>Smart Rules for device 2 :"<%=deviceDetails.getDevice2()%>"</td>
                            <td>On High Alert : 
                            <% if(deviceDetails.getSmartRules2()==1 || deviceDetails.getSmartRules2()==11 ||
                            deviceDetails.getSmartRules2()==21){%>
                                Switch ON <%=deviceDetails.getDevice2()%>
                            <% } else if(deviceDetails.getSmartRules2()==2 || deviceDetails.getSmartRules2()==12 ||
                                    deviceDetails.getSmartRules2()==22){%>
                                Switch OFF <%=deviceDetails.getDevice2()%>
                            <%} else { %>
                                No Local Smart Rule Rule                
                            <% } %>
                            <br>
                            On Low Alert : 
                            <% if(deviceDetails.getSmartRules2()==10 || deviceDetails.getSmartRules2()==11 ||
                                    deviceDetails.getSmartRules2()==12){%>
                                Switch ON <%=deviceDetails.getDevice2()%>
                            <% } else if(deviceDetails.getSmartRules2()==20 || deviceDetails.getSmartRules2()==21 ||
                                    deviceDetails.getSmartRules2()==22){%>
                                Switch OFF <%=deviceDetails.getDevice2()%>
                            <%} else { %>
                                No Local Smart Rule Rule
                            <% } %>
                            </td></tr>
                        <% } %>                    
                    </table>
                    </div>                
                    
        <% if(user.getUserRole()==IConstant.ROLE_READ_WRITE) { %>                
            <a class="w3-btn w3-ripple w3-red" href='editDevice.jsp'>Edit Device Details & Smart Rules</a> 
        &nbsp;<a class="w3-btn w3-ripple w3-red" onclick="return confirm('Confirm Delete ALL Existing Device Messages?');" href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_DELETE_ALL_DEVICE_MESSAGES%>&deviceId=<%=deviceId%>'>Purge All Device Messages</a>
            <% if(deviceDetails.getDeviceStatus()>1){ %>
                &nbsp;<a class="w3-btn w3-ripple w3-red" onclick="return confirm('Confirm Activate Device?');" href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_ACTIVATE_DEVICE%>&deviceId=<%=deviceId%>'>Activate Device</a>
            <% } else {%>
                &nbsp;<a class="w3-btn w3-ripple w3-red" onclick="return confirm('Confirm Deactivate Device?');" href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_INACTIVATE_DEVICE%>&deviceId=<%=deviceId%>'>Deactivate Device</a>
            <% } %>
	<% } %>
        &nbsp;<a class="w3-btn w3-ripple w3-red" href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_LIST_DEVICES%>'>Back to Device List Page</a>
        <button class="collapsible">Recent Device Messages</button>
        <div class="content">
        <h3>&nbsp;Total displayed messages: <%=messages.size()%></h3>
	<table>
            <%if (!messages.isEmpty()) {%>
                <tr><th>Message Payload</th><th>Received Time</th><th>Message Type</th></tr>
                <%for (int n = 0; n<messages.size(); n++) {
                        Messages message = messages.get(n);
                        if (message.getType() == 2){
                                %><tr style='color:red'><%
                        } else if (message.getType() == 3){ 
                                %><tr style='color:#FF8C00'><%
                        } else{
                                %><tr><%} %>
                        <td><%=IoTUtil.payloadExtractorForWeb(deviceDetails,message.getPayload(),message.getType())%>
                        </td><td><%=formatter.format(message.getMessageTime())%>
                        </td><td><%=IoTUtil.getDisplayMessageTypeForWeb(message.getType())%>
                        </td></tr><%
                }
            } else {%>
                    <tr><td>N/A</td></tr><%
            }%>
	</table>
        </div>
    <script>
        var coll = document.getElementsByClassName("collapsible");
        var i;
        for (i = 0; i < coll.length; i++) {
          coll[i].addEventListener("click", function() {
            this.classList.toggle("active");
            var content = this.nextElementSibling;
            if (content.style.maxHeight){
              content.style.maxHeight = null;
            } else {
              content.style.maxHeight = content.scrollHeight + "px";
            } 
          });
        }
    </script>        
	<br><a class="w3-btn w3-ripple w3-red" href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_LIST_DEVICES%>'>Back to Device List Page</a><br><br>
    <%@include file="footer.jsp" %>
</body>
</html>
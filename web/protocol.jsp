<%@page import="osa.ora.iot.db.beans.TenantSettings"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.TimeZone"%>
<%@page import="osa.ora.iot.db.beans.DeviceModel"%>
<%@page import="osa.ora.iot.db.beans.Workflows"%>
<%@page import="java.util.ArrayList"%>
<%@page import="osa.ora.iot.beans.IConstant"%>
<%@page import="java.util.Date"%>
<%@page import="osa.ora.iot.util.IoTUtil"%>
<%@page import="osa.ora.iot.db.beans.Users"%>
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
if(user.getSystemAdmin()!=1){
    request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
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
<title>SensorWays IoT, Protocol Information Page</title>
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
        &nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > SensorWays Open Protocol<br>
        &nbsp;In this page the protocol description between the IoT Devices and the IoT Platform.
	<table>
            <tr><td>Communications</td><td>Secure HTTPS (SSL) for all communication messages.</td></tr>
            <tr><td>Header Parameters</td><td>The following header parameters are required:<br>
                User-Agent: OsaOraIoTDevice<br>
                Content-Type: application/x-www-form-urlencoded<br>
                Content-Length: the length of the message body<br>
                </td></tr>
            <tr><td>Login Message</td><td>
                    The URL for the request is: /IoTHub/IoTServlet<br>
                    The following parameters are required: <br>
                    i=base64 device Identifier<br> 
                    p=base64 encoded device password<br>
                    t=message type (0 for Login)<br>
                    a=action (0 for login)<br>
                    v=current device firmware version
                    <br>
                    The response will be:<br>
                    s:response values (success otherwise failed) and the response contains the following values separated by #<br>
                    secret code (the generated secret code for later communications) <br>
                    #Loop wait time (number of seconds between every ping to the server) <br>
                    #Update interval (how many ping before sending an update message) <br>
                    #high alert threshold for sensor 1 <br>
                    #high alert threshold for sensor 2 <br>
                    #low alert threshold for sensor 1 <br>
                    #low alert threshold for sensor 2 <br>
                    #connected device 1 last known status (either 0 off or 1 on)<br>
                    #connected device 2 last known status (either 0 off or 1 on)<br>
                    #smart rules value for connected device 1 (<font color="red">*</font>)<br>
                    #smart rules value for connected device 2 (<font color="red">*</font>)<br>
                    #server firmware version
                    <hr>
                    <br>
                    (<font color="red">*</font>) The following are the possible values for smart rules:<br>
                    0 No Rules to be executed<br>
                    1 Switch On the connected device 1/2 on High alert Sensor Value<br>
                    2 Switch Off the connected device 1/2 on High Alert Sensor Value<br>
                    10 Switch On the connected device 1/2 on Low alert Sensor Value<br>
                    20 Switch Off the connected device 1/2 on Low Alert Sensor Value<br>
                    Mixed Rules Values:<br>
                    11 = 10 + 1<br>
                    12 = 10 + 2<br>
                    21 = 1 + 20<br>
                    22 = 2 + 20<br>
                </td></tr>
            <tr><td>Update Firmware Request(<font color="red">*</font>)<br><br>(<font color="red">*</font>) 
                    Not over HTTPS at the moment,<br> a fix will be required here by either<br>using HTTPS, encrypt the parameters except id using the sessionKey or Send the request over HTTPS and get the response later.</td>
                <td>If the device current firmware version less than the server version an update 
                    request is sent to the server to get the latest firmware
                    <br>
                    The URL for the request is: /IoTHub/IoTOTAServlet
                    <br>
                    The following parameters are required:<br>
                    i=base64 device identifier <br>
                    p=base64 encoded device password<br>
                    w=base64 encode WiFi ssid <br>
                    wp=base64 encode WiFi password <br>
                    m=device Model <br>
                    The response will be download and install the new firmware and restart the device.
                </td></tr>
            <tr><td>Ping Message</td><td>
                    The URL for the request is: /IoTHub/IoTServlet<br>
                    The following parameters are required:<br>
                    t=message type (not of any value)<br>
                    a=action (must be set to 3)<br>
                    i=base64 device identifier<br>
                    s=device secret code<br>
                    The response will be:<br>
                    s:response values which include one value:<br>
                    action required: the action that need to be executed in this device i.e. switch on/off any connected device
                </td></tr>
            <tr><td>Update Message</td><td>
                    The URL for the request is: /IoTHub/IoTServlet<br>
                    The following parameters are required:<br>
                    t=message type which depends on the message type:<br>
                    1 for normal message, 2 for high alert, 4 for low alert, 6 for error message, <br>
                    0 for login message, 7 for update firmware<br>
                    Alert/Error messages or change of device action is sent as an update message regardless of the number of ping.<br>
                    a=action (must be set to 5)<br>
                    i=base64 device identifier<br>
                    s=device secret code<br>
                    f=payload_format which could be either 0 for comma separated values, 1 for JSON and 2 for binary format e.g. image, if not sent it is set as 0<br>
                    m=message payload which contains the actual message in the described format, e.g. for comma separated it looks like:<br>
                    sensor 1 value,[sensor 2 value], [device 1 state], [device 2 state][,Low Battery Flag]<br>
                    The device status could be F (for OFF) N for (ON)<br>
                    Low Battery Flag is L<br>
                    The response will be:<br>
                    s:response values which include one value:<br>
                    action required: the action that need to be executed in this device i.e. switch on/off any connected device<br>
                    Action 0 = Re-login the device<br>
                    Action 4 = Device Restart Request<br>
                    Action 9 = Request an Update Message from the Device<br>
                    Action 1 = Switch On connected device 1<br>
                    Action 2 = Switch Off connected device 1<br>
                    Action 6 = Switch On connected device 2<br>
                    Action 7 = Switch Off connected device 2<br>
                </td></tr>
            <tr><td></td><td></td></tr>
        </table>
        <a class="w3-btn w3-ripple w3-red" href='main.jsp'>Back to Main Menu</a><br><br>
    <%@include file="footer.jsp" %>
</body>
</html>
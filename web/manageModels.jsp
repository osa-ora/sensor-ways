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
<title>SensorWays IoT, Manage Device Models Page</title>
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
        &nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > Manage Device Models
        <h3>&nbsp;List of Device Models</h3>
	<table>
	<tr><th>Name</th><th>Attached Sensors</th><th>Controlled Devices</th><th>Status</th><th>No of Registered Devices</th><th>HW Type/Firmware Version</th><th>1st Sensor High/Low Alert Values</th><th>2nd Sensor High/Low Alert Values</th><th>Actions</th></tr>
	<%
        List<DeviceModel> deviceModelsList = null;
	if(session.getAttribute("DEVICE_MODELS")!=null){
		deviceModelsList = (List<DeviceModel>)session.getAttribute("DEVICE_MODELS");
	for (int n = 0; n < deviceModelsList.size(); n++) {
            DeviceModel model = deviceModelsList.get(n);
            %>
            <tr><td><%=model.getDeviceName()%>
            </td><td><%=model.getNoOfSensors()%>
            </td><td><%=model.getNoOfDevices()%>
            </td><td><%=IoTUtil.getDisplayDeviceStatus(model.getModelStatus())%>
            </td><td><%=model.getDeviceCount()%>
            </td><td><%=model.gethWType().getType()%> / <%=model.getFirmwareVersion()%>
            </td><td><%=model.getHighAlertValue1()==null?"-":model.getHighAlertValue1()%> / <%=model.getLowAlertValue1()==null?"-":model.getLowAlertValue1()%>
            </td><td><%=model.getHighAlertValue2()==null?"-":model.getHighAlertValue2()%> / <%=model.getLowAlertValue2()==null?"-":model.getLowAlertValue2()%>
            </td><td><%
            String actionClick = "IoTWebServlet?modelId=" + model.getId()+ "&action=";
            if(user.getUserRole()==IConstant.ROLE_READ_WRITE) { 
                if(model.getModelStatus()==1){%>
                    <a style="width: 200px" class="w3-btn w3-ripple w3-red" href="<%=actionClick + IConstant.ACTION_WEB_INACTIVATE_MODEL%>">Deactivate Model</a><br>
                <%} else {%>
                    <a style="width: 200px" class="w3-btn w3-ripple w3-green" href="<%=actionClick + IConstant.ACTION_WEB_ACTIVATE_MODEL%>">Activate Model</a><br>
                <% }%>
            <%}%>
            </td></tr>
	<%}
        }%>
        </table>
        <% if(user.getUserRole()==IConstant.ROLE_READ_WRITE) { %>
        <br><a class="w3-btn w3-ripple w3-red" href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_REGISTER_MODEL%>'>Add New Device Model</a>
        <%} %>
        <a class="w3-btn w3-ripple w3-red" href='main.jsp'>Back to Main Menu</a><br><br>
    <%@include file="footer.jsp" %>
</body>
</html>
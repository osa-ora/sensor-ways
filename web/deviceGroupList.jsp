<%@page import="osa.ora.iot.db.beans.DeviceGroup"%>
<%@page import="osa.ora.iot.db.beans.TenantSettings"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.TimeZone"%>
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
String notificationCount=(String)session.getAttribute("NOTIFICATION_COUNT");
TenantSettings setting= (TenantSettings) session.getAttribute("USER_SETTINGS");
DateFormat formatter = new SimpleDateFormat("EEE dd MMM HH:mm");
formatter.setTimeZone(TimeZone.getTimeZone(setting.getTimezone()));
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SensorWays IoT, Device Group List Page</title>
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
	&nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > Device Groups
        <h3>&nbsp;List of Device Groups</h3>
	<table>
	<tr><th>Id</th><th>Name</th><th>Total Devices</th><th>Action</th></tr>
	<%
        List<DeviceGroup> deviceGroupList = null;
	if(session.getAttribute("DEVICE_GROUP_LIST")!=null){
		deviceGroupList = (List<DeviceGroup>)session.getAttribute("DEVICE_GROUP_LIST");
	for (int n = 0; n < deviceGroupList.size(); n++) {
            DeviceGroup deviceGroup = deviceGroupList.get(n);
            %>
            <tr><td><%=deviceGroup.getId()%>
            </td><td><%=deviceGroup.getName()%>
            </td><td><%=deviceGroup.getDeviceCount()%>
            </td><td><%
            String actionClick = "DeviceGroupServlet?groupId=" + deviceGroup.getId()+ "&action=";
            if(user.getUserRole()==IConstant.ROLE_READ_WRITE) {  %>
                <a style="width: 200px" class="w3-btn w3-ripple w3-red" href="<%=actionClick + IConstant.ACTION_WEB_INACTIVATE_DEVICE_GROUP%>">Deactivate All Devices</a> 
                <a style="width: 200px" class="w3-btn w3-ripple w3-green" href="<%=actionClick + IConstant.ACTION_WEB_ACTIVATE_DEVICE_GROUP%>">Activate All Devices</a>
                <a style="width: 200px" class="w3-btn w3-ripple w3-purple" href="<%=actionClick + IConstant.ACTION_WEB_RESTART_DEVICE_GROUP%>">Restart All Devices</a><br> 
                <a style="width: 200px" class="w3-btn w3-ripple w3-purple" href="<%=actionClick + IConstant.ACTION_WEB_GET_MESSAGE_DEVICE_GROUP%>">Request New Messages</a>
                <a style="width: 200px" class="w3-btn w3-ripple w3-yellow" href="<%=actionClick + IConstant.ACTION_WEB_UPDATE_DEVICE_GROUP%>">Update Information</a>
                <a style="width: 200px" class="w3-btn w3-ripple w3-teal" onclick="return confirm('Confirm Delete this Group?');" href="<%=actionClick + IConstant.ACTION_WEB_DELETE_DEVICE_GROUP%>">Delete Device Group</a></td>
            <%}%>
            </td></tr>
	<%}
        }%>
        </table>
        <% if(user.getUserRole()==IConstant.ROLE_READ_WRITE  && (deviceGroupList==null || deviceGroupList.size()<50)) { %>
        <br><a class="w3-btn w3-ripple w3-red" href='deviceGroup.jsp'>Create New Device Group</a>
        <%} %>
        <a class="w3-btn w3-ripple w3-red" href='main.jsp'>Back to Main Menu</a><br><br>
    <%@include file="footer.jsp" %>
</body>
</html>
<%@page import="osa.ora.iot.db.beans.DeviceGroup"%>
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
<title>SensorWays IoT, Create New IoT Application</title>
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
        &nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > <a href='IoTAppServlet?action=110'>Application List</a> > Create New IoT Application<br>
        &nbsp;You can create IoT applications from the existing supported ready IoT applications.
	<br>
        <form method="post" action="IoTAppServlet" enctype="multipart/form-data">
        <input type="hidden" name="action" value="<%=IConstant.ACTION_WEB_REGISTER_NEW_APP%>"/>
	<table>
            <tr><td>Application Name</td><td><input type="text" name="name" size="40" maxlength="40" value="" style="width: 400pt;"/></td></tr>
            <tr><td>Application Description</td><td><input type="text" name="scope" size="200" maxlength="200" value="" style="width: 400pt;"/></td></tr>
            <tr><td>Application Banner</td><td><input id="file" name="file" type="file" style="width: 400pt;"></td></tr>
            <tr><td>Application Type</td><td>
                <select name="type" style="width: 400pt;">
                  <option value="1">Fridge Monitoring v1.0</option>
                  <option value="2">Fire Alarm v1.0</option>
                  <option value="3">Farm Irrigation v1.0</option>
                  <option value="4">Security Monitoring v1.0</option>
                  <option value="5">Home Monitoring v1.0</option>
                  <option value="6">Home Automation v1.0</option>
                  <option value="7">Generic IoT Application v1.0</option>
                </select>
            </td></tr>
            <tr><td>Login Required</td><td>
                    <select name="login" style="width: 400pt;">
                      <option value="1">Always</option>
                    </select>
            </td></tr>
            <tr><td>Alert Notification Enabled</td><td>
                    <select name="alert" style="width: 400pt;">
                      <option value="1" selected>Yes</option>
                      <option value="2">No</option>
                    </select>
            </td></tr>
            <tr><td>Device Notification Email</td><td><input type="email" multiple name="email" size="70" maxlength="70" value="" style="width: 400pt;" /><br>(*) Can be multiple email addresses separated by ","</td></tr>
            <tr><td>Device Notification Mobile</td><td><input type="tel" name="mobile" size="20" maxlength="20" value="" style="width: 400pt;"/></td></tr>            
            <tr><td>Initial Status</td><td>
                    <select name="status" style="width: 400pt;">
                      <option value="1">Active</option>
                      <option value="2">Inactive</option>
                    </select>
            </td></tr>
            <tr><td>Device Groups</td>
                <td><select name="groups" multiple style="width: 400pt;"/>
                <%
                List<DeviceGroup> deviceGroupList = null;
                if(session.getAttribute("DEVICE_GROUP_LIST")!=null){
                    deviceGroupList = (List<DeviceGroup>)session.getAttribute("DEVICE_GROUP_LIST");
                    for (int n = 0; n < deviceGroupList.size(); n++) {
                        DeviceGroup deviceGroup = deviceGroupList.get(n);
                        %>
                    <option value="<%=deviceGroup.getId()%>"><%=deviceGroup.getName()%></option>
                <% }
                } %>
                </select>
                </td>
            </tr>
	<tr><td colspan=2><input value="Create New Application" type="submit"/></td></tr>
	</table>
	<br><a class="w3-btn w3-ripple w3-red" href='IoTAppServlet?action=110'>Back to Applications List Page</a>
	</form>
    <%@include file="footer.jsp" %>
</body>
</html>
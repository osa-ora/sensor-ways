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
<title>SensorWays IoT, Build Workflows</title>
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
        &nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > <a href='WorkflowServlet?action=16'>Manage Workflows</a> > Create New Workflow<br>
        &nbsp;You can build subsequent actions as per input data received from different connected IoT devices in your account.<br>&nbsp;The workflows differ from the smart local rules being executed in the platform not in the local devices so it require the device to be connected and can be designed across many IoT devices.
	<br>
	<form method="post" action="WorkflowServlet">
        <input type="hidden" name="action" value="<%=IConstant.ACTION_WEB_REGISTER_WORKFLOW%>"/>
	<table>
            <tr><td>Workflow Rule Name</td><td><input type="text" name="name" size="20" maxlength="20" value="" style="width: 200pt;"/></td></tr>
        <tr><td>Source Type</td><td>
            <select name="type"/>
            <option value="1" selected="true" onclick="document.forms[0].source.disabled = false;document.forms[0].group.disabled = true;">Device</option>
                <option value="2" onclick="document.forms[0].source.disabled = true;document.forms[0].group.disabled = false;">Device Group</option>
            </select>
        </td></tr>
        <tr><td>Source IoT Device</td>
            <td><select name="source" style="width: 200pt;">
	<%
            List<Devices> deviceList = null;
            if(session.getAttribute("DEVICE_LIST")!=null){
                    deviceList = (List<Devices>)session.getAttribute("DEVICE_LIST");
            }else{
                    deviceList = new ArrayList<Devices>(); 
            }
            for (int i = 0; i < deviceList.size(); i++) {
                %> <option value="<%=deviceList.get(i).getDeviceId() %>"><%=deviceList.get(i).getFriendlyName()%></option>
            <% } %>
            </select>     
            </td></tr>
        <tr><td>Source IoT Device Group</td>
            <td><select name="group" disabled="true"/>
                <option value="-1" selected >No Device Group</option>
                <%
                List<DeviceGroup> deviceGroupList = null;
                if(session.getAttribute("DEVICE_GROUP_LIST")!=null){
                    deviceGroupList = (List<DeviceGroup>)session.getAttribute("DEVICE_GROUP_LIST");
                    for (int n = 0; n < deviceGroupList.size(); n++) {
                        DeviceGroup deviceGroup = deviceGroupList.get(n);%>
                            <option value="<%=deviceGroup.getId()%>"><%=deviceGroup.getName()%></option>
                <% }
                } %>
                </select>     
            </td></tr>
        <tr><td>Source Event</td><td>
                <select name="event" style="width: 200pt;">
		  <option value="10">High Alert</option>
                  <option value="11">Normal Reading</option>
		  <option value="13">Low Alert</option>
		  <option value="14">Error Message</option>
		</select>
	</td></tr>
        <tr><td></td><td colspan="2"><img src="images/green_arrow.png"/></td></tr>
        <tr><td>Target IoT Device</td>
            <td><select name="target" style="width: 200pt;">
            <%for (int i = 0; i < deviceList.size(); i++) {
                if(deviceList.get(i).getDeviceModel()>IConstant.ONE_DEVICE_THRESHOLD){
                %> <option value="<%=deviceList.get(i).getDeviceId() %>"><%=deviceList.get(i).getFriendlyName()%></option>
            <%  }
              } %>                    
            </td></tr>
        <tr><td>Target Action</td><td>
                <select name="target_action" style="width: 200pt;">
                  <option value="1">Switch on device 1</option>
                  <option value="2">Switch off device 1</option>
                  <option value="6">Switch on device 2</option>
                  <option value="7">Switch off device 2</option>
		</select>
	</td></tr>
        <tr><td>Initial Status</td><td>
                <select name="status">
		  <option value="1">Active</option>
		  <option value="2">Inactive</option>
		</select>
	</td></tr>
	<tr><td colspan=2><input value="Create Rule" type="submit"/></td></tr>
	</table>
	<br><a class="w3-btn w3-ripple w3-red" href='WorkflowServlet?action=16'>Back to Workflow List Page</a>
	</form>
    <%@include file="footer.jsp" %>
</body>
</html>
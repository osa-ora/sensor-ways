<%@page import="osa.ora.iot.db.beans.DeviceGroup"%>
<%@page import="osa.ora.iot.db.beans.TenantSettings"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.TimeZone"%>
<%@page import="osa.ora.iot.db.beans.Devices"%>
<%@page import="osa.ora.iot.db.beans.DeviceModel"%>
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
if(user.getUserRole()!=IConstant.ROLE_READ_WRITE) { 
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
<title>SensorWays IoT, Edit Device Details</title>
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
	&nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > <a href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_LIST_DEVICES%>'>Device List</a> > Edit Device Details
	<br>
	<form method="post" action="IoTWebServlet">
        <input type="hidden" name="action" value="<%=IConstant.ACTION_WEB_EDIT_DEVICE%>"/>
	<table>
        <%
            Devices deviceDetails = null;
            if(session.getAttribute("DEVICE_DETAILS")!=null){
                    deviceDetails = (Devices)session.getAttribute("DEVICE_DETAILS");
            }
        %>
	<tr><td>Device Friendly Name</td><td><input type="text" name="friendlyName" size="50" maxlength="50" value="<%=deviceDetails.getFriendlyName()%>"/></td></tr>
        <tr><td>Device Group</td><td>
            <select name="group"/>
                <% if(deviceDetails.getGroupId()==null){ %>
                    <option value="-1" selected >No Device Group</option>
                <% } else { %>
                    <option value="-1">No Device Group</option>
                <% } %>
                <%
                List<DeviceGroup> deviceGroupList = null;
                if(session.getAttribute("DEVICE_GROUP_LIST")!=null){
                    deviceGroupList = (List<DeviceGroup>)session.getAttribute("DEVICE_GROUP_LIST");
                    for (int n = 0; n < deviceGroupList.size(); n++) {
                        DeviceGroup deviceGroup = deviceGroupList.get(n);
                        String id=deviceGroup.getId()+"";
                        if(id.equals(deviceDetails.getGroupId())){%>
                            <option value="<%=deviceGroup.getId()%>" selected><%=deviceGroup.getName()%></option>
                        <% } else { %>
                            <option value="<%=deviceGroup.getId()%>"><%=deviceGroup.getName()%></option>
                        <% } %>
                <% }
                } %>
                </select>
        </td></tr>
        <tr><td>Device Location</td><td><input type="text" name="location" size="50" maxlength="50" value="<%=deviceDetails.getLocation()%>"/></td></tr>
        <tr><td>Device Tag(s)</td><td><input type="text" name="tag" size="50" maxlength="50" value="<%=deviceDetails.getTags()%>"/></td></tr>
        <tr><td>Custom Field Name</td><td><input type="text" name="custom_name" size="30" maxlength="20" value="<%=deviceDetails.getCustomName()%>"/></td></tr>
        <tr><td>Custom Filed Value</td><td><input type="text" name="custom_value" size="30" maxlength="20" value="<%=deviceDetails.getCustomValue()%>"/></td></tr>
        <tr><td>Device Notification Email</td><td><input type="email" name="email" multiple size="70" maxlength="70" value="<%=deviceDetails.getNotificationEmail()%>"/><br>(*) Can be multiple email addresses separated by ","</td></tr>
        <tr><td>Device Notification Mobile</td><td><input type="tel" name="mobile" size="20" maxlength="20" value="<%=deviceDetails.getNotificationMobile()==null?"":deviceDetails.getNotificationMobile()%>"/></td></tr>
        <tr><td>Device Management Option for New Firmware</td><td>
                <% if(deviceDetails.getDeviceManagement()==0){ %>
                <input type="radio" name="dev_manage" value="0" checked="checked">Wait for device next restart/login<br>
                <input type="radio" name="dev_manage" value="1">Wait for next restart/login but send notification to the device contact<br>
                <input type="radio" name="dev_manage" value="2">Restart Device to Apply it Immediately
                <% } else if(deviceDetails.getDeviceManagement()==1){ %>
                <input type="radio" name="dev_manage" value="0">Wait for device next restart/login<br>
                <input type="radio" name="dev_manage" value="1" checked="checked">Wait for next restart/login but send notification to the device contact<br>
                <input type="radio" name="dev_manage" value="2">Restart Device to Apply it Immediately
                <% } else { %>
                <input type="radio" name="dev_manage" value="0">Wait for device next restart/login<br>
                <input type="radio" name="dev_manage" value="1">Wait for next restart/login but send notification to the device contact<br>
                <input type="radio" name="dev_manage" value="2" checked="checked">Restart Device to Apply it Immediately                
                <% } %>
	</td></tr>
        <%if(deviceDetails.getDeviceModel()>IConstant.ONE_DEVICE_THRESHOLD) {%>
        <tr><td>Connected Device 1 Name</td><td><input type="text" name="device1Name" size="30" maxlength="30" value="<%=deviceDetails.getDevice1()%>"/>
                <select name="suggested" onchange="document.forms[0].device1Name.value=this.value;">
		  <option value="Light">Light</option>
		  <option value="Socket">Socket</option>
                  <option value="Switch">Switch</option>
		  <option value="Outlet Switch">Outlet Switch</option>
                  <option value="TV">TV</option>
                  <option value="Air Condition">Air Condition</option>
                  <option value="Fan">Fan</option>
                  <option value="Camera">Camera</option>
                  <option value="Door Switch">Door Switch</option>
                  <option value="Alarm">Alarm</option>
                  <option value="Heart Rate">Heart Rate</option>
                  <option value="Music Device">Music Device</option>
                  <option value="ECG">ECG</option>
		</select>          
            </td></tr>
        <% } %>
        <%if(deviceDetails.getDeviceModel()>IConstant.TWO_DEVICE_THRESHOLD) {%>        
                <tr><td>Connected Device 2 Name</td><td><input type="text" name="device2Name" size="30" maxlength="30" value="<%=deviceDetails.getDevice2()%>"/>
                <select name="suggested2" onchange="document.forms[0].device2Name.value=this.value;">
		  <option value="Light">Light</option>
		  <option value="Socket">Socket</option>
                  <option value="Switch">Switch</option>
		  <option value="Outlet Switch">Outlet Switch</option>
                  <option value="TV">TV</option>
                  <option value="Air Condition">Air Condition</option>
                  <option value="Fan">Fan</option>
                  <option value="Camera">Camera</option>
                  <option value="Door Switch">Door Switch</option>
                  <option value="Alarm">Alarm</option>
                  <option value="Heart Rate">Heart Rate</option>
                  <option value="Music Device">Music Device</option>
                  <option value="ECG">ECG</option>
                </select>  
            </td></tr>
	<% } %>
        <% if(deviceDetails.getModel().getNoOfSensors()>0){%>
        <tr><td colspan="2">Note: Any changes to the Sensor Alert Values, will not be reflected unless the device login again or via device restart.
        </td></tr>
        <tr><td>High Alert Threshold Value (for 1st Sensor)</td><td>
                <input type="text" name="high_alert1" id="high_alert1" size="8" maxlength="8" value="<%=deviceDetails.getHighAlertValue1()%>"/>
        </td></tr>
        <tr><td>Low Alert Threshold Value (for 1st Sensor)</td><td>
                <input type="text" name="low_alert1" id="low_alert1" size="8" maxlength="8" value="<%=deviceDetails.getLowAlertValue1()%>"/>
        </td></tr>
        <%} %>
        <% if(deviceDetails.getModel().getNoOfSensors()>1){%>
        <tr><td>High Alert Threshold Value 2 (for 2nd Sensor)</td><td>
                <input type="text" name="high_alert2" id="high_alert2" size="8" maxlength="8" value="<%=deviceDetails.getHighAlertValue2()%>"/>
        </td></tr>
        <tr><td>Low Alert Threshold Value 2 (for 2nd Sensor)</td><td>
                <input type="text" name="low_alert2" id="low_alert2" size="8" maxlength="8" value="<%=deviceDetails.getLowAlertValue2()%>"/>
        </td></tr>        
        <%} %>
        <%if(deviceDetails.getDeviceModel()>IConstant.ONE_DEVICE_THRESHOLD) {%>
        <tr><td>Device 1 "<%=deviceDetails.getDevice1()%>" Smart Rules<br>Note: Smart Rules are executed locally in the device even if it is disconnected from the internet.</td><td>
            On High Alert <select name="sr1">
                <% if(deviceDetails.getSmartRules1()==1 || deviceDetails.getSmartRules1()==11 ||
                        deviceDetails.getSmartRules1()==21){%>
                    <option value="0">No Local Smart Rule Rule</option>
                    <option value="1" selected>Switch ON <%=deviceDetails.getDevice1()%></option>
                    <option value="2">Switch OFF <%=deviceDetails.getDevice1()%></option>
                <% } else if(deviceDetails.getSmartRules1()==2 || deviceDetails.getSmartRules1()==12 ||
                        deviceDetails.getSmartRules1()==22){%>
                    <option value="0">No Local Smart Rule Rule</option>
                    <option value="1">Switch ON <%=deviceDetails.getDevice1()%></option>
                    <option value="2" selected>Switch OFF <%=deviceDetails.getDevice1()%></option>
                <%} else { %>
                    <option value="0" selected>No Local Smart Rule Rule</option>
                    <option value="1">Switch ON <%=deviceDetails.getDevice1()%></option>
                    <option value="2">Switch OFF <%=deviceDetails.getDevice1()%></option>                
                <% } %>
            </select><br>
            On Low Alert <select name="sr2">
                <% if(deviceDetails.getSmartRules1()==10 || deviceDetails.getSmartRules1()==11 ||
                        deviceDetails.getSmartRules1()==12){%>
                    <option value="0">No Local Smart Rule Rule</option>
                    <option value="10" selected>Switch ON <%=deviceDetails.getDevice1()%></option>
                    <option value="20">Switch OFF <%=deviceDetails.getDevice1()%></option>
                <% } else if(deviceDetails.getSmartRules1()==20 || deviceDetails.getSmartRules1()==21 ||
                        deviceDetails.getSmartRules1()==22){%>
                    <option value="0">No Local Smart Rule Rule</option>
                    <option value="10">Switch ON <%=deviceDetails.getDevice1()%></option>
                    <option value="20" selected>Switch OFF <%=deviceDetails.getDevice1()%></option>
                <%} else { %>
                    <option value="0" selected>No Local Smart Rule Rule</option>
                    <option value="10">Switch ON <%=deviceDetails.getDevice1()%></option>
                    <option value="20">Switch OFF <%=deviceDetails.getDevice1()%></option>                
                <% } %>
            </select>
        </td></tr>
        <% } %>
        <%if(deviceDetails.getDeviceModel()>IConstant.TWO_DEVICE_THRESHOLD) {%>        
        <tr><td>Device 2 "<%=deviceDetails.getDevice2()%>" Smart Rules</td><td>
            On High Alert <select name="sr3">
                <% if(deviceDetails.getSmartRules2()==1 || deviceDetails.getSmartRules2()==11 ||
                        deviceDetails.getSmartRules2()==21){%>
                    <option value="0">No Local Smart Rule Rule</option>
                    <option value="1" selected>Switch ON <%=deviceDetails.getDevice2()%></option>
                    <option value="2">Switch OFF <%=deviceDetails.getDevice2()%></option>
                <% } else if(deviceDetails.getSmartRules2()==2 || deviceDetails.getSmartRules2()==12 ||
                        deviceDetails.getSmartRules2()==22){%>
                    <option value="0">No Local Smart Rule Rule</option>
                    <option value="1">Switch ON <%=deviceDetails.getDevice2()%></option>
                    <option value="2" selected>Switch OFF <%=deviceDetails.getDevice2()%></option>
                <%} else { %>
                    <option value="0" selected>No Local Smart Rule Rule</option>
                    <option value="1">Switch ON <%=deviceDetails.getDevice2()%></option>
                    <option value="2">Switch OFF <%=deviceDetails.getDevice2()%></option>                
                <% } %>
            </select><br>
            On Low Alert <select name="sr4">
                <% if(deviceDetails.getSmartRules2()==10 || deviceDetails.getSmartRules2()==11 ||
                        deviceDetails.getSmartRules2()==12){%>
                    <option value="0">No Local Smart Rule Rule</option>
                    <option value="10" selected>Switch ON <%=deviceDetails.getDevice2()%></option>
                    <option value="20">Switch OFF <%=deviceDetails.getDevice2()%></option>
                <% } else if(deviceDetails.getSmartRules2()==20 || deviceDetails.getSmartRules2()==21 ||
                        deviceDetails.getSmartRules2()==22){%>
                    <option value="0">No Local Smart Rule Rule</option>
                    <option value="10">Switch ON <%=deviceDetails.getDevice2()%></option>
                    <option value="20" selected>Switch OFF <%=deviceDetails.getDevice2()%></option>
                <%} else { %>
                    <option value="0" selected>No Local Smart Rule Rule</option>
                    <option value="10">Switch ON <%=deviceDetails.getDevice2()%></option>
                    <option value="20">Switch OFF <%=deviceDetails.getDevice2()%></option>                
                <% } %>
            </select>
        </td></tr>
        <%} %>
        <tr><td colspan=2><input  name="submitButton" id="submitButton" value="Update Device Details" type="submit"/></td></tr>
	</table>
	<br><a class="w3-btn w3-ripple w3-red" href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_LIST_DEVICES%>'>Back to Devices List Page</a><br><br>
	</form>
    <%@include file="footer.jsp" %>
</body>
</html>
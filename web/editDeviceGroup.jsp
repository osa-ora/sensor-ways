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
<title>SensorWays IoT, Update Device Group</title>
        <link rel="stylesheet" href="css/w3.css">
        <link rel='stylesheet' href='css/style.css'>
        <link rel='stylesheet' href='css/style2.css'>
        <script src="js/ajax_check_notifications.js"></script>  
        <script>
            function enableContact(){
                document.forms[0].email.disabled = false;
                document.forms[0].mobile.disabled = false;                
            }
            function disableContact(){
                document.forms[0].email.value='';
                document.forms[0].mobile.value='';
                document.forms[0].email.disabled = true;
                document.forms[0].mobile.disabled = true;
            }
            function enableAlert(){
                document.forms[0].high_alert1.disabled = false;
                document.forms[0].high_alert2.disabled = false;
                document.forms[0].low_alert1.disabled = false;
                document.forms[0].low_alert2.disabled = false;                                
            }
            function disableAlert(){
                document.forms[0].high_alert1.value='';
                document.forms[0].high_alert2.value='';
                document.forms[0].low_alert1.value='';
                document.forms[0].low_alert2.value='';
                document.forms[0].high_alert1.disabled = true;
                document.forms[0].high_alert2.disabled = true;
                document.forms[0].low_alert1.disabled = true;
                document.forms[0].low_alert2.disabled = true;                
            }
        </script>
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
        &nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > <a href='DeviceGroupServlet?action=100'>Device Groups</a> > Update Device Group<br>
        &nbsp;You can update the device group name or contact information for all devices in this group.
	<br>
	<form method="post" action="DeviceGroupServlet">
        <input type="hidden" name="action" value="<%=IConstant.ACTION_WEB_SAVE_DEVICE_GROUP%>"/>
        <%
            DeviceGroup deviceGroup = null;
            if(session.getAttribute("DEVICE_GROUP")!=null){
                    deviceGroup = (DeviceGroup)session.getAttribute("DEVICE_GROUP");
            }
        %>        
	<table>
            <tr><td>Device Group Name</td><td><input type="text" name="name" size="20" maxlength="20" value="<%=deviceGroup.getName()%>" style="width: 200pt;"/></td></tr>
            <tr><td>Device Management Option for New Firmware (Note: Will be reflected in all devices in this Device Group)</td><td>
                <input type="radio" name="dev_manage" value="0" checked="checked">Wait for device next restart/login<br>
                <input type="radio" name="dev_manage" value="1">Wait for next restart/login but send notification to the device contact<br>
                <input type="radio" name="dev_manage" value="2">Restart Device to Apply it Immediately
            </td></tr>
            <tr><td colspan="2"><input style="width: 30px;" type="checkbox" name="contact" checked onclick="if(document.forms[0].contact.checked) enableContact(); else disableContact();"/> Update Notification Details (Note: Will be reflected in all devices in this Device Group)</td><td>
            </td></tr>
                <tr><td>All Devices Notification Email</td><td><input type="email" multiple name="email" size="70" maxlength="70" value=""/><br>(*) Can be multiple email addresses separated by ","</td></tr>
                <tr><td>All Devices Notification Mobile</td><td><input type="tel" name="mobile" size="20" maxlength="20" value=""/></td></tr>
            <tr><td colspan="2"><input style="width: 30px;" type="checkbox" name="alerts" checked onclick="if(document.forms[0].alerts.checked) enableAlert(); else disableAlert();"/> Update Alert Thresholds (Note: Will be reflected in all devices in this Device Group)</td><td>
            </td></tr>
                <tr><td>High Alert Threshold Value (for 1st Sensor)</td><td>
                        <input type="text" name="high_alert1" id="high_alert1" size="8" maxlength="8" value=""/>
                </td></tr>
                <tr><td>Low Alert Threshold Value (for 1st Sensor)</td><td>
                        <input type="text" name="low_alert1" id="low_alert1" size="8" maxlength="8" value=""/>
                </td></tr>
                <tr><td>High Alert Threshold Value 2 (for 2nd Sensor)</td><td>
                        <input type="text" name="high_alert2" id="high_alert2" size="8" maxlength="8" value=""/>
                </td></tr>
                <tr><td>Low Alert Threshold Value 2 (for 2nd Sensor)</td><td>
                        <input type="text" name="low_alert2" id="low_alert2" size="8" maxlength="8" value=""/>
            </td></tr>        
	<tr><td colspan=2><input value="Update Device Group" type="submit"/></td></tr>
	</table>
	<br><a class="w3-btn w3-ripple w3-red" href='DeviceGroupServlet?action=100'>Back to Device Group List Page</a>
	</form>
    <%@include file="footer.jsp" %>
</body>
</html>
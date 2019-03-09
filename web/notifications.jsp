<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.TimeZone"%>
<%@page import="osa.ora.iot.db.beans.TenantSettings"%>
<%@page import="osa.ora.iot.db.beans.IdentityNotifications"%>
<%@page import="osa.ora.iot.db.beans.Scheduler"%>
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
List<IdentityNotifications> identityNotificationList = null;
identityNotificationList = (List<IdentityNotifications>)session.getAttribute("NOTIFICATION_LIST");
TenantSettings setting= (TenantSettings) session.getAttribute("USER_SETTINGS");
DateFormat formatter = new SimpleDateFormat("EEE dd MMM HH:mm:ss");
formatter.setTimeZone(TimeZone.getTimeZone(setting.getTimezone()));
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SensorWays IoT, Alert Notification List Page</title>
<style>
	table {width: 100%;}
	th, td {text-align: left;padding: 8px;}
	tr:nth-child(even){background-color: #f2f2f2}
	th {background-color: #4CAF50;color: white;}
</style>
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
        &nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > Notifications
        <h3>&nbsp;List of Alert Notifications</h3><br>
        <% if(user.getUserRole()==IConstant.ROLE_READ_WRITE) { %>                
            <a class="w3-btn w3-ripple w3-red" onclick="return confirm('Confirm Delete ALL READ Alert Notification?');" href='NotificationServlet?action=<%=IConstant.ACTION_WEB_DELETE_READ_NOTIFICATIONS%>'>Delete All Read Notification Alerts</a> 
            <a class="w3-btn w3-ripple w3-red" onclick="return confirm('Confirm Delete ALL Alert Notification?');" href='NotificationServlet?action=<%=IConstant.ACTION_WEB_DELETE_ALL_NOTIFICATIONS%>'>Delete All Notification Alerts</a> 
	<% } %>        
        <form method="post" action="NotificationServlet">
            <input type="hidden" name="action" value="<%=IConstant.ACTION_WEB_LIST_NOTIFICATIONS%>"/>
                &nbsp;&nbsp;Alert Type <select name="typeId"/>
                <option value="10">Alert Notification</option>
                <option value="14">Device Fault Notification</option>
                <option value="29">User Registration Confirmation</option>
                <option value="42">Device Registration Confirmation</option>
                <option value="43">Device Update Confirmation</option>
                <option value="45">Device Online Notification</option>
                <option value="46">Device Offline Notification</option>
                <option value="55">Device Update Confirmation</option>
                <option value="62">Account Update Confirmation</option>
                <option value="65">Device New Firmware Notification</option>
            </select>
            <input type="submit" value="Refresh" />
        </form>
	<h3>&nbsp;Total Displayed Alerts <%=identityNotificationList.size()%></h3>
        <table>
	<tr><th>Title</th><th>Body</th><th>Alert Time</th><th>Actions</th></tr>
	<%
	for (int n = 0; n < identityNotificationList.size(); n++) {
            IdentityNotifications identityNotification = identityNotificationList.get(n);
                String body = IoTUtil.getEmailBodyLanguage(identityNotification.getTemplate(), setting.getAlertEmailLanguage());
                String subject = IoTUtil.getEmailSubjectLanguage(identityNotification.getTemplate(), setting.getAlertEmailLanguage());            
            %>
            <tr><td><%=subject%>
                </td><td><%=IoTUtil.applyParameters(body, identityNotification.getParams())%>
            </td><td><%=formatter.format(identityNotification.getNotifiedOn())%>
            </td><td><%
            String actionClick = "NotificationServlet?notificationId=" + identityNotification.getId()+ "&action=";
            if(identityNotification.getReadFlag()==1){%>
                <a style="width: 200px" class="w3-btn w3-ripple w3-red" href="<%=actionClick + IConstant.ACTION_WEB_UNREAD_NOTIFICATION%>">Mark Unread</a><br>
            <%} else {%>
                <a style="width: 200px" class="w3-btn w3-ripple w3-green" href="<%=actionClick + IConstant.ACTION_WEB_READ_NOTIFICATION%>">Mark Read</a><br>
            <% }%>
                <a style="width: 200px" class="w3-btn w3-ripple w3-teal" onclick="return confirm('Confirm Delete this Alert Notification?');" href="<%=actionClick + IConstant.ACTION_WEB_DELETE_NOTIFICATION%>">Delete Notification</a></td>
            </td></tr>
	<%}%>
        </table>
        <a class="w3-btn w3-ripple w3-red" href='main.jsp'>Back to Main Menu</a><br><br>
    <%@include file="footer.jsp" %>
</body>
</html>
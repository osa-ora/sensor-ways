<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.TimeZone"%>
<%@page import="osa.ora.iot.db.beans.TenantSettings"%>
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
TenantSettings setting= (TenantSettings) session.getAttribute("USER_SETTINGS");
DateFormat formatter = new SimpleDateFormat("EEE dd MMM HH:mm");
formatter.setTimeZone(TimeZone.getTimeZone(setting.getTimezone()));
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SensorWays IoT, Scheduler List Page</title>
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
        &nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > Manage Schedulers
        <h3>&nbsp;List of Schedulers</h3>
	<table>
	<tr><th>Id</th><th>Name</th><th>Scheduler Icon</th><th>Scheduler Time</th>
	<th>Target Device</th><th>Target Action</th><th>Status</th><th>Last Execution Time</th><th>Action</th></tr>
	<%
        List<Scheduler> schedulerList = null;
	if(session.getAttribute("SCHEDULER_LIST")!=null){
		schedulerList = (List<Scheduler>)session.getAttribute("SCHEDULER_LIST");
	for (int n = 0; n < schedulerList.size(); n++) {
            Scheduler scheduler = schedulerList.get(n);
            %>
            <tr><td><%=scheduler.getId()%>
            </td><td><%=scheduler.getSchedulerName()%>
            </td><td><%=IoTUtil.getDisplayTriggerIcon(scheduler.getTriggeringDay(),scheduler.getTriggeringHour())%>
            </td><td><%=IoTUtil.getDisplayTrigger(scheduler.getTriggeringDay(),scheduler.getTriggeringHour(),scheduler.getTriggeringMin())%>
            </td><td><%=scheduler.getTargetDevice().getFriendlyName()%>
            </td><td><%=IoTUtil.getDisplayTargetAction(scheduler.getTargetAction(),scheduler.getTargetDevice().getDevice1(),scheduler.getTargetDevice().getDevice2())%>
            </td><td><%=IoTUtil.getDisplayDeviceStatus(scheduler.getSchedulerStatus())%>
            </td><td><%=formatter.format(scheduler.getUpdateTime())%>
            </td><td><%
            String actionClick = "SchedulerServlet?schedulerId=" + scheduler.getId()+ "&action=";
            if(user.getUserRole()==IConstant.ROLE_READ_WRITE) { 
                if(scheduler.getSchedulerStatus()==1){%>
                    <a style="width: 200px" class="w3-btn w3-ripple w3-red" href="<%=actionClick + IConstant.ACTION_WEB_INACTIVATE_SCHEDULER%>">Deactivate Scheduler</a><br>
                <%} else {%>
                    <a style="width: 200px" class="w3-btn w3-ripple w3-green" href="<%=actionClick + IConstant.ACTION_WEB_ACTIVATE_SCHEDULER%>">Activate Scheduler</a><br>
                <% }%>
                    <a style="width: 200px" class="w3-btn w3-ripple w3-teal" onclick="return confirm('Confirm Delete this Scheduler?');" href="<%=actionClick + IConstant.ACTION_WEB_DELETE_SCHEDULER%>">Delete Scheduler</a></td>
            <%}%>
            </td></tr>
	<%}
        }%>
        </table>
        <% if(user.getUserRole()==IConstant.ROLE_READ_WRITE && user.getSchedulerCreation()==1 && (schedulerList==null || schedulerList.size()<50)) { %>
        <br><a class="w3-btn w3-ripple w3-red" href='scheduler.jsp'>Create New Scheduler</a>
        <%} %>
        <a class="w3-btn w3-ripple w3-red" href='main.jsp'>Back to Main Menu</a><br><br>
    <%@include file="footer.jsp" %>
</body>
</html>
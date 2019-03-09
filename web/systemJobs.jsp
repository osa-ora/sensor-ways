<%@page import="osa.ora.iot.db.beans.SystemJobs"%>
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
<title>SensorWays IoT, System Jobs List Page</title>
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
        &nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > System Jobs List
        <h3>&nbsp;List of System Jobs</h3>
	<table>
	<tr><th>Id</th><th>Name</th><th>Parameter(s)</th><th>Status</th><th>Affected Rows (last execution)</th><th>Last Execution Time</th>
	<th>Last Execution Results</th><th>Error Message</th><th>Action</th></tr>
	<%
        List<SystemJobs> schedulerList = null;
	if(session.getAttribute("JOBS_LIST")!=null){
		schedulerList = (List<SystemJobs>)session.getAttribute("JOBS_LIST");
	for (int n = 0; n < schedulerList.size(); n++) {
            SystemJobs scheduler = schedulerList.get(n);
            %>
            <tr><td><%=scheduler.getId()%>
            </td><td><%=scheduler.getName()%>
            </td><td><%=scheduler.getParam1Value()==null?"N/A":scheduler.getParam1Value()+" "+scheduler.getParam1Type()%>
            </td><td><%=IoTUtil.getDisplayDeviceStatus(scheduler.getStatus())%>
            </td><td><%=scheduler.getLastExecutionRows()==null?"N/A":scheduler.getLastExecutionRows()%>
            </td><td><%=scheduler.getLastExecution()==null?"N/A":formatter.format(scheduler.getLastExecution())%>
            </td><td><%=scheduler.getLastExecutionResults()==null?"N/A":IoTUtil.getDisplayResults(scheduler.getLastExecutionResults())%>
            </td><td><%=scheduler.getExecutionLog()==null?"N/A":scheduler.getExecutionLog()%>
            </td><td><%
            String actionClick = "IoTWebServlet?jobId=" + scheduler.getId()+ "&action=";
            if(scheduler.getStatus()==1){%>
                <a style="width: 200px" class="w3-btn w3-ripple w3-red" onclick="return confirm('Confirm Disable this Job?');" href="<%=actionClick + IConstant.ACTION_WEB_DISABLED_SYS_JOBS%>">Disable Job</a><br>
            <%} else {%>
                <a style="width: 200px" class="w3-btn w3-ripple w3-green" href="<%=actionClick + IConstant.ACTION_WEB_ENABLED_SYS_JOBS%>">Enable Job</a><br>
            <% }%>
            </td></tr>
	<%}
        }%>
        </table>
        <a class="w3-btn w3-ripple w3-red" href='main.jsp'>Back to Main Menu</a><br><br>
    <%@include file="footer.jsp" %>
</body>
</html>
<%@page import="osa.ora.iot.db.beans.TenantSettings"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.TimeZone"%>
<%@page import="osa.ora.iot.db.beans.Simulations"%>
<%@page import="osa.ora.iot.db.beans.Workflows"%>
<%@page import="java.util.ArrayList"%>
<%@page import="osa.ora.iot.beans.IConstant"%>
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
<title>SensorWays IoT, Simulation List Page</title>
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
	&nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > Manage Simulations
        <h3>&nbsp;List of Simulations</h3>
	<table>
	<tr><th>Name</th><th>Device Id</th><th>Device Name</th><th>Status</th><th>Duration</th><th>Alert Probability</th><th>Error Probability</th><th>Action</th></tr>
	<%
        List<Simulations>simulationList = null;
	if(session.getAttribute("SIMULATION_LIST")!=null){
		simulationList = (List<Simulations>)session.getAttribute("SIMULATION_LIST");
	for (int n = 0; n < simulationList.size(); n++) {
            Simulations simulation = simulationList.get(n);
            %>
            <tr><td><%=simulation.getName()%>
            </td><td><%=simulation.getDeviceId()%>
            </td><td><%=simulation.getDevice().getFriendlyName()%>
            </td><td><%=IoTUtil.getDisplaySimulationStatus(simulation.getSimulationStatus())%>
            </td><td><%=simulation.getDuration()%> Minute(s)
            </td><td><%=simulation.getAlertProbability()%>
            </td><td><%=simulation.getErrorProbanility()%>
            </td><td><%
            String actionClick = "SimulationServlet?simulationId=" + simulation.getId()+ "&action=";
            if(user.getUserRole()==IConstant.ROLE_READ_WRITE) { 
                if(simulation.getSimulationStatus()==1){%>
                    <a style="width: 200px" class="w3-btn w3-ripple w3-red" href="<%=actionClick + IConstant.ACTION_WEB_STOP_SIMULATION%>">Stop Simulation</a><br>
                <%} else {%>
                    <a style="width: 200px" class="w3-btn w3-ripple w3-green" href="<%=actionClick + IConstant.ACTION_WEB_START_SIMULATION%>">Start Simulation</a><br>
                <% }%>
                    <a style="width: 200px" class="w3-btn w3-ripple w3-teal" onclick="return confirm('Confirm Delete this Simulation?');" href="<%=actionClick + IConstant.ACTION_WEB_STOP_AND_DELETE_SIMULATION%>">Delete Simulation</a></td>
            <%}%>
            </td></tr>
	<%}
        }%>
        </table>
        <% if(user.getUserRole()==IConstant.ROLE_READ_WRITE && user.getWorkflowCreation()==1 && (simulationList==null || simulationList.size()<5)) { %>
        <br><a class="w3-btn w3-ripple w3-red" href='SimulationServlet?action=<%=IConstant.ACTION_WEB_CREATE_SIMULATION%>'>Create New Simulation</a>
        <%} %>
        <a class="w3-btn w3-ripple w3-red" href='main.jsp'>Back to Main Menu</a><br><br>
    <%@include file="footer.jsp" %>
</body>
</html>
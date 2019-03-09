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
<title>SensorWays IoT, Create Device Simulator</title>
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
        &nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > <a href='SimulationServlet?action=<%=IConstant.ACTION_WEB_LIST_SIMULATION%>'>Manage Simulations</a> > Add New Simulator<br>
        &nbsp;You can build simulator for any device that simulate the runtime environment so you can predict the response and integration flows.
	<br>
	<form method="post" action="SimulationServlet">
        <input type="hidden" name="action" value="<%=IConstant.ACTION_WEB_SAVE_SIMULATION%>"/>
	<table>
            <tr><td>Simulator Name</td><td><input type="text" name="name" size="20" maxlength="20" value="" style="width: 200pt;"/></td></tr>
            <tr><td>IoT Device</td>
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
        <tr><td>Alerts Probability</td><td>
                <select name="alert" style="width: 200pt;">
                    <option value="0.1" selected>0.1</option>
                    <option value="0.2">0.2</option>
                    <option value="0.3">0.3</option>
		</select>
	</td></tr>
        <tr><td>Errors Probability</td><td>
                <select name="error" style="width: 200pt;">
                    <option value="0.01">0.01</option>
                    <option value="0.05">0.05</option>
                    <option value="0.1">0.1</option>
		</select>
	</td></tr>
        <tr><td>Duration</td><td>
                <select name="duration" style="width: 200pt;">
                    <option value="30">30 Minutes</option>
                    <option value="60">60 Minutes</option>
                    <option value="90">90 Minutes</option>
		</select>
	</td></tr>
        <tr><td>Initial Status</td><td>
                <select name="status">
		  <option value="2">Stopped</option>
		</select>
	</td></tr>
	<tr><td colspan=2><input value="Create Simulation" type="submit"/></td></tr>
	</table>
	<br><a class="w3-btn w3-ripple w3-red" href='SimulationServlet?action=<%=IConstant.ACTION_WEB_LIST_SIMULATION%>'>Back to Simulations List Page</a>
	</form>
    <%@include file="footer.jsp" %>
</body>
</html>
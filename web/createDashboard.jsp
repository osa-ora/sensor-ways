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
String notificationCount=(String)session.getAttribute("NOTIFICATION_COUNT");
if(user.getDashboardCreation()==0) { 
    response.sendRedirect("error.jsp");
    return;
}
    TenantSettings setting= (TenantSettings) session.getAttribute("USER_SETTINGS");
    DateFormat formatter = new SimpleDateFormat("EEE dd MMM HH:mm");
    formatter.setTimeZone(TimeZone.getTimeZone(setting.getTimezone()));

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SensorWays IoT, Build Dashboard</title>
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
	<br>&nbsp;You can build your own customized dashboard to monitor data from different connected IoT devices in your account.
	<form method="post" action="DashboardServlet">
        <input type="hidden" name="action" value="38"/>
	<table>
            <tr><td>Dashboard Details</td><td></td></tr>
            <tr><td>Dashboard Type</td>
                <td>
                    <input value="1" alt="2" name="layout" type="radio" onclick="document.forms[0].source3.disabled=true;document.forms[0].source4.disabled=true;"><img src="images/layout1.png"></input>
                    <input value="2" alt="4" name="layout" checked type="radio" onclick="document.forms[0].source3.disabled=false;document.forms[0].source4.disabled=false;"><img src="images/layout2.png"></input>
                </td>
            </tr>
            <tr><td>First IoT Device</td>
            <td><select name="source1" style="width: 200pt;">
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
        <tr><td>Second IoT Device</td>
            <td><select name="source2" style="width: 200pt;">
	<%
            for (int i = 0; i < deviceList.size(); i++) {
                %> <option value="<%=deviceList.get(i).getDeviceId() %>"><%=deviceList.get(i).getFriendlyName()%></option>
            <% } %>
            </select>     
            </td></tr>
        <tr><td>Third IoT Device</td>
            <td><select name="source3" style="width: 200pt;">
	<%
            for (int i = 0; i < deviceList.size(); i++) {
                %> <option value="<%=deviceList.get(i).getDeviceId() %>"><%=deviceList.get(i).getFriendlyName()%></option>
            <% } %>
            </select>     
            </td></tr>
        <tr><td>Fourth IoT Device</td>
            <td><select name="source4" style="width: 200pt;">
	<%
            for (int i = 0; i < deviceList.size(); i++) {
                %> <option value="<%=deviceList.get(i).getDeviceId() %>"><%=deviceList.get(i).getFriendlyName()%></option>
            <% } %>
            </select>     
            </td></tr>
	<tr><td colspan=2><input value="Create Dashboard" type="submit"/></td></tr>
	</table>
	<br><a class="w3-btn w3-ripple w3-red" href='main.jsp'>Back to Main Menu Page</a>
	</form>
    <%@include file="footer.jsp" %>
</body>
</html>
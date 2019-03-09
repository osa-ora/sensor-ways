<%@page import="osa.ora.iot.db.beans.TenantSettings"%>
<%@page import="javax.ejb.EJB"%>
<%@page import="osa.ora.iot.db.session.UserSessionBean"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.TimeZone"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="osa.ora.iot.beans.IConstant"%>
<%@page import="java.util.Date"%>
<%@page import="osa.ora.iot.db.beans.Messages"%>
<%@page import="osa.ora.iot.util.IoTUtil"%>
<%@page import="osa.ora.iot.db.beans.Users"%>
<%@page import="osa.ora.iot.db.beans.Devices"%>
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
TenantSettings setting= (TenantSettings) session.getAttribute("USER_SETTINGS");
String notificationCount=(String)session.getAttribute("NOTIFICATION_COUNT");
DateFormat formatter = new SimpleDateFormat("EEE dd MMM HH:mm");
formatter.setTimeZone(TimeZone.getTimeZone(setting.getTimezone()));
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SensorWays IoT, Device Registration Confirmation Page</title>
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
	&nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > <a href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_LIST_DEVICES%>'>Device List</a> > Device Registration Confirmation
        <h3>&nbsp;Device Registration Confirmation</h3><br>
        <br>
        &nbsp;&nbsp;Device Successfully Registered, please use the device Id <b><%=session.getAttribute("NEW_DEVICE_ID")%></b> with the 
        provided password <b><%=session.getAttribute("NEW_DEVICE_PASSWORD")%></b> to connect your IoT device to the Platform.
        <br>&nbsp;&nbsp;The password cannot retrieved back from the system as it is stored encrypted to please remember it or you won't be able to connect the device to the platform.
        <br><br>
        <%
            session.removeAttribute("NEW_DEVICE_ID");
            session.removeAttribute("NEW_DEVICE_PASSWORD");
        %>
	<br><a class="w3-btn w3-ripple w3-red" href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_LIST_DEVICES%>'>Back to Device List Page</a><br><br>
    <%@include file="footer.jsp" %>
</body>
</html>
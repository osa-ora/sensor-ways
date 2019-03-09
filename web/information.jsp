<%@page import="osa.ora.iot.beans.IConstant"%>
<%@page import="osa.ora.iot.db.beans.Users"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String userName="Guest";
String notificationCount="0";
if(session.getAttribute("USER")!=null) {
    Users user = (Users) session.getAttribute("USER");
    notificationCount=(String)session.getAttribute("NOTIFICATION_COUNT");
    userName=user.getUsername();
}
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SensorWays IoT - Information Page</title>
        <link rel="stylesheet" href="css/w3.css">
        <link rel='stylesheet' href='css/style.css'>
<% if(session.getAttribute("USER")!=null) { %>            
        <script src="js/ajax_check_notifications.js"></script>  
<% } %>
    </head>
    <body>
        <div class="navbar">
            <a href="index.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/home.png"> Home Page</a>
            <a class="active" href="information.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/humidity.png"> Supported Devices</a>
            <a href="contact.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/notification.png"> Contact Us</a>
<% if(session.getAttribute("USER")==null) { %>            
            <a href="login.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/members.png"> Members Login</a>
<% } else { %>
            <a href="main.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/menu.png"> Main Menu</a>
            <a href="NotificationServlet?action=<%=IConstant.ACTION_WEB_LIST_NOTIFICATIONS%>"><i style="float:right;"></i><img src="images/alert.png" style="width: 30px; height: 25px;"> Alerts <span id="total">(<%=notificationCount%>)</span></a>
            <a href="LogoutServlet"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/members.png"> Logout</a>
<% } %>
        </div> 
                &nbsp;&nbsp;<h3>&nbsp;Welcome <%=userName%></h3>
        <br>
        <form method="post" action="Login">
            <table>
                <tr><td>Plenty of devices are available for selection plus ability to customize any devices based on user's requirements</td></tr>
                <tr><td>These devices can come with different models either sensor only or with some switch control to control different devices as well.</td></tr>
                <tr><td></td><td></td></tr>               
                <tr><td><ul>
                    <li> Temperature & Humidity Device</li>
                    <li> Gas Leakage Sensor Device</li>
                    <li> Fire & Smoke Detection Device</li>                    
                    <li> Dust-Pollution Sensor Device</li>
                    <li> Water-Quality Sensor Device</li>
                    <li> Door/Window Open/Close Sensor Device</li>
                    <li> Motion Detection Sensor Device</li>
                    <li> RFID-based Device</li>
                    <li> Access Control Device</li>
                    <li> Custom Tailored Device</li>
                  </ul>
                </td><td></td></tr>
                <tr><td>Can be operated by direct electricity or battery or even solar power according to the device location.</td><td></td></tr>
                <tr><td>Devices can be upgraded easily being very dynamic from both H/W and S/W.<br> - H/W is upgraded by adding/changing/removing different attached components.<br> - While S/W upgrade use Over The Air (OTA) upgrade automatically from the IoT platform the devices pick the new firmware versions automatically once available.</td><td></td></tr>
            </table>
        </form>
        <%@include file="footer.jsp" %>
    </body>
</html>
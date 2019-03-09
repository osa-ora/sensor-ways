<%@page import="osa.ora.iot.db.beans.Applications"%>
<%@page import="osa.ora.iot.db.beans.DeviceGroup"%>
<%@page import="osa.ora.iot.db.beans.TenantSettings"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.TimeZone"%>
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
<title>SensorWays IoT, Applications List Page</title>
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
	&nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > Application List
        <h3>&nbsp;List of Applications</h3>
	<table>
	<tr><th>Id</th><th>Name</th><th>Status</th><th>Type</th><th>Action</th></tr>
	<%
        List<Applications> applicationList = null;
	if(session.getAttribute("APP_LIST")!=null){
		applicationList = (List<Applications>)session.getAttribute("APP_LIST");
	for (int n = 0; n < applicationList.size(); n++) {
            Applications app = applicationList.get(n);
            %>
            <tr><td><%=app.getId()%>
            </td><td><%=app.getName()%>
            </td><td><%=IoTUtil.getDisplayAppStatus(app.getStatus())%>
            </td><td><%=IoTUtil.getDisplayAppType(app.getApplicationType())%>
            </td><td><%
            String actionClick = "IoTAppServlet?appId=" + app.getId()+ "&action=";%>
            <a style="width: 200px" class="w3-btn w3-ripple w3-yellow" href="<%=actionClick + IConstant.ACTION_WEB_LUNCH_APPLICATION%>">Lunch Application</a><br>
            <%if(user.getUserRole()==IConstant.ROLE_READ_WRITE) {  %>
                <% if(app.getStatus()==1){ %>
                    <a style="width: 200px" class="w3-btn w3-ripple w3-red" href="<%=actionClick + IConstant.ACTION_WEB_INACTIVATE_APPLICATION%>">Stop Application</a><br>
                <%} else { %>
                    <a style="width: 200px" class="w3-btn w3-ripple w3-green" href="<%=actionClick + IConstant.ACTION_WEB_ACTIVATE_APPLICATION%>">Start Application</a><br>
                <% } %>
                <a style="width: 200px" class="w3-btn w3-ripple w3-yellow" href="<%=actionClick + IConstant.ACTION_WEB_UPDATE_APPLICATION%>">Update Application</a><br>
                <a style="width: 200px" class="w3-btn w3-ripple w3-teal" onclick="return confirm('Confirm Delete this Group?');" href="<%=actionClick + IConstant.ACTION_WEB_DELETE_APPLICATION%>">Delete Application</a></td>
            <%}%>
            </td></tr>
	<%}
        }%>
        </table>
        <% if(user.getUserRole()==IConstant.ROLE_READ_WRITE  && (applicationList==null || applicationList.size()<50)) { %>
        <br><a class="w3-btn w3-ripple w3-red" href='createApp.jsp'>Create New Application</a>
        <%} %>
        <a class="w3-btn w3-ripple w3-red" href='main.jsp'>Back to Main Menu</a><br><br>
    <%@include file="footer.jsp" %>
</body>
</html>
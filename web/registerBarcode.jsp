<%@page import="osa.ora.iot.db.beans.TenantSettings"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.TimeZone"%>
<%@page import="osa.ora.iot.db.beans.DeviceModel"%>
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
if(user.getUserRole()!=IConstant.ROLE_READ_WRITE || user.getSystemAdmin()==0) { 
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
<title>SensorWays IoT, Register Barcode</title>
        <link rel="stylesheet" href="css/w3.css">
        <link rel='stylesheet' href='css/style.css'>
        <link rel='stylesheet' href='css/style2.css'>
        <script src="js/ajax_check_notifications.js"></script>  
        <script src="js/ajax_check_barcode2.js"></script>  
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
        &nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > Add Bar code<br>
        &nbsp;Bar codes must be mapped to specific device model.
        <br>
	<form method="post" action="IoTWebServlet">
        <input type="hidden" name="action" value="<%=IConstant.ACTION_WEB_SAVE_BARCODE%>"/>
	<table>
	<tr><td>Bar Code Value</td><td>
                <input type="text" name="barcode" id="barcode" size="15" maxlength="15" value=""  onchange="isBarCodeCorrect();"/>
                <img id="flag" name="flag" src="images/not_allowed.png"/>
        </td></tr>
	<tr><td>Device Model</td><td>
                <select name="model" id="model" onchange="validateSeclection();">
                    <% 
                         List<DeviceModel> deviceModelList = (List<DeviceModel>)session.getAttribute("DEVICE_MODELS"); 
                         for(int i=0;i<deviceModelList.size();i++){
                             out.println("<option value='"+deviceModelList.get(i).getId()+"'>"+deviceModelList.get(i).getDeviceName()+"</option>");
                         }

                    %>
		</select>
	</td></tr>
	<tr><td colspan=2><input  name="submitButton" id="submitButton"  disabled="true" value="Register Barcode" type="submit"/></td></tr>
	</table>
	<br><a class="w3-btn w3-ripple w3-red" href='main.jsp'>Back to Main Menu</a><br><br>
	</form>
    <%@include file="footer.jsp" %>
</body>
</html>
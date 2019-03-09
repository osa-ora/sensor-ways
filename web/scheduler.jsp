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
    if(user.getUserRole()!=IConstant.ROLE_READ_WRITE || user.getSchedulerCreation()==0) { 
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
<title>SensorWays IoT, Build Scheduler</title>
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
        &nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > <a href='SchedulerServlet?action=22'>Manage Schedulers</a> > Register New Scheduler<br>
        &nbsp;You can build subsequent actions as per different schedules in the system.
	<br>
	<form method="post" action="SchedulerServlet">
        <input type="hidden" name="action" value="23"/>
	<table>
            <tr><td>Scheduler Name</td><td>
                <input type="text" name="name" size="20" maxlength="20" value="" style="width: 200pt;"/>
                <select name="suggested" onchange="document.forms[0].name.value=this.value;">
		  <option value="Wake Up Time">Wake Up Time</option>
		  <option value="Sleep Time">Sleep Time</option>
                  <option value="Back to Work">Back to Work</option>
		  <option value="Breakfast Time">Breakfast Time</option>
                  <option value="Lunch Time">Lunch Time</option>
                  <option value="Dinner Time">Dinner Time</option>
                </select>                    
            </td></tr>
        <tr><td>Time</td><td>
                <select name="hour" style="width: 95pt;">
                  <option value="1">1</option>
		  <option value="2">2</option>
                  <option value="3">3</option>
                  <option value="4">4</option>
                  <option value="5">5</option>
                  <option value="6">6</option>
                  <option value="7">7</option>
                  <option value="8">8</option>
                  <option value="9">9</option>
                  <option value="10">10</option>
                  <option value="11">11</option>
                  <option value="12">12</option>
                  <option value="13">13</option>
                  <option value="14">14</option>
                  <option value="15">15</option>
                  <option value="16">16</option>
                  <option value="17">17</option>
                  <option value="18">18</option>
                  <option value="19">19</option>
                  <option value="20">20</option>
                  <option value="21">21</option>
                  <option value="22">22</option>
                  <option value="23">23</option>
                  <option value="00">00</option>
		</select>
                <select name="min" style="width: 95pt;">
                  <option value="00">00</option>
		  <option value="15">15</option>
                  <option value="30">40</option>
		  <option value="45">45</option>
                </select></td></tr>
        <tr><td>Day of week</td><td>
                <select name="day" style="width: 200pt;">
                  <option value="0">All Days</option>
                  <option value="1">Saturday</option>
		  <option value="2">Sunday</option>
                  <option value="3">Monday</option>
                  <option value="4">Tuesday</option>
                  <option value="5">Wednesday</option>
                  <option value="6">Thursday</option>
                  <option value="7">Friday</option>
		</select>
	</td></tr>
        <tr><td></td><td colspan="2"><img src="images/green_arrow.png"/></td></tr>
        <tr><td>Target IoT Device</td>
            <td><select name="target" style="width: 200pt;">
            <%
            List<Devices> deviceList = null;
            if(session.getAttribute("DEVICE_LIST")!=null){
                    deviceList = (List<Devices>)session.getAttribute("DEVICE_LIST");
            }else{
                    deviceList = new ArrayList<Devices>(); 
            }
            for (int i = 0; i < deviceList.size(); i++) {
                if(deviceList.get(i).getDeviceModel()>IConstant.ONE_DEVICE_THRESHOLD){
                %> <option value="<%=deviceList.get(i).getDeviceId() %>"><%=deviceList.get(i).getFriendlyName()%></option>
            <%   }
            } %>
        </td></tr>
        <tr><td>Target Action</td><td>
                <select name="target_action" style="width: 200pt;">
                  <option value="1">Switch on device 1</option>
                  <option value="2">Switch off device 1</option>
                  <option value="6">Switch on device 2</option>
                  <option value="7">Switch off device 2</option>
		</select>
	</td></tr>
        <tr><td>Initial Status</td><td>
                <select name="status">
		  <option value="1">Active</option>
		  <option value="2">Inactive</option>
		</select>
	</td></tr>
	<tr><td colspan=2><input value="Create Schedule" type="submit"/></td></tr>
	</table>
	<br><a class="w3-btn w3-ripple w3-red" href='SchedulerServlet?action=22'>Back to Schedule List Page</a>
	</form>
    <%@include file="footer.jsp" %>
</body>
</html>
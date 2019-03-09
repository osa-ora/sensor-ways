<%@page import="osa.ora.iot.util.IoTUtil"%>
<%@page import="osa.ora.iot.db.beans.Applications"%>
<%@page import="osa.ora.iot.db.beans.DeviceGroup"%>
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
if(session.getAttribute("USER")==null || session.getAttribute("My_APP")==null) {
	response.sendRedirect("error.jsp");
        return;
}
Users user = (Users) session.getAttribute("USER");
    if(user.getUserRole()!=IConstant.ROLE_READ_WRITE || user.getWorkflowCreation()==0) { 
        response.sendRedirect("error.jsp");
        return;        
    }
    String notificationCount=(String)session.getAttribute("NOTIFICATION_COUNT");
TenantSettings setting= (TenantSettings) session.getAttribute("USER_SETTINGS");
DateFormat formatter = new SimpleDateFormat("EEE dd MMM HH:mm");
formatter.setTimeZone(TimeZone.getTimeZone(setting.getTimezone()));
Applications app=(Applications)session.getAttribute("My_APP");
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SensorWays IoT, Lunch <%=app.getName()%></title>
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
	&nbsp;&nbsp;<%=formatter.format(new Date())%>
        <center>
        &nbsp;<img src="ImageViewer?ID=<%=app.getId()%>" width="400" height="80"/>
        <div class="w3-panel w3-topbar w3-bottombar w3-border-blue w3-pale-blue">
            <p>Application Name:    <%=app.getName()%></p>
            <p>Total No of Devices: 20        Online Devices: 0         Total Messages: 3400        Total Alerts: 53</p>
        </div>
        </center>
        <table>
            <tr><td colspan="2">
        <%
	List<DeviceGroup> deviceGroupsList = null;
	if(session.getAttribute("My_APP_GROUPS")!=null){
		deviceGroupsList = (List<DeviceGroup>)session.getAttribute("My_APP_GROUPS");
	}else{
		deviceGroupsList = new ArrayList<DeviceGroup>(); 
	}%>
	<div class="w3-panel w3-border w3-border-black">
                <% for (int n=0;n<deviceGroupsList.size();n++){
                    List<Devices> deviceList=deviceGroupsList.get(n).getDeviceList();%>
                    <div class="w3-panel w3-border w3-border-red">
                        <font size="1">&nbsp;<b><%=deviceGroupsList.get(n).getName()%></b><br>
                    <%
                    for (int i = 0; i < deviceList.size(); i++) {
			String deviceId = deviceList.get(i).getDeviceId(); 
                        String actionClick = "IoTWebServlet?deviceId=" + deviceId + "&action=";
                        String boxColor="w3-blue-gray";//offline
                        if (deviceList.get(i).getOfflineFlag()==IConstant.STATUS_ONLINE){
                            //|| (deviceList.get(i).getLastPingTime()!=null && new Date().getTime() - deviceList.get(i).getLastPingTime().getTime() < 60000 ) { 
                            //will use the one minute to check for online and offline same as email
                            //setting.getPingInterval()*1000)+2000) { 
                            if(deviceList.get(i).getLastMessage()!=null && deviceList.get(i).getLastMessage().getType()>=2 && deviceList.get(i).getLastMessage().getType()<=6) boxColor="w3-red";
                            else boxColor="w3-green";
                        }
                    %>
                    <a style="width: 150px" class="w3-button <%=boxColor%> w3-ripple w3-border w3-border-white" href='<%=actionClick + IConstant.ACTION_WEB_LIST_DEVICE_MESSAGES%>'><%=IoTUtil.getDisplayIconForModel(deviceList.get(i))%>
                        <br><b><%=deviceList.get(i).getFriendlyName()%></b>
                        <br>[<%=deviceList.get(i).getLocation()%>]
                        <% if(deviceList.get(i).getLastMessage()!=null){ %>
                        <br><%=IoTUtil.getDisplayMessageTypeForWebMenu(deviceList.get(i).getLastMessage().getType())%> <font size="1"><%=IoTUtil.payloadExtractorForWebMenu(deviceList.get(i),deviceList.get(i).getLastMessage().getPayload(),deviceList.get(i).getLastMessage().getType())%></font>
                        <% }else{ %>
                            <br>No Messages
                        <% } %>
                    </a>&nbsp;
                <%} %>
                    </font></div>
            <% }%>
            </div>
        </td></tr>
	</table>
	<br><a class="w3-btn w3-ripple w3-red" href='IoTAppServlet?action=110'>Back to Applications List Page</a>
	</form>
    <%@include file="footer.jsp" %>
</body>
</html>
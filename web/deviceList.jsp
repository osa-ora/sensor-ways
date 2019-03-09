<%@page import="osa.ora.iot.db.beans.DeviceGroup"%>
<%@page import="osa.ora.iot.db.beans.TenantSettings"%>
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
<title>SensorWays IoT, Device List Page</title>
        <link rel="stylesheet" href="css/w3.css">
        <link rel='stylesheet' href='css/style.css'>
        <script src="js/ajax_check_notifications.js"></script>  
        <meta http-equiv="refresh" content="20" />
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
        &nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > Device List
        <br>
        <form method="post" action="IoTWebServlet">
            <input type="hidden" name="action" value="<%=IConstant.ACTION_WEB_LIST_DEVICES%>"/>
                &nbsp;&nbsp;Device Group <select name="group"/>
                <option value="-1">No Device Group</option>
                <%
                List<DeviceGroup> deviceGroupList = null;
                if(session.getAttribute("DEVICE_GROUP_LIST")!=null){
                    deviceGroupList = (List<DeviceGroup>)session.getAttribute("DEVICE_GROUP_LIST");
                    for (int n = 0; n < deviceGroupList.size(); n++) {
                        DeviceGroup deviceGroup = deviceGroupList.get(n);
                        %>
                    <option value="<%=deviceGroup.getId()%>"><%=deviceGroup.getName()%></option>
                <% }
                } %>
                </select>
                <input type="submit" value="Refresh" />
        </form>
        <%
	List<Devices> deviceList = null;
	if(session.getAttribute("DEVICE_LIST")!=null){
		deviceList = (List<Devices>)session.getAttribute("DEVICE_LIST");
	}else{
		deviceList = new ArrayList<Devices>(); 
	}%>
	<table>
                <tr><td>
                <% for (int i = 0; i < deviceList.size(); i++) {
			String deviceId = deviceList.get(i).getDeviceId(); 
                        String actionClick = "IoTWebServlet?deviceId=" + deviceId + "&action=";
                        String boxColor="w3-blue-gray";//offline
                        if (deviceList.get(i).getOfflineFlag()==IConstant.STATUS_ONLINE){
                            //|| (deviceList.get(i).getLastPingTime()!=null && new Date().getTime() - deviceList.get(i).getLastPingTime().getTime() < 60000 )) { 
                            //will use the one minute to check for online and offline same as email
                            //setting.getPingInterval()*1000)+2000) { 
                           if(deviceList.get(i).getLastMessage()!=null && deviceList.get(i).getLastMessage().getType()>=2 && deviceList.get(i).getLastMessage().getType()<=6) boxColor="w3-red";
                           else boxColor="w3-green";
                        }
                    %>
                    <a style="width: 300px" class="w3-button <%=boxColor%> w3-ripple" href='<%=actionClick + IConstant.ACTION_WEB_LIST_DEVICE_MESSAGES%>'><%=IoTUtil.getDisplayIconForModel(deviceList.get(i))%>
                        <br><b><%=deviceList.get(i).getFriendlyName()%></b>
                        <br>[<%=deviceList.get(i).getLocation()%>]
                        <br>"<%=IoTUtil.getDisplayDeviceStatus(deviceList.get(i).getDeviceStatus())%>"
                        <% if(deviceList.get(i).getLastPingTime()!=null) { 
					long timeSeconds=(new Date().getTime()-deviceList.get(i).getLastPingTime().getTime())/1000;
					out.print("<br><font size='1'>Last Seen "+IoTUtil.lastseen(timeSeconds)+"</font>"); 
                            }else{
                                        out.print("<br><font size='1'>Never Seen</font>");
                            }%>
                        <% if(deviceList.get(i).getLastMessage()!=null){ %>
                        <br><%=IoTUtil.getDisplayMessageTypeForWebMenu(deviceList.get(i).getLastMessage().getType())%> <font size="1"><%=IoTUtil.payloadExtractorForWebMenu(deviceList.get(i),deviceList.get(i).getLastMessage().getPayload(),deviceList.get(i).getLastMessage().getType())%></font>
                        <% }else{ %>
                            <br>No Messages
                        <% } %>
                    </a> 
                    </td>
                <% if((i+1)%3==0 && i!=0) {
                        %></tr><tr><td> <%
                   }else{
                        %><td> <%  
                    }
                }%>
                </td></tr>
		</table>
                <% if(user.getUserRole()==IConstant.ROLE_READ_WRITE && deviceList.size()<setting.getMaxDevices()) { %>
                <br><a class="w3-btn w3-ripple w3-red" href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_REGISTER_DEVICE%>'>Register New IoT Device</a>
                <%} %>
		<a href='main.jsp' class="w3-btn w3-ripple w3-red">Back to Main Menu</a><br><br>
    <%@include file="footer.jsp" %>
</body>
</html>
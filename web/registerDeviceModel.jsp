<%@page import="osa.ora.iot.db.beans.TenantSettings"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.TimeZone"%>
<%@page import="osa.ora.iot.db.beans.HwType"%>
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
if(user.getSystemAdmin()!=1){
    request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
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
<title>SensorWays IoT, Register New Device Model</title>
        <link rel="stylesheet" href="css/w3.css">
        <link rel='stylesheet' href='css/style.css'>
        <link rel='stylesheet' href='css/style2.css'>
        <script src="js/ajax_check_notifications.js"></script>  
        <script src="js/ajax_check_model.js"></script>  
</head>
<body onload="showHideSensors();">
        <div class="navbar">
            <a href="index.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/home.png"> Home Page</a>
            <a href="information.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/humidity.png"> Supported Devices</a>
            <a href="contact.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/notification.png"> Contact Us</a>
            <a href="main.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/menu.png"> Main Menu</a>
            <a href="NotificationServlet?action=<%=IConstant.ACTION_WEB_LIST_NOTIFICATIONS%>"><i style="float:right;"></i><img src="images/alert.png" style="width: 30px; height: 25px;"> Alerts <span id="total">(<%=notificationCount%>)</span></a>
            <a href="LogoutServlet"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/members.png"> Logout</a>
        </div> 
	&nbsp;&nbsp;<%=formatter.format(new Date())%><h3>&nbsp;Welcome <%=user.getUsername()%> [<i><font color="red"><%=setting.getIdentityName()%></font></i>]</h3>
        &nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > <a href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_MANAGE_MODELS%>'>Manage Device Models</a> > Register New Device Model<br>
        &nbsp;You can register new device models in the system by using this form, you need to upload the device firmware later from Update Firmware option.
	<br>
	<form method="post" action="IoTWebServlet">
        <input type="hidden" name="action" value="<%=IConstant.ACTION_WEB_SAVE_MODEL%>"/>
	<table>
	<tr><td>Device Model Name</td><td>
                <input type="text" name="name" id="name" size="30" maxlength="30" value="" onchange="isModelExist();"/>
                <img id="flag" name="flag" src="images/not_allowed.png"/>
        </td></tr>
	<tr><td>Device HW Type</td><td>
                <select name="hwVal" id="hwVal">
                    <% 
                         List<HwType> hwTypeList = (List<HwType>)session.getAttribute("HW_TYPES"); 
                         for(int i=0;i<hwTypeList.size();i++){
                             out.println("<option value='"+hwTypeList.get(i).getId()+"'>"+hwTypeList.get(i).getType()+"</option>");
                         }

                    %>
		</select>
	</td></tr>        
	<tr><td>Device Model Firmware Version</td><td>
            <select style="width: 300px;" name="version">
                <option value='0' selected>0</option>
                <option value='1'>1</option>
            </select>
	</td></tr>
        <tr><td>Device Sensors</td><td>
            <select style="width: 300px;" name="sensorsCount" id="sensorsCount" onchange="showHideSensors();">
                <option value='0'>0</option>
                <option value='1' selected>1</option>
                <option value='2'>2</option>
            </select>
	</td></tr>
        <tr><td>First Sensor Name</td><td>
                <input type="text" name="sensor1" size="30" maxlength="30" value=""/>
        </td></tr>
        <tr><td>First Sensor Values</td><td>
            <select style="width: 300px;" name="graph1">
                <option value='1' selected>Range of Values (1-100)</option>
                <option value='2'>Range of % (1-100)</option>
                <option value='3'>Range of values (0-1024)</option>
                <option value='4'>2-Values (On/Off)</option>
                <option value='5'>2-Values (Opened/Closed)</option>
                <option value='6'>Baseline & 1 Value</option>
            </select>
        </td></tr>
        <tr><td>High Alert Threshold Value (for 1st Sensor)</td><td>
                <input type="text" name="high_alert1" size="30" maxlength="30" value=""/>
        </td></tr>
        <tr><td>Low Alert Threshold Value (for 1st Sensor)</td><td>
                <input type="text" name="low_alert1" size="30" maxlength="30" value=""/>
        </td></tr>
        <tr><td>First Sensor Icon</td><td>
            <input type="radio" name="sensor_icon1" value="1" checked> <img src='images/temperature.png'/></input>
            <input type="radio" name="sensor_icon1" value="2"> <img src='images/humidity.png'/></input>
            <input type="radio" name="sensor_icon1" value="3"> <img src='images/motion.png'/></input>
            <input type="radio" name="sensor_icon1" value="4"> <img src='images/dust.png'/></input>
            <input type="radio" name="sensor_icon1" value="5"> <img src='images/fire.png'/></input>
            <br>
            <input type="radio" name="sensor_icon1" value="6"> <img src='images/smoke.png'/></input>
            <input type="radio" name="sensor_icon1" value="7"> <img src='images/alarm.png'/></input>
            <input type="radio" name="sensor_icon1" value="8"> <img src='images/gas.png'/></input>
            <input type="radio" name="sensor_icon1" value="9"> <img src='images/light.png'/></input>
            <input type="radio" name="sensor_icon1" value="10"> <img src='images/door.png'/></input>
            <br>            
            <input type="radio" name="sensor_icon1" value="11"> <img src='images/window.jpg'/></input>
            <input type="radio" name="sensor_icon1" value="12"> <img src='images/heart.png'/></input>
            <input type="radio" name="sensor_icon1" value="13"> <img src='images/parking.png'/></input>
            <input type="radio" name="sensor_icon1" value="14"> <img src='images/drops.png'/></input>
            <input type="radio" name="sensor_icon1" value="15"> <img src='images/no-image.png'/></input>
            <br>            
            <input type="radio" name="sensor_icon1" value="16"> <img src='images/rain.png'/></input>
            <input type="radio" name="sensor_icon1" value="17"> <img src='images/water.png'/></input>
            <input type="radio" name="sensor_icon1" value="18"> <img src='images/location.png'/></input>
        </td></tr>
        <tr name="second1" id="second1"><td>Second Sensor Name</td><td>
                <input type="text" name="sensor2" size="8" maxlength="8" value=""/>
        </td></tr>
        <tr name="second2" id="second2"><td>First Sensor Values</td><td>
            <select style="width: 300px;" name="graph2">
                <option value='1' selected>Range of Values (1-100)</option>
                <option value='2'>Range of % (1-100)</option>
                <option value='3'>Range of values (0-1024)</option>
                <option value='4'>2-Values (On/Off)</option>
                <option value='5'>2-Values (Opened/Closed)</option>
                <option value='6'>Baseline & 1 Value</option>
            </select>
        </td></tr>
        <tr name="second3" id="second3"><td>High Alert Threshold Value 2 (for 2nd Sensor)</td><td>
                <input type="text" name="high_alert2" size="8" maxlength="8" value=""/>
        </td></tr>
        <tr name="second4" id="second4"><td>Low Alert Threshold Value 2 (for 2nd Sensor)</td><td>
                <input type="text" name="low_alert2" size="8" maxlength="8" value=""/>
        </td></tr>
        <tr name="second5" id="second5"><td>Second Sensor Icon</td><td>
            <input type="radio" name="sensor_icon2" value="1" checked> <img src='images/temperature.png'/></input>
            <input type="radio" name="sensor_icon2" value="2"> <img src='images/humidity.png'/></input>
            <input type="radio" name="sensor_icon2" value="3"> <img src='images/motion.png'/></input>
            <input type="radio" name="sensor_icon2" value="4"> <img src='images/dust.png'/></input>
            <input type="radio" name="sensor_icon2" value="5"> <img src='images/fire.png'/></input>
            <br>
            <input type="radio" name="sensor_icon2" value="6"> <img src='images/smoke.png'/></input>
            <input type="radio" name="sensor_icon2" value="7"> <img src='images/alarm.png'/></input>
            <input type="radio" name="sensor_icon2" value="8"> <img src='images/gas.png'/></input>
            <input type="radio" name="sensor_icon2" value="9"> <img src='images/light.png'/></input>
            <input type="radio" name="sensor_icon2" value="10"> <img src='images/door.png'/></input>
            <br>
            <input type="radio" name="sensor_icon2" value="11"> <img src='images/window.jpg'/></input>
            <input type="radio" name="sensor_icon2" value="12"> <img src='images/heart.png'/></input>
            <input type="radio" name="sensor_icon2" value="13"> <img src='images/parking.png'/></input>
            <input type="radio" name="sensor_icon2" value="14"> <img src='images/drops.png'/></input>
            <input type="radio" name="sensor_icon2" value="15"> <img src='images/no-image.png'/></input>
            <br>            
            <input type="radio" name="sensor_icon2" value="16"> <img src='images/rain.png'/></input>
            <input type="radio" name="sensor_icon2" value="17"> <img src='images/water.png'/></input>
            <input type="radio" name="sensor_icon2" value="18"> <img src='images/location.png'/></input>
        </td></tr>
        <tr><td>Controls 0 Device</td><td>
                <input style="width: 300px;" type="checkbox" name="device0"/> Yes
        </td></tr>
        <tr><td>Controls 1 Device</td><td>
                <input style="width: 300px;" type="checkbox" name="device1"/> Supported
        </td></tr>
        <tr><td>Controls 2 Devices</td><td>
                <input style="width: 300px;" type="checkbox" name="device2"/> Supported
        </td></tr>
	<tr><td>Custom Action Name</td><td>
                <input type="text" name="custom_action" id="model" size="30" maxlength=30" value="N/A" disabled="true"/>
	</td></tr>
	<tr><td>Custom Action Value</td><td>
                <input type="number" name="custom_value" id="model" size="30" maxlength=30" value="0" disabled="true"/>
	</td></tr>
	<tr><td>Normal Message For Simulator</td><td>
                <input type="text" name="normal" size="30" maxlength=30"/>
	</td></tr>
	<tr><td>High Alert Message For Simulator</td><td>
                <input type="text" name="alert" size="30" maxlength=30"/>
	</td></tr>
	<tr><td>Error Message For Simulator</td><td>
                <input type="text" name="error" size="30" maxlength=30"/>
	</td></tr>
        <tr><td>Initial Status</td><td>
                <select style="width: 300px;" name="status">
                    <option value='1'>Active</option>
                    <option value='2'>Inactive</option>
		</select>
	</td></tr>        
	<tr><td colspan=2><input  name="submitButton" id="submitButton"  disabled="true" value="Register Device Model" type="submit"/></td></tr>
	</table>
	<br><a class="w3-btn w3-ripple w3-red" href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_MANAGE_MODELS%>'>Back to Device Models List Page</a><br><br>
	</form>
    <%@include file="footer.jsp" %>
</body>
</html>
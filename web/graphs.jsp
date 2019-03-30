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
    if (session.getAttribute("USER") == null) {
        response.sendRedirect("error.jsp");
        return;
    }
    Users user = (Users) session.getAttribute("USER");
    TenantSettings setting= (TenantSettings) session.getAttribute("USER_SETTINGS");
    String notificationCount=(String)session.getAttribute("NOTIFICATION_COUNT");
    DateFormat formatter = new SimpleDateFormat("EEE dd MMM HH:mm:ss");    
    formatter.setTimeZone(TimeZone.getTimeZone(setting.getTimezone()));
%>
<%
    Devices deviceDetails = null;
    List<Messages> messages = null;
    String type="";
    int sensorVal=1;
    if (session.getAttribute("DEVICE_DETAILS") != null) {
        deviceDetails = (Devices) session.getAttribute("DEVICE_DETAILS");
    }
    if (session.getAttribute("MESSAGES_LIST") != null) {
        messages = (List<Messages>) session.getAttribute("MESSAGES_LIST");
    }
    if (session.getAttribute("GRAPH") != null) {
        type = (String) session.getAttribute("GRAPH");
    }
    if (session.getAttribute("SENSOR") != null) {
        String sensor = (String) session.getAttribute("SENSOR");
        try{
            sensorVal= Integer.parseInt(sensor);
        }catch(Exception ex){
            out.print("Error in input data!!<br>");
        }
    }%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SensorWays IoT, Device Message Graph Page</title>
        <link rel="stylesheet" href="css/w3.css">
        <link rel='stylesheet' href='css/style.css'>
        <script src="js/ajax_check_notifications.js"></script>  
      <% 
            String auto_refresh="15";
            if(session.getAttribute("REFRESH")!=null){
                auto_refresh=(String)session.getAttribute("REFRESH");
            }
            boolean connected=false;
            if(session.getAttribute("CONNECT")!=null){
                connected=Boolean.parseBoolean((String)session.getAttribute("CONNECT"));
            }
        %>
        <meta http-equiv="refresh" content="<%=auto_refresh%>" />
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
        <table>
        <tr>
            <td>
            <h4>&nbsp;Message Graph for Device :<%=deviceDetails.getFriendlyName() %> (<%=deviceDetails.getLocation()%>)</h4>
            <canvas id="myCanvas" width="600" height="320" style="border:1px solid #c3c3c3;">
                Your browser does not support the canvas element.
            </canvas>                
            </td>
        </tr>
        <form method="post" action="IoTWebServlet">
        <input type="hidden" name="action" value="<%=IConstant.ACTION_WEB_MESSAGE_GRAPH_REFRESH%>"/>
        <tr><td>
            <img src="images/download.png" style="cursor: pointer" width="30px" onclick="exportCanvasAsPNG();" alt="Save as Image" />
            <label style="width: 120px;">Refresh Every:</label>
            <select name="refresh" onchange='if(this.value != 0) { this.form.submit(); }'/>
                <option value="0">Select Refresh Time</option>
                <option value="15">15 Seconds</option>
                <option value="30">30 Seconds</option>
                <option value="60">60 Seconds</option>
                <option value="300">300 Seconds</option>
            </select>
            <label style="width: 120px;">Graph Type:</label>
            <select name="connect" onchange='if(this.value != 0) { this.form.submit(); }'/>
                <option value="0">Select Graph Type</option>
                <option value="false">Input Sampling</option>
                <option value="true">Connected Line</option>
            </select>                
        </td></tr>
        <tr><td>
        </td></tr>
        </form>
</table>
<br><a class="w3-btn w3-ripple w3-red" href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_LIST_DEVICES%>'>Back to Device List Page</a><br><br>
<%@include file="footer.jsp" %>
</body>
</html>

<script>
    function exportCanvasAsPNG() {

    var canvasElement =  document.getElementById("myCanvas");

    var MIME_TYPE = "image/png";

    var imgURL = canvasElement.toDataURL(MIME_TYPE);

    var dlLink = document.createElement('a');
    dlLink.download = "test.png";
    dlLink.href = imgURL;
    dlLink.dataset.downloadurl = [MIME_TYPE, dlLink.download, dlLink.href].join(':');

    document.body.appendChild(dlLink);
    dlLink.click();
    document.body.removeChild(dlLink);
    }
    function saveImage(){
        var canvas = document.getElementById("myCanvas");
        var image = canvas.toDataURL("image/png");//.replace("image/png", "image/octet-stream");
        window.location.href = image;        
    }
</script>
<script>
    var canvas = document.getElementById("myCanvas");
    var ctx = canvas.getContext("2d");
    ctx.fillStyle = "#000000";
    ctx.fillRect(47,2,2,285);
    ctx.fillRect(42,280,500,2);
    ctx.fillRect(540,275,2,5);
    ctx.font = "9px Arial";
    ctx.fillStyle = "#FF0000";
    <% if(type.equals("3")){%>
        <%if(sensorVal==1) {%>
        <% if(deviceDetails.getHighAlertValue1()!=null && !"0".equals(deviceDetails.getHighAlertValue1())) {%>
            ctx.fillRect(49,280-(0.270*<%=deviceDetails.getHighAlertValue1()%>),500,1);
        <% } %>
        <% if(deviceDetails.getLowAlertValue1()!=null && !"0".equals(deviceDetails.getLowAlertValue1())) {%>
            ctx.fillStyle = "#FFFF00";
            ctx.fillRect(49,280-(0.270*<%=deviceDetails.getLowAlertValue1()%>),500,1);
        <% } %>
    <% } else {%>
        <% if(deviceDetails.getHighAlertValue2()!=null && !"0".equals(deviceDetails.getHighAlertValue2())) {%>
            ctx.fillRect(49,280-(0.270*<%=deviceDetails.getHighAlertValue2()%>),500,1);
        <% } %>
        <% if(deviceDetails.getLowAlertValue2()!=null && !"0".equals(deviceDetails.getLowAlertValue2())) {%>
            ctx.fillStyle = "#FFFF00";
            ctx.fillRect(49,280-(0.270*<%=deviceDetails.getLowAlertValue2()%>),500,1);
        <% } %>
    <% } 
    }else {
        if(sensorVal==1) {%>
        <% if(deviceDetails.getHighAlertValue1()!=null && !"0".equals(deviceDetails.getHighAlertValue1())) {%>
            ctx.fillRect(49,280-(2.7*<%=deviceDetails.getHighAlertValue1()%>),500,1);
        <% } %>
        <% if(deviceDetails.getLowAlertValue1()!=null && !"0".equals(deviceDetails.getLowAlertValue1())) {%>
            ctx.fillStyle = "#FFFF00";
            ctx.fillRect(49,280-(2.7*<%=deviceDetails.getLowAlertValue1()%>),500,1);
        <% } %>
    <% } else {%>
        <% if(deviceDetails.getHighAlertValue2()!=null && !"0".equals(deviceDetails.getHighAlertValue2())) {%>
            ctx.fillRect(49,280-(2.7*<%=deviceDetails.getHighAlertValue2()%>),500,1);
        <% } %>
        <% if(deviceDetails.getLowAlertValue2()!=null && !"0".equals(deviceDetails.getLowAlertValue2())) {%>
            ctx.fillStyle = "#FFFF00";
            ctx.fillRect(49,280-(2.7*<%=deviceDetails.getLowAlertValue2()%>),500,1);
        <% } %>
    <% }
    }%>
    ctx.fillStyle = "#FF0000";
    //hiehgt is 270 from 27 to ..
    //width is 290 from 30 to ...
    <%
        System.out.println("Type=" + type);
        double newX = 0;
        double newY = 0;
        double oldX = 0;
        double oldY = 0;
        if (!messages.isEmpty() && messages.size() > 10 && type != null) {
            long timeSpace = messages.get(0).getMessageTime().getTime() - messages.get(messages.size() - 1).getMessageTime().getTime();
            long pixelTime = timeSpace / 490;
            System.out.println("pixelTime=" + pixelTime);
            out.println("ctx.fillText('" + formatter.format(messages.get(0).getMessageTime()) + "', 35, 302);");
            out.println("ctx.fillText('" + formatter.format(messages.get(messages.size() - 1).getMessageTime()) + "', 485, 302);");
            out.println("ctx.font = '14px Arial'");
            if(sensorVal==1){
                out.println("ctx.fillText('"+deviceDetails.getModel().getSensor1Name()+" Graph', 430, 15)");
            }else{
                out.println("ctx.fillText('"+deviceDetails.getModel().getSensor2Name()+" Graph', 430, 15)");
            }
            if (type.equals("1")) {
                %>
                ctx.font = "9px Arial";
                ctx.fillText("0", 30, 285);ctx.fillText("100", 20, 15);
                ctx.fillText("90", 25, 42);ctx.fillText("80", 25, 69);
                ctx.fillText("70", 25, 96);ctx.fillText("60", 25, 123);
                ctx.fillText("50", 25, 150);ctx.fillText("40", 25, 177);
                ctx.fillText("30", 25, 204);ctx.fillText("20", 25, 231);
                ctx.fillText("10", 25, 258);
                ctx.fillRect(44,10,5,1);ctx.fillRect(44,37,5,1);
                ctx.fillRect(44,64,5,1);ctx.fillRect(44,91,5,1);
                ctx.fillRect(44,118,5,1);ctx.fillRect(44,145,5,1);
                ctx.fillRect(44,172,5,1);ctx.fillRect(44,199,5,1);
                ctx.fillRect(44,226,5,1);ctx.fillRect(44,253,5,1);
                ctx.fillStyle = "#4169E1";
                <%
                for (int n = 0; n < messages.size(); n++) {
                    Messages message = messages.get(n);
                    if (message.getType() != 0 && message.getType() != 7) {
                        message.setPayload(message.getPayload().replace('[',','));
                        String[] parts=message.getPayload().split(",");
                        int value=(int)Double.parseDouble(parts[sensorVal-1]);
                        %>
                        ctx.fillStyle = "#32CD32";
                        ctx.arc(568, 40, 15, 0, 2 * Math.PI);
                        ctx.fill();                  
                        ctx.fillStyle = "#FFFFFF";
                        ctx.font = "14px Arial";
                        ctx.fillText("<%=value%>", 560, 45);
                        ctx.fillStyle = "#000000";
                        <% break;
                    }
                }
                if(connected){
                    out.print("ctx.beginPath();");
                    for (int n = 0; n < messages.size(); n++) {
                        Messages message = messages.get(n);
                        if (message.getType() != 0 && message.getType() != 7) {
                            message.setPayload(message.getPayload().replace('[',','));
                            String[] parts=message.getPayload().split(",");
                            newY= Double.parseDouble(parts[sensorVal-1]);
                            newX = 50 + ((messages.get(0).getMessageTime().getTime() - message.getMessageTime().getTime()) / pixelTime);
                            newY = 280 - (2.7 * newY);
                            if(oldX==0 && oldY ==0) {
                                out.println("ctx.moveTo(" + newX + "," + newY + ");");
                                //out.println("ctx.fillRect(" + newX + "," + newY + "," + 1 + "," + 1 + ");");
                            } else {
                                out.println("ctx.lineTo(" + newX + "," + newY + ");");
                            }
                            oldX=newX;oldY=newY;
                        }
                    }
                    out.println("ctx.stroke();");
                }else{
                    for (int n = 0; n < messages.size(); n++) {
                        Messages message = messages.get(n);
                        if (message.getType() != 0 && message.getType() != 7) {
                            message.setPayload(message.getPayload().replace('[',','));
                            String[] parts=message.getPayload().split(",");
                            newY= Double.parseDouble(parts[sensorVal-1]);
                            newX = 50 + ((messages.get(0).getMessageTime().getTime() - message.getMessageTime().getTime()) / pixelTime);
                            newY = 280 - (2.7 * newY);
                            out.println("ctx.fillRect(" + newX + "," + newY + "," + 1 + "," + 1 + ");");
                        }
                    }                        
                }
            } else if (type.equals("2")) {
                %>
                ctx.font = "9px Arial";
                ctx.fillText("0%", 25, 285);ctx.fillText("100%", 15, 15);
                ctx.fillText("90%", 20, 42);ctx.fillText("80%", 20, 69);
                ctx.fillText("70%", 20, 96);ctx.fillText("60%", 20, 123);
                ctx.fillText("50%", 20, 150);ctx.fillText("40%", 20, 177);
                ctx.fillText("30%", 20, 204);ctx.fillText("20%", 20, 231);
                ctx.fillText("10%", 20, 258);
                ctx.fillRect(44,10,5,1);ctx.fillRect(44,37,5,1);
                ctx.fillRect(44,64,5,1);ctx.fillRect(44,91,5,1);
                ctx.fillRect(44,118,5,1);ctx.fillRect(44,145,5,1);
                ctx.fillRect(44,172,5,1);ctx.fillRect(44,199,5,1);
                ctx.fillRect(44,226,5,1);ctx.fillRect(44,253,5,1);
                ctx.fillStyle = "#4169E1";
                <%
                for (int n = 0; n < messages.size(); n++) {
                    Messages message = messages.get(n);
                    if (message.getType() != 0 && message.getType() != 7) {
                        message.setPayload(message.getPayload().replace('[',','));
                        String[] parts=message.getPayload().split(",");
                        int value=(int)Double.parseDouble(parts[sensorVal-1]);
                        %>
                        ctx.fillStyle = "#32CD32";
                        ctx.arc(568, 40, 15, 0, 2 * Math.PI);
                        ctx.fill();                  
                        ctx.fillStyle = "#FFFFFF";
                        ctx.font = "14px Arial";
                        ctx.fillText("<%=value%>", 560, 45);
                        ctx.fillStyle = "#000000";
                        <% break;
                    }
                }                    
                if(connected){
                    out.print("ctx.beginPath();");
                    for (int n = 0; n < messages.size(); n++) {
                        Messages message = messages.get(n);
                        if (message.getType() != 0 && message.getType() != 7) {
                            newX = 50 + ((messages.get(0).getMessageTime().getTime() - message.getMessageTime().getTime()) / pixelTime);
                            message.setPayload(message.getPayload().replace('[',','));
                            String[] parts=message.getPayload().split(",");
                            newY= Double.parseDouble(parts[sensorVal-1]);
                            newY = 280 - (2.7 * newY);
                            if(oldX==0 && oldY ==0) {
                                out.println("ctx.moveTo(" + newX + "," + newY + ");");
                                //out.println("ctx.fillRect(" + newX + "," + newY + "," + 1 + "," + 1 + ");");
                            } else {
                                out.println("ctx.lineTo(" + newX + "," + newY + ");");
                            }
                            oldX=newX;oldY=newY;
                        }
                    }
                    out.println("ctx.stroke();");
                }else {
                    for (int n = 0; n < messages.size(); n++) {
                        Messages message = messages.get(n);
                        if (message.getType() != 0 && message.getType() != 7) {
                            newX = 50 + ((messages.get(0).getMessageTime().getTime() - message.getMessageTime().getTime()) / pixelTime);
                            message.setPayload(message.getPayload().replace('[',','));
                            String[] parts=message.getPayload().split(",");
                            newY= Double.parseDouble(parts[sensorVal-1]);
                            newY = 280 - (2.7 * newY);
                            out.println("ctx.fillRect(" + newX + "," + newY + "," + 1 + "," + 1 + ");");
                        }
                    }                    
                }
            } else if (type.equals("3")) {
                %>
                ctx.font = "9px Arial";
                ctx.fillText("1024", 15, 8);
                ctx.fillText("0", 25, 285);ctx.fillText("1000", 15, 15);
                ctx.fillText("900", 20, 42);ctx.fillText("800", 20, 69);
                ctx.fillText("700", 20, 96);ctx.fillText("600", 20, 123);
                ctx.fillText("500", 20, 150);ctx.fillText("400", 20, 177);
                ctx.fillText("300", 20, 204);ctx.fillText("200", 20, 231);
                ctx.fillText("100", 20, 258);
                ctx.fillRect(44,10,5,1);ctx.fillRect(44,37,5,1);
                ctx.fillRect(44,64,5,1);ctx.fillRect(44,91,5,1);
                ctx.fillRect(44,118,5,1);ctx.fillRect(44,145,5,1);
                ctx.fillRect(44,172,5,1);ctx.fillRect(44,199,5,1);
                ctx.fillRect(44,226,5,1);ctx.fillRect(44,253,5,1);
                ctx.fillStyle = "#4169E1";
                <%
                for (int n = 0; n < messages.size(); n++) {
                    Messages message = messages.get(n);
                    if (message.getType() != 0 && message.getType() != 7) {
                        message.setPayload(message.getPayload().replace('[',','));
                        String[] parts=message.getPayload().split(",");
                        int value=(int)Double.parseDouble(parts[sensorVal-1]);
                        %>
                        ctx.fillStyle = "#32CD32";
                        <% if(value>100){ %>
                            ctx.arc(571, 40, 15, 0, 2 * Math.PI);
                        <% } else {%>
                            ctx.arc(568, 40, 15, 0, 2 * Math.PI);
                        <% } %>
                        ctx.fill();                  
                        ctx.fillStyle = "#FFFFFF";
                        ctx.font = "14px Arial";
                        ctx.fillText("<%=value%>", 560, 45);
                        ctx.fillStyle = "#000000";
                        <% break;
                    }
                }                    
                if(connected){
                    out.print("ctx.beginPath();");
                    for (int n = 0; n < messages.size(); n++) {
                        Messages message = messages.get(n);
                        if (message.getType() != 0 && message.getType() != 7) {
                            newX = 50 + ((messages.get(0).getMessageTime().getTime() - message.getMessageTime().getTime()) / pixelTime);
                            message.setPayload(message.getPayload().replace('[',','));
                            String[] parts=message.getPayload().split(",");
                            newY= Double.parseDouble(parts[sensorVal-1]);
                            newY = 280 - (0.27 * newY);
                            if(oldX==0 && oldY ==0) {
                                out.println("ctx.moveTo(" + newX + "," + newY + ");");
                                //out.println("ctx.fillRect(" + newX + "," + newY + "," + 1 + "," + 1 + ");");
                            } else {
                                out.println("ctx.lineTo(" + newX + "," + newY + ");");
                            }
                            oldX=newX;oldY=newY;
                        }
                    }
                    out.println("ctx.stroke();");
                }else {
                    for (int n = 0; n < messages.size(); n++) {
                        Messages message = messages.get(n);
                        if (message.getType() != 0 && message.getType() != 7) {
                            newX = 50 + ((messages.get(0).getMessageTime().getTime() - message.getMessageTime().getTime()) / pixelTime);
                            message.setPayload(message.getPayload().replace('[',','));
                            String[] parts=message.getPayload().split(",");
                            newY= Double.parseDouble(parts[sensorVal-1]);
                            newY = 280 - (0.27 * newY);
                            out.println("ctx.fillRect(" + newX + "," + newY + "," + 1 + "," + 1 + ");");
                        }
                    }                    
                }
            } else if (type.equals("5") || type.equals("4")) {
                %>
                ctx.font = "9px Arial";
                <% if (type.equals("5")){%>
                    ctx.fillText("OPENED", 5, 125);
                    ctx.fillText("CLOSED", 5, 180);
                <% } else if (type.equals("4")){ %>
                    ctx.fillText("ON", 15, 125);
                    ctx.fillText("OFF", 15, 180);                    
                <%} %>
                ctx.fillStyle = "#4169E1";
                <%
                if(connected){
                    out.print("ctx.beginPath();");
                    for (int n = 0; n < messages.size(); n++) {
                        Messages message = messages.get(n);
                        if (message.getType() != 0 && message.getType() != 7) {
                            newX = 50 + ((messages.get(0).getMessageTime().getTime() - message.getMessageTime().getTime()) / pixelTime);
                            message.setPayload(message.getPayload().replace('[',','));
                            String[] parts=message.getPayload().split(",");
                            //System.out.println("Data="+parts[sensorVal-1]);
                            if(parts[sensorVal-1].contains("1")) 
                                newY=123.0; 
                            else 
                                newY=177.0;
                            if(oldX==0 && oldY ==0) {
                                out.println("ctx.moveTo(" + newX + "," + newY + ");");
                                //out.println("ctx.fillRect(" + newX + "," + newY + "," + 1 + "," + 1 + ");");
                            } else {
                                out.println("ctx.lineTo(" + newX + "," + newY + ");");
                            }
                            oldX=newX;oldY=newY;
                        }
                    }
                    out.println("ctx.stroke();");
                }else {
                    for (int n = 0; n < messages.size(); n++) {
                        Messages message = messages.get(n);
                        if (message.getType() != 0 && message.getType() != 7) {
                            newX = 50 + ((messages.get(0).getMessageTime().getTime() - message.getMessageTime().getTime()) / pixelTime);
                            message.setPayload(message.getPayload().replace('[',','));
                            String[] parts=message.getPayload().split(",");
                            if(parts[sensorVal-1].contains("1")) 
                                newY=123.0; 
                            else 
                                newY=177.0;
                            out.println("ctx.fillRect(" + newX + "," + newY + "," + 1 + "," + 1 + ");");
                        }
                    }                    
                }
            }
        } else {%>
            ctx.font = "14px Arial";
            ctx.fillText("Device :<%=deviceDetails.getFriendlyName() %> (<%=deviceDetails.getLocation()%>) - No Messages Available", 130, 15);        
        <%}
    %>
</script>

<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.TimeZone"%>
<%@page import="osa.ora.iot.db.beans.TenantSettings"%>
<%@page import="osa.ora.iot.beans.IConstant"%>
<%@page import="osa.ora.iot.db.beans.Users"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
    if (session.getAttribute("USER") == null || session.getAttribute("USER_SETTINGS")==null) {
        response.sendRedirect("error.jsp");
        return;
    }
    Users user = (Users) session.getAttribute("USER");
    TenantSettings setting= (TenantSettings) session.getAttribute("USER_SETTINGS");
    String notificationCount=(String)session.getAttribute("NOTIFICATION_COUNT");
    DateFormat formatter = new SimpleDateFormat("EEE dd MMM  HH:mm");
    formatter.setTimeZone(TimeZone.getTimeZone(setting.getTimezone()));  
    Object originalTenant=session.getAttribute("ORIGINAL");
%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>SensorWays IoT, Main Menu Page</title>
        <link rel="stylesheet" href="css/w3.css">
        <link rel='stylesheet' href='css/style.css'>
        <script src="js/ajax_check_notifications.js"></script>  
    </head>
    <body>
        <div class="navbar">
            <a href="index.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/home.png"> Home Page</a>
            <a href="information.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/humidity.png"> Supported Devices</a>
            <a href="contact.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/notification.png"> Contact Us</a>
            <a class="active" href="main.jsp"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/menu.png"> Main Menu</a>
            <a href="NotificationServlet?action=<%=IConstant.ACTION_WEB_LIST_NOTIFICATIONS%>"><i style="float:right;"></i><img src="images/alert.png" style="width: 30px; height: 25px;"> Alerts <span id="total">(<%=notificationCount%>)</span></a>
            <a href="LogoutServlet"><i class="fa fa-fw"></i><img style="width: 25px; height: 25px;" src="images/members.png"> Logout</a>
        </div>        
             &nbsp;&nbsp;<%=formatter.format(new Date())%><h3>&nbsp;Welcome <%=user.getUsername()%> [<i><font color="red"><%=setting.getIdentityName()%></font></i>]</h3>
             <% if(originalTenant!=null) { %>
             &nbsp;&nbsp;<a href="ManageAccountsServlet?accountId=<%=originalTenant%>&action=<%=IConstant.ACTION_WEB_LOGOUT_FROM_USER_ACCOUNT%>">Logout from this Account </a>
             <% } %>
        <table>
            <tr><td><a style="width: 300px" class="w3-button w3-red w3-ripple" href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_LIST_DEVICES%>'><img style="width: 25px; height: 25px;" src="images/humidity.png"><br>Manage IoT Devices</a>
                    <a style="width: 300px" class="w3-button w3-red w3-ripple" href='DeviceGroupServlet?action=<%=IConstant.ACTION_WEB_LIST_DEVICE_GROUP%>'><img style="width: 25px; height: 25px;" src="images/group.png"><br>Manage Device Groups</a>
                <% if (user.getWorkflowCreation() == 1) { %>
                    <a style="width: 300px" class="w3-button w3-red w3-ripple" href='WorkflowServlet?action=<%=IConstant.ACTION_WEB_LIST_WORKFLOWS%>'><img style="width: 35px; height: 25px;" src="images/light.png"><br>Manage IoT Workflows</a>
                <%} %>
            </td></tr>
            <tr><td>
                <% if (user.getSchedulerCreation() == 1) { %>
                    <a style="width: 300px" class="w3-button w3-red w3-ripple" href='SchedulerServlet?action=<%=IConstant.ACTION_WEB_LIST_SCHEDULERS%>'><img style="width: 25px; height: 25px;" src="images/sun-midday.png"><br>Manage IoT Schedulers</a>
                <%} %>
                    <a style="width: 300px" class="w3-button w3-red w3-ripple" href='IoTAppServlet?action=<%=IConstant.ACTION_WEB_LIST_APPLICATIONS%>'><img style="width: 35px; height: 25px;" src="images/in_progress.png"><br>Manage Applications -In-progress</a>
            </td></tr>
            <tr><td>
                <% if (user.getDashboardCreation()== 1) { %>
                    <a style="width: 300px" class="w3-button w3-blue w3-ripple" href='DashboardServlet?action=<%=IConstant.ACTION_WEB_SHOW_DASHBOARD%>'><img style="width: 25px; height: 25px;" src="images/graph.png"><br>IoT Dashboard</a>
                <%}%>
                <% if (user.getSimulationCreation() == 1) { %>
                    <a style="width: 300px" class="w3-button w3-blue w3-ripple" href='SimulationServlet?action=<%=IConstant.ACTION_WEB_LIST_SIMULATION%>'><img style="width: 35px; height: 25px;" src="images/simulation.png"><br>Device Simulation</a>
                <%} %>
                    
                <% if (user.getIdentityAdmin()== 1) { %>
                    <a style="width: 300px" class="w3-button w3-blue w3-ripple" href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_CONFIG_REPORT%>'><img style="width: 25px; height: 25px;" src="images/reporting.png"><br>Report Execution</a>
                <%}%>
            </td></tr>
            <tr><td>
                <% if (user.getUserCreation()== 1) { %>
                    <a style="width: 300px" class="w3-button w3-teal w3-ripple" href='ManageUsersServlet?action=<%=IConstant.ACTION_WEB_LIST_USERS%>'><img style="width: 25px; height: 25px;" src="images/users.png"><br>Manage Users</a>
                <%} %>
                <% if(user.getUserRole()==IConstant.ROLE_READ_WRITE) { %>
                    <a style="width: 300px" class="w3-button w3-teal w3-ripple" href='settings.jsp'><img style="width: 25px; height: 25px;" src="images/preferences.png"><br>Account Preferences</a>
                <%} %>
                <a style="width: 300px" class="w3-button w3-teal w3-ripple" href='password.jsp'><img style="width: 35px; height: 25px;" src="images/login.png"><br>تغيير كلمة السر</a>
            </td></tr>
            <tr><td>
                <% if (user.getSystemAdmin()== 1) { %>
                    <a style="width: 300px" class="w3-button w3-purple w3-ripple" href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_REGISTER_BARCODE%>'><img style="width: 25px; height: 25px;" src="images/barcode.png"><br>Add Bar Code</a>
                    <a style="width: 300px" class="w3-button w3-purple w3-ripple" href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_MANAGE_MODELS%>'><img style="width: 25px; height: 25px;" src="images/tv.png"><br>Manage Device Models</a>
                    <a style="width: 300px" class="w3-button w3-purple w3-ripple" href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_UPDATE_FIRMWARE%>'><img style="width: 25px; height: 25px;" src="images/download.png"><br>Update Device Firmware</a>
                </td></tr>
                <tr><td>                    
                    <a style="width: 300px" class="w3-button w3-purple w3-ripple" href='ManageAccountsServlet?action=<%=IConstant.ACTION_WEB_LIST_USERS%>'><img style="width: 25px; height: 25px;" src="images/admin.png"><br>Manage Accounts</a>
                    <a style="width: 300px" class="w3-button w3-purple w3-ripple" href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_LOAD_SYS_CONFIG%>'><img style="width: 25px; height: 25px;" src="images/settings_icon.png"><br>اعدادات النظام</a>                    
                <%}%>
                <% if (user.getIdentityAdmin()== 1) { %>
                    <a style="width: 300px" class="w3-button w3-purple w3-ripple" href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_LIST_AUDIT%>&actionFilter=1000'><img style="width: 25px; height: 25px;" src="images/mag.png"><br>Audit Records</a>
                <%}%>
            </td></tr>            
            <tr><td>
                <% if (user.getSystemAdmin()== 1) { %>
                    <a style="width: 300px" class="w3-button w3-purple w3-ripple" href='protocol.jsp'><img style="width: 25px; height: 25px;" src="images/protocol.png"><br>SensorWays Open Protocol</a>
                    <a style="width: 300px" class="w3-button w3-purple w3-ripple" href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_LIST_SYS_JOBS%>'><img style="width: 25px; height: 25px;" src="images/fan.png"><br>System Jobs</a>
                    <a style="width: 300px" class="w3-button w3-purple w3-ripple" href='IoTWebServlet?action=<%=IConstant.ACTION_WEB_LIST_SYS_JOBS%>'><img style="width: 25px; height: 25px;" src="images/fan.png"><br>Manage Device Recipes</a>
                <%}%>                    
            </td></tr>
        </table>
<%@include file="footer.jsp" %>
</body>
</html>
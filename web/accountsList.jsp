<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.util.TimeZone"%>
<%@page import="osa.ora.iot.db.beans.TenantSettings"%>
<%@page import="osa.ora.iot.db.beans.Scheduler"%>
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
if(user.getSystemAdmin()!=1){
    response.sendRedirect("error.jsp");
    return;
}
TenantSettings setting= (TenantSettings) session.getAttribute("USER_SETTINGS");
String notificationCount=(String)session.getAttribute("NOTIFICATION_COUNT");
DateFormat formatter = new SimpleDateFormat("EEE dd MMM HH:mm");
formatter.setTimeZone(TimeZone.getTimeZone(setting.getTimezone()));

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SensorWays IoT, Accounts List Page</title>
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
	&nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > Manage Accounts
        <h3>&nbsp;List of Existing Accounts</h3>
	<table>
	<tr><th>Account Identifier</th><th>Account Name</th><th>Last Updated</th><th>Admin Email</th><th>Existing/Max Users</th>
	<th>Existing/Max Devices</th><th>Existing/Max SMS</th><th>Account Status</th><th>Action</th></tr>
	<%
        List<TenantSettings> tenantList = null;
	if(session.getAttribute("TENANTS_LIST")!=null){
		tenantList = (List<TenantSettings>)session.getAttribute("TENANTS_LIST");
	for (int n = 0; n < tenantList.size(); n++) {
            TenantSettings currentTenant = tenantList.get(n);
            %>
            <tr><td><%=currentTenant.getIdentity()%>
            </td><td><%=currentTenant.getIdentityName()%>
            </td><td><%=currentTenant.getUpdateTime()==null?"N/A":formatter.format(currentTenant.getUpdateTime())%>
            </td><td><%=currentTenant.getAdminUserEmail()%>
            </td><td><%=currentTenant.getCurrentUsers()%>/<%=currentTenant.getMaxUsers()%>
            </td><td><%=currentTenant.getCurrentDevices()%>/<%=currentTenant.getMaxDevices()%>
            </td><td><%=currentTenant.getSmsConsumed()%>/<%=currentTenant.getSmsQouta()%>
            </td><td><%=IoTUtil.getDisplayUserStatus(currentTenant.getTenantStatus())%>
            </td><td>
                <%
                if(user.getIdentityId()!=currentTenant.getIdentity()){
                String actionClick = "ManageAccountsServlet?accountId=" + currentTenant.getIdentity()+ "&action=";
                if(user.getSystemAdmin()==1){%>
                <a style="width: 200px" class="w3-btn w3-ripple w3-blue" onclick="return confirm('Confirm Login to this Account?');" href="<%=actionClick + IConstant.ACTION_WEB_LOGIN_TO_USER_ACCOUNT%>">Login To This Account</a><br>
                <%}                 
                if(currentTenant.getTenantStatus()==1){%>
                <a style="width: 200px" class="w3-btn w3-ripple w3-red" onclick="return confirm('Confirm Deactivate ALL Users & Devices in this Account?');" href="<%=actionClick + IConstant.ACTION_WEB_INACTIVATE_USER%>">Deactivate Account</a><br>
                <%} else {%>
                    <a style="width: 200px" class="w3-btn w3-ripple w3-green" onclick="return confirm('Confirm Ativate ALL Users & Devices in this Account?');" href="<%=actionClick + IConstant.ACTION_WEB_ACTIVATE_USER%>">Activate Account</a><br>
                <% }%>
                <a style="width: 200px" class="w3-btn w3-ripple w3-teal" href="<%=actionClick + IConstant.ACTION_WEB_LOAD_ACCOUNT_FOR_UPDATE%>">Edit Account</a></td>
                <%} %>
            </td></tr>
	<%}
        }%>
        </table>
        <br><a class="w3-btn w3-ripple w3-red" href='registerAccount.jsp'>Add New Account</a>
        <a class="w3-btn w3-ripple w3-red" href='main.jsp'>Back to Main Menu</a><br><br>
    <%@include file="footer.jsp" %>
</body>
</html>
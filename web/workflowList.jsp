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
<title>SensorWays IoT, Workflow List Page</title>
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
	&nbsp;&nbsp;<a href='main.jsp'>Main Menu</a> > Manage Workflows
        <h3>&nbsp;List of Workflows</h3>
	<table>
	<tr><th>Id</th><th>Name</th><th>Trigger Type</th><th>Workflow Description</th><th>Status</th><th>Action</th></tr>
	<%
        List<Workflows> workFlowList = null;
	if(session.getAttribute("WORKFLOW_LIST")!=null){
		workFlowList = (List<Workflows>)session.getAttribute("WORKFLOW_LIST");
	for (int n = 0; n < workFlowList.size(); n++) {
            Workflows workflow = workFlowList.get(n);
            %>
            <tr><td><%=workflow.getId()%>
            </td><td><%=workflow.getWorkflowName()%>
            </td><td><% if(workflow.getTriggerType()==1) { out.println("Device");} else { out.println("Device Group");}%>
            </td><td>
                Upon <font color="red"><%=IoTUtil.getDisplayAction(workflow.getTriggerEvent(),workflow)%></font>
                From <font color="blue"><%=workflow.getTriggerType()==1?workflow.getSourceDevice().getFriendlyName():workflow.getSourceDeviceGroup().getName()%></font>
                Then <font color="orange"><%=IoTUtil.getDisplayTargetAction(workflow.getTargetAction(),workflow.getTargetDevice().getDevice1(),workflow.getTargetDevice().getDevice2())%></font>
                Of <font color="green"><%=workflow.getTargetDevice().getFriendlyName()%></font>
            </td>
            </td><td><%=IoTUtil.getDisplayDeviceStatus(workflow.getWorkflowStatus())%>
            </td><td><%
            String actionClick = "WorkflowServlet?workflowId=" + workflow.getId()+ "&action=";
            if(user.getUserRole()==IConstant.ROLE_READ_WRITE) { 
                if(workflow.getWorkflowStatus()==1){%>
                    <a style="width: 200px" class="w3-btn w3-ripple w3-red" href="<%=actionClick + IConstant.ACTION_WEB_INACTIVATE_WORKFLOW%>">Deactivate Workflow</a><br>
                <%} else {%>
                    <a style="width: 200px" class="w3-btn w3-ripple w3-green" href="<%=actionClick + IConstant.ACTION_WEB_ACTIVATE_WORKFLOW%>">Activate Workflow</a><br>
                <% }%>
                    <a style="width: 200px" class="w3-btn w3-ripple w3-teal" onclick="return confirm('Confirm Delete this Workflow?');" href="<%=actionClick + IConstant.ACTION_WEB_DELETE_WORKFLOW%>">Delete Workflow</a></td>
            <%}%>
            </td></tr>
	<%}
        }%>
        </table>
        <% if(user.getUserRole()==IConstant.ROLE_READ_WRITE && user.getWorkflowCreation()==1 && (workFlowList==null || workFlowList.size()<50)) { %>
        <br><a class="w3-btn w3-ripple w3-red" href='workflow.jsp'>Create New Device Workflow</a>
        <%} %>
        <a class="w3-btn w3-ripple w3-red" href='main.jsp'>Back to Main Menu</a><br><br>
    <%@include file="footer.jsp" %>
</body>
</html>
/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This class is responsible for managing IoT workflows
 * across different IoT devices.
 */
package osa.ora.iot;

import java.io.IOException;
import java.util.List;
import javax.ejb.EJB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import osa.ora.iot.beans.IConstant;
import osa.ora.iot.db.beans.DeviceGroup;
import osa.ora.iot.db.beans.Devices;
import osa.ora.iot.db.beans.Users;
import osa.ora.iot.db.beans.Workflows;
import osa.ora.iot.db.session.UserSessionBean;

/**
 * IoTWebWorkflowServlet
 * @author ooransa
 */
@WebServlet(name = "IoTWebWorkflowServlet", urlPatterns = {"/WorkflowServlet"})
public class IoTWebWorkflowServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    @EJB(beanName = "UserSessionBean")
    UserSessionBean userSessionBean;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IoTWebWorkflowServlet() {
        super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (request.getSession().getAttribute("USER") == null) {
            request.getSession().setAttribute("ERROR_MSG", "Your session is expired!");
            response.sendRedirect("error.jsp");
            return;
        } else {
            Users user = (Users) request.getSession().getAttribute("USER");
            String action = request.getParameter("action");
            String workflowId = request.getParameter("workflowId");
            List<Workflows> userWorkflowList = null;
            if (action == null) {
                request.getSession().setAttribute("ERROR_MSG", "Missing required parameters!");
                response.sendRedirect("error.jsp");
                return;
            } else {
                int actionVal = 0;
                int workflowIdVal=0;
                try {
                    actionVal = Integer.parseInt(action);
                    if(workflowId!=null) workflowIdVal=Integer.parseInt(workflowId);
                } catch (Exception ex) {
                    // do no thing ...
                }
                switch (actionVal) {
                    case IConstant.ACTION_WEB_LIST_WORKFLOWS:
                        userWorkflowList = userSessionBean.getIdentityWorkflows(user.getIdentityId());
                        request.getSession().setAttribute("WORKFLOW_LIST", userWorkflowList);
                        List<Devices> userDeviceList = userSessionBean.getUserDevices(user.getIdentityId(),true,false,null);
                        request.getSession().setAttribute("DEVICE_LIST", userDeviceList);
                        List<DeviceGroup> deviceGroupList = userSessionBean.getIdentityDeviceGroups(user.getIdentityId(),true);
                        request.getSession().setAttribute("DEVICE_GROUP_LIST", deviceGroupList);                        
                        request.getRequestDispatcher("workflowList.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_REGISTER_WORKFLOW:
                        userWorkflowList = userSessionBean.getIdentityWorkflows(user.getIdentityId());
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY || (userWorkflowList!=null && userWorkflowList.size()>=50)) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Privilages or Exceeded the Limit!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        // do actual action!
                        String name=request.getParameter("name");
                        String source=request.getParameter("source");
                        String group=request.getParameter("group");
                        String event=request.getParameter("event");
                        String target=request.getParameter("target");
                        String status=request.getParameter("status");
                        String type=request.getParameter("type");
                        String targetAction=request.getParameter("target_action");
                        if(name==null || type==null || event==null || target==null || targetAction==null || status==null){
                            request.getSession().setAttribute("ERROR_MSG", "Missing required parameters!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        int typeVal=1;
                        try{
                            typeVal=Integer.parseInt(type);
                            if((typeVal==1 && source==null) || (typeVal==2 && group==null) ){
                                throw new Exception("Invalid Source Type");
                            }
                            if(typeVal==2){
                                source=group;
                            }
                        }catch(Exception ex){
                            request.getSession().setAttribute("ERROR_MSG", "Source type is invalid!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        if(typeVal==1 && source.equals(target)){
                            request.getSession().setAttribute("ERROR_MSG", "As both source & target are the same, better to use device local smart rule!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        //id should come from the DB ...
                        Workflows newWorkflow=userSessionBean.addNewWorkflow(source, Integer.parseInt(event), target, Integer.parseInt(targetAction), user.getIdentityId(), name,Integer.parseInt(status),typeVal);
                        if(newWorkflow==null){
                            request.getSession().setAttribute("ERROR_MSG", "Failed to create workflow, or workflow already exist!");
                            response.sendRedirect("error.jsp");
                            return;                            
                        }
                        //audit trail
                        userSessionBean.addAuditTrail(user.getId(), actionVal,"WorkflowId="+newWorkflow.getId()+",Name="+newWorkflow.getWorkflowName());                                
                        userWorkflowList = userSessionBean.getIdentityWorkflows(user.getIdentityId());
                        request.getSession().setAttribute("WORKFLOW_LIST", userWorkflowList);
                            request.getRequestDispatcher("workflowList.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_DELETE_WORKFLOW:
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Privilages!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        // do actual action!
                        boolean results=userSessionBean.removeWorkflow(workflowIdVal,user.getIdentityId());
                        // should route to the same page ..
                        if(results) {
                            //audit trail
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"WorkflowId="+workflowIdVal);                                                                
                            request.getRequestDispatcher("WorkflowServlet?action=" + IConstant.ACTION_WEB_LIST_WORKFLOWS)
                                .forward(request, response);
                        } else {
                            request.getSession().setAttribute("ERROR_MSG", "Error while deleting the workflow!");
                            response.sendRedirect("error.jsp");
                        }
                        break;
                    case IConstant.ACTION_WEB_ACTIVATE_WORKFLOW:
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Privilages!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        // do actual action!
                        boolean activateResults=userSessionBean.activateWorkflow(workflowIdVal,user.getIdentityId());
                        // should route to the same page ..
                        if(activateResults) {
                            //audit trail
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"WorkflowId="+workflowIdVal);                                                                
                            request.getRequestDispatcher("WorkflowServlet?action=" + IConstant.ACTION_WEB_LIST_WORKFLOWS)
                                .forward(request, response);
                        } else {
                            request.getSession().setAttribute("ERROR_MSG", "Error while activating the workflow!");
                            response.sendRedirect("error.jsp");
                        }
                        break;
                    case IConstant.ACTION_WEB_INACTIVATE_WORKFLOW:
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Privilages!");
                            response.sendRedirect("error.jsp");
                            return;
                        }
                        // do actual action!
                        boolean deactivateResults=userSessionBean.inactivateWorkflow(workflowIdVal,user.getIdentityId());
                        // should route to the same page ..
                        if(deactivateResults) {
                            //audit trail
                            userSessionBean.addAuditTrail(user.getId(), actionVal,"WorkflowId="+workflowIdVal);                                                                
                            request.getRequestDispatcher("WorkflowServlet?action=" + IConstant.ACTION_WEB_LIST_WORKFLOWS)
                                .forward(request, response);
                        } else {
                            request.getSession().setAttribute("ERROR_MSG", "Error while deactivating the workflow!");
                            response.sendRedirect("error.jsp");
                        }
                        break;
                    default:
                        request.getSession().setAttribute("ERROR_MSG", "Missing required parameters!");
                        response.sendRedirect("error.jsp");
                        return;
                }
            }
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}

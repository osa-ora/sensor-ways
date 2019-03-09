/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This class is responsible for managing device's 
 * simulation.
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
import osa.ora.iot.db.beans.Devices;
import osa.ora.iot.db.beans.Simulations;
import osa.ora.iot.db.beans.Users;
import osa.ora.iot.db.session.UserSessionBean;

/**
 * IoTWebSimulationServlet
 * @author ooransa
 */
@WebServlet(name = "IoTWebSimulationServlet", urlPatterns = {"/SimulationServlet"})
public class IoTWebSimulationServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    @EJB(beanName = "UserSessionBean")
    UserSessionBean userSessionBean;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IoTWebSimulationServlet() {
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
            String simulationId = request.getParameter("simulationId");
            List<Simulations> userSimulationList = null;
            if (action == null) {
                request.getSession().setAttribute("ERROR_MSG", "Missing required parameters!");
                response.sendRedirect("error.jsp");
                return;
            } else {
                int actionVal = 0;
                int simulationIdVal=0;
                try {
                    actionVal = Integer.parseInt(action);
                    if(simulationId!=null) simulationIdVal=Integer.parseInt(simulationId);
                } catch (Exception ex) {
                    // do no thing ...
                }
                switch (actionVal) {
                    case IConstant.ACTION_WEB_LIST_SIMULATION:
                        userSimulationList = userSessionBean.getIdentitySimulations(user.getIdentityId());
                        request.getSession().setAttribute("SIMULATION_LIST", userSimulationList);
                        request.getRequestDispatcher("simulationList.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_CREATE_SIMULATION:
                        List<Devices> userDeviceList = userSessionBean.getUserDevices(user.getIdentityId(),true,false,null);
                        request.getSession().setAttribute("DEVICE_LIST", userDeviceList);
                        request.getRequestDispatcher("registerSimulation.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_SAVE_SIMULATION:
                        userSimulationList = userSessionBean.getIdentitySimulations(user.getIdentityId());
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY || (userSimulationList!=null && userSimulationList.size()>=5)) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Privilages or Exceeded the Limit!");
                            response.sendRedirect("error.jsp");
                            return;
                        } else {
                            // do actual action!
                            String name=request.getParameter("name");
                            String source=request.getParameter("source");
                            String alert=request.getParameter("alert");
                            String error=request.getParameter("error");
                            String status=request.getParameter("status");
                            String duration=request.getParameter("duration");
                            if(name==null || source==null || alert==null || error==null || duration==null || status==null || !"2".equals(status)){
                                request.getSession().setAttribute("ERROR_MSG", "Missing The Required Parameters!");
                                response.sendRedirect("error.jsp");
                                return;
                            }else{
                                int durationVal=0;
                                float alertVal=0.0f;
                                float errorVal=0.0f;
                                int statusVal=0;
                                try{
                                    durationVal=Integer.parseInt(duration);
                                    alertVal=Float.parseFloat(alert);
                                    errorVal=Float.parseFloat(error);
                                    statusVal=Integer.parseInt(status);
                                }catch(Exception ex){
                                    request.getSession().setAttribute("ERROR_MSG", "Missing The Required Parameters!");
                                    response.sendRedirect("error.jsp");
                                    return;                                    
                                }
                                //id should come from the DB ...
                                Simulations newSimulation=userSessionBean.addNewSimulation(name, source, alertVal, errorVal, durationVal, statusVal, user.getIdentityId());
                                if(newSimulation!=null){
                                    //audit trail
                                    userSessionBean.addAuditTrail(user.getId(), actionVal,"SimulationId="+newSimulation.getId()+",Name="+newSimulation.getId());                                
                                    request.getRequestDispatcher("SimulationServlet?action=" + IConstant.ACTION_WEB_LIST_SIMULATION)
                                        .forward(request, response);
                                }else {
                                    request.getSession().setAttribute("ERROR_MSG", "Error While Creating Device Simulator!");
                                    response.sendRedirect("error.jsp");
                                    return;                                                                        
                                }
                            }
                        }
                        break;
                    case IConstant.ACTION_WEB_STOP_AND_DELETE_SIMULATION:
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                            response.sendRedirect("error.jsp");
                            return;
                        } else {
                            // do actual action!
                            boolean restuls=userSessionBean.removeAndStopStimulation(simulationIdVal,user.getIdentityId());
                            // should route to the same page ..
                            if(restuls) {
                                //audit trail
                                userSessionBean.addAuditTrail(user.getId(), actionVal,"SimulationId="+simulationIdVal);                                                                
                                request.getRequestDispatcher("SimulationServlet?action=" + IConstant.ACTION_WEB_LIST_SIMULATION)
                                    .forward(request, response);
                            } else {
                                request.getSession().setAttribute("ERROR_MSG", "Unexpected Error!");
                                response.sendRedirect("error.jsp");
                                return;
                            }
                        }
                        break;
                    case IConstant.ACTION_WEB_START_SIMULATION:
                        System.out.println("SIMULATION 1");
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                            response.sendRedirect("error.jsp");
                            return;
                        } else {
                            System.out.println("SIMULATION 2");
                            // do actual action!
                            boolean restuls=userSessionBean.startSimulation(simulationIdVal,user.getIdentityId());
                            // should route to the same page ..
                            if(restuls) {
                                //audit trail
                                userSessionBean.addAuditTrail(user.getId(), actionVal,"SimulationId="+simulationIdVal);                                                                
                                request.getRequestDispatcher("SimulationServlet?action=" + IConstant.ACTION_WEB_LIST_SIMULATION)
                                    .forward(request, response);
                            } else {
                                request.getSession().setAttribute("ERROR_MSG", "Unexpected Error!");
                                response.sendRedirect("error.jsp");
                                return;
                            }
                        }
                        break;
                    case IConstant.ACTION_WEB_STOP_SIMULATION:
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
                            response.sendRedirect("error.jsp");
                            return;
                        } else {
                            // do actual action!
                            boolean restuls=userSessionBean.stopSimulation(simulationIdVal,user.getIdentityId());
                            // should route to the same page ..
                            if(restuls) {
                                //audit trail
                                userSessionBean.addAuditTrail(user.getId(), actionVal,"SimulationId="+simulationIdVal);                                                                
                                request.getRequestDispatcher("SimulationServlet?action=" + IConstant.ACTION_WEB_LIST_SIMULATION)
                                    .forward(request, response);
                            } else {
                                request.getSession().setAttribute("ERROR_MSG", "Unexpected Error!");
                                response.sendRedirect("error.jsp");
                                return;
                            }
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

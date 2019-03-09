/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This class is responsible for managing different
 * IoT schedulers.
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
import osa.ora.iot.db.beans.Scheduler;
import osa.ora.iot.db.beans.Users;
import osa.ora.iot.db.session.UserSessionBean;

/**
 * IoTWebSchedulerServlet
 * @author ooransa
 */
@WebServlet(name = "IoTWebSchedulerServlet", urlPatterns = {"/SchedulerServlet"})
public class IoTWebSchedulerServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    @EJB(beanName = "UserSessionBean")
    UserSessionBean userSessionBean;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public IoTWebSchedulerServlet() {
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
            String schedulerId = request.getParameter("schedulerId");
            List<Scheduler> userSchedulerList = null;
            if (action == null) {
                request.getSession().setAttribute("ERROR_MSG", "Missing required parameters!");
                response.sendRedirect("error.jsp");
                return;
            } else {
                int actionVal = 0;
                int schedulerIdVal=0;
                try {
                    actionVal = Integer.parseInt(action);
                    if(schedulerId!=null) schedulerIdVal=Integer.parseInt(schedulerId);
                } catch (Exception ex) {
                    // do no thing ...
                }
                switch (actionVal) {
                    case IConstant.ACTION_WEB_LIST_SCHEDULERS: 
                        userSchedulerList = userSessionBean.getIdentitySchedulers(user.getIdentityId());
                        request.getSession().setAttribute("SCHEDULER_LIST", userSchedulerList);
                        List<Devices> userDeviceList = userSessionBean.getUserDevices(user.getIdentityId(),true,false,null);
                        request.getSession().setAttribute("DEVICE_LIST", userDeviceList);
                        request.getRequestDispatcher("schedulerList.jsp").forward(request, response);
                        break;
                    case IConstant.ACTION_WEB_REGISTER_SCHEDULER:
                        userSchedulerList = userSessionBean.getIdentitySchedulers(user.getIdentityId());
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY || (userSchedulerList!=null && userSchedulerList.size()>=50)) {
                            request.getSession().setAttribute("ERROR_MSG", "Missing The Privilages or Exceeded the Limit!");
                            response.sendRedirect("error.jsp");
                            return;
                        } else {
                            // do actual action!
                            String name=request.getParameter("name");
                            String day=request.getParameter("day");
                            String hour=request.getParameter("hour");
                            String min=request.getParameter("min");
                            String target=request.getParameter("target");
                            String status=request.getParameter("status");
                            String targetAction=request.getParameter("target_action");
                            if(name==null || day==null || hour==null || min==null || target==null || targetAction==null || status==null){
                                response.sendRedirect("error.jsp");
                            }else{
                                //id should come from the DB ...
                                Scheduler newScheduler=userSessionBean.addNewScheduler(Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(min), target, Integer.parseInt(targetAction), user.getIdentityId(), name,Integer.parseInt(status));
                                //audit trail
                                userSessionBean.addAuditTrail(user.getId(), actionVal,"SchedularId="+newScheduler.getId());                                                                
                                userSchedulerList = userSessionBean.getIdentitySchedulers(user.getIdentityId());
                                request.getSession().setAttribute("SCHEDULER_LIST", userSchedulerList);
                                request.getRequestDispatcher("schedulerList.jsp").forward(request, response);
                            }
                        }
                        break;
                    case IConstant.ACTION_WEB_DELETE_SCHEDULER:
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            response.sendRedirect("error.jsp");
                        } else {
                            // do actual action!
                            boolean restuls=userSessionBean.removeScheduler(schedulerIdVal,user.getIdentityId());
                            // should route to the same page ..
                            if(restuls) {
                                //audit trail
                                userSessionBean.addAuditTrail(user.getId(), actionVal,"SchedularId="+schedulerIdVal);                                                                
                                request.getRequestDispatcher("SchedulerServlet?action=" + IConstant.ACTION_WEB_LIST_SCHEDULERS)
                                    .forward(request, response);
                            } else {
                                response.sendRedirect("error.jsp");
                            }
                        }
                        break;
                    case IConstant.ACTION_WEB_ACTIVATE_SCHEDULER:
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            response.sendRedirect("error.jsp");
                        } else {
                            // do actual action!
                            boolean restuls=userSessionBean.activateScheduler(schedulerIdVal,user.getIdentityId());
                            // should route to the same page ..
                            if(restuls) {
                                //audit trail
                                userSessionBean.addAuditTrail(user.getId(), actionVal,"SchedularId="+schedulerIdVal);                                                                
                                request.getRequestDispatcher("SchedulerServlet?action=" + IConstant.ACTION_WEB_LIST_SCHEDULERS)
                                    .forward(request, response);
                            } else {
                                response.sendRedirect("error.jsp");
                            }
                        }
                        break;
                    case IConstant.ACTION_WEB_INACTIVATE_SCHEDULER:
                        if (user.getUserRole() == IConstant.ROLE_READ_ONLY) {
                            response.sendRedirect("error.jsp");
                        } else {
                            // do actual action!
                            boolean restuls=userSessionBean.inactivateScheduler(schedulerIdVal,user.getIdentityId());
                            // should route to the same page ..
                            if(restuls) {
                                //audit trail
                                userSessionBean.addAuditTrail(user.getId(), actionVal,"SchedularId="+schedulerIdVal);                                                                
                                request.getRequestDispatcher("SchedulerServlet?action=" + IConstant.ACTION_WEB_LIST_SCHEDULERS)
                                    .forward(request, response);
                            } else {
                                response.sendRedirect("error.jsp");
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

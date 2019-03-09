/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This class is used to display application images 
 * loaded from the database and stored in the object
 */
package osa.ora.iot;

import java.io.IOException;
import java.io.OutputStream;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import osa.ora.iot.db.beans.Applications;
import osa.ora.iot.db.session.UserSessionBean;

/**
 * ImageViewer
 * @author ooransa
 */
@WebServlet(name = "ImageViewer", urlPatterns = {"/ImageViewer"})
public class ImageViewer extends HttpServlet {
    @EJB(beanName = "UserSessionBean")
    UserSessionBean userSessionBean;        

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Note: no check for user session or object as it can be without autentication
        //id is the application id
        String id=request.getParameter("ID");
        if(id==null){
            return;
        }
        Applications app=userSessionBean.getApplicationById(Integer.parseInt(id));
        //set the content type
        if(app.getBannerName().endsWith(".jpg")){
            response.setContentType("image/jpg");
        }else if(app.getBannerName().endsWith(".png")){
            response.setContentType("image/png");
        }
        OutputStream o = response.getOutputStream();
        //write the banner byte[]
        o.write(app.getBanner());
        o.flush();
        o.close();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}

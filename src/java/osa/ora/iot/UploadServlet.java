/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This class is responsible to upload new device model
 * firmware and its source code.
 */
package osa.ora.iot;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import osa.ora.iot.db.beans.Users;
/**
 * UpdateServlet
 * @author ooransa
 */
@WebServlet(name = "UploadServlet", urlPatterns = {"/UploadServlet"})
@MultipartConfig
public class UploadServlet extends HttpServlet {

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
        if (request.getSession().getAttribute("USER") == null) {
            request.getSession().setAttribute("ERROR_MSG", "Your session is expired!");
            response.sendRedirect("error.jsp");
            return;
        }
        Users user = (Users) request.getSession().getAttribute("USER");
        String type = (String) request.getParameter("type");
        if(user.getSystemAdmin()!=1 || type==null){
            request.getSession().setAttribute("ERROR_MSG", "Missing The Required Privilages!");
            response.sendRedirect("error.jsp");
            return;                    
        }
        try {
            Part filePart = request.getPart("file"); // Retrieves <input type="file" name="file">
            String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString(); // MSIE fix.
            System.out.println("FileName="+fileName);
            System.out.println("Type="+type);
            InputStream inputStream = filePart.getInputStream();            
            final ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            int read;
            while ((read = inputStream.read(buf)) != -1) {
                out.write(buf, 0, read);
            }
            if("1".equals(type)){//upload d1 firmware
                request.getSession().setAttribute("d1FileName", fileName);
                request.getSession().setAttribute("d1FileData", out.toByteArray());
                response.sendRedirect("updateFirmware.jsp");
            } else if("3".equals(type)){ //upload source code firmware
                request.getSession().setAttribute("inoFileName", fileName);
                request.getSession().setAttribute("inoFileData", out.toByteArray());
                response.sendRedirect("updateFirmware.jsp");
            }else {
                request.getSession().setAttribute("ERROR_MSG", "Invalid Upload Options!");
                response.sendRedirect("error.jsp");
                return;
            }
        } catch (IOException e) {
            request.getSession().setAttribute("ERROR_MSG", "Error During Upload Firmware!");
            response.sendRedirect("error.jsp");
            return;
        }

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

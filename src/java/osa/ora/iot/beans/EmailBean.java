/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This class is a wrapper class for eamil object.
 */
package osa.ora.iot.beans;

import java.awt.image.BufferedImage;
import java.io.File;

/**
 * UpdateServlet
 * @author ooransa
 */
public class EmailBean {
    private String[] toUserEmail;
    private String subject;
    private String body;
    private BufferedImage myImage;
    private File file;
    private boolean isPicture;

    /**
     * @return the toUserEmail
     */
    public String[] getToUserEmail() {
        return toUserEmail;
    }

    /**
     * @param toUserEmail the toUserEmail to set
     */
    public void setToUserEmail(String[] toUserEmail) {
        this.toUserEmail = toUserEmail;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the myImage
     */
    public BufferedImage getMyImage() {
        return myImage;
    }

    /**
     * @param myImage the myImage to set
     */
    public void setMyImage(BufferedImage myImage) {
        this.myImage = myImage;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * @return the isPicture
     */
    public boolean isIsPicture() {
        return isPicture;
    }

    /**
     * @param isPicture the isPicture to set
     */
    public void setIsPicture(boolean isPicture) {
        this.isPicture = isPicture;
    }
}

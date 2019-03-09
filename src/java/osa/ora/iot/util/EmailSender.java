/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This class is responsible for sending emails
 * using gmail account.
 */
package osa.ora.iot.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import osa.ora.iot.beans.EmailBean;
import osa.ora.iot.db.beans.SystemConfig;
/**
 * EmailSender
 * @author Osama Oransa
 */
public class EmailSender {
    private boolean emailSendEnablement=true;
    private String smtpServerIP="";
    private String smtpServerPort="";
    private boolean useSSL = true;
    private String smtpServerUser="";
    private String smtpServerPassword="";
    private boolean initialized=false;
    private Session session;
    private Transport transport;
    /**
     * singleton variable
     */
    final private static EmailSender emailSender= new EmailSender();
    /**
     * get singleton method
     * @return 
     */
    public static EmailSender getInstance(){
        return emailSender;
    }
    /**
     * private constructor
     */
    private EmailSender(){
    }

    /**
     * this public method used to send the mail in a thread
     * @param toUserEmail
     * @param subject
     * @param body
     * @param myImage
     * @param file
     * @param isPicture
     */
    public boolean sendMail(EmailBean email,boolean overrideSendemail) {
        if(!emailSendEnablement && overrideSendemail!=true){
            //TODO: remove this for security reason, this is only added during early development phase :)
            System.out.println("Email message:"+email.getBody());
            System.out.println("Email sending disabled, please enable it to send emails!");
            return false;
        }
        try {
            //session.setDebug(false);
            // create a message
            Message msg = new MimeMessage(session);
            //msg.setHeader("Smart IoT", "By Osama Oransa");
            // set the from and to address
            InternetAddress addressFrom = new InternetAddress(smtpServerUser);
            msg.setFrom(addressFrom);
            msg.setHeader("Content-Type", "text/html; charset=UTF-8");
            InternetAddress[] addressTo = new InternetAddress[email.getToUserEmail().length];
            for (int i = 0; i < email.getToUserEmail().length; i++) {
                addressTo[i] = new InternetAddress(email.getToUserEmail()[i]);
            }
            msg.setRecipients(Message.RecipientType.TO, addressTo);
            String[] messageParts = email.getBody().split("##");
            //no thing attached
            if (messageParts.length == 1) {
                msg.setContent(email.getBody(), "text/html; charset=UTF-8");
                //image is attached
                //body is text and image
            } else if (messageParts.length == 2) {
                MimeMultipart multipart = new MimeMultipart();//"related");
                MimeBodyPart messageBodyPart;
                // first part html text before image
                messageBodyPart = new MimeBodyPart();
                if (email.isIsPicture()) {
                    messageBodyPart.setContent(messageParts[0] + "<br><img src=\"cid:image\"><br>" + messageParts[1], "text/html");
                    multipart.addBodyPart(messageBodyPart);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    boolean readingImageResult;
                    try {
                        readingImageResult = ImageIO.write(email.getMyImage(), "jpg", byteArrayOutputStream);
                        Logger.getLogger(EmailSender.class.getName()).log(Level.INFO,"Result of writing the image is " + readingImageResult);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                    MimeBodyPart messageBodyPart2 = new MimeBodyPart();
                    DataSource fds = new ByteArrayDataSource(byteArray, "image/jpg");
                    messageBodyPart2.setDataHandler(new DataHandler(fds));
                    messageBodyPart2.setHeader("Content-ID", "<image>");
                    multipart.addBodyPart(messageBodyPart2);
                    msg.setContent(multipart);
                } else {
                    //is file attached, like voice....
                    messageBodyPart.setContent(messageParts[0] + "<br>" + messageParts[1], "text/html");
                    multipart.addBodyPart(messageBodyPart);
                    // Part two is attachment
                    MimeBodyPart messageBodyPart2 = new MimeBodyPart();
                    DataSource source = new FileDataSource(email.getFile());
                    messageBodyPart2.setDataHandler(new DataHandler(source));
                    messageBodyPart2.setFileName(email.getFile().getName());
                    multipart.addBodyPart(messageBodyPart2);
                    msg.setContent(multipart);
                }
            }
            // Setting the Subject and Content Type
            msg.setSubject(email.getSubject());
            msg.saveChanges();
            //Transport.send(msg);
            if(!transport.isConnected()){
                transport = session.getTransport("smtp");
                transport.connect();            
                initialized = true;
            }
            transport.sendMessage(msg, addressTo);
            Logger.getLogger(EmailSender.class.getName()).log(Level.INFO, "Message sent to {0} emails.", email.getToUserEmail().length);
            return true;
        } catch (MessagingException ex) {
            Logger.getLogger(EmailSender.class.getName()).log(Level.SEVERE, null, ex);
            //set initialization to false to re-authenticate again
            initialized = false;
            return false;
        }
    }
    /**
     * @return the initialized
     */
    public boolean isInitialized() {
        return initialized;
    }

    /**
     * @param initialized the initialized to set
     */
    public void setInitialized(boolean initialized) {
        this.initialized = initialized;
    }

    public void init(SystemConfig systemConfigurations) {
        emailSendEnablement=systemConfigurations.getEmailEnabled()==1;
        System.out.println("Enablement of email:"+emailSendEnablement);
        smtpServerIP=systemConfigurations.getSmtpServerIp();
        smtpServerPort=systemConfigurations.getSmtpPort();
        useSSL=systemConfigurations.getUseSSL()==1;
        smtpServerUser=systemConfigurations.getEmailUser();
        smtpServerPassword=systemConfigurations.getEmailPassword();
        try {
            Properties props = new Properties();
            props.put("mail.smtp.host", smtpServerIP);
            props.put("mail.smtp.port", smtpServerPort);
            props.put("mail.smtp.auth", "true");
            props.put("mail.debug", "false");
            props.put("mail.smtp.socketFactory.port", smtpServerPort);
            props.put("mail.smtp.starttls.enable", "true");
            //next attributes for google gmail for SSL
            if (useSSL) {
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.fallback", "false");
            }
            Authenticator auth = new SMTPAuthenticator();
            session = Session.getInstance(props, auth);
            transport = session.getTransport("smtp");
            transport.connect();            
            initialized = true;
        } catch (Exception ex) {
            ex.printStackTrace();
            Logger.getLogger(EmailSender.class.getName()).log(Level.INFO,"Exception " + ex);
        }
    }
    // inner class that is used for authentication purposes
    private class SMTPAuthenticator extends javax.mail.Authenticator {
        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            Logger.getLogger(EmailSender.class.getName()).log(Level.INFO,"call authentication");
            String username = smtpServerUser;
            String password = smtpServerPassword;
            return new PasswordAuthentication(username, password);
        }
    }
    public EmailBean applyParameters(String body, String subject, String[]parameters){
        EmailBean emailMessage=new EmailBean();
        emailMessage.setBody(body);
        emailMessage.setSubject(subject);
        if(parameters!=null && parameters.length>0){
            int order=1;
            for(int i=0;i<parameters.length;i++){
                //int position=message.indexOf("?"+order);
                //if(position!=-1){
                if(parameters[i]!=null){
                    String toReplace="#"+order;
                    emailMessage.setBody(emailMessage.getBody().replaceAll(toReplace, parameters[i]));
                    order++;
                }
                //}
            }
        }
        return emailMessage;
    }
    
    public static void sendEmailForAction(final String[] parameters,
            final String body, final String subject, final String userEmail, SystemConfig systemConfigurations, 
            final boolean overrideSendemail) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Sending email to user " + userEmail);
                EmailSender sender = EmailSender.getInstance();
                if (systemConfigurations.getEmailEnabled()==1 || overrideSendemail) {
                    sender.init(systemConfigurations);
                    EmailBean emailMessage = sender.applyParameters(body,subject, parameters);
                    emailMessage.setToUserEmail(userEmail.split(","));
                    sender.sendMail(emailMessage, overrideSendemail);
                } else {
                    System.out.println("Sending email is disabled");
                }
            }
        }).start();
    }
}

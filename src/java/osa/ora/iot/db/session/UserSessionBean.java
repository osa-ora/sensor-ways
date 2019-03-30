/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This class is responsible for all database interactions
 * using JPA plus scheduler(s) execution.
 */
package osa.ora.iot.db.session;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import osa.ora.iot.db.beans.Messages;
import osa.ora.iot.beans.IConstant;
import osa.ora.iot.db.beans.ActionsLov;
import osa.ora.iot.db.beans.Applications;
import osa.ora.iot.db.beans.AuditTrail;
import osa.ora.iot.db.beans.BarCodes;
import osa.ora.iot.db.beans.DeviceGroup;
import osa.ora.iot.db.beans.DeviceModel;
import osa.ora.iot.db.beans.Devices;
import osa.ora.iot.db.beans.EmailTemplates;
import osa.ora.iot.db.beans.HwType;
import osa.ora.iot.db.beans.IdentityNotifications;
import osa.ora.iot.db.beans.RecievedMessages;
import osa.ora.iot.db.beans.Scheduler;
import osa.ora.iot.db.beans.Simulations;
import osa.ora.iot.db.beans.SystemConfig;
import osa.ora.iot.db.beans.SystemJobs;
import osa.ora.iot.db.beans.TenantSettings;
import osa.ora.iot.db.beans.Users;
import osa.ora.iot.db.beans.Workflows;
import osa.ora.iot.util.EmailSender;
import osa.ora.iot.util.IoTUtil;
import osa.ora.iot.util.PasswordAuthentication;

/**
 * UserSessionBean
 * @author ooransa
 */

@Singleton
public class UserSessionBean extends AbstractFacade<Users> {

    @PersistenceContext(unitName = "IoTHubPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserSessionBean() {
        super(Users.class);
    }
    /**
     * Monthly statistics job scheduler
     */
    @Schedule(dayOfMonth = "1",hour = "0", minute = "0", second = "0", persistent = false)
    public void monthlyResetExecution() {
        System.out.println("Monthly Job Execution!");
        SystemJobs monthlyJob=em.find(SystemJobs.class, IConstant.MONTHLY_JOB);
        if(monthlyJob.getStatus()!=1){
            System.out.println("Monthly Reset Execution is Disabled!");
            return;
        }
        try{
            //update all monthly columns to 0 in devices table
            Query updateQuery = em.createNativeQuery("UPDATE DEVICES set total_inbound_month=0, total_outbound_month=0, total_messages_month=0");
            int totalRows = updateQuery.executeUpdate();
            monthlyJob.setLastExecutionResults(1);
            monthlyJob.setLastExecutionRows(totalRows);
            monthlyJob.setLastExecution(new Date());
            monthlyJob.setExecutionLog(null);
            em.persist(monthlyJob);
        } catch(Throwable t){
            t.printStackTrace();
            monthlyJob.setLastExecutionResults(0);
            monthlyJob.setLastExecutionRows(0);
            monthlyJob.setLastExecution(new Date());
            monthlyJob.setExecutionLog(t.getMessage());
            em.persist(monthlyJob);            
        }
    }
    /**
     * Purge execution job scheduler to purge all messages, notifications and audit records.
     */
    @Schedule(hour = "0", minute = "0", second = "0", persistent = false)
    public void purgeExecution() {
        SystemJobs purgeJob=em.find(SystemJobs.class, IConstant.PURGE_JOB);
        if(purgeJob.getStatus()!=1){
            System.out.println("Purge Execution is Disabled!");
            return;
        }
        try{
            Calendar startDate = Calendar.getInstance();
            long duration=purgeJob.getParam1Value()*24*60*60000;//1296000000 = 15 days
            startDate.setTimeInMillis(startDate.getTimeInMillis() - duration);
            System.out.println("Will delete all messages before:"+startDate);
            int totalRows=purgeOldMessages(startDate.getTime());
            System.out.println("Will delete all notification alerts before:"+startDate);
            totalRows+=purgeOldIdentityNotifications(startDate.getTime());
            System.out.println("Will delete all audit trails before:"+startDate);
            totalRows+=purgeOldAuditRecords(startDate.getTime());        
            purgeJob.setLastExecutionResults(1);
            purgeJob.setLastExecutionRows(totalRows);
            purgeJob.setLastExecution(new Date());
            purgeJob.setExecutionLog(null);
            em.persist(purgeJob);
        } catch(Throwable t){
            t.printStackTrace();
            purgeJob.setLastExecutionResults(0);
            purgeJob.setLastExecutionRows(0);
            purgeJob.setLastExecution(new Date());
            purgeJob.setExecutionLog(t.getMessage());
            em.persist(purgeJob);            
        }
    }
    /**
     * Run IoT scheduler job
     */
    @Schedule(minute = "*/1", hour = "*",second = "0", persistent = false)
    public void schedulerExecution() {
        SystemJobs schedulerJob = em.find(SystemJobs.class, IConstant.SCHEDULER_JOB);
        if(schedulerJob.getStatus()!=1){
            System.out.println("Scheduler Execution is Disabled!");
            return;
        }        
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.MINUTE) % schedulerJob.getParam1Value() == 0) {
            System.out.println("Scheduler Runner Execution");            
            try {
                int noOfSchedulers = runSchedulers(now);
                schedulerJob.setLastExecutionResults(1);
                schedulerJob.setLastExecutionRows(noOfSchedulers);
                schedulerJob.setLastExecution(new Date());
                schedulerJob.setExecutionLog(null);
                em.persist(schedulerJob);
            } catch (Throwable t) {
                t.printStackTrace();
                schedulerJob.setLastExecutionResults(0);
                schedulerJob.setLastExecutionRows(0);
                schedulerJob.setLastExecution(new Date());
                schedulerJob.setExecutionLog(t.getMessage());
                em.persist(schedulerJob);
            }
        }
    }
    /**
     * Move recieved message to permenant store
     */
    @Schedule(minute = "*", hour = "*",second = "*/5", persistent = false)
    public void moveMessagesToStore() {
        SystemJobs recieveMsgJob = em.find(SystemJobs.class, IConstant.RECIEVE_JOB);
        if(recieveMsgJob.getStatus()!=1){
            System.out.println("Recieve Message Execution is Disabled!");
            return;
        }        
        //System.out.println("Recieve Message Execution");            
        try {
            int noOfMessages = runReciever();
            recieveMsgJob.setLastExecutionResults(1);
            recieveMsgJob.setLastExecutionRows(noOfMessages);
            recieveMsgJob.setLastExecution(new Date());
            recieveMsgJob.setExecutionLog(null);
            em.persist(recieveMsgJob);
        } catch (Throwable t) {
            t.printStackTrace();
            recieveMsgJob.setLastExecutionResults(0);
            recieveMsgJob.setLastExecutionRows(0);
            recieveMsgJob.setLastExecution(new Date());
            recieveMsgJob.setExecutionLog(t.getMessage());
            em.persist(recieveMsgJob);
        }
    }
    /**
     * Execute IoT Simulation
     */
    @Schedule(minute = "*", hour = "*",second = "*/15", persistent = false)
    public void simulatorsExecution() {
        SystemJobs simulatorJob = em.find(SystemJobs.class, IConstant.SIMULATION_JOB);
        if(simulatorJob.getStatus()!=1){
            System.out.println("Simulation Execution is Disabled!");
            return;
        }
        System.out.println("Simulator Execution");
        try {
            int noOfSimulators=runSimulators();
            simulatorJob.setLastExecutionResults(1);
            simulatorJob.setLastExecutionRows(noOfSimulators);
            simulatorJob.setLastExecution(new Date());
            simulatorJob.setExecutionLog(null);
            em.persist(simulatorJob);
        } catch (Throwable t) {
            t.printStackTrace();
            simulatorJob.setLastExecutionResults(0);
            simulatorJob.setLastExecutionRows(0);
            simulatorJob.setLastExecution(new Date());
            simulatorJob.setExecutionLog(t.getMessage());
            em.persist(simulatorJob);
        }        
    }
    /**
     * Offline device detection
     */
    @Schedule(minute = "*/1", hour = "*",second = "0", persistent = false)
    public void offlineDetectionTimeout() {
        System.out.println("Device Offline Detection execution run");
        SystemJobs offlineJob = em.find(SystemJobs.class, IConstant.OFFLINE_JOB);
        if(offlineJob.getStatus()!=1){
            System.out.println("Offline Detection Execution is Disabled!");
            return;
        } 
        try {
            int offlineDetectedDevices=offlineDeviceDetection(offlineJob.getParam1Value()*1000);
            System.out.println("Device Offline Detection: Detected:" +offlineDetectedDevices);
            offlineJob.setLastExecutionResults(1);
            offlineJob.setLastExecutionRows(offlineDetectedDevices);
            offlineJob.setLastExecution(new Date());
            offlineJob.setExecutionLog(null);
            em.persist(offlineJob);
        } catch (Throwable t) {
            t.printStackTrace();
            offlineJob.setLastExecutionResults(0);
            offlineJob.setLastExecutionRows(0);
            offlineJob.setLastExecution(new Date());
            offlineJob.setExecutionLog(t.getMessage());
            em.persist(offlineJob);
        }        
    }
    private int runReciever() {
        List<RecievedMessages> messageList = null;
        Query query = em.createQuery("SELECT m FROM RecievedMessages m order by m.id asc");
        //query.setMaxResults(100);
        try {
            messageList = (List<RecievedMessages>) query.getResultList();
            if(messageList==null || messageList.isEmpty()){
                return 0;
            }
            for(RecievedMessages message:messageList){
                //TODO: check if another message with same timestamp for this device is there or not
                //if not proceed with this logic
                Messages newMessage=new Messages();
                int typeVal = message.getType();
                newMessage.setDeviceId(message.getDeviceId());
                newMessage.setMessageTime(message.getMessageTime());
                newMessage.setPayload(message.getPayload());
                newMessage.setType(typeVal);
                em.persist(newMessage);
                //delete the message now
                em.remove(message);
                Devices device = getDeviceById(newMessage.getDeviceId(),true);
                System.out.println("Will assess workflow for this new message!");
                //send email for alert logic here
                //login can be moved into a separate scheduler ...
                if (typeVal == 2 || typeVal == 4) {                         
                    sendPayloadAlertNotification(device, newMessage);
                }else if(typeVal == 5){
                    sendFaultAlertNotification(device, newMessage);
                }             
                //check workflow related to this message ...
                List<Workflows> userWorkflowList = getIdentityActiveWorkflows(device.getIdentity());
                if (userWorkflowList != null && userWorkflowList.size() > 0) {
                    System.out.println("Some workflows!!!");
                    for (int i = 0; i < userWorkflowList.size(); i++) {
                        Workflows workflow = userWorkflowList.get(i);
                        if(workflow.getTriggerType()==1){//device as a source for workflow
                            if (workflow.getTriggeringId().equals(newMessage.getDeviceId())) {
                                if (typeVal == 2 && workflow.getTriggerEvent() == IConstant.ACTION_RECIEVE_HIGH_ALERT
                                        || typeVal == 4 && workflow.getTriggerEvent() == IConstant.ACTION_RECIEVE_LOW_ALERT
                                        || typeVal == 6 && workflow.getTriggerEvent() == IConstant.ACTION_RECIEVE_ERROR
                                        || typeVal == 1 && workflow.getTriggerEvent() == IConstant.ACTION_RECIEVE_NORMAL) {
                                       // || matchTrigger(newMessage.getPayload(), workflow.getTriggerEvent())) 
                                       //remove any on/off related workflows
                                    System.out.println("Workflow need to be fired!");
                                    setDeviceLastActionByWorkflow(workflow.getTargetAction(), workflow.getTargetDeviceId(), IConstant.ACTION_BY_WORKFLOW);
                                }
                            }
                        }else{ //device group as a source for workflow
                            if (workflow.getTriggeringId().equals(device.getGroupId())) {
                                if (typeVal == 2 && workflow.getTriggerEvent() == IConstant.ACTION_RECIEVE_HIGH_ALERT
                                        || typeVal == 4 && workflow.getTriggerEvent() == IConstant.ACTION_RECIEVE_LOW_ALERT
                                        || typeVal == 6 && workflow.getTriggerEvent() == IConstant.ACTION_RECIEVE_ERROR
                                        || typeVal == 1 && workflow.getTriggerEvent() == IConstant.ACTION_RECIEVE_NORMAL){
                                        //|| matchTrigger(newMessage.getPayload(), workflow.getTriggerEvent())) {
                                        //remove any on/off triggers
                                    System.out.println("Workflow need to be fired!");
                                    setDeviceLastActionByWorkflow(workflow.getTargetAction(), workflow.getTargetDeviceId(), IConstant.ACTION_BY_WORKFLOW);
                                }
                            }                            
                        }
                    }
                }
            }
            return messageList.size();
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load any recieved messages!");
            ex.printStackTrace();
            return 0;
        }
    }
    /**
     * Method to do action based on new firmware version uploading
     * @param existingModel
     * @param newVersion 
     */
    public void actionDevicesUponFirmwareUpdate(DeviceModel existingModel, int newVersion) {
        restartDevices(existingModel,newVersion);        
        sendNewFirmwareEmails(existingModel,newVersion);
    }
    /**
     * Restart devices request upon new firmware version to pick the new device model firmware during login
     * @param existingModel
     * @param newVersion 
     */
    private void restartDevices(DeviceModel existingModel, int newVersion) {
        List<Devices> deviceList = null;
        //re-start devices where device management is 2 i.e. apply new firmware immediatrly by restart the device
        Query query = em.createQuery("SELECT d FROM Devices d WHERE d.deviceStatus=1 and d.deviceManagement=2 and d.deviceModel=:deviceModel");
        query.setParameter("deviceModel", existingModel.getId());
        try {
            deviceList = (List<Devices>) query.getResultList();
            if (deviceList != null && !deviceList.isEmpty()) {
                System.out.println("Detected "+deviceList.size());
                for (Devices device : deviceList) {
                    device.setLastAction(IConstant.ACTION_RESTART);// restart or login same effect ..
                    em.persist(device);
                }
            }
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't detect any device with this new device model for restart!");
            ex.printStackTrace();
        }        
    }
    /**
     * Send notification alert that a new device model firmware is available
     * @param existingModel
     * @param newVersion 
     */
    private void sendNewFirmwareEmails(DeviceModel existingModel, int newVersion) {
        List<Devices> deviceList = null;
        //send email where device management is 1 i.e. wait but send notification to the device contact person
        Query query = em.createQuery("SELECT d FROM Devices d WHERE d.deviceStatus=1 and d.deviceModel=:deviceModel");
        query.setParameter("deviceModel", existingModel.getId());
        try {
            deviceList = (List<Devices>) query.getResultList();
            if (deviceList != null && !deviceList.isEmpty()) {
                System.out.println("Detected "+deviceList.size());
                for (Devices device : deviceList) {
                    sendNewFirmwareEmail(device,newVersion);
                }
            }
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't detect any device with this new device model for notification!");
            ex.printStackTrace();
        }        
    }
    /**
     * run offline device detection algorithm
     * @param duration
     * @return 
     */
    private int offlineDeviceDetection(int duration){
        List<Devices> offlineDeviceList = null;
        Query query = em.createQuery("SELECT d FROM Devices d WHERE d.deviceStatus=1 and d.offlineFlag = 1 and d.lastPingTime<:lastPingTime");
        Calendar cal=Calendar.getInstance();
        cal.setTimeInMillis(cal.getTimeInMillis()-(duration));
        query.setParameter("lastPingTime", cal.getTime());
        try {
            offlineDeviceList = (List<Devices>) query.getResultList();
            if (offlineDeviceList != null && !offlineDeviceList.isEmpty()) {
                System.out.println("Detected "+offlineDeviceList.size());
                for (Devices device : offlineDeviceList) {
                    //flag this device as offline after sending the notification
                    //now this device won't have offline detection again unless it went online
                    sendOfflineEmail(device);
                    device.setOfflineFlag(IConstant.STATUS_OFFLINE);
                    em.persist(device);
                }
                return offlineDeviceList.size();
            }
            return 0;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't detect any offline devices!");
            ex.printStackTrace();
            return 0;
        }        
    }
    /**
     * Send new firmware available email notification
     * @param device
     * @param newVersion 
     */
    private void sendNewFirmwareEmail(Devices device, int newVersion) {
        boolean sendNotification=createNotificationForIdentity(true,device.getIdentity(),device.getNotificationEmail()+"#"+device.getDeviceId()+"#"+device.getLocation()+"#"+newVersion, 
                IConstant.ACTION_WEB_SAVE_FIRMWARE,device.getDeviceId(),loadIdentitySettings(device.getIdentity()).getAlertGracePeriod());
        if (sendNotification==true && loadIdentitySettings(device.getIdentity()).getAlertFirmwareAvailable()== IConstant.EMAIL_ENABLED) {
            EmailTemplates template = loadNotificationTemplate(IConstant.ACTION_WEB_SAVE_FIRMWARE);
            String body = IoTUtil.getEmailBodyLanguage(template, getSystemConfigurations().getDefaultEmailLanguage());
            String subject = IoTUtil.getEmailSubjectLanguage(template, getSystemConfigurations().getDefaultEmailLanguage());
            EmailSender.sendEmailForAction(new String[]{device.getNotificationEmail(), device.getDeviceId(), device.getLocation(),""+newVersion}, body, subject,
                    device.getNotificationEmail(), getSystemConfigurations(), false);
        }
        
    }
    /**
     * Send device offline notification
     * @param device 
     */
    private void sendOfflineEmail(Devices device) {
        boolean sendNotification=createNotificationForIdentity(true,device.getIdentity(),device.getNotificationEmail()+"#"+device.getDeviceId()+"#"+device.getLocation(), 
                IConstant.ACTION_DEVICE_OFFLINE,device.getDeviceId(),loadIdentitySettings(device.getIdentity()).getAlertGracePeriod());
        if (sendNotification==true && loadIdentitySettings(device.getIdentity()).getAlertEmailOffline()== IConstant.EMAIL_ENABLED) {
            EmailTemplates template = loadNotificationTemplate(IConstant.ACTION_DEVICE_OFFLINE);
            String body = IoTUtil.getEmailBodyLanguage(template, getSystemConfigurations().getDefaultEmailLanguage());
            String subject = IoTUtil.getEmailSubjectLanguage(template, getSystemConfigurations().getDefaultEmailLanguage());
            EmailSender.sendEmailForAction(new String[]{device.getNotificationEmail(), device.getDeviceId(), device.getLocation()}, body, subject,
                    device.getNotificationEmail(), getSystemConfigurations(), false);
        }
        
    }
    /**
     * Send device online notification
     * @param device 
     */
    private void sendOnlineEmail(Devices device) {
        boolean sendNotification=createNotificationForIdentity(true,device.getIdentity(),device.getNotificationEmail()+"#"+device.getDeviceId()+"#"+device.getLocation(),
                IConstant.ACTION_DEVICE_ONLINE,device.getDeviceId(),loadIdentitySettings(device.getIdentity()).getAlertGracePeriod());
        if (sendNotification==true && loadIdentitySettings(device.getIdentity()).getAlertEmailOnline()== IConstant.EMAIL_ENABLED) {
            EmailTemplates template = loadNotificationTemplate(IConstant.ACTION_DEVICE_ONLINE);
            String body = IoTUtil.getEmailBodyLanguage(template, getSystemConfigurations().getDefaultEmailLanguage());
            String subject = IoTUtil.getEmailSubjectLanguage(template, getSystemConfigurations().getDefaultEmailLanguage());
            EmailSender.sendEmailForAction(new String[]{device.getNotificationEmail(), device.getDeviceId(), device.getLocation()}, body, subject,
                    device.getNotificationEmail(), getSystemConfigurations(), false);
        }
    }
    /**
     * Send device registration confirmation email
     * @param device 
     */
    public void sendDeviceRegistrationEmail(Devices device) {
        createNotificationForIdentity(false,device.getIdentity(),device.getNotificationEmail()+"#"+device.getDeviceId()+"#"+device.getLocation(), 
                IConstant.ACTION_WEB_REGISTER_DEVICE,device.getDeviceId(),loadIdentitySettings(device.getIdentity()).getAlertGracePeriod());
        if (loadIdentitySettings(device.getIdentity()).getDeviceRegistrationEmail()== IConstant.EMAIL_ENABLED) {
            EmailTemplates template = loadNotificationTemplate(IConstant.ACTION_WEB_REGISTER_DEVICE);
            String body = IoTUtil.getEmailBodyLanguage(template, getSystemConfigurations().getDefaultEmailLanguage());
            String subject = IoTUtil.getEmailSubjectLanguage(template, getSystemConfigurations().getDefaultEmailLanguage());
            EmailSender.sendEmailForAction(new String[]{device.getNotificationEmail(), device.getDeviceId(), device.getLocation()}, body, subject,
                    device.getNotificationEmail(), getSystemConfigurations(), false);
        }
    }
    /**
     * Send device information update confirmation
     * @param device 
     */
    public void sendDeviceUpdateEmail(Devices device) {
        createNotificationForIdentity(false,device.getIdentity(),device.getNotificationEmail()+"#"+device.getDeviceId()+"#"+device.getLocation(), 
                IConstant.ACTION_WEB_EDIT_DEVICE,device.getDeviceId(),loadIdentitySettings(device.getIdentity()).getAlertGracePeriod());
        if (loadIdentitySettings(device.getIdentity()).getDeviceUpdateEmail()== IConstant.EMAIL_ENABLED) {
            EmailTemplates template = loadNotificationTemplate(IConstant.ACTION_WEB_EDIT_DEVICE);
            String body = IoTUtil.getEmailBodyLanguage(template, getSystemConfigurations().getDefaultEmailLanguage());
            String subject = IoTUtil.getEmailSubjectLanguage(template, getSystemConfigurations().getDefaultEmailLanguage());
            EmailSender.sendEmailForAction(new String[]{device.getNotificationEmail(), device.getDeviceId(), device.getLocation()}, body, subject,
                    device.getNotificationEmail(), getSystemConfigurations(), false);
        }
    }
    /**
     * Send device update email to the old email notification
     * @param device
     * @param oldEmail 
     */
    public void sendDeviceUpdateEmailToOldContact(Devices device, String oldEmail) {
        createNotificationForIdentity(false,device.getIdentity(),oldEmail+"#"+device.getDeviceId()+"#"+device.getLocation()+"#"+device.getNotificationEmail(), 
                IConstant.ACTION_WEB_CHANGE_DEVICE_CONTACT,device.getDeviceId(),loadIdentitySettings(device.getIdentity()).getAlertGracePeriod());
        if (loadIdentitySettings(device.getIdentity()).getDeviceUpdateEmail()== IConstant.EMAIL_ENABLED) {
            EmailTemplates template = loadNotificationTemplate(IConstant.ACTION_WEB_CHANGE_DEVICE_CONTACT);
            String body = IoTUtil.getEmailBodyLanguage(template, getSystemConfigurations().getDefaultEmailLanguage());
            String subject = IoTUtil.getEmailSubjectLanguage(template, getSystemConfigurations().getDefaultEmailLanguage());
            EmailSender.sendEmailForAction(new String[]{oldEmail, device.getDeviceId(), device.getLocation(),device.getNotificationEmail()}, body, subject,
                    oldEmail, getSystemConfigurations(), false);
        }
    }
    /**
     * Send notification for IP change of the IoT device
     * @param device
     * @param newIpAddress 
     */
    public void sendDeviceIpAddressChange(Devices device, String newIpAddress) {
        createNotificationForIdentity(false,device.getIdentity(), device.getNotificationEmail()+"#"+newIpAddress+"#"+device.getDeviceId()+"#"+device.getLocation()+"#"+device.getIpAddress(), 
                IConstant.ACTION_DEVICE_LOGIN_FROM_DIFFERENT_IP,device.getDeviceId(),loadIdentitySettings(device.getIdentity()).getAlertGracePeriod());
        if (loadIdentitySettings(device.getIdentity()).getAlertDeviceIpChange()== IConstant.EMAIL_ENABLED) {
            EmailTemplates template = loadNotificationTemplate(IConstant.ACTION_DEVICE_LOGIN_FROM_DIFFERENT_IP);
            String body = IoTUtil.getEmailBodyLanguage(template, getSystemConfigurations().getDefaultEmailLanguage());
            String subject = IoTUtil.getEmailSubjectLanguage(template, getSystemConfigurations().getDefaultEmailLanguage());
            EmailSender.sendEmailForAction(new String[]{ device.getNotificationEmail(),newIpAddress, device.getDeviceId(), device.getLocation(),device.getIpAddress()}, body, subject,
                    device.getNotificationEmail(), getSystemConfigurations(), false);
        }
    }
    /**
     * Send notification for recieving an alert in the device message payload
     * @param device
     * @param message 
     */
    public void sendPayloadAlertNotification(Devices device, Messages message) {
        //high or low alerts
        System.out.println("Will send notification for Payload alert :)");
        boolean sendNotification=createNotificationForIdentity(true,device.getIdentity(), device.getNotificationEmail() + "#"
                + IoTUtil.payloadExtractorForNotification(device,message.getPayload()) + "#"
                + device.getDeviceId() + "#" + device.getLocation(), 
                IConstant.ACTION_RECIEVE_HIGH_ALERT,device.getDeviceId(),
                loadIdentitySettings(device.getIdentity()).getAlertGracePeriod());
        if (sendNotification==true && loadIdentitySettings(device.getIdentity()).getAlertEmailMessage() == IConstant.EMAIL_ENABLED) {
            EmailTemplates template = loadNotificationTemplate(IConstant.ACTION_RECIEVE_HIGH_ALERT);
            String body = IoTUtil.getEmailBodyLanguage(template, getSystemConfigurations().getDefaultEmailLanguage());
            String subject = IoTUtil.getEmailSubjectLanguage(template, getSystemConfigurations().getDefaultEmailLanguage());
            EmailSender.sendEmailForAction(new String[]{device.getNotificationEmail(),
                IoTUtil.payloadExtractorForNotification(device,message.getPayload()),
                device.getDeviceId(), device.getLocation()}, body, subject,
                device.getNotificationEmail(), getSystemConfigurations(), false);
        }
    }
    /**
     * Send notification for recieving an error in the device message
     * @param device
     * @param message 
     */
    public void sendFaultAlertNotification(Devices device, Messages message) {
        System.out.println("Will send fault message notification:)");
        boolean sendNotification=createNotificationForIdentity(true,device.getIdentity(), device.getNotificationEmail() + "#"
                + IoTUtil.payloadExtractorForNotification(device,message.getPayload()) + "#"
                + device.getDeviceId() + "#" + device.getLocation(), 
                IConstant.ACTION_RECIEVE_ERROR,device.getDeviceId(),
                loadIdentitySettings(device.getIdentity()).getAlertGracePeriod());
        if (sendNotification==true && loadIdentitySettings(device.getIdentity()).getAlertEmailMessage() == IConstant.EMAIL_ENABLED) {
            EmailTemplates template = loadNotificationTemplate(IConstant.ACTION_RECIEVE_ERROR);
            String body = IoTUtil.getEmailBodyLanguage(template, getSystemConfigurations().getDefaultEmailLanguage());
            String subject = IoTUtil.getEmailSubjectLanguage(template, getSystemConfigurations().getDefaultEmailLanguage());
            EmailSender.sendEmailForAction(new String[]{device.getNotificationEmail(),
                IoTUtil.payloadExtractorForNotification(device,message.getPayload()),
                device.getDeviceId(), device.getLocation()}, body, subject,
                device.getNotificationEmail(), getSystemConfigurations(), false);
        }
    }
    /**
     * Send notification for user registration with the temp password
     * @param tempPassword
     * @param name
     * @param user
     * @param emailAddress 
     */
    public void sendUserRegistrationNotification(String tempPassword, String name, Users user, String emailAddress) {
        System.out.println("Send Registration Email for user:" + emailAddress);
        EmailTemplates template=loadNotificationTemplate(IConstant.ACTION_WEB_REGISTER_USER);
        String body=IoTUtil.getEmailBodyLanguage(template,getSystemConfigurations().getDefaultEmailLanguage());
        String subject=IoTUtil.getEmailSubjectLanguage(template,getSystemConfigurations().getDefaultEmailLanguage());
        EmailSender.sendEmailForAction(new String[]{name, tempPassword,""+user.getIdentityId(),"https://"+getSystemConfigurations().getPlatformHost()+":"+getSystemConfigurations().getPlatformHttpsPort()+"/IoTHub"},
                body,subject,
                emailAddress, getSystemConfigurations(),true);
        //add initrnal alert notification
        createNotificationForIdentity(false,user.getIdentityId(), name+"#*******#"+user.getIdentityId()+"#https://"+getSystemConfigurations().getPlatformHost()+":"+getSystemConfigurations().getPlatformHttpsPort()+"/IoTHub",
                IConstant.ACTION_WEB_REGISTER_USER, emailAddress,loadIdentitySettings(user.getIdentityId()).getAlertGracePeriod());
    }
    /**
     * Send notification for registering a new account 
     * @param tempPassword
     * @param name
     * @param identityId
     * @param emailAddress 
     */
    public void sendAccountRegistrationNotification(String tempPassword, String name, int identityId, String emailAddress) {
        System.out.println("Send Registration Email for Account:" + identityId);
        EmailTemplates template=loadNotificationTemplate(IConstant.ACTION_WEB_REGISTER_USER);
        String body=IoTUtil.getEmailBodyLanguage(template,getSystemConfigurations().getDefaultEmailLanguage());
        String subject=IoTUtil.getEmailSubjectLanguage(template,getSystemConfigurations().getDefaultEmailLanguage());
        EmailSender.sendEmailForAction(new String[]{name, tempPassword,""+identityId,"https://"+getSystemConfigurations().getPlatformHost()+":"+getSystemConfigurations().getPlatformHttpsPort()+"/IoTHub"},
                body,subject,
                emailAddress, getSystemConfigurations(),true);
        //add initrnal alert notification
        createNotificationForIdentity(false,identityId,  name+"#*******#"+identityId+"#https://"+getSystemConfigurations().getPlatformHost()+":"+getSystemConfigurations().getPlatformHttpsPort()+"/IoTHub",
                IConstant.ACTION_WEB_REGISTER_USER, emailAddress,100);
    }
    /**
     * Send account update notification
     * @param name
     * @param identityId
     * @param emailAddress 
     */
    public void sendAccountUpdateNotification(String name, int identityId, String emailAddress) {
        System.out.println("Send Update Email for account:" + identityId);
        EmailTemplates template=loadNotificationTemplate(IConstant.ACTION_WEB_UPDATE_ACCOUNT);
        String body=IoTUtil.getEmailBodyLanguage(template,getSystemConfigurations().getDefaultEmailLanguage());
        String subject=IoTUtil.getEmailSubjectLanguage(template,getSystemConfigurations().getDefaultEmailLanguage());
        EmailSender.sendEmailForAction(new String[]{name,""+identityId,emailAddress},
                body,subject,
                emailAddress, getSystemConfigurations(),true);
        //add initrnal alert notification
        createNotificationForIdentity(false,identityId,  name+"#"+identityId+"#"+emailAddress,
                IConstant.ACTION_WEB_UPDATE_ACCOUNT, emailAddress,100);
    }
    /**
     * Create internal notification for specific template and identity
     * @param checkGrace
     * @param identityId
     * @param params
     * @param templateId
     * @param uniqueIdentifier
     * @param gracePeriod
     * @return 
     */
    public boolean createNotificationForIdentity(boolean checkGrace,int identityId,String params, int templateId, String uniqueIdentifier, int gracePeriod) {
        try{
            if (checkGrace) {
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(cal.getTimeInMillis() - 60000 * gracePeriod);
                if (checkExistingNotificationByTime(identityId, uniqueIdentifier, cal.getTime(), templateId) == true) {
                    System.out.println("Will suppress the notification being sent before in same grace period:" + params);
                    return false;
                }
            }
            IdentityNotifications notification=new IdentityNotifications();
            notification.setIdentityId(identityId);
            notification.setNotifiedOn(new Date());
            notification.setReadFlag((short)0);
            notification.setTemplateId(templateId);
            notification.setParams(params);
            notification.setUniqueIdentiter(uniqueIdentifier);
            em.persist(notification);
        }catch(Throwable t){
            t.printStackTrace();
        }
        return true;
    }
    /**
     * Actual execution of IoT devices schedulers
     * @param now
     * @return 
     */
    private int runSchedulers(Calendar now) {
        System.out.println("**************Will load data of " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE));
        List<Scheduler> userSchedulerList = null;
        userSchedulerList = getActiveScheduler(now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);
        if(userSchedulerList==null){
            System.out.println("* No schedular available for that time!");
            return 0;
        }
        System.out.println("Scheduler Count for execution: "+userSchedulerList.size());
        for (int i = 0; i < userSchedulerList.size(); i++) {
            System.out.println("**** Execute Scheduler:" + userSchedulerList.get(i).getSchedulerName());
            setDeviceLastActionByWorkflow(userSchedulerList.get(i).getTargetAction(), userSchedulerList.get(i).getTargetDeviceId(), IConstant.ACTION_BY_SCHEDULER);
            setSchedulerLastExecutionTime(userSchedulerList.get(i).getId(), now.getTime());
        }
        return userSchedulerList.size();
    }
    /**
     * Actual execution of device simulators
     * @return 
     */
    private int runSimulators() {
        System.out.println("**************Will load Running Simulators");
        List<Simulations> activeSimulatorList = null;
        Query query = em.createQuery("SELECT s FROM Simulations s WHERE s.simulationStatus = 1");
        try {
            activeSimulatorList = (List<Simulations>) query.getResultList();
            if (activeSimulatorList != null && !activeSimulatorList.isEmpty()) {
                System.out.println("Detected "+activeSimulatorList.size());
                for (Simulations simulator : activeSimulatorList) {
                    simulator.setDevice(getDeviceById(simulator.getDeviceId(),true));
                    Calendar simulatorEndTime=Calendar.getInstance();
                    simulatorEndTime.setTimeInMillis(simulator.getStartTime().getTime()+(60000*simulator.getDuration()));
                    Calendar currentTime=Calendar.getInstance();
                    //must in simulation time and decice status still simulation, otherwise stop the simulation
                    if(simulatorEndTime.after(currentTime) && simulator.getDevice().getDeviceStatus()==IConstant.STATUS_SIMULATION){
                        System.out.println("Execute Simulator for Device:"+simulator.getDeviceId());
                        //add message
                        boolean sendUpdateMsg=false;
                        simulator.setLoopCounter(simulator.getLoopCounter()+1);
                        if(simulator.getLoopCounter()>=10){
                            sendUpdateMsg=true;
                            simulator.setLoopCounter(0);
                        }
                        RecievedMessages message=new RecievedMessages();
                        message.setDeviceId(simulator.getDeviceId());
                        message.setMessageTime(new Date());
                        message.setPayload(simulator.getDevice().getModel().getSimulationNormalMsg());
                        message.setType(1);
                        Random rand = new Random();
                        int n = rand.nextInt(100);
                        if(n<simulator.getAlertProbability()*100){
                            message.setType(2);
                            message.setPayload(simulator.getDevice().getModel().getSimulationAlertMsg());
                            sendUpdateMsg=true;
                            simulator.setLoopCounter(0);
                        }else {
                            if(n>(100-(simulator.getErrorProbanility()*100))){
                                message.setType(6);
                                message.setPayload(simulator.getDevice().getModel().getSimulationErrorMsg());
                                sendUpdateMsg=true;
                                simulator.setLoopCounter(0);
                            }
                        }
                        if(simulator.getDevice().getModel().getNoOfDevices()>0){
                            if(simulator.getDevice1Status()==0){
                                message.setPayload(message.getPayload()+",F");
                            }else{
                                message.setPayload(message.getPayload()+",N");
                            }
                        }else if(simulator.getDevice().getModel().getNoOfDevices()>1){
                            if(simulator.getDevice2Status()==0){
                                message.setPayload(message.getPayload()+",F");
                            }else{
                                message.setPayload(message.getPayload()+",N");
                            }
                        }
                        message.setPayload(message.getPayload()+" [S]");
                        //send update message
                        if(sendUpdateMsg) {
                            em.persist(message);
                        }
                        //ping the device +/-
                        simulator.getDevice().setLastPingTime(message.getMessageTime());
                        if(sendUpdateMsg){
                            simulator.getDevice().setTotalMessages(simulator.getDevice().getTotalMessages()+1);
                            if(message.getType()==IConstant.MESSAGE_ALERT) simulator.getDevice().setTotalAlerts(simulator.getDevice().getTotalAlerts()+1);
                        }
                        em.persist(simulator.getDevice());
                        //if any action to be invoked, send update message next time
                        //otherwise neglect the action as it is already the case
                        if(simulator.getDevice().getLastAction()==1 && simulator.getDevice1Status()!=1){
                            simulator.setDevice1Status(1);
                            simulator.setLoopCounter(10);
                        }else if(simulator.getDevice().getLastAction()==2 && simulator.getDevice1Status()!=0){
                            simulator.setDevice1Status(0);
                            simulator.setLoopCounter(10);
                        }else if(simulator.getDevice().getLastAction()==6 && simulator.getDevice2Status()!=1){
                            simulator.setDevice2Status(1);
                            simulator.setLoopCounter(10);
                        }else if(simulator.getDevice().getLastAction()==7 && simulator.getDevice2Status()!=0){
                            simulator.setDevice2Status(0);
                            simulator.setLoopCounter(10);
                        }
                        em.persist(simulator);
                    }else{
                        //stop the simulator, job done or device status changed:)
                        stopSimulation(simulator.getId(), simulator.getIdentityId());
                    }
                }
                return activeSimulatorList.size();
            }
            return 0;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load any simulation!");
            ex.printStackTrace();
            return 0;
        }
    }
    /**
     * Login method for users
     * @param emailAddress
     * @param password
     * @param identityId
     * @param ipAddress
     * @return Users object if successful login or null if not
     */
    public Users authenticateAdminUser(String emailAddress, String password, int identityId, String ipAddress) {
        //System.out.println("Authenticating ... emailAddress=" + emailAddress);
        Query query = em.createQuery("SELECT a FROM Users a WHERE a.emailAddress = :emailAddress and a.userStatus=1 and a.invalidLoginTrials<5 and a.identityId = :identityId");
        query.setParameter("emailAddress", emailAddress);
        query.setParameter("identityId", identityId);
        Users user = null;
        try {
            user = (Users) query.getSingleResult();
            if (PasswordAuthentication.getInstance().authenticate(password, user.getPassword())) {
                //correct password
                //create login notification
                user = em.find(Users.class, user.getId());
                //update new last login time and reset invalud login count
                user.setLastLoginTime(new Date());
                user.setInvalidLoginTrials(0);
                user.setIpAddress(ipAddress);
                em.persist(user);
                System.out.println("Update last login time!");
            } else {
                System.out.println("Invalid Login!");
                user.setInvalidLoginTrials(user.getInvalidLoginTrials() + 1);
                user.setIpAddress(ipAddress);
                System.out.println("Reason: Invalid Password!" + user.getInvalidLoginTrials());
                if (user.getInvalidLoginTrials() >= 5) {
                    user.setUserStatus(IConstant.USER_STATUS_LOCKED);
                    System.out.println("Lock User!");
                }
                em.persist(user);
                return null;
            }
        } catch (Exception ex) {
            System.out.println("Reason: Invalid emailAddress!");
            ex.printStackTrace();
            return null;
        }
        return user;
    }
    /**
     * Check if an email already registered or not
     * @param emailAddress
     * @return 
     */
    public boolean checkEmailUsed(String emailAddress) {
        System.out.println("Check ... emailAddress=" + emailAddress);
        Query query = em.createQuery("SELECT a FROM Users a WHERE a.emailAddress = :emailAddress");
        query.setParameter("emailAddress", emailAddress);
        Users user = null;
        try {
            user = (Users) query.getSingleResult();
            return true;
        } catch (Exception ex) {
            System.out.println("Reason: Invalid emailAddress!");
            ex.printStackTrace();
            return false;
        }
    }
    /**
     * Load identity settings
     * @param identity
     * @return 
     */
    public TenantSettings loadIdentitySettings(int identity) {
        //System.out.println("load settings for identity=" + identity);
        Query query = em.createQuery("SELECT t FROM TenantSettings t WHERE t.identity = :identity");
        query.setParameter("identity", identity);
        TenantSettings setting = null;
        try {
            setting = (TenantSettings) query.getSingleResult();
        } catch (Exception ex) {
            System.out.println("Reason: Invalid Settings!");
            ex.printStackTrace();
            return null;
        }
        return setting;
    }
    /**
     * Login device method, device id is unique across all accounts.
     * @param deviceId
     * @param devicePassword
     * @param version
     * @param ipAddress
     * @return Device if login is successful or null if not
     */
    public Devices loginDevice(String deviceId, String devicePassword, int version, String ipAddress) {
        //System.out.println("Login device using id =" + deviceId);
        Query query = em.createQuery("SELECT d FROM Devices d WHERE d.deviceId = :deviceId and d.deviceStatus != 2");
        query.setParameter("deviceId", deviceId);
        Devices device = null;
        try {
            device = (Devices) query.getSingleResult();
            if (PasswordAuthentication.getInstance().authenticate(devicePassword, device.getPassword())) {
                //login succeeded then send online device email 
                //only if the device was flagged as offline device by the offline detection thread ..
                //otherwise we will be sending email whenever the device is logined again and the time exceeds 60 seconds which doesn't help the customers
                if(device.getOfflineFlag()!=IConstant.STATUS_ONLINE  && (device.getLastPingTime()==null || new Date().getTime()-device.getLastPingTime().getTime()>60)){
                    //send online detection mail
                    sendOnlineEmail(device);
                }
                //send notification if ip changed
                if(device.getIpAddress()!=null && !device.getIpAddress().equals(ipAddress)){
                    sendDeviceIpAddressChange(device,ipAddress);
                }
                String secretKey = deviceId + System.currentTimeMillis();
                device.setSecretKey(secretKey);
                device.setOfflineFlag(IConstant.STATUS_ONLINE);//online device
                device.setFailedLogin(0);
                device.setDeviceStatus(IConstant.STATUS_ACTIVE);
                device.setLastPingTime(new Date());
                device.setFirmwareVersion(version);
                device.setIpAddress(ipAddress);
                System.out.println("Update device status to active ");
                em.persist(device);
                device.setModel(em.find(DeviceModel.class, device.getDeviceModel()));
                return device;
            } else {
                System.out.println("Failed Login device id =" + deviceId);
                device.setFailedLogin(device.getFailedLogin()+1);
                device.setIpAddress(ipAddress);
                if(device.getFailedLogin()>5){
                    System.out.println("Lock Device due to 5 invalid logins id =" + deviceId);
                    device.setDeviceStatus(IConstant.STATUS_INACTIVE);
                }
                em.persist(device);
                return null;
            }
        } catch (Exception ex) {
            System.out.println("Reason: Invalid Device Login!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * This method login a device for OTA upgrade
     * @param deviceId
     * @param devicePassword
     * @return 
     */
    public Devices loginDeviceForUpdate(String deviceId, String devicePassword) {
        //System.out.println("Login device using id =" + deviceId);
        Query query = em.createQuery("SELECT d FROM Devices d WHERE d.deviceId = :deviceId and d.deviceStatus != 2");
        query.setParameter("deviceId", deviceId);
        Devices device = null;
        try {
            device = (Devices) query.getSingleResult();
            if (PasswordAuthentication.getInstance().authenticate(devicePassword, device.getPassword())) {
                device.setModel(em.find(DeviceModel.class, device.getDeviceModel()));
                return device;
            } else {
                System.out.println("Failed Login device id =" + deviceId);
                device.setFailedLogin(device.getFailedLogin()+1);   
                if(device.getFailedLogin()>5){
                    System.out.println("Lock Device due to 5 invalid logins id =" + deviceId);
                    device.setDeviceStatus(IConstant.STATUS_INACTIVE);
                }
                em.persist(device);
                return null;
            }
        } catch (Exception ex) {
            System.out.println("Reason: Invalid Device Login!");
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Get device by its Id
     * @param deviceId
     * @param includeModel
     * @return 
     */
    public Devices getDeviceById(String deviceId, boolean includeModel) {
        //System.out.println("Get device using id =" + deviceId);
        Query query = em.createQuery("SELECT d FROM Devices d WHERE d.deviceId = :deviceId");
        query.setParameter("deviceId", deviceId);
        Devices device = null;
        try {
            device = (Devices) query.getSingleResult();
            if (includeModel) {
                device.setModel(getDeviceModelById(device.getDeviceModel()));
            }
            return device;
        } catch (Exception ex) {
            System.out.println("Reason: Device not found!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Get device group by its id and identity Id
     * @param deviceGroupId
     * @param identityId
     * @return 
     */
    public DeviceGroup getDeviceGroupById(int deviceGroupId, int identityId) {
        //System.out.println("Get device Group using id =" + deviceGroupId);
        Query query = em.createQuery("SELECT d FROM DeviceGroup d WHERE d.id = :id and d.identityId = :identityId");
        query.setParameter("id", deviceGroupId);
        query.setParameter("identityId", identityId);
        DeviceGroup deviceGroup = null;
        try {
            deviceGroup = (DeviceGroup) query.getSingleResult();
            return deviceGroup;
        } catch (Exception ex) {
            System.out.println("Reason: Device Group not found!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Store a recieved message to the temporary store
     * @param deviceId
     * @param secretKey
     * @param message
     * @return required device action or null if message is not accepted
     */
    public String acceptMessage(String deviceId, String secretKey, RecievedMessages message) {
        Query query = em.createQuery("SELECT d FROM Devices d WHERE d.deviceId = :deviceId and d.secretKey = :secretKey and d.deviceStatus != 2");
        query.setParameter("deviceId", deviceId);
        query.setParameter("secretKey", secretKey);
        Devices device = null;
        try {
            device = (Devices) query.getSingleResult();
            if (device != null) {
                em.persist(message);
                device.setLastPingTime(message.getMessageTime());
                if(message.getType()==IConstant.MESSAGE_ALERT) device.setTotalAlerts(device.getTotalAlerts()+1);
                device.setTotalMessages(device.getTotalMessages()+1);
                device.setTotalMessagesMonth(device.getTotalMessagesMonth()+1);
                device.setOfflineFlag(IConstant.STATUS_ONLINE);//online device
                if (device.getLastAction() == IConstant.ACTION_RESTART
                        || device.getLastAction() == IConstant.ACTION_RE_LOGIN) {
                    device.setLastAction(IConstant.ACTION_UPDATE);
                    device.setLastActionBy(IConstant.ACTION_BY_SYSTEM);
                    em.persist(device);
                    return "" + IConstant.ACTION_RESTART;
                } else if (device.getLastAction() == IConstant.ACTION_GET_MESSAGE){
                    device.setLastAction(IConstant.ACTION_UPDATE);
                    device.setLastActionBy(IConstant.ACTION_BY_SYSTEM);
                    em.persist(device);
                    return "" + IConstant.ACTION_GET_MESSAGE;
                }else {
                    em.persist(device);
                    return "" + device.getLastAction();
                }
            } else {
                return null;
            }
        } catch (Exception ex) {
            System.out.println("Reason: Invalid Device Login!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Accept ping from an IoT Device to keep it alive
     * @param deviceId
     * @param deviceSecretKey
     * @param date
     * @return required device action or null if message is not accepted
     */
    public String acceptPing(String deviceId, String deviceSecretKey, Date date) {
        Query query = em.createQuery("SELECT d FROM Devices d WHERE d.deviceId = :deviceId and d.secretKey = :secretKey and d.deviceStatus != 2");
        query.setParameter("deviceId", deviceId);
        query.setParameter("secretKey", deviceSecretKey);
        Devices device = null;
        try {
            device = (Devices) query.getSingleResult();
            if (device != null) {
                device.setLastPingTime(date);
                device.setOfflineFlag(IConstant.STATUS_ONLINE);//online device
                if (device.getLastAction() == IConstant.ACTION_RESTART
                        || device.getLastAction() == IConstant.ACTION_RE_LOGIN) {
                    device.setLastAction(IConstant.ACTION_UPDATE);
                    device.setLastActionBy(IConstant.ACTION_BY_SYSTEM);
                    em.persist(device);
                    return "" + IConstant.ACTION_RESTART;
                } else if (device.getLastAction() == IConstant.ACTION_GET_MESSAGE){
                    device.setLastAction(IConstant.ACTION_UPDATE);
                    device.setLastActionBy(IConstant.ACTION_BY_SYSTEM);
                    em.persist(device);
                    return "" + IConstant.ACTION_GET_MESSAGE;
                } else {
                    em.persist(device);
                    return "" + device.getLastAction();
                }
            } else {
                return null;
            }
        } catch (Exception ex) {
            System.out.println("Reason: Invalid Device Login!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Set device required action upon next ping/message/login
     * @param action
     * @param deviceId
     * @param identityId
     * @param actionBy
     * @return Devices object or null
     */
    public Devices setDeviceLastAction(int action, String deviceId, int identityId, int actionBy) {
        System.out.println("Update device last action for device id =" + deviceId);
        Query query = em.createQuery("SELECT d FROM Devices d WHERE d.deviceId = :deviceId and d.identity = :identity");
        query.setParameter("deviceId", deviceId);
        query.setParameter("identity", identityId);
        Devices device = null;
        try {
            device = (Devices) query.getSingleResult();
            device.setLastAction(action);
            device.setLastActionBy(actionBy);
            //store device status for each connected device
            if(action==1) { //on device 1
                device.setDevice1Status(1);
            } else if(action==2){//off device 1
                device.setDevice1Status(0);
            } else if(action==6) { //on device 2
                device.setDevice2Status(1);
            } else if(action==7){//off device 2
                device.setDevice2Status(0);
            }
            em.persist(device);
            return device;
        } catch (Exception ex) {
            System.out.println("Reason: Device update error!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Set device required action by an executed workflow
     * @param action
     * @param deviceId
     * @param actionBy
     * @return Devices or null
     */
    public Devices setDeviceLastActionByWorkflow(int action, String deviceId, int actionBy) {
        System.out.println("Update device last action for device id =" + deviceId);
        Query query = em.createQuery("SELECT d FROM Devices d WHERE d.deviceId = :deviceId");
        query.setParameter("deviceId", deviceId);
        Devices device = null;
        try {
            device = (Devices) query.getSingleResult();
            device.setLastAction(action);
            device.setLastActionBy(actionBy);
            em.persist(device);
            return device;
        } catch (Exception ex) {
            System.out.println("Reason: Device update error!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Load the list of user's devices i.e. identity devices
     * @param identity
     * @param includeModel
     * @param includeLastMessage
     * @param groupId
     * @return List<Devices> of that identity
     */
    public List<Devices> getUserDevices(int identity, boolean includeModel, boolean includeLastMessage, String groupId) {
        List<Devices> userDeviceList = null;
        String statement="SELECT d FROM Devices d WHERE d.identity = :identity";
        if(groupId!=null){
            int groupIdVal=Integer.parseInt(groupId);
            statement+=" and d.groupId="+groupIdVal;
        }
        Query query = em.createQuery(statement);
        query.setParameter("identity", identity);
        try {
            userDeviceList = (List<Devices>) query.getResultList();
            if (includeModel) {
                for (int i = 0; i < userDeviceList.size(); i++) {
                    userDeviceList.get(i).setModel(em.find(DeviceModel.class, userDeviceList.get(i).getDeviceModel()));
                }
            }
            //include last message as well?
            if(includeLastMessage){
                for (int i = 0; i < userDeviceList.size(); i++) {
                    userDeviceList.get(i).setLastMessage(getLastDeviceMessages(userDeviceList.get(i).getDeviceId()));
                }                
            }
            return userDeviceList;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load user devices for this identity!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Get a list of all active device models
     * @return List<DeviceModel> that are active
     */
    public List<DeviceModel> getActiveDeviceModels() {
        List<DeviceModel> deviceModelList = null;
        Query query = em.createQuery("SELECT d FROM DeviceModel d where d.modelStatus = 1");
        try {
            deviceModelList = (List<DeviceModel>) query.getResultList();
            return deviceModelList;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load device models!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * List of all device models even non-active
     * @param countDevices
     * @return 
     */
    public List<DeviceModel> getAllDeviceModels(boolean countDevices) {
        List<DeviceModel> deviceModelList = null;
        Query query = em.createQuery("SELECT d FROM DeviceModel d");
        try {
            deviceModelList = (List<DeviceModel>) query.getResultList();
            for(DeviceModel model:deviceModelList){
                model.sethWType(em.find(HwType.class, model.getHwType()));
                if(countDevices){
                    //set the number of registered devices using this model
                    model.setDeviceCount(loadDeviceCountPerModel(model.getId()));
                }
            }
            return deviceModelList;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load device models!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Get all messages for a device upto the maxResults threshold
     * @param deviceId
     * @param maxResults
     * @return List<Messages> includes all device messages
     */
    public List<Messages> getDeviceMessages(String deviceId, int maxResults) {
        List<Messages> deviceMessageList = null;
        Query query = em.createQuery("SELECT m FROM Messages m WHERE m.deviceId = :deviceId order by m.id desc");
        query.setParameter("deviceId", deviceId);
        query.setMaxResults(maxResults);
        try {
            deviceMessageList = (List<Messages>) query.getResultList();
            return deviceMessageList;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load devices messages for this device!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Load last device message
     * @param deviceId
     * @return 
     */
    public Messages getLastDeviceMessages(String deviceId) {
        List<Messages> deviceMessageList = null;
        Query query = em.createQuery("SELECT m FROM Messages m WHERE m.deviceId = :deviceId order by m.id desc");
        query.setParameter("deviceId", deviceId);
        query.setMaxResults(10);
        try {
            deviceMessageList = (List<Messages>) query.getResultList();
            if(deviceMessageList==null || deviceMessageList.size()==0) return null;
            for(int i=0;i<deviceMessageList.size();i++){
                if(deviceMessageList.get(i).getType()!=7 && deviceMessageList.get(i).getType()!=0){
                    return deviceMessageList.get(i);
                }
            }
            return null;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load devices messages for this device!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Get a specific device details
     * @param identity
     * @param deviceId
     * @param includeModel
     * @return 
     */
    public Devices getUserDeviceDetails(int identity, String deviceId, boolean includeModel) {
        System.out.println("Get device using id =" + deviceId);
        Query query = em.createQuery("SELECT d FROM Devices d WHERE d.deviceId = :deviceId and d.identity = :identity");
        query.setParameter("deviceId", deviceId);
        query.setParameter("identity", identity);
        Devices device = null;
        try {
            device = (Devices) query.getSingleResult();
            if (includeModel) {
                device.setModel(getDeviceModelById(device.getDeviceModel()));
            }
            return device;
        } catch (Exception ex) {
            System.out.println("Reason: Device not found!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * List all identity/account workflows
     * @param identity
     * @return 
     */
    public List<Workflows> getIdentityWorkflows(int identity) {
        List<Workflows> userWorkflowList = null;
        Query query = em.createQuery("SELECT w FROM Workflows w WHERE w.identity = :identity");
        query.setParameter("identity", identity);
        query.setMaxResults(50);
        try {
            userWorkflowList = (List<Workflows>) query.getResultList();
            for (int i = 0; i < userWorkflowList.size(); i++) {
                if(userWorkflowList.get(i).getTriggerType()==1){
                    userWorkflowList.get(i).setSourceDevice(getDeviceById(userWorkflowList.get(i).getTriggeringId(), false));
                }else{
                    userWorkflowList.get(i).setSourceDeviceGroup(getDeviceGroupById(Integer.parseInt(userWorkflowList.get(i).getTriggeringId()),identity));
                }
                userWorkflowList.get(i).setTargetDevice(getDeviceById(userWorkflowList.get(i).getTargetDeviceId(), false));
            }
            return userWorkflowList;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load identity workflows!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Load identity/account ACTIVE workflows
     * @param identity
     * @return 
     */
    public List<Workflows> getIdentityActiveWorkflows(int identity) {
        List<Workflows> userWorkflowList = null;
        Query query = em.createQuery("SELECT w FROM Workflows w WHERE w.identity = :identity and w.workflowStatus = 1");
        query.setParameter("identity", identity);
        query.setMaxResults(50);
        try {
            userWorkflowList = (List<Workflows>) query.getResultList();
            for (int i = 0; i < userWorkflowList.size(); i++) {
                if(userWorkflowList.get(i).getTriggerType()==1){
                    userWorkflowList.get(i).setSourceDevice(getDeviceById(userWorkflowList.get(i).getTriggeringId(), false));
                }else{
                    userWorkflowList.get(i).setSourceDeviceGroup(getDeviceGroupById(Integer.parseInt(userWorkflowList.get(i).getTriggeringId()),identity));
                }
                userWorkflowList.get(i).setTargetDevice(getDeviceById(userWorkflowList.get(i).getTargetDeviceId(), false));
            }
            return userWorkflowList;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load identity workflows!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Remove workflow using its Id and Identity Id
     * @param workflowId
     * @param identity
     * @return 
     */
    public boolean removeWorkflow(int workflowId, int identity) {
        Query query = em.createQuery("SELECT w FROM Workflows w WHERE w.identity = :identity and w.id = :id");
        query.setParameter("identity", identity);
        query.setParameter("id", workflowId);
        Workflows workflow = null;
        try {
            workflow = (Workflows) query.getSingleResult();
            em.remove(workflow);
            return true;
        } catch (Exception ex) {
            System.out.println("Reason: Can't activate workflow " + workflowId);
            ex.printStackTrace();
            return false;
        }
    }
    /**
     * Insert new IoT workflow
     * @param source
     * @param event
     * @param target
     * @param action
     * @param identity
     * @param name
     * @param status
     * @param type
     * @return 
     */
    public Workflows addNewWorkflow(String source, int event, String target, int action, int identity, String name, int status,int type) {
        if(checkWorkflowAlreadyExist(source,target,event, action)==true){
            return null;
        }
        Workflows workflow = new Workflows();
        workflow.setTriggerType(type);
        workflow.setTriggeringId(source);
        workflow.setTargetDeviceId(target);
        workflow.setTriggerEvent(event);
        workflow.setTargetAction(action);
        workflow.setIdentity(identity);
        workflow.setWorkflowName(name);
        workflow.setWorkflowStatus(status);
        workflow.setCreationDate(new Date());
        em.persist(workflow);
        return workflow;
    }
    /**
     * Activate a specific workflow within an identity
     * @param workflowId
     * @param identity
     * @return 
     */
    public boolean activateWorkflow(int workflowId, int identity) {
        Query query = em.createQuery("SELECT w FROM Workflows w WHERE w.identity = :identity and w.id = :id");
        query.setParameter("identity", identity);
        query.setParameter("id", workflowId);
        Workflows workflow = null;
        try {
            workflow = (Workflows) query.getSingleResult();
            workflow.setWorkflowStatus(IConstant.STATUS_ACTIVE);
            workflow.setUpdateTime(new Date());
            em.persist(workflow);
            return true;
        } catch (Exception ex) {
            System.out.println("Reason: Cann't activate workflow " + workflowId);
            ex.printStackTrace();
            return false;
        }
    }
    /**
     * Deactivate a specific workflow within specific identity
     * @param workflowId
     * @param identity
     * @return 
     */
    public boolean inactivateWorkflow(int workflowId, int identity) {
        Query query = em.createQuery("SELECT w FROM Workflows w WHERE w.identity = :identity and w.id = :id");
        query.setParameter("identity", identity);
        query.setParameter("id", workflowId);
        Workflows workflow = null;
        try {
            workflow = (Workflows) query.getSingleResult();
            workflow.setWorkflowStatus(IConstant.STATUS_INACTIVE);
            workflow.setUpdateTime(new Date());
            em.persist(workflow);
            return true;
        } catch (Exception ex) {
            System.out.println("Reason: Can't activate workflow " + workflowId);
            ex.printStackTrace();
            return false;
        }
    }
    /**
     * Add new audit trail record
     * @param userId
     * @param actionId
     * @param parameters 
     */
    public void addAuditTrail(int userId, int actionId, String parameters) {
        AuditTrail audit = new AuditTrail();
        audit.setByUserId(userId);
        audit.setActionId(actionId);
        audit.setPerformedAt(new Date());
        audit.setParameters(parameters);
        em.persist(audit);
    }
    /**
     * Get system global configurations
     * @return 
     */
    public SystemConfig getSystemConfigurations() {
        System.out.println("Get system configurations");
        Query query = em.createQuery("SELECT s FROM SystemConfig s WHERE s.versionId = :versionId");
        query.setParameter("versionId", 1);
        SystemConfig config = null;
        try {
            config = (SystemConfig) query.getSingleResult();
            return config;
        } catch (Exception ex) {
            System.out.println("Reason: failed to load system configuration!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Get Identity Schedulers list
     * @param identity
     * @return 
     */
    public List<Scheduler> getIdentitySchedulers(int identity) {
        List<Scheduler> userSchedulerList = null;
        Query query = em.createQuery("SELECT s FROM Scheduler s WHERE s.identity = :identity");
        query.setParameter("identity", identity);
        query.setMaxResults(50);
        try {
            userSchedulerList = (List<Scheduler>) query.getResultList();
            for (int i = 0; i < userSchedulerList.size(); i++) {
                userSchedulerList.get(i).setTargetDevice(getDeviceById(userSchedulerList.get(i).getTargetDeviceId(), false));
            }
            return userSchedulerList;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load identity Schedulers!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Get active scheduler for specific hour/min and optionally related device for the action to be done on this time
     * @param hour
     * @param min
     * @param loadDevice
     * @return 
     */
    public List<Scheduler> getActiveScheduler(int hour, int min, boolean loadDevice) {
        System.out.println("Will load data for hour:[" + hour + "]and min{" + min + "]");
        List<Scheduler> userSchedulerList = null;
        Query query = em.createQuery("SELECT s FROM Scheduler s WHERE s.triggeringHour = :triggeringHour and s.triggeringMin = :triggeringMin and s.schedulerStatus = 1");
        query.setParameter("triggeringHour", hour);
        query.setParameter("triggeringMin", min);
        try {
            userSchedulerList = (List<Scheduler>) query.getResultList();
            if (loadDevice) {
                for (int i = 0; i < userSchedulerList.size(); i++) {
                    userSchedulerList.get(i).setTargetDevice(getDeviceById(userSchedulerList.get(i).getTargetDeviceId(), false));
                }
            }
            return userSchedulerList;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load active Schedulers!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * delete a scheduler in an entity
     * @param schedulerId
     * @param identity
     * @return 
     */
    public boolean removeScheduler(int schedulerId, int identity) {
        Query query = em.createQuery("SELECT s FROM Scheduler s WHERE s.identity = :identity and s.id = :id");
        query.setParameter("identity", identity);
        query.setParameter("id", schedulerId);
        Scheduler scheduler = null;
        try {
            scheduler = (Scheduler) query.getSingleResult();
            em.remove(scheduler);
            return true;
        } catch (Exception ex) {
            System.out.println("Reason: Can't activate Scheduler " + schedulerId);
            ex.printStackTrace();
            return false;
        }
    }
    /**
     * Create new scheduler for an entity
     * @param day
     * @param time
     * @param min
     * @param target
     * @param action
     * @param identity
     * @param name
     * @param status
     * @return 
     */
    public Scheduler addNewScheduler(int day, int time, int min, String target, int action, int identity, String name, int status) {
        Scheduler scheduler = new Scheduler();
        scheduler.setTriggeringDay(day);
        scheduler.setTriggeringHour(time);
        scheduler.setTriggeringMin(min);
        scheduler.setTargetDeviceId(target);
        scheduler.setTargetAction(action);
        scheduler.setIdentity(identity);
        scheduler.setSchedulerName(name);
        scheduler.setSchedulerStatus(status);
        scheduler.setCreationDate(new Date());
        em.persist(scheduler);
        return scheduler;
    }
    /**
     * Activate scheduler 
     * @param schedulerId
     * @param identity
     * @return 
     */
    public boolean activateScheduler(int schedulerId, int identity) {
        Query query = em.createQuery("SELECT s FROM Scheduler s WHERE s.identity = :identity and s.id = :id");
        query.setParameter("identity", identity);
        query.setParameter("id", schedulerId);
        Scheduler scheduler = null;
        try {
            scheduler = (Scheduler) query.getSingleResult();
            scheduler.setSchedulerStatus(IConstant.STATUS_ACTIVE);
            em.persist(scheduler);
            return true;
        } catch (Exception ex) {
            System.out.println("Reason: Can't activate Scheduler " + schedulerId);
            ex.printStackTrace();
            return false;
        }
    }
    /**
     * Deactivate scheduler
     * @param schedulerId
     * @param identity
     * @return 
     */
    public boolean inactivateScheduler(int schedulerId, int identity) {
        Query query = em.createQuery("SELECT s FROM Scheduler s WHERE s.identity = :identity and s.id = :id");
        query.setParameter("identity", identity);
        query.setParameter("id", schedulerId);
        Scheduler scheduler = null;
        try {
            scheduler = (Scheduler) query.getSingleResult();
            scheduler.setSchedulerStatus(IConstant.STATUS_INACTIVE);
            em.persist(scheduler);
            return true;
        } catch (Exception ex) {
            System.out.println("Reason: Can't inactivate Scheduler " + schedulerId);
            ex.printStackTrace();
            return false;
        }
    }
    /**
     * Update scheduler last execution time
     * @param schedulerId
     * @param execDate
     * @return 
     */
    public boolean setSchedulerLastExecutionTime(int schedulerId, Date execDate) {
        Query query = em.createQuery("SELECT s FROM Scheduler s WHERE s.id = :id");
        query.setParameter("id", schedulerId);
        Scheduler scheduler = null;
        try {
            scheduler = (Scheduler) query.getSingleResult();
            scheduler.setUpdateTime(execDate);
            em.persist(scheduler);
            return true;
        } catch (Exception ex) {
            System.out.println("Reason: Can't update execution time for Scheduler " + schedulerId);
            ex.printStackTrace();
            return false;
        }
    }
    /**
     * Get list of the user for a specific identity
     * @param identity
     * @return 
     */
    public List<Users> getUsersForIdentity(int identity) {
        List<Users> usersList = null;
        Query query = em.createQuery("SELECT u FROM Users u WHERE u.identityId = :identityId");
        query.setParameter("identityId", identity);
        try {
            usersList = (List<Users>) query.getResultList();
            return usersList;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load users list for identity!" + identity);
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Activate user in an identity
     * @param userId
     * @param identity
     * @return 
     */
    public boolean activateUser(int userId, int identity) {
        Query query = em.createQuery("SELECT u FROM Users u WHERE u.identityId = :identityId and u.id = :id");
        query.setParameter("identityId", identity);
        query.setParameter("id", userId);
        Users user = null;
        try {
            user = (Users) query.getSingleResult();
            user.setUserStatus(IConstant.STATUS_ACTIVE);
            user.setInvalidLoginTrials(0);
            user.setUpdateTime(new Date());
            em.persist(user);
            return true;
        } catch (Exception ex) {
            System.out.println("Reason: Can't activate User " + userId);
            ex.printStackTrace();
            return false;
        }
    }
    /**
     * Deactivate user in a specific identity
     * @param userId
     * @param identity
     * @return 
     */
    public boolean inactivateUser(int userId, int identity) {
        Query query = em.createQuery("SELECT u FROM Users u WHERE u.identityId = :identityId and u.id = :id");
        query.setParameter("identityId", identity);
        query.setParameter("id", userId);
        Users user = null;
        try {
            user = (Users) query.getSingleResult();
            user.setUserStatus(IConstant.STATUS_INACTIVE);
            user.setUpdateTime(new Date());
            em.persist(user);
            return true;
        } catch (Exception ex) {
            System.out.println("Reason: Can't inactivate User " + userId);
            ex.printStackTrace();
            return false;
        }
    }
    /**
     * Update user role in a specific identity
     * @param userId
     * @param identity
     * @param newRole
     * @return 
     */
    public boolean updateUserRole(int userId, int identity, int newRole) {
        Query query = em.createQuery("SELECT u FROM Users u WHERE u.identityId = :identityId and u.id = :id");
        query.setParameter("identityId", identity);
        query.setParameter("id", userId);
        Users user = null;
        try {
            user = (Users) query.getSingleResult();
            user.setUserRole(newRole);
            user.setUpdateTime(new Date());
            em.persist(user);
            return true;
        } catch (Exception ex) {
            System.out.println("Reason: Can't change User role: " + userId);
            ex.printStackTrace();
            return false;
        }
    }
    /**
     * delete identity user using user id
     * @param userId
     * @param identity
     * @return true if succeeded to delete the user or false otherwise
     */
    public boolean deleteUser(int userId, int identity) {
        Query query = em.createQuery("SELECT u FROM Users u WHERE u.identityId = :identityId and u.id = :id");
        query.setParameter("identityId", identity);
        query.setParameter("id", userId);
        Users user = null;
        try {
            user = (Users) query.getSingleResult();
            em.remove(user);
            return true;
        } catch (Exception ex) {
            System.out.println("Reason: Can't delete User " + userId);
            ex.printStackTrace();
            return false;
        }
    }
    /**
     * Create new identity user
     * @param name
     * @param emailAddress
     * @param identityId
     * @param status
     * @param role
     * @param workflow
     * @param scheduler
     * @param dashboard
     * @param simulation
     * @param userManagement
     * @param identityAdmin
     * @return 
     */
    public String addNewUser(String name, String emailAddress, Integer identityId, int status, int role, String workflow, String scheduler, String dashboard,String simulation, String userManagement,int identityAdmin) {
        Users user = new Users();
        user.setEmailAddress(emailAddress);
        user.setCreationDate(new Date());
        user.setIdentityId(identityId);
        user.setInvalidLoginTrials(0);
        user.setLanguage(1);
        if (dashboard == null) {
            user.setDashboardCreation(0);
        } else {
            user.setDashboardCreation(1);
        }
        if (simulation == null) {
            user.setSimulationCreation(0);
        } else {
            user.setSimulationCreation(1);
        }
        if (userManagement == null) {
            user.setUserCreation(0);
        } else {
            user.setUserCreation(1);
        }
        if (scheduler == null) {
            user.setSchedulerCreation(0);
        } else {
            user.setSchedulerCreation(1);
        }
        if (workflow == null) {
            user.setWorkflowCreation(0);
        } else {
            user.setWorkflowCreation(1);
        }
        user.setIdentityAdmin(0);
        String timeStamp = ("" + Calendar.getInstance().getTimeInMillis()).substring(6);
        String tempPassword = emailAddress.substring(1, 5).concat(timeStamp);
        String tempPasswordHashed = PasswordAuthentication.getInstance().hash(tempPassword);
        user.setPassword(tempPasswordHashed);
        user.setUserRole(role);
        user.setUserStatus(status);
        user.setUsername(name);
        user.setIdentityAdmin(identityAdmin);
        user.setSystemAdmin(0);
        em.persist(user);
        return tempPassword;
    }
    /**
     * Change user's password
     * @param userId
     * @param old
     * @param newPass
     * @return 
     */
    public boolean updateUserPassword(int userId, String old, String newPass) {
        Query query = em.createQuery("SELECT u FROM Users u WHERE u.id = :id");
        query.setParameter("id", userId);
        Users user = null;
        try {
            user = (Users) query.getSingleResult();
            if (PasswordAuthentication.getInstance().authenticate(old, user.getPassword())) {
                user.setPassword(PasswordAuthentication.getInstance().hash(newPass));
                user.setUpdateTime(new Date());
                em.persist(user);
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Reason: Can't change password for User " + userId);
            ex.printStackTrace();
            return false;
        }
    }
    /**
     * Update user's dashboard details 
     * @param userId
     * @param dashboard
     * @return 
     */
    public Users updateUserDetails(int userId, String dashboard) {
        Query query = em.createQuery("SELECT u FROM Users u WHERE u.id = :id");
        query.setParameter("id", userId);
        Users user = null;
        try {
            user = (Users) query.getSingleResult();
            user.setDashboard(dashboard);
            user.setUpdateTime(new Date());
            em.persist(user);
            return user;
        } catch (Exception ex) {
            System.out.println("Reason: Can't update user's dashboard for User " + userId);
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Load specific template using the performed action
     * @param action
     * @return 
     */
    public EmailTemplates loadNotificationTemplate(int action) {
        System.out.println("load EmailTemplates for action=" + action);
        Query query = em.createQuery("SELECT e FROM EmailTemplates e WHERE e.actionId = :actionId");
        query.setParameter("actionId", action);
        EmailTemplates template = null;
        try {
            template = (EmailTemplates) query.getSingleResult();
        } catch (Exception ex) {
            System.out.println("Reason: Invalid Template!");
            ex.printStackTrace();
            return null;
        }
        return template;
    }
    /**
     * Check unique user email within the same identity
     * @param identityId
     * @param email
     * @return 
     */
    public boolean checkUniqueEmailAddressPerIdentity(Integer identityId, String email) {
        Query query = em.createQuery("SELECT u FROM Users u WHERE u.identityId = :identityId and u.emailAddress = :emailAddress");
        query.setParameter("identityId", identityId);
        query.setParameter("emailAddress", email);
        Users user = null;
        try {
            user = (Users) query.getSingleResult();
            return false;
        } catch (Exception ex) {
            System.out.println("User email is unique in this identity!");
            return true;
        }
    }
    /**
     * Check if barcode already exist or not.
     * @param barcode
     * @return 
     */
    public int checkbarCodeIsThere(String barcode) {
        BarCodes barCode = em.find(BarCodes.class, barcode);
        if(barCode!=null){
            return barCode.getDeviceModel();
        } else{
            return 0;
        }
    }
    /**
     * Check if barcode exist and return the corresponding model
     * @param barcode
     * @return 
     */
    public DeviceModel checkbarCodeIsThereAndLoadModel(String barcode) {
        BarCodes barCode = em.find(BarCodes.class, barcode);
        if(barCode!=null){
            return em.find(DeviceModel.class, barCode.getDeviceModel());
        } else{
            return null;
        }
    }
    /**
     * Save identity settings
     * @param identityId
     * @param heartbeatsVal
     * @param alertMessage
     * @param faultMesgVal
     * @param alertOffline
     * @param alertOnline
     * @param alertDeviceReg
     * @param alertDeviceUpdate
     * @param messagesVal
     * @param timezone
     * @param intervalsVal
     * @param languageVal
     * @param graceVal
     * @param ipChangeAlert
     * @param firmwareAlert
     * @return 
     */
    public TenantSettings saveIdentitySettings(int identityId,int heartbeatsVal, int alertMessage, int faultMesgVal,int alertOffline, 
            int alertOnline, int alertDeviceReg, int alertDeviceUpdate, int messagesVal, String timezone, 
            int intervalsVal, int languageVal, int graceVal, int ipChangeAlert, int firmwareAlert) {
        System.out.println("load settings for identity=" + identityId);
        Query query = em.createQuery("SELECT t FROM TenantSettings t WHERE t.identity = :identity");
        query.setParameter("identity", identityId);
        TenantSettings setting = null;
        try {
            setting = (TenantSettings) query.getSingleResult();
            setting.setAlertEmailMessage(alertMessage);
            setting.setFaultNotificationEmail(faultMesgVal);
            setting.setAlertEmailOffline(alertOffline);
            setting.setAlertEmailOnline(alertOnline);
            setting.setDeviceRegistrationEmail(alertDeviceReg);
            setting.setDeviceUpdateEmail(alertDeviceUpdate);
            setting.setAlertFirmwareAvailable(firmwareAlert);
            setting.setAlertDeviceIpChange(ipChangeAlert);
            setting.setMaxRetainedMessages(messagesVal);
            setting.setPingInterval(15);
            setting.setTimezone(timezone);
            setting.setUpdateInterval(intervalsVal);
            setting.setAlertEmailLanguage(languageVal);
            setting.setUpdateTime(new Date());
            setting.setAlertGracePeriod(graceVal);
            em.persist(setting);
            return setting;
        } catch (Exception ex) {
            System.out.println("Reason: Not Able to Update Settings!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Load identity notifications by identity Id, type and max results
     * @param identityId
     * @param typeId
     * @param maxResults 
     * @return 
     */
    public List<IdentityNotifications> getIdentityNotifications(int identityId, String typeId, int maxResults) {
        List<IdentityNotifications> identityNotificationList = null;
        String statement="SELECT i FROM IdentityNotifications i WHERE i.identityId = :identityId";
        if(typeId!=null){
            statement+=" and i.templateId = "+typeId;
        }
        statement+=" order by i.id desc";
        Query query = em.createQuery(statement);
        query.setParameter("identityId", identityId);
        query.setMaxResults(maxResults);
        try {
            identityNotificationList = (List<IdentityNotifications>) query.getResultList();
            for(IdentityNotifications notification:identityNotificationList){
                notification.setTemplate(loadNotificationTemplate(notification.getTemplateId()));
            }
            return identityNotificationList;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load identity notifications!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Check existing notification by time to avoid sending same notification within the configured grace period
     * @param identityId
     * @param uniqueIdentifier
     * @param date
     * @param templateId
     * @return 
     */
    public boolean checkExistingNotificationByTime(int identityId, String uniqueIdentifier, Date date, int templateId) {
        Query query = em.createQuery("SELECT i FROM IdentityNotifications i WHERE i.identityId = :identityId and i.templateId = :templateId and i.uniqueIdentiter = :uniqueIdentiterorder and i.notifiedOn >= :notifiedOn");
        query.setParameter("identityId", identityId);
        query.setParameter("uniqueIdentiterorder", uniqueIdentifier);
        query.setParameter("notifiedOn", date);
        query.setParameter("templateId", templateId);
        query.setMaxResults(1);
        try {
            IdentityNotifications identityNotification = (IdentityNotifications) query.getSingleResult();
            return true;
        } catch (Exception ex) {
            System.out.println("No identity notification in the same grace period with same template id and unique identifier!");
            return false;
        }
    }
    /**
     * Load all unread notification for an identity
     * @param identityId
     * @return 
     */
    public long getUnreadIdentityNotitifcations(int identityId) {
        Query query=em.createNativeQuery("SELECT count(*) FROM Identity_Notifications i WHERE i.read_flag=0 and i.identity_Id = "+identityId);
        Long count=(Long)query.getSingleResult();
        return count;
    }
    /**
     * Mark a notification as read or unread
     * @param notificationId
     * @param identityId
     * @param flag
     * @return 
     */
    public boolean updateNotificationRaadFlag(int notificationId, int identityId,short flag) {
        IdentityNotifications notification = em.find(IdentityNotifications.class, notificationId);
        if (notification == null || identityId!=notification.getIdentityId()) {
            return false;
        }
        notification.setReadFlag(flag);
        em.persist(notification);
        return true;
    }
    /**
     * Delete a notification in an identity
     * @param notificationId
     * @param identityId
     * @return 
     */
    public boolean deleteNotification(int notificationId, Integer identityId) {
        IdentityNotifications notification = em.find(IdentityNotifications.class, notificationId);
        if (notification == null || identityId!=notification.getIdentityId()) {
            return false;
        }
        em.remove(notification);
        return true;
    }
    /**
     * Register new IoT device
     * @param identity
     * @param barcode
     * @param password
     * @param friendlyName
     * @param location
     * @param tag
     * @param customName
     * @param customValue
     * @param email
     * @param mobile
     * @param modelId
     * @param device1Name
     * @param device2Name
     * @param highAlert1
     * @param highAlert2
     * @param lowAlert1
     * @param lowAlert2
     * @param deviceManagement
     * @param group
     * @return 
     */
    public String registerNewDevice(int identity,String barcode, String password, String friendlyName, String location, String tag, String customName, 
            String customValue, String email,String mobile, int modelId, String device1Name, String device2Name,
            String highAlert1, String highAlert2, String lowAlert1, String lowAlert2,int deviceManagement, String group) {
        Devices device=new Devices();
        device.setBarCode(barcode);
        device.setFriendlyName(friendlyName);
        device.setCustomName(customName);
        device.setCustomValue(customValue);
        device.setDevice1(device1Name);
        device.setDevice2(device2Name);
        device.setDeviceModel(modelId);
        device.setDeviceStatus(IConstant.STATUS_PENDING_LOGIN);
        device.setLastAction(IConstant.ACTION_UPDATE);
        device.setIdentity(identity);
        device.setLastActionBy(IConstant.ACTION_BY_SYSTEM);
        device.setLocation(location);
        device.setNotificationEmail(email);
        device.setNotificationMobile(mobile);
        device.setTags(tag);
        device.setRegistrationDate(new Date());
        System.out.println("Password will be:"+password);
        device.setPassword(PasswordAuthentication.getInstance().hash(password));
        device.setHighAlertValue1(highAlert1);
        device.setHighAlertValue2(highAlert2);
        device.setLowAlertValue1(lowAlert1);
        device.setLowAlertValue2(lowAlert2);
        device.setTotalInbound(0);
        device.setTotalOutbound(0);
        device.setTotalInboundMonth(0);
        device.setTotalOutboundMonth(0);
        device.setTotalMessages(0);
        device.setOfflineFlag(0);
        device.setTotalMessagesMonth(0);
        device.setTotalAlerts(0);
        device.setDevice1Status(0);
        device.setDevice2Status(0);
        device.setSmartRules1(0);
        device.setSmartRules2(0);
        device.setDeviceManagement(deviceManagement);
        if("-1".equals(group)){
            device.setGroupId(null);
        }else{
            device.setGroupId(Integer.parseInt(group));
        }        
        em.persist(device);
        em.flush();
        String deviceId="A"+device.getId();
        device.setDeviceId(deviceId);
        em.persist(device);
        //send registration notification
        sendDeviceRegistrationEmail(device);
        return device.getDeviceId();
        
    }
    /**
     * Remove barcode so it can be used for a registered IoT device
     * @param barcode 
     */
    public void removeBarCode(String barcode) {
        BarCodes barCode = em.find(BarCodes.class, barcode);
        if(barCode!=null){
            em.remove(barCode);
        }
    }
    /**
     * Update an IoT device
     * @param deviceToEdit
     * @param friendlyName
     * @param location
     * @param tag
     * @param customName
     * @param customValue
     * @param email
     * @param mobile
     * @param device1Name
     * @param device2Name
     * @param highAlert1
     * @param highAlert2
     * @param lowAlert1
     * @param lowAlert2
     * @param smartRule1
     * @param smartRule2
     * @param deviceManagement
     * @param group
     * @return 
     */
    public Devices updateDevice(Devices deviceToEdit, String friendlyName, String location, String tag, String customName, 
            String customValue, String email, String mobile,String device1Name, String device2Name,String highAlert1, String highAlert2,
            String lowAlert1, String lowAlert2, int smartRule1, int smartRule2,int deviceManagement,String group) {
        Devices oldDevice=em.find(Devices.class, deviceToEdit.getId());
        if(oldDevice==null){
            return null;
        }
        oldDevice.setFriendlyName(friendlyName);
        oldDevice.setCustomName(customName);
        oldDevice.setCustomValue(customValue);
        oldDevice.setDevice1(device1Name);
        oldDevice.setDevice2(device2Name);
        oldDevice.setLocation(location);
        String oldEmail=oldDevice.getNotificationEmail();
        oldDevice.setNotificationEmail(email);
        oldDevice.setNotificationMobile(mobile);
        oldDevice.setTags(tag);
        oldDevice.setUpdateTime(new Date());
        oldDevice.setHighAlertValue1(highAlert1);
        oldDevice.setHighAlertValue2(highAlert2);
        oldDevice.setLowAlertValue1(lowAlert1);
        oldDevice.setLowAlertValue2(lowAlert2);
        oldDevice.setSmartRules1(smartRule1);
        oldDevice.setSmartRules2(smartRule2);
        oldDevice.setDeviceManagement(deviceManagement);
        if("-1".equals(group)){
            oldDevice.setGroupId(null);
        }else{
            oldDevice.setGroupId(Integer.parseInt(group));
        }
        em.persist(oldDevice);
        sendDeviceUpdateEmail(oldDevice);
        if(!oldEmail.equalsIgnoreCase(email)){
            sendDeviceUpdateEmailToOldContact(oldDevice,oldEmail);
        }
        return oldDevice;
    }
    /**
     * Delete all identity notifications
     * @param identityId
     * @return 
     */
    public boolean deleteAllIdentityNotifications(Integer identityId) {
        Query deleteQuery = em.createNativeQuery("DELETE FROM Identity_Notifications WHERE identity_Id = "+identityId);
        int deletedRows = deleteQuery.executeUpdate();
        System.out.println("Deleted All Notifications for Identity:"+identityId+" no of rows deleted :" + deletedRows);
        return true;
    }
    /**
     * Delete all read notification for an identity
     * @param identityId
     * @return 
     */
    public boolean deleteAllReadIdentityNotifications(Integer identityId) {
        Query deleteQuery = em.createNativeQuery("DELETE FROM Identity_Notifications WHERE identity_Id = "+identityId+" and read_Flag = 1");
        int deletedRows = deleteQuery.executeUpdate();
        System.out.println("Deleted All Read Notifications for Identity:"+identityId+" no of rows deleted :" + deletedRows);
        return true;
    }
    /**
     * Purge all device messages
     * @param deviceId
     * @return 
     */
    public int purgeDeviceMessages(String deviceId) {
        Query deleteQuery = em.createNativeQuery("DELETE FROM messages WHERE device_id = '"+deviceId+"'");
        int deletedRows = deleteQuery.executeUpdate();
        System.out.println("Deleted All Device Messages for deviceId:"+deviceId+" no of rows deleted :" + deletedRows);
        return deletedRows;
    }
    /**
     * Purge device messages before start date parameter
     * @param startDate
     * @return 
     */
    public int purgeOldMessages(Date startDate) {
        Query deleteQuery = em.createQuery("DELETE FROM Messages m WHERE m.messageTime < :messageTime");
        deleteQuery.setParameter("messageTime", startDate);
        int deletedRows = deleteQuery.executeUpdate();
        System.out.println("Purge old messages before date:" + startDate+", total deleted messages: "+deletedRows);
        return deletedRows;
    }
    /**
     * Purge old identity notifications before the start date parameter
     * @param startDate
     * @return 
     */
    public int purgeOldIdentityNotifications(Date startDate) {
        Query deleteQuery = em.createQuery("DELETE FROM IdentityNotifications i WHERE i.notifiedOn < :messageTime");
        deleteQuery.setParameter("messageTime", startDate);
        int deletedRows = deleteQuery.executeUpdate();
        System.out.println("Purge old notification alerts before date:" + startDate+", total deleted alerts: "+deletedRows);
        return deletedRows;
    }
    /**
     * Purge all audit records before the start Date parameter
     * @param startDate
     * @return 
     */
    public int purgeOldAuditRecords(Date startDate) {
        Query deleteQuery = em.createQuery("DELETE FROM AuditTrail i WHERE i.performedAt < :messageTime");
        deleteQuery.setParameter("messageTime", startDate);
        int deletedRows = deleteQuery.executeUpdate();
        System.out.println("Purge old audit records before date:" + startDate+", total deleted audit records: "+deletedRows);
        return deletedRows;
    }
    /**
     * Get all system accounts/identities
     * @return 
     */
    public List<TenantSettings> getIdentityLists() {
        List<TenantSettings> tenantList = null;
        Query query = em.createQuery("SELECT t FROM TenantSettings t");
        try {
            tenantList = (List<TenantSettings>) query.getResultList();
            for(TenantSettings tenant:tenantList){
                tenant.setCurrentDevices(getCurentDevices(tenant.getIdentity()));
                tenant.setCurrentUsers(getCurentUsers(tenant.getIdentity()));
            }
            return tenantList;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load identity list!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Get current users count in a specific identity
     * @param identity
     * @return 
     */
    public int getCurentUsers(int identity) {
        Query query=em.createNativeQuery("SELECT count(*) FROM Users u WHERE u.identity_Id = "+identity);
        long count=(Long)query.getSingleResult();
        return (int)count;
    }
    /**
     * Get current devices count in a specific identity
     * @param identity
     * @return 
     */
    public int getCurentDevices(int identity) {
        Query query=em.createNativeQuery("SELECT count(*) FROM Devices d WHERE d.identity = "+identity);
        long count=(Long)query.getSingleResult();
        return (int)count;
    }
    /**
     * Register a new barcode for a device model
     * @param newBarcode
     * @param model
     * @return 
     */
    public String registerNewBarcode(String newBarcode, int model) {
        BarCodes barcode=new BarCodes();
        barcode.setBarCode(newBarcode);
        barcode.setDeviceModel(model);
        barcode.setCreationDate(new Date());
        em.persist(barcode);
        return barcode.getBarCode();
    }
    /**
     * Add new Account/Identity
     * @param accountName
     * @param emailAddress
     * @param statusVal
     * @param maxUsersVal
     * @param maxDevicesVal
     * @param maxSmsVal
     * @return 
     */
    public int addNewAccount(String accountName, String emailAddress, int statusVal, int maxUsersVal, int maxDevicesVal, int maxSmsVal) {
        TenantSettings tenant=new TenantSettings();
        tenant.setIdentityName(accountName);
        tenant.setAdminUserEmail(emailAddress);
        tenant.setTenantStatus(statusVal);
        tenant.setMaxDevices(maxDevicesVal);
        tenant.setMaxUsers(maxUsersVal);
        tenant.setSmsQouta(maxSmsVal);
        tenant.setUpdateTime(new Date());
        tenant.setMaxRetainedMessages(150);
        tenant.setAlertEmailLanguage(1);
        tenant.setAlertEmailMessage(1);
        tenant.setAlertEmailOffline(1);
        tenant.setAlertEmailOnline(1);
        tenant.setAlertGracePeriod(5);
        tenant.setDeviceRegistrationEmail(1);
        tenant.setDeviceUpdateEmail(1);
        tenant.setFaultNotificationEmail(1);
        tenant.setPingInterval(15);
        tenant.setPurgeAfter(15);
        tenant.setSmsConsumed(0);
        tenant.setTimezone("Asia/Qatar");
        tenant.setUpdateInterval(20);
        tenant.setIdentity(getNewIdentityNumber());
        em.persist(tenant);
        return tenant.getIdentity();
    }
    /**
     * Get the next available identity number
     * @return 
     */
    private Integer getNewIdentityNumber() {
        Query query=em.createNativeQuery("SELECT max(identity)+1 FROM iot.tenant_settings");
        long count=(Long)query.getSingleResult();
        return (int)count;
    }
    /**
     * Activate account/identity
     * @param accountIdVal
     * @return 
     */
    public boolean activateAccount(int accountIdVal) {
        Query query = em.createQuery("SELECT t FROM TenantSettings t WHERE t.identity = :identity");
        query.setParameter("identity", accountIdVal);
        TenantSettings tenantSettings = null;
        try {
            tenantSettings = (TenantSettings) query.getSingleResult();
            tenantSettings.setTenantStatus(IConstant.STATUS_ACTIVE);
            tenantSettings.setUpdateTime(new Date());
            em.persist(tenantSettings);
            List<Users> userList=getUsersForIdentity(accountIdVal);
            for(Users user: userList){
                user.setUserStatus(IConstant.STATUS_ACTIVE);
                user.setUpdateTime(new Date());
                em.persist(user);
            }
            List<Devices> deviceList=getUserDevices(accountIdVal, false,false,null);
            for(Devices device: deviceList){
                device.setDeviceStatus(IConstant.STATUS_ACTIVE);
                device.setUpdateTime(new Date());
                em.persist(device);
            }            
            return true;
        } catch (Exception ex) {
            System.out.println("Reason: Can't activate Tenant " + accountIdVal);
            ex.printStackTrace();
            return false;
        }
    }
    /**
     * Deactivate account/identity
     * @param accountIdVal
     * @return 
     */
    public boolean inactivateAccount(int accountIdVal) {
        Query query = em.createQuery("SELECT t FROM TenantSettings t WHERE t.identity = :identity");
        query.setParameter("identity", accountIdVal);
        TenantSettings tenantSettings = null;
        try {
            tenantSettings = (TenantSettings) query.getSingleResult();
            tenantSettings.setTenantStatus(IConstant.STATUS_INACTIVE);
            tenantSettings.setUpdateTime(new Date());
            em.persist(tenantSettings);
            List<Users> userList=getUsersForIdentity(accountIdVal);
            for(Users user: userList){
                user.setUserStatus(IConstant.STATUS_INACTIVE);
                user.setUpdateTime(new Date());
                em.persist(user);
            }
            List<Devices> deviceList=getUserDevices(accountIdVal, false,false,null);
            for(Devices device: deviceList){
                device.setDeviceStatus(IConstant.STATUS_INACTIVE);
                device.setUpdateTime(new Date());
                em.persist(device);
            }
            return true;
        } catch (Exception ex) {
            System.out.println("Reason: Can't inactivate Tenant " + accountIdVal);
            ex.printStackTrace();
            return false;
        }
    }
    /**
     * Update account/identity
     * @param accountIdVal
     * @param accountName
     * @param emailAddress
     * @param maxUsersVal
     * @param maxDevicesVal
     * @param maxSmsVal 
     */
    public void updateAccount(int accountIdVal, String accountName, String emailAddress, int maxUsersVal, int maxDevicesVal, int maxSmsVal) {
        Query query = em.createQuery("SELECT t FROM TenantSettings t WHERE t.identity = :identity");
        query.setParameter("identity", accountIdVal);
        TenantSettings tenantSettings = null;
        try {
            tenantSettings = (TenantSettings) query.getSingleResult();
            tenantSettings.setIdentityName(accountName);
            tenantSettings.setAdminUserEmail(emailAddress);
            tenantSettings.setMaxDevices(maxDevicesVal);
            tenantSettings.setMaxUsers(maxUsersVal);
            tenantSettings.setSmsQouta(maxSmsVal);
            tenantSettings.setUpdateTime(new Date());
            em.persist(tenantSettings);
        } catch (Exception ex) {
            System.out.println("Reason: Can't update Tenant " + accountIdVal);
            ex.printStackTrace();
        }
    }
    /**
     * Update Tenant Admin User Details
     * @param emailAddress
     * @param accountIdVal
     * @param workflow
     * @param scheduler
     * @param dashboard
     * @param simulation
     * @param userManagement
     * @return 
     */
    public Users updateTenantAdminUser(String emailAddress, int accountIdVal, String workflow, String scheduler, String dashboard, String simulation, String userManagement) {
        List<Users> userList=getUsersForIdentity(accountIdVal);
        Users adminUser=null;
        for(Users user: userList){
            if (dashboard == null) {
                user.setDashboardCreation(0);
            } else {
                user.setDashboardCreation(1);
            }
            if (simulation == null) {
                user.setSimulationCreation(0);
            } else {
                user.setSimulationCreation(1);
            }
            if (userManagement == null) {
                user.setUserCreation(0);
            } else {
                user.setUserCreation(1);
            }
            if (scheduler == null) {
                user.setSchedulerCreation(0);
            } else {
                user.setSchedulerCreation(1);
            }
            if (workflow == null) {
                user.setWorkflowCreation(0);
            } else {
                user.setWorkflowCreation(1);
            }
            if(user.getEmailAddress().equalsIgnoreCase(emailAddress)){
                user.setIdentityAdmin(1);
                adminUser=user;
            }else{
                user.setIdentityAdmin(0);
            }
            user.setUpdateTime(new Date());
            em.persist(user);
        }
        return adminUser;
    }
    /**
     * Get Admin User by Email for an account/identity
     * @param adminUserEmail
     * @param accountIdVal
     * @return 
     */
    public Users getUser(String adminUserEmail, int accountIdVal) {
        Query query = em.createQuery("SELECT u FROM Users u WHERE u.emailAddress = :emailAddress and u.identityId = :identityId");
        query.setParameter("emailAddress", adminUserEmail);
        query.setParameter("identityId", accountIdVal);
        Users user = null;
        try {
            user = (Users) query.getSingleResult();
            return user;
        } catch (Exception ex) {
            System.out.println("Reason: Can't load admin user for Tenant " + accountIdVal);
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Get device model by id
     * @param deviceModelId
     * @return 
     */
    public DeviceModel getDeviceModelById(int deviceModelId) {
        DeviceModel model = em.find(DeviceModel.class, deviceModelId);
        model.sethWType(em.find(HwType.class, model.getHwType()));
        return model;
    }
    /**
     * Save new device model firmware including the binary and source code
     * @param modelId
     * @param newVersion
     * @param fileData
     * @param sourceCode
     * @return 
     */
    public boolean saveNewFirmware(int modelId, int newVersion, byte[] fileData, byte[] sourceCode) {
        DeviceModel model=em.find(DeviceModel.class, modelId);
        if(model!=null){
            model.setFirmwareVersion(newVersion);
            model.setFirmwareCode(fileData);
            model.setFirmwareSource(sourceCode);
            model.setUpdateTime(new Date());
            em.persist(model);
            return true;
        }
        return false;
    }
    /**
     * Load device count for specific device model
     * @param modelId
     * @return 
     */
    private int loadDeviceCountPerModel(Integer modelId) {
        Query query=em.createNativeQuery("SELECT count(*) FROM Devices d WHERE d.device_model = "+modelId);
        long count=(Long)query.getSingleResult();
        return (int)count;
    }
    /**
     * Deactivate device model
     * @param deviceModelId
     * @return 
     */
    public boolean inactivateDeviceModel(int deviceModelId) {
        DeviceModel model = em.find(DeviceModel.class, deviceModelId);
        if(model!=null){
            model.setModelStatus(IConstant.STATUS_INACTIVE);
            model.setUpdateTime(new Date());
            em.persist(model);
            return true;
        }
        return false;
    }
    /**
     * Activate Device Model
     * @param deviceModelId
     * @return 
     */
    public boolean activateDeviceModel(int deviceModelId) {
        DeviceModel model = em.find(DeviceModel.class, deviceModelId);
        if(model!=null){
            model.setModelStatus(IConstant.STATUS_ACTIVE);
            model.setUpdateTime(new Date());
            em.persist(model);
            return true;
        }
        return false;
    }
    /**
     * Check if device Model Name Exist or Not
     * @param modelName
     * @return 
     */
    public boolean checkModelNameIsThere(String modelName) {
        Query query = em.createQuery("SELECT d FROM DeviceModel d where d.deviceName = :name");
        query.setParameter("name", modelName);
        try {
            List<DeviceModel> deviceModelList = (List<DeviceModel>) query.getResultList();
            if(deviceModelList==null || deviceModelList.size()==0) return false;
            return true;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load device models!");
            ex.printStackTrace();
            return false;
        }
    }
    /**
     * Create new device model
     * @param name
     * @param version
     * @param status
     * @param deviceSensor
     * @param deviceControl1
     * @param deviceControl2
     * @param highAlert1
     * @param highAlert2
     * @param lowAlert1
     * @param lowAlert2
     * @param hwType
     * @param sensor1
     * @param sensor2
     * @param sensorIcon1
     * @param sensorIcon2
     * @param normal
     * @param alert
     * @param error
     * @param sensor1Graph
     * @param sensor2Graph
     * @param noOfSensors
     * @return 
     */
    public int createNewModel(String name, int version, int status, boolean deviceSensor, boolean deviceControl1, boolean deviceControl2, String highAlert1, 
            String highAlert2, String lowAlert1, String lowAlert2, int hwType, String sensor1, String sensor2, int sensorIcon1, int sensorIcon2, String normal,
            String alert, String error, int sensor1Graph, int sensor2Graph, int noOfSensors) {
        int countOfModels=0;
        if(deviceSensor){        
            DeviceModel model=new DeviceModel();
            model.setDeviceName(name);
            model.setFirmwareVersion(version);
            model.setModelStatus(status);
            model.setUpdateTime(new Date());
            model.setNoOfSensors(noOfSensors);
            model.setNoOfDevices(0);
            model.setHighAlertValue1(highAlert1);
            model.setHighAlertValue2(highAlert2);
            model.setLowAlertValue1(lowAlert1);
            model.setLowAlertValue2(lowAlert2);
            model.setHwType(hwType);
            model.setSimulationNormalMsg(normal);
            model.setSimulationAlertMsg(alert);
            model.setSimulationErrorMsg(error);
            model.setSensor1Name(sensor1);
            model.setSensor2Name(sensor2);
            model.setSensor1Icon(sensorIcon1);
            model.setSensor2Icon(sensorIcon2);
            model.setSensor1Graph(sensor1Graph);
            model.setSensor2Graph(sensor2Graph);
            model.setId(getModelId(10000));
            em.persist(model);
            countOfModels++;
        }
        if(deviceControl1){
            DeviceModel model=new DeviceModel();
            model.setDeviceName(name);
            model.setFirmwareVersion(version);
            model.setModelStatus(status);
            model.setUpdateTime(new Date());
            model.setNoOfSensors(noOfSensors);
            model.setNoOfDevices(1);
            model.setHighAlertValue1(highAlert1);
            model.setHighAlertValue2(highAlert2);
            model.setLowAlertValue1(lowAlert1);
            model.setLowAlertValue2(lowAlert2);
            model.setHwType(hwType);
            model.setSimulationNormalMsg(normal);
            model.setSimulationAlertMsg(alert);
            model.setSimulationErrorMsg(error);
            model.setSensor1Icon(sensorIcon1);
            model.setSensor2Icon(sensorIcon2);
            model.setId(getModelId(20000));
            em.persist(model);     
            countOfModels++;
        }
        if(deviceControl2){
            DeviceModel model=new DeviceModel();
            model.setDeviceName(name);
            model.setFirmwareVersion(version);
            model.setModelStatus(status);
            model.setUpdateTime(new Date());
            model.setNoOfSensors(noOfSensors);
            model.setNoOfDevices(2);
            model.setHighAlertValue1(highAlert1);
            model.setHighAlertValue2(highAlert2);
            model.setLowAlertValue1(lowAlert1);
            model.setLowAlertValue2(lowAlert2);
            model.setHwType(hwType);
            model.setSimulationNormalMsg(normal);
            model.setSimulationAlertMsg(alert);
            model.setSimulationErrorMsg(error);
            model.setSensor1Icon(sensorIcon1);
            model.setSensor2Icon(sensorIcon2);
            model.setId(getModelId(1000000));
            em.persist(model);
            countOfModels++;
        }
        return countOfModels;
    }
    /**
     * Get Device model by Id
     * @param id
     * @return 
     */
    public int getModelId(int id) {
        Query query=em.createNativeQuery("SELECT max(id) FROM Device_Model m WHERE m.id<"+id);
        int maxId=(int)query.getSingleResult();
        return maxId+1;
    }
    /**
     * Get all ordered list of audit records for specific identity and optional action
     * @param maxRows
     * @param identity
     * @param action
     * @param sysAdmin
     * @return 
     */
    public List<Object> selectAllAuditRecords(int maxRows,int identity, int action,boolean sysAdmin) {
        String queryString="SELECT a.id, u.email_address, ac.actions_name, a.performed_at, a.parameters FROM audit_trail a,users u, actions_lov ac " +
            "where a.action_id=ac.action_id and a.by_user_id=u.id";
        if(!sysAdmin){
            queryString+=" and u.identity_id="+identity;
        }
        if(action!=1000){
            queryString+=" and ac.action_id="+action;
        }
        queryString+=" order by a.id desc";
        Query selectQuery = em.createNativeQuery(queryString);
        selectQuery.setMaxResults(maxRows);
        List<Object> results=selectQuery.getResultList();
        return results;
    }
    /**
     * Save system global configurations
     * @param host
     * @param http
     * @param https
     * @param email_enabled
     * @param language
     * @param emailUser
     * @param emailpassword
     * @param server
     * @param ssl
     * @param port
     * @param rest
     * @param android
     * @param iOs
     * @param timezone
     * @return 
     */
    public SystemConfig saveSystemConfigurations(String host, int http, int https, int email_enabled, int language, String emailUser, String emailpassword, 
            String server, int ssl, String port, String rest, String android, String iOs, String timezone) {
        SystemConfig newConfigurations=em.find(SystemConfig.class, 1);
        newConfigurations.setAndroidVersion(android);
        newConfigurations.setApiVersion(rest);
        newConfigurations.setDefaultEmailLanguage(language);
        newConfigurations.setDefaultTimezone(timezone);
        newConfigurations.setEmailEnabled(email_enabled);
        newConfigurations.setEmailPassword(emailpassword);
        newConfigurations.setEmailUser(emailUser);
        newConfigurations.setIosVersion(iOs);
        newConfigurations.setPlatformHost(host);
        newConfigurations.setPlatformHttpPort(http);
        newConfigurations.setPlatformHttpsPort(https);
        newConfigurations.setSmtpPort(port);
        newConfigurations.setSmtpServerIp(server);
        newConfigurations.setUpdatedDate(new Date());
        newConfigurations.setUseSSL(ssl);
        em.persist(newConfigurations);
        return newConfigurations;
    }
    /**
     * Get all system actions to use for filtering Audit Records
     * @return 
     */
    //TODO: need to add additional column for admin actions filterations
    public List<ActionsLov> getAllActions() {
        List<ActionsLov> actionsList = null;
        Query query = em.createQuery("SELECT a FROM ActionsLov a");
        try {
            actionsList = (List<ActionsLov>) query.getResultList();
            return actionsList;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load actions list!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Generate Statistics Report
     * @param source
     * @param eventVal
     * @param durationVal
     * @param identity
     * @param maxRows
     * @return 
     */
    public List<String> generateReport(String source, int eventVal, int durationVal, int identity, int maxRows) {
        String queryString="";
        //System.out.println("Event:"+eventVal);
        if(eventVal==1){ //devices report
            queryString="SELECT concat(d.device_id,',\"',d.friendly_name,'\",', m.device_name,',',ifnull(d.firmware_version,'N\\A'),',', d.bar_code,',', d.device_status,',\"'," +
                "d.location,'\",',ifnull(d.last_ping_time,'N\\A'),',\"', ifnull(d.tags,'N\\A'),'\",', ifnull(d.update_time,'N\\A'),',', ifnull(d.total_inbound,'N\\A'),',', ifnull(d.total_outbound,'N\\A'))"
                    + " FROM iot.devices d, iot.device_model m where d.identity="+identity+" and d.device_model=m.id";
            //source 0 : all devices so no filter ..
            if(source.equals("1")){//only active
                queryString+=" and d.device_status=1";
            }else if(source.equals("2")){//only inactive
                queryString+=" and d.device_status=2";
            }else if(source.equals("3")){//only inactive
                queryString+=" and d.device_status=0";
            }else {
                if(!source.equals("0")){ // specific device is required
                    queryString+=" and d.device_id='"+source+"'";
                }
            }
            if(durationVal==1) { //one day
                queryString+=" and d.update_time >= ( CURDATE() - INTERVAL 1 DAY )";
            }else if(durationVal==2){ // one week
                queryString+=" and d.update_time >= ( CURDATE() - INTERVAL 7 DAY )";
            }else if(durationVal==3){ // one month
                queryString+=" and d.update_time >= ( CURDATE() - INTERVAL 1 MONTH )";
            }//4 means no duration /
        }else{ //case 2 or others i.e. messages report
            queryString="SELECT concat(m.device_id,',', m.id,',\"', m.payload,'\",', t.type,',', m.message_time) FROM iot.messages m, iot.message_types t, iot.devices d where m.type=t.id and m.device_id=d.device_id and d.identity="+identity;                    
            switch(eventVal){
                case 3: // high alert messages ..
                    queryString+=" and m.type=2";
                    break;        
                case 4: // low alert messages ..
                    queryString+=" and m.type=4";
                    break;        
                case 5: // login messages ..
                    queryString+=" and m.type=0";
                    break;        
                case 6: // upgrade messages ..
                    queryString+=" and m.type=7";
                    break;        
                case 7: // error messages ..
                    queryString+=" and m.type=6";
                    break;        
            }
            //source 0 : all devices so no filter ..
            if(source.equals("1")){//only active
                queryString+=" and m.device_id=d.device_id and d.device_status=1";
            }else if(source.equals("2")){//only inactive
                queryString+=" and m.device_id=d.device_id and d.device_status=2";
            } else {
                if(!source.equals("0")){ // specific device is required
                    queryString+=" and m.device_id='"+source+"'";
                }
            }
            if(durationVal==1) { //one day
                queryString+=" and m.message_time >= ( CURDATE() - INTERVAL 1 DAY )";
            }else if(durationVal==2){ // one week
                queryString+=" and m.message_time >= ( CURDATE() - INTERVAL 7 DAY )";
            }else if(durationVal==3){ // one month
                queryString+=" and m.message_time >= ( CURDATE() - INTERVAL 1 MONTH )";
            }//4 means no duration /
            queryString+=" order by m.id desc";
        }
        System.out.println("Final Report Query:"+queryString);
        Query selectQuery = em.createNativeQuery(queryString);
        selectQuery.setMaxResults(maxRows); //TODO: need to find a value here ...
        List<String> results=selectQuery.getResultList();
        return results;
    }
    /**
     * Get List of Hardware Types supported in the system
     * @return 
     */
    public List<HwType> getHWTypes() {
        List<HwType> hwTypeList = null;
        Query query = em.createQuery("SELECT h FROM HwType h");
        try {
            hwTypeList = (List<HwType>) query.getResultList();
            return hwTypeList;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load HW Types list!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Activate Device in a specific identity
     * @param deviceId
     * @param identityId
     * @return 
     */
    public Devices activateDevice(String deviceId, Integer identityId) {
        System.out.println("Activate device status for device id =" + deviceId);
        Query query = em.createQuery("SELECT d FROM Devices d WHERE d.deviceId = :deviceId and d.identity = :identity");
        query.setParameter("deviceId", deviceId);
        query.setParameter("identity", identityId);
        Devices device = null;
        try {
            device = (Devices) query.getSingleResult();
            device.setDeviceStatus(IConstant.STATUS_ACTIVE);
            device.setUpdateTime(new Date());
            em.persist(device);
            return device;
        } catch (Exception ex) {
            System.out.println("Reason: Device status update error!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Deactivate device in a specific identity
     * @param deviceId
     * @param identityId
     * @return 
     */
    public Devices inactivateDevice(String deviceId, Integer identityId) {
        System.out.println("Deactivate device status for device id =" + deviceId);
        Query query = em.createQuery("SELECT d FROM Devices d WHERE d.deviceId = :deviceId and d.identity = :identity");
        query.setParameter("deviceId", deviceId);
        query.setParameter("identity", identityId);
        Devices device = null;
        try {
            device = (Devices) query.getSingleResult();
            device.setDeviceStatus(IConstant.STATUS_INACTIVE);
            device.setUpdateTime(new Date());
            em.persist(device);
            return device;
        } catch (Exception ex) {
            System.out.println("Reason: Device status update error!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Get list of all identity simulations
     * @param identityId
     * @return 
     */
    public List<Simulations> getIdentitySimulations(Integer identityId) {
        List<Simulations> userSimulationList = null;
        Query query = em.createQuery("SELECT s FROM Simulations s WHERE s.identityId = :identityId");
        query.setParameter("identityId", identityId);
        try {
            userSimulationList = (List<Simulations>) query.getResultList();
            for (int i = 0; i < userSimulationList.size(); i++) {
                userSimulationList.get(i).setDevice(getDeviceById(userSimulationList.get(i).getDeviceId(),false));
            }
            return userSimulationList;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load user simulations for this identity!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Start specific simulation in this identity
     * @param simulationIdVal
     * @param identityId
     * @return 
     */
    public boolean startSimulation(int simulationIdVal, int identityId) {
        Simulations simulation=em.find(Simulations.class, simulationIdVal);
        if(simulation!=null && identityId==simulation.getIdentityId()){
            Devices device=getDeviceById(simulation.getDeviceId(),false);
            if(device!=null){
                simulation.setOriginalDeviceStatus(device.getDeviceStatus());
                simulation.setSimulationStatus(IConstant.STATUS_ACTIVE);
                simulation.setStartTime(new Date());
                simulation.setLoopCounter(10);
                em.persist(simulation);
                RecievedMessages message=new RecievedMessages();
                message.setDeviceId(device.getDeviceId());
                message.setMessageTime(new Date());
                message.setPayload("SIM START");
                message.setType(0);
                em.persist(message);
                device.setLastPingTime(message.getMessageTime());
                device.setTotalMessages(device.getTotalMessages()+1);               
                device.setDeviceStatus(IConstant.STATUS_SIMULATION);
                device.setSecretKey("");
                device.setUpdateTime(new Date());
                device.setOfflineFlag(IConstant.STATUS_ONLINE);
                em.persist(device);
                return true;
            }
        }
        return false;
    }
    /**
     * Stop simulation in this identity
     * @param simulationIdVal
     * @param identityId
     * @return 
     */
    public boolean stopSimulation(int simulationIdVal, int identityId) {
        Simulations simulation=em.find(Simulations.class, simulationIdVal);
        if(simulation!=null && simulation.getIdentityId()==identityId){
            Devices device=getDeviceById(simulation.getDeviceId(),false);
            if(device!=null){
                device.setDeviceStatus(simulation.getOriginalDeviceStatus());
                device.setUpdateTime(new Date());
                em.persist(device);
                simulation.setSimulationStatus(IConstant.STATUS_INACTIVE);
                simulation.setStartTime(new Date());
                em.persist(simulation);
                return true;
            }
        }
        return false;
    }
    /**
     * Remove and Stop simulation
     * @param simulationIdVal
     * @param identityId
     * @return 
     */
    public boolean removeAndStopStimulation(int simulationIdVal, int identityId) {
        if(stopSimulation(simulationIdVal, identityId)){
            //delete simulation
            Simulations simulation=em.find(Simulations.class, simulationIdVal);
            em.remove(simulation);
            return true;
        }
        return false;
    }
    /**
     * Add new simulation in an identity
     * @param name
     * @param source
     * @param alertVal
     * @param errorVal
     * @param durationVal
     * @param status
     * @param identityId
     * @return 
     */
    public Simulations addNewSimulation(String name, String source, float alertVal, float errorVal, int durationVal, int status, Integer identityId) {
        Devices device=getDeviceById(source, true);
        if(device==null) return null;
        Simulations simulation=new Simulations();
        simulation.setDeviceId(device.getDeviceId());
        simulation.setOriginalDeviceStatus(device.getDeviceStatus());
        simulation.setDuration(durationVal);
        simulation.setIdentityId(identityId);
        simulation.setAlertProbability(alertVal);
        simulation.setErrorProbanility(errorVal);
        simulation.setName(name);
        simulation.setSimulationStatus(status);
        em.persist(simulation);
        em.flush();
        return simulation;
    }
    /**
     * Save device traffic (request & response)
     * @param deviceId
     * @param requestSize
     * @param responseSize 
     */
    //TODO: need to add identity Id here even if device id is unique
    public void saveDeviceTraffic(String deviceId, int requestSize, int responseSize) {
        Devices device=getDeviceById(deviceId, false);
        if(device==null) return;
        device.setTotalInbound(device.getTotalInbound()+requestSize);
        device.setTotalInboundMonth(device.getTotalInboundMonth()+requestSize);
        device.setTotalOutbound(device.getTotalOutbound()+responseSize);
        device.setTotalOutboundMonth(device.getTotalOutboundMonth()+responseSize);
        em.persist(device);
    }
    /**
     * Get user's dashboard
     * @param id
     * @return 
     */
    public String getUserDashboard(Integer id) {
        Users user=em.find(Users.class, id);
        if(user!=null) return user.getDashboard();
        return null;
    }
    /**
     * Get all system automated jobs
     * @return 
     */
    public List<SystemJobs> getJobsList() {
        List<SystemJobs> jobsList = null;
        Query query = em.createQuery("SELECT s FROM SystemJobs s");
        try {
            jobsList = (List<SystemJobs>) query.getResultList();
            return jobsList;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load system jobs list!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Enable system job
     * @param jobId 
     */
    public void enableJob(int jobId) {
        SystemJobs job=em.find(SystemJobs.class, jobId);
        job.setStatus(IConstant.STATUS_ACTIVE);
        em.persist(job);
    }
    /**
     * Disable system job
     * @param jobId 
     */
    public void disableJob(int jobId) {
        SystemJobs job=em.find(SystemJobs.class, jobId);
        job.setStatus(IConstant.STATUS_INACTIVE);
        em.persist(job);
    }
    /**
     * List of device group of a specific identity with optional include of device counts in each device group
     * @param identityId
     * @param countIncluded
     * @return 
     */
    public List<DeviceGroup> getIdentityDeviceGroups(Integer identityId, boolean countIncluded) {
        List<DeviceGroup> userDeviceGroupList = null;
        Query query = em.createQuery("SELECT d FROM DeviceGroup d WHERE d.identityId = :identityId");
        query.setParameter("identityId", identityId);
        query.setMaxResults(50);
        try {
            userDeviceGroupList = (List<DeviceGroup>) query.getResultList();
            if(countIncluded){
                for (int i = 0; i < userDeviceGroupList.size(); i++) {
                    userDeviceGroupList.get(i).setDeviceCount(getDeviceGroupDevicesCount(userDeviceGroupList.get(i).getId()));
                }
            }
            return userDeviceGroupList;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load identity device groups!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Get count of devices in specific device group
     * @param deviceGroupId
     * @return 
     */
    //TODO: Need to add identity id 
    private Integer getDeviceGroupDevicesCount(int deviceGroupId) {
        Query query=em.createNativeQuery("SELECT count(*) FROM iot.devices where group_id="+deviceGroupId);
        long count=(Long)query.getSingleResult();
        return (int)count;
    }
    /**
     * Create new device group in a specific identity
     * @param name
     * @param identityId
     * @return 
     */
    public DeviceGroup addNewDeviceGroup(String name, Integer identityId) {
        DeviceGroup deviceGroup=new DeviceGroup();
        deviceGroup.setName(name);
        deviceGroup.setIdentityId(identityId);
        em.persist(deviceGroup);
        return deviceGroup;
    }
    /**
     * Delete device group
     * @param deviceGroupIdVal
     * @param identityId
     * @return 
     */
    public boolean removeDeviceGroup(Integer deviceGroupIdVal, Integer identityId) {
        System.out.println("Delete Device Group:"+deviceGroupIdVal+" identity="+identityId);
        //check the apps
        //if any app is using this device group, delete the app 1st 
        removeAssignedDeviceGroup(deviceGroupIdVal,identityId);
        removeWorkflowsFroThisDeviceGroup(deviceGroupIdVal,identityId);
        DeviceGroup deviceGroup=em.find(DeviceGroup.class, deviceGroupIdVal);
        System.out.println("Delete Device Group:"+deviceGroup.getId());
        if(deviceGroup!=null && deviceGroup.getIdentityId().equals(identityId)){
            em.remove(deviceGroup);
            return true;
        }
        return false;
    }
    /**
     * Remove assign device group from all devices in that device group
     * @param deviceGroupIdVal
     * @param identityId
     * @return 
     */
    public boolean removeAssignedDeviceGroup(int deviceGroupIdVal, Integer identityId) {
        List<Devices> deviceList = getUserDevices(identityId, false, false, ""+deviceGroupIdVal);
        for(Devices device:deviceList){
            device.setGroupId(null);
            em.persist(device);
        }
        return true;
    }
    /**
     * Activate device group
     * @param deviceGroupIdVal
     * @param identityId
     * @return 
     */
    public boolean activateDeviceGroup(int deviceGroupIdVal, Integer identityId) {
        List<Devices> deviceList = getUserDevices(identityId, false, false, ""+deviceGroupIdVal);
        for(Devices device:deviceList){
            device.setDeviceStatus(IConstant.STATUS_ACTIVE);
            em.persist(device);
        }
        return true;
    }
    /**
     * Deactivate device group
     * @param deviceGroupIdVal
     * @param identityId
     * @param actionBy
     * @return 
     */
    public boolean restartDeviceGroup(int deviceGroupIdVal, Integer identityId, Integer actionBy) {
        List<Devices> deviceList = getUserDevices(identityId, false, false, ""+deviceGroupIdVal);
        for(Devices device:deviceList){
            device.setLastAction(IConstant.ACTION_RESTART);
            device.setLastActionBy(actionBy);
            em.persist(device);
        }
        return true;
    }
    /**
     * Request update from a device group which will send message to all devices to send new update messages
     * @param deviceGroupIdVal
     * @param identityId
     * @param actionBy
     * @return 
     */
    public boolean requestUpdateFromDeviceGroup(int deviceGroupIdVal, Integer identityId, Integer actionBy) {
        List<Devices> deviceList = getUserDevices(identityId, false, false, ""+deviceGroupIdVal);
        for(Devices device:deviceList){
            device.setLastAction(IConstant.ACTION_GET_MESSAGE);
            device.setLastActionBy(actionBy);
            em.persist(device);
        }
        return true;
    }
    /**
     * Deactivate device group
     * @param deviceGroupIdVal
     * @param identityId
     * @return 
     */
    public boolean inactivateDeviceGroup(int deviceGroupIdVal, Integer identityId) {
        List<Devices> deviceList = getUserDevices(identityId, false, false, ""+deviceGroupIdVal);
        for(Devices device:deviceList){
            device.setDeviceStatus(IConstant.STATUS_INACTIVE);
            em.persist(device);
        }
        return true;
    }
    /**
     * Remove workflow for a device group
     * @param deviceGroupIdVal
     * @param identityId 
     */
    private void removeWorkflowsFroThisDeviceGroup(Integer deviceGroupIdVal, Integer identityId) {
        List<Workflows> deviceGroupWorkflowList = null;
        Query query = em.createQuery("SELECT w FROM Workflows w WHERE w.type=2 and w.targetDeviceId=:groupId and w.identity = :identity");
        query.setParameter("identity", identityId);
        query.setParameter("groupId", deviceGroupIdVal);
        try {
            deviceGroupWorkflowList = (List<Workflows>) query.getResultList();
            for (int i = 0; i < deviceGroupWorkflowList.size(); i++) {
                em.remove(deviceGroupWorkflowList.get(i));
            }
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load identity workflows for this device group!");
            ex.printStackTrace();
        }
    }
    /**
     * Check if a workflow with same configurations already exist
     * @param source
     * @param target
     * @param sourceAction
     * @param targetAction
     * @return 
     */
    private boolean checkWorkflowAlreadyExist(String source,String target, int sourceAction, int targetAction) {
        List<Workflows> workflowList = null;
        Query query = em.createQuery("SELECT w FROM Workflows w WHERE w.triggeringId=:triggerId and w.triggerEvent=:triggerEvent"
                + " and w.targetDeviceId=:targetDeviceId and w.targetAction = :targetAction");
        query.setParameter("triggerId", source);
        query.setParameter("triggerEvent", sourceAction);
        query.setParameter("targetDeviceId", target);
        query.setParameter("targetAction", targetAction);
        try {
            workflowList = (List<Workflows>) query.getResultList();
            if(workflowList==null || workflowList.size()==0){
                return false;
            }
            return true;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load identity workflows for this device group!");
            ex.printStackTrace();
            return false;
        }
    }
    /**
     * Update device group information
     * @param deviceGroupId
     * @param identityId
     * @param groupName
     * @param devManagement
     * @param contact
     * @param email
     * @param mobile
     * @param alerts
     * @param highAlert1
     * @param highAlert2
     * @param lowAlert1
     * @param lowAlert2
     * @return 
     */
    public DeviceGroup updateDeviceGroup(int deviceGroupId, Integer identityId, String groupName, String devManagement, String contact, String email, String mobile, String alerts, String highAlert1, String highAlert2, String lowAlert1, String lowAlert2) {
        DeviceGroup deviceGroup=getDeviceGroupById(deviceGroupId, identityId);
        deviceGroup.setName(groupName);
        em.persist(deviceGroup);
        List<Devices> deviceGroupDevices=getUserDevices(identityId,false,false,""+deviceGroupId);
        for(Devices device:deviceGroupDevices){
            device.setDeviceManagement(Integer.parseInt(devManagement));
            if(contact!=null){
                device.setNotificationEmail(email);
                device.setNotificationMobile(mobile);
            }
            if(alerts!=null){
                device.setHighAlertValue1(highAlert1);
                device.setHighAlertValue2(highAlert2);
                device.setLowAlertValue1(lowAlert1);
                device.setLowAlertValue2(lowAlert2);
            }
            em.persist(device);
        }
        return deviceGroup;
    }
    /**
     * Get identity list of applications
     * @param identityId
     * @return 
     */
    public List<Applications> getIdentityApplications(Integer identityId) {
        List<Applications> userDeviceGroupList = null;
        Query query = em.createQuery("SELECT a FROM Applications a WHERE a.identityId = :identityId");
        query.setParameter("identityId", identityId);
        query.setMaxResults(50);
        try {
            userDeviceGroupList = (List<Applications>) query.getResultList();
            return userDeviceGroupList;
        } catch (Exception ex) {
            System.out.println("Reason: Couldn't load identity applications!");
            ex.printStackTrace();
            return null;
        }
    }
    /**
     * Save new application for an identity
     * @param identityId
     * @param name
     * @param scope
     * @param statusVal
     * @param typeVal
     * @param loginVal
     * @param alertVal
     * @param email
     * @param mobile
     * @param deviceGroup
     * @param bannerName
     * @param fileData
     * @return 
     */
    public Applications saveNewApplication(Integer identityId, String name, String scope, int statusVal, int typeVal, int loginVal, 
            int alertVal,String email, String mobile, String deviceGroup,String bannerName, byte[] fileData) {
        Applications newApp=new Applications();
        newApp.setName(name);
        newApp.setProjectScope(scope);
        newApp.setAlertEnabled(alertVal);
        newApp.setApplicationType(typeVal);
        newApp.setBanner(fileData);
        newApp.setBannerName(bannerName);
        newApp.setContactEmail(email);
        newApp.setContactMobile(mobile);
        newApp.setCreatedDate(new Date());
        newApp.setIdentityId(identityId);
        newApp.setLoginRequired(loginVal);
        newApp.setStatus(statusVal);
        if(statusVal==IConstant.STATUS_ACTIVE){
            newApp.setStartDate(new Date());
        }
        newApp.setDeviceGroups(deviceGroup);
        em.persist(newApp);
        return newApp;
    }
    /**
     * Get an application by id
     * @param id
     * @return 
     */
    public Applications getApplicationById(Integer id) {
        return em.find(Applications.class, id);
    }

}

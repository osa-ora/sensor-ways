/**
 * SensorWays IoT Platform
 * =================================================
 * Copyright (c) 2019 : Osama Oransa
 * =================================================
 * This interface hold all static contents in the project
 */
package osa.ora.iot.beans;
/**
 * IConstant Interface
 * @author ooransa
 */
public interface IConstant {

    public static final int ACTION_RE_LOGIN = 0;
    public static final int ACTION_ON_DEVICE1 = 1;
    public static final int ACTION_OFF_DEVICE1 = 2;
    public static final int ACTION_PING = 3;
    public static final int ACTION_RESTART = 4;
    public static final int ACTION_UPDATE = 5;
    public static final int ACTION_ON_DEVICE2 = 6;
    public static final int ACTION_OFF_DEVICE2 = 7;
    public static final int ACTION_DELETE_DEVICE = 8;
    public static final int ACTION_RECIEVE_HIGH_ALERT = 10;
    public static final int ACTION_RECIEVE_NORMAL = 11;
    public static final int ACTION_RECIEVE_LOW_ALERT = 13;
    public static final int ACTION_RECIEVE_ERROR = 14;
    //to be updated...
    public static final int ACTION_GET_MESSAGE = 9;
    public static final int ACTION_NOT_ASSIGNED = 12;
    //15 is reserved ...
    public static final int ACTION_WEB_LIST_WORKFLOWS = 16;
    public static final int ACTION_WEB_REGISTER_WORKFLOW = 17;
    public static final int ACTION_WEB_EDIT_WORKFLOW = 18;
    public static final int ACTION_WEB_DELETE_WORKFLOW = 19;
    public static final int ACTION_WEB_ACTIVATE_WORKFLOW = 20;
    public static final int ACTION_WEB_INACTIVATE_WORKFLOW = 21;
    public static final int ACTION_WEB_LIST_SCHEDULERS = 22;
    public static final int ACTION_WEB_REGISTER_SCHEDULER = 23;
    public static final int ACTION_WEB_EDIT_SCHEDULER = 24;
    public static final int ACTION_WEB_DELETE_SCHEDULER = 25;
    public static final int ACTION_WEB_ACTIVATE_SCHEDULER = 26;
    public static final int ACTION_WEB_INACTIVATE_SCHEDULER = 27;
    public static final int ACTION_WEB_LIST_USERS = 28;
    public static final int ACTION_WEB_REGISTER_USER = 29;
    public static final int ACTION_WEB_EDIT_USER = 30;
    public static final int ACTION_WEB_DELETE_USER = 31;
    public static final int ACTION_WEB_ACTIVATE_USER = 32;
    public static final int ACTION_WEB_INACTIVATE_USER = 33;
    public static final int ACTION_WEB_MESSAGE_GRAPH = 34;
    public static final int ACTION_WEB_MESSAGE_GRAPH_REFRESH = 35;
    public static final int ACTION_WEB_SHOW_DASHBOARD = 36;
    public static final int ACTION_WEB_CREATE_DASHBOARD = 37;
    public static final int ACTION_WEB_SAVE_DASHBOARD = 38;
    public static final int ACTION_WEB_LIST_DEVICES = 39;
    public static final int ACTION_WEB_LIST_MESSAGES = 40;
    public static final int ACTION_WEB_LIST_DEVICE_MESSAGES = 41;
    public static final int ACTION_WEB_REGISTER_DEVICE = 42;
    public static final int ACTION_WEB_EDIT_DEVICE = 43;
    public static final int ACTION_WEB_DELETE_DEVICE = 44;
    public static final int ACTION_DEVICE_ONLINE = 45;
    public static final int ACTION_DEVICE_OFFLINE = 46;
    public static final int ACTION_WEB_MAKE_USER_READONLY = 47;
    public static final int ACTION_WEB_MAKE_USER_READWRITE = 48;
    public static final int ACTION_WEB_SAVE_NEW_DEVICE = 49;
    public static final int ACTION_USER_WEB_LOGIN=50;
    public static final int ACTION_WEB_LIST_NOTIFICATIONS = 51;
    public static final int ACTION_WEB_READ_NOTIFICATION = 52;
    public static final int ACTION_WEB_DELETE_NOTIFICATION = 53;
    public static final int ACTION_WEB_UNREAD_NOTIFICATION = 54;
    public static final int ACTION_WEB_CHANGE_DEVICE_CONTACT = 55;
    public static final int ACTION_WEB_DELETE_READ_NOTIFICATIONS = 56;
    public static final int ACTION_WEB_DELETE_ALL_NOTIFICATIONS = 57;
    public static final int ACTION_WEB_DELETE_ALL_DEVICE_MESSAGES = 58;
    public static final int ACTION_WEB_REGISTER_BARCODE = 59;
    public static final int ACTION_WEB_SAVE_BARCODE = 60;
    public static final int ACTION_WEB_LOAD_ACCOUNT_FOR_UPDATE = 61;
    public static final int ACTION_WEB_UPDATE_ACCOUNT = 62;
    public static final int ACTION_WEB_UPDATE_FIRMWARE = 63;
    public static final int ACTION_WEB_UPLOAD_FIRMWARE = 64;
    public static final int ACTION_WEB_SAVE_FIRMWARE = 65;
    public static final int ACTION_WEB_MANAGE_MODELS = 66;
    public static final int ACTION_WEB_CREATE_MODEL = 67;
    public static final int ACTION_WEB_SAVE_MODEL = 68;
    public static final int ACTION_WEB_INACTIVATE_MODEL = 69;
    public static final int ACTION_WEB_ACTIVATE_MODEL = 70;
    public static final int ACTION_WEB_LIST_AUDIT = 71;
    public static final int ACTION_WEB_LOAD_SYS_CONFIG = 72;
    public static final int ACTION_WEB_SAVE_SYS_CONFIG = 73;
    public static final int ACTION_WEB_CONFIG_REPORT = 74;
    public static final int ACTION_WEB_EXECUTE_REPORT = 75;
    public static final int ACTION_WEB_REGISTER_MODEL = 76;
    public static final int ACTION_WEB_ACTIVATE_DEVICE = 77;
    public static final int ACTION_WEB_INACTIVATE_DEVICE = 78;
    public static final int ACTION_WEB_LIST_SIMULATION = 79;
    public static final int ACTION_WEB_CREATE_SIMULATION = 80;
    public static final int ACTION_WEB_SAVE_SIMULATION = 81;
    public static final int ACTION_WEB_START_SIMULATION = 82;
    public static final int ACTION_WEB_STOP_SIMULATION = 83;
    public static final int ACTION_WEB_STOP_AND_DELETE_SIMULATION = 84;
    public static final int ACTION_WEB_LIST_SYS_JOBS = 85;
    public static final int ACTION_WEB_DISABLED_SYS_JOBS = 86;
    public static final int ACTION_WEB_ENABLED_SYS_JOBS = 87;
    public static final int ACTION_WEB_GET_LOGS_SYS_JOBS = 88;

    public static final int ACTION_WEB_LOGOUT_FROM_USER_ACCOUNT = 98;
    public static final int ACTION_WEB_LOGIN_TO_USER_ACCOUNT = 99;
    public static final int ACTION_WEB_LIST_DEVICE_GROUP = 100;
    public static final int ACTION_WEB_REGISTER_DEVICE_GROUP = 101;
    public static final int ACTION_WEB_EDIT_DEVICE_GROUP = 102;
    public static final int ACTION_WEB_DELETE_DEVICE_GROUP = 103;
    public static final int ACTION_WEB_ACTIVATE_DEVICE_GROUP = 104;
    public static final int ACTION_WEB_INACTIVATE_DEVICE_GROUP = 105;
    public static final int ACTION_WEB_RESTART_DEVICE_GROUP = 106;
    public static final int ACTION_WEB_GET_MESSAGE_DEVICE_GROUP = 107;
    public static final int ACTION_WEB_UPDATE_DEVICE_GROUP = 108;
    public static final int ACTION_WEB_SAVE_DEVICE_GROUP = 109;

    public static final int ACTION_WEB_LIST_APPLICATIONS = 110;
    public static final int ACTION_WEB_REGISTER_NEW_APP = 111;
    public static final int ACTION_WEB_START_NEW_APPLICATION = 112;
    public static final int ACTION_WEB_DELETE_APPLICATION = 113;
    public static final int ACTION_WEB_ACTIVATE_APPLICATION = 114;
    public static final int ACTION_WEB_INACTIVATE_APPLICATION = 115;
    public static final int ACTION_WEB_UPDATE_APPLICATION = 116;
    public static final int ACTION_WEB_SAVE_APPLICATION = 117;
    public static final int ACTION_WEB_LUNCH_APPLICATION = 118;

    
    public static final int ACTION_DEVICE_LOGIN_FROM_DIFFERENT_IP = 119;

    public static final int PURGE_JOB=1;
    public static final int SIMULATION_JOB=2;
    public static final int OFFLINE_JOB=3;
    public static final int SCHEDULER_JOB=4;
    public static final int MONTHLY_JOB=5;
    public static final int RECIEVE_JOB=6;
    

    public static final int MESSAGE_LOGIN = 0;
    public static final int MESSAGE_DATA = 1;
    public static final int MESSAGE_ALERT = 2;
    public static final int MESSAGE_WARNING = 3;
    public static final int MESSAGE_LOW_ALERT = 4;
    public static final int MESSAGE_LOW_WARNING = 5;
    public static final int MESSAGE_ERROR = 6;
    
    public static final int STATUS_PENDING_LOGIN = 0;
    //for both workflow and device 1 and 2
    public static final int STATUS_ACTIVE = 1;
    public static final int STATUS_INACTIVE = 2;
    public static final int STATUS_SIMULATION = 3;
    
    public static final int STATUS_ONLINE = 1;
    public static final int STATUS_OFFLINE = 0;
    
    public static final int ROLE_READ_WRITE = 1;
    public static final int ROLE_READ_ONLY = 2;

    public static final int ACTION_BY_NONE = 0; //not defined
    public static final int ACTION_BY_SYSTEM = 1;
    public static final int ACTION_BY_WEB = 2;
    public static final int ACTION_BY_MOBILE = 3;
    public static final int ACTION_BY_WORKFLOW = 4;
    public static final int ACTION_BY_SCHEDULER = 5;

    public static final int USER_STATUS_ACTIVE = 1;
    public static final int USER_STATUS_LOCKED = 3;

    public static final int EMAIL_ENABLED = 1;
    public static final int EMAIL_DISABLED = 0;
    
    public static final int ENGLISH=2;
    public static final int ARABIC=1;
    
    public static final int ONE_DEVICE_THRESHOLD = 10000;
    public static final int TWO_DEVICE_THRESHOLD = 20000;
    public static final int THREE_DEVICE_THRESHOLD = 30000;
}

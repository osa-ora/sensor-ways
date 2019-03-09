-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: localhost    Database: iot
-- ------------------------------------------------------
-- Server version	5.7.17-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `actions_lov`
--

LOCK TABLES `actions_lov` WRITE;
/*!40000 ALTER TABLE `actions_lov` DISABLE KEYS */;
INSERT INTO `actions_lov` VALUES (0,'ACTION_RE_LOGIN'),(1,'ACTION_ON_DEVICE1'),(2,'ACTION_OFF_DEVICE1'),(3,'ACTION_PING'),(4,'ACTION_RESTART'),(5,'ACTION_UPDATE'),(6,'ACTION_ON_DEVICE2'),(7,'ACTION_OFF_DEVICE2'),(8,'ACTION_DELETE_DEVICE'),(9,'ACTION_GET_MESSAGE'),(10,'ACTION_RECIEVE_HIGH_ALERT'),(11,'ACTION_RECIEVE_NORMAL'),(12,'ACTION_NOT_ASSIGNED'),(13,'ACTION_RECIEVE_LOW_ALERT'),(14,'ACTION_RECIEVE_ERROR'),(15,'RESERVED'),(16,'ACTION_WEB_LIST_WORKFLOWS'),(17,'ACTION_WEB_REGISTER_WORKFLOW'),(18,'ACTION_WEB_EDIT_WORKFLOW'),(19,'ACTION_WEB_DELETE_WORKFLOW'),(20,'ACTION_WEB_ACTIVATE_WORKFLOW'),(21,'ACTION_WEB_INACTIVATE_WORKFLOW'),(22,'ACTION_WEB_LIST_SCHEDULERS'),(23,'ACTION_WEB_REGISTER_SCHEDULER'),(24,'ACTION_WEB_EDIT_SCHEDULER'),(25,'ACTION_WEB_DELETE_SCHEDULER'),(26,'ACTION_WEB_ACTIVATE_SCHEDULER'),(27,'ACTION_WEB_INACTIVATE_SCHEDULER'),(28,'ACTION_WEB_LIST_USERS'),(29,'ACTION_WEB_REGISTER_USER'),(30,'ACTION_WEB_EDIT_USER'),(31,'ACTION_WEB_DELETE_USER'),(32,'ACTION_WEB_ACTIVATE_USER'),(33,'ACTION_WEB_INACTIVATE_USER'),(34,'ACTION_WEB_MESSAGE_GRAPH'),(35,'ACTION_WEB_MESSAGE_GRAPH_REFRESH'),(36,'ACTION_WEB_SHOW_DASHBOARD'),(37,'ACTION_WEB_CREATE_DASHBOARD'),(38,'ACTION_WEB_SAVE_DASHBOARD'),(39,'ACTION_WEB_LIST_DEVICES'),(40,'ACTION_WEB_LIST_MESSAGES'),(41,'ACTION_WEB_LIST_DEVICE_MESSAGES'),(42,'ACTION_WEB_REGISTER_DEVICE'),(43,'ACTION_WEB_EDIT_DEVICE'),(44,'ACTION_WEB_DELETE_DEVICE'),(45,'Device Online'),(46,'Device Offline'),(47,'ACTION_WEB_MAKE_USER_READONLY'),(48,'ACTION_WEB_MAKE_USER_READWRITE'),(49,'ACTION_WEB_SAVE_NEW_DEVICE'),(50,'ACTION_USER_WEB_LOGIN'),(51,'ACTION_WEB_LIST_NOTIFICATIONS'),(52,'ACTION_WEB_READ_NOTIFICATIONS'),(53,'ACTION_WEB_DELETE_NOTIFICATIONS'),(54,'ACTION_WEB_UNREAD_NOTIFICATIONS'),(55,'ACTION_WEB_CHANGE_DEVICE_CONTACT'),(56,'ACTION_WEB_DELETE_READ_NOTIFICATIONS'),(57,'ACTION_WEB_DELETE_ALL_NOTIFICATIONS'),(58,'ACTION_WEB_DELETE_ALL_DEVICE_MESSAGES'),(59,'ACTION_WEB_REGISTER_BARCODE'),(60,'ACTION_WEB_SAVE_BARCODE'),(61,'ACTION_WEB_LOAD_ACCOUNT_FOR_UPDATE'),(62,'ACTION_WEB_UPDATE_ACCOUNT'),(63,'ACTION_WEB_UPDATE_FIRMWARE'),(64,'ACTION_WEB_UPLOAD_FIRMWARE'),(65,'ACTION_WEB_SAVE_FIRMWARE'),(66,'ACTION_WEB_MANAGE_MODELS'),(67,'ACTION_WEB_CREATE_MODEL'),(68,'ACTION_WEB_SAVE_MODEL'),(69,'ACTION_WEB_INACTIVATE_MODEL'),(70,'ACTION_WEB_ACTIVATE_MODEL'),(71,'ACTION_WEB_LIST_AUDIT'),(72,'ACTION_WEB_LOAD_SYS_CONFIG'),(73,'ACTION_WEB_SAVE_SYS_CONFIG'),(74,'ACTION_WEB_CONFIG_REPORT'),(75,'ACTION_WEB_EXECUTE_REPORT'),(76,'ACTION_WEB_REGISTER_MODEL'),(77,'ACTION_WEB_ACTIVATE_DEVICE'),(78,'ACTION_WEB_INACTIVATE_DEVICE'),(79,'ACTION_WEB_LIST_SIMULATION'),(80,'ACTION_WEB_CREATE_SIMULATION'),(81,'ACTION_WEB_SAVE_SIMULATION'),(82,'ACTION_WEB_START_SIMULATION'),(83,'ACTION_WEB_STOP_SIMULATION'),(84,'ACTION_WEB_STOP_AND_DELETE_SIMULATION'),(85,'ACTION_WEB_LIST_SYS_JOBS'),(86,'ACTION_WEB_DISABLED_SYS_JOBS'),(87,'ACTION_WEB_ENABLED_SYS_JOBS'),(88,'ACTION_WEB_GET_LOGS_SYS_JOBS'),(98,'ACTION_WEB_LOGOUT_FROM_USER_ACCOUNT'),(99,'ACTION_WEB_LOGIN_TO_USER_ACCOUNT'),(100,'ACTION_WEB_LIST_DEVICE_GROUP'),(101,'ACTION_WEB_REGISTER_DEVICE_GROUP'),(102,'ACTION_WEB_EDIT_DEVICE_GROUP'),(103,'ACTION_WEB_DELETE_DEVICE_GROUP'),(104,'ACTION_WEB_ACTIVATE_DEVICE_GROUP'),(105,'ACTION_WEB_INACTIVATE_DEVICE_GROUP'),(106,'ACTION_WEB_RESTART_DEVICE_GROUP'),(107,'ACTION_WEB_GET_MESSAGE_DEVICE_GROUP'),(108,'ACTION_WEB_UPDATE_DEVICE_GROUP'),(109,'ACTION_WEB_SAVE_DEVICE_GROUP'),(110,'ACTION_WEB_LIST_APPLICATIONS'),(111,'ACTION_WEB_REGISTER_NEW_APP'),(112,'ACTION_WEB_START_NEW_APPLICATION'),(113,'ACTION_WEB_DELETE_APPLICATION'),(114,'ACTION_WEB_ACTIVATE_APPLICATION'),(115,'ACTION_WEB_INACTIVATE_APPLICATION'),(116,'ACTION_WEB_UPDATE_APPLICATION'),(117,'ACTION_WEB_SAVE_APPLICATION'),(118,'ACTION_WEB_LUNCH_APPLICATION'),(119,'ACTION_DEVICE_LOGIN_FROM_DIFFERENT_IP');
/*!40000 ALTER TABLE `actions_lov` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `email_templates`
--

LOCK TABLES `email_templates` WRITE;
/*!40000 ALTER TABLE `email_templates` DISABLE KEYS */;
INSERT INTO `email_templates` VALUES (10,'Smart IoT - Alert Notification','Smart IoT - تنبيه وارد من الجهاز','Hi <b>#1</b>,<br>This is a system generated notification for the following alert details:<br><br><hr>#2</red><br>Originated from the following device: Device identifier: #3 - Device Location: #4<br><hr><br>Best Regards,<br>Smart IoT','Hi <b>#1</b>,<br>This is a system generated notification for the following alert details:<br><br><hr>#2</red><br>Originated from the following device: Device identifier: #3 - Device Location: #4<br><hr><br>Best Regards,<br>Smart IoT'),(14,'Smart IoT - Device Fault Notification','Smart IoT - عطب في قراءة الجهاز','Hi <b>#1</b>,<br>This is a system generated notification for the following recieved error reading:<br><br><hr>#2</red><br>Originated from the following device: Device identifier: #3 - Device Location: #4<br><hr><br>Best Regards,<br>Smart IoT','Hi <b>#1</b>,<br>This is a system generated notification for the following recieved error reading:<br><br><hr>#2</red><br>Originated from the following device: Device identifier: #3 - Device Location: #4<br><hr><br>Best Regards,<br>Smart IoT'),(29,'Smart IoT - User Registration Confirmation','Smart IoT - تأكيد تسجيل المستخدم','Hi <b>#1</b>,<br>This is a system generated notification to confirm the registration of your user into our Platform:<br>Use your email and the following temporary password to login to the system: #2<br>Your Account Identifier: #3<br>You Can Login to the System from the following URL: #4<br>Don\'t forget to change your password once logged into the Smart IoT Platform.<br><hr><br>Best Regards,<br>Smart IoT','Hi <b>#1</b>,<br>This is a system generated notification to confirm the registration of your user into our Platform:<br>Use your email and the following temporary password to login to the system: #2<br>Your Account Identifier: #3<br>You Can Login to the System from the following URL: #4<br>Don\'t forget to change your password once logged into the Smart IoT Platform.<br><hr><br>Best Regards,<br>Smart IoT'),(42,'Smart IoT - Device Registration Confirmation','Smart IoT - تأكيد تسجيل الجهاز','Hi <b>#1</b>,<br>This is a system generated notification to confirm the registration of your new device:<br>Device identifier: #2 - Device Location: #3<br><hr><br>Best Regards,<br>Smart IoT','Hi <b>#1</b>,<br>This is a system generated notification to confirm the registration of your new device:<br>Device identifier: #2 - Device Location: #3<br><hr><br>Best Regards,<br>Smart IoT'),(43,'Smart IoT - Device Update Confirmation','Smart IoT - تأكيد تحديث بيانات الجهاز','Hi <b>#1</b>,<br>This is a system generated notification to confirm the Update of your device details:<br>Device identifier: #2 - Device Location: #3<br><hr><br>Best Regards,<br>Smart IoT','Hi <b>#1</b>,<br>This is a system generated notification to confirm the Update of your device details:<br>Device identifier: #2 - Device Location: #3<br><hr><br>Best Regards,<br>Smart IoT'),(45,'Smart IoT - Device Online Notification','Smart IoT - اشعار باستمرار عمل الجهاز','Hi <b>#1</b>,<br>This is a system generated notification to notify you that following device is now online.<br>Device identifier: #2 - Device Location: #3<br><hr><br>Best Regards,<br>Smart IoT','Hi <b>#1</b>,<br>This is a system generated notification to notify you that following device is now online.<br>Device identifier: #2 - Device Location: #3<br><hr><br>Best Regards,<br>Smart IoT'),(46,'Smart IoT - Device Offline Notification','Smart IoT - اشعار بتوقف الجهاز','Hi <b>#1</b>,<br>This is a system generated notification to notify you that following device is now offline.<br>Device identifier: #2 - Device Location: #3<br><hr><br>Best Regards,<br>Smart IoT','Hi <b>#1</b>,<br>This is a system generated notification to notify you that following device is now offline.<br>Device identifier: #2 - Device Location: #3<br><hr><br>Best Regards,<br>Smart IoT'),(55,'Smart IoT - Device Update Confirmation','Smart IoT - تأكيد تحديث بيانات الجهاز','Hi <b>#1</b>,<br>This is a system generated notification to confirm the Update of your device details:<br>Device identifier: #2 - Device Location: #3<br>\r The device has a new notification email: #4<br><hr><br>Best Regards,<br>Smart IoT','Hi <b>#1</b>,<br>This is a system generated notification to confirm the Update of your device details:<br>Device identifier: #2 - Device Location: #3<br>\r The device has a new notification email: #4<br><hr><br>Best Regards,<br>Smart IoT'),(62,'Smart IoT - Account Update Confirmation','Smart IoT - تأكيد تحديث بيانات الحساب','Hi <b>#1</b>,<br>This is a system generated notification to confirm the Update of your account details:<br>Account identifier: #2 - Account Administrator: #3<br><hr><br>Best Regards,<br>Smart IoT','Hi <b>#1</b>,<br>This is a system generated notification to confirm the Update of your account details:<br>Account identifier: #2 - Account Administrator: #3<br><hr><br>Best Regards,<br>Smart IoT'),(65,'Smart IoT - Device New Firmware Notification','Smart IoT - اشعار بتوفر تحديث للجهاز','Hi <b>#1</b>,<br>This is a system generated notification to notify you that this device model has a new firmware version available, the device will pick this automatically upon the next login/restart, device details:<br>Device identifier: #2 - Device Location: #3 - Device Model New Version: #4<br><br><hr><br>Best Regards,<br>Smart IoT','Hi <b>#1</b>,<br>This is a system generated notification to notify you that this device model has a new firmware version available, the device will pick this automatically upon the next login/restart, device details:<br>Device identifier: #2 - Device Location: #3 - Device Model New Version: #4<br><br><hr><br>Best Regards,<br>Smart IoT'),(119,'Smart IoT - Device IP Change Notification','Smart IoT - اشعار بتغير موقع الجهاز','Hi <b>#1</b>,<br>This is a system generated notification to notify you that your device has logged-in from a new IP Address: <b>#2</b><br>Device details:<br>Device identifier: #3 - Device Location: #4 - Old Device IP Address: #5<br><br><hr><br>Best Regards,<br>Smart IoT','Hi <b>#1</b>,<br>This is a system generated notification to notify you that your device has logged-in from a new IP Address: <b>#2</b><br>Device details:<br>Device identifier: #3 - Device Location: #4 - Old Device IP Address: #5<br><br><hr><br>Best Regards,<br>Smart IoT');
/*!40000 ALTER TABLE `email_templates` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `hw_type`
--

LOCK TABLES `hw_type` WRITE;
/*!40000 ALTER TABLE `hw_type` DISABLE KEYS */;
INSERT INTO `hw_type` VALUES (1,'Arduino D1','D1_VERSION_'),(2,'Arduino D1 Mini','D1_MINI_VERSION_');
/*!40000 ALTER TABLE `hw_type` ENABLE KEYS */;
UNLOCK TABLES;


--
-- Dumping data for table `message_types`
--

LOCK TABLES `message_types` WRITE;
/*!40000 ALTER TABLE `message_types` DISABLE KEYS */;
INSERT INTO `message_types` VALUES (0,'Device Login'),(1,'Update Message'),(2,'High Alert'),(3,'High Warning'),(4,'Low Alert'),(5,'Low Warning'),(6,'Error Message'),(7,'Upgrade Firmware');
/*!40000 ALTER TABLE `message_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `system_jobs`
--

LOCK TABLES `system_jobs` WRITE;
/*!40000 ALTER TABLE `system_jobs` DISABLE KEYS */;
INSERT INTO `system_jobs` VALUES (1,'Purge Job','2019-02-03 08:38:37',6,NULL,1,1,15,'Days'),(2,'Simulation Job','2019-02-03 13:29:30',0,NULL,1,1,NULL,NULL),(3,'Device Offline Detection Job','2019-02-03 13:29:00',0,NULL,1,1,60,'Seconds'),(4,'Scheduler Execution Job','2019-02-03 13:15:00',0,NULL,1,1,15,'Every x min past the hour'),(5,'Monthly Traffic Job','2019-02-02 14:56:38',14,NULL,1,1,NULL,NULL),(6,'Recieve New Device Messages Job','2019-02-03 13:29:30',0,NULL,1,1,NULL,NULL);
/*!40000 ALTER TABLE `system_jobs` ENABLE KEYS */;
UNLOCK TABLES;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-03-09 23:21:46

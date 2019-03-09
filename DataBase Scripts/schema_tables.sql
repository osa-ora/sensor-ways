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
-- Table structure for table `actions_lov`
--

DROP TABLE IF EXISTS `actions_lov`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `actions_lov` (
  `action_id` int(11) NOT NULL,
  `actions_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`action_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `applications`
--

DROP TABLE IF EXISTS `applications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `applications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(150) DEFAULT NULL,
  `identity_id` int(11) DEFAULT NULL,
  `banner` mediumblob,
  `banner_name` varchar(60) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `login_required` int(11) DEFAULT NULL,
  `application_type` int(11) DEFAULT NULL,
  `contact_email` varchar(120) DEFAULT NULL,
  `contact_mobile` varchar(45) DEFAULT NULL,
  `alert_enabled` int(11) DEFAULT NULL,
  `start_date` datetime DEFAULT NULL,
  `created_date` datetime DEFAULT NULL,
  `total_messages` int(11) DEFAULT NULL,
  `total_alerts` int(11) DEFAULT NULL,
  `total_devices` int(11) DEFAULT NULL,
  `project_scope` varchar(300) DEFAULT NULL,
  `device_groups` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `audit_trail`
--

DROP TABLE IF EXISTS `audit_trail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_trail` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `action_id` int(11) DEFAULT NULL,
  `by_user_id` int(11) DEFAULT NULL,
  `performed_at` datetime DEFAULT NULL,
  `parameters` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=821 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bar_codes`
--

DROP TABLE IF EXISTS `bar_codes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `bar_codes` (
  `bar_code` varchar(30) NOT NULL,
  `device_model` int(11) DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL,
  PRIMARY KEY (`bar_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `device_group`
--

DROP TABLE IF EXISTS `device_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `device_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(60) DEFAULT NULL,
  `identity_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `device_model`
--

DROP TABLE IF EXISTS `device_model`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `device_model` (
  `id` int(11) NOT NULL,
  `device_name` varchar(90) DEFAULT NULL,
  `no_of_devices` int(11) DEFAULT NULL,
  `no_of_sensors` int(11) DEFAULT '1',
  `custom_action_name` varchar(40) DEFAULT NULL,
  `custom_action_value` int(11) DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `firmware_version` int(11) DEFAULT '1',
  `firmware_code` mediumblob,
  `firmware_source` mediumblob,
  `model_status` int(11) DEFAULT '1',
  `high_alert_value_1` varchar(10) DEFAULT NULL,
  `low_alert_value_1` varchar(10) DEFAULT NULL,
  `high_alert_value_2` varchar(10) DEFAULT NULL,
  `low_alert_value_2` varchar(10) DEFAULT NULL,
  `hw_type` int(11) DEFAULT '1',
  `sensor1_icon` int(11) DEFAULT NULL,
  `sensor2_icon` int(11) DEFAULT NULL,
  `simulation_normal_msg` varchar(50) DEFAULT NULL,
  `simulation_alert_msg` varchar(50) DEFAULT NULL,
  `simulation_error_msg` varchar(50) DEFAULT NULL,
  `sensor1_name` varchar(45) DEFAULT NULL,
  `sensor2_name` varchar(45) DEFAULT NULL,
  `sensor1_graph` int(11) DEFAULT NULL,
  `sensor2_graph` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `devices`
--

DROP TABLE IF EXISTS `devices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `devices` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device_id` varchar(30) DEFAULT NULL,
  `group_id` int(11) DEFAULT NULL,
  `password` varchar(60) DEFAULT NULL,
  `friendly_name` varchar(60) DEFAULT NULL,
  `secret_key` varchar(60) DEFAULT NULL,
  `location` varchar(60) DEFAULT NULL,
  `tags` varchar(90) DEFAULT NULL,
  `device1` varchar(40) DEFAULT NULL,
  `device2` varchar(40) DEFAULT NULL,
  `custom_name` varchar(40) DEFAULT NULL,
  `custom_value` varchar(40) DEFAULT NULL,
  `notification_email` varchar(300) DEFAULT NULL,
  `notification_mobile` varchar(20) DEFAULT NULL,
  `bar_code` varchar(20) DEFAULT NULL,
  `last_action` int(11) DEFAULT NULL,
  `last_action_by` int(11) DEFAULT NULL,
  `device_model` int(11) DEFAULT NULL,
  `device_status` int(11) DEFAULT NULL,
  `registration_date` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `last_ping_time` datetime DEFAULT NULL,
  `identity` int(11) DEFAULT NULL,
  `firmware_version` int(11) DEFAULT '1',
  `high_alert_value_1` varchar(10) DEFAULT NULL,
  `low_alert_value_1` varchar(10) DEFAULT NULL,
  `high_alert_value_2` varchar(10) DEFAULT NULL,
  `low_alert_value_2` varchar(10) DEFAULT NULL,
  `total_inbound` int(11) DEFAULT '0',
  `total_outbound` int(11) DEFAULT '0',
  `smart_rules1` int(11) DEFAULT '0',
  `smart_rules2` int(11) DEFAULT '0',
  `device1_status` int(11) DEFAULT '0',
  `device2_status` int(11) DEFAULT '0',
  `total_messages` int(11) DEFAULT '0',
  `total_alerts` int(11) DEFAULT '0',
  `total_inbound_month` int(11) DEFAULT '0',
  `total_outbound_month` int(11) DEFAULT '0',
  `total_messages_month` int(11) DEFAULT '0',
  `device_management` int(11) DEFAULT '0',
  `failed_login` int(11) DEFAULT '0',
  `ip_address` varchar(45) DEFAULT NULL,
  `offline_flag` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `email_templates`
--

DROP TABLE IF EXISTS `email_templates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `email_templates` (
  `action_id` int(11) NOT NULL,
  `title_en` tinytext,
  `title_ar` tinytext,
  `body_en` text,
  `body_ar` text,
  PRIMARY KEY (`action_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `hw_type`
--

DROP TABLE IF EXISTS `hw_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `hw_type` (
  `id` int(11) NOT NULL,
  `type` varchar(80) DEFAULT NULL,
  `value` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `identity_notifications`
--

DROP TABLE IF EXISTS `identity_notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `identity_notifications` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `identity_id` int(11) NOT NULL,
  `template_id` int(11) DEFAULT NULL,
  `params` varchar(140) DEFAULT NULL,
  `read_flag` tinyint(4) DEFAULT NULL,
  `notified_on` datetime DEFAULT NULL,
  `unique_identiter` varchar(30) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=326 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `message_types`
--

DROP TABLE IF EXISTS `message_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `message_types` (
  `id` int(11) NOT NULL,
  `type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device_id` varchar(30) DEFAULT NULL,
  `payload` varchar(120) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `message_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3953 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `recieved_messages`
--

DROP TABLE IF EXISTS `recieved_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `recieved_messages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `device_id` varchar(30) DEFAULT NULL,
  `payload` varchar(120) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `message_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `scheduler`
--

DROP TABLE IF EXISTS `scheduler`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `scheduler` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `scheduler_name` varchar(30) DEFAULT NULL,
  `triggering_day` int(11) DEFAULT NULL,
  `triggering_hour` int(11) DEFAULT NULL,
  `triggering_min` int(11) DEFAULT NULL,
  `target_device_id` varchar(30) DEFAULT NULL,
  `target_action` int(11) DEFAULT NULL,
  `scheduler_status` int(11) DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `identity` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `simulations`
--

DROP TABLE IF EXISTS `simulations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `simulations` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) DEFAULT NULL,
  `device_id` varchar(30) DEFAULT NULL,
  `simulation_status` int(11) DEFAULT NULL,
  `alert_probability` float DEFAULT NULL,
  `error_probanility` float DEFAULT NULL,
  `original_device_status` int(11) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `start_time` datetime DEFAULT NULL,
  `identity_id` int(11) DEFAULT NULL,
  `device1_status` int(11) DEFAULT '0',
  `device2_status` int(11) DEFAULT '0',
  `loop_counter` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `system_config`
--

DROP TABLE IF EXISTS `system_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system_config` (
  `version_id` int(11) NOT NULL,
  `api_version` varchar(10) DEFAULT NULL,
  `android_version` varchar(10) DEFAULT NULL,
  `email_enabled` int(11) DEFAULT NULL,
  `smtp_server_ip` varchar(45) DEFAULT NULL,
  `smtp_port` varchar(10) DEFAULT NULL,
  `useSSL` int(11) DEFAULT NULL,
  `email_user` varchar(60) DEFAULT NULL,
  `email_password` varchar(60) DEFAULT NULL,
  `default_email_language` int(11) DEFAULT NULL,
  `updated_date` datetime DEFAULT NULL,
  `ios_version` varchar(10) DEFAULT NULL,
  `platform_host` varchar(45) DEFAULT NULL,
  `platform_https_port` int(10) DEFAULT NULL,
  `platform_http_port` int(10) DEFAULT NULL,
  `default_timezone` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`version_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `system_jobs`
--

DROP TABLE IF EXISTS `system_jobs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `system_jobs` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(120) DEFAULT NULL,
  `last_execution` datetime DEFAULT NULL,
  `last_execution_rows` int(11) DEFAULT NULL,
  `execution_log` text,
  `status` int(11) DEFAULT NULL,
  `last_execution_results` int(11) DEFAULT NULL,
  `param1_value` int(11) DEFAULT NULL,
  `param1_type` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `tenant_settings`
--

DROP TABLE IF EXISTS `tenant_settings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tenant_settings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `identity` int(11) DEFAULT NULL,
  `identity_name` varchar(100) DEFAULT NULL,
  `tenant_status` int(11) DEFAULT '1',
  `max_retained_messages` int(11) DEFAULT NULL,
  `ping_interval` int(11) DEFAULT NULL,
  `update_interval` int(11) DEFAULT NULL,
  `timezone` varchar(20) DEFAULT NULL,
  `alert_email_message` int(11) DEFAULT '0',
  `update_time` datetime DEFAULT NULL,
  `max_users` int(11) DEFAULT '3',
  `max_devices` int(11) DEFAULT '5',
  `alert_email_language` int(11) DEFAULT '2',
  `alert_email_online` int(11) DEFAULT '0',
  `alert_email_offline` int(11) DEFAULT '0',
  `device_registration_email` int(11) DEFAULT '0',
  `device_update_email` int(11) DEFAULT '0',
  `alert_grace_period` int(11) DEFAULT '5',
  `sms_qouta` int(11) DEFAULT '0',
  `sms_consumed` int(11) DEFAULT '0',
  `fault_notification_email` int(11) DEFAULT '0',
  `admin_user_email` varchar(40) NOT NULL,
  `purge_after` int(11) DEFAULT '15',
  `alert_device_ip_change` int(11) DEFAULT '0',
  `alert_firmware_available` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(30) DEFAULT NULL,
  `password` varchar(60) DEFAULT NULL,
  `email_address` varchar(40) DEFAULT NULL,
  `identity_id` int(11) DEFAULT NULL,
  `user_role` int(11) DEFAULT NULL,
  `invalid_login_trials` int(11) DEFAULT NULL,
  `last_login_time` datetime DEFAULT NULL,
  `user_status` int(11) DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `language` int(11) DEFAULT NULL,
  `dashboard` varchar(100) DEFAULT NULL,
  `user_creation` int(11) DEFAULT '0',
  `workflow_creation` int(11) DEFAULT '0',
  `scheduler_creation` int(11) DEFAULT '0',
  `dashboard_creation` int(11) DEFAULT '0',
  `simulation_creation` int(11) DEFAULT '0',
  `identity_admin` int(11) DEFAULT '0',
  `system_admin` int(11) DEFAULT '0',
  `gui_language` int(11) DEFAULT '2',
  `ip_address` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `workflows`
--

DROP TABLE IF EXISTS `workflows`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workflows` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `workflow_name` varchar(30) DEFAULT NULL,
  `triggering_id` varchar(30) DEFAULT NULL,
  `trigger_type` int(11) DEFAULT '1',
  `trigger_event` int(11) DEFAULT NULL,
  `target_device_id` varchar(30) DEFAULT NULL,
  `target_action` int(11) DEFAULT NULL,
  `workflow_status` int(11) DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `identity` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-03-09 23:20:09

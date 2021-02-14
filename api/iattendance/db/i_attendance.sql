-- phpMyAdmin SQL Dump
-- version 5.0.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Nov 07, 2020 at 11:37 AM
-- Server version: 10.4.14-MariaDB
-- PHP Version: 7.2.33

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `i_attendance`
--

-- --------------------------------------------------------

--
-- Table structure for table `attendance`
--

CREATE TABLE `attendance` (
  `id` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `date` date NOT NULL,
  `in_time` time DEFAULT NULL,
  `out_time` time DEFAULT NULL,
  `in_loc` varchar(255) DEFAULT NULL,
  `out_loc` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '1=in ok\r\n2=out ok'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `attendance`
--

INSERT INTO `attendance` (`id`, `uid`, `date`, `in_time`, `out_time`, `in_loc`, `out_loc`, `status`) VALUES
(1, 1, '2020-10-04', '11:11:40', '17:35:53', 'Jobbar tower', 'Jobbar tower', 2),
(4, 1, '2020-10-05', '11:45:39', '12:56:50', 'Jobbar tower', 'Jobbar tower', 2),
(6, 1, '2020-10-06', '14:46:50', '19:21:19', 'Jobbar tower', 'Jobbar tower', 2),
(7, 1, '2020-10-07', '15:35:46', '19:26:29', 'Jobbar tower', 'Jobbar tower', 2),
(8, 1, '2020-10-10', '17:38:18', NULL, 'Jobbar tower', NULL, 1),
(9, 1, '2020-10-17', '12:02:58', NULL, 'Jobbar tower', NULL, 1),
(10, 1, '2020-10-18', '11:15:04', '18:02:25', 'Jobbar tower', 'Jobbar tower', 2),
(11, 1, '2020-10-19', '18:59:39', NULL, 'Jobbar tower', NULL, 1),
(12, 1, '2020-10-20', '13:26:59', NULL, 'Jobbar tower', NULL, 1),
(13, 1, '2020-10-21', '12:34:06', '13:37:46', 'Jobbar tower', 'Jobbar tower', 2),
(14, 1, '2020-10-24', '13:09:27', '16:00:06', 'Jobbar tower', 'Jobbar tower', 2),
(15, 1, '2020-10-25', '13:40:21', NULL, 'Jobbar tower', NULL, 1),
(16, 1, '2020-10-27', '14:07:52', '17:39:38', '44 Gulshan Badda Link Rd, Dhaka 1212, Bangladesh', '44 Gulshan Badda Link Rd, Dhaka 1212, Bangladesh', 2),
(17, 1, '2020-10-28', '12:11:41', '18:00:46', 'Gulshan Avenue, 42 Bir Uttam Mir Shawkat Sarak, Dhaka 1212, Bangladesh', 'Bir Uttam Mir Shawkat Sarak, Dhaka 1208, Bangladesh', 2),
(18, 1, '2020-10-31', '15:34:04', '16:48:49', '42 Jabbar Tower (3rd floor, Gulshan Ave, Dhaka 1212, Bangladesh', '42 Jabbar Tower (3rd floor, Gulshan Ave, Dhaka 1212, Bangladesh', 2);

-- --------------------------------------------------------

--
-- Table structure for table `department`
--

CREATE TABLE `department` (
  `id` int(11) NOT NULL,
  `name` varchar(155) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `department`
--

INSERT INTO `department` (`id`, `name`) VALUES
(1, 'Software'),
(4, 'Accounts');

-- --------------------------------------------------------

--
-- Table structure for table `designation`
--

CREATE TABLE `designation` (
  `id` int(11) NOT NULL,
  `dpt_id` int(11) NOT NULL,
  `name` varchar(155) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `designation`
--

INSERT INTO `designation` (`id`, `dpt_id`, `name`) VALUES
(1, 1, 'sr dev'),
(2, 1, 'jr dev');

-- --------------------------------------------------------

--
-- Table structure for table `holiday`
--

CREATE TABLE `holiday` (
  `id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `holiday`
--

INSERT INTO `holiday` (`id`, `name`, `date`) VALUES
(1, 'The Independence Day', '2020-03-26'),
(33, 'tty', '2020-11-01');

-- --------------------------------------------------------

--
-- Table structure for table `images`
--

CREATE TABLE `images` (
  `id` int(11) NOT NULL,
  `description` varchar(1000) NOT NULL,
  `cv_url` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `images`
--

INSERT INTO `images` (`id`, `description`, `cv_url`) VALUES
(6, 'dfgdfgdfgdgf', 'E:\\xampp\\htdocs\\android\\iattendance\\api\\src\\v1\\include/../uploads\\1603112268.pdf'),
(7, 'dfgdfgdfgdgf', 'E:\\xampp\\htdocs\\android\\iattendance\\api\\src\\v1\\include/../uploads\\1603114386.pdf'),
(8, 'dfgdfgdfgdgf', 'E:\\xampp\\htdocs\\android\\iattendance\\api\\src\\v1\\include/../uploads\\1603116339.jpg'),
(9, '', 'E:\\xampp\\htdocs\\android\\iattendance\\api\\src\\v1\\include/../uploads\\1603174233.'),
(10, '', 'E:\\xampp\\htdocs\\android\\iattendance\\api\\src\\v1\\include/../uploads\\1603174265.'),
(11, '', 'E:\\xampp\\htdocs\\android\\iattendance\\api\\src\\v1\\include/../uploads\\1603174570.'),
(12, '', 'E:\\xampp\\htdocs\\android\\iattendance\\api\\src\\v1\\include/../uploads\\1603174656.');

-- --------------------------------------------------------

--
-- Table structure for table `leave_history`
--

CREATE TABLE `leave_history` (
  `id` int(11) NOT NULL,
  `uid` int(11) NOT NULL,
  `apply_date` date NOT NULL,
  `purpose` varchar(255) NOT NULL,
  `from_date` date NOT NULL,
  `to_date` date NOT NULL,
  `type_id` int(11) DEFAULT NULL,
  `max_type_no` int(11) NOT NULL COMMENT 'max no of typed_id',
  `status` enum('0','1','2') NOT NULL COMMENT 'pending = 0\r\napproved = 1\r\ndecline = 2',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `leave_history`
--

INSERT INTO `leave_history` (`id`, `uid`, `apply_date`, `purpose`, `from_date`, `to_date`, `type_id`, `max_type_no`, `status`, `created_at`) VALUES
(39, 1, '2020-05-10', 'testing', '2020-05-22', '2020-05-25', 1, 1, '1', '2020-09-14 07:05:14'),
(49, 1, '2020-09-01', 'hhhh', '2020-09-01', '2020-09-02', 2, 12, '2', '2020-09-30 05:54:13'),
(50, 1, '2020-09-03', 'gggg', '2020-09-01', '2020-09-02', NULL, 0, '1', '2020-09-30 05:54:13'),
(51, 5, '2020-10-04', 'test', '2020-10-04', '2020-10-07', 1, 0, '1', '2020-10-04 07:21:42'),
(52, 1, '2020-10-04', 'tezttyuiott', '2020-10-04', '2020-10-04', 3, 1, '1', '2020-10-04 08:55:43'),
(53, 2, '2020-10-04', 'yyy', '2020-10-28', '2020-10-29', NULL, 1, '1', '2020-10-04 09:54:44');

-- --------------------------------------------------------

--
-- Table structure for table `leave_type`
--

CREATE TABLE `leave_type` (
  `type_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `balance` varchar(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `leave_type`
--

INSERT INTO `leave_type` (`type_id`, `name`, `balance`) VALUES
(1, 'Sick leave', '15'),
(2, 'Earn', '16'),
(3, 'Casual', '10');

-- --------------------------------------------------------

--
-- Table structure for table `office_time`
--

CREATE TABLE `office_time` (
  `id` int(11) NOT NULL,
  `day_name` varchar(20) NOT NULL,
  `starting_time` time NOT NULL,
  `ending_time` time NOT NULL,
  `status` enum('1','2') NOT NULL COMMENT '2=offday,\r\n1=onday'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `office_time`
--

INSERT INTO `office_time` (`id`, `day_name`, `starting_time`, `ending_time`, `status`) VALUES
(1, 'Sunday', '10:00:00', '18:00:00', '1'),
(2, 'Monday', '10:00:00', '18:00:00', '1'),
(3, 'Tuesday', '10:00:00', '18:00:00', '1'),
(4, 'Wednesday', '10:00:00', '18:00:00', '1'),
(5, 'Thursday', '10:00:00', '18:00:00', '1'),
(6, 'Friday', '10:00:00', '18:00:00', '2'),
(7, 'Saturday', '10:00:00', '18:00:00', '2');

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `id` int(11) NOT NULL,
  `name` varchar(100) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `dg_id` int(11) DEFAULT NULL,
  `dpt_id` int(11) DEFAULT NULL,
  `joining_date` date DEFAULT NULL,
  `role_id` int(11) DEFAULT 2,
  `api_key` varchar(255) DEFAULT NULL,
  `cv_url` varchar(255) DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(55) DEFAULT NULL,
  `blood_group` varchar(5) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`id`, `name`, `password`, `dg_id`, `dpt_id`, `joining_date`, `role_id`, `api_key`, `cv_url`, `phone`, `email`, `blood_group`) VALUES
(1, 'superadmin', '123456', 1, NULL, NULL, 1, 'acb46207cb2e4d72eb12b76a0d9b7579', NULL, NULL, NULL, NULL),
(25, 'general', '123456', 1, 1, '2020-10-28', 2, 'b9e84e3b56d0d105c220a09ee4a01a63', '1603869282.pdf', '88454616333', 'su@g.m', 'B+');

-- --------------------------------------------------------

--
-- Table structure for table `user_info`
--

CREATE TABLE `user_info` (
  `id` int(11) NOT NULL,
  `uid` int(11) DEFAULT NULL,
  `p_photo` varchar(255) DEFAULT NULL,
  `f_name` varchar(55) DEFAULT NULL,
  `m_name` varchar(55) DEFAULT NULL,
  `dob` varchar(55) DEFAULT NULL,
  `religion` varchar(55) DEFAULT NULL,
  `gender` varchar(55) DEFAULT NULL,
  `marital_status` varchar(55) DEFAULT NULL,
  `nationality` varchar(55) DEFAULT NULL,
  `nid_no` varchar(55) DEFAULT NULL,
  `c_address` varchar(255) DEFAULT NULL,
  `p_address` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user_info`
--

INSERT INTO `user_info` (`id`, `uid`, `p_photo`, `f_name`, `m_name`, `dob`, `religion`, `gender`, `marital_status`, `nationality`, `nid_no`, `c_address`, `p_address`) VALUES
(13, 1, '1604141177.png', 'father name', 'father name', '2020-10-28', 'Buddha', NULL, NULL, NULL, NULL, 'shshdhdhehsg', 'shshdhdhehsg');

-- --------------------------------------------------------

--
-- Table structure for table `user_role`
--

CREATE TABLE `user_role` (
  `id` int(11) NOT NULL,
  `role_name` varchar(100) NOT NULL,
  `access_code` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user_role`
--

INSERT INTO `user_role` (`id`, `role_name`, `access_code`, `created_at`) VALUES
(1, 'Super admin', 111111, '2020-09-05 10:11:47'),
(2, 'General', 999999, '2020-09-05 10:11:47'),
(3, 'testedit', 23, '2020-09-05 10:48:53'),
(4, 'wwwwwww', 2, '2020-09-05 10:49:32'),
(6, 'test role', 12, '2020-10-01 10:25:47'),
(7, 'ttt', 23, '2020-10-01 10:35:14');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `attendance`
--
ALTER TABLE `attendance`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `department`
--
ALTER TABLE `department`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `designation`
--
ALTER TABLE `designation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `dpt_id` (`dpt_id`);

--
-- Indexes for table `holiday`
--
ALTER TABLE `holiday`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `images`
--
ALTER TABLE `images`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `leave_history`
--
ALTER TABLE `leave_history`
  ADD PRIMARY KEY (`id`),
  ADD KEY `type_id` (`type_id`);

--
-- Indexes for table `leave_type`
--
ALTER TABLE `leave_type`
  ADD PRIMARY KEY (`type_id`);

--
-- Indexes for table `office_time`
--
ALTER TABLE `office_time`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_ibfk_2` (`dpt_id`),
  ADD KEY `user_ibfk_3` (`dg_id`),
  ADD KEY `role_id` (`role_id`);

--
-- Indexes for table `user_info`
--
ALTER TABLE `user_info`
  ADD PRIMARY KEY (`id`),
  ADD KEY `user_info_ibfk_1` (`uid`);

--
-- Indexes for table `user_role`
--
ALTER TABLE `user_role`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `attendance`
--
ALTER TABLE `attendance`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `department`
--
ALTER TABLE `department`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

--
-- AUTO_INCREMENT for table `designation`
--
ALTER TABLE `designation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT for table `holiday`
--
ALTER TABLE `holiday`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=35;

--
-- AUTO_INCREMENT for table `images`
--
ALTER TABLE `images`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `leave_history`
--
ALTER TABLE `leave_history`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=54;

--
-- AUTO_INCREMENT for table `leave_type`
--
ALTER TABLE `leave_type`
  MODIFY `type_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;

--
-- AUTO_INCREMENT for table `office_time`
--
ALTER TABLE `office_time`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- AUTO_INCREMENT for table `user`
--
ALTER TABLE `user`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=26;

--
-- AUTO_INCREMENT for table `user_info`
--
ALTER TABLE `user_info`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;

--
-- AUTO_INCREMENT for table `user_role`
--
ALTER TABLE `user_role`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `designation`
--
ALTER TABLE `designation`
  ADD CONSTRAINT `designation_ibfk_1` FOREIGN KEY (`dpt_id`) REFERENCES `department` (`id`);

--
-- Constraints for table `leave_history`
--
ALTER TABLE `leave_history`
  ADD CONSTRAINT `leave_history_ibfk_1` FOREIGN KEY (`type_id`) REFERENCES `leave_type` (`type_id`) ON DELETE SET NULL ON UPDATE NO ACTION;

--
-- Constraints for table `user`
--
ALTER TABLE `user`
  ADD CONSTRAINT `user_ibfk_2` FOREIGN KEY (`dpt_id`) REFERENCES `department` (`id`) ON DELETE SET NULL ON UPDATE NO ACTION,
  ADD CONSTRAINT `user_ibfk_3` FOREIGN KEY (`dg_id`) REFERENCES `designation` (`id`) ON DELETE SET NULL ON UPDATE NO ACTION,
  ADD CONSTRAINT `user_ibfk_4` FOREIGN KEY (`role_id`) REFERENCES `user_role` (`id`) ON DELETE SET NULL;

--
-- Constraints for table `user_info`
--
ALTER TABLE `user_info`
  ADD CONSTRAINT `user_info_ibfk_1` FOREIGN KEY (`uid`) REFERENCES `user` (`id`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

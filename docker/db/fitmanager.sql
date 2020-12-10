-- phpMyAdmin SQL Dump
-- version 4.6.6deb5
-- https://www.phpmyadmin.net/
--
-- Host: localhost:3306
-- Generation Time: Mar 24, 2020 at 10:31 AM
-- Server version: 5.7.29-0ubuntu0.18.04.1
-- PHP Version: 7.2.24-0ubuntu0.18.04.3

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
-- SET time_zone = "+2:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `fitmanager`
--

--
-- Table structure for table `application_user`
--

CREATE TABLE `application_user` (
	`id` int NOT NULL UNIQUE,
	`email` varchar(31) NOT NULL UNIQUE,
	`password` varchar(255) NOT NULL,
	`token` varchar(255),
	`firstName` varchar(31),
	`lastName` varchar(31),
	`roleId` int NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Table structure for table `class`
--

CREATE TABLE `class` (	
	`id` int NOT NULL UNIQUE,
	`title` varchar(255) NOT NULL,
	`limit_p` int NOT NULL,
	`date_w` date NOT NULL,
	`time_w` time NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Table structure for table `user_class`
--

CREATE TABLE `user_class` (	
	`userId` int,
	`classId` int
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Table structure for table `role`
--

CREATE TABLE `role` (	
	`id` int NOT NULL UNIQUE,
	`role_value` int NOT NULL UNIQUE,
	`name` varchar(15) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `application_user`
--
ALTER TABLE `application_user`
  ADD PRIMARY KEY `user_pkey` (`id`);
  
--
-- Indexes for table `class`
--
ALTER TABLE `class`
  ADD PRIMARY KEY `class_pkey` (`id`);
  
--
-- Indexes for table `user_class`
--
ALTER TABLE `user_class`
  ADD PRIMARY KEY `user_class_pkey` (`userId`, `classId`);
  
--
-- Indexes for table `user_class`
--
ALTER TABLE `role`
  ADD PRIMARY KEY `role_pkey` (`id`);    
  
--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `application_user`
--
ALTER TABLE `application_user`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;   
  
--
-- AUTO_INCREMENT for table `class`
--
ALTER TABLE `class`
  MODIFY `id` int NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;  

  
--
-- Constraints for dumped tables
--

--
-- Constraints for table `user_class`
--
ALTER TABLE `user_class`
  ADD CONSTRAINT `user_fkey` FOREIGN KEY (`userId`) REFERENCES `application_user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `class_fkey` FOREIGN KEY (`classId`) REFERENCES `class` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

--
-- Constraints for table `application_user`
--
ALTER TABLE `application_user`
  ADD CONSTRAINT `role_fkey` FOREIGN KEY (`roleId`) REFERENCES `role` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
  
INSERT INTO `role`(`id`, `role_value`, `name`) VALUES
(1, 2, 'admin'),
(2, 1, 'user');

INSERT INTO `application_user`(`id`, `email`, `password`, `token`, `roleId`) VALUES
(2, 'abak@wp.pl', '$2a$10$DHfQgC/HYjmMqpc/FdQUhe7.yKIY8a/SD6Mr3aJlWmVW.efFFd94q', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhYmFrQHdwLnBsIn0.8vtTluhHz6j4Z9pz2ruNeHVR4n-QutWvyw-Cuk0-DtYK_4gXLl93AunKxytbKiSq1NYgDgeXA1pr8CUVVt5aBQ', 2),
(3, 'jkowalski@wp.pl', '$2a$10$EqKapon72e5NBtJ/ivE2cu.GBlGU1KQEzwKNN0jcV534bgN6eHKPi', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJqa293YWxza2lAd3AucGwifQ.2zCEwXOlj1D4O_Hp5TE_t7PljXP_hL8pt5ehyN-OSOXKNXLOAnNdG4TrjGx_zo6AwQsuW0Msw9mquox4LqKidQ', 1),
(4, 'mnowak@wp.pl', '$2a$10$GP/u0Lq8Z7hwGIc6mohdc.wE4X84kf49QEmxt.ABy7KcRrydwB6/e', 'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtbm93YWtAd3AucGwifQ.s2vQ7QaqYlLzxNvC-xTm2rQo_LTJrl2Vyul-Stft5H5TcHUEWXHiqA0rAfsW_SFHADLeDtJ9W2ruo9UM7mDYhg', 2);

UPDATE `application_user` SET `firstName`='Anna', `lastName`='Bąk' WHERE `email`='abak@wp.pl';
UPDATE `application_user` SET `firstName`='Mateusz', `lastName`='Nowak' WHERE `email`='mnowak@wp.pl';

INSERT INTO `class`(`id`, `title`, `limit_p`, `date_w`, `time_w`) VALUES
(2, 'Beginners', 0, '2020-12-17', '20:00'),
(3, 'Inter', 12, '2020-12-17', '20:00'),
(4, 'Weightlifting', 12, '2020-12-17', '20:00'),
(5, 'Core', 8, '2020-12-17', '20:00');

INSERT INTO `user_class`(`userId`, `classId`) VALUES
(2, 5),
(3, 5),
(4, 3);

-- ------------------------------------------------------  

CREATE USER 'admin'@'%' IDENTIFIED BY 'mysecretpassword';
GRANT ALL PRIVILEGES ON *.* TO 'admin'@'%' WITH GRANT OPTION;  
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
